package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReBlogMapper;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * 博客服务实现类
 *
 * @author ljt
 * @version 1.0.2
 * @date 2019/11/16
 */
@Service
public class ReBlogServiceImpl extends ServiceImpl<ReBlogMapper, ReBlog> implements IReBlogService {

    private static Logger logger = LoggerFactory.getLogger(ReBlogServiceImpl.class);

    private final RedisUtil redisUtil;

    @Autowired
    public ReBlogServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 获取首页猜你喜欢
     *
     * @return 首页猜你喜欢博客数据
     */
    @Override
    public List<ReBlog> listGuessYouLike() {
        // 根据modify降序获取前6条记录
        return list(new QueryWrapper<ReBlog>().orderByDesc("modify_time", "view").last("LIMIT 6"));
    }

    /**
     * @param page  页码
     * @param count 每页显示的条数
     * @param type  类型
     * @return JsonResult
     */
    @Override
    public JsonResult listBlogPageByType(Integer page, Integer count, final String type) {
        return null;
    }

    /**
     * 获取所有博客的浏览量总数
     *
     * @return 所有博客的浏览量总数
     */
    @Override
    public Integer countView() {
        return baseMapper.countView();
    }

    /**
     * 获获取所有博客的评论总数
     *
     * @return 获取所有博客的评论总数
     */
    @Override
    public Integer countComment() {
        return baseMapper.countComment();
    }

    /**
     * 无条件分页查询博客信息
     *
     * @param page  页数
     * @param count 每页条数
     * @return 返回分页数据
     */
    @Override
    public JsonResult listBlogPage(Integer page, Integer count) {
        Optional<Integer> optionalPage = Optional.ofNullable(page);
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        optionalPage.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalCount.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalPage.filter(p -> p >= 0 && p <= 1000)
                .orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR));
        optionalCount.filter(c -> c >= 0 && c <= 60)
                .orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR));
        String redisKey = ReEntityRedisKeyEnum.RE_BLOG_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_BLOG_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            logger.info("从缓存中获取" + page + "页博客数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResult.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReBlog> pageResult = page(new Page<>(page, count), new QueryWrapper<ReBlog>().orderByDesc("modify_time"));
            logger.info("获取" + page + "页博客数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    @Override
    public JsonResult saveEntity(ReBlog entity) {
        Optional<ReBlog> optionalReBlog = Optional.ofNullable(entity);
        optionalReBlog.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        if (save) {
            // 删除所有的blog缓存相关
            redisUtil.deleteByPattern("re_blog:*");
            redisUtil.deleteByPattern("re_blog_page:*");
            redisUtil.deleteByPattern("re_blog_page_total:*");
            redisUtil.deleteByPattern("re_blog_page_type:*");
            // 将实体类存储到缓存中去
            redisUtil.set(ReEntityRedisKeyEnum.RE_BLOG_KEY.getKey()
                    .replace(":id", ":" + entity.getId())
                    .replace(":author", ":" + entity.getAuthor())
                    .replace(":title", ":" + entity.getTitle())
                    .replace(":type", ":" + entity.getType()), entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            return JsonResult.successForMessage("操作成功", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResult deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        Integer blogId = Integer.parseInt(id.toString());
        if (blogId >= 10001) {
            boolean deleteResult = update(new UpdateWrapper<ReBlog>().set("status", 0).eq("id", blogId));
            if (deleteResult) {
                // 查出来
                ReBlog byId = getById(blogId);
                // 如果缓存中存在，那么删除缓存
                // 删除所有缓存
                redisUtil.deleteByPattern("re_blog:*");
                redisUtil.deleteByPattern("re_blog_page:*");
                redisUtil.deleteByPattern("re_blog_page_total:*");
                redisUtil.deleteByPattern("re_blog_page_type:*");
                return JsonResult.success(Collections.singletonList(byId), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult updateEntityById(Serializable id, ReBlog entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReBlog> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        Integer blogId = Integer.parseInt(id.toString());
        if (blogId >= 10001) {
            boolean updateResult = update(entity, new UpdateWrapper<ReBlog>().eq("id", blogId));
            if (updateResult) {
                // 如果缓存中有的话，那么淘汰缓存
                // 删除所有缓存
                redisUtil.deleteByPattern("re_blog:*");
                redisUtil.deleteByPattern("re_blog_page:*");
                redisUtil.deleteByPattern("re_blog_page_total:*");
                redisUtil.deleteByPattern("re_blog_page_type:*");
                return JsonResult.successForMessage("操作成功", 200);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }


    @Override
    public JsonResult getEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        Integer blogId = Integer.parseInt(id.toString());
        if (blogId >= 10001) {
            JsonResult jsonResult;
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
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                jsonResult = JsonResult.success(Collections.singletonList(reBlog), 1);
            } else {
                reBlog = getById(blogId);
                // 如果不存在，那么返回 找不到资源错误
                if (reBlog == null || reBlog.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_BLOG_KEY.getKey()
                        .replace(":id", ":" + reBlog.getId())
                        .replace(":author", ":" + reBlog.getAuthor())
                        .replace(":title", ":" + reBlog.getTitle())
                        .replace(":type", ":" + reBlog.getType()), reBlog, RedisUtil.EXPIRE_TIME_DEFAULT);
                jsonResult = JsonResult.success(Collections.singletonList(reBlog), 1);
            }
            jsonResult.setMessage("操作成功");
            return jsonResult;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult listEntityAll() {
        // 直接从数据库中获取所有 这里mybatis-plus 会返回空集合
        List<ReBlog> blogList = list(new QueryWrapper<ReBlog>().orderByDesc("view", "modify_time").last("LIMIT 6"));
        // 将数据写入缓存中
        Optional<List<ReBlog>> optionalList = Optional.ofNullable(blogList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent(l -> l.forEach(reBlog -> redisUtil.set(ReEntityRedisKeyEnum.RE_BLOG_KEY.getKey()
                .replace(":id", ":" + reBlog.getId())
                .replace(":author", ":" + reBlog.getAuthor())
                .replace(":title", ":" + reBlog.getTitle())
                .replace(":type", ":" + reBlog.getType()), reBlog, RedisUtil.EXPIRE_TIME_DEFAULT)));
        optionalList.ifPresent(l -> logger.info("从数据库中获取所有博客列表，总条数：" + l.size()));
        JsonResult success = JsonResult.success(blogList, blogList.size());
        success.setMessage("操作成功");
        return success;
    }
}
