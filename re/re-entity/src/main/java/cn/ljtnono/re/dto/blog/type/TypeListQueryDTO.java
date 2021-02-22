package cn.ljtnono.re.dto.blog.type;

import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 博客类型列表DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/23 12:49 上午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "博客类型列表DTO对象")
public class TypeListQueryDTO extends BaseListQueryDTO {



}
