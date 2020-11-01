import axios from "axios";
import {getToken, proxyUrlPrefix} from "@/api/base/base";
// 基础url
const baseUrl = proxyUrlPrefix + "/re/api/v1/system/user";

/**
 * 登陆接口
 * @param username 用户名
 * @param password 密码
 * @param verifyCodeId 验证码id
 * @param verifyCode 验证码
 * @returns {AxiosPromise<any>}
 */
export const login = (username, password, verifyCodeId, verifyCode) => {
    return axios.post(baseUrl + "/login", {
        username: username,
        password: password,
        verifyCodeId: verifyCodeId,
        verifyCode: verifyCode
    });
};
/**
 * TODO 用户登出
 */
export const logout = () => {
    let token = getToken();
    return axios.get(baseUrl + "/logout", {
        headers: {
            "Authorization": token
        }
    });
}
