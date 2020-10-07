package cn.ljtnono.re.dto.base;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/13 23:27 下午
 * Description: DTO基类
 */
@Data
@ToString
public class ReBaseDTO implements Serializable {

    private static final long serialVersionUID = -212253609435316598L;

    /** id */
    private Integer id;

    /** 分页页数 默认为1 */
    private Integer pageNum = 1;

    /** 每页条数 默认为10 */
    private Integer pageSize = 10;
}
