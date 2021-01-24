package cn.ljtnono.re.common.exception.system;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;

/**
 * 数据库操作异常
 *
 * @author Ling, Jiatong
 * Date: 2020/9/22 0:47
 */
public class DataBaseException extends SystemException{

    public DataBaseException(GlobalErrorEnum globalErrorEnum) {
        super(globalErrorEnum);
    }

}
