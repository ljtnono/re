package cn.ljtnono.re.dto.system.role;

import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 角色列表查询DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/11/25 0:21
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "角色列表查询DTO对象")
public class RoleListQueryDTO extends BaseListQueryDTO {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Integer id;

    /**
     * 角色名
     */
    @ApiModelProperty("角色名")
    private String name;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    private String description;
}
