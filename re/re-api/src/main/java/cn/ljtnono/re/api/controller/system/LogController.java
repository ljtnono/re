package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.system.log.LogDeleteBatchDTO;
import cn.ljtnono.re.dto.system.log.LogListQueryDTO;
import cn.ljtnono.re.service.system.LogService;
import cn.ljtnono.re.statistic.system.log.LogOpTop10Pie;
import cn.ljtnono.re.statistic.system.log.LogUserTop10Pie;
import cn.ljtnono.re.vo.system.log.LogListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统日志接口
 *
 * @author Ling, Jiatong
 * Date: 2020/12/30 23:05
 */
@Slf4j
@RestController
@Api(tags = "系统日志模块")
@RequestMapping("/api/v1/system/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 获取系统日志列表
     *
     * @param dto 系统日志列表查询DTO对象
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取系统日志列表", httpMethod = "GET")
    public JsonResultVO<IPage<LogListVO>> getList(LogListQueryDTO dto) {
        log.info("[re-system -> LogController -> getList()] 获取系统日志列表，参数：{}", dto);
        return JsonResultVO.success(logService.getList(dto));
    }

    /**
     * 批量删除系统日志
     *
     * @param dto 批量删除系统日志DTO对象
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @DeleteMapping("/physicDeleteBatch")
    @ApiOperation(value = "批量删除系统日志", httpMethod = "DELETE")
    public JsonResultVO<?> physicDeleteBatch(@RequestBody LogDeleteBatchDTO dto) {
        log.info("[re-system -> LogController -> physicDeleteBatch()] 批量删除系统日志，参数：{}", dto);
        logService.physicDeleteBatch(dto);
        return JsonResultVO.success();
    }

    /**
     * 获取系统操作日志前top10
     * 默认获取一周内的数据
     *
     * @return 日志操作top10饼状图对象列表
     * @author Ling, Jiatong
     */
    @GetMapping("/opTop10Pie")
    @ApiOperation(value = "获取系统操作日志前top10", httpMethod = "GET")
    public JsonResultVO<List<LogOpTop10Pie>> getLogOpTop10Pie() {
        log.info("[re-system -> LogController -> getLogOpTop10Pie()] 获取系统操作日志前top10");
        return JsonResultVO.success(logService.getLogOpTop10Pie());
    }

    /**
     * 获取系统操作者前top10
     * 默认获取一周内的数据
     *
     * @return 日志操作者top10饼状图对象列表
     * @author Ling, Jiatong
     */
    @GetMapping("/userTop10Pie")
    @ApiOperation(value = "获取日志操作者前top10", httpMethod = "GET")
    public JsonResultVO<List<LogUserTop10Pie>> getLogUserTop10Pie() {
        log.info("[re-system -> LogController -> getLogUserTop10Pie()] 获取日志操作者前top10");
        return JsonResultVO.success(logService.getLogUserTop10Pie());
    }

}
