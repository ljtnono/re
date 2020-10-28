import {login} from "@/api/system/user";

export default {
    state: {
        // 用户id，
        userId: null,
        // 用户角色id
        roleId: null,
        // 用户token
        token: "",
        // 权限列表
        permissionList: [],
        // 角色名
        roleName: "",
        // email
        email: "",
        // phone
        phone: "",
        // 是否删除 0 正常 1 已删除
        deleted: null
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
                }).catch(error => {
                    reject(error);
                });
            });
        }
    }
};
