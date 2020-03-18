package cn.ljtnono.re.exception;

import cn.ljtnono.re.enumeration.HttpStatusEnum;

import java.io.Serializable;

/**
 * 全局返回错误页面异常
 * @author ljt
 * @date 2019/11/9
 * @version 1.0.2
 */
public class GlobalToViewException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6668131900686521973L;

    private HttpStatusEnum httpStatusEnum;

    public GlobalToViewException(HttpStatusEnum httpStatusEnum) {
        this.httpStatusEnum = httpStatusEnum;
    }

    public GlobalToViewException() {
        this(HttpStatusEnum.INTERNAL_SERVER_ERROR);
    }

    public HttpStatusEnum getHttpStatusEnum() {
        return httpStatusEnum;
    }

    public void setHttpStatusEnum(HttpStatusEnum httpStatusEnum) {
        this.httpStatusEnum = httpStatusEnum;
    }
}
