import Vue from "vue";
import Router from "vue-router";
import routes from "./routers";
import Cookies from 'js-cookie';
import {canTurnToPage} from "@/utils/permissionUtil";
import {LOGIN_PAGE_NAME, FORBIDDEN_PAGE_NAME} from "@/constant/system/constant";
// 使用路由插件
Vue.use(Router);
// 创建路由对象
const router = new Router({
    routes,
    base: "/admin/",
    mode: "history"
});
// 跳转前操作
router.beforeEach((to, from, next) => {
    // 开启iView的LoadingBar
    Vue.prototype.$LoadingBar.start();
    if (to.name === LOGIN_PAGE_NAME) {
        next();
    } else {
        let userInfo = Cookies.getJSON("userInfo");
        if (userInfo === null || userInfo === undefined) {
            Vue.prototype.$Message.error({
                background: true,
                content: "用户未认证"
            });
            next({
                name: LOGIN_PAGE_NAME
            });
        } else {
            if (canTurnToPage(to.name)) {
                // 有权限，可访问
                next();
            } else {
                // 无权限，重定向到403页面
                Vue.prototype.$Message.error({
                    background: true,
                    content: "没有权限，禁止访问"
                });
                next({replace: true, name: FORBIDDEN_PAGE_NAME});
            }
        }
    }
});

// 跳转后操作
router.afterEach(to => {
    Vue.prototype.$LoadingBar.finish();
    window.scrollTo(0, 0);
});

export default router;
