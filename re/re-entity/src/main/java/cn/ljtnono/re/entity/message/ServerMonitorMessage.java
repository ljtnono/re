package cn.ljtnono.re.entity.message;

import cn.ljtnono.re.entity.base.BaseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/15 13:02
 * Description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SystemMonitorMessage extends BaseMessage {

    private Integer cpu;

    private Integer memory;

}
