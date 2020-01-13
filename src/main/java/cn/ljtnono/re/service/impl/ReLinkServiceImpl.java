package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReLinkSearchDTO;
import cn.ljtnono.re.entity.ReLink;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReLinkMapper;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReLinkService;
import cn.ljtnono.re.util.RedisUtil;
import cn.ljtnono.re.util.StringUtil;
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
 * 链接服务实现类
 *
 * @author ljt
 * @version 1.0
 * @date 2019/11/23
 */
@Service
@Slf4j
public class ReLinkServiceImpl extends ServiceImpl<ReLinkMapper, ReLink> implements IReLinkService {

    private final RedisUtil redisUtil;

    @Autowired
    public ReLinkServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 获取所有外部链接数据
     *
     * @return 所有外部链接数据
     */
    @Override
    public List<ReLink> listOutLinkAll() {
        // 首先从缓存获取
        List<ReLink> objects = (List<ReLink>) redisUtil.getByPattern("re_link*");
        if (objects == null || objects.size() == 0) {
            // 从数据库获取
            List<ReLink> list = list(new QueryWrapper<ReLink>().eq("type", "外部链接"));
            list.forEach(reLink -> {
                redisUtil.set(ReEntityRedisKeyEnum.RE_LINK_KEY.getKey()
                        .replace("id", reLink.getId() + "")
                        .replace("name", reLink.getName())
                        .replace("type", reLink.getType()), reLink, RedisUtil.EXPIRE_TIME_DEFAULT);
            });
            return list;
        }
        log.info("从缓存中加载全部外链数据");
        return objects;
    }

    /**
     * 分页获取链接列表
     *
     * @param page  页数
     * @param count 每页获取的条数
     * @return JsonResult对象
     */
    @Override
    public JsonResult listLinkPage(Integer page, Integer count) {
        Optional<Integer> optionalPage = Optional.ofNullable(page);
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        optionalPage.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalCount.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        // 首先从缓存中获取
        String redisKey = ReEntityRedisKeyEnum.RE_LINK_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_LINK_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页链接数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResult.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReLink> pageResult = page(new Page<>(page, count), new QueryWrapper<ReLink>().orderByDesc("modify_time"));
            log.info("获取" + page + "页链接数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    /**
     * 恢复删除的链接
     *
     * @param id 需要恢复的链接id
     * @return JsonResult 对象
     */
    @Override
    public JsonResult restore(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int linkId = Integer.parseInt(id.toString());
        if (linkId >= 1001) {
            // 更新状态
            boolean update = update(new UpdateWrapper<ReLink>().set("status", 1).eq("id", linkId));
            if (update) {
                // 删除缓存中的相关数据
                ReLink reLink = getById(linkId);
                redisUtil.deleteByPattern("re_link:*");
                redisUtil.deleteByPattern("re_link_page:*");
                redisUtil.deleteByPattern("re_link_page_total:*");
                return JsonResult.success(Collections.singletonList(reLink), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    /**
     * 链接分页条件查询
     *
     * @param reLinkSearchDTO 条件查询条件DTO
     * @param pageDTO         分页对象
     * @return JsonResult 对象
     */
    @Override
    public JsonResult search(ReLinkSearchDTO reLinkSearchDTO, PageDTO pageDTO) {
        Optional<ReLinkSearchDTO> optionalReLinkSearchDTO = Optional.ofNullable(reLinkSearchDTO);
        optionalReLinkSearchDTO.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_ERROR));
        QueryWrapper<ReLink> reLinkQueryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(reLinkSearchDTO.getName())) {
            reLinkQueryWrapper.like("name", reLinkSearchDTO.getName());
        }
        if (!StringUtil.isEmpty(reLinkSearchDTO.getUrl())) {
            reLinkQueryWrapper.like("url", reLinkSearchDTO.getUrl());
        }
        if (!StringUtil.isEmpty(reLinkSearchDTO.getType())) {
            reLinkQueryWrapper.eq("type", reLinkSearchDTO.getType());
        }
        IPage<ReLink> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reLinkQueryWrapper);
        if (pageResult != null) {
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }


    @Override
    public JsonResult saveEntity(ReLink entity) {
        Optional<ReLink> optionalReBook = Optional.ofNullable(entity);
        optionalReBook.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_LINK_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":name", ":" + entity.getName())
                .replace(":type", ":" + entity.getType());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            redisUtil.deleteByPattern("re_link_page:*");
            redisUtil.deleteByPattern("re_link_page_total:*");
            return JsonResult.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }


    @Override
    public JsonResult deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int linkId = Integer.parseInt(id.toString());
        if (linkId >= 1001) {
            // 在数据库中更新
            boolean updateResult = update(new UpdateWrapper<ReLink>().set("status", 0).eq("id", linkId));
            if (updateResult) {
                // 删除缓存中的相关数据
                ReLink reLink = getById(linkId);
                redisUtil.deleteByPattern("re_link:*");
                redisUtil.deleteByPattern("re_link_page:*");
                redisUtil.deleteByPattern("re_link_page_total:*");
                return JsonResult.success(Collections.singletonList(reLink), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }


    @Override
    public JsonResult updateEntityById(Serializable id, ReLink entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReLink> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int linkId = Integer.parseInt(id.toString());
        if (linkId >= 1001) {
            boolean updateResult = update(entity, new UpdateWrapper<ReLink>().eq("id", linkId));
            if (updateResult) {
                // 如果缓存中有的话，那么淘汰缓存
                // 删除所有缓存
                redisUtil.deleteByPattern("re_link:*");
                redisUtil.deleteByPattern("re_link_page:*");
                redisUtil.deleteByPattern("re_link_page_total:*");
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
        int linkId = Integer.parseInt(id.toString());
        if (linkId >= 1001) {
            JsonResult jsonResult;
            // 如果缓存中存在，那么首先从缓存中获取
            String key = ReEntityRedisKeyEnum.RE_LINK_KEY.getKey()
                    .replace(":id", ":" + linkId)
                    .replace(":name", ":*")
                    .replace(":type", ":*");
            boolean b = redisUtil.hasKeyByPattern(key);
            // 如果存在，那么直接获取
            ReLink reLink;
            if (b) {
                reLink = (ReLink) redisUtil.getByPattern(key);
                if (reLink == null || reLink.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
            } else {
                reLink = getById(linkId);
                // 如果不存在，那么返回 找不到资源错误
                if (reLink == null || reLink.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_LINK_KEY.getKey()
                        .replace(":id", ":" + reLink.getId())
                        .replace(":name", ":" + reLink.getName())
                        .replace(":type", ":" + reLink.getType()), reLink, RedisUtil.EXPIRE_TIME_DEFAULT);
            }
            jsonResult = JsonResult.success(Collections.singletonList(reLink), 1);
            jsonResult.setMessage("操作成功");
            return jsonResult;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult listEntityAll() {
        List<ReLink> reLinkList = list();
        Optional<List<ReLink>> optionalList = Optional.ofNullable(reLinkList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reLink -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                    .replace(":id", ":" + reLink.getId())
                    .replace(":name", ":" + reLink.getName())
                    .replace(":type", ":" + reLink.getType()), reLink, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有链接列表，总条数：" + l.size()));
        JsonResult success = JsonResult.success(reLinkList, reLinkList.size());
        success.setMessage("操作成功");
        return success;
    }
}
