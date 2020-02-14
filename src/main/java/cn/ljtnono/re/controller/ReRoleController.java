package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReRoleSaveDTO;
import cn.ljtnono.re.dto.ReRoleSearchDTO;
import cn.ljtnono.re.dto.ReRoleUpdateDTO;
import cn.ljtnono.re.entity.ReRole;
import cn.ljtnono.re.service.IReRoleService;
import cn.ljtnono.re.vo.JsonResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色controller
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.1
 */
@RestController
@RequestMapping("/role")
public class ReRoleController {

    private IReRoleService iReRoleService;

    @Autowired
    public ReRoleController(IReRoleService iReRoleService) {
        this.iReRoleService = iReRoleService;
    }

    @GetMapping
    @ApiOperation(value = "获取所有角色列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReRoleService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个角色", httpMethod = "POST")
    public JsonResultVO saveEntity(ReRoleSaveDTO reRoleSaveDTO) {
        ReRole entity = new ReRole();
        BeanUtils.copyProperties(reRoleSaveDTO, entity);
        entity.setStatus((byte) 1);
        entity.setCreateTime(new Date());
        entity.setModifyTime(new Date());
        return iReRoleService.saveEntity(entity);
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新角色", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, ReRoleUpdateDTO reRoleUpdateDTO) {
        ReRole entity = new ReRole();
        BeanUtils.copyProperties(reRoleUpdateDTO, entity);
        entity.setStatus((byte) 1);
        return iReRoleService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除一个角色", httpMethod = "DELETE")
    public JsonResultVO deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReRoleService.deleteEntityById(id);
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的角色", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReRoleService.restore(id);
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取角色", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReRoleService.getEntityById(id);
    }


    @GetMapping("/listRolePage")
    @ApiOperation(value = "分页查询角色列表", httpMethod = "GET")
    public JsonResultVO listRolePage(@Validated PageDTO pageDTO) {
        return iReRoleService.listRolePage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接name和description模糊查询", notes = "根据链接name和description模糊查询", httpMethod = "POST")
    public JsonResultVO search(@Validated ReRoleSearchDTO reRoleSearchDTO, @Validated PageDTO pageDTO) {
        return iReRoleService.search(reRoleSearchDTO, pageDTO);
    }
}
