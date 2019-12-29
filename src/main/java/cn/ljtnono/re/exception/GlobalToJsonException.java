package cn.ljtnono.re.exception;

import cn.ljtnono.re.enumeration.GlobalErrorEnum;

import java.io.Serializable;

/**
 * 全局返回Json数据异常
 * @author ljt
 * @date 2019/11/9
 * @version 1.0.2
 */
public class GlobalToJsonException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6650149484548862961L;

    private GlobalErrorEnum globalErrorEnum;

    public GlobalToJsonException(GlobalErrorEnum globalErrorEnum) {
        this.globalErrorEnum = globalErrorEnum;
    }

    public GlobalToJsonException() {
        this(GlobalErrorEnum.SYSTEM_ERROR);
    }

    public GlobalErrorEnum getGlobalErrorEnum() {
        return globalErrorEnum;
    }

    public void setGlobalErrorEnum(GlobalErrorEnum globalErrorEnum) {
        this.globalErrorEnum = globalErrorEnum;
    }
}
