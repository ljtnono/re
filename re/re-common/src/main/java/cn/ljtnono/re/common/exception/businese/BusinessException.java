package cn.ljtnono.re.common.exception.businese;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/2 0:55
 * Description: 通用业务异常
 */
public class BusinessException extends GlobalException {

    public BusinessException(GlobalErrorEnum globalErrorEnum) {
        super(globalErrorEnum);
    }
}
