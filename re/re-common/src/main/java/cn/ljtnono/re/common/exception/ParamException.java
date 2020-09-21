package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.businese.BusinessException;

/**
 * @author ljt
 * Date: 2020/9/22 0:39
 * Description: 参数异常
 */
public class ParamException extends BusinessException {

    public ParamException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }
}
