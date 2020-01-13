package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 链接类型新增DTO
 * @author ljt
 * @date 2020/1/13
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReLinkSaveDTO implements Serializable {

    private static final long serialVersionUID = 3502617307120961761L;

    /** 链接的url */
    @URL(message = "链接地址必须为url格式")
    private String url;

    /** 链接名 */
    @NotNull(message = "链接名不能为null")
    private String name;

    /** 链接所属类型 */
    @NotNull
    @Length(min = 2, max = 20, message = "类型所属类型为2-20个字符")
    private String type;

}
