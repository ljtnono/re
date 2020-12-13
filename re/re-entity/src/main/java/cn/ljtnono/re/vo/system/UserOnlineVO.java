package cn.ljtnono.re.vo.system;

import lombok.Data;
import lombok.ToString;

/**
 * <p>用户在线统计VO对象，此对象新增角色之后可以新增相关字段</p>
 * @author Ling, Jiatong
 * Date: 2020/12/9 0:04
 */
@Data
@ToString
public class UserOnlineVO {

    /** 总在线人数 */
    private Integer totalNum;

    /** 超级管理员数 */
    private Integer adminNum;

    /** 游客数量 */
    private Integer touristNum;

    public UserOnlineVO() {
        totalNum = 0;
        touristNum = 0;
        adminNum = 0;
    }
}
