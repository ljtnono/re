import {http} from "@/config/axios";

/**
 * 获取验证码
 * @return {AxiosPromise<any>}
 */
export const getVerifyCode = () => {
    return http.get("/system/verifyCode", {
        responseType: 'blob'
    });
}
