package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReLinkTypeSaveDTO;
import cn.ljtnono.re.dto.ReLinkTypeUpdateDTO;
import cn.ljtnono.re.entity.ReLinkType;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.service.IReLinkTypeService;
import cn.ljtnono.re.vo.JsonResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 链接类型controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/link_type")
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
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReLinkTypeUpdateDTO reLinkTypeUpdateDTO) {
        ReLinkType reLinkType = new ReLinkType();
        reLinkType.setName(reLinkTypeUpdateDTO.getName());
        reLinkType.setStatus((byte) 1);
        return iReLinkTypeService.updateEntityById(id, reLinkType);
    }

    @DeleteMapping("/{id:\\d+}")
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
        if (null == name) {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR);
        }
        return iReLinkTypeService.search(name, pageDTO);
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的链接类型", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReLinkTypeService.restore(id);
    }


}
