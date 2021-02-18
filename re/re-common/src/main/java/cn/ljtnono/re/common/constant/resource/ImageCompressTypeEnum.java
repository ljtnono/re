package cn.ljtnono.re.common.constant.resource;

/**
 * 图片压缩包压缩类型枚举
 *
 * @author Ling, Jiatong
 * Date: 2021/1/30 0:19
 */
public enum ImageCompressTypeEnum {

    // zip压缩格式
    ZIP(1),
    // tar.gz 格式
    TAR_GZ(2),
    // rar格式
    RAR(3)
    ;


    public Integer getCode() {
        return code;
    }

    private final Integer code;

    ImageCompressTypeEnum(Integer code) {
        this.code = code;
    }
}
