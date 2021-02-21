package cn.ljtnono.re.dto.resource.image;

import cn.ljtnono.re.dto.base.BaseBatchDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 图片批量下载DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/1/29 23:52
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "图片批量下载DTO对象")
public class ImageDownloadBatchDTO extends BaseBatchDTO<Integer> {

    /**
     * 文件压缩类型
     * 1 zip
     * 2 tar.gz
     */
    @ApiModelProperty(value = "压缩类型", example = "1 zip 2 tar.gz")
    private Integer compressType;
}
