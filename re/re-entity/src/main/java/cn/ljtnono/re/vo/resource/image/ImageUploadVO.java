package cn.ljtnono.re.vo.resource.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 上传图片返回VO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/8/27 0:32
 */
@Data
@ToString
@ApiModel(description = "上传图片返回VO对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageUploadVO {

    /**
     * 是否成功
     * 0 失败
     * 1 成功
     */
    @ApiModelProperty(value = "是否成功", notes = "0 失败 1 成功")
    private Integer result;

    /**
     * 图片id
     */
    @ApiModelProperty(value = "图片id")
    private String imageId;

    /**
     * 图片访问url
     */
    @ApiModelProperty(value = "图片访问url")
    private String url;

    /**
     * 图片存放路径
     */
    @ApiModelProperty(value = "图片存放路径")
    private String savePath;
}
