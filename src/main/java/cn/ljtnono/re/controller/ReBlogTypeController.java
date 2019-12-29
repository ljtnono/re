package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReBlogType;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReBlogTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.ljtnono.re.controller.common.AbstractReController;

import java.io.Serializable;

/**
 * 博客类型Controller
 * @author ljt
 * @date 2019/12/23
 * @version 1.0.1
 */
@RestController
@RequestMapping("/blog_type")
@Slf4j
public class ReBlogTypeController extends AbstractReController<ReBlogType> {

    private IReBlogTypeService iReBlogTypeService;

    @Autowired
    public ReBlogTypeController(IReBlogTypeService iReBlogTypeService) {
        this.iReBlogTypeService = iReBlogTypeService;
    }

    @Override
    @GetMapping
    public JsonResult listEntityAll() {
        return iReBlogTypeService.listEntityAll();
    }


    @Override
    @PostMapping
    public JsonResult saveEntity(ReBlogType entity) {
        return iReBlogTypeService.saveEntity(entity);
    }


    @Override
    @PostMapping("/{id:\\d}")
    public JsonResult updateEntityById(@PathVariable(value = "id", required = false) Serializable id, ReBlogType entity) {
        return iReBlogTypeService.updateEntityById(id, entity);
    }


    @Override
    @DeleteMapping("/{id:\\d}")
    public JsonResult deleteEntityById(@PathVariable(value = "id", required = false) Serializable id) {
        return iReBlogTypeService.deleteEntityById(id);
    }


    @Override
    @GetMapping("/{id:\\d}")
    public JsonResult getEntityById(@PathVariable(value = "id", required = false) Serializable id) {
        return iReBlogTypeService.getEntityById(id);
    }


    @GetMapping("/listBlogTypePage")
    public JsonResult listBlogTypePage(PageDTO pageDTO) {
        return iReBlogTypeService.listBlogTypePage(pageDTO.getPage(), pageDTO.getCount());
    }

}
