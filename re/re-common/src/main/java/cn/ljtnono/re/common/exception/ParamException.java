package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.businese.BusinessException;

/**
 * 参数异常
 *
 * @author Ling, Jiatong
 * Date: 2020/9/22 0:39
 */
public class ParamException extends BusinessException {

    public ParamException(GlobalErrorEnum globalErrorEnum) {
        super(globalErrorEnum);
    }
}
