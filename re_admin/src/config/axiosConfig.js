import axios from "axios";
import iView from "view-design";
import Cookies from "js-cookie";
import {PASS_TOKEN_API} from "@/constant/systemConstant";

// 创建axios实例
const http = axios.create({
    baseURL: "/re/api/v1",
    timeout: 5000,
    responseType: "json",
    withCredentials: true,
    headers: {
        "content-Type": "application/json;charset=utf-8"
    }
});

// 请求前拦截器
http.interceptors.request.use(request => {
    let userInfo = Cookies.getJSON("userInfo");
    // 当token存在，并且不是请求需要token的接口时
    if (userInfo && !PASS_TOKEN_API.includes(request.url)) {
        request.headers["Authorization"] = "Bearer " + userInfo["token"];
    }
    return request;
});

// 响应拦截器
http.interceptors.response.use(response => {
    let result = response.data;
    // 如果返回类型是json类型并且返回消息码不为0，那么弹出提示消息
    if (response.config.headers.responseType === "application/json") {
        // 如果存在消息码不为0
        if (result.code !== 0) {
            iView.Message.error({
                background: true,
                content: result.message
            });
        }
    }
    return response;
}, error => {
    iView.Message.error({
        background: true,
        content: error
    });
});

export {
    http
}
