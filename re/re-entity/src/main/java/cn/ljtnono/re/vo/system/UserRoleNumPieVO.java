package cn.ljtnono.re.vo.system;

import lombok.Data;
import lombok.ToString;

/**
 * <p>用户根据角色分类统计饼状图VO对象</p>
 * @author Ling, Jiatong
 * Date: 2020/12/7 2:16
 */
@Data
@ToString
public class UserRoleNumPieVO {

    /** 角色名 */
    private String roleName;

    /** 用户个数 */
    private Integer num;
}
