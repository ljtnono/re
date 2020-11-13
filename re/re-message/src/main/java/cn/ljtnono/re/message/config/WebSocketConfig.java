package cn.ljtnono.re.message.config;

import cn.ljtnono.re.message.component.MessageInputChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/14 0:48
 * Description:
 */
@Slf4j
@Component
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private MessageInputChannel messageInputChannel;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/message")
                .setAllowedOrigins("localhost")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 用户代理消息发送到user
        registry.setUserDestinationPrefix("/user")
                .setApplicationDestinationPrefixes("/app");
        // queue 代表点对点消息  topic 代表广播消息
        registry.enableSimpleBroker("/queue", "/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(messageInputChannel);
    }
}
