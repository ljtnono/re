/**
 * 用户信息过期时间 1 天
 * @type {number}
 */
const USERINFO_EXPIRE = 1;

/**
 * 用户登录页名字
 * @type {string}
 */
const LOGIN_PAGE_NAME = "login";

/**
 * 系统后台主页名
 * @type {string}
 */
const HOME_PAGE_NAME = "home";

/**
 * 404找不到资源页面名
 * @type {string}
 */
const NOT_FOUNT_PAGE_NAME = "error_404";

/**
 * 401未认证页面名
 * @type {string}
 */
const NOT_AUTHENTICATE_PAGE_NAME = "error_401";

/**
 * 403 禁止访问页面
 * @type {string}
 */
const FORBIDDEN_PAGE_NAME = "error_403";

export {
    USERINFO_EXPIRE,
    LOGIN_PAGE_NAME,
    NOT_AUTHENTICATE_PAGE_NAME,
    NOT_FOUNT_PAGE_NAME,
    FORBIDDEN_PAGE_NAME,
    HOME_PAGE_NAME
}
