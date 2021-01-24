package cn.ljtnono.re.dto.system.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 新增用户DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/6 0:53
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户新增DTO对象")
public class UserAddDTO extends UserAddAndUpdateBaseDTO{

}
