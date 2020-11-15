package cn.ljtnono.re.message.listener;

import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.security.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/15 0:22
 * Description:
 */
@Slf4j
@Component
public class StompConnectListener implements ApplicationListener<SessionConnectedEvent> {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    @Qualifier("webSocketOnlineMap")
    private ConcurrentHashMap<String, User> onlineMap;
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        // 获取头
        Message<byte[]> message = sessionConnectedEvent.getMessage();
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
        List<String> authorization = accessor.getNativeHeader("Authorization");
        if (!CollectionUtils.isEmpty(authorization)) {
            String token = authorization.get(0);
            String username = jwtUtil.getUsernameFromToken(token);
            User user = (User) userDetailsService.loadUserByUsername(username);
            // 如果存在，那么将
            onlineMap.put(username, user);
        }
    }
}
