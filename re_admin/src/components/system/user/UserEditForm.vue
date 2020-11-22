<template>
    <div class="user-edit-form">
        <Form v-model="formData" :label-width="60">
            <FormItem label="用户名">
                <Input v-model="formData.username" placeholder="请输入用户名" />
            </FormItem>
            <FormItem label="用户名">
                <Input v-model="formData.username" placeholder="请输入用户名" />
            </FormItem>
            <FormItem label="用户名">
                <Input v-model="formData.username" placeholder="请输入用户名" />
            </FormItem>
            <FormItem label="用户名">
                <Input v-model="formData.username" placeholder="请输入用户名" />
            </FormItem>
            <FormItem label="用户名">
                <Input v-model="formData.username" placeholder="请输入用户名" />
            </FormItem>
            <FormItem label="用户名">
                <Input v-model="formData.username" placeholder="请输入用户名" />
            </FormItem>
            <FormItem label="用户名">
                <Input v-model="formData.username" placeholder="请输入用户名" />
            </FormItem>
        </Form>
    </div>
</template>

<script>
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
            }
        }
    },
    props: {}
}
</script>

<style scoped lang="less">

</style>
