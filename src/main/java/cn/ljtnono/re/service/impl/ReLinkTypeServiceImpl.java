package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReLink;
import cn.ljtnono.re.entity.ReLinkType;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReLinkTypeMapper;
import cn.ljtnono.re.vo.JsonResultVO;
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

/**
 * 链接类型服务实现类
 * @author ljt
 * @date 2019/11/23
 * @version 1.0.1
 */
@Service
@Slf4j
public class ReLinkTypeServiceImpl extends ServiceImpl<ReLinkTypeMapper, ReLinkType> implements IReLinkTypeService {

    private RedisUtil redisUtil;

    private IReLinkService iReLinkService;

    @Autowired
    public ReLinkTypeServiceImpl(RedisUtil redisUtil, IReLinkService iReLinkService) {
        this.redisUtil = redisUtil;
        this.iReLinkService = iReLinkService;
    }

    @Override
    public JsonResultVO saveEntity(ReLinkType entity) {
        boolean save = save(entity);
        if (save) {
            // 将实体类存储到缓存中去
            deleteCacheAll();
            setCache(entity);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            log.error("新增链接类型失败, id = {}", entity.getId());
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        int linkTypeId = Integer.parseInt(id.toString());
        // 在数据库中更新
        boolean result = update(new UpdateWrapper<ReLinkType>().set("status", 0).eq("id", linkTypeId));
        ReLinkType reLinkType = getById(id);
        // 查看是否有相关类型的博客
        List<ReLink> type = iReLinkService.list(new QueryWrapper<ReLink>().eq("type", reLinkType.getName()));
        if (null != type && type.size() > 0) {
            iReLinkService.update(new UpdateWrapper<ReLink>().set("status", 0).eq("type", reLinkType.getName()));
        }
        if (result) {
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reLinkType), 1);
        } else {
            log.error("删除链接类型失败, id = {}", id);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO updateEntityById(Serializable id, ReLinkType entity) {
        int linkTypeId = Integer.parseInt(id.toString());
        boolean updateResult = update(entity, new UpdateWrapper<ReLinkType>().eq("id", linkTypeId));
        if (updateResult) {
            deleteCacheAll();
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            log.error("更新链接类型失败, id = {}", id);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }


    @Override
    public JsonResultVO getEntityById(Serializable id) {
        int linkTypeId = Integer.parseInt(id.toString());
        JsonResultVO jsonResultVO;
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
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reLinkType), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }

    @Override
    public JsonResultVO listLinkTypePage(Integer page, Integer count) {
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
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReLinkType> pageResult = page(new Page<>(page, count), new QueryWrapper<ReLinkType>().orderByDesc("modify_time"));
            log.info("获取" + page + "页链接类型数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }


    @Override
    public JsonResultVO restore(Serializable id) {
        int linkTypeId = Integer.parseInt(id.toString());
        // 更新状态
        boolean update = update(new UpdateWrapper<ReLinkType>().set("status", 1).eq("id", linkTypeId));
        ReLinkType reLinkType = getById(id);
        List<ReLink> type = iReLinkService.list(new QueryWrapper<ReLink>().eq("type", reLinkType.getName()));
        if (null != type && type.size() > 0) {
            iReLinkService.update(new UpdateWrapper<ReLink>().set("status", 1).eq("type", reLinkType.getName()));
        }
        if (update) {
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reLinkType), 1);
        } else {
            log.error("恢复链接类型失败, id = {}", id);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO search(final String name, PageDTO pageDTO) {
        QueryWrapper<ReLinkType> reLinkTypeQueryWrapper = new QueryWrapper<ReLinkType>()
                .like("name", name)
                .orderByDesc("modify_time");
        IPage<ReLinkType> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reLinkTypeQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            log.error("查询链接类型失败, name = {}, pageDTO = {}", name, pageDTO);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReLinkType> reLinkTypeList = list(new QueryWrapper<ReLinkType>().orderByDesc("modify_time"));
        setCache(reLinkTypeList);
        log.info("从数据库中获取所有链接类型列表，总条数：" + reLinkTypeList.size());
        JsonResultVO success = JsonResultVO.success(reLinkTypeList, reLinkTypeList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public void setCache(ReLinkType value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY.getKey()
                .replace(":id", ":" + value.getId())
                .replace(":name", ":" + value.getName()), value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void setCache(List<ReLinkType> cache) {
        cache.forEach(this::setCache);
    }

    @Override
    public void deleteCacheAll() {
        // 删除缓存中的相关数据
        redisUtil.deleteByPattern("re_link_type:*");
        redisUtil.deleteByPattern("re_link_type_page:*");
        redisUtil.deleteByPattern("re_link_type_page_total:*");
        // 删除所有的link缓存相关
        redisUtil.deleteByPattern("re_link:*");
        redisUtil.deleteByPattern("re_link_page:*");
        redisUtil.deleteByPattern("re_link_page_total:*");
    }
}
