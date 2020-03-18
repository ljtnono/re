import Vue from 'vue'
import App from './App'
import axios from "axios";
import VueAxios from 'vue-axios'
import VueRouter from 'vue-router'
import router from "./router/router";
import Valine from 'valine';
import config from "./config/config";
import moment from "moment";
// 引入iview
import ViewUI from 'view-design';

// 引入全局css
import "bootstrap/dist/css/bootstrap.min.css";
import "font-awesome/css/font-awesome.min.css";
import 'view-design/dist/styles/iview.css';

import "./assets/css/style.min.css";

Vue.use(ViewUI);

Vue.config.productionTip = false;
Vue.use(VueAxios, axios);
Vue.use(VueRouter);
// 注册valine评论
Vue.use(new Valine(config.valineConfig));
// 配置全局过滤器
Vue.filter("timeFormat", function (time) {
  return moment(time).format("YYYY-MM-DD");
});
// 全局注册config
Vue.prototype.$config = config;

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
