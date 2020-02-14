package cn.ljtnono.re.security;

import cn.ljtnono.re.enumeration.DateStyleEnum;
import cn.ljtnono.re.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 登陆之后处理器
 * @author ljt
 * @date 2020/2/13
 * @version 1.0.1
 */
@Component
@Slf4j
public class ReAuthenticationPostHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登陆时间：{}", DateUtil.formatDate(new Date(), DateStyleEnum.yyyy_MM_dd_HH_mm_ss));
        log.info("用户信息：{}", JSONObject.fromObject(authentication).toString());
        PrintWriter writer = response.getWriter();
        String result = new JSONObject()
                .accumulate("message", "success")
                .accumulate("status", 200)
                .toString();
        writer.write(result);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("登陆失败：{}", exception.getMessage());
        PrintWriter writer = response.getWriter();
        String result = new JSONObject()
                .accumulate("message", exception.getMessage())
                .accumulate("status", 401)
                .toString();
        writer.write(result);
    }
}
