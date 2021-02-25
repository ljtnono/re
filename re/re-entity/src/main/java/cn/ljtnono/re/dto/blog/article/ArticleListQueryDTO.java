package cn.ljtnono.re.dto.blog.article;

import cn.ljtnono.re.dto.base.BaseDTO;
import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 博客文章列表查询DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/25 11:01 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "博客文章列表查询DTO对象")
public class ArticleListQueryDTO extends BaseListQueryDTO {

    /**
     * 标题模糊查询
     */
    @ApiModelProperty(value = "标题", notes = "标题模糊查询条件")
    private String title;

    /**
     * 是否推荐
     * 0 是
     * 1 否
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 否")
    private Integer recommend;


    /**
     * 允许的排序字段列表，这是一个不可变列表，子类重写此字段
     */
    @ApiModelProperty(value = "允许的排序字段列表", notes = "这是一个不可变列表，子类重写此字段")
    private List<String> sortFieldValueList = List.of("create_time", "view", "favorite");
}
