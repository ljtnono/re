package cn.ljtnono.re.dto.system.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 用户新增和更新基类DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/24 23:32
 */
@Data
@ToString
@ApiModel(description = "用户新增和更新基类DTO对象")
public class UserAddAndUpdateBaseDTO {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("用户密码")
    private String password;

    /**
     * 电话
     */
    @ApiModelProperty("用户手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 角色id
     */
    @ApiModelProperty("用户角色id")
    private Integer roleId;

    /**
     * 验证码id
     */
    @ApiModelProperty("验证码Id")
    private String verifyCodeId;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码值")
    private String verifyCode;

}
