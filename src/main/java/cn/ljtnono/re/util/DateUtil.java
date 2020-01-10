package cn.ljtnono.re.util;

import cn.ljtnono.re.enumeration.DateStyleEnum;

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
