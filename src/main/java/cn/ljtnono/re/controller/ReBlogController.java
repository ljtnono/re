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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
@Slf4j
public class ReBlogController {

    private final IReBlogService iReBlogService;

    @Autowired
    public ReBlogController(IReBlogService iReBlogService) {
        this.iReBlogService = iReBlogService;
    }

    @GetMapping
    @ApiOperation(value = "/blog/", notes = "获取全部博客信息列表", httpMethod = "GET")
    public JsonResult listEntityAll() {
        return iReBlogService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "/blog/", notes = "新增一个博客实体", httpMethod = "POST")
    public JsonResult saveEntityByDTO(@Validated ReBlogSaveDTO reBlogSaveDTO) throws Exception {
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
        if (StringUtil.isEmpty(build.getCoverImage())) {
            build.setCoverImage(GlobalVariableEnum.RE_IMAGE_DEFAULT_URL.getValue().toString());
        }
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
        blogIndexUtil.addIndex(build);

        log.info("新发表博客请求参数" + reBlogSaveDTO.toString());
        JsonResult jsonResult = iReBlogService.saveEntity(build);
        log.info("新发表博客返回参数：" + jsonResult);
        return jsonResult;
    }

    @PutMapping("/{id:\\d+}")
    public JsonResult updateEntityById(@PathVariable(value = "id", required = false) Serializable id, ReBlog entity) {
        // TODO 这里调用entity本身实现的参数校验
        return iReBlogService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    public JsonResult deleteEntityById(@PathVariable(value = "id", required = false) Serializable id) {
        return iReBlogService.deleteEntityById(id);
    }

    @GetMapping("/{id:\\d+}")
    public JsonResult getEntityById(@PathVariable(value = "id", required = false) Serializable id) {
        return iReBlogService.getEntityById(id);
    }

    @GetMapping("/listBlogPage")
    public JsonResult listBlogPage(PageDTO reBlogListPageDTO) {
        return iReBlogService.listBlogPage(reBlogListPageDTO.getPage(), reBlogListPageDTO.getCount());
    }




}
