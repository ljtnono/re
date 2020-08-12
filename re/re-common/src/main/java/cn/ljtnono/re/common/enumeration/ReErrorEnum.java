package cn.ljtnono.re.common.enumeration;

/**
 * @author ljt
 * Date: 2020/7/11 23:44 下午
 * Description: 异常码枚举类
 */
public enum ReErrorEnum {

    //**************************通用**************************//
    /** 系统异常 */
    SYSTEM_ERROR(50000, "系统异常"),
    /** 请求参数错误 */
    REQUEST_PARAM_ERROR(50001, "请求参数错误"),

    //**************************用户相关**************************//
    /** 用户名格式错误 */
    USERNAME_FORMAT_ERROR(50002, "用户名是4-16位字符（字母，数字，下划线，减号）"),
    /** 密码格式错误 */
    PASSWORD_FORMAT_ERROR(50003, "密码是6-20位字符，必须包括字母和数字与特殊字符（!@#$%^&*?）的其中一种"),
    /** 用户不存在 */
    USER_NOT_EXIST(50004, "用户不存在"),
    /** 用户名重复 */
    USERNAME_ALREADY_EXIST(50005, "用户名重复"),
    /** 验证码过期 */
    VERIFY_CODE_EXPIRED(50006, "验证码过期"),
    /** 验证码错误 */
    VERIFY_CODE_ERROR(50007, "验证码错误"),
    /** 邮箱格式错误 */
    EMAIL_FORMAT_ERROR(50008, "邮箱格式错误"),
    /** 手机号码格式错误 */
    PHONE_FORMAT_ERROR(50009, "手机号码格式错误"),
    /** 用户id不能为NULL */
    USER_ID_NULL_ERROR(50010, "用户id为NULL"),


    //**************************token**************************//
    /** token过期 */
    TOKEN_EXPIRED_ERROR(60001, "token过期"),
    TOKEN_FORMAT_ERROR(60002, "token格式错误"),
    TOKEN_SIGNATURE_ERROR(60003, "token签名错误"),
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
