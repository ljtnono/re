<template>
  <!-- 内容区 -->
  <div class="content pr">
    <!-- 文章详情部分 -->
    <div class="content-main">
      <section class="content-detail mt15">
        <nav class="detail-nav p10">
          <a href="/">
            <i class="fa fa-home"></i>
          </a>
          <i class="fa fa-angle-double-right mr5 ml5"></i>
          <a href="/articles/ALL">
            <span>技术文章</span>
          </a>
          <i class="fa fa-angle-double-right mr5 ml5"></i>
          <a :href="'/articles/' + blog.type">
            <span>{{blog.type}}</span>
          </a>
          <i class="fa fa-angle-double-right mr5 ml5"></i>
          <a :href="'/article/' + blog.id">
            <span>{{blog.title}}</span>
          </a>
        </nav>
        <header class="detail-header p10">
          <p class="article-title f14 mb5">{{blog.title}}</p>
          <a href="javascript:" class="mr5"><i class="fa fa-list"></i><span>{{blog.type}}</span></a>
          <a href="javascript:" class="mr5"><i class="fa fa-user"></i><span>{{blog.author}}</span></a>
          <a href="javascript:" class="mr5"><i class="fa fa-calendar-times-o"></i><span>{{blog.modifyTime | timeFormat}}</span></a>
          <a href="javascript:" class="mr5"><i class="fa fa-eye"></i><span>{{blog.view}}浏览</span></a>
          <a href="javascript:" class="mr5"><i class="fa fa-comment"></i><span>{{blog.comment}}评论</span></a>
        </header>
        <section class="detail-content" id="detail-content" style="min-height: 1000px">
          <mavon-editor
            class="md"
            :value="blog.contentMarkdown"
            :subfield = "false"
            :defaultOpen = "'preview'"
            :toolbarsFlag = "false"
            :editable="false"
            :scrollStyle="true"
            :boxShadow="false"
            :transition="true"
            :previewBackground="'#ffffff'"
            :ishljs = "true" style="border: none;padding: 0;">
          </mavon-editor>
        </section>
        <section class="detail-label p10">
          <i class="fa fa-tag f20"></i>
          <a class="label" :href="'/articles/' + blog.type">
            {{blog.type}}
          </a>
        </section>
<!--        <section class="detail-pre-next p10 flex flex-justify-content-space-between">-->
<!--          <div class="pre" v-if="blog.prev != null">-->
<!--            <a th:href="'/article/' + ${prev.id}" th:inline="text"><i class="fa fa-angle-double-left"></i>{{blog.prev.title}}</a>-->
<!--          </div>-->
<!--          <div class="next" v-if="blog.next != null">-->
<!--            <a th:href="'/article/' + ${next.id}" th:inline="text">[[${next.title}]]<i class="fa fa-angle-double-right"></i></a>-->
<!--          </div>-->
<!--        </section>-->
<!--        <section class="detail-hot-list p10">-->
<!--          <ul>-->
<!--            <li class="mb10 mr10" th:each="blog : ${application.guessYouLikeList}"><i class="fa fa-minus"></i><a th:href="'/article/' + ${blog.id}" class="ml5" th:text="${blog.title}">[Python3 网络爬虫开发实战]9.5-使用代理爬取微信</a></li>-->
<!--          </ul>-->
<!--        </section>-->
        <!--留言区-->
        <div class="title p10">网友评论</div>
        <section class="mb20 p10" id="comments" style="background-color:#FFFFFF;"></section>
      </section>
    </div>
    <ContentSide></ContentSide>
  </div>
</template>

<script>
  import ContentSide from "../components/ContentSide";
  import {getBlogById} from "../api/blog";
  import 'mavon-editor/dist/css/index.css'
  import mavonEditor from 'mavon-editor'
  import Vue from "vue";
  Vue.use(mavonEditor);

  export default {
    name: "Article",
    data() {
      return {
        blog: {
          id: 10001,
          title: "",
          author: "",
          type: "",
          contentMarkdown: "",
          contentHtml: "",
          coverImage: "",
          comment: "",
          view: ""
        }
      }
    },
    methods: {
      getBlogById(blogId) {
        getBlogById(blogId).then(res => {
          if (res.data.request === "success" && res.data.status === 200) {
            this.blog = res.data.data[0];
          }
        }).catch(() => {

        });
      }
    },
    mounted() {
      this.getBlogById(this.blogId);
    },
    components: {
      ContentSide
    },
    props: {
      blogId: {
        type: String,
        default: "10001"
      }
    }
  }
</script>

<style scoped lang="scss">

</style>
