package cn.ljtnono.re.common.enumeration;

/**
 * @author ljt
 * Date: 2020/8/9 20:19
 * Description: redis Key存储枚举
 */
public enum ReRedisKeyEnum {

    /**
     * 用户存储在redis中的键
     */
    USER_KEY("system:user:id:username")


    ;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    ReRedisKeyEnum(String key) {
        this.key = key;
    }
}
