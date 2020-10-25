import axios from "axios";
import {proxyUrlPrefix} from "@/api/base/base";
/**
 * 登陆接口
 * @param username 用户名
 * @param password 密码
 * @param verifyCodeId 验证码id
 * @param verifyCode 验证码
 * @returns {AxiosPromise<any>}
 */
export const login = (username, password, verifyCodeId, verifyCode) => {
    return axios.post(proxyUrlPrefix + "/re/api/v1/system/user/login", {
        username: username,
        password: password,
        verifyCodeId: verifyCodeId,
        verifyCode: verifyCode
    });
};

export const getUserInfo = (token) => {
  return axios.get(proxyUrlPrefix + "/user/getUserInfoByToken", {
    params: {
      token: token
    }
  });
};
