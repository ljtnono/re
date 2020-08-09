package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;

/**
 * @author ljt
 * Date: 2020/8/9 18:20
 * Description:
 */
public class UserValidateException extends GlobalException {

    public UserValidateException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }

    public UserValidateException(String message) {
        super(message);
    }
}
