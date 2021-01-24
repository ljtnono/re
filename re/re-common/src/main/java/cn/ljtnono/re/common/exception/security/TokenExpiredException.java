package cn.ljtnono.re.common.exception.security;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * token过期异常
 *
 * @author Ling, Jiatong
 * Date: 2020/9/22 0:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TokenExpiredException extends RuntimeException {
    private Integer code;

    private GlobalErrorEnum globalErrorEnum;

    public TokenExpiredException(GlobalErrorEnum globalErrorEnum) {
        super(globalErrorEnum.getMessage());
        this.globalErrorEnum = globalErrorEnum;
        this.code = globalErrorEnum.getCode();
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
