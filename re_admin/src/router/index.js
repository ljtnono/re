import Vue from "vue";
import Router from "vue-router";
import routes from "./routers";
import iView from "iview";
import Cookies from 'js-cookie';
import {canTurnToPage} from "@/utils/permissionUtil";

Vue.use(Router);
const LOGIN_PAGE_NAME = "login";
const router = new Router({
    routes,
    base: "/admin/",
    mode: "history"
});


/**
 * 跳转页面, 有权限那么跳转到相应页面，
 * 如果没有权限，那么返回401页面
 */
const turnTo = (to, next) => {
    if (canTurnToPage(to.name)) {
        // 有权限，可访问
        next();
    } else {
        // 无权限，重定向到401页面
        next({replace: true, name: "error_401"});
    }
};

// 跳转前操作
router.beforeEach((to, from, next) => {
    iView.LoadingBar.start();
    const token = Cookies.get("token");
    if (!token && to.name !== LOGIN_PAGE_NAME) {
        // 未登录且要跳转的页面不是登录页
        next({
            name: LOGIN_PAGE_NAME // 跳转到登录页
        });
    } else if (!token && to.name === LOGIN_PAGE_NAME) {
        // 未登陆且要跳转的页面是登录页
        next(); // 跳转
    } else if (token && to.name === LOGIN_PAGE_NAME) {
        // 这里改为跳转到登录页，不管是否登录，跳转到登录页都可以
        next();
    } else {
        // 这里应该初始化用户信息，然后再跳转，比如设置权限，设置用户头像等
        turnTo(to, next);
    }
});

// 跳转后操作
router.afterEach(to => {
    // 设置各种信息
    iView.LoadingBar.finish();
    window.scrollTo(0, 0);
});

export default router;
