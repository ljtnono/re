package cn.ljtnono.re.mapper.system;

import cn.ljtnono.re.dto.system.UserDTO;
import cn.ljtnono.re.dto.system.UserListQueryDTO;
import cn.ljtnono.re.entity.system.Permission;
import cn.ljtnono.re.entity.system.Role;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.vo.system.UserListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/14 0:28 上午
 * Description: 用户Mapper
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户id获取用户所有权限表达式列表
     * @param userId 用户id
     * @return List<RePermission> 权限表达式列表
     */
    List<Permission> getPermissionExpressionListByUserId(@Param("userId") Integer userId);

    /**
     * 插入用户角色表
     * @param reUserDTO 参数封装
     * @return 影响的行数
     */
    int insertUserRole(@Param("reUserDTO") UserDTO reUserDTO);

    /**
     * 根据用户id获取角色
     * @param id 用户id
     * @return ReRole
     */
    Role getRoleById(@Param("id") Integer id);

    /**
     * 分页获取用户列表
     * @param dto 参数封装
     * @param page 分页参数
     * @return IPage<UserListVO>
     * @author Ling, Jiatong
     *
     */
    IPage<UserListVO> getList(Page<User> page, @Param("dto") UserListQueryDTO dto);
}
