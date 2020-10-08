package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.ReBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/30 0:58
 * Description: 用户技能实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_user_skill")
public class ReUserSkill extends ReBaseEntity implements Serializable {

    private static final long serialVersionUID = 2502063850498750506L;

    /** 技能标题 */
    private String title;

    /** 技能所属用户id */
    private Integer userId;

    /** 技能百分比 */
    private BigDecimal percentage;

}
