package cn.ljtnono.re.schedule.system;

import cn.ljtnono.re.service.system.ReUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/8 2:20
 * Description: 用户相关定时任务
 */
@Slf4j
@Component
public class ReUserSchedule {

    @Autowired
    private ReUserService reUserService;

    @Scheduled(cron = "*/3 * * * * ?")
    public void run() {
        log.info("---------------------------");
    }
}
