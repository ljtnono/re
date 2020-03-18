<template>
  <div>
    <!-- 头部分 -->
    <header id="header">
      <div class="logo">
        <a href="/" class="cursor-pointer">
          <h1>
            <img :src="this.$config.imgLogoWord" alt="RootElement根元素" class="logo-word" title="RootElement根元素">
          </h1>
        </a>
      </div>
      <!-- 导航按钮和搜索按钮 -->
      <div class="side-nav-header" @click="showMinMenu">
        <div class="side-nav-bar cursor-pointer fl">
          <i class="fa fa-bars" aria-hidden="true"></i>
        </div>
        <div class="side-nav-search cursor-pointer fr">
          <i class="fa fa-search" aria-hidden="true"></i>
        </div>
      </div>
      <!-- 小分辨率下的导航栏 -->
      <ul class="nav-mini flex flex-direction-column mb10" v-show="showMiniMenuFlag">
        <li v-for="page in pages" :key="page.name">
          <a :href="page.url">
            <i :class="page.icon" aria-hidden="true"></i>
            {{page.name}}
          </a>
        </li>
      </ul>
      <!-- 导航栏 -->
      <nav class="nav pr">
        <ul class="nav-menu">
          <li class="nav-item fl" v-for="page in pages" :key="page.name">
            <a :href="page.url" class="nav-active" v-if="currentPage === page.url">
              <i :class="page.icon" aria-hidden="true"></i>
              {{page.name}}
            </a>
            <a :href="page.url" v-else><i :class="page.icon" aria-hidden="true"></i>{{page.name}}</a>
          </li>
          <li class="nav-item nav-search fr">
            <a href="#" style="padding: 20px 22px;">
              <i class="fa fa-search" aria-hidden="true"></i>
            </a>
          </li>
        </ul>
      </nav>
      <div class="line h10" style="opacity: 0.9"></div>
    </header>
    <!-- 消息通知栏 -->
    <div class="w message">
      <div class="message-content flex flex-direction-row flex-align-items-start">
        <i class="fa fa-volume-up"></i>
        <ul class="message-list">
          <li class="f16" v-for="message in messages" :key="message">
            {{message}}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    name: 'Header',
    data() {
      return {
        showMiniMenuFlag: false,
        messages: [
          "看到网上新闻美国的贴吧将成为中国公司",
          "主题更新一下，修复了头像挂掉的问题",
          "后期更新网站文章可能不会那么闲了",
          "不得不说，美国贴吧的界面真是难看到死",
          "推送试试",
          "发现了一个非常不错的插件",
          "国庆快乐，放假三天",
          "准备增加一个免登录支付功能",
          "微信扫码登录功能已经 ok 了",
          "睡觉之前测试一下"
        ],
        pages: [
          {name: "首页", url: "/", icon: "fa fa-home"},
          {name: "技术文章", url: "/articles/ALL", icon: "fa fa-wrench"},
          {name: "支持我", url: "/support", icon: "fa fa-thumbs-up"},
          {name: "关于作者", url: "/about", icon: "fa fa-info-circle"}
        ],
        currentPage: this.getCurrentPage()
      }
    },
    methods: {
      showMinMenu() {
        this.showMiniMenuFlag = !this.showMiniMenuFlag;
      },
      getCurrentPage() {
        let articleRex = /\/article[s]?/;
        if (articleRex.test(this.$route.path)) {
          return "/articles/ALL";
        } else {
          return this.$route.path;
        }
      }
    }
  }
</script>

<style scoped lang="scss">

  @keyframes color-change-5x {
    0% {
      background: #19dcea;
    }
    25% {
      background: #b22cff;
    }
    50% {
      background: #ea2222;
    }
    75% {
      background: #f5be10;
    }
    100% {
      background: #3bd80d;
    }
  }

  /*header*/
  #header {
    max-width: 100%;
    background: #1BDAEA;
    text-align: center;
    animation: color-change-5x 10s ease-in-out .2s infinite alternate both;
    position: relative;

    .logo {
      padding: 10px 15px 10px 15px;
      text-align: center;

      a {
        h1 {
          padding: 20px 0 20px 0;

          .logo-word {
            height: auto;
            max-width: 100%;
            border: 0;
            vertical-align: middle;
          }
        }
      }
    }

    .side-nav-header {
      width: 100%;
      height: auto;
      position: absolute;
      top: 0;
      left: 0;
      color: #ffffff;

      .side-nav-bar, .side-nav-search {
        padding: 10px;

        i.fa {
          font-size: 16px;
        }
      }
    }

    .nav {
      width: 100%;
      display: none;
      height: 60px;
      background-color: #4A4A4A;
      bottom: 0;
      opacity: .9;

      .nav-menu {
        height: 100%;
        width: auto;
        margin: 0 auto;
        max-width: 1200px;

        .nav-search {
          background-color: #5F9EA0;
        }

        .nav-item {
          height: 100%;
          color: #ffffff;

          a {
            display: block;
            width: 100%;
            height: 100%;
            padding: 20px 16px;

            &.nav-active {
              background-color: #5F9EA0;
            }

            &:hover {
              background-color: #5F9EA0;
            }
          }
        }
      }
    }

    .nav-mini {
      width: 100%;
      height: auto;

      li {
        width: 100%;
        height: 36px;
        text-align: left;
        line-height: 36px;
        padding-left: 20px;

        a {
          color: #ffffff;
          font-size: 16px;

          i.fa {
            padding-right: 10px;
          }
        }
      }
    }

  }

  /*ipad 768px以上*/
  @media screen and (min-width: 768px) {
    #header {
      .side-nav-header {
        display: none;
      }

      .side-nav {
        display: none !important;
      }

      .nav {
        display: block;
      }

      .nav-mini {
        display: none;
      }
    }
  }

  /*web 1200px以上*/
  @media screen and (min-width: 1200px) {

  }

</style>
