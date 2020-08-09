package cn.ljtnono.re.common.advice;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author ljt
 * Date: 2020/8/9 16:36
 * Description:
 */
@ControllerAdvice
@Slf4j
public class ReControllerAdvice {


    /**
     * 全局异常捕获
     * @param exception 全局异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(GlobalException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> globalException(GlobalException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.SYSTEM_ERROR.getCode(), ReErrorEnum.SYSTEM_ERROR.getMessage());
    }




}
