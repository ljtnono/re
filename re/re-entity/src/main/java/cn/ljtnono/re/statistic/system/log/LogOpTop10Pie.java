package cn.ljtnono.re.statistic.system.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 日志操作top10饼状图对象
 *
 * @author Ling, Jiatong
 * Date: 2021/3/2 11:26 下午
 */
@Data
@ApiModel(description = "日志操作top10饼状图对象")
public class LogOpTop10Pie {

    /**
     * 操作名
     */
    @ApiModelProperty("操作名")
    private String opName;

    /**
     * 操作次数
     */
    @ApiModelProperty("操作次数")
    private Long count;
}
