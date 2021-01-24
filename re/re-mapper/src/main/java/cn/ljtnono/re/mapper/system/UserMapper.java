package cn.ljtnono.re.mapper.system;

import cn.ljtnono.re.dto.system.user.UserListQueryDTO;
import cn.ljtnono.re.dto.system.userrole.UserRoleAddDTO;
import cn.ljtnono.re.entity.system.Permission;
import cn.ljtnono.re.entity.system.Role;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.vo.system.user.UserListVO;
import cn.ljtnono.re.vo.system.user.UserRoleNumPieVO;
import cn.ljtnono.re.vo.system.user.UserDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>用户mapper层</p>
 * @author Ling, Jiatong
 * Date: 2020/7/14 0:28 上午
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * <p>根据用户id获取用户所有权限列表（只包含expression字段）</p>
     * @param userId 用户id
     * @return 权限列表 {@link Permission}
     */
    List<Permission> getPermissionExpressionListByUserId(@Param("userId") Integer userId);

    /**
     * <p>插入用户角色表</p>
     * @param dto 参数封装
     * @return 影响的行数
     */
    int insertUserRole(@Param("dto") UserRoleAddDTO dto);

    /**
     * <p>根据用户id获取角色</p>
     * @param id 用户id
     * @return 角色对象 {@link Role}
     */
    Role getRoleById(@Param("id") Integer id);

    /**
     * <p>分页获取用户列表</p>
     * @param dto 用户分页查询列表DTO对象 {@link UserListQueryDTO}
     * @param page 分页参数 {@link Page}
     * @return 用户分页列表VO对象分页对象 {@link UserListVO}，{@link IPage}
     * @author Ling, Jiatong
     */
    IPage<UserListVO> getList(Page<?> page, @Param("dto") UserListQueryDTO dto);

    /**
     * <p>根据用户id获取用户信息<br/>
     * 此函数只获取一部分字段，不会获取以删除用户，但是会获取已经删除的角色信息</p>
     * @param id 用户id
     * @return 用户通用VO对象 {@link UserDetailVO}
     * @author Ling, Jiatong
     */
    UserDetailVO getUserById(@Param("id") Integer id);

    /**
     * <p>获取用户根据角色分类统计饼状图</p>
     * @return 用户根据角色分类统计饼状图VO对象列表 {@link UserRoleNumPieVO}
     * @author Ling, Jiatong
     */
    List<UserRoleNumPieVO> roleNumPie();
}
