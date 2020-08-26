package cn.ljtnono.re.vo.base;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljt
 * Date: 2020/8/27 0:32
 * Description:
 */
@Data
@ToString
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 4593999010097299274L;

    /**
     * id
     */
    private Integer id;

    /**
     * 是否删除 0 正常 1 删除
     */
    private Integer deleted;
}
