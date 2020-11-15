package cn.ljtnono.re.common.constant.message;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/14 23:25
 * Description: 消息类型枚举
 */
public enum MessageTypeEnum {

    
    /** 系统监控型消息 */
    SERVER_MONITOR(100, "系统监控消息", "/topic/server/monitor"),

    ;

    /**
     * 消息类型码
     */
    private final Integer code;

    /**
     * 消息名
     */
    private final String name;

    /**
     * 消息订阅路径
     */
    private final String destination;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    MessageTypeEnum(Integer code, String name, String destination) {
        this.code = code;
        this.name = name;
        this.destination = destination;
    }
}
