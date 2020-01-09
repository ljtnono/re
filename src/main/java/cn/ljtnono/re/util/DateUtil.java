package cn.ljtnono.re.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *  日期工具类
 *  @author ljt
 *  @date 2018/11/29
 *  @version 1.0
*/
public class DateUtil {


    /**
     * 日期风格格式化枚举类
     */
    public enum DateStyleEnum {
        /**
         * MM-dd 例如 03-13
         */
        MM_DD("MM-dd"),
        /**
         * yyyy-MM 例如 2017-03
         */
        yyyy_MM("yyyy-MM"),
        /**
         * yyyy-MM-dd 例如 2017-03-13
         */
        yyyy_MM_dd("yyyy-MM-dd"),
        /**
         * MM-dd HH:mm 例如 03-13 15:16
         */
        MM_dd_HH_mm("MM-dd HH:mm"),
        /**
         * MM-dd HH:mm:ss 例如 03-13 15:16:47
         */
        MM_dd_HH_mm_ss("MM-dd HH:mm:ss"),
        /**
         * yyyy-MM-dd HH:mm 例如 2017-03-13 15
         */
        yyyy_MM_dd_HH("yyyy-MM-dd HH"),
        /**
         * yyyy-MM-dd HH:mm 例如 2017-03-13 15:16
         */
        yyyy_MM_dd_HH_mm("yyyy-MM-dd HH:mm"),
        /**
         * yyyy-MM-dd HH:mm:ss 例如 2017-03-13 15:16:47
         */
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
        /**
         * yyyy-MM-dd E HH:mm:ss 例如 2017-03-13 星期一 15:16:47
         */
        yyyy_MM_dd_E_HH_mm_ss("yyyy-MM-dd E HH:mm:ss"),
        /**
         * yyyy-MM-dd HH:mm:ss:S 例如 2017-03-13 15:16:47:356
         */
        yyyy_MM_dd_HH_mm_ss_S("yyyy-MM-dd HH:mm:ss:S"),
        /**
         * yyyy-MM-dd E HH:mm:ss:S 例如 2017-03-13 星期一 15:16:47:356
         */
        yyyy_MM_dd_E_HH_mm_ss_S("yyyy-MM-dd E HH:mm:ss:S"),
        /**
         * MM/dd 例如 03/13
         */
        MM_dd_EN("MM/dd"),
        /**
         * yyyy/MM 例如 2017/03
         */
        yyyy_MM_EN("yyyy/MM"),
        /**
         * yyyy/MM/dd 例如 2017/03/13
         */
        yyyy_MM_dd_EN("yyyy/MM/dd"),
        /**
         * MM/dd HH:mm 例如 03/13 15:16
         */
        MM_dd_HH_mm_EN("MM/dd HH:mm"),
        /**
         * MM/dd HH:mm:ss 例如 03/13 15:16:47
         */
        MM_dd_HH_mm_ss_EN("MM/dd HH:mm:ss"),
        /**
         * yyyy/MM/dd HH:mm 例如 2017/03/13 15
         */
        yyyy_MM_dd_HH_EN("yyyy/MM/dd HH"),
        /**
         * yyyy/MM/dd HH:mm 例如 2017/03/13 15:16
         */
        yyyy_MM_dd_HH_mm_EN("yyyy/MM/dd HH:mm"),
        /**
         * yyyy/MM/dd HH:mm:ss 例如 2017/03/13 15:16:47
         */
        yyyy_MM_dd_HH_mm_ss_EN("yyyy/MM/dd HH:mm:ss"),
        /**
         * yyyy/MM/dd E HH:mm:ss:S 例如 2017/03/13 星期一 15:16:47
         */
        yyyy_MM_dd_E_HH_mm_ss_EN("yyyy/MM/dd E HH:mm:ss"),
        /**
         * yyyy/MM/dd HH:mm:ss:S 例如 2017/03/13 15:16:47:356
         */
        yyyy_MM_dd_HH_mm_ss_S_EN("yyyy/MM/dd HH:mm:ss:S"),
        /**
         * yyyy/MM/dd E HH:mm:ss:S 例如 2017/03/13 星期一 15:16:47:356
         */
        yyyy_MM_dd_E_HH_mm_ss_S_EN("yyyy/MM/dd E HH:mm:ss:S"),

        /**
         * MM月dd日 例如 07月27日
         */
        MM_dd_CN("MM月dd日"),
        /**
         * yyyy年MM月 例如 2017年07月
         */
        yyyy_MM_CN("yyyy年MM月"),
        /**
         * yyyy年MM月dd日 例如 2017年07月27日
         */
        yyyy_MM_dd_CN("yyyy年MM月dd日"),
        /**
         * MM月dd日 HH:mm 例如 07月27日 13:32
         */
        MM_dd_HH_mm_CN("MM月dd日 HH:mm"),
        /**
         * MM月dd日 HH:mm:ss 例如 07月27日 13:32:53
         */
        MM_dd_HH_mm_ss_CN("MM月dd日 HH:mm:ss"),
        /**
         * yyyy年MM月dd日 HH:mm 例如 2017年07月27日 13时
         */
        yyyy_MM_dd_HH_CN("yyyy年MM月dd日 HH时"),
        /**
         * yyyy年MM月dd日 HH:mm 例如 2017年07月27日 13:32
         */
        yyyy_MM_dd_HH_mm_CN("yyyy年MM月dd日 HH:mm"),
        /**
         * yyyy年MM月dd日 HH:mm:ss:S 例如 2017年07月27日 13:32:53
         */
        yyyy_MM_dd_HH_mm_ss_CN("yyyy年MM月dd日 HH:mm:ss"),
        /**
         * yyyy年MM月dd日 HH:mm:ss:S 例如 2017年07月27日 星期四 13:32:53
         */
        yyyy_MM_dd_E_HH_mm_ss_CN("yyyy年MM月dd日 E HH:mm:ss"),
        /**
         * yyyy年MM月dd日 HH:mm:ss:S 例如 2017年07月27日 13:32:53:356
         */
        yyyy_MM_dd_HH_mm_ss_S_CN("yyyy年MM月dd日 HH:mm:ss:S"),
        /**
         * yyyy年MM月dd日 HH:mm:ss:S 例如 2017年07月27日 星期四 13:32:53:356
         */
        yyyy_MM_dd_E_HH_mm_ss_S_CN("yyyy年MM月dd日 E HH:mm:ss:S"),
        /**
         * yy 例如 17
         */
        yy("yy"),
        /**
         * yyyy 例如 2017
         */
        yyyy("yyyy"),
        /**
         * MM 例如 07
         */
        MM("MM"),
        /**
         * dd 例如 27
         */
        dd("dd"),
        /**
         * yyyyMM 例如 201707
         */
        yyyyMM("yyyyMM"),
        /**
         * yyyyMMdd 例如 20170727
         */
        yyyyMMdd("yyyyMMdd"),
        /**
         * yyyyMMddHH 例如 2017072713
         */
        yyyyMMddHH("yyyyMMddHH"),
        /**
         * yyyyMMddHHmm 例如 201707271332
         */
        yyyyMMddHHmm("yyyyMMddHHmm"),
        /**
         * yyyyMMddHHmmss 例如 20170727133253
         */
        yyyyMMddHHmmss("yyyyMMddHHmmss"),
        /**
         * yyMMddHHmmss 例如 170727133253
         */
        yyMMddHHmmss("yyMMddHHmmss"),

        /**
         * HH:mm 例如 32:45
         */
        HH_mm("HH:mm"),
        /**
         * HH:mm:ss 例如 13:32:45
         */
        HH_mm_ss("HH:mm:ss");

        private String value;

        public String getValue() {
            return value;
        }

        DateStyleEnum(String value) {
            this.value = value;
        }
    }

    /**
     * 防止工具类进行实例化操作
     */
    private DateUtil() {}

    /** 默认日期格式化 例如：2015-10-10 10:10:10 */
    private static DateStyleEnum DEFAULT_STYLE = DateStyleEnum.yyyy_MM_dd_HH_mm_ss;

    /**
     * 将时间戳转换为时间格式字符串
     * @param timestamp 时间戳
     * @return 转换出的Date对象的字符串
     */
    public static String formatTimeStamp(final long timestamp, DateStyleEnum style) {
        if (style == null) {
            style = DEFAULT_STYLE;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(style.getValue());
        return dateFormat.format(timestamp);
    }

    /**
     * 将时间格式化为字符串
     * @param date 时间对象
     * @param style 格式化风格
     * @return 时间对象字符串
     */
    public static String formatDate(final Date date, DateStyleEnum style) {
        if (style == null) {
            style = DEFAULT_STYLE;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(style.getValue());
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间戳
     * @return 当前时间戳
     */
    public static long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间的字符串表示， 如果Style为null，则为默认格式
     * @param style 字符串格式
     * @return 当前时间的字符串表示
     */
    public static String getNowDateString(DateStyleEnum style) {
        if (style == null) {
            style = DEFAULT_STYLE;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(style.getValue());
        return dateFormat.format(new Date());
    }


    /**
     * 指定获取当前中指定时刻的Date对象
     * @param hour 小时
     * @param min 分钟
     * @param sec 秒钟
     * @return date 一天中指定时刻的date对象
     */
    public static Date getDayTime(int hour,int min,int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        calendar.set(Calendar.SECOND,sec);
        return calendar.getTime();
    }

    /**
     * 获取当前时间加上月份之后的时间
     * @param startDate 开始的日期,输入null,为当前日期
     * @param months 获取当前日期加上月份之后的时间
     * @return date 时间对象
     */
    public static Date dateAddMonths(Date startDate, int months) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);
        return c.getTime();
    }

    /**
     * 从字符串格式化Date对象
     * @param str 字符串类型格式
     * @param style 日期格式化风格
     * @return 格式化的Date对象
     * @throws Exception 格式化异常时抛出
     */
    public static Date formatFromString(String str, DateStyleEnum style) throws Exception{
        if(StringUtil.isEmpty(str)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(style.getValue());
        return sdf.parse(str);
    }

    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat(DateStyleEnum.MM_DD.getValue());
        System.out.println(format.format(new Date()));
    }
}
