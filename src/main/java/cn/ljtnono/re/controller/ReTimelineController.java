package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.*;
import cn.ljtnono.re.entity.ReTimeline;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReTimelineService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 时间轴controller
 * @author ljt
 * @date 2020/1/15
 * @version 1.0.1
 */
@RestController
@RequestMapping("/timeline")
@Slf4j
public class ReTimelineController {

    private IReTimelineService iReTimelineService;

    @Autowired
    public ReTimelineController(IReTimelineService iReTimelineService) {
        this.iReTimelineService = iReTimelineService;
    }

    @GetMapping
    @ApiOperation(value = "查询所有时间轴列表", httpMethod = "GET")
    public JsonResult listEntityAll() {
        return iReTimelineService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个时间轴记录", httpMethod = "POST")
    public JsonResult saveEntity(ReTimelineSaveDTO reTimelineSaveDTO) {
        ReTimeline reTimeline = new ReTimeline();
        reTimeline.setStatus((byte) 1);
        BeanUtils.copyProperties(reTimelineSaveDTO, reTimeline);
        reTimeline.setCreateTime(new Date());
        reTimeline.setModifyTime(new Date());
        return iReTimelineService.saveEntity(reTimeline);
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新一个时间轴实体", notes = "id只能是数字类型", httpMethod = "PUT")
    public JsonResult updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReTimelineUpdateDTO reTimelineUpdateDTO) {
        ReTimeline reTimeline = new ReTimeline();
        BeanUtils.copyProperties(reTimelineUpdateDTO, reTimeline);
        reTimeline.setStatus((byte) 1);
        return iReTimelineService.updateEntityById(id, reTimeline);
    }
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除一个timeline记录", notes = "id只能为数字类型", httpMethod = "DELETE")
    public JsonResult deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReTimelineService.deleteEntityById(id);
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的时间轴", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResult restore(@PathVariable(value = "id") Serializable id) {
        return iReTimelineService.restore(id);
    }

    @GetMapping("/{id:\\d+}")
    public JsonResult getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReTimelineService.getEntityById(id);
    }

    @GetMapping("/listTimelinePage")
    @ApiOperation(value = "分页查询时间轴列表", httpMethod = "GET")
    public JsonResult listTimelinePage(@Validated PageDTO pageDTO) {
        return iReTimelineService.listTimelinePage(pageDTO.getPage(), pageDTO.getCount());
    }

    @GetMapping("/listUpdateLogTimeline")
    @ApiOperation(value = "获取博客更新日志时间轴", notes = "根据日期排序，获取前20条数据", httpMethod = "GET")
    public JsonResult listUpdateLogTimeline() {
        return iReTimelineService.listUpdateLogTimeline();
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接content和pushDate模糊查询", notes = "根据链接content和pushDate模糊查询", httpMethod = "POST")
    public JsonResult search(ReTimelineSearchDTO reTimelineSearchDTO, @Validated PageDTO pageDTO) {
        return iReTimelineService.search(reTimelineSearchDTO, pageDTO);
    }
}
