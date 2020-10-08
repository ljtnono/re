package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.service.system.ReRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ling, Jiatong
 * Date 2020/7/16 1:19 上午
 * Description: 角色接口
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/role")
public class ReRoleController {

    @Autowired
    private ReRoleService reRoleService;


}
