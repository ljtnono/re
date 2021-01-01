package cn.ljtnono.re.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目配置类
 *
 * @author Ling, Jiatong
 * Date: 2020/8/24 1:16
 */
@Data
@Component
@ConfigurationProperties(prefix = "re")
public class ReProperties {

    /**
     * spring security相关配置项
     */
    private ReSecurityProperties security;

    /**
     * 是否打开swagger文档
     * 开启:true
     * 关闭:false
     */
    private boolean swaggerUiOpen;

    /**
     * 静态资源文件根目录
     */
    private String staticFileBasePath;
}
