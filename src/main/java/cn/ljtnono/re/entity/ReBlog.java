package cn.ljtnono.re.entity;

import cn.ljtnono.re.entity.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "re", type = "blog")
public class ReBlog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3699612037615768897L;

    /** 博客的id */
    @Field(type = FieldType.Integer)
    @Id
    private Integer id;

    /** 博客的标题 */

    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", store = true, analyzer = "ik_smart")
    private String title;

    /** 博客的作者 */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", store = true, analyzer = "ik_smart")
    private String author;

    /** 博客的类型 */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", store = true, analyzer = "ik_smart")
    private String type;

    /** 博客的摘要信息 */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String summary;

    /** 博客的markdown */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String contentMarkdown;

    /** 博客的html */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", store = true, analyzer = "ik_smart")
    private String contentHtml;

    /** 博客的封面图片url */
    private String coverImage;

    /** 博客的评论数 */
    private int comment;

    /** 博客的浏览量 */
    private int view;

}
