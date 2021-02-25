package cn.ljtnono.re.common.constant.blog;

/**
 * 博客模块常量池
 *
 * @author Ling, Jiatong
 * Date: 2021/2/22 2:02 上午
 */
public interface BlogConstant {

    // 推荐
    Integer RECOMMEND = 0;
    // 不推荐
    Integer NOT_RECOMMEND = 1;
    // 博客类型名最大字符数
    Integer BLOG_TYPE_NAME_MAX_LENGTH = 6;
    // 博客标签名最大字符数
    Integer BLOG_TAG_NAME_MAX_LENGTH = 10;

    // 博客标题最大字符数
    Integer BLOG_TITLE_MAX_LENGTH = 50;
    // 博客标题最小字符数
    Integer BLOG_TITLE_MIN_LENGTH = 2;
    // 博客是草稿
    Integer BLOG_DRAFT = 0;
    // 博客不是草稿
    Integer BLOG_NOT_DRAFT = 1;

}
