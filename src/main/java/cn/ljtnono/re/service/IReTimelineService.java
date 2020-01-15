package cn.ljtnono.re.service;

import cn.ljtnono.re.entity.ReTimeline;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.common.IReEntityService;
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
    JsonResult listTimelinePage(Integer page, Integer count);

    /**
     * 恢复删除的时间轴
     * @param id 需要恢复的时间轴id
     * @return JsonResult 对象
     */
    JsonResult restore(Serializable id);
}
