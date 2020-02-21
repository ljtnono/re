package cn.ljtnono.re.aop;

import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.exception.GlobalToViewException;
import cn.ljtnono.re.vo.JsonResultVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    @ExceptionHandler({GlobalToJsonException.class})
    @ResponseBody
    public JsonResultVO globalToJsonExceptionHandler(GlobalToJsonException e) {
        return JsonResultVO.newBuilder()
                .message(e.getGlobalErrorEnum().getErrorMsg())
                .data(null)
                .request("fail")
                .totalCount(null)
                .status(e.getGlobalErrorEnum().getErrorCode())
                .build();
    }

    /**
     * 处理参数异常信息
     * @return JsonResult 携带异常信息
     */
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseBody
    public JsonResultVO illegalArgumentExceptionHandler() {
        return JsonResultVO.newBuilder()
                .message(GlobalErrorEnum.PARAM_ERROR.getErrorMsg())
                .data(null)
                .request("fail")
                .totalCount(null)
                .status(GlobalErrorEnum.PARAM_ERROR.getErrorCode())
                .build();
    }

    /**
     * 处理需要返回错误页面的异常
     * @param e 异常信息
     * @return ModelAndView 携带了错误信息和页面
     */
    @ExceptionHandler({GlobalToViewException.class})
    public ModelAndView globalToViewExceptionHandler(GlobalToViewException e) {
        return new ModelAndView("error/fail")
                .addObject("errorCode", e.getGlobalErrorEnum().getErrorCode())
                .addObject("errorMsg", e.getGlobalErrorEnum().getErrorMsg());
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
        JsonResultVO resultVO = JsonResultVO.fail(GlobalErrorEnum.PARAM_ERROR.getErrorCode());
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
}
