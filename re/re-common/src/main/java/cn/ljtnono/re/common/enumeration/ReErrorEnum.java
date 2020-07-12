package cn.ljtnono.re.common.enumeration;


/**
 * @author ljt
 * Date: 2020/7/11 23:44 下午
 * Description: 异常码枚举类
 */
public enum ReErrorEnum {

    /** 系统异常 */
    SYSTEM_ERROR(50000, "系统异常"),

    /** 用户名格式错误 */
    USERNAME_FORMAT_ERROR(51000, "用户名格式错误");

    /** 异常码 */
    private Integer code;

    /** 异常消息 */
    private String message;

    ReErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
