package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 角色实体
 *
 * @author Ling, Jiatong
 * Date: 2020/7/12 22:23 下午
 */
@Data
@TableName("sys_role")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    /** 角色名 */
    private String name;

    /** 角色描述 */
    private String description;

    /** 是否删除 1 删除 0 正常 */
    @TableField("is_deleted")
    private Integer deleted;

}
