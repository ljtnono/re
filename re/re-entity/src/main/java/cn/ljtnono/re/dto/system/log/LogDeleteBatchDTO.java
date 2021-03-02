package cn.ljtnono.re.dto.system.log;

import cn.ljtnono.re.dto.base.BaseBatchDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批量删除系统日志DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/3/2 11:16 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "批量删除系统日志DTO对象")
public class LogDeleteBatchDTO extends BaseBatchDTO<Integer> {
}
