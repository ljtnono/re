import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import iView from 'iview'
import moment from "moment";
import i18n from '@/locale'
import config from '@/config'
import installPlugin from '@/plugin'
import './index.less'
import '@/assets/icons/iconfont.css'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Vue.use(mavonEditor);
// 实际打包时应该不引入mock
/* eslint-disable */
// if (process.env.NODE_ENV !== 'production') require('@/mock');

Vue.use(iView, {
  i18n: (key, value) => i18n.t(key, value)
});
/**
 * @description 注册admin内置插件
 */
installPlugin(Vue);

/**
 * @description 生产环境关掉提示
 */
Vue.config.productionTip = false;

/**
 * @description 全局注册应用配置
 */
Vue.prototype.$config = config;

/**
 * 生产环境时要关闭这个选项
 * @type {boolean}
 */
Vue.config.devtools = true;

// 配置全局过滤器
Vue.filter("timeFormat", function (time) {
  return moment(time).format("YYYY-MM-DD");
});

new Vue({
  el: '#app',
  router,
  i18n,
  store,
  render: h => h(App)
});
