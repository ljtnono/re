package cn.rootelement.controller;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReBlogTypeSaveDTO;
import cn.rootelement.dto.ReBlogTypeUpdateDTO;
import cn.rootelement.entity.ReBlogType;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.service.IReBlogTypeService;
import cn.rootelement.vo.JsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * 博客类型Controller
 *
 * @author ljt
 * @version 1.0.2
 * @date 2020/1/6
 */
@RestController
@RequestMapping("/blog_type")
@Api(value = "ReBlogTypeController", tags = {"博客类型接口"})
public class ReBlogTypeController {

    private IReBlogTypeService iReBlogTypeService;

    @Autowired
    public ReBlogTypeController(IReBlogTypeService iReBlogTypeService) {
        this.iReBlogTypeService = iReBlogTypeService;
    }

    @GetMapping
    @ApiOperation(value = "获取所有博客类型列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReBlogTypeService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个博客类型", httpMethod = "POST")
    public JsonResultVO saveEntity(@Validated ReBlogTypeSaveDTO reBlogTypeSaveDTO) {
        ReBlogType entity = new ReBlogType();
        BeanUtils.copyProperties(reBlogTypeSaveDTO, entity);
        entity.setStatus((byte) 1);
        entity.setCreateTime(new Date());
        entity.setModifyTime(new Date());
        return iReBlogTypeService.saveEntity(entity);
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新博客类型", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReBlogTypeUpdateDTO reBlogTypeUpdateDTO) {
        ReBlogType reBlogType = new ReBlogType();
        BeanUtils.copyProperties(reBlogTypeUpdateDTO, reBlogType);
        // 这里如果不设置为1，会默认设置status为0
        reBlogType.setStatus((byte) 1);
        return iReBlogTypeService.updateEntityById(id, reBlogType);
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除一个博客类型", notes = "id只能为数字类型", httpMethod = "DELETE")
    public JsonResultVO deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReBlogTypeService.deleteEntityById(id);
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取博客类型", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReBlogTypeService.getEntityById(id);
    }

    @GetMapping("/listBlogTypePage")
    @ApiOperation(value = "分页获取博客类型列表", httpMethod = "GET")
    public JsonResultVO listBlogTypePage(PageDTO pageDTO) {
        return iReBlogTypeService.listBlogTypePage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的博客类型", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReBlogTypeService.restore(id);
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据博客类型名字模糊查询", notes = "根据博客类型名模糊查询", httpMethod = "POST")
    public JsonResultVO search(final String name, @Validated PageDTO pageDTO) {
        Optional.ofNullable(name)
                .orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        return iReBlogTypeService.search(name, pageDTO);
    }
}
