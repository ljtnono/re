package cn.ljtnono.re.dto.resource.image;

import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * 图片分页查询DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/28 0:06
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "图片分页查询DTO对象")
public class ImageListQueryDTO extends BaseListQueryDTO {

    /**
     * 图片类型筛选条件
     */
    @ApiModelProperty(value = "图片类型")
    private Integer type;

    /**
     * 排序允许的字段列表
     */
    @ApiModelProperty(value = "排序允许的字段列表")
    private List<String> sortFieldValueList = Arrays.asList("create_date", "size", "origin_name");

}
