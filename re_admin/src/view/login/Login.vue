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
import {HOME_PAGE_NAME, USERINFO_EXPIRE} from "@/constant/system/constant";
import {GlobalError} from "@/constant/globalError";

export default {
  name: "Login",
  data() {
    return {
      verifyCode: ""
    }
  },
  components: {
    LoginForm
  },
  methods: {
    ...mapActions([
      "handleLogin"
    ]),
    handleSubmit({username, password, verifyCodeId, verifyCode, forceLogin}) {
      let param = {username, password, verifyCodeId, verifyCode, forceLogin}
      let promise = this.handleLogin(param);
      promise.then(result => {
        // 登陆成功
        this.$Message.success({
          background: true,
          content: "登陆成功"
        });
        // 存储用户相关信息
        this.saveUserInfo(result.data);
        // 跳转到页面
        this.$router.push({
          name: HOME_PAGE_NAME
        });
      }).catch(error => {
        // 处理用户已经登录情况
        let code = error.code;
        if (code === GlobalError.get("USER_ALREADY_LOGIN_ERROR")) {
          let that = this;
          this.$Modal.warning({
            title: "用户已经登录，是否强制登陆",
            content: "您的账号已经在其他地方登陆，请确认是否是您自己的操作",
            onOk() {
              // 重新发送登陆请求
              param.forceLogin = 1;
              that.handleLogin(param).then(result => {
                // 登陆成功
                that.$Message.success({
                  background: true,
                  content: "登陆成功"
                });
                // 存储用户相关信息
                that.saveUserInfo(result.data);
                // 跳转到页面
                that.$router.push({
                  name: HOME_PAGE_NAME
                });
              }).catch(error => {});
            }
          });
        }
      });
    },
    // 存储用户信息, 存储在token中去
    saveUserInfo(data) {
      Cookies.set("userInfo", {
        id: data.id,
        roleId: data.roleId,
        token: data.token,
        username: data.username,
        permissionIdList: data.permissionIdList,
        roleName: data.roleName,
        email: data.email,
        phone: data.phone,
        deleted: data.deleted,
        avatarImage: data.avatarImage
      }, {expires: USERINFO_EXPIRE});
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
