package cn.ljtnono.re.dto.resource.image;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/25 0:19
 */
@Data
@ToString
@ApiModel(description = "图片上传DTO对象")
public class ImageUploadDTO {

    /**
     * 图片md5值
     */
    @ApiModelProperty(value = "图片md5值")
    private String md5;

    /**
     * 上传的图片
     */
    @ApiModelProperty(value = "上传图片")
    private MultipartFile file;

    /**
     * 上传的图片类型
     */
    @ApiModelProperty(value = "图片类型", notes = "1 博客类型 2 全局配置")
    private Integer type;
}
