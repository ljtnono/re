package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.ReBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljt
 * Date: 2020/7/30 1:01
 * Description: 时间轴实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReTimeline extends ReBaseEntity implements Serializable {

    private static final long serialVersionUID = -7676495007544545758L;

    /** 时间轴内容 */
    private String content;
}
