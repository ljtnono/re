package cn.ljtnono.re.security.config;

import cn.ljtnono.re.security.component.ReTokenFilter;
import cn.ljtnono.re.security.component.ReUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author ljt
 * Date: 202/7/11 23:24 下午
 * Description: 安全配置
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class ReSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ReTokenFilter reTokenFilter;

    private final ReUserDetailsServiceImpl reUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    public ReSecurityConfig(ReTokenFilter reTokenFilter, ReUserDetailsServiceImpl reUserDetailsService, PasswordEncoder passwordEncoder) {
        this.reTokenFilter = reTokenFilter;
        this.reUserDetailsService = reUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.headers().frameOptions().disable();
        // 禁用缓存
        http.headers().cacheControl();
        // 设置不使用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers("/api/system/user/login")
                .permitAll()
                .and()
                .exceptionHandling();

        http.addFilterBefore(reTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(reUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
