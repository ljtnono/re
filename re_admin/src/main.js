import Vue from 'vue'
import app from './app'
import router from './router'
import store from './store'
import iView from 'view-design'
import moment from "moment";
import editor from 'mavon-editor'
import './index.less'
import '@/assets/icons/iconfont.css'
import 'mavon-editor/dist/css/index.css'
import * as systemConstant from "@/constant/system/constant";

Vue.use(editor);
// 配置iView全局配置
Vue.use(iView);
// 挂载全局配置
Vue.prototype.$LoadingBar = iView.LoadingBar;
// 配置Vue消息全局
Vue.prototype.$Message = iView.Message;
Vue.prototype.$Message.config({
    duration: 2,
    top: 50
});
// 生产环境关掉提示
Vue.config.productionTip = false;
// 全局注册应用配置
Vue.prototype.$systemConstant = systemConstant;
// 配置全局过滤器
Vue.filter("timeFormat", function (time) {
    return moment(time).format("YYYY-MM-DD HH:mm:ss");
});
new Vue({
    el: '#app',
    router,
    store,
    render: h => h(app)
});
