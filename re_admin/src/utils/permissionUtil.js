import Cookies from "js-cookie";

/**
 * 权限id和权限映射（参照权限表）
 * @type {Map<number, string>}
 */
const permissionMap = new Map([
    // blog相关
    [1000, "blog"],
    // 博客管理
    [1001, "blog:blog"],
    [1002, "blog:blog:view"],
    [1003, "blog:blog:add"],
    [1004, "blog:blog:update"],
    [1005, "blog:blog:delete"],
    // 博客标签
    [1020, "blog:tag"],
    [1021, "blog:tag:view"],
    [1022, "blog:tag:add"],
    [1023, "blog:tag:update"],
    [1024, "blog:tag:delete"],
    // 评论管理
    [1040, "blog:comment"],
    [1041, "blog:"]
]);

/**
 * 权限id和菜单之间的映射（参照权限表），只有存在相应的权限才会渲染相应的菜单
 * @type {Map<string, *[]>}
 */
const permissionMenuMap = new Map([
    ["blog", [1000]],
    ["blog:blog", [1001]],
    ["blog:tag", [1020]],
    ["blog:comment", [1040]],
    ["resource", [2000]],
    ["resource:image", [2001]],
    ["resource:link", [2020]],
    ["timeline", [3000]],
    ["skill", [4000]],
    ["message", [5000]],
    ["message:recycle", [5020]],
    ["system", [6000]],
    ["system:user", [6001]],
    ["system:role", [6020]],
    ["system:permission", [6040]],
    ["system:log", [6060]],
    ["system:config", [6080]]
]);

/**
 * TODO 根据权限id列表，生成菜单
 * @param permissionIdList 权限id菜单
 */
export const generateMenu = (permissionIdList) => {

}


/**
 * 判断当前用户是否有权限进入相应的页面
 * @param page 要跳转的页面
 */
export const canTurnToPage = (page) => {
    if (page == null || page === "") {
        // 页面不存在
        return false;
    } else {
        let userInfo = Cookies.getJSON("userInfo");
        // 没有登陆过
        if (userInfo === null) {
            return false;
        } else {
            // 校验各种页面的权限
            let permissionIdList = userInfo["permissionIdList"];
            return true;
        }
    }
};
