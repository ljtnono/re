package cn.ljtnono.re.common.exception;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljt
 * Date: 2020/8/9 18:02
 * Description: 全局异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GlobalException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 2334681264720751974L;

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
