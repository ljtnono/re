package cn.ljtnono.re.schedule.message;

import cn.ljtnono.re.entity.message.ServerMonitorMessage;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.service.server.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/14 23:10
 * Description:
 */
@Slf4j
@Component
public class MessageSchedule {

    @Resource
    private SimpMessagingTemplate template;
    @Autowired
    @Qualifier("websocketOnlineMap")
    private ConcurrentHashMap<String, User> onlineMap;
    @Autowired
    private ServerService serverService;

    /**
     * 系统监控消息
     * @author Ling, Jiatong
     *
     */
    @Scheduled(cron = "*/3 * * * * ?")
    public void serverMonitor() {
        log.info("========== websocket当前在线人数：{} ==========", onlineMap.size());
        List<String> users = onlineMap.values()
                .parallelStream()
                .map(User::getUsername)
                .distinct()
                .collect(Collectors.toList());
        log.info("========== websocket在线用户列表：{} ==========", users.toString());
        ServerMonitorMessage message = serverService.serverMonitorMessage();
        template.convertAndSend(message.getDestination(), message);
    }
}
