package cn.ljtnono.re.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 通用VO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/8/27 0:32
 */
@Data
@ToString
@ApiModel(description = "通用VO对象")
public class BaseVO {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 是否删除 0 正常 1 删除
     */
    @ApiModelProperty(value = "是否删除", notes = "0 正常 1 已删除")
    private Integer deleted;
}
