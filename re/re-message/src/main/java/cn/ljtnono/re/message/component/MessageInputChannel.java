package cn.ljtnono.re.message.component;

import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/14 1:01
 * Description:
 */
@Component
@Order(999)
public class MessageInputChannel implements ChannelInterceptor {

    /**
     * 发送消息拦截器
     * @param message 消息体
     * @param channel 消息通道
     * @author Ling, Jiatong
     *
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {



        return message;
    }
}
