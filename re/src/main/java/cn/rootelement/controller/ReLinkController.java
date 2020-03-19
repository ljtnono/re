package cn.rootelement.controller;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReLinkSaveDTO;
import cn.rootelement.dto.ReLinkSearchDTO;
import cn.rootelement.dto.ReLinkUpdateDTO;
import cn.rootelement.entity.ReLink;
import cn.rootelement.service.IReLinkService;
import cn.rootelement.vo.JsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 链接controller
 * @author ljt
 * @date 2020/1/20
 * @version 1.0.1
 */
@RestController
@RequestMapping("/link")
@Api(value = "ReLinkController", tags = {"链接接口"})
public class ReLinkController {

    private IReLinkService iReLinkService;

    @Autowired
    public ReLinkController(IReLinkService iReLinkService) {
        this.iReLinkService = iReLinkService;
    }

    @GetMapping
    @ApiOperation(value = "获取所有链接", httpMethod = "GET")
    public JsonResultVO listLinkAll() {
        return iReLinkService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个链接类型", httpMethod = "POST")
    public JsonResultVO saveEntity(@Validated ReLinkSaveDTO reLinkSaveDTO) {
        ReLink reLink = new ReLink();
        BeanUtils.copyProperties(reLinkSaveDTO, reLink);
        reLink.setStatus((byte) 1);
        reLink.setCreateTime(new Date());
        reLink.setModifyTime(new Date());
        return iReLinkService.saveEntity(reLink);
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新链接", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReLinkUpdateDTO reLinkUpdateDTO) {
        ReLink reLink = new ReLink();
        BeanUtils.copyProperties(reLinkUpdateDTO, reLink);
        reLink.setStatus((byte) 1);
        return iReLinkService.updateEntityById(id, reLink);
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除一个链接", notes = "id只能为数字类型", httpMethod = "DELETE")
    public JsonResultVO deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReLinkService.deleteEntityById(id);
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取一个链接", notes = "id只能为数字类型", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReLinkService.getEntityById(id);
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的链接", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReLinkService.restore(id);
    }

    @GetMapping("/listLinkPage")
    @ApiOperation(value = "分页获取链接", httpMethod = "GET")
    public JsonResultVO listLinkPage(@Validated PageDTO pageDTO) {
        return iReLinkService.listLinkPage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接name和url还有type模糊查询", notes = "根据链接name和url还有type模糊查询", httpMethod = "POST")
    public JsonResultVO search(ReLinkSearchDTO reLinkSearchDTO, @Validated PageDTO pageDTO) {
        return iReLinkService.search(reLinkSearchDTO, pageDTO);
    }
}
