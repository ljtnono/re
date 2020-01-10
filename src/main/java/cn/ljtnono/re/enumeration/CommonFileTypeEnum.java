package cn.ljtnono.re.enumeration;


/**
 * 常见文件类型枚举
 * @author ljt
 * @date 2019/11/27
 * @version 1.0.1
 */
public enum CommonFileTypeEnum {

    /** word文档 */
    DOC("doc"),

    /** word文档 */
    DOCX("docx"),

    /** 普通文本文件 */
    TXT("txt"),

    /** excel表格 */
    XLSX("xlsx"),

    /** excel表格 */
    XLS("xls"),

    /** java源文件 */
    JAVA("java"),

    /** xml文件 */
    XML("xml"),

    /** json文件 */
    JSON("json"),

    /** conf配置文件 */
    CONF("conf"),

    /** jsp文件 */
    JSP("jsp"),

    /** ini文件 */
    INI("ini"),

    /** pdf文件 */
    PDF("pdf"),

    /** ppt文件 */
    PPT("ppt"),

    /** pptx文件 */
    PPTX("pptx"),

    /** zip压缩文件 */
    ZIP("zip"),

    /** rar压缩文件 */
    RAR("rar"),

    /** gz压缩文件 */
    GZ("gz"),

    /** html文件 */
    HTML("html"),

    /** css文件 */
    CSS("css"),

    /** javascript文件 */
    JS("javascript"),

    /** mp3文件 */
    MP3("mp3"),

    /** avi文件 */
    AVI("avi"),

    /** mpeg文件 */
    MPEG("mpeg"),

    /** mov文件 */
    MOV("mov"),

    /** wmv文件 */
    WMV("wmv"),

    /** jpg图片文件 */
    JPG("jpg"),

    /** jpeg图片文件 */
    JPEG("jpeg"),

    /** png图片文件 */
    PNG("png"),

    /** bmp图片文件 */
    BMP("bmp"),

    /** gif图片文件 */
    GIF("gif");

    private String value;

    CommonFileTypeEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
