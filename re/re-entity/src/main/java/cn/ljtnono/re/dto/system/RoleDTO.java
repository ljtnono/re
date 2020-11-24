package cn.ljtnono.re.dto.system;

import cn.ljtnono.re.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * <p>角色DTO对象</p>
 * @author Ling, Jiatong
 * Date: 2020/8/13 23:18
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends BaseDTO {

    /** 角色id列表 */
    private List<Integer> idList;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色权限表达式列表
     */
    private List<Integer> permissionIdList;

}
