package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.businese.BusinessException;

/**
 * @author Ling, Jiatong
 * Date: 2020/9/22 0:44
 * Description: 资源已存在异常
 */
public class ResourceAlreadyExistException extends BusinessException {
    public ResourceAlreadyExistException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }
}
