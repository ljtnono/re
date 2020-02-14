package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReSkillSearchDTO;
import cn.ljtnono.re.entity.ReSkill;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReSkillMapper;
import cn.ljtnono.re.vo.JsonResultVO;
import cn.ljtnono.re.service.IReSkillService;
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
import java.util.Optional;

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
        Optional<ReSkill> reSkill = Optional.ofNullable(entity);
        reSkill.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_SKILL_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":name", ":" + entity.getName())
                .replace(":owner", ":" + entity.getOwner());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            redisUtil.deleteByPattern("re_skill:*");
            redisUtil.deleteByPattern("re_skill_page:*");
            redisUtil.deleteByPattern("re_skill_page_total:*");
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }


    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int skillId = Integer.parseInt(id.toString());
        if (skillId >= 1001) {
            // 在数据库中更新
            boolean updateResult = update(new UpdateWrapper<ReSkill>().set("status", 0).eq("id", skillId));
            if (updateResult) {
                // 删除缓存中的相关数据
                ReSkill reSkill = getById(skillId);
                redisUtil.deleteByPattern("re_skill:*");
                redisUtil.deleteByPattern("re_skill_page:*");
                redisUtil.deleteByPattern("re_skill_page_total:*");
                return JsonResultVO.success(Collections.singletonList(reSkill), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }


    @Override
    public JsonResultVO updateEntityById(Serializable id, ReSkill entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReSkill> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int skillId = Integer.parseInt(id.toString());
        if (skillId >= 1001) {
            boolean update = update(entity, new UpdateWrapper<ReSkill>().eq("id", skillId));
            if (update) {
                // 删除缓存
                redisUtil.deleteByPattern("re_skill:*");
                redisUtil.deleteByPattern("re_skill_page:*");
                redisUtil.deleteByPattern("re_skill_page_total:*");
                return JsonResultVO.successForMessage("操作成功", 200);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }


    @Override
    public JsonResultVO getEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int skillId = Integer.parseInt(id.toString());
        if (skillId >= 1001) {
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
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
            } else {
                reSkill = getById(skillId);
                // 如果不存在，那么返回 找不到资源错误
                if (reSkill == null || reSkill.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_SKILL_KEY.getKey()
                        .replace(":id", ":" + reSkill.getId())
                        .replace(":name", ":" + reSkill.getName())
                        .replace(":owner", ":" + reSkill.getOwner()), reSkill, RedisUtil.EXPIRE_TIME_DEFAULT);
            }
            jsonResultVO = JsonResultVO.success(Collections.singletonList(reSkill), 1);
            jsonResultVO.setMessage("操作成功");
            return jsonResultVO;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }


    @Override
    public JsonResultVO listEntityAll() {
        List<ReSkill> reSkillList = list();
        Optional<List<ReSkill>> optionalList = Optional.ofNullable(reSkillList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reSkill -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_SKILL_KEY.getKey()
                    .replace(":id", ":" + reSkill.getId())
                    .replace(":name", ":" + reSkill.getName())
                    .replace(":owner", ":" + reSkill.getOwner()), reSkill, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有技能列表，总条数：" + l.size()));
        JsonResultVO success = JsonResultVO.success(reSkillList, reSkillList.size());
        success.setMessage("操作成功");
        return success;
    }


    @Override
    public JsonResultVO listSkillPage(Integer page, Integer count) {
        Optional<Integer> optionalPage = Optional.ofNullable(page);
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        optionalPage.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalCount.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
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
            log.info("获取" + page + "页技能数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }


    @Override
    public JsonResultVO restore(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int skillId = Integer.parseInt(id.toString());
        if (skillId >= 1001) {
            // 更新状态
            boolean update = update(new UpdateWrapper<ReSkill>().set("status", 1).eq("id", skillId));
            if (update) {
                // 删除缓存中的相关数据
                ReSkill skill = getById(skillId);
                redisUtil.deleteByPattern("re_skill:*");
                redisUtil.deleteByPattern("re_skill_page:*");
                redisUtil.deleteByPattern("re_skill_page_total:*");
                return JsonResultVO.success(Collections.singletonList(skill), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }


    @Override
    public JsonResultVO search(ReSkillSearchDTO reSkillSearchDTO, PageDTO pageDTO) {
        Optional<ReSkillSearchDTO> optionalReLinkSearchDTO = Optional.ofNullable(reSkillSearchDTO);
        optionalReLinkSearchDTO.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_ERROR));
        QueryWrapper<ReSkill> reSkillQueryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(reSkillSearchDTO.getName())) {
            reSkillQueryWrapper.like("name", reSkillSearchDTO.getName());
        }
        if (!StringUtil.isEmpty(reSkillSearchDTO.getOwner())) {
            reSkillQueryWrapper.like("owner", reSkillSearchDTO.getOwner());
        }
        IPage<ReSkill> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reSkillQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }
}
