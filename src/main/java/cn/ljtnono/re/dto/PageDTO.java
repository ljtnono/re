package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author ljt
 * @date 2019/12/12
 * @version 1.0.1
 */
@Data
@ToString
public class PageDTO implements Serializable {

    private static final long serialVersionUID = -3320275112750620130L;

    /** 请求的页数 */
    private Integer page;

    /** 每页显示的条数 */
    private Integer count;

}
