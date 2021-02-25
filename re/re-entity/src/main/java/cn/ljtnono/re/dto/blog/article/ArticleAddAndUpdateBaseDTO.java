package cn.ljtnono.re.dto.blog.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 新增和更新博客文章基类DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/26 12:44 上午
 */
@Data
@ApiModel(description = "新增和更新博客文章基类DTO对象")
public class ArticleAddAndUpdateBaseDTO {

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题", notes = "不超过50个字符")
    private String title;

    /**
     * 文章简介
     */
    @ApiModelProperty(value = "文章简介", notes = "不超过250个字符")
    private String summary;

    /**
     * 文章markdown内容
     */
    @ApiModelProperty(value = "文章markdown内容")
    private String markdownContent;

    /**
     * 发表用户id
     */
    @ApiModelProperty(value = "发表用户id")
    private Integer userId;

    /**
     * 文章所属类型id
     */
    @ApiModelProperty(value = "文章所属类型id")
    private Integer typeId;

    /**
     * 文章封面图片url
     */
    @ApiModelProperty(value = "文章封面图片url")
    private String coverUrl;

    /**
     * 是否推荐
     * 0 是
     * 1 否
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 否")
    private Integer recommend;

    /**
     * 博客标签列表
     */
    @ApiModelProperty(value = "博客标签列表")
    private List<String> tagList;

}
