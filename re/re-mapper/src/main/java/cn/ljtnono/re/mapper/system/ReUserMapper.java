package cn.ljtnono.re.mapper.system;

import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.RePermission;
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
     * 获取权限列表
     * @param reUserDTO 参数封装
     * @return List<RePermission>
     */
    List<RePermission> getPermissionList(@Param("reUserDTO")ReUserDTO reUserDTO);

    /**
     * 获取用户
     * @param reUserDTO 参数封装
     * @return ReUser
     */
    ReUser getUser(@Param("reUserDTO") ReUserDTO reUserDTO);
}
