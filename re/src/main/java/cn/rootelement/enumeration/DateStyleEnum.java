package cn.rootelement.enumeration;


/**
 * 日期风格格式化枚举类
 * @author ljt
 * @date 2020/1/10
 * @version 1.0.2
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