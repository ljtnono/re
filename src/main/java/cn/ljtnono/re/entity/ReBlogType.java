package cn.ljtnono.re.entity;

import cn.ljtnono.re.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 *  博客类型实体
 *  @author ljt
 *  @date 2019/12/11
 *  @version 1.0.2
 *  添加了lombok 相关，简化操作
 *
*/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReBlogType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1997697590986661638L;

    /** 博客类型id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 博客类型名 */
    private String name;

    /** 博客类型描述 */
    private String description;
}
