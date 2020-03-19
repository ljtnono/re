package cn.rootelement.mapper;

import cn.rootelement.entity.ReRole;
import cn.rootelement.entity.ReUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户mapper
 * @author ljt
 * @date 2020/1/18
 * @version 1.0.2
 */
public interface ReUserMapper extends BaseMapper<ReUser> {

    /**
     * 根据用户id获取用户的角色列表
     * @param userId 用户的id
     * @return 用户的角色列表
     */
    @Select("SELECT re_role.* " +
            "FROM re_user " +
            "LEFT JOIN re_user_role ON re_user_role.user_id = re_user.id " +
            "LEFT JOIN re_role ON re_user_role.role_id = re_role.id " +
            "WHERE re_user.id = #{userId}")
    List<ReRole> listRoleByUserId(@Param("userId") Integer userId);


    /**
     * 在用户删除时删除相关表的记录
     * @param userId 用户id
     * @return
     */
    @Update("UPDATE re_user_role SET status = 0 WHERE user_id = #{userId}")
    boolean deleteUserRole(@Param("userId") Integer userId);

    /**
     * 恢复数据库相关表中的记录
     * @param userId 用户id
     * @return
     */
    @Update("UPDATE re_user_role SET status = 1 WHERE user_id = #{userId}")
    boolean restoreUserRole(@Param("userId") Integer userId);


    /**
     * 新增用户的时候新增相关角色
     * @param userId 用户id
     * @param roleId 角色id
     * @return
     */
    @Insert("INSERT INTO re_user_role(user_id, role_id, create_time, modify_time, status) values(#{userId}, #{roleId}, NOW(), NOW(), 1)")
    boolean insertUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}
