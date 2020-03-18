package cn.ljtnono.re.enumeration;

/**
 * 根元素角色枚举类
 * @author ljt
 * @date 2019/11/15
 * @version 1.0
 */
public enum ReRoleEnum {

    /** 超级管理员 */
    ROLE_ROOT(1001, "超级管理员", "root"),

    /** 管理员 */
    ROLE_ADMIN(1002, "管理员", "admin"),

    /** 普通用户 */
    ROLE_USER(1003, "普通用户", "user");

    /** 角色id */
    private Integer id;

    /** 角色描述 */
    private String description;

    /** 角色名 */
    private String name;

    ReRoleEnum(Integer id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
