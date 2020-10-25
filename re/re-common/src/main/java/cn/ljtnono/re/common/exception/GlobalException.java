package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 18:02
 * Description: 全局异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GlobalException extends RuntimeException {

    private Integer code;

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
