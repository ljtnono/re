package cn.rootelement.controller;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReLinkTypeSaveDTO;
import cn.rootelement.dto.ReLinkTypeUpdateDTO;
import cn.rootelement.entity.ReLinkType;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.service.IReLinkTypeService;
import cn.rootelement.vo.JsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * 链接类型controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/link_type")
@Api(value = "ReLinkTypeController", tags = {"链接类型接口"})
public class ReLinkTypeController {

    private final IReLinkTypeService iReLinkTypeService;

    @Autowired
    public ReLinkTypeController(IReLinkTypeService iReLinkTypeService) {
        this.iReLinkTypeService = iReLinkTypeService;
    }

    @GetMapping
    @ApiOperation(value = "获取全部链接列表信息", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReLinkTypeService.listEntityAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "新增一个链接类型", httpMethod = "POST")
    public JsonResultVO saveEntity(@Validated ReLinkTypeSaveDTO reLinkTypeSaveDTO) {
        ReLinkType reLinkType = new ReLinkType();
        reLinkType.setStatus((byte) 1);
        reLinkType.setName(reLinkTypeSaveDTO.getName());
        reLinkType.setCreateTime(new Date());
        reLinkType.setModifyTime(new Date());
        return iReLinkTypeService.saveEntity(reLinkType);
    }

    @PutMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReLinkTypeUpdateDTO reLinkTypeUpdateDTO) {
        ReLinkType reLinkType = new ReLinkType();
        reLinkType.setName(reLinkTypeUpdateDTO.getName());
        reLinkType.setStatus((byte) 1);
        return iReLinkTypeService.updateEntityById(id, reLinkType);
    }

    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "根据id删除链接类型", httpMethod = "DELETE")
    public JsonResultVO deleteEntityById(@PathVariable("id") Serializable id) {
        return iReLinkTypeService.deleteEntityById(id);
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id查询链接类型实体", notes = "id只能为数字类型", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReLinkTypeService.getEntityById(id);
    }

    @GetMapping("/listLinkTypePage")
    @ApiOperation(value = "分页查询链接类型", httpMethod = "GET")
    public JsonResultVO listLinkTypePage(@Validated PageDTO pageDTO) {
        return iReLinkTypeService.listLinkTypePage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接类型名字模糊查询", notes = "根据链接类型名模糊查询", httpMethod = "POST")
    public JsonResultVO search(final String name, @Validated PageDTO pageDTO) {
        Optional.ofNullable(name)
                .orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        return iReLinkTypeService.search(name, pageDTO);
    }

    @PutMapping("/restore/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "恢复删除的链接类型", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReLinkTypeService.restore(id);
    }
}
