import {http} from "@/config/axios";
import qs from "qs";

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
 * @return {AxiosPromise<any>}
 */
export const logout = () => {
    return http.get("/system/user/logout");
}

/**
 * 分页获取用户列表
 * @param username 用户名
 * @param roleId 用户角色id
 * @param sortFieldList 排序字段列表
 * @param sortTypeList 排序类型列表
 * @param pageNum 页数
 * @param pageSize 每页条数
 * @return {AxiosPromise<any>}
 */
export const getList = ({username, roleId, sortFieldList, sortTypeList, pageNum, pageSize}) => {
    let params = qs.stringify({username, roleId, sortFieldList, sortTypeList, pageNum, pageSize}, { arrayFormat: 'repeat' })
    return http.get("/system/user/list?" + params);
}

/**
 * 逻辑删除用户
 * @param idList 用户id列表
 * @return {AxiosPromise}
 */
export const logicDelete = ({idList}) => {
    return http.delete("/system/user/logic", {
        data: {
            idList
        }
    });
}
