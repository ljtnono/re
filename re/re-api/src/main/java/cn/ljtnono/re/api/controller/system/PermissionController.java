package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.service.system.PermissionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限模块接口
 *
 * @author Ling, Jiatong
 * Date 2020/7/16 1:19 上午
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/permission")
@Api(value = "/api/v1/system/permission", tags = "权限模块接口")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


}
