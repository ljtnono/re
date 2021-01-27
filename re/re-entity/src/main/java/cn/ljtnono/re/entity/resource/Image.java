package cn.ljtnono.re.entity.resource;

import cn.ljtnono.re.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 图片对象
 *
 * @author Ling, Jiatong
 * Date: 2020/7/30 1:04
 */
@Data
@TableName("rs_image")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Image extends BaseEntity {

    /**
     * 图片id
     * UUID 32位
     */
    private String imageId;

    /**
     * 图片的原名
     * 带后缀名
     */
    private String originName;

    /**
     * 图片访问url
     */
    private String url;

    /**
     * 图片大小
     * 字节大小
     */
    private Long size;

    /**
     * 图片存放路径
     */
    private String savePath;

    /**
     * 图片的MD5值
     */
    private String md5;

    /**
     * 是否删除 1 删除 0 正常
     */
    @TableField("is_deleted")
    private Integer deleted;

    /**
     * 图片后缀名
     */
    private String suffix;

    /**
     * 图片类型
     * 0
     */
    private Integer type;
}
