package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;

/**
 * @author ljt
 * Date: 2020/8/2 0:55
 * Description: 通用业务异常
 */
public class BusinessException extends GlobalException{

    public BusinessException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }
}
