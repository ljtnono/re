package cn.ljtnono.re.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ljt
 * Date: 2020/10/6 23:33
 * Description: 安全配置类
 */
@Data
@Component
@ConfigurationProperties("re.security")
public class ReSecurityProperties {

    /** token过期时间 */
    private Long tokenExpire;

    /** token生成秘钥 */
    private String tokenSecretKey;
}


