package cn.ljtnono.re.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * DTO对象基类
 *
 * @author Ling, Jiatong
 * Date: 2020/7/13 23:27 下午
 */
@Data
@ToString
@ApiModel(description = "DTO对象基类")
public class BaseDTO {

    /** id */
    @ApiModelProperty("id")
    private Integer id;

    /** 分页页数 默认为1 */
    @ApiModelProperty(value = "分页页数", example = "1")
    private Integer pageNum = 1;

    /** 每页条数 默认为10 */
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;
}
