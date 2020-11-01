import Cookies from "js-cookie";

/** 权限和页面的映射对象 */
const permissionMap = new Map([
    [1, ""],
    [2, ""],
    [3, ""]
]);

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
        if (userInfo === null || userInfo === undefined) {
            return false;
        } else {
            // 校验各种页面的权限
            let permissionIdList = userInfo["permissionIdList"];

            switch (page) {
                case "fff": {
                    // 是否能够
                    break;
                }
                case "fdaf": {
                    break;
                }
                default:
            }
            return true;
        }
    }
};
