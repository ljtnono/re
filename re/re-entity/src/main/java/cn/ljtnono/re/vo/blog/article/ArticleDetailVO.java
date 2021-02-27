package cn.ljtnono.re.vo.blog.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 博客详情VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/28 12:37 上午
 */
@Data
@ApiModel(description = "博客详情VO对象")
public class ArticleDetailVO {

    /**
     * 博客id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 博客标题
     */
    @ApiModelProperty("博客标题")
    private String title;

    /**
     * 博客简介
     */
    @ApiModelProperty("博客简介")
    private String summary;

    /**
     * markdown内容
     */
    @ApiModelProperty("markdown内容")
    private String markdownContent;


}
