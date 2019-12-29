package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.entity.ReUserRole;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReUserRoleMapper;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReUserRoleService;
import cn.ljtnono.re.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 用户角色服务实现类
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@Service
@Slf4j
public class ReUserRoleServiceImpl extends ServiceImpl<ReUserRoleMapper, ReUserRole> implements IReUserRoleService {

    private final RedisUtil redisUtil;

    @Autowired
    public ReUserRoleServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }


    /**
     * 新增单个实体类
     *
     * @param entity 具体的实体类
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult saveEntity(ReUserRole entity) {
        Optional<ReUserRole> reUserRole = Optional.ofNullable(entity);
        reUserRole.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_USER_ROLE_KEY.getKey()
                .replace(":id", ":" + entity.getId());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            return JsonResult.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 根据id删除一个实体类
     *
     * @param id 实体类id
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“, data: {删除的实体类}}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult deleteEntityById(Serializable id) {
//        Optional<Serializable> optionalId = Optional.ofNullable(id);
//        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
//        Integer userRoleId = Integer.parseInt(id.toString());
//        if (userRoleId >= 1) {
//            // 在数据库中更新
//            boolean updateResult = update(new UpdateWrapper<ReUserRole>().set("`delete`", 0).eq("id", userRoleId));
//            if (updateResult) {
//                // 删除缓存中的相关数据
//                ReTimeline reTimeline = getById(userRoleId);
//                String key = ReEntityRedisKeyEnum.RE_TIMELINE_KEY.getKey()
//                        .replace(":id", ":" + reTimeline.getId());
//                boolean b = redisUtil.hasKey(key);
//                if (b) {
//                    redisUtil.del(key);
//                }
//                return JsonResult.success(Collections.singletonList(reTimeline), 1);
//            } else {
//                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
//            }
//        } else {
//            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
//        }
        return null;
    }

    /**
     * 根据id更新一个实体类
     *
     * @param id     实体类的id
     * @param entity 要更新的实体类
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult updateEntityById(Serializable id, ReUserRole entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReUserRole> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        Integer userRoleId = Integer.parseInt(id.toString());
        if (userRoleId >= 1) {
            boolean updateResult = update(new UpdateWrapper<ReUserRole>().setEntity(entity).eq("id", userRoleId));
            if (updateResult) {
                // 更新操作
                String key = ReEntityRedisKeyEnum.RE_USER_ROLE_KEY.getKey()
                        .replace(":id", ":" + entity.getId());
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

    /**
     * 根据id获取一个实体类
     *
     * @param id 实体类id
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“, data: {实体类}}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult getEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        Integer userRoleId = Integer.parseInt(id.toString());
        if (userRoleId >= 1) {
            JsonResult jsonResult;
            // 如果缓存中存在，那么首先从缓存中获取
            String key = ReEntityRedisKeyEnum.RE_USER_ROLE_KEY.getKey()
                    .replace(":id", ":" + userRoleId);
            boolean b = redisUtil.hasKeyByPattern(key);
            // 如果存在，那么直接获取
            ReUserRole reUserRole;
            if (b) {
                reUserRole = (ReUserRole) redisUtil.getByPattern(key);
                if (reUserRole == null) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                jsonResult = JsonResult.success(Collections.singletonList(reUserRole), 1);
            } else {
                reUserRole = getById(userRoleId);
                // 如果不存在，那么返回 找不到资源错误
                if (reUserRole == null) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_USER_ROLE_KEY.getKey()
                        .replace(":id", ":" + reUserRole.getId()), reUserRole, RedisUtil.EXPIRE_TIME_DEFAULT);
                jsonResult = JsonResult.success(Collections.singletonList(reUserRole), 1);
            }
            jsonResult.setMessage("操作成功");
            return jsonResult;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    /**
     * 获取实体类的所有列表
     *
     * @return 实体类所有列表
     * 操作成功{request: "success", status: 200, message: "操作成功“, data: {列表}}
     * 操作失败{request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult listEntityAll() {
        List<ReUserRole> reUserRoleList = list();
        Optional<List<ReUserRole>> optionalList = Optional.ofNullable(reUserRoleList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reUserRole -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_USER_ROLE_KEY.getKey()
                    .replace(":id", ":" + reUserRole.getId()), reUserRole, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有用户角色列表，总条数：" + l.size()));
        JsonResult success = JsonResult.success(reUserRoleList, reUserRoleList.size());
        success.setMessage("操作成功");
        return success;
    }
}
