package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.businese.BusinessException;

/**
 * 资源已存在异常
 *
 * @author Ling, Jiatong
 * Date: 2020/9/22 0:44
 */
public class ResourceAlreadyExistException extends BusinessException {
    public ResourceAlreadyExistException(GlobalErrorEnum globalErrorEnum) {
        super(globalErrorEnum);
    }
}
