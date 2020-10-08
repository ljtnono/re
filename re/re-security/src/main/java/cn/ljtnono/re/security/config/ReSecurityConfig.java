package cn.ljtnono.re.security.config;

import cn.ljtnono.re.common.annotation.PassToken;
import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.common.util.SpringBeanUtil;
import cn.ljtnono.re.common.util.jackson.JacksonUtil;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.security.component.ReTokenFilter;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author ljt
 * Date: 202/7/11 23:24 下午
 * Description: 安全配置
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class ReSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ReTokenFilter reTokenFilter;
    @Resource
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReSecurityProperties reSecurityProperties;

    /**
     * 获取使用了PassToken修饰的url
     * @return Set<String>
     * @author Ling, Jiatong
     *
     */
    public Set<String> getPassTokenUrl() {
        TreeSet<String> result = Sets.newTreeSet();
        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringBeanUtil.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> methods = requestMappingHandlerMapping.getHandlerMethods();
        Set<Map.Entry<RequestMappingInfo, HandlerMethod>> entrySet = methods.entrySet();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : entrySet) {
            HandlerMethod method = entry.getValue();
            boolean b = method.hasMethodAnnotation(PassToken.class);
            if (b) {
                result.addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
        }
        return result;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
        // 禁用缓存
        http.headers().cacheControl();
        // 设置不使用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 获取使用了PassToken注解的url
        Set<String> passTokenUrl = getPassTokenUrl();
        if (!CollectionUtils.isEmpty(passTokenUrl)) {
            http.authorizeRequests()
                    .antMatchers(passTokenUrl.toArray(new String[]{}))
                    .permitAll();
        }
        http
                .authorizeRequests()
                .antMatchers(reSecurityProperties.getWhiteUrl().toArray(new String[]{}))
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json");
                    String s = JacksonUtil.objectToString(ReJsonResultVO.error(ReErrorEnum.USER_NOT_AUTHENTICATION));
                    response.getWriter().write(s);
                });

        http.addFilterBefore(reTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
