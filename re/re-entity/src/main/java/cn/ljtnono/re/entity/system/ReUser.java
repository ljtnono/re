package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.ReBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljt
 * Date: 2020/7/12 22:23 下午
 * Description: 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_user")
public class ReUser extends ReBaseEntity implements Serializable {

    private static final long serialVersionUID = 4057606458753697276L;

    /** 用户名 */
    private String username;

    /** 用户密码 */
    private String password;

    /** 用户qq */
    private String qq;

    /** 手机号码 */
    private String tel;

    /** 用户邮箱 */
    private String email;

}
