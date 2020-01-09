package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReBlogType;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReBlogTypeService;
import cn.ljtnono.re.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.Serializable;

/**
 * 博客类型Controller
 *
 * @author ljt
 * @version 1.0.2
 * @date 2020/1/6
 */
@RestController
@RequestMapping("/blog_type")
@Slf4j
@Api(value = "博客分类类型Controller", tags = {"博客类型Controller"})
public class ReBlogTypeController {

    private IReBlogTypeService iReBlogTypeService;

    @Autowired
    public ReBlogTypeController(IReBlogTypeService iReBlogTypeService) {
        this.iReBlogTypeService = iReBlogTypeService;
    }

    @GetMapping
    @ApiOperation(value = "获取所有博客类型列表", httpMethod = "GET")
    public JsonResult listEntityAll() {
        return iReBlogTypeService.listEntityAll();
    }

    @PostMapping
    public JsonResult saveEntity(ReBlogType entity) {
        return iReBlogTypeService.saveEntity(entity);
    }

    @PutMapping("/{id:\\d+}")
    public JsonResult updateEntityById(@PathVariable(value = "id") Serializable id, ReBlogType entity) {
        return iReBlogTypeService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除一个博客类型", notes = "id只能为数字类型", httpMethod = "DELETE")
    public JsonResult deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReBlogTypeService.deleteEntityById(id);
    }

    @GetMapping("/{id:\\d+}")
    public JsonResult getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReBlogTypeService.getEntityById(id);
    }

    @GetMapping("/listBlogTypePage")
    public JsonResult listBlogTypePage(PageDTO pageDTO) {
        return iReBlogTypeService.listBlogTypePage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的博客类型", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResult restore(@PathVariable(value = "id") Serializable id) {
        return iReBlogTypeService.restore(id);
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据博客类型名字模糊查询", notes = "根据博客类型名模糊查询", httpMethod = "POST")
    public JsonResult search(final String name, PageDTO pageDTO) {
        if (null == name) {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR);
        }
        return iReBlogTypeService.search(name, pageDTO);
    }
}
