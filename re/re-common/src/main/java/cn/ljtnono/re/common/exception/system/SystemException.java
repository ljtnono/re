package cn.ljtnono.re.common.exception.system;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统异常
 *
 * @author Ling, Jiatong
 * Date: 2020/8/9 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemException extends RuntimeException {

    private Integer code;

    private GlobalErrorEnum globalErrorEnum;

    public SystemException(GlobalErrorEnum globalErrorEnum) {
        super(globalErrorEnum.getMessage());
        this.globalErrorEnum = globalErrorEnum;
        this.code = globalErrorEnum.getCode();
    }

    public SystemException(String message) {
        super(message);
    }
}

