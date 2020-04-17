package cn.rootelement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ljt
 * @date 2020/1/10
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReBlogTypeUpdateDTO implements Serializable {

    private static final long serialVersionUID = -6256092868002005446L;

    /** id */
    private Integer id;

    /** 博客类型名 */
    @NotNull
    @Length(min = 2, max = 20, message = "博客类型名只能为2-20个字符")
    private String name;

    /** 博客类型描述 */
    @NotNull
    @Length(max = 255, message = "博客类型描述只能为0-255个字符")
    private String description;

}
