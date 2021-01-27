package cn.ljtnono.re.common.constant.resource;

import java.util.Arrays;
import java.util.List;

/**
 * 图片模块常量池
 *
 * @author Ling, Jiatong
 * Date: 2021/1/25 23:08
 */
public class ImageConstant {

    /**
     * 单张图片最大大小为 4m
     */
    public static final Integer MAX_SIZE_PER_IMAGE = 4 * 1024 * 1024;

    /**
     * 允许上传的图片的格式
     */
    public static final List<String> ALLOWED_SUFFIX = Arrays.asList("jpg", "jpeg", "png", "gif");

    /**
     * 文件上传失败返回码
     */
    public static final Integer UPLOAD_FAILED_CODE = 0;

    /**
     * 文件上传成功返回码
     */
    public static final Integer UPLOAD_SUCCESS_CODE = 1;

    /**
     * 图片访问域名，默认端口为80
     */
    public static final String ACCESS_HOST = "http://localhost";
}
