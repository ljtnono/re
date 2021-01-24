package cn.ljtnono.re.dto.system.userrole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 用户角色表新增DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/24 23:34
 */
@Data
@ToString
@ApiModel(description = "用户角色表新增DTO对象")
public class UserRoleAddDTO {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private Integer roleId;

}
