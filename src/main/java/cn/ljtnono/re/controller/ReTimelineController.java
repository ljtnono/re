package cn.ljtnono.re.controller;

import cn.ljtnono.re.entity.ReTimeline;
import cn.ljtnono.re.pojo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 时间轴controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/timeline")
@Slf4j
public class ReTimelineController {

    public JsonResult listEntityAll() {
        return null;
    }

    public JsonResult saveEntity(ReTimeline entity) {
        return null;
    }

    public JsonResult updateEntityById(Serializable id, ReTimeline entity) {
        return null;
    }

    public JsonResult deleteEntityById(Serializable id) {
        return null;
    }

    public JsonResult getEntityById(Serializable id) {
        return null;
    }
}
