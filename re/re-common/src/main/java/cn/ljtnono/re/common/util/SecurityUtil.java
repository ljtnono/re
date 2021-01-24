package cn.ljtnono.re.common.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * spring security相关工具类
 *
 * @author Ling, Jiatong
 * Date: 2020/10/7 17:59
 */
public class SecurityUtil {

    private SecurityUtil() {}

    public static SecurityUtil getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final SecurityUtil INSTANCE = new SecurityUtil();
    }

    /**
     * 获取当前登录用户
     * @return Object 当前登录用户
     * @author Ling, Jiatong
     */
    public Object getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 不支持匿名登陆
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return authentication.getPrincipal();
        } else {
            // 未登陆
            return null;
        }
    }
}
