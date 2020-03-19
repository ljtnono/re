package cn.rootelement.service.impl;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.entity.ReBlog;
import cn.rootelement.entity.ReBlogType;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.enumeration.ReEntityRedisKeyEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.mapper.ReBlogTypeMapper;
import cn.rootelement.vo.JsonResultVO;
import cn.rootelement.service.IReBlogService;
import cn.rootelement.service.IReBlogTypeService;
import cn.rootelement.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 博客类型服务实现类
 *
 * @author ljt
 * @version 1.0.2
 * @date 2019/12/23
 */
@Service
@Slf4j
public class ReBlogTypeServiceImpl extends ServiceImpl<ReBlogTypeMapper, ReBlogType> implements IReBlogTypeService {

    private RedisUtil redisUtil;

    private IReBlogService iReBlogService;

    @Autowired
    public ReBlogTypeServiceImpl(IReBlogService iReBlogService, RedisUtil redisUtil) {
        this.iReBlogService = iReBlogService;
        this.redisUtil = redisUtil;
    }

    @Override
    public JsonResultVO listBlogTypePage(Integer page, Integer count) {
        // 首先从缓存中获取，如果缓存中没有，那么从数据库中获取
        String redisKey = ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页ReBlogType数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReBlogType> pageResult = page(new Page<>(page, count), new QueryWrapper<ReBlogType>().orderByDesc("modify_time"));
            log.info("从数据库中获取" + page + "页ReBlogType数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    /**
     * 恢复删除的博客类型
     *
     * @param id 需要恢复的博客类型id
     * @return JsonResult 对象
     */
    @Override
    public JsonResultVO restore(Serializable id) {
        int blogTypeId = Integer.parseInt(id.toString());
        // 更新状态
        boolean update = update(new UpdateWrapper<ReBlogType>().set("status", 1).eq("id", id));
        // 更新相关博客的状态
        ReBlogType reBlogType = getById(blogTypeId);
        // 查看是否有相关类型的博客
        List<ReBlog> type = iReBlogService.list(new QueryWrapper<ReBlog>().eq("type", reBlogType.getName()));
        if (null != type && type.size() > 0) {
            iReBlogService.update(new UpdateWrapper<ReBlog>().set("status", 1).eq("type", reBlogType.getName()));
        }
        if (update) {
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reBlogType), 1);
        } else {
            log.error("恢复博客类型失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 博客类型名称模糊查询
     *
     * @param name 博客类型名称
     * @param pageDTO 页码对象
     * @return JsonResult 对象
     */
    @Override
    public JsonResultVO search(final String name, PageDTO pageDTO) {
        IPage<ReBlogType> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), new QueryWrapper<ReBlogType>().like("name", name).orderByDesc("modify_time"));
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            log.error("查询博客类型失败, name = {}, pageDTO = {}", name, pageDTO);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO saveEntity(ReBlogType entity) {
        boolean save = save(entity);
        if (save) {
            // 删除所有相关缓存
            // 将实体类存储到缓存中去
            deleteCacheAll();
            setCache(entity);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            log.error("新增博客类型失败, id = {}", entity.getId());
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        int blogTypeId = Integer.parseInt(id.toString());
        // 在数据库中更新相关标签的状态
        boolean update = update(new UpdateWrapper<ReBlogType>().set("status", 0).eq("id", blogTypeId));
        ReBlogType reBlogType = getById(blogTypeId);
        // 查看是否有相关类型的博客
        List<ReBlog> type = iReBlogService.list(new QueryWrapper<ReBlog>().eq("type", reBlogType.getName()));
        if (null != type && type.size() > 0) {
            iReBlogService.update(new UpdateWrapper<ReBlog>().set("status", 0).eq("type", reBlogType.getName()));
        }
        if (update) {
            // 删除所有相关缓存
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reBlogType), 1);
        } else {
            log.error("删除博客类型失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO updateEntityById(Serializable id, ReBlogType entity) {
        int blogTypeId = Integer.parseInt(id.toString());
        boolean updateResult = update(entity, new UpdateWrapper<ReBlogType>().eq("id", blogTypeId));
        if (updateResult) {
            // 删除所有的缓存
            deleteCacheAll();
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            log.error("更新博客类型失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO getEntityById(Serializable id) {
        int blogTypeId = Integer.parseInt(id.toString());
        JsonResultVO jsonResultVO;
        // 如果缓存中存在，那么首先从缓存中获取
        String key = ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY.getKey()
                .replace(":id", ":" + blogTypeId)
                .replace(":name", ":*");
        boolean b = redisUtil.hasKeyByPattern(key);
        // 如果存在，那么直接获取
        ReBlogType reBlogType;
        if (b) {
            reBlogType = (ReBlogType) redisUtil.getByPattern(key);
            if (reBlogType == null || reBlogType.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
        } else {
            reBlogType = getById(blogTypeId);
            // 如果不存在，那么返回 找不到资源错误
            if (reBlogType == null || reBlogType.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
            setCache(reBlogType);
        }
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reBlogType), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReBlogType> reBlogTypeList = list(new QueryWrapper<ReBlogType>().orderByDesc("modify_time"));
        reBlogTypeList.forEach(this::setCache);
        JsonResultVO success = JsonResultVO.success(reBlogTypeList, reBlogTypeList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public void deleteCacheAll() {
        // 删除所有的缓存
        redisUtil.deleteByPattern("re_blog_type:*");
        redisUtil.deleteByPattern("re_blog_type_page:*");
        redisUtil.deleteByPattern("re_blog_type_page_total:*");
        // 删除所有的blog缓存相关
        redisUtil.deleteByPattern("re_blog:*");
        redisUtil.deleteByPattern("re_blog_page:*");
        redisUtil.deleteByPattern("re_blog_page_total:*");
        redisUtil.deleteByPattern("re_blog_page_type:*");
    }

    @Override
    public void setCache(ReBlogType value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY.getKey()
                .replace(":id", ":" + value.getId())
                .replace(":name", ":" + value.getName()), value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void setCache(List<ReBlogType> cache) {
        cache.forEach(this::setCache);
    }
}
