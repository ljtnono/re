package cn.ljtnono.re.common.enumeration.system;

import lombok.Getter;

/**
 * 系统角色枚举类
 *
 * @author Ling, Jiatong
 * Date: 2021/1/1 23:59
 */
@Getter
public enum RoleEnum {

    // 超管
    ADMIN(1, "超级管理员"),
    // 测试
    TEST(2, "测试人员"),
    // 普通游客
    TOURIST(3, "游客")
    ;

    /**
     * 角色id
     */
    private final Integer id;

    /**
     * 角色名
     */
    private final String name;

    RoleEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
