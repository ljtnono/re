package cn.ljtnono.re.api.controller.blog;

import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.blog.tag.TagAddDTO;
import cn.ljtnono.re.service.blog.TagService;
import cn.ljtnono.re.vo.blog.tag.TagSelectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 博客标签模块接口
 *
 * @author Ling, Jiatong
 * Date: 2020/9/22 1:08
 */
@Slf4j
@RestController
@Api(tags = "博客标签模块接口")
@RequestMapping("/api/v1/blog/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 新增博客标签
     *
     * @param dto 新增博客标签DTO对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @PostMapping
    @ApiOperation(value = "新增博客标签", httpMethod = "POST")
    public JsonResultVO<?> addTag(@RequestBody TagAddDTO dto) {
        log.info("[re-blog -> TagController -> addTag()] 新增博客标签，参数：{}", dto);
        tagService.addTag(dto);
        return JsonResultVO.success();
    }

    /**
     * 获取标签下拉列表
     *
     * @return 博客标签下拉列表VO对象列表
     * @author Ling, Jiatong
     */
    @GetMapping("/select")
    @ApiOperation(value = "获取标签下拉列表", httpMethod = "GET")
    public JsonResultVO<List<TagSelectVO>> select() {
        log.info("[re-blog -> TagController -> select()] 获取博客下拉列表");
        return JsonResultVO.success(tagService.select());
    }

}
