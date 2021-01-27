package cn.ljtnono.re.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * 加密工具
 *
 * @author Ling, Jiatong
 * Date: 2020/7/8 9:49 上午
 */
public class EncryptUtil {

    /**
     * 工具类不允许实例化
     */
    private EncryptUtil() {
    }

    public static EncryptUtil getInstance() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        private final static EncryptUtil INSTANCE = new EncryptUtil();
    }

    /**
     * 使用MD5对字符串进行加密
     *
     * @param source 源字符串
     * @return MD5加密后的字符串
     */
    public String getMd5(String source) {
        return getMd5(source.getBytes());
    }

    /**
     * MD5加密算法
     *
     * @param source 源字节数组
     * @return 加密后的字符串
     */
    public String getMd5(byte[] source) {
        String s = null;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] tmp;
            synchronized (EncryptUtil.class) {
                md.update(source);
                tmp = md.digest();
            }
            char[] str = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            // 换后的结果转换为字符串
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * MD5加密转小写
     *
     * @param source 源字符串
     * @return MD5小写加密字符串
     */
    public String getMd5LowerCase(String source) {
        return getMd5(source).toLowerCase();
    }

    /**
     * MD5加密转大写
     *
     * @param source 源字符串
     * @return MD5大写加密
     */
    public String getMd5UpperCase(String source) {
        return getMd5(source).toUpperCase();
    }
}
