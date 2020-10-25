import {login, getUserInfo} from "@/api/system/user";
import {getVerifyCode} from "@/api/verifyCode/verifyCode";

export default {
    state: {},
    mutations: {},
    getters: {},
    actions: {
        // 处理用户登录
        handleLogin({dispatch, commit}, {username, password, verifyCodeId, verifyCode}) {
            return new Promise((resolve, reject) => {
                login(username, password, verifyCodeId, verifyCode).then(res => {
                    let result = res.data;
                    if (result.code === 0 && result.message === "success") {
                        resolve(result);
                    } else {
                        reject(result);
                    }
                }).catch(error => {
                    reject(error);
                });
            });
        }
    }
};
