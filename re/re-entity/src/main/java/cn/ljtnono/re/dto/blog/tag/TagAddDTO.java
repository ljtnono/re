package cn.ljtnono.re.dto.blog.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增博客标签DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/24 1:01 上午
 */
@Data
@ApiModel(description = "新增博客标签DTO对象")
public class TagAddDTO {

    /**
     * 博客标签名
     */
    @ApiModelProperty(value = "标签名", notes = "不超过10个字符")
    private String name;
}
