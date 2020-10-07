package cn.ljtnono.re.common.annotation;

import java.lang.annotation.*;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/7 23:39
 * Description: 用来标记是否加入白名单
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {

}
