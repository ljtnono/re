package cn.ljtnono.re.controller;


import cn.ljtnono.re.entity.ReLinkType;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReLinkTypeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final IReLinkTypeService iReLinkTypeService;

    @Autowired
    public ReLinkTypeController(IReLinkTypeService iReLinkTypeService) {
        this.iReLinkTypeService = iReLinkTypeService;
    }

    @GetMapping
    @ApiOperation(value = "获取全部链接列表信息", httpMethod = "GET")
    public JsonResult listEntityAll() {
        return iReLinkTypeService.listEntityAll();
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
