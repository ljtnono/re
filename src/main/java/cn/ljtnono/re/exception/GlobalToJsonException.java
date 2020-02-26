package cn.ljtnono.re.exception;

import cn.ljtnono.re.enumeration.HttpStatusEnum;

import java.io.Serializable;

/**
 * 全局返回Json数据异常
 * @author ljt
 * @date 2019/11/9
 * @version 1.0.2
 */
public class GlobalToJsonException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6650149484548862961L;

    private HttpStatusEnum httpStatusEnum;

    public GlobalToJsonException(HttpStatusEnum httpStatusEnum) {
        this.httpStatusEnum = httpStatusEnum;
    }

    public GlobalToJsonException() {
        this(HttpStatusEnum.INTERNAL_SERVER_ERROR);
    }

    public HttpStatusEnum getHttpStatusEnum() {
        return httpStatusEnum;
    }

    public void setHttpStatusEnum(HttpStatusEnum httpStatusEnum) {
        this.httpStatusEnum = httpStatusEnum;
    }
}
