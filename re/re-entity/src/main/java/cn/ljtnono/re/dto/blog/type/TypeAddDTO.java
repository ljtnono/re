package cn.ljtnono.re.dto.blog.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增博客类型DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/22 1:02 上午
 */
@Data
@ApiModel(description = "新增博客类型DTO对象")
public class TypeAddDTO {

    /**
     * 博客类型名
     */
    @ApiModelProperty(value = "博客类型名", notes = "不超过20个字符")
    private String name;

    /**
     * 是否推荐类型
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 不是")
    private Integer recommend;
}
