package cn.rootelement.service;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReRoleSearchDTO;
import cn.rootelement.entity.RePermission;
import cn.rootelement.entity.ReRole;
import cn.rootelement.service.common.IReEntityService;
import cn.rootelement.vo.JsonResultVO;
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
    JsonResultVO listRolePage(Integer page, Integer count);

    /**
     * 恢复角色
     * @param id 角色的id
     * @return JsonResult对象
     */
    JsonResultVO restore(Serializable id);

    /**
     * 根据角色name和角色description字段模糊查询
     * @param reRoleSearchDTO 查血天骄DTO
     * @param pageDTO 分页DTO
     * @return JsonResult对象
     */
    JsonResultVO search(ReRoleSearchDTO reRoleSearchDTO, PageDTO pageDTO);
}
