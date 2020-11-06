import {http} from "@/config/axiosConfig";
/**
 * 登陆接口
 * @param username 用户名
 * @param password 密码
 * @param verifyCodeId 验证码id
 * @param verifyCode 验证码
 * @returns {AxiosPromise<any>}
 */
export const login = (username, password, verifyCodeId, verifyCode) => {
    return http.post("/system/user/login", {
        username: username,
        password: password,
        verifyCodeId: verifyCodeId,
        verifyCode: verifyCode
    });
};
/**
 * 用户登出
 */
export const logout = () => {
    return http.get("/system/user/logout");
}
