package cn.ljtnono.re.enumeration;

/**
 * 全局异常枚举, 如果异常跟http错误一样，那么使用http的状态码来标示
 * 如果状态跟http错误状态不一致，那么从10000开始递增状态码
 * @author ljt
 * @date 2019/11/4
 * @version 1.1.2
 */
public enum GlobalErrorEnum {
    /** 系统错误 */
    SYSTEM_ERROR(500, "系统错误"),

    /**  找不到资源404错误 */
    NOTFOUND_ERROR(404, "找不到资源"),

    /** 请求method错误 */
    METHOD_ERROR(10000, "请求方式错误"),

    /** 参数格式异常 */
    PARAM_FORMAT_ERROR(10001, "参数格式错误"),

    /** 参数缺失异常  */
    PARAM_MISSING_ERROR(10002, "参数缺失"),

    /** 参数无效 */
    PARAM_INVALID_ERROR(10003, "参数无效"),

    /** 参数异常 */
    PARAM_ERROR(10004, "参数异常"),

    /** 资源不存在 */
    NOT_EXIST_ERROR(10005, "该资源不存在或已经删除");

    /** 错误码 */
    private final Integer errorCode;

    /** 错误信息*/
    private final String errorMsg;

    GlobalErrorEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
