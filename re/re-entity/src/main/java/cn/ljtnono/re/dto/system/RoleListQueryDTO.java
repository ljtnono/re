package cn.ljtnono.re.dto.system;

import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>角色列表查询DTO对象</p>
 * @author Ling, Jiatong
 * Date: 2020/11/25 0:21
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleListQueryDTO extends BaseListQueryDTO {

    /** 角色id */
    private Integer id;

    /** 角色名 */
    private String name;

    /** 角色描述 */
    private String description;

}
