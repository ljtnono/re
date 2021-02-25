package cn.ljtnono.re.vo.blog.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 博客文章列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/25 11:17 下午
 */
@Data
@ApiModel(description = "博客文章列表VO对象")
public class ArticleListVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
     * 文章简介
     */
    @ApiModelProperty(value = "文章简介")
    private String summary;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    /**
     * 发布者
     */
    @ApiModelProperty(value = "文章发布者")
    private String username;

    /**
     * 浏览量
     */
    @ApiModelProperty(value = "文章浏览量")
    private Integer view;

    /**
     * 文章喜欢数量
     */
    @ApiModelProperty(value = "文章喜欢数量")
    private Integer favorite;

    /**
     * 是否推荐
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 不是")
    private Integer recommend;
}
