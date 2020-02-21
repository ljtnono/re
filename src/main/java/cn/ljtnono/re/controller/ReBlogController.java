package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReBlogSaveDTO;
import cn.ljtnono.re.dto.ReBlogSearchDTO;
import cn.ljtnono.re.dto.ReBlogUpdateDTO;
import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.enumeration.GlobalVariableEnum;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.util.HtmlUtil;
import cn.ljtnono.re.util.SpringBeanUtil;
import cn.ljtnono.re.util.StringUtil;
import cn.ljtnono.re.vo.JsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class ReBlogController {

    private IReBlogService iReBlogService;

    private SpringBeanUtil springBeanUtil;

    @Autowired
    public ReBlogController(IReBlogService iReBlogService, SpringBeanUtil springBeanUtil) {
        this.iReBlogService = iReBlogService;
        this.springBeanUtil = springBeanUtil;
    }

    @GetMapping
    @ApiOperation(value = "获取全部博客信息列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReBlogService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个博客记录", httpMethod = "POST")
    public JsonResultVO saveEntity(@Validated ReBlogSaveDTO reBlogSaveDTO) {
        ReBlog entity = ReBlog.newBuilder()
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
        if (StringUtil.isEmpty(entity.getCoverImage())) {
            entity.setCoverImage(GlobalVariableEnum.RE_IMAGE_DEFAULT_URL.getValue().toString());
        }
        // 设置
        if (StringUtil.isEmpty(entity.getSummary())) {
            String deleteHtml = HtmlUtil.delHtmlTagFromStr(entity.getContentHtml());
            if (deleteHtml.length() <= 300 && deleteHtml.length() >= 4) {
                entity.setSummary(deleteHtml);
            } else {
                entity.setSummary(entity.getSummary());
            }
        }
        JsonResultVO jsonResultVO = iReBlogService.saveEntity(entity);
        ServletContext servletContext = springBeanUtil.getServletContext();
        List<ReBlog> listGuessYouLike = iReBlogService.listGuessYouLike();
        servletContext.setAttribute("guessYouLikeList", listGuessYouLike);
        return jsonResultVO;
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新一个博客实体", notes = "id只能是数字类型", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReBlogUpdateDTO reBlogUpdateDTO) {
        ReBlog entity = new ReBlog();
        BeanUtils.copyProperties(reBlogUpdateDTO, entity);
        // 设置封面图片，如果没有那么就设置为默认封面图片url
        if (StringUtil.isEmpty(entity.getCoverImage())) {
            entity.setCoverImage(GlobalVariableEnum.RE_IMAGE_DEFAULT_URL.getValue().toString());
        }
        // 设置
        if (StringUtil.isEmpty(entity.getSummary())) {
            String deleteHtml = HtmlUtil.delHtmlTagFromStr(entity.getContentHtml());
            if (deleteHtml.length() <= 300 && deleteHtml.length() >= 4) {
                entity.setSummary(deleteHtml);
            } else {
                entity.setSummary(entity.getSummary());
            }
        }
        return iReBlogService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id删除一个博客实体", notes = "id只能是数字类型", httpMethod = "DELETE")
    public JsonResultVO deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReBlogService.deleteEntityById(id);
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取一个博客实体", notes = "id只能是数字类型", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReBlogService.getEntityById(id);
    }

    @GetMapping("/listBlogPage")
    @ApiOperation(value = "分页获取博客列表", httpMethod = "GET")
    public JsonResultVO listBlogPage(PageDTO reBlogListPageDTO) {
        return iReBlogService.listBlogPage(reBlogListPageDTO.getPage(), reBlogListPageDTO.getCount());
    }

    @GetMapping("/listBlogPageByType")
    @ApiOperation(value = "根据类型分页查询博客列表", httpMethod = "GET")
    public JsonResultVO listBlogPageByType(String type, @Validated PageDTO pageDTO) {
        if (StringUtil.isEmpty(type) || "ALL".equals(type)) {
            return iReBlogService.listBlogPageByType(pageDTO.getPage(), pageDTO.getCount(), null);
        } else {
            return iReBlogService.listBlogPageByType(pageDTO.getPage(), pageDTO.getCount(), type);
        }
    }


    @PostMapping("/search")
    @ApiOperation(value = "根据博客标题，博客分类，博客作者模糊查询", notes = "根据博客标题，博客分类，博客作者模糊查询", httpMethod = "POST")
    public JsonResultVO search(ReBlogSearchDTO reBlogSearchDTO, @Validated PageDTO pageDTO) {
        if (reBlogSearchDTO == null) {
            reBlogSearchDTO = new ReBlogSearchDTO();
        }
        return iReBlogService.search(reBlogSearchDTO, pageDTO);
    }
}
