package cn.ljtnono.re.controller;


import cn.ljtnono.re.entity.ReLinkType;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.pojo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 链接类型controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/link_type")
@Slf4j
public class ReLinkTypeController {

    public JsonResult listEntityAll() {
        return null;
    }

    public JsonResult saveEntity(ReLinkType entity) {
        return null;
    }

    public JsonResult updateEntityById(Serializable id, ReLinkType entity) {
        return null;
    }

    public JsonResult deleteEntityById(Serializable id) {
        return null;
    }

    public JsonResult getEntityById(Serializable id) {
        return null;
    }
}
