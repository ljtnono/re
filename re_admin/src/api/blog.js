import axios from "axios";
import qs from "querystring";

export const listBlogPage = (page, count) => {
  let p = page || 1;
  let c = count || 10;
  return axios.get("/api/blog/listBlogPage", {
    params: {
      page: p,
      count: c
    }
  });
};

export const deleteBlog = (index) => {
  let id = parseInt(index);
  return axios.delete("/api/blog/" + id);
};

export const restoreBlog = (index) => {
  let id = parseInt(index);
  return axios.put("/api/blog/restore/" + id);
};

export const search = (searchForm, pageParam) => {
  const data = qs.stringify({
    title: searchForm.title,
    type: searchForm.type,
    author: searchForm.author,
    page: pageParam.page,
    count: pageParam.count
  });
  return axios.post("/api/blog/search", data, {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  });
};
