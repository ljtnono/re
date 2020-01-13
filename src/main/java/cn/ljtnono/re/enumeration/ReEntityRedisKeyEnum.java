package cn.ljtnono.re.enumeration;

/**
 * 实体类在redis中的键值存储格式
 * 格式说明：
 * 如果是普通的实体类，那么redis的键值设定规则如下
 * 单体存储：
 * 实体类对应表名:id:重要字段1的值:重要字段2的值:重要字段三的值，例如 re_blog:1001:ljtnono:git教程:git
 * 分页查询存储：
 * 实体类对应的表名_page:id:页数:每页条数，例如 re_blog_page:1:10
 * 分页条件查询存储：
 * 实体类对应的表名_page_字段名1_字段名2:页数:每页条数:字段1值:字段2值，例如：re_blog_page_type_author:1:10:git:ljtnono
 * 普通字符串存储：
 * 任意字符串都可以作为键值
 *
 * @author ljt
 * @date 2020/1/13
 * @version 1.1.3
 */
public enum ReEntityRedisKeyEnum {

    /** ReBlog实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BLOG_KEY("re_blog:id:author:title:type"),

    /** ReBlog实体类分页查询在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BLOG_PAGE_KEY("re_blog_page:page:count"),

    /** ReBlog实体类分页查询附加信息在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BLOG_PAGE_TOTAL_KEY("re_blog_page_total:page:count"),

    /** ReBlog实体类根据type分页查询在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BLOG_PAGE_TYPE_KEY("re_blog_page_type:page:count:type"),

    /** ReBlogType实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BLOG_TYPE_KEY("re_blog_type:id:name"),

    /** ReBlogType实体分页查询在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BLOG_TYPE_PAGE_KEY("re_blog_type_page:page:count"),

    /** ReBlogType实体分页查询附加信息在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BLOG_TYPE_PAGE_TOTAL_KEY("re_blog_type_page_total:page:count"),

    /** ReBook实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BOOK_KEY("re_book:id:name:author:type"),

    /** ReBookType实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_BOOK_TYPE_KEY("re_book_type:id:name"),

    /** ReConfig实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_CONFIG_KEY("re_config:id:key"),

    /** ReImage实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_IMAGE_KEY("re_image:id:origin_name:type:owner"),

    /** ReLink实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_LINK_KEY("re_link:id:name:type"),

    /** ReLink实体类分页查询在redis中的存储的键的格式，通过替换相应的值来存储 */
    RE_LINK_PAGE_KEY("re_link_page:page:count"),

    /** ReLink实体类分页查询附加信息在redis中的存储的键的格式，通过替换相应的值来存储 */
    RE_LINK_PAGE_TOTAL_KEY("re_link_page_total:page:count"),

    /** ReLinkType实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_LINK_TYPE_KEY("re_link_type:id:name"),

    /** ReLinkType实体类分页查询在redis中的存储的键的格式，通过替换相应的值来存储 */
    RE_LINK_TYPE_PAGE_KEY("re_link_type_page:page:count"),

    /** ReLinkType实体类分页查询附加信息在redis中的存储的键的格式，通过替换相应的值来存储 */
    RE_LINK_TYPE_PAGE_TOTAL_KEY("re_link_type_page_total:page:count"),

    /** RePermission实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_PERMISSION_KEY("re_permission:id:res"),

    /** ReRole实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_ROLE_KEY("re_role:id:name"),

    /** ReRolePermission实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_ROLE_PERMISSION_KEY("re_role_permission:id"),

    /** ReSkill实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_SKILL_KEY("re_skill:id:name:owner"),

    /** ReTimeLine实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_TIMELINE_KEY("re_timeline:id"),

    /** ReUser实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_USER_KEY("re_user:id:username:qq:tel"),

    /** ReUserRole实体类在redis中存储的键的格式，通过替换相应的值来存储 */
    RE_USER_ROLE_KEY("re_user_role:id");

    /** 键的值 */
    private String key;

    ReEntityRedisKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
