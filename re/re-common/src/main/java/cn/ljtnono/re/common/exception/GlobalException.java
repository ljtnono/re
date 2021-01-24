package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 全局异常
 *
 * @author Ling, Jiatong
 * Date: 2020/8/9 18:02
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {

    /**
     * 异常码
     */
    private Integer code;

    /**
     * 异常枚举
     */
    private GlobalErrorEnum globalErrorEnum;

    public GlobalException(GlobalErrorEnum globalErrorEnum) {
        super(globalErrorEnum.getMessage());
        this.globalErrorEnum = globalErrorEnum;
        this.code = globalErrorEnum.getCode();
    }

    public GlobalException(String message) {
        super(message);
    }
}
