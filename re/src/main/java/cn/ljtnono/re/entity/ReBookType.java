package cn.ljtnono.re.entity;

import cn.ljtnono.re.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;

/**
 * 书籍类型实体类
 * @author ljt
 * @date 2019/12/11
 * @version 1.1
 * 添加了lombok简化操作
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReBookType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2561242836029170847L;

    /** 书籍类型id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 书籍类型的名字 */
    private String name;

    /** 书籍类型描述 */
    private String description;

}
