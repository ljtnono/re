import Router from 'vue-router';
import Index from "../view/Index";
import Articles from "../view/Articles";
import Support from "../view/Support";
import About from "../view/About";
import Article from "../view/Article";


let routes = [
  {
    path: "/",
    component: Index
  },
  {
    path: "/articles/:type",
    component: Articles
  },
  {
    path: "/article/:blogId",
    component: Article,
    props: true
  },
  {
    path: "/support",
    component: Support
  },
  {
    path: "/about",
    component: About
  },
  // {
  //   path: '/401',
  //   name: 'error_401',
  //   meta: {
  //     hideInMenu: true
  //   },
  //   component: () => import('@/view/error-page/401.vue')
  // },
  // {
  //   path: '/500',
  //   name: 'error_500',
  //   meta: {
  //     hideInMenu: true
  //   },
  //   component: () => import('@/view/error-page/500.vue')
  // },
  // {
  //   path: '*',
  //   name: 'error_404',
  //   meta: {
  //     hideInMenu: true
  //   },
  //   component: () => import('@/view/error-page/404.vue')
  // }
];




export default new Router({
  mode: 'history',
  base: '/',
  routes
});
