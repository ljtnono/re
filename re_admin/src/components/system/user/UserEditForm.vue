<template>
    <div class="user-edit-form">
        <Form ref="formData" v-model="formData" :label-width="60" :rules="rules">
            <FormItem prop="username" label="用户名">
                <Input type="text" v-model="formData.username" placeholder="请输入用户名" />
            </FormItem>
            <FormItem prop="password" label="密码">
                <Input type="password" v-model="formData.password" placeholder="请输入用户名" />
            </FormItem>
            <FormItem prop="roleId" label="角色">
                <Select v-model="formData.roleId">
                    <Option v-for="item in roleSelectList" :value="item.id" :key="item.name">{{ item.name }}</Option>
                </Select>
            </FormItem>
            <FormItem prop="email" label="邮箱">
                <Input v-model="formData.email" placeholder="请输入用户名" />
            </FormItem>
            <FormItem prop="phone" label="电话">
                <Input v-model="formData.phone" placeholder="请输入用户名" />
            </FormItem>
        </Form>
    </div>
</template>

<script>
import {select} from "@/api/system/role";

export default {
    name: "UserEditForm",
    data() {
        return {
            formData: {
                id: null,
                username: "",
                password: "",
                roleId: "",
                email: "",
                phone: ""
            },
            roleSelectList: [],
            rules: {
                username: [{required: true, message: "用户名不能为空"}],
                password: [{required: true, message: "密码不能为空"}],
                email: [{required: true, message: "邮箱不能为空"}],
                phone: [{required: true, message: "电话不能为空"}]
            }
        }
    },
    props: {
        // 是否处于显示状态
        isShow: {
            type: Boolean,
            default: false
        }
    },
    // 监听是否处于显示状态
    watch: {
        isShow(val) {
            if (val) {
                // 打开的时候清空表单数据
                this.$refs["formData"].resetFields();
                this.getSelectList();
            }
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
            }).catch(error => {});
        }
    },
    mounted() {
    }
}
</script>

<style scoped lang="less">

</style>
