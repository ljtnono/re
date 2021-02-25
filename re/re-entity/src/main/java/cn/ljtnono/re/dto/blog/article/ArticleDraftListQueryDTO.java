package cn.ljtnono.re.dto.blog.article;

import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章草稿列表查询DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/25 11:36 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "文章草稿列表查询DTO对象")
public class ArticleDraftListQueryDTO extends BaseListQueryDTO {


}
