package cn.ljtnono.re.exception;

import cn.ljtnono.re.enumeration.GlobalErrorEnum;

import java.io.Serializable;

/**
 * 全局返回错误页面异常
 * @author ljt
 * @date 2019/11/9
 * @version 1.0.2
 */
public class GlobalToViewException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6668131900686521973L;

    private GlobalErrorEnum globalErrorEnum;

    public GlobalToViewException(GlobalErrorEnum globalErrorEnum) {
        this.globalErrorEnum = globalErrorEnum;
    }

    public GlobalToViewException() {
        this(GlobalErrorEnum.SYSTEM_ERROR);
    }

    public GlobalErrorEnum getGlobalErrorEnum() {
        return globalErrorEnum;
    }

    public void setGlobalErrorEnum(GlobalErrorEnum globalErrorEnum) {
        this.globalErrorEnum = globalErrorEnum;
    }
}
