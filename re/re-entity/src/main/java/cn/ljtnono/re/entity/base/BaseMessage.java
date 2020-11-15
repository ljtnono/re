package cn.ljtnono.re.entity.base;

import lombok.Data;
import lombok.ToString;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/15 13:01
 * Description: 基础消息实体类
 */
@Data
@ToString
public class BaseMessage {

    /**
     * 消息码
     */
    private Integer code;

    /**
     * 消息名
     */
    private String name;

    /**
     * 消息订阅路径
     */
    private String destination;

}
