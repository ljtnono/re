package cn.ljtnono.re.vo.blog.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 博客标签下拉列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/24 1:22 上午
 */
@Data
@ApiModel(description = "博客标签下拉列表VO对象")
public class TagSelectVO {

    /**
     * 标签id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 标签名
     */
    @ApiModelProperty("标签名")
    private String name;
}
