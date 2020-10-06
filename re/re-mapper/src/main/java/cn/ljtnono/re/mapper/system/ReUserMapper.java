package cn.ljtnono.re.mapper.system;

import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReRole;
import cn.ljtnono.re.entity.system.ReUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
    List<RePermission> getPermissionExpressionListByUserId(@Param("userId") Integer userId);

    /**
     * 插入用户角色表
     * @param reUserDTO 参数封装
     * @return 影响的行数
     */
    int insertUserRole(@Param("reUserDTO") ReUserDTO reUserDTO);

    /**
     * 根据用户id获取角色
     * @param id 用户id
     * @return ReRole
     */
    ReRole getRoleById(@Param("id") Integer id);
}
