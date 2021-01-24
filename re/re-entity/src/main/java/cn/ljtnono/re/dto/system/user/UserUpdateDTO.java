package cn.ljtnono.re.dto.system.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户更新DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/9 21:44
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户更新DTO对象")
public class UserUpdateDTO extends UserAddAndUpdateBaseDTO{

    /**
     * 用户id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

}
