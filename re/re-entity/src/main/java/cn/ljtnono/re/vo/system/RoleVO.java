package cn.ljtnono.re.vo.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/**
 * @author ljt
 * Date: 2020/8/24 23:39
 * Description:
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleVO {

    /** 角色id */
    private Integer id;

    /** 角色名 */
    private String name;
}
