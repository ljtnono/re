package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.system.RoleDTO;
import cn.ljtnono.re.dto.system.RoleListQueryDTO;
import cn.ljtnono.re.service.system.RoleService;
import cn.ljtnono.re.vo.system.RoleListVO;
import cn.ljtnono.re.vo.system.RoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>角色接口</p>
 * @author Ling, Jiatong
 * Date 2020/7/16 1:19 上午
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/role")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * <p>获取角色下拉列表</p>
     * @return 角色VO对象列表 {@link RoleVO}
     * @author Ling, Jiatong
     */
    @GetMapping("/select")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    public JsonResultVO<List<RoleVO>> select() {
        log.info("[re-system -> ReRoleController -> select()] 获取角色下拉列表");
        return JsonResultVO.success(roleService.select());
    }

    /**
     * <p>根据角色id获取角色信息</p>
     * @param id 角色id
     * @return 角色VO对象 {@link RoleVO}
     * @author Ling, Jiatong
     */
    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    public JsonResultVO<RoleVO> getRoleById(@PathVariable Integer id) {
        log.info("[re-system -> ReRoleController -> getRoleById()] 获取角色信息，角色id：{}", id);
        return JsonResultVO.success(roleService.getRoleById(id));
    }

    /**
     * <p>新增角色</p>
     * @param dto 角色通用DTO对象 {@link RoleDTO}
     * @return 通用返回消息对象
     * @author Ling, Jiatong
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:role:add')")
    public JsonResultVO<?> addRole(@RequestBody RoleDTO dto) {
        log.info("[re-system -> ReRoleController -> addRole()] 新增角色，参数：{}", dto);
        roleService.addRole(dto);
        return JsonResultVO.success();
    }

    /**
     * <p>分页获取角色列表</p>
     * @param dto 角色DTO对象 {@link RoleListQueryDTO}
     * @return 角色ListVO对象 {@link RoleListVO}
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    public JsonResultVO<IPage<RoleListVO>> getList(RoleListQueryDTO dto) {
        log.info("[re-system -> ReRoleController -> getRoleListPage()] 分页获取角色列表，参数：{}", dto);
        return JsonResultVO.success(roleService.getList(dto));
    }

    /**
     * <p>根据id列表逻辑删除角色</p>
     * @param dto 角色DTO对象 {@link RoleDTO}
     * @return 通用返回消息对象
     * @author Ling, Jiatong
     */
    @DeleteMapping("/delete/logic")
    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    public JsonResultVO<?> logicDeleteById(RoleDTO dto) {
        log.info("[re-system -> ReRoleController -> logicDeleteById()] 逻辑删除角色，参数：{}", dto);
        roleService.logicDelete(dto);
        return JsonResultVO.success();
    }

    /**
     * <p>更新角色信息</p>
     * @param dto 参数封装
     * @return 通用角色返回对象
     * @author Ling, Jiatong
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:role:update')")
    public JsonResultVO<?> updateRole(@RequestBody RoleDTO dto) {
        log.info("[re-system -> ReRoleController -> updateRole()] 更新角色，参数：{}", dto);
        roleService.updateRole(dto);
        return JsonResultVO.success();
    }

}
