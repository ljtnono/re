package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.enumeration.GlobalVariableEnum;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.ljtnono.re.controller.common.AbstractReController;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客Controller
 *
 * @author ljt
 * @version 1.1
 * @date 2019/11/18
 */
@RestController
@RequestMapping("/blog")
@Api(value = "blog")
public class ReBlogController extends AbstractReController<ReBlog> {

    @Autowired
    private IReBlogService iReBlogService;

    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(ReBlogController.class);


    @Override
    @GetMapping
    @ApiOperation(value = "获取信息",notes = "获取信息1",httpMethod = "GET")
    public JsonResult listEntityAll() {
        return iReBlogService.listEntityAll();
    }

    @Override
    @PostMapping
    public JsonResult saveEntity(ReBlog entity) {
        // TODO 这里使用DTO参数，并且校验参数
        ReBlog build = ReBlog.newBuilder(entity)
                .status((byte) 1)
                .createTime(new Date())
                .modifyTime(new Date())
                .view(0)
                .comment(0)
                .build();
        // 设置博客封面图片
        if (build.getCoverImage() == null || build.getCoverImage().isEmpty()) {
            build.setCoverImage(GlobalVariableEnum.RE_IMAGE_DEFAULT_URL.getValue().toString());
        }
        // TODO 设置博客简介内容
        logger.info("新发表博客 entity = " + build.toString());
        return iReBlogService.saveEntity(build);
    }


    @Override
    @PutMapping("/{id}")
    public JsonResult updateEntityById(@PathVariable(value = "id", required = false) Serializable id, ReBlog entity) {
        // TODO 这里调用entity本身实现的参数校验
        return iReBlogService.updateEntityById(id, entity);
    }

    @Override
    @DeleteMapping("/{id}")
    public JsonResult deleteEntityById(@PathVariable(value = "id", required = false) Serializable id) {
        return iReBlogService.deleteEntityById(id);
    }

    @Override
    @GetMapping("/{id}")
    public JsonResult getEntityById(@PathVariable(value = "id", required = false) Serializable id) {
        return iReBlogService.getEntityById(id);
    }


    @GetMapping("/listBlogPage")
    public JsonResult listBlogPage(PageDTO reBlogListPageDTO) {
        return iReBlogService.listBlogPage(reBlogListPageDTO.getPage(), reBlogListPageDTO.getCount());
    }
}
