package cn.ljtnono.re.dto.system;

import cn.ljtnono.re.dto.base.ReBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/13 23:18
 * Description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReRoleDTO extends ReBaseDTO implements Serializable {

    private static final long serialVersionUID = 4505176109063156696L;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色权限表达式列表
     */
    private List<Integer> permissionIdList;

}
