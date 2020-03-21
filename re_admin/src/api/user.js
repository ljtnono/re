import axios from "@/libs/api.request";
import qs from "querystring";
import ax from "axios";

export const listUserPage = (page, count) => {
  let p = page || 1;
  let c = count || 10;
  return ax.get("/api/user/listUserPage", {
    params: {
      page: p,
      count: c
    }
  });
};

export const deleteUser = (index) => {
  let id = parseInt(index);
  return ax.delete("/api/user/" + id);
};

export const restoreUser = (index) => {
  let id = parseInt(index);
  return ax.put("/api/user/restore/" + id);
};

export const search = (searchForm, pageParam) => {
  const data = qs.stringify({
    username: searchForm.username,
    tel: searchForm.tel,
    email: searchForm.email,
    page: pageParam.page,
    count: pageParam.count
  });
  return ax.post("/api/user/search", data, {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  });
};

export const updateUser = (userForm) => {
  const data = qs.stringify({
    id: userForm.id,
    username: userForm.username,
    password: userForm.password,
    qq: userForm.qq,
    url: userForm.url,
    email: userForm.email
  });
  return ax.put("/api/user/" + userForm.id, data, {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  });
};

export const getUserById = (userId) => {
  return ax.get("/api/user/" + userId);
};

export const addUser = (userForm) => {
  const data = qs.stringify({
    username: userForm.username,
    password: userForm.password,
    qq: userForm.qq,
    tel: userForm.tel,
    email: userForm.email
  });
  return ax.post("/api/user", data, {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  });
};

export const login = (username, password) => {
  const data = qs.stringify({
    username: username,
    password: password
  });
  return ax.post("/api/doLogin", data, {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  });
};

export const getUserInfo = (token) => {
  return axios.request({
    url: "get_info",
    params: {
      token
    },
    method: "get"
  });
};

export const getUnreadCount = () => {
  return axios.request({
    url: "message/count",
    method: "get"
  });
};

export const getMessage = () => {
  return axios.request({
    url: "message/init",
    method: "get"
  });
};

export const getContentByMsgId = msg_id => {
  return axios.request({
    url: "message/content",
    method: "get",
    params: {
      msg_id
    }
  });
};

export const hasRead = msg_id => {
  return axios.request({
    url: "message/has_read",
    method: "post",
    data: {
      msg_id
    }
  });
};

export const removeReaded = msg_id => {
  return axios.request({
    url: "message/remove_readed",
    method: "post",
    data: {
      msg_id
    }
  });
};

export const restoreTrash = msg_id => {
  return axios.request({
    url: "message/restore",
    method: "post",
    data: {
      msg_id
    }
  });
};
