package cn.ljtnono.re.vo.system;

import cn.ljtnono.re.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/19 0:36
 * Description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListVO extends BaseVO {

    /** 用户名 */
    private String username;

    /** 用户手机 */
    private String phone;

    /** 用户邮箱 */
    private String email;

    /** 角色id */
    private Integer roleId;

    /** 角色名 */
    private String roleName;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /** 最后修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
}