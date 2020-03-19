package cn.rootelement.service.impl;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReLinkSearchDTO;
import cn.rootelement.entity.ReLink;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.enumeration.ReEntityRedisKeyEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.mapper.ReLinkMapper;
import cn.rootelement.vo.JsonResultVO;
import cn.rootelement.service.IReLinkService;
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
 * 链接服务实现类
 *
 * @author ljt
 * @version 1.0.1
 * @date 2019/11/23
 */
@Service
@Slf4j
public class ReLinkServiceImpl extends ServiceImpl<ReLinkMapper, ReLink> implements IReLinkService {

    private RedisUtil redisUtil;

    @Autowired
    public ReLinkServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public List<ReLink> listOutLinkAll() {
        // 首先从缓存获取
        Object byPattern = redisUtil.getByPattern("re_link:*");
        List<ReLink> reLinks;
        if (byPattern == null) {
            // 从数据库获取
            List<ReLink> list = list(new QueryWrapper<ReLink>().eq("type", "外部链接").orderByDesc("modify_time"));
            setCache(list);
            reLinks = list;
        } else {
            reLinks = (List<ReLink>) byPattern;
            log.info("从缓存中加载全部外链数据");
        }
        return reLinks;
    }

    @Override
    public JsonResultVO listLinkPage(Integer page, Integer count) {
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
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReLink> pageResult = page(new Page<>(page, count), new QueryWrapper<ReLink>().orderByDesc("modify_time"));
            log.info("从数据库获取" + page + "页链接数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    @Override
    public JsonResultVO restore(Serializable id) {
        int linkId = Integer.parseInt(id.toString());
        // 更新状态
        boolean update = update(new UpdateWrapper<ReLink>().set("status", 1).eq("id", linkId));
        if (update) {
            // 删除缓存中的相关数据
            ReLink reLink = getById(linkId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reLink), 1);
        } else {
            log.error("恢复链接失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO search(ReLinkSearchDTO reLinkSearchDTO, PageDTO pageDTO) {
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
        reLinkQueryWrapper.orderByDesc("modify_time");
        IPage<ReLink> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reLinkQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            log.error("查询链接失败, reLinkSearchDTO = {}, pageDTO = {}", reLinkSearchDTO, pageDTO);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO saveEntity(ReLink entity) {
        boolean save = save(entity);
        if (save) {
            deleteCacheAll();
            setCache(entity);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            log.error("新增链接失败, id = {}", entity.getId());
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        int linkId = Integer.parseInt(id.toString());
        // 在数据库中更新
        boolean updateResult = update(new UpdateWrapper<ReLink>().set("status", 0).eq("id", linkId));
        if (updateResult) {
            // 删除缓存中的相关数据
            ReLink reLink = getById(linkId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reLink), 1);
        } else {
            log.error("删除链接失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO updateEntityById(Serializable id, ReLink entity) {
        int linkId = Integer.parseInt(id.toString());
        boolean updateResult = update(entity, new UpdateWrapper<ReLink>().eq("id", linkId));
        if (updateResult) {
            // 如果缓存中有的话，那么淘汰缓存
            // 删除所有缓存
            deleteCacheAll();
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            log.error("更新链接失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO getEntityById(Serializable id) {
        int linkId = Integer.parseInt(id.toString());
        JsonResultVO jsonResultVO;
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
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
        } else {
            reLink = getById(linkId);
            // 如果不存在，那么返回 找不到资源错误
            if (reLink == null || reLink.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
            setCache(reLink);
        }
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reLink), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReLink> reLinkList = list(new QueryWrapper<ReLink>().orderByDesc("modify_time"));
        setCache(reLinkList);
        log.info("从数据库中获取所有链接列表，总条数：" + reLinkList.size());
        JsonResultVO success = JsonResultVO.success(reLinkList, reLinkList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public void setCache(ReLink value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_LINK_KEY.getKey()
                .replace(":id", ":" + value.getId())
                .replace(":name", ":" + value.getName())
                .replace(":type", ":" + value.getType()), value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void setCache(List<ReLink> cache) {
        cache.forEach(this::setCache);
    }

    @Override
    public void deleteCacheAll() {
        // 删除所有缓存
        redisUtil.deleteByPattern("re_link:*");
        redisUtil.deleteByPattern("re_link_page:*");
        redisUtil.deleteByPattern("re_link_page_total:*");
    }
}
