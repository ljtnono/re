package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.entity.system.ReUserRole;
import cn.ljtnono.re.mapper.system.ReUserRoleMapper;
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
@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
public class ReUserRoleService {

    @Resource
    private ReUserRoleMapper reUserRoleMapper;

    //*********************************** 接口方法 ***********************************//

    //*********************************** 私有方法 ***********************************//

    //*********************************** 公有方法 ***********************************//

    /**
     * 通过用户id获取角色id
     *
     * @param userId 用户id
     * @return Integer 角色id
     */
    @Transactional(readOnly = true)
    public Integer getReRoleIdByReUserId(Integer userId) {
        ReUserRole userRole = reUserRoleMapper.selectOne(new LambdaQueryWrapper<ReUserRole>()
                .select(ReUserRole::getId, ReUserRole::getRoleId)
                .eq(ReUserRole::getUserId, userId));
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
                .orElseThrow(() -> new ParamException(ReErrorEnum.USER_ID_NULL_ERROR));
        Optional.ofNullable(roleId)
                .orElseThrow(() -> new ParamException(ReErrorEnum.ROLE_ID_NULL_ERROR));
        int update = reUserRoleMapper.update(null, new LambdaUpdateWrapper<ReUserRole>()
                .set(ReUserRole::getRoleId, roleId)
                .eq(ReUserRole::getUserId, userId));
        if (update <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    //*********************************** 其他方法 ***********************************//

}
