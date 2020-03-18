package cn.ljtnono.re.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 链接类型查询DTO
 * @author ljt
 * @date 2020/1/13
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReLinkSearchDTO implements Serializable {

    private static final long serialVersionUID = 8353561336347139779L;

    /** 链接的url */
    private String url;

    /** 链接名 */
    private String name;

    /** 链接所属类型 */
    private String type;
}
