package cn.ljtnono.re.security.component;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.security.UserPermissionException;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.security.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/11 23:23 下午
 * Description: token过滤器
 */
@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ReSecurityProperties reSecurityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String token = httpServletRequest.getHeader(reSecurityProperties.getTokenHeader());
        if (!StringUtils.isEmpty(token) && token.startsWith(reSecurityProperties.getTokenPrefix())) {
            // 获取到用户名
            token = token.substring(reSecurityProperties.getTokenPrefix().length());
            // 检查token是否过期
            if (jwtUtil.isTokenExpired(token)) {
                throw new UserPermissionException(GlobalErrorEnum.TOKEN_EXPIRED_ERROR);
            }
            username = jwtUtil.getUsernameFromToken(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User reUser = (User) userDetailsService.loadUserByUsername(username);
            // 检验Token是否合法
            if (jwtUtil.validateToken(token, reUser)) {
                UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(reUser, reUser.getPassword(), reUser.getAuthorities());
                upToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(upToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
