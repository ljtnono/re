package cn.ljtnono.re.security.component;

import cn.ljtnono.re.security.util.ReJwtUtil;
import cn.ljtnono.re.service.system.ReUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ljt
 * Date: 2020/7/11 23:23 下午
 * Description: token过滤器
 */
@Component
@Slf4j
public class ReTokenFilter extends OncePerRequestFilter {

    private final ReUserService reUserService;

    private final ReJwtUtil reJwtUtil;

    public ReTokenFilter(ReUserService reUserService, ReJwtUtil reJwtUtil) {
        this.reUserService = reUserService;
        this.reJwtUtil = reJwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader(ReJwtUtil.HEADER_NAME);
        String username = null;
        if (!StringUtils.isEmpty(token) && token.startsWith(ReJwtUtil.TOKEN_PREFIX)) {
            // 获取到用户名
            token = token.substring(ReJwtUtil.TOKEN_PREFIX.length());
            username = reJwtUtil.getUsernameFromToken(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 检验Token是否合法
            UserDetails userDetails = reUserService.loadUserByUsername(username);
            if (reJwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                upToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(upToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
