package cn.ljtnono.re.dto.blog.type;

import cn.ljtnono.re.dto.base.BaseBatchDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 博客类型批量删除DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/23 11:09 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "博客类型批量删除DTO对象")
public class TypeDeleteBatchDTO extends BaseBatchDTO<Integer> {

}
