package cn.ljtnono.re.mapper.system;

import cn.ljtnono.re.entity.system.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/13 23:16
 * Description:
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过用户id获取角色id和名字
     *
     * @param userId 用户id
     * @return ReRole 角色实体
     */
    Role getRoleIdAndNameByUserId(@Param("userId") Integer userId);
}
