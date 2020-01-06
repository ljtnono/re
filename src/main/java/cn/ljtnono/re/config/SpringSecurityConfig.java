package cn.ljtnono.re.config;

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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * spring security 配置
 *
 * @author 凌家童
 * @version 1.0
 * @date 2019/10/6
 */
@SpringBootConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("reUserDetailService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Qualifier("rePasswordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

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
                .successHandler(authenticationSuccessHandler)
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
