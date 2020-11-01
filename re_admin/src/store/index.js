import Vue from "vue";
import Vuex from "vuex";
import user from "./module/system/user";
import app from "./module/app";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        //
    },
    mutations: {
        //
    },
    actions: {
        //
    },
    modules: {
        user,
        app
    }
});
