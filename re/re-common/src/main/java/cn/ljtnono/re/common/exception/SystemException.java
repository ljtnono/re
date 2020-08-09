package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;

/**
 * @author ljt
 * Date: 2020/8/2 1:11
 * Description: 系统异常
 */
public class SystemException extends GlobalException {

    public SystemException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }
}
