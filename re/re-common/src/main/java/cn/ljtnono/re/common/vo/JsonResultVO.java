package cn.ljtnono.re.common.vo;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/**
 * <p>HTTP Restful Api 统一返回值封装</p>
 * @author Ling, Jiatong
 * Date: 2020/7/12 23:53 下午
 */
@Data
@ToString
public class JsonResultVO<T> {

    public static final Integer CODE_SUCCESS = 0;

    public static final String MESSAGE_SUCCESS = "success";

    public static final String MESSAGE_FAILED = "failed";

    private Integer code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public JsonResultVO(T data) {
        this.code = CODE_SUCCESS;
        this.message = MESSAGE_SUCCESS;
        this.data = data;
    }

    /**
     *
     * @return ReJsonResultVO<T>
     */
    public static <T> JsonResultVO<T> success() {
        return JsonResultVO.success(null);
    }

    /**
     * 私有构造方法 供失败响应调用
     *
     * @param code    响应码
     * @param message 错误信息
     */
    private JsonResultVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 成功响应 仅传入data
     *
     * @param data 数据
     * @param <T>  范型
     * @return 统一返回值封装
     */
    public static <T> JsonResultVO<T> success(T data) {
        return new JsonResultVO<T>(data);
    }
    /**
     * 失败响应 传入code和message
     *
     * @param code    响应码
     * @param message 错误信息
     * @param <T>     范型
     * @return 统一返回值封装
     */
    public static <T> JsonResultVO<T> error(Integer code, String message) {
        return new JsonResultVO<>(code, message);
    }

    /**
     * 失败响应 传入错误枚举
     *
     * @param globalErrorEnum HTTP Restful Api 错误枚举
     * @param <T>       范型
     * @return 统一返回值封装
     */
    public static <T> JsonResultVO<T> error(GlobalErrorEnum globalErrorEnum) {
        return new JsonResultVO<>(globalErrorEnum.getCode(), globalErrorEnum.getMessage());
    }
}
