package cn.ljtnono.re.common.enumeration;

/**
 * 实体类常量值枚举
 *
 * @author Ling, Jiatong
 * Date: 2020/8/9 22:22
 */
public enum EntityConstantEnum {

    //******************** 通用实体 ********************//

    /** 实体类是否删除   未删除 */
    ENTITY_IS_DELETED_NOT_DELETED(0),
    /** 实体类是否删除   已删除 */
    ENTITY_IS_DELETED_DELETED(1),

    //******************** 系统日志 ********************//
    /** 系统日志操作结果  成功 */
    LOG_RESULT_SUCCESS(1),
    /** 系统日志操作结果  失败*/
    LOG_RESULT_FAILED(2),
    /** 系统日志类型  用户日志 */
    LOG_TYPE_USER(1),
    /** 系统日志类型  系统日志 */
    LOG_TYPE_SYSTEM(2),


    ;

    /** 状态值 */
    private final Integer value;

    public Integer getValue() {
        return value;
    }

    EntityConstantEnum(Integer value) {
        this.value = value;
    }
}
