package cn.ljtnono.re.vo.system.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 角色模块通用VO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/8/24 23:39
 */
@Data
@ToString
@ApiModel(description = "角色模块通用VO对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleVO {

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
