package cn.rootelement.enumeration;

/**
 * 全局变量
 * @author ljt
 * @date 2019/11/23
 * @version 1.0.1
 */
public enum GlobalVariableEnum {

    /** 博客默认图片的url地址 */
    RE_IMAGE_DEFAULT_URL("https://ftp.ljtnono.cn/re/images/default_img.jpg"),

    /** 本项目ftp服务器的基础存储地址 */
    RE_FTP_SAVE_PREFIX("https://ftp.ljtnono.cn/re");

    private Object value;

    public Object getValue() {
        return value;
    }

    GlobalVariableEnum(Object value) {
        this.value = value;
    }
}
