package cn.lingjiatong.re.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author Ling, Jiatong
 * Date: 2023/7/27 23:58
 */
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.api", "cn.lingjiatong.re.service"})
@MapperScan(basePackages = {"cn.lingjiatong.re.service.mapper"})
public class ReApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReApiApplication.class, args);
    }
}
