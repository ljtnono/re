package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 时间轴实体
 *
 * @author Ling, Jiatong
 * Date: 2020/7/30 1:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Timeline extends BaseEntity {


    /**
     * 时间轴内容
     */
    private String content;
}
