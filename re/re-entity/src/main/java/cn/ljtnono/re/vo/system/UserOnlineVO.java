package cn.ljtnono.re.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 用户在线统计VO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/12/9 0:04
 */
@Data
@ToString
@ApiModel(description = "用户在线统计VO对象")
public class UserOnlineVO {

    /** 总在线人数 */
    @ApiModelProperty("用户在线总人数")
    private Integer totalNum;

    /** 超级管理员数 */
    @ApiModelProperty("超级管理员在线人数")
    private Integer adminNum;

    /** 游客数量 */
    @ApiModelProperty("游客在线人数")
    private Integer touristNum;

    public UserOnlineVO() {
        totalNum = 0;
        touristNum = 0;
        adminNum = 0;
    }
}
