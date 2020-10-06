package cn.ljtnono.re.service.system;

import cn.ljtnono.re.entity.system.ReUserRole;
import cn.ljtnono.re.mapper.ReUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author ljt
 * Date: 2020/10/6 21:44
 * Description: 用户角色Service类
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
public class ReUserRoleService {

    @Resource
    private ReUserRoleMapper reUserRoleMapper;

    /**
     * 通过用户id获取角色id
     *
     * @param userId 用户id
     * @return 角色id
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
}
