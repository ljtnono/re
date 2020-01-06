package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReBlogSaveDTO;
import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.enumeration.GlobalVariableEnum;
import cn.ljtnono.re.util.BlogIndexUtil;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.util.HtmlUtil;
import cn.ljtnono.re.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * 博客Controller
 *
 * @author ljt
 * @version 1.0.3
 * @date 2019/12/29
 */
@RestController
@RequestMapping("/blog")
@Api(value = "博客相关Controller", tags = {"博客操作接口"})
@Slf4j
public class ReBlogController {

    private final IReBlogService iReBlogService;

    @Autowired
    public ReBlogController(IReBlogService iReBlogService) {
        this.iReBlogService = iReBlogService;
    }

    @GetMapping
    @ApiOperation(value = "获取全部博客信息列表", httpMethod = "GET")
    public JsonResult listEntityAll() {
        return iReBlogService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个博客记录", httpMethod = "POST")
    public JsonResult saveEntityByDTO(@Validated ReBlogSaveDTO reBlogSaveDTO) {
        ReBlog build = ReBlog.newBuilder()
                .title(reBlogSaveDTO.getTitle())
                .author(reBlogSaveDTO.getAuthor())
                .type(reBlogSaveDTO.getType())
                .contentHtml(reBlogSaveDTO.getContentHtml())
                .contentMarkdown(reBlogSaveDTO.getContentMarkdown())
                .coverImage(reBlogSaveDTO.getCoverImage())
                .status((byte) 1)
                .createTime(new Date())
                .modifyTime(new Date())
                .view(0)
                .comment(0)
                .build();
        // 设置封面图片，如果没有那么就设置为默认封面图片url
        if (StringUtil.isEmpty(build.getCoverImage())) {
            build.setCoverImage(GlobalVariableEnum.RE_IMAGE_DEFAULT_URL.getValue().toString());
        }
        // 设置
        if (StringUtil.isEmpty(build.getSummary())) {
            String deleteHtml = HtmlUtil.delHtmlTagFromStr(build.getContentHtml());
            if (deleteHtml.length() <= 300 && deleteHtml.length() >= 4) {
                build.setSummary(deleteHtml);
            } else {
                build.setSummary(build.getSummary());
            }
        }
        // 创建博文之后 在本地进行分词
        BlogIndexUtil blogIndexUtil = BlogIndexUtil.getInstance();
        // TODO 处理异常，设置applicationContext中的内容更新
        try {
            blogIndexUtil.addIndex(build);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("新发表博客请求参数" + reBlogSaveDTO.toString());
        JsonResult jsonResult = iReBlogService.saveEntity(build);
        log.info("新发表博客返回参数：" + jsonResult);
        return jsonResult;
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新一个博客实体类", notes = "id只能是数字类型", httpMethod = "PUT")
    public JsonResult updateEntityById(@PathVariable(value = "id") Serializable id, ReBlog entity) {
        return iReBlogService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除一个博客实体类", notes = "id只能是数字类型", httpMethod = "DELETE")
    public JsonResult deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReBlogService.deleteEntityById(id);
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取一个博客实体类", notes = "id只能是数字类型", httpMethod = "GET")
    public JsonResult getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReBlogService.getEntityById(id);
    }

    @GetMapping("/listBlogPage")
    @ApiOperation(value = "分页获取博客列表", httpMethod = "GET")
    public JsonResult listBlogPage(PageDTO reBlogListPageDTO) {
        return iReBlogService.listBlogPage(reBlogListPageDTO.getPage(), reBlogListPageDTO.getCount());
    }
}
