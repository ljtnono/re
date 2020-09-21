package cn.ljtnono.re.common.advice;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ljt
 * Date: 2020/8/9 16:36
 * Description: 异常处理器
 */
@RestControllerAdvice
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

    /**
     * token过期异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> expiredJwtException() {
        return ReJsonResultVO.error(ReErrorEnum.TOKEN_EXPIRED_ERROR.getCode(), ReErrorEnum.TOKEN_EXPIRED_ERROR.getMessage());
    }

    /**
     * token格式错误
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> malformedJwtException() {
        return ReJsonResultVO.error(ReErrorEnum.TOKEN_FORMAT_ERROR.getCode(), ReErrorEnum.TOKEN_FORMAT_ERROR.getMessage());
    }

    /**
     * token签名错误
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> signatureException() {
        return ReJsonResultVO.error(ReErrorEnum.TOKEN_SIGNATURE_ERROR.getCode(), ReErrorEnum.TOKEN_SIGNATURE_ERROR.getMessage());
    }
}
