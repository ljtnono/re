package cn.ljtnono.re.dto.system.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 用户登录DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/9 21:43
 */
@Data
@ToString
@ApiModel(description = "用户登录DTO对象")
public class UserLoginDTO {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;

    /**
     * 验证码id
     */
    @ApiModelProperty(value = "验证码id")
    private String verifyCodeId;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verifyCode;

    /**
     * 是否强制登陆
     */
    @ApiModelProperty(value = "是否强制登陆", notes = "1 强制登陆 2 不强制登陆")
    private Integer forceLogin;
}
