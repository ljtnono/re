package cn.ljtnono.re.vo.blog.article;

import cn.ljtnono.re.entity.blog.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 博客详情VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/28 12:37 上午
 */
@Data
@ApiModel(description = "博客详情VO对象")
public class ArticleDetailVO {

    /**
     * 博客id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 博客标题
     */
    @ApiModelProperty("博客标题")
    private String title;

    /**
     * 博客简介
     */
    @ApiModelProperty("博客简介")
    private String summary;

    /**
     * markdown内容
     */
    @ApiModelProperty("markdown内容")
    private String markdownContent;

    /**
     * 博客封面
     */
    @ApiModelProperty("博客封面")
    private String coverUrl;

    /**
     * 博客浏览量
     */
    @ApiModelProperty("博客浏览量")
    private Integer view;

    /**
     * 博客喜欢数
     */
    @ApiModelProperty("博客喜欢数")
    private Integer favorite;

    /**
     * 是否推荐
     * 0 是
     * 1 不是
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 不是")
    private Integer recommend;

    /**
     * 是否是草稿
     * 0 是
     * 1 不是
     */
    @ApiModelProperty(value = "是否草稿", notes = "0 是 1 不是")
    private Integer draft;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @ApiModelProperty("最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    /**
     * 博客的标签列表
     */
    @ApiModelProperty("博客的标签列表")
    private List<Tag> tagList;

    /**
     * 博客的发布作者
     */
    @ApiModelProperty("博客的发布作者")
    private String author;
}
