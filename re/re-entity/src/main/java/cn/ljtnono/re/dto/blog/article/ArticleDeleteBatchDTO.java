package cn.ljtnono.re.dto.blog.article;

import cn.ljtnono.re.dto.base.BaseBatchDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 博客文章批量删除DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/25 12:41 上午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "博客文章批量删除DTO对象")
public class ArticleDeleteBatchDTO extends BaseBatchDTO<Integer> {
}
