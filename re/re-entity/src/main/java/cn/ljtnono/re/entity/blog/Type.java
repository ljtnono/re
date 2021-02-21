package cn.ljtnono.re.entity.blog;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 博客类型实体
 *
 * @author Ling, Jiatong
 * Date: 2020/7/30 1:03
 */
@Data
@TableName("blog_type")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "博客类型实体")
public class Type extends BaseEntity {

    /**
     * 类型名
     */
    @ApiModelProperty(value = "类型名")
    private String name;

    /**
     * 是否删除
     * 0 正常
     * 1 已删除
     */
    @TableField("is_deleted")
    @ApiModelProperty(value = "是否删除", notes = "0 正常 1 已删除")
    private Integer deleted;

    /**
     * 该分类的总浏览量
     */
    @ApiModelProperty(value = "类型总浏览量")
    private Integer view;

    /**
     * 类型总浏览量
     */
    @ApiModelProperty(value = "类型总喜欢数")
    private Integer favorite;

    /**
     * 是否推荐类型
     * 0 是
     * 1 不是
     */
    @TableField("is_recommend")
    @ApiModelProperty(value = "是否推荐类型", notes = "0 是 1 不是")
    private Integer recommend;
}
