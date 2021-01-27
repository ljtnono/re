package cn.ljtnono.re.common.constant.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图片类型枚举类型
 *
 * @author Ling, Jiatong
 * Date: 2021/1/26 1:05
 */
public enum ImageTypeEnum {

    // 博客文章引用图片
    BLOG_IMAGE(1),
    // 全局配置图片
    GLOBAL_SETTING(2),
    ;


    /**
     * 获取所有图片类型枚举码列表
     *
     * @author Ling, Jiatong
     */
    public static List<Integer> getCodeList() {
        return Arrays.stream(values())
                .map(ImageTypeEnum::getCode)
                .collect(Collectors.toList());
    }

    private final Integer code;

    public Integer getCode() {
        return code;
    }

    ImageTypeEnum(Integer code) {
        this.code = code;
    }
}
