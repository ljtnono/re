package cn.ljtnono.re.mapper.system;

import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author ljt
 * Date: 2020/7/14 0:28 上午
 * Description: 用户Mapper
 */
public interface ReUserMapper extends BaseMapper<ReUser> {

    /**
     * 根据用户id获取用户所有权限表达式列表
     * @param userId 用户id
     * @return List<RePermission> 权限表达式列表
     */
    List<RePermission> getPermissionExpressionListByUserId(Integer userId);
}
