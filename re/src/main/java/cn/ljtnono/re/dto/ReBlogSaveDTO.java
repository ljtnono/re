package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 封装新增博客对象输入参数
 * @author ljt
 * @date 2019/12/11
 * @version 1.0.1
 */
@Data
@ToString
@NoArgsConstructor
public class ReBlogSaveDTO implements Serializable {

    private static final long serialVersionUID = -2747756701272030287L;

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
}
