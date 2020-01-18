package cn.ljtnono.re.mapper;

import cn.ljtnono.re.entity.ReRole;
import cn.ljtnono.re.entity.ReUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * @return JsonResult对象
     */
    @Update("UPDATE re_user_role SET status = 0 WHERE user_id = #{userId}")
    boolean deleteUserRole(@Param("userId") Integer userId);

    /**
     * 恢复数据库相关表中的记录
     * @param userId 用户id
     * @return JsonResult对象
     */
    @Update("UPDATE re_user_role SET status = 1 WHERE user_id = #{userId}")
    boolean restoreUserRole(@Param("userId") Integer userId);
}
