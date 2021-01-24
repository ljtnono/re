package cn.ljtnono.re.common.annotation;

import java.lang.annotation.*;

/**
 * 当前用户
 *
 * @author Ling, Jiatong
 * Date: 2020/10/8 0:06
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
