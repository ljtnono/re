package cn.ljtnono.re.entity;

import cn.ljtnono.re.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 我看的书籍实体类
 * @author ljt
 * @date 2019/10/28
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReBook extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5908659796059572626L;

    /** 书籍的id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 书籍的名字 */
    private String name;

    /** 书籍的作者 */
    private String author;

    /** 出版日期 */
    private Date publishTime;

    /** 封面图片id */
    private String image;

    /** 书籍的摘要信息 */
    private String summary;

    /** 书籍描述 */
    private String description;

    /** 书籍分类 */
    private String type;
}
