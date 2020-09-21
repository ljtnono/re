package cn.ljtnono.re.common.exception.security;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.businese.BusinessException;

/**
 * @author ljt
 * Date: 2020/9/22 0:43
 * Description: token过期异常
 */
public class TokenExpiredException extends BusinessException {
    public TokenExpiredException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }
}
