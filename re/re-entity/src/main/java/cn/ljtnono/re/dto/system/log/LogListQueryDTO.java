package cn.ljtnono.re.dto.system.log;

import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统日志模块列表查询DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/3/2 10:51 下午
 */
@Data
@ApiModel(description = "系统日志模块列表查询DTO对象")
@EqualsAndHashCode(callSuper = true)
public class LogListQueryDTO extends BaseListQueryDTO {

    /**
     * 操作名
     */
    @ApiModelProperty(value = "操作名")
    private String opName;

    /**
     * 操作结果
     * 1 成功
     * 2 失败
     */
    @ApiModelProperty(value = "操作结果", notes = "1 成功 2 失败")
    private Integer result;

    /**
     * 允许的排序字段列表，这是一个不可变列表，子类重写此字段
     */
    @ApiModelProperty(value = "允许的排序字段列表", notes = "这是一个不可变列表，子类重写此字段")
    private List<String> sortFieldValueList = List.of("create_time");
}
