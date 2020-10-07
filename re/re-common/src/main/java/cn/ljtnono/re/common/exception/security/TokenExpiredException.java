package cn.ljtnono.re.common.exception.security;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.businese.BusinessException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Ling, Jiatong
 * Date: 2020/9/22 0:43
 * Description: token过期异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TokenExpiredException extends RuntimeException {
    private Integer code;

    private ReErrorEnum reErrorEnum;

    public TokenExpiredException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum.getMessage());
        this.reErrorEnum = reErrorEnum;
        this.code = reErrorEnum.getCode();
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
