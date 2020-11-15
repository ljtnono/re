package cn.ljtnono.re.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 23:04
 * Description: api层启动类
 */
@CrossOrigin
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"cn.ljtnono.re"})
@MapperScan(basePackages = {"cn.ljtnono.re.mapper"})
public class ReApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReApiApplication.class, args);
    }

}

