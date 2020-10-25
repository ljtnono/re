package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/30 0:56
 * Desciription: 用户消息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_user_message")
public class UserMessage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6450065004950075220L;

    /** 所属用户id */
    private Integer userId;

    /** 消息内容 */
    private String message;

    /** 消息状态 0 未读 1 已读 */
    private Integer status;

    @TableField("is_deleted")
    private Integer deleted;

}
