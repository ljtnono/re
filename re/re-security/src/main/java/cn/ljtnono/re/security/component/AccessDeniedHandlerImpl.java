package cn.ljtnono.re.security.component;

import cn.ljtnono.re.common.vo.ReJsonResultVO;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ljt
 * Date: 2020/8/15 22:09
 * Description:
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler, AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 匿名用户访问无权限资源时的异常
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        ReJsonResultVO<?> vo = ReJsonResultVO.error(401, "未认证的用户");
        JSONObject result = new JSONObject();
        try {
            result.accumulate("code", 401).accumulate("message", "未认证过的用户");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.getWriter().write(result.toString());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 认证过的用户访问无权限资源时的异常
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        ReJsonResultVO<?> vo = ReJsonResultVO.error(403, "没有访问权限");
        JSONObject result = new JSONObject();
        try {
            result.accumulate("code", 403).accumulate("message", "没有访问权限");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.getWriter().write(result.toString());
    }
}
