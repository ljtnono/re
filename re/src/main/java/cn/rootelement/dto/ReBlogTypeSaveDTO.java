package cn.rootelement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 博客类型存储DTO
 * @author ljt
 * @date 2020/1/6
 * @version 1.0.1
 */
@Data
@ToString
@NoArgsConstructor
public class ReBlogTypeSaveDTO implements Serializable {

    private static final long serialVersionUID = -3422022161048146231L;

    /** 博客类型名 */
    @NotNull
    @Length(min = 2, max = 20, message = "博客类型名只能为2-20个字符")
    private String name;

    /** 博客类型描述 */
    @NotNull
    @Length(max = 255, message = "博客类型描述只能为0-255个字符")
    private String description;

}
