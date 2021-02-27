package cn.ljtnono.re.api.controller.blog;

import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.blog.article.*;
import cn.ljtnono.re.service.blog.ArticleService;
import cn.ljtnono.re.vo.blog.article.ArticleDetailVO;
import cn.ljtnono.re.vo.blog.article.ArticleDraftListVO;
import cn.ljtnono.re.vo.blog.article.ArticleListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 博客文章模块接口
 *
 * @author Ling, Jiatong
 * Date: 2020/9/22 1:08
 */
@Slf4j
@RestController
@Api(tags = "博客文章模块接口")
@RequestMapping("/api/v1/blog/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增博客文章
     *
     * @param dto 新增博客文章DTO对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @PostMapping
    @ApiOperation(value = "新增博客文章", httpMethod = "POST")
    public JsonResultVO<?> addArticle(@RequestBody ArticleAddDTO dto) {
        log.info("[re-blog -> ArticleController -> addArticle()] 新增博客文章，参数：{}", dto);
        articleService.addArticle(dto);
        return JsonResultVO.success();
    }

    /**
     * 批量逻辑删除博客
     *
     * @param dto 博客文章批量删除DTO对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @DeleteMapping("/logicDeleteBatch")
    @ApiOperation(value = "批量逻辑删除博客", httpMethod = "DELETE")
    public JsonResultVO<?> logicDeleteBatch(@RequestBody ArticleDeleteBatchDTO dto) {
        log.info("[re-blog -> ArticleController -> logicDeleteBatch()] 批量逻辑删除博客，参数：{}", dto);
        articleService.logicDeleteBatch(dto);
        return JsonResultVO.success();
    }

    /**
     * 批量物理删除博客
     *
     * @param dto 博客文章批量删除DTO对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @DeleteMapping("/physicDeleteBatch")
    @ApiOperation(value = "批量物理删除博客", httpMethod = "DELETE")
    public JsonResultVO<?> physicDeleteBatch(@RequestBody ArticleDeleteBatchDTO dto) {
        log.info("[re-blog -> ArticleController -> physicDeleteBatch()] 批量物理删除博客，参数：{}", dto);
        articleService.physicDeleteBatch(dto);
        return JsonResultVO.success();
    }

    /**
     * 分页获取博客列表
     *
     * @param dto 博客文章列表查询DTO对象
     * @return 博客列表VO对象分页对象
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页获取博客列表", httpMethod = "GET")
    public JsonResultVO<IPage<ArticleListVO>> getList(ArticleListQueryDTO dto) {
        log.info("[re-blog -> ArticleController -> getList()] 分页获取博客列表，参数：{}", dto);
        return JsonResultVO.success(articleService.getList(dto));
    }

    /**
     * 更新博客
     *
     * @param dto 博客文章更新DTO对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @PutMapping
    @ApiOperation(value = "更新博客", httpMethod = "PUT")
    public JsonResultVO<?> updateArticle(@RequestBody ArticleUpdateDTO dto) {
        log.info("[re-blog -> ArticleController -> updateArticle()] 更新博客，参数：{}", dto);
        articleService.updateArticle(dto);
        return JsonResultVO.success();
    }

    /**
     * 分页获取博客草稿列表
     *
     * @param dto 文章草稿列表查询DTO对象
     * @return 博客文章草稿列表查询VO对象分页对象
     * @author Ling, Jiatong
     */
    @GetMapping("/draftList")
    @ApiOperation(value = "分页获取博客草稿列表", httpMethod = "GET")
    public JsonResultVO<IPage<ArticleDraftListVO>> getDraftList(ArticleDraftListQueryDTO dto) {
        return JsonResultVO.success();
    }

    /**
     * 批量更新博客推荐状态
     *
     * @param dto 批量更新博客推荐状态DTO对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @PutMapping("/updateRecommendBatch")
    @ApiOperation(value = "批量更新博客推荐状态", httpMethod = "PUT")
    public JsonResultVO<?> updateRecommendBatch(@RequestBody ArticleUpdateRecommendBatchDTO dto) {
        log.info("[re-blog -> ArticleController -> updateRecommendBatch()] 批量更新博客推荐状态，参数：{}", dto);
        articleService.updateRecommendBatch(dto);
        return JsonResultVO.success();
    }

    /**
     * 获取博客文章详情
     *
     * @param dto 博客详情DTO对象
     * @return 博客详情VO对象
     * @author Ling, Jiatong
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取博客详情", httpMethod = "GET")
    public JsonResultVO<ArticleDetailVO> getDetail(ArticleDetailDTO dto) {
        log.info("[re-blog -> ArticleController -> getDetail()] 获取博客详情，参数：{}", dto);
        return JsonResultVO.success(articleService.getDetail(dto));
    }

}
