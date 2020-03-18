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
 * 博客链接实体类
 * @author ljt
 * @date 2019/12/11
 * @version 1.1
 * 使用lombok简化操作
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReLink extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2713285364411129741L;

    /** 链接id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 链接url */
    private String url;

    /** 链接名 */
    private String name;

    /** 链接类型 */
    private String type;

}
