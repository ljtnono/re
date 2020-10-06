package cn.ljtnono.re.vo.system;

import cn.ljtnono.re.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author ljt
 * Date: 2020/8/15 22:29
 * Description: 用户登录VO
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReUserLoginVO extends BaseVO implements Serializable {

    private static final long serialVersionUID = 1927623273715296589L;

    /**
     * 用户名
     */
    private Integer username;

    /**
     * 登陆token
     */
    private String token;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户权限id列表
     */
    private List<Integer> permissionIdList;
}
