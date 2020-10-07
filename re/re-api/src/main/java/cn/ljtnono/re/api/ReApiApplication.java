package cn.ljtnono.re.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 23:04
 * Description: api层启动类
 */
@SpringBootApplication(scanBasePackages = {"cn.ljtnono.re"})
@MapperScan(basePackages = {"cn.ljtnono.re.mapper"})
@EnableScheduling
public class ReApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReApiApplication.class, args);
    }

}
