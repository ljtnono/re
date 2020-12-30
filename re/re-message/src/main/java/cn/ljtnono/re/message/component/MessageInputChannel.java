package cn.ljtnono.re.message.component;

import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.security.util.JwtUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 消息认证拦截器
 *
 * @author Ling, Jiatong
 * Date: 2020/11/14 1:01
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class MessageInputChannel implements ChannelInterceptor {

    @Resource
    private UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    public MessageInputChannel(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 发送消息拦截器
     * @param message 消息体
     * @param channel 消息通道
     * @author Ling, Jiatong
     *
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String token = accessor.getFirstNativeHeader("Authorization");
        if (!StringUtils.isEmpty(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            User user = (User) userDetailsService.loadUserByUsername(username);
            // 设置消息通知
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            accessor.setUser(upToken);
        }
        return message;
    }
}
