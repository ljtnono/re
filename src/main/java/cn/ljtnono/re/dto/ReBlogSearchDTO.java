package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 博客查询DTO对象封装
 * @author ljt
 * @date 2020/2/14
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReBlogSearchDTO implements Serializable {

    private static final long serialVersionUID = -5072075640967713543L;

    private String title;

    private String type;

    private String author;

}
