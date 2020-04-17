package cn.rootelement.entity;

import cn.rootelement.entity.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 存储图片信息的实体
 * @author ljt
 * @date 2019/10/28
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReImage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5419098997286534575L;

    /** 图片存储的id */
    @TableId
    private String id;

    /** 图片的文件名 */
    private String originName;

    /** 图片大小 B为单位*/
    private Long size;

    /** 图片的类型，主要是后缀名 */
    private String type;

    /** 图片的访问url */
    private String url;

    /** 图片的上传者 */
    private String owner;

}
