package cn.rootelement.entity;

import cn.rootelement.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 配置类
 * @author ljt
 * @date 2019/12/11
 * @version 1.0.1
 * 使用lombok简化操作
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8489311168805539341L;
    /** 配置项id  */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 配置项key  */
    @TableField("`key`")
    private String key;

    /** 配置项value  */
    @TableField("`value`")
    private String value;
}
