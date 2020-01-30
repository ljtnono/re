package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 图片查询DTO对象
 * @author ljt
 * @date 2020/1/30
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReImageSearchDTO implements Serializable {

    private static final long serialVersionUID = -2427324584438423258L;

    private String originName;

    private String type;

    private String owner;
}
