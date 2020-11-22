package cn.ljtnono.re.dto.system;

import cn.ljtnono.re.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/13 23:26 下午
 * Description: 用户前端传输对象封装
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDTO extends BaseDTO {

    /** id列表，当进行批量操作时可以使用此字段 */
    private List<Integer> idList;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 是否删除 1 删除 0 正常 */
    private Integer deleted;

    /** 角色id */
    private Integer roleId;

    /** 验证码id */
    private String verifyCodeId;

    /** 验证码 */
    private String verifyCode;

    /** 是否强制登陆 1 强制登陆 2 不强制登陆 */
    private Integer forceLogin;
}
