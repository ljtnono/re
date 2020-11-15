package cn.ljtnono.re.message.component;

import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/14 1:01
 * Description:
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class MessageInputChannel implements ChannelInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    @Resource
    private UserDetailsService userDetailsService;

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
        List<String> authentication = accessor.getNativeHeader("Authentication");
        String token;
        if (!CollectionUtils.isEmpty(authentication)) {
            token = authentication.get(0);
            String username = jwtUtil.getUsernameFromToken(token);
            User user = (User) userDetailsService.loadUserByUsername(username);
            // 设置消息通知
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            accessor.setUser(upToken);
        }
        return message;
    }
}
