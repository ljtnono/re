import Cookies from "js-cookie";
// 代理前缀
export const proxyUrlPrefix = "/api";

/**
 * 获取token
 * @return 如果token存在那么返回token，如果不存在返回null
 */
export const getToken = () => {
    let userInfo = Cookies.getJSON("userInfo");
    if (userInfo === null) {
        return null;
    } else {
        return userInfo["token"];
    }
}
