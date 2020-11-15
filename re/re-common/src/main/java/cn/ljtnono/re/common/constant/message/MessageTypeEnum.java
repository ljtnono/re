package cn.ljtnono.re.message.constant;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/14 23:25
 * Description: 消息类型枚举
 */
public enum MessageTypeEnum {

    
    /** 系统监控型消息 */
    SYSTEM_MONITOR(100, "系统监控消息"),

    ;

    /**
     * 消息类型码
     */
    private final Integer code;

    /**
     * 消息名
     */
    private final String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    MessageTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
