package cn.rootelement.entity.common;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.util.Date;

/**
 * 所有实体类的公共字段
 * @author ljt
 * @date 2019/12/11
 * @version 1.0.2
 */
@Data
@NoArgsConstructor
public class BaseEntity {

    /** 创建时间 */
    protected Date createTime;

    /** 最后修改时间 */
    protected Date modifyTime;

    /** 状态 0已经删除 1正常 */
    @TableField("status")
    protected byte status;

}
