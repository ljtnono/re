package cn.ljtnono.re;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ljt
 * Date: 2020/8/9 22:55
 * Description:
 */
@SpringBootApplication(scanBasePackages = {"cn.ljtnono.re"})
@MapperScan(basePackages = {"cn.ljtnono.re.mapper"})
public class ReServiceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReServiceSystemApplication.class, args);
    }
}
