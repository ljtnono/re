package cn.rootelement.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * md.editor编辑器上传图片返回VO
 * @author ljt
 * @date 2020/2/23
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class MarkdownEditorUploadImageVO implements Serializable {

    private static final long serialVersionUID = -7372259841156483967L;

    /** 图片url */
    private String url;

    /** 1 成功 0 失败 */
    private Integer success;

    /** 消息 */
    private String message;

}
