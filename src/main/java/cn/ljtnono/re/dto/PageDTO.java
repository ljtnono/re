package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author ljt
 * @date 2019/12/12
 * @version 1.0.1
 */
@Data
@ToString
@NoArgsConstructor
public class PageDTO implements Serializable {

    private static final long serialVersionUID = -3320275112750620130L;

    /** 请求的页数 */
    @NotNull
    @Min(value = 1, message = "请求页数不能小于1")
    private Integer page;

    /** 每页显示的条数 */
    @NotNull
    @Min(value = 10, message = "每页条数不能小于10")
    private Integer count;

}
