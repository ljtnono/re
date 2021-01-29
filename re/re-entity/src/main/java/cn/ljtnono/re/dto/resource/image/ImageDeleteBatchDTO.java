package cn.ljtnono.re.dto.resource.image;

import cn.ljtnono.re.dto.base.BaseBatchDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 批量删除图片DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/29 23:21
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ImageDeleteBatchDTO extends BaseBatchDTO<Integer> {
}
