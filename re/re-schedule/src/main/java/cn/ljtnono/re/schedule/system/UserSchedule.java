package cn.ljtnono.re.schedule.system;

import cn.ljtnono.re.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/8 2:20
 * Description: 用户相关定时任务
 */
@Slf4j
@Component
public class UserSchedule {

    @Autowired
    private UserService userService;

    public void run() {

    }
}
