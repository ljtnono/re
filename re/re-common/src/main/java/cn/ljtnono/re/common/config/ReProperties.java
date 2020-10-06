package cn.ljtnono.re.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ljt
 * Date: 2020/8/24 1:16
 * Description: 项目配置类
 */
@Data
@Component
@ConfigurationProperties("re")
public class ReProperties {

    private ReSecurityProperties security;
}
