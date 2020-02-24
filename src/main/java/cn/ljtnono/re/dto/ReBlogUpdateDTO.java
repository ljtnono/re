package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 博客查询DTO对象封装
 * @author ljt
 * @date 2020/2/19
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReBlogUpdateDTO implements Serializable {

    private static final long serialVersionUID = 3409946296504363887L;

    /** 博客的标题 */
    @NotEmpty(message = "博客标题不能为空")
    @NotNull(message = "博客标题不能为null")
    @Length(min = 4, max = 64, message = "博客标题必须为4-64个字符")
    private String title;

    /** 博客的作者 */
    @NotEmpty(message = "博客作者不能为空")
    @NotNull(message = "博客作者不能为null")
    @Length(min = 2, max = 10, message = "博客作者2-10个字符")
    private String author;

    /** 博客的类型 */
    @NotEmpty(message = "博客类型不能为空")
    @NotNull(message = "博客类型不能为null")
    private String type;

    /** 博客的摘要信息 */
    private String summary;

    /** 博客的markdown */
    @NotEmpty(message = "博客类型不能为空")
    @NotNull(message = "博客类型不能为null")
    private String contentMarkdown;

    /** 博客的html */
    @NotEmpty(message = "博客html内容不能为空")
    @NotNull(message = "博客html内容不能为null")
    private String contentHtml;

    /** 博客的封面图片url */
    @NotEmpty(message = "博客封面不能为空")
    @NotNull(message = "博客封面不能为null")
    @URL(message = "博客封面必须是有效的url")
    private String coverImage;

    @NotNull(message = "博客状态只能为0(删除)1(正常)")
    @Min(value = 0, message = "博客状态只能为0(删除)1(正常)")
    @Max(value = 1, message = "博客状态只能为0(删除)1(正常)")
    private Byte status;

    @NotNull(message = "博客浏览量不为null")
    @Min(value = 0, message = "博客浏览量大于0")
    private Integer view;

    @NotNull(message = "博客评论数不为null")
    @Min(value = 0, message = "博客评论数大于0")
    private Integer comment;

}
