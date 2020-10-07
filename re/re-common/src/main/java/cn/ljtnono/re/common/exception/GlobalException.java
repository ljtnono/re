package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

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

    private ReErrorEnum reErrorEnum;

    public GlobalException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum.getMessage());
        this.reErrorEnum = reErrorEnum;
        this.code = reErrorEnum.getCode();
    }

    public GlobalException(String message) {
        super(message);
    }
}
