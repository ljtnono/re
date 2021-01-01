<template>
  <div class="ck-editor">
    <ckeditor :editor="editor" v-model="editorData" :config="editorConfig">
    </ckeditor>
  </div>
</template>

<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import CKEditor from "@ckeditor/ckeditor5-vue2"
export default {
  name: "BlogEditor",
  components: {
    ckeditor: CKEditor.component
  },
  data() {
    return {
      editor: ClassicEditor,
      editorData: '<p>Content of the editor.</p>',
      editorConfig: {

      }
    }
  }
}
</script>

<style scoped lang="less">
  .ck-editor {
    width: 500px;
    height: 600px;
  }
</style>
