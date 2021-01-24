package cn.ljtnono.re.dto.system.user;

import cn.ljtnono.re.dto.base.BaseBatchDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户批量删除DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/24 23:26
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户批量删除DTO对象")
public class UserDeleteBatchDTO extends BaseBatchDTO<Integer> {
}
