package cn.ljtnono.re.common.enumeration;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 22:22
 * Description: 各种状态值枚举
 */
public enum StatusEnum {

    /** 实体类是否删除   未删除 */
    ENTITY_IS_DELETED_NOT_DELETED(0),
    /** 实体类是否删除   已删除 */
    ENTITY_IS_DELETED_DELETED(1),
    ;

    /** 状态值 */
    private final Integer value;

    public Integer getValue() {
        return value;
    }

    StatusEnum(Integer value) {
        this.value = value;
    }
}
