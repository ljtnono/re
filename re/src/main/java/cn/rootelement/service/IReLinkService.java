package cn.rootelement.service;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReLinkSearchDTO;
import cn.rootelement.entity.ReLink;
import cn.rootelement.service.common.IReEntityService;
import cn.rootelement.vo.JsonResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 链接服务接口
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
public interface IReLinkService extends IService<ReLink>, IReEntityService<ReLink> {

    /**
     * 获取所有外部链接数据
     * @return 所有外部链接数据
     */
    List<ReLink> listOutLinkAll();

    /**
     * 分页获取链接列表
     * @param page 页数
     * @param count 每页获取的条数
     * @return JsonResult对象
     */
    JsonResultVO listLinkPage(Integer page, Integer count);

    /**
     * 恢复删除的链接
     * @param id 需要恢复的链接id
     * @return JsonResult 对象
     */
    JsonResultVO restore(Serializable id);

    /**
     * 链接分页条件查询
     * @param reLinkSearchDTO 条件查询条件DTO
     * @param pageDTO 分页对象
     * @return JsonResult 对象
     */
    JsonResultVO search(final ReLinkSearchDTO reLinkSearchDTO, PageDTO pageDTO);
}
