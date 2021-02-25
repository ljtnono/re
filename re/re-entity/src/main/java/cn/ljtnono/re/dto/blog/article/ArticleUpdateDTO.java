package cn.ljtnono.re.dto.blog.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 更新博客文章DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/26 12:30 上午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "更新博客文章DTO对象")
public class ArticleUpdateDTO extends ArticleAddAndUpdateBaseDTO{

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Integer id;

}
