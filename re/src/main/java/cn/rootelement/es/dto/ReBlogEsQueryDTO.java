package cn.rootelement.es.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询条件封装
 * @author ljt
 * @date 2020/3/21
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReBlogEsQueryDTO implements Serializable {

    private static final long serialVersionUID = -7829460048598956068L;

    private String title;

    private String author;

    private String summary;

    private String contentMarkdown;
}
