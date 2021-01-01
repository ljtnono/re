package cn.ljtnono.re;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * api层启动类
 *
 * @author Ling, Jiatong
 * Date: 2020/8/9 23:04
 */
@EnableScheduling
@SpringBootApplication
public class ReApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReApiApplication.class, args);
    }

}

