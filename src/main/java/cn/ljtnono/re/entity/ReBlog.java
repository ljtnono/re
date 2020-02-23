package cn.ljtnono.re.entity;

import cn.ljtnono.re.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;

/**
 * 博客实体类
 * @author ljt
 * @date 2019/12/11
 * @version 1.1
 * 新增使用lombok简化代码
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReBlog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3699612037615768897L;

    /** 博客的id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 博客的标题 */
    private String title;

    /** 博客的作者 */
    private String author;

    /** 博客的类型 */
    private String type;

    /** 博客的摘要信息 */
    private String summary;

    /** 博客的markdown */
    private String contentMarkdown;

    /** 博客的html */
    private String contentHtml;

    /** 博客的封面图片url */
    private String coverImage;

    /** 博客的评论数 */
    private int comment;

    /** 博客的浏览量 */
    private int view;

}
