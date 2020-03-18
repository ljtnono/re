<template>
  <div>
    <Card style="margin-bottom: 20px;">
      <template slot="title">
        <p>博客属性</p>
      </template>
      <Form ref="blog" :model="blog">
        <FormItem prop="title">
          <Input v-model="blog.title" placeholder="请输入博客标题"></Input>
        </FormItem>
        <FormItem prop="author">
          <Input v-model="blog.author" placeholder="请输入博客作者"></Input>
        </FormItem>
        <FormItem prop="author">
          <Input v-model="blog.author" placeholder="请输入博客作者"></Input>
        </FormItem>
        <FormItem prop="summary">
          <Input v-model="blog.summary" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
                 placeholder="请输入博客简介"></Input>
        </FormItem>
        <FormItem>
          <Button type="success" long style="margin-bottom: 10px;">提交</Button>
          <Button type="warning" long>重置</Button>
        </FormItem>
      </Form>
    </Card>
    <mavon-editor :toolbars="markdownOption" style="height: 680px"/>
  </div>
</template>

<script>
  export default {
    name: "blog-add",
    data() {
      return {
        blog: {
          title: "",
          author: "",
          type: "",
          summary: ""
        },
        markdownOption: this.$config.markdownOption
      };
    },
    methods: {}
  };
</script>

<style scoped lang="less">

</style>
