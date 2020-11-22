import {login, logout} from "@/api/system/user";
import Cookies from "js-cookie";

export default {
    state: {
        client: null,
    },
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
                });
            });
        },
        // 处理用户退出登陆
        handleLogOut({state, commit}) {
            return new Promise((resolve, reject) => {
                logout().then(res => {
                    let result = res.data;
                    if (result.code === 0 && result.message === "success") {
                        resolve(result);
                    } else {
                        reject(result);
                    }
                });
            });
        },
        // 删除用户cookie
        clearUserCookie() {
            Cookies.remove("userInfo");
        },
        // 连接上websocket
        connectWebSocketServer() {

        },
        // 处理webSocket消息
        handlerMessage() {

        }
    }
};
