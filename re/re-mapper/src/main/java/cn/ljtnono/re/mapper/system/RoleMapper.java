package cn.ljtnono.re.mapper.system;

import cn.ljtnono.re.dto.system.RoleDTO;
import cn.ljtnono.re.dto.system.RoleListQueryDTO;
import cn.ljtnono.re.entity.system.Role;
import cn.ljtnono.re.vo.system.RoleListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/13 23:16
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过用户id获取角色id和名字
     *
     * @param userId 用户id
     * @return ReRole 角色实体
     */
    Role getRoleIdAndNameByUserId(@Param("userId") Integer userId);

    /**
     * <p>分页获取角色列表</p>
     * @param page 分页参数 {@link Page}
     * @param dto 分页查询参数 {@link RoleListQueryDTO}
     * @return 角色列表VO对象列表 {@link RoleListVO}
     * @author Ling, Jiatong
     */
    IPage<RoleListVO> getList(Page<Role> page, @Param("dto") RoleListQueryDTO dto);
}
