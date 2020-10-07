package cn.ljtnono.re.security.config;

import cn.ljtnono.re.security.component.LoginUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/8 0:40
 * Description: mvc配置相关
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginUserResolver loginUserResolver;

    /**
     * 配置controller方法参数处理器
     * @param resolvers 方法参数处理器集合
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserResolver);
    }
}
