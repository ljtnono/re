package cn.ljtnono.re.common.enumeration;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 20:19
 * Description: redis Key存储枚举
 */
public enum ReRedisKeyEnum {

    /**
     * 用户存储在redis中的键
     */
    USER_INFO_KEY("re:system:user:id:username"),

    ;

    private final String value;

    public String getValue() {
        return value;
    }

    ReRedisKeyEnum(String value) {
        this.value = value;
    }
}
