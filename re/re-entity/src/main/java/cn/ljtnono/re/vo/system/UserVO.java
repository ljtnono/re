package cn.ljtnono.re.vo.system;

import lombok.Data;
import lombok.ToString;

/**
 * 用户通用VO实体
 * @author Ling, Jiatong
 * Date: 2020/12/6 22:41
 */
@Data
@ToString
public class UserVO {

    /** 用户id */
    private Integer id;

    /** 用户名 */
    private String username;

    /** 用户邮箱 */
    private String email;

    /** 用户手机号码 */
    private String phone;

    /** 用户角色id */
    private Integer roleId;

    /** 用户角色名 */
    private String roleName;

}
