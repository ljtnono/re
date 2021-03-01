package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.util.IpUtil;
import cn.ljtnono.re.common.util.SpringBeanUtil;
import cn.ljtnono.re.entity.system.Log;
import cn.ljtnono.re.mapper.system.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
