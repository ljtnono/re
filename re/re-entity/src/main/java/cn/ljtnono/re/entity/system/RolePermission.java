package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/6 21:45
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_role_permission")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolePermission extends BaseEntity {

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限id
     */
    private Integer permissionId;
}
