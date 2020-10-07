package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.ReBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/6 21:54
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_user_role")
public class ReUserRole extends ReBaseEntity {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;
}
