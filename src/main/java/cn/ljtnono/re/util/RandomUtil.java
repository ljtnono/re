package cn.ljtnono.re.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 随机工具类
 *
 * @author ljt
 * @version 1.0.0
 * 随机数工具类
 * @date 2019/12/19
 */
public class RandomUtil {

    private static final String[] DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private static final String[] HEXSTR_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static final String[] ALLSTR_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z"};


    private RandomUtil() {
        // 私有类构造方法
    }


    /***
     * 获取指定长度数字字符串
     */
    public static String getRandomNumStr(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            String temp = DIGITS[random.nextInt(DIGITS.length)];

            sb.append(temp);
        }
        return sb.toString();
    }


    /***
     * 获取指定长度随机字符串
     */
    public static String getRandomStr(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            String temp = ALLSTR_DIGITS[random.nextInt(ALLSTR_DIGITS.length)];

            sb.append(temp);
        }
        return sb.toString();
    }


    /**
     * 返回指定长度的字母加数字随机数
     */
    public static String getRandomNumWordStr(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (choice + random.nextInt(26)));
                // 数字
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString();
    }


    /***
     * 获取指定长度16进制字符串
     */
    public static String getRandomHexStr(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            String temp = HEXSTR_DIGITS[random.nextInt(HEXSTR_DIGITS.length)];

            sb.append(temp);
        }
        return sb.toString();
    }


    /***
     * 根据UUID作为引子的随机数字符串
     */
    public static String getRandomUuidStr(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random(UUID.randomUUID().hashCode());
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }


    /**
     * 生产UUID
     */
    public static String getUuid() {
        return UUID.randomUUID().toString();
    }


    /**
     * 生产不带横线的UUID
     */
    public static String getUuidNoLine() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String creatId(int endIndex) {
        String id = "";
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String tc = sf.format(new Date());
        double du = Math.random();
        String random = String.valueOf(du);
        random = random.length() < 18 ? random + "000" : random;
        String sj = random.substring(2, 18);
        id = (tc + sj).substring(0, endIndex);
        return id;
    }

    public static void main(String[] args) {
//		System.out.println(getRandomNumStr(6));
//		System.out.println(getRandomStr(6));
//		System.out.println(getRandomNumWordStr(6));
//		System.out.println(getRandomHexStr(6));
//		System.out.println(getRandomUuidStr(6));
//		System.out.println(getUuid());
//		System.out.println(getUuidNoLine());
        System.out.println(creatId(30));
    }
}
