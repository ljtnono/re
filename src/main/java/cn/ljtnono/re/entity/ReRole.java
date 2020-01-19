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
 * 角色实体
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.1
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6155560455596066680L;

    /** 角色id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 角色名 */
    private String name;

    /** 角色描述 */
    private String description;
}
