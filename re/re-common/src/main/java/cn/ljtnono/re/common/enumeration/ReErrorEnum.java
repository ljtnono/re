package cn.ljtnono.re.common.enumeration;

/**
 * @author ljt
 * Date: 2020/7/11 23:44 下午
 * Description: 异常码枚举类
 */
public enum ReErrorEnum {

    //**************************通用**************************//
    /** 请求参数错误 */
    REQUEST_PARAM_ERROR(50001, "请求参数错误"),
    /** 系统异常 */
    SYSTEM_ERROR(50000, "系统异常"),

    //**************************用户相关**************************//
    /** 用户名格式错误 */
    USERNAME_FORMAT_ERROR(50002, "用户名格式错误"),
    /** 用户不存在 */
    USER_NOT_EXIST(50003, "用户不存在"),
    /** 用户名重复 */
    USERNAME_ALREADY_EXIST(50004, "用户名重复"),
    ;

    /** 异常码 */
    private final Integer code;

    /** 异常消息 */
    private final String message;

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
