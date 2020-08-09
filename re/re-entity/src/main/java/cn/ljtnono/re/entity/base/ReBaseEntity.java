package cn.ljtnono.re.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ljt
 * Date: 2020/7/11 23:56 下午
 * Description:
 */
@Data
@ToString
public class ReBaseEntity implements Serializable {

    private static final long serialVersionUID = -5097540987940971509L;

    /** 实体id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 实体创建时间 */
    private Date createTime;

    /** 实体最后修改时间 */
    private Date modifyTime;

}
