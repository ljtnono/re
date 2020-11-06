package cn.ljtnono.re.vo.system;

import cn.ljtnono.re.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/15 22:29
 * Description: 用户登录VO
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginVO extends BaseVO {

    /**
     * 用户名
     */
    private Integer username;

    /**
     * 登陆token
     */
    private String token;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户权限id列表
     */
    private List<Integer> permissionIdList;

    /**
     * 用户头像图片路径
     */
    private String avatarImage;

    /**
     * menu菜单的结构
     */
    @Data
    @ToString
    public static class MenuItem {

        /** 菜单项的id，对应permission的id */
        private Integer id;

        /** 菜单项的标题，对应permission的标题 */
        private String title;

        /** 菜单项的类型，对应permission类型 */
        private Integer type;

        /** 菜单项的表达式，对应permission表达式 */
        private String expression;

        /** 菜单项的父级菜单id */
        private Integer parentId;

        /** 菜单项子项列表 */
        private List<MenuItem> sub;

    }
}
