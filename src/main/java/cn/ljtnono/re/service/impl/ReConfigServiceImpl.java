package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.entity.ReConfig;
import cn.ljtnono.re.enumeration.HttpStatusEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReConfigMapper;
import cn.ljtnono.re.vo.JsonResultVO;
import cn.ljtnono.re.service.IReConfigService;
import cn.ljtnono.re.service.common.IReEntityService;
import cn.ljtnono.re.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 配置类服务实现类
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@Service
@Slf4j
public class ReConfigServiceImpl extends ServiceImpl<ReConfigMapper, ReConfig> implements IReConfigService, IReEntityService<ReConfig> {

    private RedisUtil redisUtil;

    @Autowired
    public ReConfigServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }


    @Override
    public JsonResultVO saveEntity(ReConfig entity) {
        Optional<ReConfig> optionalReBook = Optional.ofNullable(entity);
        optionalReBook.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":key", ":" + entity.getKey());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        Integer configId = Integer.parseInt(id.toString());
        if (configId >= 1) {
            // 在数据库中更新
            boolean updateResult = update(new UpdateWrapper<ReConfig>().set("`delete`", 0).eq("id", configId));
            if (updateResult) {
                // 删除缓存中的相关数据
                ReConfig reConfig = getById(configId);
                String key = ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                        .replace(":id", ":" + reConfig.getId())
                        .replace(":key", ":" + reConfig.getKey());
                boolean b = redisUtil.hasKey(key);
                if (b) {
                    redisUtil.del(key);
                }
                return JsonResultVO.success(Collections.singletonList(reConfig), 1);
            } else {
                throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.PARAM_INVALID_ERROR);
        }
    }


    @Override
    public JsonResultVO updateEntityById(Serializable id, ReConfig entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReConfig> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        Integer configId = Integer.parseInt(id.toString());
        if (configId >= 1) {
            boolean updateResult = update(new UpdateWrapper<ReConfig>().setEntity(entity).eq("id", configId));
            if (updateResult) {
                // 更新操作
                String key = ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                        .replace(":id", ":" + entity.getId())
                        .replace(":key", ":" + entity.getKey());
                boolean b = redisUtil.hasKey(key);
                if (b) {
                    redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
                }
                return JsonResultVO.successForMessage("操作成功", 200);
            } else {
                throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.PARAM_INVALID_ERROR);
        }
    }


    @Override
    public JsonResultVO getEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        Integer configId = Integer.parseInt(id.toString());
        if (configId >= 1) {
            JsonResultVO jsonResultVO;
            // 如果缓存中存在，那么首先从缓存中获取
            String key = ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                    .replace(":id", ":" + configId)
                    .replace(":key", ":*");
            boolean b = redisUtil.hasKeyByPattern(key);
            // 如果存在，那么直接获取
            ReConfig reConfig;
            if (b) {
                reConfig = (ReConfig) redisUtil.getByPattern(key);
                if (reConfig == null || reConfig.getStatus() == 0) {
                    throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
                }
                jsonResultVO = JsonResultVO.success(Collections.singletonList(reConfig), 1);
            } else {
                reConfig = getById(configId);
                // 如果不存在，那么返回 找不到资源错误
                if (reConfig == null || reConfig.getStatus() == 0) {
                    throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                        .replace(":id", ":" + reConfig.getId())
                        .replace(":key", ":" + reConfig.getKey()), reConfig, RedisUtil.EXPIRE_TIME_DEFAULT);
                jsonResultVO = JsonResultVO.success(Collections.singletonList(reConfig), 1);
            }
            jsonResultVO.setMessage("操作成功");
            return jsonResultVO;
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReConfig> reConfigList = list();
        Optional<List<ReConfig>> optionalList = Optional.ofNullable(reConfigList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reConfig -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                    .replace(":id", ":" + reConfig.getId())
                    .replace(":key", ":" + reConfig.getKey()), reConfig, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有配置项列表，总条数：" + l.size()));
        JsonResultVO success = JsonResultVO.success(reConfigList, reConfigList.size());
        success.setMessage("操作成功");
        return success;
    }
}
