package cn.ljtnono.re.common.advice;

import cn.ljtnono.re.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/24 1:19
 * Description: 日志切面
 */
@Aspect
@Component
@Slf4j
public class GlobalLogAdvice {

    /** 进入方法时间戳 */
    private Long startTime;

    /** 方法结束时间戳(计时) */
    private Long endTime;

    public GlobalLogAdvice() {}

    /**
     * 定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径
     */
    @Pointcut("execution(public * cn.ljtnono.re.api.controller..*.*.*(..))")
    public void webLogPointcut() {}

    /**
     * 前置通知：
     * 1. 在执行目标方法之前执行，比如请求接口之前的登录验证;
     * 2. 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
     *
     * @param joinPoint 切点
     */
    @Before("webLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        //打印请求的内容
        startTime = System.currentTimeMillis();
//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        assert attributes != null;
//        HttpServletRequest request = attributes.getRequest();
//        log.info("请求开始时间：{}", DateUtil.formatDate(new Date(), DateUtil.DateStyleEnum.yyyy_MM_dd_HH_mm_ss));
//        log.info("请求Url : {}", request.getRequestURL().toString());
//        log.info("请求方式 : {}", request.getMethod());
//        log.info("请求ip : {}", request.getRemoteAddr());
//        log.info("请求方法 : {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        log.info("请求参数 : {}", Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 返回通知：
     * 1. 在目标方法正常结束之后执行
     * 1. 在返回通知中补充请求日志信息，如返回时间，方法耗时，返回值，并且保存日志信息
     *
     * @param ret 返回参数
     */
    @AfterReturning(returning = "ret", pointcut = "webLogPointcut()")
    public void doAfterReturning(Object ret) {
        endTime = System.currentTimeMillis();
        log.info("请求结束时间：{}", DateUtil.formatDate(new Date(), DateUtil.DateStyleEnum.yyyy_MM_dd_HH_mm_ss));
        log.info("请求耗时：{}", (endTime - startTime));
    }

    /**
     * 异常通知：
     * 1. 在目标方法非正常结束，发生异常或者抛出异常时执行
     * 1. 在异常通知中设置异常信息，并将其保存
     *
     * @param throwable 异常
     */
    @AfterThrowing(value = "webLogPointcut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        // 保存异常日志记录
        log.error("发生异常时间：{}", DateUtil.formatDate(new Date(), DateUtil.DateStyleEnum.yyyy_MM_dd_HH_mm_ss));
        log.error("抛出异常：{}", throwable.getMessage());
    }

}
