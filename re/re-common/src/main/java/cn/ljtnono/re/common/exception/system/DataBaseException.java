package cn.ljtnono.re.common.exception.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;

/**
 * @author ljt
 * Date: 2020/9/22 0:47
 * Description: 数据库操作异常
 */
public class DataBaseException extends SystemException{

    public DataBaseException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum);
    }
}
