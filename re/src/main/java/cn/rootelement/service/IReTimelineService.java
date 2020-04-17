package cn.rootelement.service;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReTimelineSearchDTO;
import cn.rootelement.entity.ReTimeline;
import cn.rootelement.service.common.IReEntityService;
import cn.rootelement.vo.JsonResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * 时间轴服务接口
 * @author ljt
 * @date 2020/1/15
 * @version 1.0.1
 */
public interface IReTimelineService extends IService<ReTimeline>, IReEntityService<ReTimeline> {


    /**
     * 无条件分页查询timeline列表
     * @param page 页数
     * @param count 每页条数
     * @return JsonResult 对象
     */
    JsonResultVO listTimelinePage(Integer page, Integer count);

    /**
     * 恢复删除的时间轴
     * @param id 需要恢复的时间轴id
     * @return JsonResult 对象
     */
    JsonResultVO restore(Serializable id);

    /**
     * 获取博客更新日志时间轴
     * @return JsonResult对象
     */
    JsonResultVO listUpdateLogTimeline();

    /**
     * 根据content和pushDate模糊查询列表
     * @param reTimelineSearchDTO 查询条件DTO
     * @param pageDTO 分页DTO
     * @return JsonResult对象
     */
    JsonResultVO search(ReTimelineSearchDTO reTimelineSearchDTO, PageDTO pageDTO);
}
