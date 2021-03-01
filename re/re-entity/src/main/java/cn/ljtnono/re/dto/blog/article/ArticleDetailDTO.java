package cn.ljtnono.re.dto.blog.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 博客详情DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/28 12:52 上午
 */
@Data
@ApiModel(description = "博客详情DTO对象")
public class ArticleDetailDTO {

    /**
     * 博客id
     */
    @ApiModelProperty("id")
    private Integer id;

}
