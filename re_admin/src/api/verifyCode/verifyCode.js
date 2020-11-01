import axios from "axios";
import {proxyUrlPrefix} from "@/api/base/base";

// 基础url
const baseUrl = proxyUrlPrefix + "/re/api/v1/system/verifyCode";

/**
 * 获取验证码
 * @return {AxiosPromise<any>}
 */
export const getVerifyCode = () => {
    return axios.get(baseUrl, {
        responseType: 'blob'
    });
}
