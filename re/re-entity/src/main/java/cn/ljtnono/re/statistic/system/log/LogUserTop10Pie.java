package cn.ljtnono.re.statistic.system.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 日志操作者top10饼状图对象
 *
 * @author Ling, Jiatong
 * Date: 2021/3/2 11:34 下午
 */
@Data
@ApiModel(description = "日志操作者top10饼状图对象")
public class LogUserTop10Pie {

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 操作次数
     */
    @ApiModelProperty("操作次数")
    private Long count;
}
