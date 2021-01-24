package cn.ljtnono.re.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author Ling, Jiatong
 * Date: 2020/8/2 1:10
 */
public class DateUtil {


    /**
     * 将一个时间转换为对应格式的字符串
     * @param date 时间对象
     * @param dateStyleEnum 时间格式
     * @return 时间字符串
     */
    public static String formatDate(Date date, DateStyleEnum dateStyleEnum) {
        SimpleDateFormat format = new SimpleDateFormat(dateStyleEnum.getValue());
        return format.format(date);
    }

    /**
     * 日期格式化风格
     * @author Ling, Jiatong
     *
     */
    public enum DateStyleEnum {

        /**
         * 形如yyyy-MM-dd HH:mm:ss 例如
         */
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss");

        /** 格式化风格的值 */
        private final String value;

        DateStyleEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

}
