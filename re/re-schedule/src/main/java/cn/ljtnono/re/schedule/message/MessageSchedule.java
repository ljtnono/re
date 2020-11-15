package cn.ljtnono.re.schedule.message;

import cn.ljtnono.re.entity.system.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

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

    @Scheduled(cron = "*/3 * * * * ?")
    public void run() {
        log.info("[re-schedule-MessageSchedule -> run] websocket当前在线人数：{}");
    }

}
