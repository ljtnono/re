package cn.ljtnono.re.dto.blog.article;

import cn.ljtnono.re.dto.base.BaseBatchDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批量更新博客文章推荐状态DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/26 12:32 上午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "批量更新博客文章推荐状态DTO对象")
public class ArticleUpdateRecommendBatchDTO extends BaseBatchDTO<Integer> {

    /**
     * 是否推荐
     * 0 是
     * 1 否
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 否")
    private Integer recommend;
}
