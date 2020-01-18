package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增用户DTO对象
 * @author ljt
 * @date 2020/1/18
 * @version 1.0.2
 */
@Data
@ToString
@NoArgsConstructor
public class ReUserSaveDTO implements Serializable {

    private static final long serialVersionUID = -1522314980698223917L;

    @NotNull(message = "username不能为null")
    @Length(min = 2, max = 20, message = "用户名的长度为2-20个字符")
    private String username;

    @NotNull(message = "password不能为null")
    @Length(min = 6, max = 32, message = "密码的长度为6-32个字符")
    private String password;

    private String qq;

    private String tel;

    private String email;
}
