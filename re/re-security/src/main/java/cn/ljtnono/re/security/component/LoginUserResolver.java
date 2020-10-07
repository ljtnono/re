package cn.ljtnono.re.security.component;

import cn.ljtnono.re.common.annotation.LoginUser;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.security.util.ReJwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
    private ReJwtUtil reJwtUtil;
    @Autowired
    private ReSecurityProperties reSecurityProperties;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(ReUser.class)
                && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(reSecurityProperties.getTokenHeader());
        // token格式错误或者过期，参数类型不正确都会返回null
        if (StringUtils.isEmpty(token) ||
                !token.startsWith(reSecurityProperties.getTokenPrefix()) ||
                parameter.getParameterType().isInstance(ReUser.class)) {
            return null;
        }
        token = token.substring(reSecurityProperties.getTokenPrefix().length());
        try {
            reJwtUtil.isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            return null;
        }
        Claims claims = reJwtUtil.getClaimsFromToken(token);
        ReUser reUser = new ReUser();
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
