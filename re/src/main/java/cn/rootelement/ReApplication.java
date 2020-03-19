package cn.rootelement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot 启动类
 * @author ljt
 * @date 2020/1/6
 * @version 1.0.2
 */
@SpringBootApplication
@MapperScan(value = {"cn.rootelement.mapper"})
public class ReApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReApplication.class, args);
    }
}
