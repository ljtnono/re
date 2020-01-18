package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户查询DTO对象
 * @author ljt
 * @date 2020/1/18
 * @version 1.0.2
 */
@Data
@ToString
@NoArgsConstructor
public class ReUserSearchDTO implements Serializable {

    private static final long serialVersionUID = -7807409596603603785L;

    private String username;

    private String tel;

    private String email;
}
