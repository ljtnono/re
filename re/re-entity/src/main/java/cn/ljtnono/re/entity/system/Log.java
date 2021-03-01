package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统日志实体
 *
 * @author Ling, Jiatong
 * Date: 2020/12/30 23:05
 */
@Data
@TableName("sys_log")
@EqualsAndHashCode(callSuper = true)
public class Log extends BaseEntity {

    /**
     * 成功码
     */
    @TableField(exist = false)
    private static final Integer RESULT_SUCCESS = 1;

    /**
     * 失败码
     */
    @TableField(exist = false)
    private static final Integer RESULT_FAIL = 2;

    /**
     * 系统日志用户id
     */
    @TableField(exist = false)
    private static final Integer SYSTEM_LOG_USER_ID = -1;

    /**
     * 系统日志类型
     */
    @TableField(exist = false)
    private static final Integer SYSTEM_LOG_TYPE = 2;

    /**
     * 用户日志类型
     */
    @TableField(exist = false)
    private static final Integer USER_LOG_TYPE = 1;

    /**
     * 操作用户id
     */
    private Integer userId;

    /**
     * 日志类型 1 用户日志 2 系统日志
     */
    private Integer type;

    /**
     * 日志操作名
     */
    private String opName;

    /**
     * 日志操作详情
     */
    private String opDetail;

    /**
     * 日志操作结果
     * 1 成功
     * 2 失败
     */
    private Integer result;

    /**
     * 操作客户端ip
     */
    private String ip;

    /**
     * 客户端浏览器详情
     */
    private String browser;

    /**
     * 系统日志
     *
     * @param l 日志对象
     * @return 系统日志对象
     * @author Ling, Jiatong
     */
    public Log systemLog(Log l) {
        if (l == null) {
            l = new Log();
        }
        l.setUserId(SYSTEM_LOG_USER_ID);
        l.setType(SYSTEM_LOG_TYPE);
        l.setBrowser(null);
        l.setIp("127.0.0.1");
        return l;
    }

    /**
     * 用户日志
     *
     * @param l 日志对象
     * @param userId 用户id
     * @return 用户日志对象
     * @author Ling, Jiatong
     */
    public Log userLog(Log l, Integer userId) {
        if (l == null) {
            l = new Log();
        }
        l.setUserId(userId);
        l.setType(USER_LOG_TYPE);
        return l;
    }

    /**
     * 成功日志
     *
     * @param l 日志对象
     * @return 成功日志对象
     * @author Ling, Jiatong
     */
    public static Log successLog(Log l) {
        if (l == null) {
            l = new Log();
        }
        l.setResult(RESULT_SUCCESS);
        return l;
    }

    /**
     * 失败日志
     *
     * @param l 日志对象
     * @return 失败日志对象
     * @author Ling, Jiatong
     */
    public static Log failLog(Log l) {
        if (l == null) {
            l = new Log();
        }
        l.setResult(RESULT_FAIL);
        return l;
    }
  }
