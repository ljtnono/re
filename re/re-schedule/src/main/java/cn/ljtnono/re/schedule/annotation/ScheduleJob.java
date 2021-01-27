package cn.ljtnono.re.schedule.annotation;

import java.lang.annotation.*;

/**
 * 定时任务注解
 *
 * @author Ling, Jiatong
 * Date: 2021/1/24 17:21
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduleJob {

    /**
     * 定时任务cron表达式
     *
     * @return 定时任务的cron表达式
     */
    String cron();

    long fixRate();
}
