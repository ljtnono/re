package cn.ljtnono.re.mapper.system;

import cn.ljtnono.re.dto.system.log.LogListQueryDTO;
import cn.ljtnono.re.entity.system.Log;
import cn.ljtnono.re.statistic.system.log.LogOpTop10Pie;
import cn.ljtnono.re.statistic.system.log.LogUserTop10Pie;
import cn.ljtnono.re.vo.system.log.LogListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统日志模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2021/1/31 21:25
 */
public interface LogMapper extends BaseMapper<Log> {

    /**
     * 列表查询系统日志
     *
     * @param dto 系统日志模块列表查询DTO对象
     * @param page 分页对象
     * @return 系统日志列表VO对象分页对象
     * @author Ling, Jiatong
     */
    IPage<LogListVO> getList(Page<?> page, @Param("dto") LogListQueryDTO dto);

    /**
     * 获取操作前top10饼状图数据
     *
     * @return 日志操作top10饼状图对象列表
     * @author Ling, Jiatong
     */
    List<LogOpTop10Pie> getLogOpTop10Pie();

    /**
     * 获取操作者前top10饼状图数据
     *
     * @return 日志操作者top10饼状图对象列表
     * @author Ling, Jiatong
     */
    List<LogUserTop10Pie> getLogUserTop10Pie();
}
