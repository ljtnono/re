package cn.ljtnono.re.common.constant;

import java.util.regex.Pattern;

/**
 * @author ljt
 * Date: 2020/8/9 17:26
 * Description: 用户校验正则常量
 */
public class UserValidatePatternConstant {

    /** 用户名校验 4到16位（字母，数字，下划线，减号） */
    public static final Pattern USERNAME_VALIDATE_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{4,16}$");

    /** 密码校验  6-20位字符，必须包括字母和数字与特殊字符（!@#$%^&*?）的其中一种*/
    public static final Pattern PASSWORD_VALIDATE_PATTERN = Pattern.compile("^.*(?=.{6,20})(?=.*[a-zA-Z])(?=.*[0-9!@#$%^&*?]).*$");

    /** 邮箱校验 */
    public static final Pattern EMAIL_VALIDATE_PATTERN = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /** 手机校验 */
    public static final Pattern PHONE_VALIDATE_PATTERN = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\\\d{8}$");
}
