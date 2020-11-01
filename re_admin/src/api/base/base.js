import Cookies from "js-cookie";
// 代理前缀
export const proxyUrlPrefix = "/api";

/**
 * 获取token
 */
export const getToken = () => {
    let userInfo = Cookies.getJSON("userInfo");
    if (userInfo === null || userInfo === undefined) {
        throw new Error("");
    }
}
