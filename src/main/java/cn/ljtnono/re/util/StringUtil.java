package cn.ljtnono.re.util;

import java.util.UUID;

/**
 *  字符串处理工具
 *  @author ljt
 *  @date 2020/1/21
 *  @version 1.0.2
*/
public class StringUtil {

    /**
     * 防止工具类进行实例化
     */
    private StringUtil() {}


    /**
     * 判断字符串是否为空
     *
     * @param str 判断的字符串参数
     * @return 如果字符串为null或者空串，返回true，否者返回false
     */
    public static boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }


    /**
     * 判断字符串是否为空格或者空串
     *
     * @param str 判断的字符串参数
     * @return 如果字符串全是空格或者空串或者null，返回true，否者返回false
     */
    public static boolean isBlank(final String str) {
        return isEmpty(str.trim());
    }

    /**
     * 删除
     * @param str 需要删除空格的字符串
     * @return 已经删除空格的字符串, 如果为null 则返回null
     */
    public static String deleteBlank(String str) {
        if (!isEmpty(str)) {
            return str.replaceAll(" ", "");
        }
        return str;
    }


    /**
     * 将id数组，替换成 1,2,3,4 类型 方便sql 使用 in 语句
     * @return 拼接好的字符串
     */
    public static String toSqlInCondition(String[] params) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i == params.length - 1) {
                builder.append(params[i]);
            } else {
                builder.append(params[i]).append(",");
            }
        }
        return builder.toString();
    }

    /**
     * 获取UUID
     * @return UUID字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取没有连接符的UUID
     * @return 没有连接符的UUID
     */
    public static String getUUIDWithoutJoiner() {
        return getUUID().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.getUUIDWithoutJoiner());
    }

}
