package cn.ljtnono.re.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 安全配置类
 *
 * @author Ling, Jiatong
 * Date: 2020/10/6 23:33
 */
@Data
@Component
@ConfigurationProperties(prefix = "re.security")
public class ReSecurityProperties {

    /**
     * token过期时间
     */
    private Long tokenExpire;

    /**
     * token生成秘钥
     */
    private String tokenSecretKey;

    /**
     * url白名单（不需要登录）
     */
    private Set<String> whiteUrl;

    /**
     * 令牌前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Header头名称
     */
    private String tokenHeader = "Authorization";

}


