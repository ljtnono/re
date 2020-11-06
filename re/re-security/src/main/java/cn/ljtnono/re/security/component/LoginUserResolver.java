package cn.ljtnono.re.security.component;

import cn.ljtnono.re.common.annotation.LoginUser;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/8 0:49
 * Description:
 */
@Component
public class LoginUserResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ReSecurityProperties reSecurityProperties;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(User.class)
                && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(reSecurityProperties.getTokenHeader());
        // token格式错误或者过期，参数类型不正确都会返回null
        if (StringUtils.isEmpty(token) ||
                !token.startsWith(reSecurityProperties.getTokenPrefix()) ||
                parameter.getParameterType().isInstance(User.class)) {
            return null;
        }
        token = token.substring(reSecurityProperties.getTokenPrefix().length());
        // 处理token过期
        if (jwtUtil.isTokenExpired(token)) {
            return null;
        }
        Claims claims = jwtUtil.getClaimsFromToken(token);
        User reUser = new User();
        Integer userId = claims.get("userId", Integer.class);
        String username = claims.get("username", String.class);
        Integer roleId = claims.get("roleId", Integer.class);
        reUser.setId(userId);
        reUser.setUsername(username);
        reUser.setRoleId(roleId);
        // TODO 如果需要获取其他信息，可以在这里实现
        return reUser;
    }
}
