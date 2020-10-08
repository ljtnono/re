package cn.ljtnono.re.common.exception.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 18:02
 * Description: 系统异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemException extends RuntimeException {

    private Integer code;

    private ReErrorEnum reErrorEnum;

    public SystemException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum.getMessage());
        this.reErrorEnum = reErrorEnum;
        this.code = reErrorEnum.getCode();
    }

    public SystemException(String message) {
        super(message);
    }
}

