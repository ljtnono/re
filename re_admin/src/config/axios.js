import axios from "axios";
import iView from "view-design";
import Cookies from "js-cookie";
import {LOGIN_PAGE_NAME, PASS_TOKEN_API} from "@/constant/system/constant";
import router from "@/router";
import {GlobalError} from "@/constant/globalError"
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
// 不自动弹出消息的错误码集合
const notShowMessageCode = [
  GlobalError.get("USER_ALREADY_LOGIN_ERROR")
];
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
  // 如果是json格式的返回类型，提示消息，其他地方不需要提示消息了
  if (/application\/json/.test(response.headers["content-type"])) {
    // 当结果为用户未认证时，删除本地的缓存，并且跳转到登陆页面
    if (result.code === GlobalError.get("USER_NOT_AUTHENTICATION")) {
      Cookies.remove("userInfo");
      // 跳转到相关页面
      router.push({
        name: LOGIN_PAGE_NAME
      });
    }
    // 如果存在消息码不为0
    if (result.code !== 0) {
      if (!notShowMessageCode.includes(result.code)) {
        // 这里这里请求的时候会自动拦截相关的错误信息并提示出来
        iView.Message.error({
          background: true,
          content: result.message
        });
      }
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
