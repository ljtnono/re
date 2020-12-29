package cn.ljtnono.re.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 用户模块通用VO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/12/6 22:41
 */
@Data
@ToString
@ApiModel(description = "用户模块通用VO对象")
public class UserVO {

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Integer id;

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    private String username;

    /** 用户邮箱 */
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    /** 用户手机号码 */
    @ApiModelProperty(value = "用户手机号")
    private String phone;

    /** 用户角色id */
    @ApiModelProperty(value = "用户角色Id")
    private Integer roleId;

    /** 用户角色名 */
    @ApiModelProperty(value = "用户角色名")
    private String roleName;
}
