package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.util.IpUtil;
import cn.ljtnono.re.common.util.SpringBeanUtil;
import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.system.log.LogDeleteBatchDTO;
import cn.ljtnono.re.dto.system.log.LogListQueryDTO;
import cn.ljtnono.re.entity.system.Log;
import cn.ljtnono.re.mapper.system.LogMapper;
import cn.ljtnono.re.statistic.system.log.LogOpTop10Pie;
import cn.ljtnono.re.statistic.system.log.LogUserTop10Pie;
import cn.ljtnono.re.vo.system.log.LogListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * 日志模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/1/31 21:01
 */
@Service
public class LogService {

    @Resource
    private LogMapper logMapper;
    @Autowired
    @Qualifier("commonThreadPool")
    private ExecutorService commonThreadPool;


    //*********************************** 接口方法 ***********************************//

    /**
     * 系统日志列表查询
     *
     * @param dto 系统日志列表查询DTO对象
     * @return 系统日志列表查询VO对象分页对象
     * @author Ling, Jiatong
     */
    public IPage<LogListVO> getList(LogListQueryDTO dto) {
        try {
            dto.generateSortCondition();
        } catch (IllegalArgumentException e) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        return logMapper.getList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto);
    }

    /**
     * 批量删除系统日志
     * 不校验是否存在
     *
     * @param dto 批量删除系统日志DTO对象
     * @author Ling, Jiatong
     */
    public void physicDeleteBatch(LogDeleteBatchDTO dto) {
        List<Integer> idList = dto.getBatchKey();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        logMapper.delete(new LambdaQueryWrapper<Log>()
                .in(Log::getId, idList));
    }

    /**
     * 获取操作前top10饼状图数据
     *
     * @return 日志操作top10饼状图对象列表
     * @author Ling, Jiatong
     */
    public List<LogOpTop10Pie> getLogOpTop10Pie() {
        return logMapper.getLogOpTop10Pie();
    }

    /**
     * 获取操作者前top10饼状图数据
     *
     * @return 日志操作者top10饼状图对象列表
     * @author Ling, Jiatong
     */
    public List<LogUserTop10Pie> getLogUserTop10Pie() {
        return logMapper.getLogUserTop10Pie();
    }


    //*********************************** 私有方法 ***********************************//

    //*********************************** 公共方法 ***********************************//

    /**
     * 插入受事务回滚影响的日志
     *
     * @param l 日志对象
     * @param opName 日志操作名
     * @param opDetail 日志操作详情
     * @author Ling, Jiatong
     */
    public void insertTransactionalLog(Log l, String opName, String opDetail) {
        // 默认设置了userId和结果
        String ipAddress = IpUtil.getInstance().getIpAddress(Objects.requireNonNull(SpringBeanUtil.getCurrentReq()));
        l.setIp(ipAddress);
        l.setModifyTime(new Date());
        l.setCreateTime(new Date());
        l.setOpName(opName);
        l.setOpDetail(opDetail);
        // TODO 设置browser
        logMapper.insert(l);
    }

    /**
     * 插入不受事务回滚影响的日志
     *
     * @param l 日志对象
     * @param opName 日志操作名
     * @param opDetail 日志操作详情
     * @author Ling, Jiatong
     */
    public void insertAsyncLog(Log l, String opName, String opDetail) {
        String ipAddress = IpUtil.getInstance().getIpAddress(Objects.requireNonNull(SpringBeanUtil.getCurrentReq()));
        l.setIp(ipAddress);
        l.setModifyTime(new Date());
        l.setCreateTime(new Date());
        l.setOpName(opName);
        l.setOpDetail(opDetail);
        // TODO 设置browser
        // 异步插入日志
        commonThreadPool.execute(() -> logMapper.insert(l));
    }

    //*********************************** 其他方法 ***********************************//


}
