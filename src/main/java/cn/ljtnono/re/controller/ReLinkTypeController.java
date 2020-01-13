package cn.ljtnono.re.controller;


import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReLinkType;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReLinkTypeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 链接类型controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/link_type")
@Slf4j
public class ReLinkTypeController {

    private final IReLinkTypeService iReLinkTypeService;

    @Autowired
    public ReLinkTypeController(IReLinkTypeService iReLinkTypeService) {
        this.iReLinkTypeService = iReLinkTypeService;
    }

    @GetMapping
    @ApiOperation(value = "获取全部链接列表信息", httpMethod = "GET")
    public JsonResult listEntityAll() {
        return iReLinkTypeService.listEntityAll();
    }

    public JsonResult saveEntity(ReLinkType entity) {
        return null;
    }

    public JsonResult updateEntityById(Serializable id, ReLinkType entity) {
        return null;
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除链接类型", httpMethod = "DELETE")
    public JsonResult deleteEntityById(@PathVariable("id") Serializable id) {
        return iReLinkTypeService.deleteEntityById(id);
    }

    public JsonResult getEntityById(Serializable id) {
        return null;
    }

    @GetMapping("/listLinkTypePage")
    @ApiOperation(value = "分页查询链接类型", httpMethod = "GET")
    public JsonResult listLinkTypePage(@Validated PageDTO pageDTO) {
        return iReLinkTypeService.listLinkTypePage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接类型名字模糊查询", notes = "根据链接类型名模糊查询", httpMethod = "POST")
    public JsonResult search(final String name, @Validated PageDTO pageDTO) {
        if (null == name) {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR);
        }
        return iReLinkTypeService.search(name, pageDTO);
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的链接类型", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResult restore(@PathVariable(value = "id") Serializable id) {
        return iReLinkTypeService.restore(id);
    }


}
