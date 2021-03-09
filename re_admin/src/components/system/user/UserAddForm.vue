<template>
  <div class="user-edit-form">
    <Form ref="form" v-model="formData" :label-width="60" :rules="rules">
      <FormItem prop="username" label="用户名">
        <Input type="text" v-model="formData.username" placeholder="请输入用户名"/>
      </FormItem>
      <FormItem prop="password" label="密码">
        <Input type="password" v-model="formData.password" placeholder="请输入密码"/>
      </FormItem>
      <FormItem prop="roleId" label="角色">
        <Select v-model="formData.roleId">
          <Option v-for="item in roleSelectList" :value="item.id" :key="item.name">{{ item.name }}</Option>
        </Select>
      </FormItem>
      <FormItem prop="email" label="邮箱">
        <Input v-model="formData.email" placeholder="请输入邮箱"/>
      </FormItem>
      <FormItem prop="phone" label="手机号码">
        <Input v-model="formData.phone" placeholder="请输入手机号码"/>
      </FormItem>
      <FormItem>
        <Button type="primary" @click="submit">提交</Button>
        <Button style="margin-left: 20px;" @click="cancel">取消</Button>
      </FormItem>
    </Form>
    <Spin size="large" fix v-if="spinShow"/>
  </div>
</template>

<script>

import {select} from "@/api/system/role";
import {addUser} from "@/api/system/user";

export default {
  name: "UserAddForm",
  data() {
    return {
      formData: {
        username: "",
        password: "",
        roleId: "",
        email: "",
        phone: ""
      },
      rules: {
        username: [{validator: this.validateUsername, required: true, trigger: 'message'}],
        email: [{validator: this.validateEmail,required: true, message: "邮箱不能为空"}],
        phone: [{validator: this.validatePhone,required: true, message: "手机号码不能为空"}],
        roleId: [{validator: this.validateRoleId, required: true, message: "用户角色不能为空"}]
      },
      roleSelectList: [],
      spinShow: false
    }
  },
  props: {
    // 是否处于显示状态
    isShow: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    // 获取角色下拉列表并设置到下拉列表中去
    getSelectList() {
      select().then(result => {
        let data = result.data;
        if (data.code === 0 && data.message === "success") {
          this.roleSelectList = data.data;
        }
      });
    },
    // 提交表单
    submit() {
      addUser({...this.formData}).then(result => {
        this.spinShow = true;
        let data = result.data;
        if (data.code === 0 && data.message === "success") {
          this.$Message.success({
            background: true,
            content: "修改成功"
          });
          // 关闭窗口，删除cookie
          this.closeModal();
          this.clearUserCookie();
        }
        this.spinShow = false;
      }).catch(error => {
      });
    },
    // 点击取消
    cancel() {
      this.closeModal();
    },
    // 关闭弹窗
    closeModal() {
      this.$emit("closeModal");
    },
    // 校验用户名
    validateUsername() {
      let username = this.formData.username;
      return !(username == null || username === "");
    },
    validateEmail() {
      let email = this.formData.email;
      return !(email === null || email === "");
    },
    validatePhone() {
      let phone = this.formData.phone;
      return !(phone === null || phone === "");
    },
    validateRoleId() {
      let roleId = this.formData.roleId;
      let roleIdList = this.roleSelectList.map(v => v.id);
      return roleIdList.includes(roleId);
    }
  },
  mounted() {
    this.getSelectList();
  }
}
</script>

<style scoped>

</style>
