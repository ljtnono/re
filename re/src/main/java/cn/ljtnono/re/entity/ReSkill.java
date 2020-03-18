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
 * 我的技能实体
 * @author ljt
 * @date 2019/10/28
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReSkill extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5222819912129815867L;

    /** 技能id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 技能名字 */
    private String name;

    /** 技能的所有者 */
    private String owner;

    /** 技能得分 */
    private Integer percent;
}
