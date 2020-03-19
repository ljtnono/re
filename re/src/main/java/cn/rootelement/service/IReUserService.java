package cn.rootelement.service;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReUserSearchDTO;
import cn.rootelement.entity.ReRole;
import cn.rootelement.entity.ReUser;
import cn.rootelement.service.common.IReEntityService;
import cn.rootelement.vo.JsonResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 用户服务类接口
 * @author ljt
 * @date 2020/1/18
 * @version 1.0。2
 */
public interface IReUserService extends IService<ReUser>, IReEntityService<ReUser> {

    /**
     * 根据用户ID查询用户角色列表
     * @param userId 用户id
     * @return 用户角色列表
     */
    List<ReRole> listRoleByUserId(Integer userId);

    /**
     * 分页获取用户列表
     * @param page 页数
     * @param count 每页条数
     * @return JsonResult对象
     */
    JsonResultVO listUserPage(Integer page, Integer count);

    /**
     * 恢复删除的用户
     * @param id 用户id
     * @return JsonResult对象
     */
    JsonResultVO restore(Serializable id);

    /**
     * 根据username、tel和email模糊查询
     * @param reUserSearchDTO 查询条件DTO
     * @param pageDTO 分页DTO
     * @return JsonResult对象
     */
    JsonResultVO search(ReUserSearchDTO reUserSearchDTO, PageDTO pageDTO);
}
