<template>
    <Form ref="loginForm" :model="form" :rules="rules" @keydown.enter.native="handleSubmit">
        <FormItem prop="username">
            <Input v-model="form.username" placeholder="请输入用户名">
                <span slot="prepend">
                    <Icon :size="16" type="ios-person" />
                </span>
            </Input>
        </FormItem>
        <FormItem prop="password">
            <Input type="password" v-model="form.password" placeholder="请输入密码">
                <span slot="prepend">
                    <Icon :size="14" type="md-lock"/>
                </span>
            </Input>
        </FormItem>
        <FormItem>
            <Input class="verifyCode" v-model="form.verifyCode" placeholder="请输入验证码">
                <!-- 验证码 -->
                <div slot="suffix" @click="refreshVerifyCode">
                    <img :src="verifyCodeSrc" alt="验证码" />
                </div>
            </Input>
        </FormItem>
        <FormItem>
            <Button @click="handleSubmit" type="primary" long>登录</Button>
        </FormItem>
    </Form>
</template>
<script>
import {getVerifyCode} from "@/api/verifyCode/verifyCode";

export default {
    name: 'LoginForm',
    data() {
        return {
            form: {
                username: "",
                password: "",
                verifyCode: "",
                verifyCodeId: ""
            },
            verifyCodeSrc: null
        }
    },
    computed: {
        rules() {
            return {
                username: this.usernameRules,
                password: this.passwordRules
            }
        }
    },
    methods: {
        handleSubmit() {
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    this.$emit('on-success-valid', {
                        username: this.form.username,
                        password: this.form.password,
                        verifyCodeId: this.form.verifyCodeId,
                        verifyCode: this.form.verifyCode
                    });
                }
            });
        },
        refreshVerifyCode() {
            getVerifyCode().then(res => {
                const blob = new Blob([res.data]);
                this.verifyCodeSrc = URL.createObjectURL(blob);
                // 获取验证码结果
                this.form.verifyCodeId = res.headers["verifyCodeId".toLocaleLowerCase()];
            });
        }
    },
    props: {
        usernameRules: {
            type: Array,
            default: () => {
                return [
                    {required: true, message: '账号不能为空', trigger: 'message'}
                ]
            }
        },
        passwordRules: {
            type: Array,
            default: () => {
                return [
                    {required: true, message: '密码不能为空', trigger: 'message'}
                ]
            }
        }
    },
    mounted() {
        // 刷新验证码
        this.refreshVerifyCode();
    }
}
</script>

<style scoped lang="less">
.verifyCode {
    width: 150px;
    height: inherit;
    cursor: pointer;
    div {
        position: absolute;
        right: -100px;
    }
}
</style>
