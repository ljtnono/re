package cn.rootelement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * 角色新增DTO
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReRoleSaveDTO implements Serializable {

    private static final long serialVersionUID = -5885362974972044461L;

    @NotNull(message = "角色名不能为null")
    @Length(min = 2, max = 20, message = "角色名为2-20个字符")
    private String name;

    @NotNull(message = "角色描述不能为null")
    @Length(max = 255, message = "角色描述不能超过255个字符")
    private String description;
}
