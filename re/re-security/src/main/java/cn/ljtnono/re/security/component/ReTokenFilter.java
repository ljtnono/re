package cn.ljtnono.re.security.component;

import cn.ljtnono.re.cache.UserInfoCache;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.enumeration.RedisKeyEnum;
import cn.ljtnono.re.common.exception.security.UserPermissionException;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.security.util.ReJwtUtil;
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
public class ReTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;
    @Autowired
    private ReJwtUtil reJwtUtil;
    @Autowired
    private ReSecurityProperties reSecurityProperties;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String token = httpServletRequest.getHeader(reSecurityProperties.getTokenHeader());
        if (!StringUtils.isEmpty(token) && token.startsWith(reSecurityProperties.getTokenPrefix())) {
            // 获取到用户名
            token = token.substring(reSecurityProperties.getTokenPrefix().length());
            // 检查token是否过期
            if (reJwtUtil.isTokenExpired(token)) {
                throw new UserPermissionException(GlobalErrorEnum.TOKEN_EXPIRED_ERROR);
            }
            username = reJwtUtil.getUsernameFromToken(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User reUser = (User) userDetailsService.loadUserByUsername(username);
            // 检验Token是否合法
            if (reJwtUtil.validateToken(token, reUser)) {
                if (isAlreadyLogin(reUser, token)) {
                    throw new UserPermissionException(GlobalErrorEnum.USER_ALREADY_LOGIN_ERROR);
                }
                UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(reUser, reUser.getPassword(), reUser.getAuthorities());
                upToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(upToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 校验是否已经登录
     * @param reUser 当前携带token的用户
     * @return 已经登录返回true，没有登陆返回false
     * @author Ling, Jiatong
     *
     */
    public boolean isAlreadyLogin(User reUser, String token) {
        // 缓存校验, 如果存在缓存说明已经登录
        Object cache = redisUtil.get(RedisKeyEnum.USER_INFO_KEY.getValue()
                .replace("id", String.valueOf(reUser.getId()))
                .replace("username", reUser.getUsername()));
        if (cache == null) {
            return false;
        }
        // TODO 以后可以添加通过给ReUser添加一个字段来实现强制登陆
        UserInfoCache userInfoCache = (UserInfoCache) cache;
        return userInfoCache.getToken().equals(token);
    }

}
