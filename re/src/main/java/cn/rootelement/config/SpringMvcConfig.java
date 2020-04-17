package cn.rootelement.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置SpringMvc处理静态文件
 *
 * @author ljt
 * @version 1.0
 * @date 2019/10/19
 */
@SpringBootConfiguration
public class SpringMvcConfig implements WebMvcConfigurer {

    /**
     * 配置SpringMvc静态资源拦截
     *
     * @param registry 静态资源注册中心
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( "swagger-ui.html", "/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
    }
}
