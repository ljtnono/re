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
                this.$Message.info(res.message);
                this.$router.push({
                    name: this.$config.homeName
                });
            }).catch(error => {
                this.$Message.error(error.message);
            })
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
