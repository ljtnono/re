package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.dto.system.RoleDTO;
import cn.ljtnono.re.service.system.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ling, Jiatong
 * Date 2020/7/16 1:19 上午
 * Description: 角色接口
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 根据id获取角色信息
     * @param id 角色id
     * @author Ling, Jiatong
     *
     */
    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    public ReJsonResultVO<?> getRoleById(@PathVariable Integer id) {
        log.info("[re-system -> ReRoleController -> getRoleById()] 获取角色信息，角色id：{}", id);
        return ReJsonResultVO.success(roleService.getRoleById(id));
    }

    /**
     * 获取角色下拉列表
     * @author Ling, Jiatong
     *
     */
    @GetMapping("/select")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    public ReJsonResultVO<?> select() {
        log.info("[re-system -> ReRoleController -> select()] 获取角色下拉列表");
        return ReJsonResultVO.success(roleService.select());
    }

    /**
     * 新增角色
     * @param reRoleDTO 参数封装
     * @author Ling, Jiatong
     *
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:role:add')")
    public ReJsonResultVO<?> addRole(@RequestBody RoleDTO reRoleDTO) {
        log.info("[re-system -> ReRoleController -> addRole()] 新增角色，参数：{}", reRoleDTO);
        roleService.addRole(reRoleDTO);
        return ReJsonResultVO.success();
    }

    /**
     * 分页获取角色列表
     * @param reRoleDTO 参数封装
     * @author Ling, Jiatong
     *
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    public ReJsonResultVO<?> getRoleListPage(RoleDTO reRoleDTO) {
        log.info("[re-system -> ReRoleController -> getRoleListPage()] 分页获取角色列表，参数：{}", reRoleDTO);
        return ReJsonResultVO.success(roleService.getRoleListPage(reRoleDTO));
    }

    /**
     * 根据id逻辑删除
     * @param id 角色id
     * @author Ling, Jiatong
     *
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    public ReJsonResultVO<?> logicDeleteById(@PathVariable Integer id) {
        log.info("[re-system -> ReRoleController -> logicDeleteById()] 逻辑删除角色，角色id：{}", id);
        roleService.logicDeleteById(id);
        return ReJsonResultVO.success();
    }

    /**
     * 更新角色信息
     * @param reRoleDTO 参数封装
     * @author Ling, Jiatong
     *
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:role:update')")
    public ReJsonResultVO<?> updateRole(@RequestBody RoleDTO reRoleDTO) {
        log.info("[re-system -> ReRoleController -> updateRole()] 更新角色，参数：{}", reRoleDTO);
        roleService.updateRole(reRoleDTO);
        return ReJsonResultVO.success();
    }

}
