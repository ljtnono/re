<template>
  <!-- 内容区 -->
  <div class="content pr">
    <div class="content-main">
      <swiper :options="swiperOption" id="swiper">
        <!-- slides -->
        <swiper-slide v-for="slider in slides" v-bind:key="slider">
          <img :src="slider" style="width: 100%; height: 100%;" :alt="slider"/>
        </swiper-slide>
        <!-- Optional controls -->
        <div class="swiper-pagination" slot="pagination"></div>
        <div class="swiper-button-prev" slot="button-prev"></div>
        <div class="swiper-button-next" slot="button-next"></div>
      </swiper>
      <!-- hot -->
      <div class="hot f16">
        <div class="title f14">热门文章</div>
        <div class="hot-content">
          <Loading :show="hotDefaultFlag" style="height: 182px;"></Loading>
          <ul class="hot-list">
            <li class="hot-item mb10" v-for="(blog, index) in hotArticles" :key="blog.id">
              <span class="hot-label mr5">{{index + 1}}</span>
              <a class="hot-title f14" :href="'/article/' + blog.id">{{blog.title}}</a>
              <span class="hot-comment fr">
                <i class="fa fa-comment" style="color: #FF8E8E">{{blog.view}}浏览</i>
              </span>
              <span class="hot-view mr15 fr">
                <i class="fa fa-eye" style="color: #3DB1AD">{{blog.comment}}评论</i>
              </span>
            </li>
          </ul>
        </div>
      </div>
      <!-- 文章列表项 -->
      <div class="articles">
        <Loading :show="articlesDefaultFlag" style="top: 20px;"></Loading>
        <ArticleItem :article="article" v-for="article in articles" :key="article.id"></ArticleItem>
      </div>
      <!-- 分页导航 -->
      <div class="mt-3" v-if="articles.length !== 0">
        <BPagination
          :total-rows="totalCount"
          :per-page="count"
          :value="page"
          first-text="首页"
          prev-text="上一页"
          next-text="下一页"
          last-text="尾页"
          limit="5"
          @change="loadData"
          align="center">
        </BPagination>
      </div>
    </div>
    <ContentSide></ContentSide>
  </div>
</template>

<script>
  import ContentSide from "../components/ContentSide";
  import Loading from "../components/Loading";
  import 'swiper/dist/css/swiper.css'
  import {swiper, swiperSlide} from 'vue-awesome-swiper'
  import ArticleItem from "../components/ArticleItem";
  import {listBlogPage, listHotArticles} from "../api/blog";

  export default {
    name: "Index",
    data() {
      return {
        hotDefaultFlag: true,
        articlesDefaultFlag: true,
        swiperOption: this.$config.swiperOption,
        hotArticles: [],
        count: 10,
        page: 1,
        articles: [],
        totalPages: 1,
        totalCount: 1,
        slides: [
          require("@a/image/bird.jpg"),
          require("@a/image/coffee.jpg"),
          require("@a/image/leaves.jpg"),
          require("@a/image/sunset.jpg")
        ]
      }
    },
    components: {
      ContentSide,
      swiper,
      swiperSlide,
      ArticleItem,
      Loading
    },
    methods: {
      loadData(page) {
        this.articles = [];
        this.listBlogPage(page, this.count);
      },
      listBlogPage(page, count) {
        this.articlesDefaultFlag = true;
        listBlogPage(page, count).then(res => {
          if (res.data.request === "success" && res.status === 200) {
            let data = res.data.data;
            this.articles = data;
            this.page = page;
            this.count = count;
            this.totalPages = parseInt(res.data.fields.totalPages);
            this.totalCount = parseInt(res.data.fields.totalCount);
          }
          this.articlesDefaultFlag = false;
        }).catch(() => {
          this.articlesDefaultFlag = false;
        });
      },
      listHotArticles() {
        this.hotDefaultFlag = true;
        listHotArticles().then(res => {
          if (res.data.request === "success" && res.data.status === 200) {
            this.hotArticles = res.data.data;
          }
          this.hotDefaultFlag = false;
        }).catch(() => {
          this.hotDefaultFlag = false;
        });
      }
    },
    mounted() {
      this.listBlogPage(this.page, this.count);
      this.listHotArticles();
    }
  }
</script>

<style scoped lang="scss">
  .ivu-scroll-wrapper {
    ::-webkit-scrollbar {
      display:none
    }
  }
</style>
