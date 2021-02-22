package cn.ljtnono.re.vo.blog.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 博客类型详情VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/2/22 11:16 下午
 */
@Data
@ApiModel(description = "博客类型详情VO对象")
public class TypeDetailVO {

    /**
     * 博客类型id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 博客类型名
     */
    @ApiModelProperty(value = "类型名")
    private String name;

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
     * 博客类型总浏览量
     */
    @ApiModelProperty(value = "博客类型总浏览量")
    private Integer view;

    /**
     * 博客类型总喜欢数
     */
    @ApiModelProperty(value = "博客类型总喜欢数")
    private Integer favorite;

    /**
     * 是否推荐
     * 0 是
     * 1 不是
     */
    @ApiModelProperty(value = "是否推荐", notes = "0 是 1 不是")
    private Integer recommend;
}
