package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.system.role.RoleDTO;
import cn.ljtnono.re.dto.system.role.RoleListQueryDTO;
import cn.ljtnono.re.service.system.RoleService;
import cn.ljtnono.re.vo.system.role.RoleListVO;
import cn.ljtnono.re.vo.system.role.RoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色模块接口
 *
 * @author Ling, Jiatong
 * Date 2020/7/16 1:19 上午
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/role")
@Api(value = "/api/v1/system/role", tags = "角色模块接口")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 获取角色下拉列表
     *
     * @return 角色VO对象列表
     * @author Ling, Jiatong
     */
    @GetMapping("/select")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    @ApiOperation("获取角色下拉列表")
    public JsonResultVO<List<RoleVO>> select() {
        log.info("[re-system -> ReRoleController -> select()] 获取角色下拉列表");
        return JsonResultVO.success(roleService.select());
    }

    /**
     * 根据角色id获取角色信息
     *
     * @param id 角色id
     * @return 角色VO对象
     * @author Ling, Jiatong
     */
    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    @ApiOperation("根据id获取角色信息")
    public JsonResultVO<RoleVO> getRoleById(@PathVariable Integer id) {
        log.info("[re-system -> ReRoleController -> getRoleById()] 获取角色信息，角色id：{}", id);
        return JsonResultVO.success(roleService.getRoleById(id));
    }

    /**
     * 新增角色
     *
     * @param dto 角色通用DTO对象
     * @return 通用返回消息对象
     * @author Ling, Jiatong
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:role:add')")
    @ApiOperation("新增角色")
    public JsonResultVO<?> addRole(@RequestBody @ApiParam RoleDTO dto) {
        log.info("[re-system -> ReRoleController -> addRole()] 新增角色，参数：{}", dto);
        roleService.addRole(dto);
        return JsonResultVO.success();
    }

    /**
     * 分页获取角色列表
     *
     * @param dto 角色DTO对象
     * @return 角色ListVO对象
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('system:role:view')")
    @ApiOperation("分页获取角色列表")
    public JsonResultVO<IPage<RoleListVO>> getList(@ApiParam RoleListQueryDTO dto) {
        log.info("[re-system -> ReRoleController -> getRoleListPage()] 分页获取角色列表，参数：{}", dto);
        return JsonResultVO.success(roleService.getList(dto));
    }

    /**
     * 根据id列表逻辑删除角色
     *
     * @param dto 角色DTO对象
     * @return 通用返回消息对象
     * @author Ling, Jiatong
     */
    @DeleteMapping("/delete/logic")
    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    @ApiOperation("根据id逻辑删除角色")
    public JsonResultVO<?> logicDeleteById(@ApiParam RoleDTO dto) {
        log.info("[re-system -> ReRoleController -> logicDeleteById()] 逻辑删除角色，参数：{}", dto);
        roleService.logicDelete(dto);
        return JsonResultVO.success();
    }

    /**
     * 更新角色信息
     *
     * @param dto 参数封装
     * @return 通用角色返回对象
     * @author Ling, Jiatong
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:role:update')")
    @ApiOperation("更新角色信息")
    public JsonResultVO<?> updateRole(@RequestBody @ApiParam RoleDTO dto) {
        log.info("[re-system -> ReRoleController -> updateRole()] 更新角色，参数：{}", dto);
        roleService.updateRole(dto);
        return JsonResultVO.success();
    }
}
