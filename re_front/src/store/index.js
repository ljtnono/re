import Vue from "vue"
import Vuex from "vuex"
import blog from "./module/blog"
import web from "./module/web"

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {},
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    blog,
    web
  }
});
