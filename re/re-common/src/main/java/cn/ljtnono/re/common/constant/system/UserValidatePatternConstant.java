package cn.ljtnono.re.common.constant;

import java.util.regex.Pattern;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 17:26
 * Description: 用户校验正则常量
 */
public class UserValidatePatternConstant {

    /** 匹配中文或者数字或者字母 */
    public static final Pattern REGEX_CHINESE_OR_NUMBER_LETTER = Pattern.compile("^[a-z0-9A-Z\\u4e00-\\u9fa5]+$");

    /** 匹配用户名，4-20位非空白字符 */
    public static final Pattern REGEX_USERNAME = Pattern.compile("^\\w{4,20}$");

    /** 匹配大写字母 */
    public static final Pattern REGEX_UPPER_LETTER = Pattern.compile("[A-Z]");

    /** 匹配小写字母 */
    public static final Pattern REGEX_LOWER_LETTER = Pattern.compile("[a-z]");

    /** 匹配数字 */
    public static final Pattern REGEX_NUMBER = Pattern.compile("[0-9]");

    /** 密码特殊字符 */
    public static final Pattern REGEX_PASSWORD_SPECIAL_PATTERN = Pattern.compile("[!@#\\-]+");

    /** 邮箱校验 */
    public static final Pattern REGEX_EMAIL = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /** 手机校验 */
    public static final Pattern REGEX_PHONE = Pattern.compile("^1[3456789]\\d{9}$");

    /**
     * 用户名校验
     * @param username 用户名
     * @return true: 校验通过 false: 校验失败
     * @author Ling, Jiatong
     */
    public static boolean checkUsername(final String username) {
        return REGEX_USERNAME.matcher(username).matches();
    }

    /**
     * 校验密码 8-16位，
     * @param password 密码
     * @return 成功返回true 失败返回false
     * @author Ling, Jiatong
     *
     */
    public static boolean checkPassword(final String password) {
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }
        int ls = 0;
        if (REGEX_UPPER_LETTER.matcher(password).find()) {
            ls++;
        }
        if (REGEX_NUMBER.matcher(password).find()) {
            ls++;
        }
        if (REGEX_LOWER_LETTER.matcher(password).find()) {
            ls++;
        }
        if (REGEX_PASSWORD_SPECIAL_PATTERN.matcher(password).find()) {
            ls++;
        }
        // 满足其中两种就行
        if (ls < 2) {
            return false;
        }
        return true;
    }

    /**
     * 校验用户邮箱
     * @param email 用户邮箱
     * @return 校验成功返回true，校验失败返回false
     * @author Ling, Jiatong
     *
     */
    public static boolean checkEmail(final String email) {
        return REGEX_EMAIL.matcher(email).matches();
    }

    /**
     * 校验用户手机号
     * @param phone 用户手机号
     * @return 校验成功返回true，校验失败返回false
     * @author Ling, Jiatong
     *
     */
    public static boolean checkPhone(final String phone) {
        return REGEX_PHONE.matcher(phone).matches();
    }
}
