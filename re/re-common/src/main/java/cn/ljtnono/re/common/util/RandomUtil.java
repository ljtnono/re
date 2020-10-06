package cn.ljtnono.re.common.util;

import java.util.Random;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/20 18:59
 * Description: 随机工具类
 */
public class RandomUtil {

    private RandomUtil() {}

    public static RandomUtil getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RandomUtil INSTANCE = new RandomUtil();
    }

    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String numberChar = "0123456789";

    /**
     * 生成随机字符串数据
     * @param length 随机串的长度
     * @return String 随机字符串
     */
    public String randomString(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return builder.toString();
    }

    /**
     * 生成随机字母串（只含字母）
     * @param length 随机串长度
     * @return String 随机字母串
     */
    public String randomLetter(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(letterChar.charAt(random.nextInt(letterChar.length())));
        }
        return builder.toString();
    }

    /**
     * 生成随机数字串（只含数字）
     * @param length 随机串长度
     * @return String 随机数字串
     */
    public String randomNumber(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return builder.toString();
    }

}
