package cn.ljtnono.re.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ljt
 * Date: 2020/8/2 1:10
 * Description: 日期工具类
 */
public class DateUtil {


    /**
     * 将一个时间转换为对应格式的字符串
     * @param date 时间对象
     * @param dateStyleEnum 时间格式
     * @return 时间字符串
     */
    public static String formatDate(Date date, DateStyleEnum dateStyleEnum) {
        SimpleDateFormat format = new SimpleDateFormat(dateStyleEnum.getStyle());
        return format.format(date);
    }

    public enum DateStyleEnum {

        /**
         * 形如yyyy-MM-dd HH:mm:ss 例如
         */
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss");

        private String style;

        DateStyleEnum(String style) {
            this.style = style;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }
    }

}
