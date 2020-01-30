package cn.ljtnono.re.enumeration;


/**
 * 常见文件类型枚举，文件分为以下几种类型
 * 1. 图片 2. 音乐 3. 文档 4. 视频 5. pdf书籍 6. markdown 7. 代码 8. 其他
 * 其中3表示excel word ppt office文件
 * @author ljt
 * @date 2020/1/30
 * @version 1.0.2
 */
public enum CommonFileTypeEnum {

    //################################ 1.图片 ################################//
    /** jpg图片文件 */
    JPG("jpg", 1),

    /** jpeg图片文件 */
    JPEG("jpeg", 1),

    /** png图片文件 */
    PNG("png", 1),

    /** bmp图片文件 */
    BMP("bmp", 1),

    /** gif图片文件 */
    GIF("gif", 1),

    //################################ 2.音乐 ################################//
    /** mp3文件 */
    MP3("mp3", 2),

    /** wma文件 */
    WMA("wma", 2),

    /** wav文件 */
    WAV("wav", 2),

    //################################ 3.文档 ################################//
    /** word文档 */
    DOC("doc", 3),

    /** word文档 */
    DOCX("docx", 3),

    /** 普通文本文件 */
    TXT("txt", 3),

    /** excel表格 */
    XLSX("xlsx", 3),

    /** excel表格 */
    XLS("xls", 3),

    /** ppt文件 */
    PPT("ppt", 3),

    /** pptx文件 */
    PPTX("pptx", 3),

    //################################ 4.视频 ################################//
    /** avi文件 */
    AVI("avi", 4),

    /** mpeg文件 */
    MPEG("mpeg", 4),

    /** mov文件 */
    MOV("mov", 4),

    /** wmv文件 */
    WMV("wmv", 4),

    /** rmvb文件 */
    RMVB("rmvb", 4),

    //################################ 5.pdf ################################//
    /** pdf文件 */
    PDF("pdf", 5),

    //################################ 6.md ################################//
    /** md文件 */
    MD("md", 6),

    //################################ 7.code ################################//
    /** sql文件 */
    SQL("sql", 7),

    /** java源文件 */
    JAVA("java", 7),

    /** xml文件 */
    XML("xml", 7),

    /** json文件 */
    JSON("json", 7),

    /** conf配置文件 */
    CONF("conf", 7),

    /** jsp文件 */
    JSP("jsp", 7),

    /** ini文件 */
    INI("ini", 7),

    /** html文件 */
    HTML("html", 7),

    /** css文件 */
    CSS("css", 7),

    /** javascript文件 */
    JS("javascript", 7),

    //################################ 8.其他 ################################//
    /** bin文件 */
    BIN("bin", 8),

    /** zip压缩文件 */
    ZIP("zip", 8),

    /** rar压缩文件 */
    RAR("rar", 8),

    /** gz压缩文件 */
    GZ("gz", 8);

    private String value;

    private int type;

    CommonFileTypeEnum(String value, Integer type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
