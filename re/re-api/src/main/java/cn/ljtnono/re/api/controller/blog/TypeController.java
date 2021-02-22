package cn.ljtnono.re.api.controller.blog;

import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.blog.type.TypeAddDTO;
import cn.ljtnono.re.dto.blog.type.TypeListQueryDTO;
import cn.ljtnono.re.service.blog.TypeService;
import cn.ljtnono.re.vo.blog.type.TypeDetailVO;
import cn.ljtnono.re.vo.blog.type.TypeListQueryVO;
import cn.ljtnono.re.vo.blog.type.TypeSelectVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 博客类型模块接口
 *
 * @author Ling, Jiatong
 * Date: 2021/2/22 12:57 上午
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/blog/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 获取博客类型下拉列表
     *
     * @return 博客类型下拉列表VO对象列表
     */
    @GetMapping("/select")
    @ApiOperation(value = "获取博客类型下拉列表", httpMethod = "GET")
    public JsonResultVO<List<TypeSelectVO>> select() {
        log.info("[re-blog -> TypeController -> select()] 获取博客类型下拉列表");
        return JsonResultVO.success(typeService.select());
    }

    /**
     * 新增博客类型
     *
     * @param dto 新增博客类型DTO对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @PostMapping
    @ApiOperation(value = "新增博客类型", httpMethod = "POST")
    public JsonResultVO<?> addType(@RequestBody TypeAddDTO dto) {
        log.info("[re-blog -> TypeController -> addType()] 新增博客类型，参数：{}", dto);
        typeService.addType(dto);
        return JsonResultVO.success();
    }

    /**
     * 获取博客类型详情
     *
     * @param id 博客类型id
     * @return 博客类型详情VO对象
     * @author Ling, Jiatong
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取博客类型详情", httpMethod = "GET")
    public JsonResultVO<TypeDetailVO> getDetail(Integer id) {
        log.info("[re-blog -> TypeController -> getDetail()] 获取博客类型详情，参数：{}", id);
        return JsonResultVO.success(typeService.getDetailById(id));
    }

    /**
     * 分页获取博客类型列表
     *
     * @param dto 博客类型列表DTO对象
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
    public JsonResultVO<IPage<TypeListQueryVO>> getList(TypeListQueryDTO dto) {

        return null;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新博客类型", httpMethod = "PUT")
    public JsonResultVO<?> updateType() {
        return null;
    }

    @DeleteMapping("/logicDeleteBatch")
    @ApiOperation(value = "批量逻辑删除博客类型", httpMethod = "DELETE")
    public JsonResultVO<?> logicDeleteBatch() {
        return null;
    }

    @DeleteMapping("/physicDeleteBatch")
    @ApiOperation(value = "批量物理删除博客类型", httpMethod = "DELETE")
    public JsonResultVO<?> physicDeleteBatch() {
        return null;
    }

    @PutMapping("/updateRecommendBatch")
    @ApiOperation(value = "批量设置推荐状态", httpMethod = "PUT")
    public JsonResultVO<?> updateRecommendBatch() {
        return null;
    }

}
