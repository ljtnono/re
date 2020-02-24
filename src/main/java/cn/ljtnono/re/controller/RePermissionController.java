package cn.ljtnono.re.controller;


import cn.ljtnono.re.entity.RePermission;
import cn.ljtnono.re.vo.JsonResultVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 权限controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/permission")
@Api(value = "RePermissionController", tags = {"权限接口"})
public class RePermissionController {

    public JsonResultVO listEntityAll() {
        return null;
    }

    public JsonResultVO saveEntity(RePermission entity) {
        return null;
    }

    public JsonResultVO updateEntityById(Serializable id, RePermission entity) {
        return null;
    }

    public JsonResultVO deleteEntityById(Serializable id) {
        return null;
    }

    public JsonResultVO getEntityById(Serializable id) {
        return null;
    }
}
