package cn.ljtnono.re.service;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReRoleSearchDTO;
import cn.ljtnono.re.entity.RePermission;
import cn.ljtnono.re.entity.ReRole;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.common.IReEntityService;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 角色服务接口
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.1
 */
public interface IReRoleService extends IService<ReRole>, IReEntityService<ReRole> {

    /**
     * 根据角色id获取权限列表
     * @param roleId 角色id
     * @return 角色代表的权限id
     */
    List<RePermission> listPermissionByRoleId(@Param("roleId") Integer roleId);

    /**
     * 分页查询角色列表
     * @param page 分页页码
     * @param count 每页查询的条数
     * @return JsonResult对象
     */
    JsonResult listRolePage(Integer page, Integer count);

    /**
     * 恢复角色
     * @param id 角色的id
     * @return JsonResult对象
     */
    JsonResult restore(Serializable id);

    /**
     * 根据角色name和角色description字段模糊查询
     * @param reRoleSearchDTO 查血天骄DTO
     * @param pageDTO 分页DTO
     * @return JsonResult对象
     */
    JsonResult search(ReRoleSearchDTO reRoleSearchDTO, PageDTO pageDTO);
}
