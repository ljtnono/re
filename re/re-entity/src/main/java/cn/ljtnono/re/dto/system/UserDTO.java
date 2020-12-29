package cn.ljtnono.re.dto.system;

import cn.ljtnono.re.dto.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 用户模块通用DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/7/13 23:26 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户模块通用DTO对象")
public class UserDTO extends BaseDTO {

    /** id列表，当进行批量操作时可以使用此字段 */
    @ApiModelProperty(value = "用户id列表", notes = "进行批量操作时使用此参数")
    private List<Integer> idList;

    /** 用户名 */
    @ApiModelProperty("用户名")
    private String username;

    /** 密码 */
    @ApiModelProperty("用户密码")
    private String password;

    /** 电话 */
    @ApiModelProperty("用户手机号")
    private String phone;

    /** 邮箱 */
    @ApiModelProperty("用户邮箱")
    private String email;

    /** 是否删除 1 删除 0 正常 */
    @ApiModelProperty(value = "是否删除", notes = "1 删除 0 正常")
    private Integer deleted;

    /** 角色id */
    @ApiModelProperty("用户角色id")
    private Integer roleId;

    /** 验证码id */
    @ApiModelProperty("验证码Id")
    private String verifyCodeId;

    /** 验证码 */
    @ApiModelProperty("验证码值")
    private String verifyCode;

    /** 是否强制登陆 1 强制登陆 2 不强制登陆 */
    @ApiModelProperty(value = "是否强制登陆", notes = "1 强制登陆 2 不强制登陆")
    private Integer forceLogin;
}
