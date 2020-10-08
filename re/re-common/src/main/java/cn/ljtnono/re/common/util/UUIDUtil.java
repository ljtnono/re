package cn.ljtnono.re.common.util;

import java.util.UUID;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/21 10:01 上午
 * Description: UUID工具类
 */
public class UUIDUtil {

    /**
     * 生成UUID
     *
     * @return UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成简单UUID, 去除-
     * @return UUID字符串
     */
    public static String generateSimpleUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
