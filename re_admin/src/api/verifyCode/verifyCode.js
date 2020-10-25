import axios from "axios";
import {proxyUrlPrefix} from "@/api/base/base";

/**
 * 获取验证码
 */
export const getVerifyCode = () => {
    return axios.get(proxyUrlPrefix + "/re/api/v1/system/verifyCode", {
        responseType: 'blob'
    });
}
