package cn.rootelement.service.impl;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReSkillSearchDTO;
import cn.rootelement.entity.ReSkill;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.enumeration.ReEntityRedisKeyEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.mapper.ReSkillMapper;
import cn.rootelement.vo.JsonResultVO;
import cn.rootelement.service.IReSkillService;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 技能类服务实现类
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.2
 */
@Service
@Slf4j
public class ReSkillServiceImpl extends ServiceImpl<ReSkillMapper, ReSkill> implements IReSkillService {

    private RedisUtil redisUtil;

    @Autowired
    public ReSkillServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public JsonResultVO saveEntity(ReSkill entity) {
        boolean save = save(entity);
        if (save) {
            // 将实体类存储到缓存中去
            deleteCacheAll();
            setCache(entity);
            return JsonResultVO.forMessage("操作成功！", 200);
        } else {
            log.error("新增技能失败, {}", entity);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        int skillId = Integer.parseInt(id.toString());
        // 在数据库中更新
        boolean updateResult = update(new UpdateWrapper<ReSkill>().set("status", 0).eq("id", skillId));
        if (updateResult) {
            // 删除缓存中的相关数据
            ReSkill reSkill = getById(skillId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reSkill), 1);
        } else {
            log.error("删除技能失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO updateEntityById(Serializable id, ReSkill entity) {
        int skillId = Integer.parseInt(id.toString());
        boolean update = update(entity, new UpdateWrapper<ReSkill>().eq("id", skillId));
        if (update) {
            // 删除缓存
            deleteCacheAll();
            return JsonResultVO.forMessage("操作成功", 200);
        } else {
            log.error("更新技能失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO getEntityById(Serializable id) {
        int skillId = Integer.parseInt(id.toString());
        JsonResultVO jsonResultVO;
        // 如果缓存中存在，那么首先从缓存中获取
        String key = ReEntityRedisKeyEnum.RE_SKILL_KEY.getKey()
                .replace(":id", ":" + skillId)
                .replace(":name", ":*")
                .replace(":owner", ":*");
        boolean b = redisUtil.hasKeyByPattern(key);
        // 如果存在，那么直接获取
        ReSkill reSkill;
        if (b) {
            reSkill = (ReSkill) redisUtil.getByPattern(key);
            if (reSkill == null || reSkill.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
        } else {
            reSkill = getById(skillId);
            // 如果不存在，那么返回 找不到资源错误
            if (reSkill == null || reSkill.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
            setCache(reSkill);
        }
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reSkill), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }

    @Override
    public JsonResultVO listSkillPage(Integer page, Integer count) {
        String redisKey = ReEntityRedisKeyEnum.RE_SKILL_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_SKILL_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页技能数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReSkill> pageResult = page(new Page<>(page, count), new QueryWrapper<ReSkill>().orderByDesc("modify_time"));
            log.info("从数据库中获取" + page + "页技能数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }


    @Override
    public JsonResultVO restore(Serializable id) {
        int skillId = Integer.parseInt(id.toString());
        // 更新状态
        boolean update = update(new UpdateWrapper<ReSkill>().set("status", 1).eq("id", skillId));
        if (update) {
            // 删除缓存中的相关数据
            ReSkill skill = getById(skillId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(skill), 1);
        } else {
            log.error("恢复技能失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO search(ReSkillSearchDTO reSkillSearchDTO, PageDTO pageDTO) {
        QueryWrapper<ReSkill> reSkillQueryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(reSkillSearchDTO.getName())) {
            reSkillQueryWrapper.like("name", reSkillSearchDTO.getName());
        }
        if (!StringUtil.isEmpty(reSkillSearchDTO.getOwner())) {
            reSkillQueryWrapper.like("owner", reSkillSearchDTO.getOwner());
        }
        reSkillQueryWrapper.orderByDesc("modify_time");
        IPage<ReSkill> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reSkillQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            log.error("查询技能失败, {}, {}", reSkillSearchDTO, pageDTO);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReSkill> reSkillList = list(new QueryWrapper<ReSkill>().orderByDesc("modify_time"));
        setCache(reSkillList);
        log.info("从数据库中获取所有技能列表，总条数：" + reSkillList.size());
        JsonResultVO success = JsonResultVO.success(reSkillList, reSkillList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public void setCache(ReSkill value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_SKILL_KEY.getKey()
                .replace(":id", ":" + value.getId())
                .replace(":name", ":" + value.getName())
                .replace(":owner", ":" + value.getOwner()), value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void setCache(List<ReSkill> cache) {
        cache.forEach(this::setCache);
    }

    @Override
    public void deleteCacheAll() {
        redisUtil.deleteByPattern("re_skill:*");
        redisUtil.deleteByPattern("re_skill_page:*");
        redisUtil.deleteByPattern("re_skill_page_total:*");
    }
}
