package cn.rootelement.controller;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReBlogSaveDTO;
import cn.rootelement.dto.ReBlogSearchDTO;
import cn.rootelement.dto.ReBlogUpdateDTO;
import cn.rootelement.entity.ReBlog;
import cn.rootelement.es.service.IReBlogEsService;
import cn.rootelement.service.IReBlogService;
import cn.rootelement.util.HtmlUtil;
import cn.rootelement.util.StringUtil;
import cn.rootelement.vo.JsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(value = "ReBlogController", tags = {"博客接口"})
public class ReBlogController {

    private IReBlogService iReBlogService;

    private IReBlogEsService iReBlogEsService;

    @Autowired
    public ReBlogController(IReBlogService iReBlogService, IReBlogEsService iReBlogEsService) {
        this.iReBlogService = iReBlogService;
        this.iReBlogEsService = iReBlogEsService;
    }

    @GetMapping
    @ApiOperation(value = "获取全部博客信息列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        JsonResultVO resultVO = iReBlogService.listEntityAll();
        List<ReBlog> blogList = (List<ReBlog>) resultVO.getData();
        iReBlogEsService.deleteAll();
        blogList.forEach(iReBlogEsService::save);
        return resultVO;
    }

    @PostMapping
    @ApiOperation(value = "新增一个博客记录", httpMethod = "POST")
    @PreAuthorize("hasRole('root')")
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
        iReBlogEsService.save(entity);
        return iReBlogService.saveEntity(entity);
    }

    @PutMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "根据id更新一个博客实体", notes = "id只能是数字类型", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReBlogUpdateDTO reBlogUpdateDTO) {
        ReBlog entity = new ReBlog();
        BeanUtils.copyProperties(reBlogUpdateDTO, entity);
        // 处理博客的简介信息
        if (StringUtil.isEmpty(entity.getSummary())) {
            String deleteHtml = HtmlUtil.delHtmlTagFromStr(entity.getContentHtml());
            entity.setSummary(deleteHtml.substring(0, 300));
        }
        entity.setId(Integer.parseInt((String) id));
        iReBlogEsService.delete(entity);
        iReBlogEsService.save(entity);
        return iReBlogService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
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
    @PreAuthorize("hasRole('root')")
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

    @GetMapping("/search/es")
    @ApiOperation(value = "从elasticsearch中模糊查询", httpMethod = "GET")
    public JsonResultVO searchEsPageByCondition(String condition, PageDTO pageDTO) {
        Page<ReBlog> query = iReBlogEsService.query(condition, pageDTO);
        List<ReBlog> blogList = query.toList();
        JsonResultVO success = JsonResultVO.success(blogList, blogList.size());
        success.addField("totalPages", query.getTotalPages());
        success.addField("totalCount", query.getTotalElements());
        return success;
    }
}
