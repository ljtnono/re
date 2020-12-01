import Cookies from "js-cookie";

/**
 * 权限表的映射
 * @type {{name: string, id: number}[]}
 */
export const permissionMap = [
    {
        id: 1000,
        name: "博客管理",
        type: 0,
        expression: "blog"
    },
    {
        id: 1001,
        name: "博客管理",
        type: 0,
        expression: "blog:blog"
    },
    {
        id: 1002,
        name: "查看博客",
        type: 1,
        expression: "blog:blog:view"
    }
]

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
            if (page === "blog") {

            }
            return true;
        }
    }
};
