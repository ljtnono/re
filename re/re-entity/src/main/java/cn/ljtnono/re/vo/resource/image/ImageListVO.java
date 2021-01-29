package cn.ljtnono.re.vo.resource.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 图片列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/28 0:08
 */
@Data
@ToString
@ApiModel(description = "图片列表VO对象")
public class ImageListVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 图片id
     */
    @ApiModelProperty(value = "图片id")
    private String imageId;

    /**
     * 图片名
     */
    @ApiModelProperty(value = "图片名")
    private String originName;

    /**
     * 访问url
     */
    @ApiModelProperty(value = "访问url")
    private String url;

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
     * 图片大小
     * 字节大小
     */
    @ApiModelProperty(value = "图片大小")
    private Long size;

    /**
     * 图片所属分类
     * 1 博客图片
     * 2 全局配置图片
     */
    @ApiModelProperty(value = "图片所属分类")
    private Integer type;
}
