package cn.ljtnono.re.dto.system;

import cn.ljtnono.re.dto.base.ReBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljt
 * Date: 2020/7/13 23:26 下午
 * Description: 用户前端传输对象封装
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReUserDTO extends ReBaseDTO implements Serializable {

    private static final long serialVersionUID = 3301630533495460518L;

    private String username;

    private String password;

    private String tel;

    private String qq;

    private String email;

}
