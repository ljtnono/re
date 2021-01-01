import {http} from "@/config/axios";
import qs from "qs";

/**
 * 登陆接口
 * @param username 用户名
 * @param password 密码
 * @param verifyCodeId 验证码id
 * @param verifyCode 验证码
 * @param forceLogin 是否强制登陆 1 强制登陆 2 不强制登陆
 * @returns {AxiosPromise<any>}
 */
export const login = (username, password, verifyCodeId, verifyCode, forceLogin) => {
  return http.post("/system/user/login", {
    username: username,
    password: password,
    verifyCodeId: verifyCodeId,
    verifyCode: verifyCode,
    forceLogin: forceLogin
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
 * @param searchCondition 模糊查询条件
 * @param roleId 用户角色id
 * @param sortFieldList 排序字段列表
 * @param sortTypeList 排序类型列表
 * @param pageNum 页数
 * @param pageSize 每页条数
 * @return {AxiosPromise<any>}
 */
export const getList = ({searchCondition, roleId, sortFieldList, sortTypeList, pageNum, pageSize}) => {
  let params = qs.stringify({
    searchCondition,
    roleId,
    sortFieldList,
    sortTypeList,
    pageNum,
    pageSize
  }, {arrayFormat: 'repeat'})
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

/**
 * 修改用户信息
 * @param id 用户id
 * @param username 用户名
 * @param password 用户密码
 * @param phone 用户手机
 * @param email 用户邮箱
 * @param roleId 用户角色
 */
export const updateUser = ({id, username, password, phone, email, roleId}) => {
  return http.put("/system/user", {
    id, username, password, phone, email, roleId
  });
}

/**
 * 根据用户id获取用户信息
 * @param id 用户id
 */
export const getUserById = (id) => {
  return http.get("/system/user/" + id);
}

/**
 * 获取用户根据角色分类统计饼状图
 */
export const roleNumPie = () => {
  return http.get("/system/user/roleNumPie");
}

/**
 * 获取当前在线用户统计图
 */
export const onlineNumPie = () => {
  return http.get("/system/user/onlineNumPie")
}
