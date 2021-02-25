package cn.ljtnono.re.dto.blog.article;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 添加博客文章DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/24 15:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "添加博客文章DTO对象")
public class ArticleAddDTO extends ArticleAddAndUpdateBaseDTO{
}
