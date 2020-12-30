package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 全局配置实体
 *
 * @author Ling, Jiatong
 * Date: 2020/11/6 23:32
 */
@Data
@TableName("sys_config")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Config extends BaseEntity {

    /**
     * 配置键
     */
    @TableField("`key`")
    private String key;

    /**
     * 配置值
     */
    @TableField("`value`")
    private String value;

}
