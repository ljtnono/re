package cn.ljtnono.re.entity;

import cn.ljtnono.re.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 *  用户实体对象
 *  @author 凌家童
 *  @date 2019/10/6
 *  @version 1.0
 *
*/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5314772925085498518L;

    /** 用户id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户名 */
    private String username;

    /** 用户密码 */
    @TableField("`password`")
    private String password;

    /** 用户qq */
    private String qq;

    /** 用户电话 */
    private String tel;

    /** 用户邮箱 */
    private String email;

}
