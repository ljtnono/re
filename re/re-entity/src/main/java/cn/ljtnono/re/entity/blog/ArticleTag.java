package cn.ljtnono.re.entity.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 博客标签关联表实体
 *
 * @author Ling, Jiatong
 * Date: 2021/2/24 1:07 上午
 */
@Data
@TableName("blog_article_tag")
@ApiModel(description = "博客标签关联表实体")
public class ArticleTag {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    private Integer articleId;

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Integer tagId;
}
