package cn.ljtnono.re.enumeration;

/**
 * 实体类状态枚举
 * @author ljt
 * @date 2019/12/19
 * @version 1.0.0
 */
public enum ReStatusEnum {

    /** 已经删除 */
    DELETE(0, "删除"),
    /** 正常 */
    NORMAL(1, "正常");

    /** 值 */
    private Integer code;

    /** 状态 */
    private String msg;

    ReStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
