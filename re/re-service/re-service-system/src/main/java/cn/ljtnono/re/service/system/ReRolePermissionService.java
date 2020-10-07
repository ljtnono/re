package cn.ljtnono.re.service.system;

import cn.ljtnono.re.entity.system.ReRolePermission;
import cn.ljtnono.re.mapper.system.ReRolePermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/6 21:47
 * Description: 角色权限Service类
 */
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
public class ReRolePermissionService {

    @Resource
    private ReRolePermissionMapper reRolePermissionMapper;
    @Autowired
    private ReUserRoleService reUserRoleService;

    //*********************************** 接口方法 ***********************************//

    /**
     * 根据用户id获取权限id列表
     *
     * @param userId 用户id
     * @return List<Integer> 用户权限id列表
     */
    @Transactional(readOnly = true)
    public List<Integer> getRePermissionIdListByReUserId(Integer userId) {
        Integer roleId = reUserRoleService.getReRoleIdByReUserId(userId);
        return reRolePermissionMapper.selectList(new LambdaQueryWrapper<ReRolePermission>()
                .select(ReRolePermission::getId, ReRolePermission::getPermissionId)
                .eq(ReRolePermission::getRoleId, roleId))
                .parallelStream()
                .map(ReRolePermission::getPermissionId)
                .distinct()
                .collect(Collectors.toList());
    }
    //*********************************** 私有方法 ***********************************//

    //*********************************** 公有方法 ***********************************//

    //*********************************** 其他方法 ***********************************//

}
