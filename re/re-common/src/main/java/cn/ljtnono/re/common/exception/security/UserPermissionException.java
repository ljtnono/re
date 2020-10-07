package cn.ljtnono.re.common.exception.security;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/6 21:09
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserPermissionException extends RuntimeException{

    private Integer code;

    private ReErrorEnum reErrorEnum;

    public UserPermissionException(ReErrorEnum reErrorEnum) {
        super(reErrorEnum.getMessage());
        this.reErrorEnum = reErrorEnum;
        this.code = reErrorEnum.getCode();
    }
    public UserPermissionException(String message) {
        super(message);
    }
}
