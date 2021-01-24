package cn.ljtnono.re.service.server;

import cn.ljtnono.re.common.enumeration.message.MessageTypeEnum;
import cn.ljtnono.re.bo.message.ServerMonitorMessageBOBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

/**
 * 系统信息模块service层
 *
 * @author Ling, Jiatong
 * Date: 2020/11/15 14:22
 */
@Slf4j
@Service
public class ServerService {

    //*********************************** 接口方法 ***********************************//

    //*********************************** 私有方法 ***********************************//

    //*********************************** 公共方法 ***********************************//

    //*********************************** 其他方法 ***********************************//

    /**
     * 获取系统监控信息消息实体类
     * @author Ling, Jiatong
     *
     */
    public ServerMonitorMessageBOBO serverMonitorMessage() {
        ServerMonitorMessageBOBO serverMonitorMessageBO = new ServerMonitorMessageBOBO();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory memory = hardware.getMemory();

        // 获取内存信息
        long available = memory.getAvailable();
        long total = memory.getTotal();
        serverMonitorMessageBO.setMemoryAvailable(available);
        serverMonitorMessageBO.setMemoryTotal(total);

        // 系统信息
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        String systemName = operatingSystem.getManufacturer() + " " + operatingSystem.getFamily() + " " + operatingSystem.getVersionInfo();
        serverMonitorMessageBO.setSystemName(systemName);

        serverMonitorMessageBO.setCode(MessageTypeEnum.SERVER_MONITOR.getCode());
        serverMonitorMessageBO.setName(MessageTypeEnum.SERVER_MONITOR.getName());
        serverMonitorMessageBO.setDestination(MessageTypeEnum.SERVER_MONITOR.getDestination());
        serverMonitorMessageBO.setTimeStamp(System.currentTimeMillis());

        return serverMonitorMessageBO;
    }
}
