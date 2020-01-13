package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.entity.ReBlogType;
import cn.ljtnono.re.entity.ReLink;
import cn.ljtnono.re.entity.ReLinkType;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReLinkTypeMapper;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReLinkService;
import cn.ljtnono.re.service.IReLinkTypeService;
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
 * 链接类型服务实现类
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@Service
@Slf4j
public class ReLinkTypeServiceImpl extends ServiceImpl<ReLinkTypeMapper, ReLinkType> implements IReLinkTypeService {

    private final RedisUtil redisUtil;

    private final IReLinkService iReLinkService;

    @Autowired
    public ReLinkTypeServiceImpl(RedisUtil redisUtil, IReLinkService iReLinkService) {
        this.redisUtil = redisUtil;
        this.iReLinkService = iReLinkService;
    }

    @Override
    public JsonResult saveEntity(ReLinkType entity) {
        Optional<ReLinkType> optionalReBook = Optional.ofNullable(entity);
        optionalReBook.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":name", ":" + entity.getName());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            return JsonResult.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResult deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int linkTypeId = Integer.parseInt(id.toString());
        if (linkTypeId >= 1001) {
            // 在数据库中更新
            boolean result = update(new UpdateWrapper<ReLinkType>().set("status", 0).eq("id", linkTypeId));
            ReLinkType reLinkType = getById(id);
            // 查看是否有相关类型的博客
            List<ReLink> type = iReLinkService.list(new QueryWrapper<ReLink>().eq("type", reLinkType.getName()));
            if (null != type && type.size() > 0) {
                iReLinkService.update(new UpdateWrapper<ReLink>().set("status", 0).eq("type", reLinkType.getName()));
            }
            if (result) {
                // 删除缓存中的相关数据
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY
                        .getKey().replace(":id", ":*")
                        .replace(":name", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_LINK_TYPE_PAGE_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_LINK_TYPE_PAGE_TOTAL_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                // 删除所有的link缓存相关
                redisUtil.deleteByPattern("re_link:*");
                redisUtil.deleteByPattern("re_link_page:*");
                redisUtil.deleteByPattern("re_link_page_total:*");
                return JsonResult.success(Collections.singletonList(reLinkType), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult updateEntityById(Serializable id, ReLinkType entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReLinkType> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int linkTypeId = Integer.parseInt(id.toString());
        if (linkTypeId >= 1001) {
            boolean updateResult = update(new UpdateWrapper<ReLinkType>().setEntity(entity).eq("id", linkTypeId));
            if (updateResult) {
                // 更新操作
                String key = ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY.getKey()
                        .replace(":id", ":" + entity.getId())
                        .replace(":name", ":" + entity.getName());
                boolean b = redisUtil.hasKey(key);
                if (b) {
                    redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
                }
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
        int linkTypeId = Integer.parseInt(id.toString());
        if (linkTypeId >= 1001) {
            JsonResult jsonResult;
            // 如果缓存中存在，那么首先从缓存中获取
            String key = ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY.getKey()
                    .replace(":id", ":" + linkTypeId)
                    .replace(":name", ":*");
            boolean b = redisUtil.hasKeyByPattern(key);
            // 如果存在，那么直接获取
            ReLinkType reLinkType;
            if (b) {
                reLinkType = (ReLinkType) redisUtil.getByPattern(key);
                if (reLinkType == null || reLinkType.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
            } else {
                reLinkType = getById(linkTypeId);
                // 如果不存在，那么返回 找不到资源错误
                if (reLinkType == null || reLinkType.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY.getKey()
                        .replace(":id", ":" + reLinkType.getId())
                        .replace(":name", ":" + reLinkType.getName()), reLinkType, RedisUtil.EXPIRE_TIME_DEFAULT);
            }
            jsonResult = JsonResult.success(Collections.singletonList(reLinkType), 1);
            jsonResult.setMessage("操作成功");
            return jsonResult;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult listEntityAll() {
        List<ReLinkType> reLinkTypeList = list();
        Optional<List<ReLinkType>> optionalList = Optional.ofNullable(reLinkTypeList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reLinkType -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY.getKey()
                    .replace(":id", ":" + reLinkType.getId())
                    .replace(":name", ":" + reLinkType.getName()), reLinkType, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有链接类型列表，总条数：" + l.size()));
        JsonResult success = JsonResult.success(reLinkTypeList, reLinkTypeList.size());
        success.setMessage("操作成功");
        return success;
    }


    @Override
    public JsonResult listLinkTypePage(Integer page, Integer count) {
        Optional<Integer> optionalPage = Optional.ofNullable(page);
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        optionalPage.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalCount.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        String redisKey = ReEntityRedisKeyEnum.RE_LINK_TYPE_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_LINK_TYPE_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页链接类型数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResult.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReLinkType> pageResult = page(new Page<>(page, count), new QueryWrapper<ReLinkType>().orderByDesc("modify_time"));
            log.info("获取" + page + "页链接类型数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }


    @Override
    public JsonResult restore(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int linkTypeId = Integer.parseInt(id.toString());
        if (linkTypeId >= 1001) {
            // 更新状态
            boolean update = update(new UpdateWrapper<ReLinkType>().set("status", 1).eq("id", linkTypeId));
            ReLinkType reLinkType = getById(id);
            List<ReLink> type = iReLinkService.list(new QueryWrapper<ReLink>().eq("type", reLinkType.getName()));
            if (null != type && type.size() > 0) {
                iReLinkService.update(new UpdateWrapper<ReLink>().set("status", 1).eq("type", reLinkType.getName()));
            }
            if (update) {
                // 删除缓存中的相关数据
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY
                        .getKey().replace(":id", ":*")
                        .replace(":name", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_LINK_TYPE_PAGE_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                redisUtil.deleteByPattern(ReEntityRedisKeyEnum.RE_LINK_TYPE_PAGE_TOTAL_KEY
                        .getKey().replace(":page", ":*")
                        .replace(":count", ":*"));
                // 删除所有的link缓存相关
                redisUtil.deleteByPattern("re_link:*");
                redisUtil.deleteByPattern("re_link_page:*");
                redisUtil.deleteByPattern("re_link_page_total:*");
                return JsonResult.success(Collections.singletonList(reLinkType), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    /**
     * 链接类型名称模糊查询
     *
     * @param name    链接类型名称
     * @param pageDTO 页码对象
     * @return JsonResult 对象
     */
    @Override
    public JsonResult search(final String name, PageDTO pageDTO) {
        Optional<String> optionalName = Optional.ofNullable(name);
        optionalName.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_ERROR));
        IPage<ReLinkType> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), new QueryWrapper<ReLinkType>().like("name", name));
        if (pageResult != null) {
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }
}
