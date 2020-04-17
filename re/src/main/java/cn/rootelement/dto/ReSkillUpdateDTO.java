package cn.rootelement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 技能更新DTO
 * @author ljt
 * @date 2020/1/14
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReSkillUpdateDTO implements Serializable {

    private static final long serialVersionUID = -5604629667147187615L;

    /** 技能名 */
    @NotNull
    @Length(min = 2, max = 20, message = "技能名为2-20个字符")
    private String name;

    /** 所有者 */
    @NotNull
    @Length(min = 2, max = 20, message = "技能所有者为2-20个字符")
    private String owner;

    @NotNull
    @Min(value = 0, message = "最低分0")
    @Max(value = 100, message = "最高分100")
    private Integer percent;
}
