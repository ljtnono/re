package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReBlogSaveDTO;
import cn.ljtnono.re.dto.ReBlogSearchDTO;
import cn.ljtnono.re.dto.ReBlogUpdateDTO;
import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.util.HtmlUtil;
import cn.ljtnono.re.util.StringUtil;
import cn.ljtnono.re.vo.JsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(value = "ReBlogController", tags = {"博客接口"})
public class ReBlogController {

    private IReBlogService iReBlogService;

    @Autowired
    public ReBlogController(IReBlogService iReBlogService) {
        this.iReBlogService = iReBlogService;
    }

    @GetMapping
    @ApiOperation(value = "获取全部博客信息列表", httpMethod = "GET")
    @PreAuthorize("hasRole('admin')")
    public JsonResultVO listEntityAll() {
        return iReBlogService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个博客记录", httpMethod = "POST")
    public JsonResultVO saveEntity(@Validated ReBlogSaveDTO reBlogSaveDTO) {
        ReBlog entity = new ReBlog();
        BeanUtils.copyProperties(reBlogSaveDTO, entity);
        entity.setView(0);
        entity.setCreateTime(new Date());
        entity.setModifyTime(new Date());
        entity.setStatus((byte) 1);
        entity.setComment(0);
        // 处理博客的简介信息
        if (StringUtil.isEmpty(entity.getSummary())) {
            String deleteHtml = HtmlUtil.delHtmlTagFromStr(entity.getContentHtml());
            entity.setSummary(deleteHtml.substring(0, 300));
        }
        return iReBlogService.saveEntity(entity);
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新一个博客实体", notes = "id只能是数字类型", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReBlogUpdateDTO reBlogUpdateDTO) {
        ReBlog entity = new ReBlog();
        BeanUtils.copyProperties(reBlogUpdateDTO, entity);
        // 处理博客的简介信息
        if (StringUtil.isEmpty(entity.getSummary())) {
            String deleteHtml = HtmlUtil.delHtmlTagFromStr(entity.getContentHtml());
            entity.setSummary(deleteHtml.substring(0, 300));
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
    public JsonResultVO listBlogPage(@Validated PageDTO pageDTO) {
        return iReBlogService.listBlogPage(pageDTO.getPage(), pageDTO.getCount());
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

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "根据id恢复博客", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReBlogService.restore(id);
    }

    @GetMapping("/listHotArticles")
    @ApiOperation(value = "获取热门文章列表", httpMethod = "GET")
    public JsonResultVO listHotArticles() {
        return iReBlogService.listHotArticles();
    }

    @GetMapping("/listGuessYouLike")
    @ApiOperation(value = "获取猜你喜欢文章列表", httpMethod = "GET")
    public JsonResultVO listGuessYouLike() {
        return iReBlogService.listGuessYouLike();
    }
}
