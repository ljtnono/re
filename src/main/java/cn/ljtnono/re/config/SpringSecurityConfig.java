package cn.ljtnono.re.config;

import cn.ljtnono.re.security.ReAuthenticationPostHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * spring security 配置
 *
 * @author ljt
 * @version 1.0.2
 * @date 2020/1/11
 */
@SpringBootConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    private ReAuthenticationPostHandler reAuthenticationPostHandler;

    @Autowired
    public SpringSecurityConfig(@Qualifier("reUserDetailService") UserDetailsService userDetailsService, @Qualifier("rePasswordEncoder") PasswordEncoder passwordEncoder, ReAuthenticationPostHandler reAuthenticationPostHandler) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.reAuthenticationPostHandler = reAuthenticationPostHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .frameOptions()
                .disable()
                .and()
                .authorizeRequests()
                .antMatchers("/fore/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/admin/login")
                .successHandler(reAuthenticationPostHandler)
                .failureHandler(reAuthenticationPostHandler)
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login")
                .and()
                .authorizeRequests()
                .antMatchers("/admin/login").permitAll()
                .antMatchers("/admin/**").authenticated()
                .and()
                .csrf()
                .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
