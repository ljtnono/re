package cn.rootelement.aop;

import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.vo.JsonResultVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置springRest风格错误
 *
 * @author ljt
 * @version 1.0.3
 * @date 2020/1/18
 */
@ControllerAdvice
@Slf4j
public class ReControllerExceptionAdvice {

    /**
     * 处理需要返回Json数据的异常
     * @param e 异常信息
     * @return JsonResult 携带异常信息
     */
    @ExceptionHandler({GlobalToJsonException.class, IllegalArgumentException.class})
    @ResponseBody
    public JsonResultVO globalToJsonExceptionHandler(GlobalToJsonException e) {
        return JsonResultVO.newBuilder()
                .message(e.getHttpStatusEnum().getMsg())
                .data(null)
                .request("fail")
                .totalCount(null)
                .status(e.getHttpStatusEnum().getCode())
                .build();
    }


    @ResponseBody
    @ExceptionHandler(BindException.class)
    public JsonResultVO bindExceptionHandler(BindException e) {
        return handleValidatedException(e);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResultVO methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return handleValidatedException(e);
    }

    /**
     * 处理controller参数校验异常
     * @param e 参数校验异常
     * @return JsonResultVO
     */
    public JsonResultVO handleValidatedException(Exception e) {
        JsonResultVO resultVO = JsonResultVO.fail(HttpStatusEnum.PARAM_ERROR.getCode());
        JSONObject errorProperty = new JSONObject();
        BindingResult bindingResult = ((BindException) e).getBindingResult();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> {
                String field = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                errorProperty.accumulate(field, defaultMessage);
            });
            resultVO.addField("error", errorProperty);
        }
        resultVO.setMessage("参数错误");
        return resultVO;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public JsonResultVO handlerException(AccessDeniedException e){
        JsonResultVO fail = JsonResultVO.fail(HttpStatusEnum.FORBIDDEN.getCode());
        fail.setMessage("请求失败！权限不足");
        return fail;
    }
}
