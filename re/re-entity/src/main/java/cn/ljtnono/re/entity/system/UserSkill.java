package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 用户技能实体
 *
 * @author Ling, Jiatong
 * Date: 2020/7/30 0:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_user_skill")
public class UserSkill extends BaseEntity {

    /** 技能标题 */
    private String title;

    /** 技能所属用户id */
    private Integer userId;

    /** 技能百分比 */
    private BigDecimal percentage;

}
