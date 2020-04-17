# Root Element 根元素
这是一个使用SpringBoot作为后端，前端使用vue.js的博客系统，使用的是mavon-editor插件作为markdown文本编辑器<br>
本博客部分内容参考温志怀博客和思欲主题，详情请移步  [温志怀博客](http://www.wenzhihuai.com) | [欲思主题](https://yusi123.com/) | [GitCafe](https://gitcafe.net/) | [崔庆才博客](https://cuiqingcai.com/)<br>
网站的站点：[https://rootelement.cn/](https://rootelement.cn/)<br>

## 博客的技术选型

[![GitHub stars](https://img.shields.io/github/stars/ljtnono/re.svg)](https://github.com/ljtnono/re/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/ljtnono/re.svg)](https://github.com/ljtnono/re/network)
[![GitHub issues](https://img.shields.io/github/issues/ljtnono/re.svg)](https://github.com/ljtnono/re/issues)

**后端**：SpringBoot、Mybatis-plus、SpringData    <br>
**前端**：Vue全家桶、BootstrapVue、Jquery、Echarts、vue-awesome-swiper（图片切换）、iview-admin模板    <br>
**图片存储**：vsftpd    <br>
**缓存**：Redis    <br>
**数据库**：MySQL8   <br>
**部署**：Tomcat、Nginx、阿里云服务器、华为云服务器   <br>
**全文索引**：Elasticsearch <br>

## 仓库结构
- re <br>
后端项目，提供数据接口
- re_front <br>
前端系统，博客展示的主要页面，对外开放
- re_admin <br>
后台系统，博客后台管理系统，不对外开放

## 项目架构
<div align="center">
    <img src="http://rootelement.oss-cn-beijing.aliyuncs.com/images/%E9%A1%B9%E7%9B%AE%E6%9E%B6%E6%9E%84%E5%9B%BE.png" />
    <p style="margin-bottom: 20px;">组织架构图</p>
</div>


