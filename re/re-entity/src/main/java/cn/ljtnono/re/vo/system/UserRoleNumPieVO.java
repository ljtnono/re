package cn.ljtnono.re.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 用户根据角色分类统计饼状图VO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/12/7 2:16
 */
@Data
@ToString
@ApiModel(description = "用户根据角色分类统计饼状图VO对象")
public class UserRoleNumPieVO {

    /** 角色名 */
    @ApiModelProperty("角色名")
    private String roleName;

    /** 用户个数 */
    @ApiModelProperty("角色个数")
    private Integer num;
}
