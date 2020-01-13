package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.entity.ReBlogType;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReBlogTypeMapper;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.service.IReBlogTypeService;
import cn.ljtnono.re.util.RedisUtil;
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
import java.util.Optional;

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

    private final RedisUtil redisUtil;

    private final IReBlogService iReBlogService;

    @Autowired
    public ReBlogTypeServiceImpl(IReBlogService iReBlogService, RedisUtil redisUtil) {
        this.iReBlogService = iReBlogService;
        this.redisUtil = redisUtil;
    }


    @Override
    public JsonResult listBlogTypeAll() {
        // 首先从缓存中获取，如果缓存中没有的话从数据库获取
        String redisKey = ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY.getKey()
                .replace(":id", ":*")
                .replace(":name", ":*");
        List<ReBlogType> getByPattern = (List<ReBlogType>) redisUtil.getByPattern(redisKey);
        Optional<List<ReBlogType>> optionalGetByPattern = Optional.ofNullable(getByPattern);
        optionalGetByPattern.ifPresent(l -> log.info("从缓存中获取所有博客类型列表，总条数：" + l.size()));
        List<ReBlogType> reBlogTypeList = optionalGetByPattern.orElseGet(() -> {
            List<ReBlogType> list = list();
            list.forEach(reBlogType -> {
                redisUtil.set(ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY.getKey()
                        .replace(":id", ":" + reBlogType.getId())
                        .replace(":name", ":" + reBlogType.getName()), reBlogType, RedisUtil.EXPIRE_TIME_DEFAULT);
            });
            log.info("从数据库中获取所有博客类型列表，总条数：" + list.size());
            return list;
        });
        return JsonResult.success(reBlogTypeList, reBlogTypeList.size());
    }

    @Override
    public JsonResult listBlogTypePage(Integer page, Integer count) {
        Optional<Integer> optionalPage = Optional.ofNullable(page);
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        optionalPage.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalCount.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalPage.filter(p -> p >= 0 && p <= 1000)
                .orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR));
        optionalCount.filter(c -> c >= 0 && c <= 60)
                .orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR));
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
            return JsonResult.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReBlogType> pageResult = page(new Page<>(page, count), new QueryWrapper<ReBlogType>().orderByDesc("modify_time"));
            log.info("获取" + page + "页ReBlogType数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    /**
     * 恢复删除的博客类型
     *
     * @param id 需要恢复的博客类型id
     * @return JsonResult 对象
     */
    @Override
    public JsonResult restore(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int blogTypeId = Integer.parseInt(id.toString());
        if (blogTypeId >= 1001) {
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
                // 删除所有相关缓存
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY
                        .getKey().replace(":id", ":*")
                        .replace(":name", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_TOTAL_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                // 删除所有的blog缓存相关
                redisUtil.deleteByPattern("re_blog:*");
                redisUtil.deleteByPattern("re_blog_page:*");
                redisUtil.deleteByPattern("re_blog_page_total:*");
                redisUtil.deleteByPattern("re_blog_page_type:*");
                return JsonResult.success(Collections.singletonList(reBlogType), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
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
    public JsonResult search(final String name, PageDTO pageDTO) {
        Optional<String> optionalName = Optional.ofNullable(name);
        optionalName.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_ERROR));
        IPage<ReBlogType> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), new QueryWrapper<ReBlogType>().like("name", name));
        if (pageResult != null) {
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResult saveEntity(ReBlogType entity) {
        Optional<ReBlogType> optionalReBlogType = Optional.ofNullable(entity);
        optionalReBlogType.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        if (save) {
            // 删除所有相关缓存
            redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY
                    .getKey().replace(":id", ":*")
                    .replace(":name", ":*"));
            redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_KEY
                    .getKey().replace(":page", ":*")
                    .replace(":count", ":*"));
            redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_TOTAL_KEY
                    .getKey().replace(":page", ":*")
                    .replace(":count", ":*"));
            // 删除所有的blog缓存相关
            redisUtil.deleteByPattern("re_blog:*");
            redisUtil.deleteByPattern("re_blog_page:*");
            redisUtil.deleteByPattern("re_blog_page_total:*");
            redisUtil.deleteByPattern("re_blog_page_type:*");
            return JsonResult.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResult deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int blogTypeId = Integer.parseInt(id.toString());
        if (blogTypeId >= 1) {
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
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY
                        .getKey().replace(":id", ":*")
                        .replace(":name", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_TOTAL_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                // 删除所有的blog缓存相关
                redisUtil.deleteByPattern("re_blog:*");
                redisUtil.deleteByPattern("re_blog_page:*");
                redisUtil.deleteByPattern("re_blog_page_total:*");
                redisUtil.deleteByPattern("re_blog_page_type:*");
                return JsonResult.success(Collections.singletonList(reBlogType), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult updateEntityById(Serializable id, ReBlogType entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReBlogType> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int blogTypeId = Integer.parseInt(id.toString());
        if (blogTypeId >= 1) {
            boolean updateResult = update(entity, new UpdateWrapper<ReBlogType>().eq("id", blogTypeId));
            if (updateResult) {
                // 删除所有的缓存
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY
                        .getKey().replace(":id", ":*")
                        .replace(":name", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_BLOG_TYPE_PAGE_TOTAL_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                // 删除所有的blog缓存相关
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
        int blogTypeId = Integer.parseInt(id.toString());
        if (blogTypeId >= 1) {
            JsonResult jsonResult;
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
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                jsonResult = JsonResult.success(Collections.singletonList(reBlogType), 1);
            } else {
                reBlogType = getById(blogTypeId);
                // 如果不存在，那么返回 找不到资源错误
                if (reBlogType == null || reBlogType.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY.getKey()
                        .replace(":id", ":" + reBlogType.getId())
                        .replace(":name", ":" + reBlogType.getName()), reBlogType, RedisUtil.EXPIRE_TIME_DEFAULT);
                jsonResult = JsonResult.success(Collections.singletonList(reBlogType), 1);
            }
            jsonResult.setMessage("操作成功");
            return jsonResult;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult listEntityAll() {
        List<ReBlogType> reBlogTypeList = list();
        Optional<List<ReBlogType>> optionalList = Optional.ofNullable(reBlogTypeList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reBlogType -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_BLOG_TYPE_KEY.getKey()
                    .replace(":id", ":" + reBlogType.getId())
                    .replace(":name", ":" + reBlogType.getName()), reBlogType, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有博客类型列表，总条数：" + l.size()));
        JsonResult success = JsonResult.success(reBlogTypeList, reBlogTypeList.size());
        success.setMessage("操作成功");
        return success;
    }
}
