package cn.ljtnono.re.entity.blog;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 博客文章实体类
 *
 * @author Ling, Jiatong
 * Date: 2020/7/30 1:03
 */
@Data
@TableName("blog_article")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "博客文章实体类")
public class Article extends BaseEntity {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", notes = "不超过50个字符")
    private String title;

    /**
     * 简介
     */
    @ApiModelProperty(value = "博客", notes = "不超过500个字符")
    private String summary;

    /**
     * 博客的markdown内容
     */
    @ApiModelProperty(value = "博客markdown内容")
    private String markdownContent;

    /**
     * 博客分类id
     */
    @ApiModelProperty(value = "博客分类id")
    private Integer typeId;

    /**
     * 发表博客用户id
     */
    @ApiModelProperty(value = "发表用户id")
    private Integer userId;

    /**
     * 博客封面图路径
     */
    @ApiModelProperty(value = "封面图路径")
    private String coverUrl;

    /**
     * 是否删除
     * 0 正常
     * 1 已删除
     */
    @TableField("is_deleted")
    @ApiModelProperty(value = "是否删除", notes = "0 正常 1 已删除")
    private Integer deleted;

    /**
     * 是否推荐
     * 0 是
     * 1 不是
     */
    @TableField("is_recommend")
    @ApiModelProperty(value = "是否推荐文章", notes = "0 是 1 不是")
    private Integer recommend;

    /**
     * 博客浏览量
     */
    @ApiModelProperty(value = "浏览量")
    private Integer view;

    /**
     * 博客喜欢数
     */
    @ApiModelProperty(value = "喜欢数")
    private Integer favorite;

    /**
     * 是否是草稿
     * 0 不是
     * 1 是
     */
    @TableField("is_draft")
    @ApiModelProperty(value = "是否是草稿")
    private Integer draft;
}
