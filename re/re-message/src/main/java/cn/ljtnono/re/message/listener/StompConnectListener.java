package cn.ljtnono.re.message.listener;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.businese.BusinessException;
import cn.ljtnono.re.entity.system.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * stomp连接监听器
 *
 * @author Ling, Jiatong
 * Date: 2020/11/15 0:22
 */
@Slf4j
@Component
public class StompConnectListener implements ApplicationListener<SessionConnectedEvent> {

    private final ConcurrentHashMap<String, User> onlineMap;
    public StompConnectListener(@Qualifier("websocketOnlineMap") ConcurrentHashMap<String, User> onlineMap) {
        this.onlineMap = onlineMap;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        // 获取头
        Message<byte[]> message = sessionConnectedEvent.getMessage();
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
        UsernamePasswordAuthenticationToken upToken = (UsernamePasswordAuthenticationToken) accessor.getUser();
        User user = (User) upToken.getPrincipal();
        if (user != null) {
            // 如果存在，那么将
            onlineMap.put(user.getUsername(), user);
        } else {
            throw new BusinessException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
    }
}
