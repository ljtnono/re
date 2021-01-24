package cn.ljtnono.re.common.exception.security;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户权限异常
 *
 * @author Ling, Jiatong
 * Date: 2020/10/6 21:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserPermissionException extends RuntimeException{

    private Integer code;

    private GlobalErrorEnum globalErrorEnum;

    public UserPermissionException(GlobalErrorEnum globalErrorEnum) {
        super(globalErrorEnum.getMessage());
        this.globalErrorEnum = globalErrorEnum;
        this.code = globalErrorEnum.getCode();
    }
    public UserPermissionException(String message) {
        super(message);
    }
}
