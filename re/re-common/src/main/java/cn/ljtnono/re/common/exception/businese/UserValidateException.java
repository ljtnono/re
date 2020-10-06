package cn.ljtnono.re.common.exception.businese;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;

/**
 * @author ljt
 * Date: 2020/8/9 18:20
 * Description: 用户校验异常
 */
public class UserValidateException extends BusinessException {

    public UserValidateException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }

}
