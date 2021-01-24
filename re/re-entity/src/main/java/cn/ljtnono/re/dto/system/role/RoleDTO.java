package cn.ljtnono.re.dto.system.role;

import cn.ljtnono.re.dto.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 角色模块通用DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/8/13 23:18
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "角色模块通用DTO对象")
public class RoleDTO extends BaseDTO {

    /**
     * 角色id列表
     */
    @ApiModelProperty(value = "角色id列表", notes = "进行批量操作时使用此参数")
    private List<Integer> idList;

    /**
     * 角色名
     */
    @ApiModelProperty(value = "角色名")
    private String name;

    /**
     * 角色权限id列表
     */
    @ApiModelProperty("角色权限id列表")
    private List<Integer> permissionIdList;

}
