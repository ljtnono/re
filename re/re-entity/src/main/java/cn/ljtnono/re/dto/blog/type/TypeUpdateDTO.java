package cn.ljtnono.re.dto.blog.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新博客类型DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/24 12:17 上午
 */
@Data
@ApiModel(description = "更新博客类型DTO对象")
public class TypeUpdateDTO {

    /**
     * 博客类型id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 博客类型名
     */
    @ApiModelProperty("类型名")
    private String name;

    /**
     * 是否推荐
     * 0 是
     * 1 否
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 否")
    private Integer recommend;
}