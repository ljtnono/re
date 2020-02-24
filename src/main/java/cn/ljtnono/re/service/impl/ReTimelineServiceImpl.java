package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReTimelineSearchDTO;
import cn.ljtnono.re.entity.ReTimeline;
import cn.ljtnono.re.enumeration.DateStyleEnum;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReTimelineMapper;
import cn.ljtnono.re.vo.JsonResultVO;
import cn.ljtnono.re.service.IReTimelineService;
import cn.ljtnono.re.util.DateUtil;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 时间轴服务实现类
 * @author ljt
 * @date 2019/11/23
 * @version 1.0.1
 */
@Service
@Slf4j
public class ReTimelineServiceImpl extends ServiceImpl<ReTimelineMapper, ReTimeline> implements IReTimelineService {

    private RedisUtil redisUtil;

    @Autowired
    public ReTimelineServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public JsonResultVO saveEntity(ReTimeline entity) {
        boolean save = save(entity);
        if (save) {
            // 将实体类存储到缓存中去
            deleteCacheAll();
            setCache(entity);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            log.error("新增时间轴失败, {}", entity);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        int timelineId = Integer.parseInt(id.toString());
        // 在数据库中更新
        boolean updateResult = update(new UpdateWrapper<ReTimeline>().set("status", 0).eq("id", timelineId));
        if (updateResult) {
            // 删除缓存中的相关数据
            ReTimeline reTimeline = getById(timelineId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reTimeline), 1);
        } else {
            log.error("删除时间轴失败, id = {}", id);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO updateEntityById(Serializable id, ReTimeline entity) {
        int timelineId = Integer.parseInt(id.toString());
        boolean updateResult = update(entity, new UpdateWrapper<ReTimeline>().eq("id", timelineId));
        if (updateResult) {
            // 删除相关缓存
            deleteCacheAll();
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            log.error("更新时间轴失败, id = {}", id);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO getEntityById(Serializable id) {
        int timelineId = Integer.parseInt(id.toString());
        JsonResultVO jsonResultVO;
        // 如果缓存中存在，那么首先从缓存中获取
        String key = ReEntityRedisKeyEnum.RE_TIMELINE_KEY.getKey()
                .replace(":id", ":" + timelineId)
                .replace(":pushDate", ":*");
        boolean b = redisUtil.hasKeyByPattern(key);
        // 如果存在，那么直接获取
        ReTimeline reTimeline;
        if (b) {
            reTimeline = (ReTimeline) redisUtil.getByPattern(key);
            if (reTimeline == null || reTimeline.getStatus() == 0) {
                throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
            }
        } else {
            reTimeline = getById(timelineId);
            // 如果不存在，那么返回 找不到资源错误
            if (reTimeline == null || reTimeline.getStatus() == 0) {
                throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
            }
            setCache(reTimeline);
        }
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reTimeline), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReTimeline> reTimelineList = list(new QueryWrapper<ReTimeline>().orderByDesc("modify_time"));
        setCache(reTimelineList);
        log.info("从数据库中获取所有时间轴列表，总条数：" + reTimelineList.size());
        JsonResultVO success = JsonResultVO.success(reTimelineList, reTimelineList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public JsonResultVO listTimelinePage(Integer page, Integer count) {
        String redisKey = ReEntityRedisKeyEnum.RE_TIMELINE_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_TIMELINE_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页时间轴数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReTimeline> pageResult = page(new Page<>(page, count), new QueryWrapper<ReTimeline>().orderByDesc("modify_time"));
            log.info("从数据库中获取" + page + "页时间轴数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    /**
     * 恢复删除的时间轴
     *
     * @param id 需要恢复的时间轴id
     * @return JsonResult 对象
     */
    @Override
    public JsonResultVO restore(Serializable id) {
        int timelineId = Integer.parseInt(id.toString());
        // 更新状态
        boolean update = update(new UpdateWrapper<ReTimeline>().set("status", 1).eq("id", timelineId));
        if (update) {
            // 删除缓存中的相关数据
            ReTimeline timeline = getById(timelineId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(timeline), 1);
        } else {
            log.error("恢复时间轴失败, id = {}", id);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 获取博客更新日志时间轴
     *
     * @return JsonResult对象
     */
    @Override
    public JsonResultVO listUpdateLogTimeline() {
        List<ReTimeline> reTimelineList = list(new QueryWrapper<ReTimeline>().orderByDesc("push_date").last("limit 20"));
        setCache(reTimelineList);
        log.info("从数据库中获取日志更新时间轴列表，总条数：" + reTimelineList.size());
        JsonResultVO success = JsonResultVO.success(reTimelineList, reTimelineList.size());
        success.setMessage("操作成功");
        return success;
    }


    @Override
    public JsonResultVO search(ReTimelineSearchDTO reTimelineSearchDTO, PageDTO pageDTO) {
        QueryWrapper<ReTimeline> reTimelineQueryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(reTimelineSearchDTO.getContent())) {
            reTimelineQueryWrapper.like("content", reTimelineSearchDTO.getContent());
        }
        if (reTimelineSearchDTO.getPushDate() != null) {
            reTimelineQueryWrapper.eq("push_date", DateUtil.formatDate(reTimelineSearchDTO.getPushDate(), DateStyleEnum.yyyy_MM_dd));
        }
        reTimelineQueryWrapper.orderByDesc("modify_time");
        IPage<ReTimeline> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reTimelineQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            log.error("查询时间轴失败, {}, {}", reTimelineSearchDTO, pageDTO);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public void setCache(ReTimeline value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_TIMELINE_KEY.getKey()
                .replace(":id", ":" + value.getId())
                .replace(":pushDate", ":" + value.getPushDate()), value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void setCache(List<ReTimeline> cache) {
        cache.forEach(this::setCache);
    }

    @Override
    public void deleteCacheAll() {
        redisUtil.deleteByPattern("re_timeline:*");
        redisUtil.deleteByPattern("re_timeline_page:*");
        redisUtil.deleteByPattern("re_timeline_page_total:*");
    }
}
