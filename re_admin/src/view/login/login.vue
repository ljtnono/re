<template>
    <div class="login">
        <div class="login-con">
            <Card icon="log-in" title="欢迎登录" :bordered="false">
                <div class="form-con">
                    <login-form @on-success-valid="handleSubmit"/>
                </div>
            </Card>
        </div>
    </div>
</template>

<script>
import LoginForm from '_c/login-form'
import {mapActions} from 'vuex'
import Cookies from 'js-cookie';

export default {
    data() {
        return {
            verifyCode: null
        }
    },
    components: {
        LoginForm
    },
    methods: {
        ...mapActions([
            "handleLogin"
        ]),
        handleSubmit({username, password, verifyCodeId, verifyCode}) {
            this.handleLogin({username, password, verifyCodeId, verifyCode}).then(res => {
                // 登陆成功
                this.$Message.success("登陆成功");
                // 存储用户相关信息
                this.saveUserInfo(res);
                // 跳转到页面
                this.$router.push({
                    name: this.$config.homeName
                });
            }).catch(error => {
                this.$Message.error(error.message);
            })
        },
        // 存储用户信息
        saveUserInfo(res) {
            Cookies.set("token", res["token"]);
            this.$store.state.userId = res["userId"];
            this.$store.state.roleId = res["roleId"];
            this.$store.state.token = res["token"];
            this.$store.state.permissionList = res["permissionList"];
            this.$store.state.roleName = res["roleName"];
            this.$store.state.email = res["email"];
            this.$store.state.phone = res["phone"];
            this.$store.state.deleted = res["deleted"];
        }
    }
}
</script>

<style scoped lang="less">
.login {
    width: 100%;
    height: 100%;
    background-image: url('../../assets/images/login-bg.jpg');
    background-size: cover;
    background-position: center;
    position: relative;

    &-con {
        position: absolute;
        right: 160px;
        top: 50%;
        transform: translateY(-60%);
        width: 300px;

        &-header {
            font-size: 16px;
            font-weight: 300;
            text-align: center;
            padding: 30px 0;
        }

        .form-con {
            padding: 10px 0 0;
        }

        .login-tip {
            font-size: 10px;
            text-align: center;
            color: #c3c3c3;
        }
    }
}
</style>
