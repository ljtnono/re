package cn.ljtnono.re.common.exception.businese;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;

/**
 * @author ljt
 * Date: 2020/8/9 18:20
 * Description: 用户校验异常
 */
public class UserValidateException extends GlobalException {

    public UserValidateException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }

    public UserValidateException(String message) {
        super(message);
    }
}
