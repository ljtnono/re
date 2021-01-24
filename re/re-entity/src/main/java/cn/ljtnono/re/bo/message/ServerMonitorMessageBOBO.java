package cn.ljtnono.re.bo.message;

import cn.ljtnono.re.bo.base.BaseMessageBO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统监控消息实体
 *
 * @author Ling, Jiatong
 * Date: 2020/11/15 13:02
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ServerMonitorMessageBOBO extends BaseMessageBO {

    /**
     * 操作系统名（厂商 + 系统名 + 版本）
     */
    private String systemName;

    /**
     * 可用内存数（字节大小）
     */
    private long memoryAvailable;

    /**
     * 总内存数（字节大小）
     */
    private long memoryTotal;

    /**
     * 获取信息时的时间戳
     */
    private long timeStamp;

}
