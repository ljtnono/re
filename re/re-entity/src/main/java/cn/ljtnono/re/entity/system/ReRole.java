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
 * Description: 角色实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_role")
public class ReRole extends ReBaseEntity implements Serializable {

    private static final long serialVersionUID = -152690006554286058L;

    /** 角色名 */
    private String name;

}
