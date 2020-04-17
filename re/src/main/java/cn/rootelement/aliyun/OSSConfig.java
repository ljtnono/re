package cn.rootelement.aliyun;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 阿里云oss配置类
 * @author ljt
 * @date 2020/2/19
 * @version 1.0.1
 */
@Component
public class OSSConfig {

    @Value("${server.port}")
    private String title;

}
