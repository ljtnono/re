package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReRoleSearchDTO;
import cn.ljtnono.re.entity.RePermission;
import cn.ljtnono.re.entity.ReRole;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReRoleMapper;
import cn.ljtnono.re.vo.JsonResultVO;
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

    private RedisUtil redisUtil;

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
    public JsonResultVO listRolePage(Integer page, Integer count) {
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
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReRole> pageResult = page(new Page<>(page, count), new QueryWrapper<ReRole>().orderByDesc("modify_time"));
            log.info("从数据库中获取" + page + "页角色数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    @Override
    public JsonResultVO restore(Serializable id) {
        int roleId = Integer.parseInt(id.toString());
        // 更新状态
        boolean update = update(new UpdateWrapper<ReRole>().set("status", 1).eq("id", roleId));
        getBaseMapper().restoreUserRole(roleId);
        if (update) {
            // 删除缓存中的相关数据
            ReRole reRole = getById(roleId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reRole), 1);
        } else {
            log.error("恢复角色失败, id = {}", id);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO search(ReRoleSearchDTO reRoleSearchDTO, PageDTO pageDTO) {
        QueryWrapper<ReRole> reRoleQueryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(reRoleSearchDTO.getName())) {
            reRoleQueryWrapper.like("name", reRoleSearchDTO.getName());
        }
        if (!StringUtil.isEmpty(reRoleSearchDTO.getDescription())) {
            reRoleQueryWrapper.like("description", reRoleSearchDTO.getDescription());
        }
        reRoleQueryWrapper.orderByDesc("modify_time");
        IPage<ReRole> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reRoleQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            log.error("查询角色列表失败, {}, {}", reRoleSearchDTO, pageDTO);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO saveEntity(ReRole entity) {
        boolean save = save(entity);
        if (save) {
            // 将实体类存储到缓存中去
            deleteCacheAll();
            setCache(entity);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            log.error("新增角色失败, {}", entity);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        int roleId = Integer.parseInt(id.toString());
        // 在数据库中更新
        boolean updateResult = update(new UpdateWrapper<ReRole>().set("status", 0).eq("id", roleId));
        // 删除数据库中所有含有该角色的关联表的数据
        getBaseMapper().deleteUserRole(roleId);
        if (updateResult) {
            // 删除缓存中的相关数据
            ReRole reRole = getById(roleId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reRole), 1);
        } else {
            log.error("删除角色失败, id = {}", id);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO updateEntityById(Serializable id, ReRole entity) {
        int roleId = Integer.parseInt(id.toString());
        boolean updateResult = update(entity, new UpdateWrapper<ReRole>().eq("id", roleId));
        if (updateResult) {
            // 删除所有缓存
            deleteCacheAll();
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            log.error("更新角色失败, {}", entity);
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO getEntityById(Serializable id) {
        int roleId = Integer.parseInt(id.toString());
        JsonResultVO jsonResultVO;
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
            setCache(reRole);
        }
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reRole), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReRole> reRoleList = list(new QueryWrapper<ReRole>().orderByDesc("modify_time"));
        setCache(reRoleList);
        log.info("从数据库中获取所有角色列表，总条数：" + reRoleList.size());
        JsonResultVO success = JsonResultVO.success(reRoleList, reRoleList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public void setCache(ReRole value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                .replace(":id", ":" + value.getId())
                .replace(":name", ":" + value.getName())
                .replace(":description", ":" + value.getDescription()), value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void setCache(List<ReRole> cache) {
        cache.forEach(this::setCache);
    }

    @Override
    public void deleteCacheAll() {
        redisUtil.deleteByPattern("re_role:*");
        redisUtil.deleteByPattern("re_role_page:*");
        redisUtil.deleteByPattern("re_role_page_total:*");
    }
}
