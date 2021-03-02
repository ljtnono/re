package cn.ljtnono.re.vo.system.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 系统日志列表查询VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/3/2 11:04 下午
 */
@Data
@ApiModel(description = "系统日志列表查询VO对象")
public class LogListVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 操作者用户名
     */
    @ApiModelProperty(value = "操作者用户名")
    private String username;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 操作名
     */
    @ApiModelProperty(value = "操作名")
    private String opName;

    /**
     * 操作详情
     */
    @ApiModelProperty(value = "操作详情")
    private String opDetail;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ip;

    /**
     * 操作结果
     * 1 成功
     * 2 失败
     */
    @ApiModelProperty(value = "操作结果", notes = "1 成功 2 失败")
    private Integer result;
}
