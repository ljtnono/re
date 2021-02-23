package cn.ljtnono.re.dto.blog.type;

import cn.ljtnono.re.dto.base.BaseBatchDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批量更新博客类型推荐状态DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/23 11:55 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "批量更新博客类型推荐状态DTO对象")
public class TypeUpdateRecommendBatchDTO extends BaseBatchDTO<Integer> {

    /**
     * 是否推荐
     * 0 是
     * 1 不是
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 否")
    private Integer recommend;

}
