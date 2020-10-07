package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.service.system.ReRoleService;
import cn.ljtnono.re.vo.system.ReRoleVO;
import lombok.extern.slf4j.Slf4j;
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

    private final ReRoleService reRoleService;

    public ReRoleController(ReRoleService reRoleService) {
        this.reRoleService = reRoleService;
    }

    /**
     *
     * @return
     */
    public ReJsonResultVO<ReRoleVO> list() {

        return null;
    }

}
