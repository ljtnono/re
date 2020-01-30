package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReLinkSaveDTO;
import cn.ljtnono.re.dto.ReLinkSearchDTO;
import cn.ljtnono.re.dto.ReLinkUpdateDTO;
import cn.ljtnono.re.entity.ReLink;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReLinkService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReLinkController {

    private IReLinkService iReLinkService;

    @Autowired
    public ReLinkController(IReLinkService iReLinkService) {
        this.iReLinkService = iReLinkService;
    }

    @PostMapping
    @ApiOperation(value = "新增一个链接类型", httpMethod = "POST")
    public JsonResult saveEntity(@Validated ReLinkSaveDTO reLinkSaveDTO) {
        ReLink reLink = new ReLink();
        BeanUtils.copyProperties(reLinkSaveDTO, reLink);
        reLink.setStatus((byte) 1);
        reLink.setCreateTime(new Date());
        reLink.setModifyTime(new Date());
        return iReLinkService.saveEntity(reLink);
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新链接", httpMethod = "PUT")
    public JsonResult updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReLinkUpdateDTO reLinkUpdateDTO) {
        ReLink reLink = new ReLink();
        BeanUtils.copyProperties(reLinkUpdateDTO, reLink);
        reLink.setStatus((byte) 1);
        return iReLinkService.updateEntityById(id, reLink);
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除一个链接", notes = "id只能为数字类型", httpMethod = "DELETE")
    public JsonResult deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReLinkService.deleteEntityById(id);
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取一个链接", notes = "id只能为数字类型", httpMethod = "GET")
    public JsonResult getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReLinkService.getEntityById(id);
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的链接", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResult restore(@PathVariable(value = "id") Serializable id) {
        return iReLinkService.restore(id);
    }

    @GetMapping("/listLinkPage")
    @ApiOperation(value = "分页获取链接", httpMethod = "GET")
    public JsonResult listLinkPage(@Validated PageDTO pageDTO) {
        return iReLinkService.listLinkPage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接name和url还有type模糊查询", notes = "根据链接name和url还有type模糊查询", httpMethod = "POST")
    public JsonResult search(ReLinkSearchDTO reLinkSearchDTO, @Validated PageDTO pageDTO) {
        return iReLinkService.search(reLinkSearchDTO, pageDTO);
    }
}
