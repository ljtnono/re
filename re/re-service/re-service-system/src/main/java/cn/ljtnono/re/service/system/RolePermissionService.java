package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.entity.system.RolePermission;
import cn.ljtnono.re.mapper.system.RolePermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/6 21:47
 * Description: 角色权限Service类
 */
@Service
public class RolePermissionService {

    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private UserRoleService userRoleService;

    //*********************************** 接口方法 ***********************************//

    /**
     * 根据用户id获取权限id列表
     *
     * @param userId 用户id
     * @return List<Integer> 用户权限id列表
     */
    public List<Integer> getPermissionIdListByUserId(Integer userId) {
        Integer roleId = userRoleService.getRoleIdByUserId(userId);
        return rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>()
                .select(RolePermission::getId, RolePermission::getPermissionId)
                .eq(RolePermission::getRoleId, roleId))
                .parallelStream()
                .map(RolePermission::getPermissionId)
                .distinct()
                .collect(Collectors.toList());
    }


    //*********************************** 私有方法 ***********************************//

    /**
     * 设置创建时间和最后修改时间为当前时间
     * @param reRolePermission 角色权限对象
     * @author Ling, Jiatong
     *
     */
    private void setCreateTimeAndModifyTimeNow(RolePermission reRolePermission) {
        reRolePermission.setCreateTime(new Date());
        reRolePermission.setModifyTime(new Date());
    }


    //*********************************** 公有方法 ***********************************//

    /**
     * 插入角色权限表
     * @param roleId 角色id
     * @param permissionIdList 权限列表
     * @author Ling, Jiatong
     *
     */
    public void insertRolePermission(Integer roleId, List<Integer> permissionIdList) {
        Optional.ofNullable(roleId)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.ROLE_ID_NULL_ERROR));
        if (CollectionUtils.isEmpty(permissionIdList)) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        for (Integer permissionId : permissionIdList) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            setCreateTimeAndModifyTimeNow(rolePermission);
            rolePermission.setPermissionId(permissionId);
            int insert = rolePermissionMapper.insert(rolePermission);
            if (insert <= 0) {
                throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
            }
        }
    }

    /**
     * 根据角色id删除角色权限表的数据
     * @param roleId 角色id
     * @author Ling, Jiatong
     *
     */
    public void deleteRolePermissionByRoleId(Integer roleId) {
        Optional.ofNullable(roleId)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.ROLE_ID_NULL_ERROR));
        // 查看是否存在该数据
        Integer count = rolePermissionMapper.selectCount(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId));
        if (count > 0) {
            int delete = rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>()
                    .eq(RolePermission::getRoleId, roleId));
            if (delete <= 0) {
                throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
            }
        }
    }

    /**
     * 根据角色id获取角色所拥有的权限id列表
     * @param roleId 角色id
     * @return List<Integer> 权限id列表
     * @author Ling, Jiatong
     *
     */
    public List<Integer> getPermissionIdListByRoleId(Integer roleId) {
        Optional.ofNullable(roleId)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.ROLE_ID_NULL_ERROR));
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId));
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            return Lists.newArrayList();
        } else {
            return rolePermissionList.parallelStream()
                    .map(RolePermission::getPermissionId)
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    //*********************************** 其他方法 ***********************************//

}
