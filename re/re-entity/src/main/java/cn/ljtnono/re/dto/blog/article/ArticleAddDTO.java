package cn.ljtnono.re.dto.blog.article;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * 添加博客文章DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/24 15:19
 */
@Data
@ToString
@ApiModel(description = "添加博客文章DTO对象")
public class ArticleAddDTO {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章简介
     */
    private String summary;

    /**
     * 文章markdown内容
     */
    private String markdown;

    /**
     * 文章所属标签id
     */
    private Integer tagId;

    /**
     * 文章封面图所属id
     */
    private Long imageId;

    /**
     * 是否是草稿
     * 0 不是
     * 1 是
     */
    private Integer isDraft;
}
