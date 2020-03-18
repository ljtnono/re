package cn.ljtnono.re.entity;

import cn.ljtnono.re.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 权限实体类
 * @author ljt
 * @date 2019/10/28
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RePermission extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5664674075785947553L;

    /** 权限id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 权限描述 */
    private String description;

    /** 权限访问路径 */
    private String res;

}
