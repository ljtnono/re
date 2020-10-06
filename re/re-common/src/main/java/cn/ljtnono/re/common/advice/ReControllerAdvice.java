package cn.ljtnono.re.common.advice;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceAlreadyExistException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.businese.BusinessException;
import cn.ljtnono.re.common.exception.businese.UserValidateException;
import cn.ljtnono.re.common.exception.security.TokenExpiredException;
import cn.ljtnono.re.common.exception.security.UserPermissionException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.common.exception.system.SystemException;
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
@Slf4j
@RestControllerAdvice
public class ReControllerAdvice {

    /**
     * 未知异常捕获
     *
     * @param exception 未知异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ReJsonResultVO<?> unknownException(Exception exception) {
        log.error(exception.toString(), exception);
        return ReJsonResultVO.error(500, "未知异常");
    }

    /**
     * 全局异常捕获
     *
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
     * 请求参数异常
     *
     * @param exception 请求参数异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(ParamException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> paramException(ParamException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.REQUEST_PARAM_ERROR.getCode(), ReErrorEnum.REQUEST_PARAM_ERROR.getMessage());
    }

    /**
     * 资源已经存在异常
     *
     * @param exception 资源已经存在异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> resourceAlreadyExistException(ResourceAlreadyExistException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.RESOURCE_ALREADY_EXIST_ERROR.getCode(), ReErrorEnum.RESOURCE_ALREADY_EXIST_ERROR.getMessage());
    }

    /**
     * 资源不存在异常
     *
     * @param exception 资源不存在异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(ResourceNotExistException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> resourceNotExistException(ResourceNotExistException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), ReErrorEnum.RESOURCE_NOT_EXIST_ERROR.getMessage());
    }

    /**
     * 系统异常, 主要是IOException或者网络之类的异常
     *
     * @param exception 系统异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> systemException(SystemException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.SYSTEM_ERROR.getCode(), ReErrorEnum.SYSTEM_ERROR.getMessage());
    }

    /**
     * 数据库操作异常
     *
     * @param exception 系统异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(DataBaseException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> dataBaseException(DataBaseException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.DATABASE_OPERATION_ERROR.getCode(), ReErrorEnum.DATABASE_OPERATION_ERROR.getMessage());
    }

    /**
     * 用户权限异常
     *
     * @param exception 用户权限异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(UserPermissionException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> userPermissionException(UserPermissionException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.USER_PERMISSION_ERROR.getCode(), ReErrorEnum.USER_PERMISSION_ERROR.getMessage());
    }

    /**
     * 业务异常
     *
     * @param exception 业务异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> businessException(BusinessException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.BUSINESS_ERROR.getCode(), ReErrorEnum.BUSINESS_ERROR.getMessage());
    }


    /**
     * 用户校验异常
     *
     * @param exception 用户校验异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(UserValidateException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> userValidateException(UserValidateException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.USER_VALIDATE_ERROR.getCode(), ReErrorEnum.USER_VALIDATE_ERROR.getMessage());
    }


    /**
     * token过期异常
     *
     * @param exception token过期异常
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> tokenExpiredException(TokenExpiredException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
            return ReJsonResultVO.error(exception.getReErrorEnum().getCode(), exception.getReErrorEnum().getMessage());
        }
        return ReJsonResultVO.error(ReErrorEnum.TOKEN_EXPIRED_ERROR.getCode(), ReErrorEnum.TOKEN_EXPIRED_ERROR.getMessage());
    }


    /**
     * token过期异常
     *
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> expiredJwtException(ExpiredJwtException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
        }
        return ReJsonResultVO.error(ReErrorEnum.TOKEN_EXPIRED_ERROR.getCode(), ReErrorEnum.TOKEN_EXPIRED_ERROR.getMessage());
    }

    /**
     * token格式错误
     *
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> malformedJwtException(MalformedJwtException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
        }
        return ReJsonResultVO.error(ReErrorEnum.TOKEN_FORMAT_ERROR.getCode(), ReErrorEnum.TOKEN_FORMAT_ERROR.getMessage());
    }

    /**
     * token签名错误
     *
     * @return ReJsonResultVO<?> 自定义异常消息
     */
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReJsonResultVO<?> signatureException(SignatureException exception) {
        if (exception != null) {
            log.error(exception.toString(), exception);
        }
        return ReJsonResultVO.error(ReErrorEnum.TOKEN_SIGNATURE_ERROR.getCode(), ReErrorEnum.TOKEN_SIGNATURE_ERROR.getMessage());
    }
}