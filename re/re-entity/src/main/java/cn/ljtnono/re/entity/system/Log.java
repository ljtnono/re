package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统日志实体
 *
 * @author Ling, Jiatong
 * Date: 2020/12/30 23:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Log extends BaseEntity {

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
  }
