package cn.ljtnono.re.advice;

import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.exception.GlobalToViewException;
import cn.ljtnono.re.pojo.JsonResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 * 配置springRest风格错误
 *
 * @author ljt
 * @version 1.0.3
 * @date 2020/1/18
 */
@ControllerAdvice
public class ReControllerExceptionAdvice {

    /**
     * 处理需要返回Json数据的异常
     * @param e 异常信息
     * @return JsonResult 携带异常信息
     */
    @ExceptionHandler({GlobalToJsonException.class})
    @ResponseBody
    public JsonResult globalToJsonExceptionHandler(GlobalToJsonException e) {
        return JsonResult.newBuilder()
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
    public JsonResult illegalArgumentExceptionHandler() {
        return JsonResult.newBuilder()
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
}
