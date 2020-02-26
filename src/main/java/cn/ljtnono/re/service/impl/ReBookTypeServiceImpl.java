package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.entity.ReBookType;
import cn.ljtnono.re.enumeration.HttpStatusEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReBookTypeMapper;
import cn.ljtnono.re.vo.JsonResultVO;
import cn.ljtnono.re.service.IReBookTypeService;
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
 * 书籍类型服务实现类
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@Service
@Slf4j
public class ReBookTypeServiceImpl extends ServiceImpl<ReBookTypeMapper, ReBookType> implements IReBookTypeService {

    private RedisUtil redisUtil;

    @Autowired
    public ReBookTypeServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public JsonResultVO saveEntity(ReBookType entity) {
        Optional<ReBookType> optionalReBook = Optional.ofNullable(entity);
        optionalReBook.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_BOOK_TYPE_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":name", ":" + entity.getName());
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
        Integer bookTypeId = Integer.parseInt(id.toString());
        if (bookTypeId >= 1) {
            // 在数据库中更新
            boolean updateResult = update(new UpdateWrapper<ReBookType>().set("`delete`", 0).eq("id", bookTypeId));
            if (updateResult) {
                // 删除缓存中的相关数据
                ReBookType reBookType = getById(bookTypeId);
                String key = ReEntityRedisKeyEnum.RE_BOOK_TYPE_KEY.getKey()
                        .replace(":id", ":" + reBookType.getId())
                        .replace(":name", ":" + reBookType.getName());
                boolean b = redisUtil.hasKey(key);
                if (b) {
                    redisUtil.del(key);
                }
                return JsonResultVO.success(Collections.singletonList(reBookType), 1);
            } else {
                throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResultVO updateEntityById(Serializable id, ReBookType entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReBookType> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        Integer bookTypeId = Integer.parseInt(id.toString());
        if (bookTypeId >= 1) {
            boolean updateResult = update(new UpdateWrapper<ReBookType>().setEntity(entity).eq("id", bookTypeId));
            if (updateResult) {
                // 更新操作
                String key = ReEntityRedisKeyEnum.RE_BOOK_TYPE_KEY.getKey()
                        .replace(":id", ":" + entity.getId())
                        .replace(":name", ":" + entity.getName());
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
        int bookTypeId = Integer.parseInt(id.toString());
        if (bookTypeId >= 1) {
            JsonResultVO jsonResultVO;
            // 如果缓存中存在，那么首先从缓存中获取
            String key = ReEntityRedisKeyEnum.RE_BOOK_KEY.getKey()
                    .replace(":id", ":" + bookTypeId)
                    .replace(":name", ":*");
            boolean b = redisUtil.hasKeyByPattern(key);
            // 如果存在，那么直接获取
            ReBookType reBookType;
            if (b) {
                reBookType = (ReBookType) redisUtil.getByPattern(key);
                if (reBookType == null || reBookType.getStatus() == 0) {
                    throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
                }
                jsonResultVO = JsonResultVO.success(Collections.singletonList(reBookType), 1);
            } else {
                reBookType = getById(bookTypeId);
                // 如果不存在，那么返回 找不到资源错误
                if (reBookType == null || reBookType.getStatus() == 0) {
                    throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_BOOK_TYPE_KEY.getKey()
                        .replace(":id", ":" + reBookType.getId())
                        .replace(":name", ":" + reBookType.getName()), reBookType, RedisUtil.EXPIRE_TIME_DEFAULT);
                jsonResultVO = JsonResultVO.success(Collections.singletonList(reBookType), 1);
            }
            jsonResultVO.setMessage("操作成功");
            return jsonResultVO;
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReBookType> reBookTypeList = list();
        Optional<List<ReBookType>> optionalList = Optional.ofNullable(reBookTypeList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reBookType -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_BOOK_TYPE_KEY.getKey()
                    .replace(":id", ":" + reBookType.getId())
                    .replace(":name", ":" + reBookType.getName()), reBookType, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有书籍类型列表，总条数：" + l.size()));
        JsonResultVO success = JsonResultVO.success(reBookTypeList, reBookTypeList.size());
        success.setMessage("操作成功");
        return success;
    }
}
