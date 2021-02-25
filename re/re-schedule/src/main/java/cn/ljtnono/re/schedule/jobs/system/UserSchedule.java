package cn.ljtnono.re.schedule.jobs.system;

import cn.ljtnono.re.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户相关定时任务
 *
 * @author Ling, Jiatong
 * Date: 2020/10/8 2:20
 */
@Slf4j
@Component
public class UserSchedule {

    @Autowired
    private UserService userService;

    public void run() {

    }
}
