import {http} from "@/config/axios"

/**
 * 获取角色下拉列表
 * @return {AxiosPromise<any>}
 */
export const select = () => {

    return http.get("/system/role/select");
}
