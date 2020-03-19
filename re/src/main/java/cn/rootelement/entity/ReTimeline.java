package cn.rootelement.entity;

import cn.rootelement.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 时间轴实体
 * @author ljt
 * @date 2019/10/28
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReTimeline extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3919934196588131827L;
    /** 技能id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 时间轴内容 */
    private String content;

    /** 时间轴显示的时间 */
    private Date pushDate;

}
