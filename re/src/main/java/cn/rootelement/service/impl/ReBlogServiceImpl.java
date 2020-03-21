package cn.rootelement.service.impl;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReBlogSearchDTO;
import cn.rootelement.entity.ReBlog;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.enumeration.ReEntityRedisKeyEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.mapper.ReBlogMapper;
import cn.rootelement.vo.JsonResultVO;
import cn.rootelement.service.IReBlogService;
import cn.rootelement.service.common.IReEntityService;
import cn.rootelement.util.RedisUtil;
import cn.rootelement.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * 博客服务实现类
 *
 * @author ljt
 * @version 1.0.3
 * @date 2020/1/11
 */
@Service
@Slf4j
public class ReBlogServiceImpl extends ServiceImpl<ReBlogMapper, ReBlog> implements IReEntityService<ReBlog>, IReBlogService {

    private RedisUtil redisUtil;

    @Autowired
    public ReBlogServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public JsonResultVO listGuessYouLike() {
        // 根据modify, view, comment降序获取前6条记录
        List<ReBlog> list = list(new QueryWrapper<ReBlog>().eq("status", 1).orderByDesc("modify_time", "view", "comment").last("LIMIT 6"));
        return JsonResultVO.success(list, list.size());
    }

    @Override
    public JsonResultVO listHotArticles() {
        List<ReBlog> list = list(new QueryWrapper<ReBlog>().eq("status", 1).orderByDesc("modify_time", "view", "comment").last("LIMIT 6"));
        return JsonResultVO.success(list, list.size());
    }

    @Override
    public Integer countView() {
        return baseMapper.countView();
    }

    @Override
    public Integer countComment() {
        return baseMapper.countComment();
    }

    @Override
    public JsonResultVO listBlogPageByType(Integer page, Integer count, final String type) {
        String redisKey = ReEntityRedisKeyEnum.RE_BLOG_PAGE_TYPE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_BLOG_PAGE_TYPE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        if (!StringUtil.isEmpty(type)) {
            redisKey = redisKey.replace(":type", ":" + type);
            totalRedisKey = totalRedisKey.replace(":type", ":" + type);
        } else {
            redisKey = redisKey.replace(":type", ":ALL");
            totalRedisKey = totalRedisKey.replace(":type", ":ALL");
        }
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        // TODO 这里可以尝试先测试key是否存在
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页博客数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            QueryWrapper<ReBlog> reBlogQueryWrapper;
            if (!StringUtil.isEmpty(type)) {
                reBlogQueryWrapper = new QueryWrapper<ReBlog>().eq("type", type).orderByDesc("modify_time", "view", "comment");
            } else {
                reBlogQueryWrapper = new QueryWrapper<ReBlog>().orderByDesc("modify_time", "view", "comment");
            }
            IPage<ReBlog> pageResult = page(new Page<>(page, count), reBlogQueryWrapper);
            log.info("从数据库中获取" + page + "页博客数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }


    @Override
    public JsonResultVO search(ReBlogSearchDTO reBlogSearchDTO, PageDTO pageDTO) {
        QueryWrapper<ReBlog> reBlogQueryWrapper = new QueryWrapper<>();
        reBlogQueryWrapper.orderByDesc("modify_time", "view", "comment");
        if (!StringUtil.isEmpty(reBlogSearchDTO.getTitle())) {
            reBlogQueryWrapper.like("title", reBlogSearchDTO.getTitle());
        }
        if (!StringUtil.isEmpty(reBlogSearchDTO.getAuthor())) {
            reBlogQueryWrapper.like("author", reBlogSearchDTO.getAuthor());
        }
        if (!StringUtil.isEmpty(reBlogSearchDTO.getType())) {
            reBlogQueryWrapper.eq("type", reBlogSearchDTO.getType());
        }
        IPage<ReBlog> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reBlogQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            log.error("分页查询错误 {} {}", reBlogSearchDTO, pageDTO);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO restore(Serializable id) {
        int blogId = Integer.parseInt(id.toString());
        boolean restoreResult = update(new UpdateWrapper<ReBlog>().set("status", 1).eq("id", blogId));
        if (restoreResult) {
            // 查出来
            ReBlog byId = getById(blogId);
            // 删除所有缓存
            deleteCacheAll();
            // 返回删除的博客的整体
            return JsonResultVO.success(Collections.singletonList(byId), 1);
        } else {
            log.error("恢复博客失败, id = {}", blogId);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO listBlogPage(Integer page, Integer count) {
        String redisKey = ReEntityRedisKeyEnum.RE_BLOG_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_BLOG_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (objects != null && !objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页博客数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReBlog> pageResult = page(new Page<>(page, count), new QueryWrapper<ReBlog>().orderByDesc("modify_time", "view", "comment"));
            log.info("从数据库中获取" + page + "页博客数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    @Override
    public JsonResultVO saveEntity(ReBlog entity) {
        boolean save = save(entity);
        if (save) {
            // 删除所有的blog缓存相关
            deleteCacheAll();
            // 将实体类存储到缓存中去
            setCache(entity);
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            log.error("新增博客失败, id = {}", entity.getId());
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        int blogId = Integer.parseInt(id.toString());
        boolean deleteResult = update(new UpdateWrapper<ReBlog>().set("status", 0).eq("id", blogId));
        if (deleteResult) {
            // 查出来
            ReBlog byId = getById(blogId);
            // 删除所有缓存
            deleteCacheAll();
            // 返回删除的博客的整体
            return JsonResultVO.success(Collections.singletonList(byId), 1);
        } else {
            log.error("删除博客失败, id = {}", blogId);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO updateEntityById(Serializable id, ReBlog entity) {
        int blogId = Integer.parseInt(id.toString());
        boolean updateResult = update(entity, new UpdateWrapper<ReBlog>().eq("id", blogId));
        if (updateResult) {
            // 删除所有缓存
            deleteCacheAll();
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            log.error("更新除博客失败, id = {}", entity.getId());
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO getEntityById(Serializable id) {
        int blogId = Integer.parseInt(id.toString());
        JsonResultVO jsonResultVO;
        // TODO 看看是否可以改进
        // 如果缓存中存在，那么首先从缓存中获取
        String key = ReEntityRedisKeyEnum.RE_BLOG_KEY.getKey()
                .replace(":id", ":" + blogId)
                .replace(":author", ":*")
                .replace(":title", ":*")
                .replace(":type", ":*");
        boolean b = redisUtil.hasKeyByPattern(key);
        // 如果存在，那么直接获取
        ReBlog reBlog;
        if (b) {
            reBlog = (ReBlog) redisUtil.getByPattern(key);
            if (reBlog == null || reBlog.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
        } else {
            reBlog = getById(blogId);
            // 如果不存在，那么返回 找不到资源错误
            if (reBlog == null || reBlog.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
            setCache(reBlog);
        }
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reBlog), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }

    @Override
    public JsonResultVO listEntityAll() {
        // 直接从数据库中获取所有 这里mybatis-plus 会返回空集合
        List<ReBlog> blogList = list(new QueryWrapper<ReBlog>().orderByDesc("modify_time", "view", "comment"));
        log.info("从数据库中获取所有博客列表，总条数：" + blogList.size());
        // 将数据写入缓存中
        if (blogList.size() != 0) {
            setCache(blogList);
        }
        JsonResultVO success = JsonResultVO.success(blogList, blogList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public void setCache(List<ReBlog> cache) {
        cache.forEach(this::setCache);
    }

    @Override
    public void setCache(ReBlog value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_BLOG_KEY.getKey()
                .replace(":id", ":" + value.getId())
                .replace(":author", ":" + value.getAuthor())
                .replace(":title", ":" + value.getTitle())
                .replace(":type", ":" + value.getType()), value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void deleteCacheAll() {
        redisUtil.deleteByPattern("re_blog:*");
        redisUtil.deleteByPattern("re_blog_page:*");
        redisUtil.deleteByPattern("re_blog_page_total:*");
        redisUtil.deleteByPattern("re_blog_page_type:*");
    }
}
