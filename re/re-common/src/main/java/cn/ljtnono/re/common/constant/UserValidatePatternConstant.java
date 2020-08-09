package cn.ljtnono.re.common.constant;

import java.util.regex.Pattern;

/**
 * @author ljt
 * Date: 2020/8/9 17:26
 * Description: 用户校验正则常量
 */
public class UserValidatePatternConstant {

    /** 用户名校验 TODO 正则表达式待完成 */
    public static final Pattern USERNAME_VALIDATE_PATTERN = Pattern.compile("^\\w{4,20}$");

    /** 密码校验 TODO 正则表达式待完成 */
    public static final Pattern PASSWORD_VALIDATE_PATTERN = Pattern.compile("^\\w{4,20}$");

    /** 邮箱校验 */
    public static final Pattern EMAIL_VALIDATE_PATTERN = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /** 手机校验 */
    public static final Pattern PHONE_VALIDATE_PATTERN = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\\\d{8}$");
}
