package cn.ljtnono.re.common.vo;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/12 23:53 下午
 * Description: HTTP Restful Api 统一返回值封装
 */
@Data
@ToString
public class ReJsonResultVO<T> implements Serializable {

    private static final long serialVersionUID = 7266682980444049698L;

    public static final Integer CODE_SUCCESS = 0;

    public static final String MESSAGE_SUCCESS = "success";

    public static final String MESSAGE_FAILED = "failed";

    @ApiModelProperty(value = "响应码，0为请求成功，非0请求失败")
    private Integer code;

    @ApiModelProperty(value = "响应信息，请求成功时为success，请求失败时为具体错误信息", example = "success")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ReJsonResultVO(T data) {
        this.code = CODE_SUCCESS;
        this.message = MESSAGE_SUCCESS;
        this.data = data;
    }

    /**
     *
     * @return ReJsonResultVO<T>
     */
    public static <T> ReJsonResultVO<T> success() {
        return ReJsonResultVO.success(null);
    }

    /**
     * 私有构造方法 供失败响应调用
     *
     * @param code    响应码
     * @param message 错误信息
     */
    private ReJsonResultVO(Integer code, String message) {
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
    public static <T> ReJsonResultVO<T> success(T data) {
        return new ReJsonResultVO<T>(data);
    }
    /**
     * 失败响应 传入code和message
     *
     * @param code    响应码
     * @param message 错误信息
     * @param <T>     范型
     * @return 统一返回值封装
     */
    public static <T> ReJsonResultVO<T> error(Integer code, String message) {
        return new ReJsonResultVO<>(code, message);
    }

    /**
     * 失败响应 传入错误枚举
     *
     * @param reErrorEnum HTTP Restful Api 错误枚举
     * @param <T>       范型
     * @return 统一返回值封装
     */
    public static <T> ReJsonResultVO<T> error(ReErrorEnum reErrorEnum) {
        return new ReJsonResultVO<>(reErrorEnum.getCode(), reErrorEnum.getMessage());
    }
}
