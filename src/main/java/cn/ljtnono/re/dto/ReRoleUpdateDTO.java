package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 角色更新DTO类
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.1
 */
@Data
@ToString
@NoArgsConstructor
public class ReRoleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 8882538233766884332L;

    @NotNull(message = "角色名不能为null")
    @Length(min = 2, max = 20, message = "角色名为2-20个字符")
    private String name;

    @NotNull(message = "角色描述不能为null")
    @Length(min = 0, max = 255, message = "角色描述不能超过255个字符")
    private String description;

}
