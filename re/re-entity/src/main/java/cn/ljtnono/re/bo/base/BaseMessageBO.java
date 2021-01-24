package cn.ljtnono.re.bo.base;

import lombok.Data;
import lombok.ToString;

/**
 * 基础消息实体类
 *
 * @author Ling, Jiatong
 * Date: 2020/11/15 13:01
 */
@Data
@ToString
public class BaseMessageBO {

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
