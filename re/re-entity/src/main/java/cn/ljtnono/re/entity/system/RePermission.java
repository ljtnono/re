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
 * Description: 权限实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_permission")
public class RePermission extends ReBaseEntity implements Serializable {

    private static final long serialVersionUID = -152690006554286058L;

    /** 权限名 */
    private String name;

    /** 权限类型 0 菜单 1 按钮 */
    private Integer type;

    /** 父级菜单id，顶层菜单为-1 */
    private Integer parentId;

    /** 权限表达式 */
    private String expression;

}
