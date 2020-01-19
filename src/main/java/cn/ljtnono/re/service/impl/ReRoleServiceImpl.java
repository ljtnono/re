package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReRoleSearchDTO;
import cn.ljtnono.re.dto.ReSkillSearchDTO;
import cn.ljtnono.re.entity.RePermission;
import cn.ljtnono.re.entity.ReRole;
import cn.ljtnono.re.entity.ReSkill;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.GlobalVariableEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReRoleMapper;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReRoleService;
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
 * 角色服务实现类
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.2
 */
@Service
@Slf4j
public class ReRoleServiceImpl extends ServiceImpl<ReRoleMapper, ReRole> implements IReRoleService {

    private final RedisUtil redisUtil;

    @Autowired
    public ReRoleServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public List<RePermission> listPermissionByRoleId(Integer roleId) {
        Optional<Integer> id = Optional.ofNullable(roleId);
        // 如果为null 抛出参数缺失异常
        id.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        // 如果id > 0 并且 id < 60 执行查询
        Optional<List<RePermission>> rePermissions = id.filter(i -> i > 0 && i < 60)
                .map((i) -> getBaseMapper().listPermissionByRoleId(i));
        return rePermissions.orElseGet(ArrayList::new);
    }

    @Override
    public JsonResult listRolePage(Integer page, Integer count) {
        Optional<Integer> optionalPage = Optional.ofNullable(page);
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        optionalPage.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalCount.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        String redisKey = ReEntityRedisKeyEnum.RE_ROLE_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_ROLE_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页角色数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResult.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReRole> pageResult = page(new Page<>(page, count), new QueryWrapper<ReRole>().orderByDesc("modify_time"));
            log.info("获取" + page + "页角色数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    @Override
    public JsonResult restore(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int roleId = Integer.parseInt(id.toString());
        if (roleId >= (Integer) GlobalVariableEnum.RE_ENTITY_MIN_ID.getValue()) {
            // 更新状态
            boolean update = update(new UpdateWrapper<ReRole>().set("status", 1).eq("id", roleId));
            boolean restoreUserRole = getBaseMapper().restoreUserRole(roleId);
            if (update && restoreUserRole) {
                // 删除缓存中的相关数据
                ReRole reRole = getById(roleId);
                redisUtil.deleteByPattern("re_role:*");
                redisUtil.deleteByPattern("re_role_page:*");
                redisUtil.deleteByPattern("re_role_page_total:*");
                return JsonResult.success(Collections.singletonList(reRole), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult search(ReRoleSearchDTO reRoleSearchDTO, PageDTO pageDTO) {
        Optional<ReRoleSearchDTO> optionalReRoleSearchDTO = Optional.ofNullable(reRoleSearchDTO);
        optionalReRoleSearchDTO.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_ERROR));
        QueryWrapper<ReRole> reRoleQueryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(reRoleSearchDTO.getName())) {
            reRoleQueryWrapper.like("name", reRoleSearchDTO.getName());
        }
        if (!StringUtil.isEmpty(reRoleSearchDTO.getDescription())) {
            reRoleQueryWrapper.like("description", reRoleSearchDTO.getDescription());
        }
        IPage<ReRole> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reRoleQueryWrapper);
        if (pageResult != null) {
            return JsonResult.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResult saveEntity(ReRole entity) {
        Optional<ReRole> optionalReRole = Optional.ofNullable(entity);
        optionalReRole.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_ROLE_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":name", ":" + entity.getName())
                .replace(":description", ":" + entity.getDescription());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            return JsonResult.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResult deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int roleId = Integer.parseInt(id.toString());
        if (roleId >= (Integer) GlobalVariableEnum.RE_ENTITY_MIN_ID.getValue()) {
            // 在数据库中更新
            boolean updateResult = update(new UpdateWrapper<ReRole>().set("status", 0).eq("id", roleId));
            // 删除数据库中所有含有该角色的关联表的数据
            boolean deleteUserRole = getBaseMapper().deleteUserRole(roleId);
            if (updateResult && deleteUserRole) {
                // 删除缓存中的相关数据
                ReRole reRole = getById(roleId);
                redisUtil.deleteByPattern("re_role:*");
                redisUtil.deleteByPattern("re_role_page:*");
                redisUtil.deleteByPattern("re_role_page_total:*");
                return JsonResult.success(Collections.singletonList(reRole), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult updateEntityById(Serializable id, ReRole entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReRole> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int roleId = Integer.parseInt(id.toString());
        if (roleId >= (Integer) GlobalVariableEnum.RE_ENTITY_MIN_ID.getValue()) {
            boolean updateResult = update(entity, new UpdateWrapper<ReRole>().eq("id", roleId));
            if (updateResult) {
                // 删除所有缓存
                redisUtil.deleteByPattern("re_role:*");
                redisUtil.deleteByPattern("re_role_page:*");
                redisUtil.deleteByPattern("re_role_page_total:*");
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
        int roleId = Integer.parseInt(id.toString());
        if (roleId >= (Integer) GlobalVariableEnum.RE_ENTITY_MIN_ID.getValue()) {
            JsonResult jsonResult;
            // 如果缓存中存在，那么首先从缓存中获取
            String key = ReEntityRedisKeyEnum.RE_ROLE_KEY.getKey()
                    .replace(":id", ":" + roleId)
                    .replace(":name", ":*")
                    .replace(":description", ":*");
            boolean b = redisUtil.hasKeyByPattern(key);
            // 如果存在，那么直接获取
            ReRole reRole;
            if (b) {
                reRole = (ReRole) redisUtil.getByPattern(key);
                if (reRole == null || reRole.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
            } else {
                reRole = getById(roleId);
                // 如果不存在，那么返回 找不到资源错误
                if (reRole == null || reRole.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_LINK_TYPE_KEY.getKey()
                        .replace(":id", ":" + reRole.getId())
                        .replace(":name", ":" + reRole.getName())
                        .replace(":description", ":" + reRole.getDescription()), reRole, RedisUtil.EXPIRE_TIME_DEFAULT);
            }
            jsonResult = JsonResult.success(Collections.singletonList(reRole), 1);
            jsonResult.setMessage("操作成功");
            return jsonResult;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResult listEntityAll() {
        List<ReRole> reRoleList = list();
        Optional<List<ReRole>> optionalList = Optional.ofNullable(reRoleList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reRole -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                    .replace(":id", ":" + reRole.getId())
                    .replace(":name", ":" + reRole.getName())
                    .replace(":description", ":" + reRole.getDescription()), reRole, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有角色列表，总条数：" + l.size()));
        JsonResult success = JsonResult.success(reRoleList, reRoleList.size());
        success.setMessage("操作成功");
        return success;
    }
}
