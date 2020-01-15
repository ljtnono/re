package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.entity.ReSkill;
import cn.ljtnono.re.entity.ReTimeline;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReTimelineMapper;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReTimelineService;
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
 * 时间轴服务实现类
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@Service
@Slf4j
public class ReTimelineServiceImpl extends ServiceImpl<ReTimelineMapper, ReTimeline> implements IReTimelineService {

    private final RedisUtil redisUtil;

    @Autowired
    public ReTimelineServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public JsonResult saveEntity(ReTimeline entity) {
        Optional<ReTimeline> reTimeline = Optional.ofNullable(entity);
        reTimeline.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_TIMELINE_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":pushDate", ":" + entity.getPushDate());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            redisUtil.deleteByPattern("re_timeline_page:*");
            redisUtil.deleteByPattern("re_timeline_page_total:*");
            return JsonResult.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResult deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int timelineId = Integer.parseInt(id.toString());
        if (timelineId >= 1001) {
            // 在数据库中更新
            boolean updateResult = update(new UpdateWrapper<ReTimeline>().set("status", 0).eq("id", timelineId));
            if (updateResult) {
                // 删除缓存中的相关数据
                ReTimeline reTimeline = getById(timelineId);
                redisUtil.deleteByPattern("re_timeline:*");
                redisUtil.deleteByPattern("re_timeline_page:*");
                redisUtil.deleteByPattern("re_timeline_page_total:*");
                return JsonResult.success(Collections.singletonList(reTimeline), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult updateEntityById(Serializable id, ReTimeline entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReTimeline> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int timelineId = Integer.parseInt(id.toString());
        if (timelineId >= 1001) {
            boolean updateResult = update(entity, new UpdateWrapper<ReTimeline>().eq("id", timelineId));
            if (updateResult) {
                // 删除相关缓存
                redisUtil.deleteByPattern("re_timeline:*");
                redisUtil.deleteByPattern("re_timeline_page:*");
                redisUtil.deleteByPattern("re_timeline_page_total:*");
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
        int timelineId = Integer.parseInt(id.toString());
        if (timelineId >= 1001) {
            JsonResult jsonResult;
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
                redisUtil.set(ReEntityRedisKeyEnum.RE_TIMELINE_KEY.getKey()
                        .replace(":id", ":" + reTimeline.getId())
                        .replace(":pushDate", ":" +reTimeline.getPushDate()), reTimeline, RedisUtil.EXPIRE_TIME_DEFAULT);
            }
            jsonResult = JsonResult.success(Collections.singletonList(reTimeline), 1);
            jsonResult.setMessage("操作成功");
            return jsonResult;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult listEntityAll() {
        List<ReTimeline> reTimelineList = list();
        Optional<List<ReTimeline>> optionalList = Optional.ofNullable(reTimelineList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reTimeline -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_TIMELINE_KEY.getKey()
                    .replace(":id", ":" + reTimeline.getId())
                    .replace(":pushDate", ":" + reTimeline.getPushDate()), reTimeline, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有时间轴列表，总条数：" + l.size()));
        JsonResult success = JsonResult.success(reTimelineList, reTimelineList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public JsonResult listTimelinePage(Integer page, Integer count) {
        Optional<Integer> optionalPage = Optional.ofNullable(page);
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        optionalPage.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalCount.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
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
            return JsonResult.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReTimeline> pageResult = page(new Page<>(page, count), new QueryWrapper<ReTimeline>().orderByDesc("modify_time"));
            log.info("获取" + page + "页时间轴数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    /**
     * 恢复删除的时间轴
     *
     * @param id 需要恢复的时间轴id
     * @return JsonResult 对象
     */
    @Override
    public JsonResult restore(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int timelineId = Integer.parseInt(id.toString());
        if (timelineId >= 1001) {
            // 更新状态
            boolean update = update(new UpdateWrapper<ReTimeline>().set("status", 1).eq("id", timelineId));
            if (update) {
                // 删除缓存中的相关数据
                ReTimeline timeline = getById(timelineId);
                redisUtil.deleteByPattern("re_timeline:*");
                redisUtil.deleteByPattern("re_timeline_page:*");
                redisUtil.deleteByPattern("re_timeline_page_total:*");
                return JsonResult.success(Collections.singletonList(timeline), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }
}
