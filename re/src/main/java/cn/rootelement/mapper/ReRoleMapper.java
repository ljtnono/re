package cn.rootelement.mapper;

import cn.rootelement.entity.RePermission;
import cn.rootelement.entity.ReRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 角色mapper
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.2
 *
 */
public interface ReRoleMapper extends BaseMapper<ReRole> {


    /**
     * 根据角色id获取角色的权限列表
     * @param id 角色id
     * @return 角色权限列表
     */
    @Select("SELECT re_permission.* " +
            "FROM re_role_permission " +
            "LEFT JOIN re_role ON re_role_permission.role_id = re_role.id " +
            "LEFT JOIN re_permission ON re_role_permission.permission_id = re_permission.id " +
            "WHERE re_role.id = #{roleId}")
    List<RePermission> listPermissionByRoleId(@Param("roleId") Integer id);

    /**
     * 在删除角色的时候删除关联表的数据
     * @param roleId 角色id
     * @return 删除成功返回true，删除失败返回false
     */
    @Update("UPDATE re_user_role SET status = 0 WHERE role_id = #{roleId}")
    boolean deleteUserRole(@Param("roleId") Integer roleId);

    /**
     * 恢复删除的用户角色关联表数据
     * @param roleId 角色id
     * @return 恢复成功返回true，恢复失败返回false
     */
    @Update("UPDATE re_user_role SET status = 1 WHERE role_id = #{roleId}")
    boolean restoreUserRole(@Param("roleId") Integer roleId);

}
