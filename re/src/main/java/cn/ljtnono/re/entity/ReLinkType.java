package cn.ljtnono.re.entity;

import cn.ljtnono.re.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 链接类型实体类
 * @author ljt
 * @date 2019/10/28
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReLinkType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5481376664245861120L;

    /** 链接类型id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 链接类型的名称 */
    private String name;

}
