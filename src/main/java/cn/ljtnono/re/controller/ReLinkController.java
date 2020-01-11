package cn.ljtnono.re.controller;


import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReLink;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReLinkService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 链接controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/link")
@Slf4j
public class ReLinkController {

    private IReLinkService iReLinkService;

    @Autowired
    public ReLinkController(IReLinkService iReLinkService) {
        this.iReLinkService = iReLinkService;
    }

    public JsonResult saveEntity(ReLink entity) {
        return null;
    }

    public JsonResult updateEntityById(Serializable id, ReLink entity) {
        return null;
    }

    public JsonResult deleteEntityById(Serializable id) {
        return null;
    }

    public JsonResult getEntityById(Serializable id) {
        return null;
    }


    @GetMapping("/listLinkPage")
    @ApiOperation(value = "分页获取链接", httpMethod = "GET")
    public JsonResult listLinkPage(@Validated PageDTO pageDTO) {
        return null;
    }
}
