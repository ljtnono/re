package cn.ljtnono.re.message.listener;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.businese.BusinessException;
import cn.ljtnono.re.entity.system.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;
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
    @Qualifier("websocketOnlineMap")
    private ConcurrentHashMap<String, User> onlineMap;

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
