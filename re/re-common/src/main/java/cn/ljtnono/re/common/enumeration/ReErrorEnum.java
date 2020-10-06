package cn.ljtnono.re.common.enumeration;

/**
 * @author ljt
 * Date: 2020/7/11 23:44 下午
 * Description: 异常码枚举类
 */
public enum ReErrorEnum {

    //**************************通用**************************//
    /** 请求参数错误 */
    REQUEST_PARAM_ERROR(400001, "请求参数错误"),
    /** 资源已经存在 */
    RESOURCE_ALREADY_EXIST_ERROR(400002, "资源已经存在"),
    /** 资源不存在 */
    RESOURCE_NOT_EXIST_ERROR(400003, "资源不存在"),

    //**************************用户相关**************************//
    /** 用户名格式错误 */
    USERNAME_FORMAT_ERROR(400101, "用户名是8-16位字符（字母，数字，下划线，减号）"),
    /** 密码格式错误 */
    PASSWORD_FORMAT_ERROR(400102, "密码是6-20位字符，必须包括字母和数字与特殊字符（!@#$%^&*?）的其中一种"),
    /** 用户不存在 */
    USER_NOT_EXIST(400103, "用户不存在"),
    /** 用户名重复 */
    USERNAME_ALREADY_EXIST(400104, "用户名重复"),
    /** 验证码过期 */
    VERIFY_CODE_EXPIRED(400105, "验证码过期"),
    /** 验证码错误 */
    VERIFY_CODE_ERROR(400106, "验证码错误"),
    /** 邮箱格式错误 */
    EMAIL_FORMAT_ERROR(400107, "邮箱格式错误"),
    /** 手机号码格式错误 */
    PHONE_FORMAT_ERROR(400108, "手机号码格式错误"),
    /** 用户id不能为NULL */
    USER_ID_NULL_ERROR(400109, "用户id不能为NULL"),
    /** 请输入用户名 */
    INPUT_USERNAME(400110, "请输入用户名"),
    /** 请输入密码 */
    INPUT_PASSWORD(400111, "请输入密码"),
    /** 密码错误 */
    PASSWORD_ERROR(400112, "密码错误"),
    /** 验证码ID为空 */
    VERIFY_CODE_NULL_ERROR(400113, "验证码不能为空"),
    //**************************角色相关**************************//
    /** 角色id不能为空 */
    ROLE_ID_NULL_ERROR(400210, "角色id不能为空"),
    /** 角色名不能为空 */
    ROLE_NAME_EMPTY_ERROR(400211, "角色名不能为空"),
    /** 角色已经存在 */
    ROLE_ALREADY_EXIST(400212, "角色已存在"),

    //**************************token**************************//
    /** token过期 */
    TOKEN_EXPIRED_ERROR(403001, "token过期"),
    /** token格式错误 */
    TOKEN_FORMAT_ERROR(403002, "token格式错误"),
    /** token签名错误 */
    TOKEN_SIGNATURE_ERROR(403003, "token签名错误"),
    /** 用户权限异常 */
    USER_PERMISSION_ERROR(4003004, "用户权限异常"),

    //**************************系统异常相关**************************//
    /** 系统异常 */
    SYSTEM_ERROR(500000, "系统异常"),
    /** 数据库操作异常 */
    DATABASE_OPERATION_ERROR(500001, "操作失败"),
    /** 业务异常 */
    BUSINESS_ERROR(500002, "业务异常"),
    /** 用户校验异常 */
    USER_VALIDATE_ERROR(500003, "用户校验异常"),
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
