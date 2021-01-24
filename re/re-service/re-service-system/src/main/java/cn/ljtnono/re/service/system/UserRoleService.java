package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.entity.system.UserRole;
import cn.ljtnono.re.mapper.system.UserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/6 21:44
 * Description: 用户角色Service类
 */
@Slf4j
@Service
public class UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    //*********************************** 接口方法 ***********************************//

    //*********************************** 私有方法 ***********************************//

    //*********************************** 公有方法 ***********************************//

    /**
     * 通过用户id获取角色id
     *
     * @param userId 用户id
     * @return Integer 角色id
     */
    public Integer getRoleIdByUserId(Integer userId) {
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>()
                .select(UserRole::getId, UserRole::getRoleId)
                .eq(UserRole::getUserId, userId));
        if (userRole != null) {
            return userRole.getRoleId();
        }
        return null;
    }

    /**
     * 更新用户的角色
     * @param userId 用户id
     * @param roleId 角色id
     * @author Ling, Jiatong
     *
     */
    public void updateRoleIdByUserId(Integer userId, Integer roleId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.USER_ID_NULL_ERROR));
        Optional.ofNullable(roleId)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.ROLE_ID_NULL_ERROR));
        int update = userRoleMapper.update(null, new LambdaUpdateWrapper<UserRole>()
                .set(UserRole::getRoleId, roleId)
                .eq(UserRole::getUserId, userId));
        if (update <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    //*********************************** 其他方法 ***********************************//

}
