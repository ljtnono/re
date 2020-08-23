package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;

/**
 * @author ljt
 * Date: 2020/8/24 1:43
 * Description: 用户登录异常
 */
public class UserLoginException extends BusinessException{

    public UserLoginException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }
}
