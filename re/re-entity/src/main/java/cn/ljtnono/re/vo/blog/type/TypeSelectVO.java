package cn.ljtnono.re.vo.blog.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 博客类型下拉列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/22 10:48 下午
 */
@Data
@ApiModel(description = "博客类型下拉列表VO对象")
public class TypeSelectVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 类型名
     */
    @ApiModelProperty(value = "类型名")
    private String name;

}
