<template>
    <div class="user-avatar-dropdown">
        <Dropdown @on-click="handleClick">
            <Badge :dot="!!messageUnreadCount">
                <Avatar :src="userAvatar"/>
            </Badge>
            <Icon :size="18" type="md-arrow-dropdown"/>
            <DropdownMenu slot="list">
                <DropdownItem name="message">
                    消息中心
                    <Badge style="margin-left: 10px" :count="messageUnreadCount"/>
                </DropdownItem>
                <DropdownItem name="logout">退出登录</DropdownItem>
            </DropdownMenu>
        </Dropdown>
    </div>
</template>

<script>
import "./user.less";
import {mapActions} from "vuex";
import {LOGIN_PAGE_NAME} from "@/constant/systemConstant";
import Cookies from "js-cookie";

export default {
    name: "User",
    props: {
        userAvatar: {
            type: String,
            default: ""
        },
        messageUnreadCount: {
            type: Number,
            default: 22
        },
        username: {
            type: String,
            default: "admin"
        },
        roleName: {
            type: String,
            default: "超级管理员"
        }
    },
    methods: {
        ...mapActions([
            "handleLogOut",
            "clearUserCookie"
        ]),
        logout() {
            this.handleLogOut().then(result => {
                this.$Message.success({
                    background: true,
                    content: "退出登陆"
                });
                // 删除cookie
                this.clearUserCookie();
                this.$router.push({
                    name: LOGIN_PAGE_NAME
                });
            }).catch(error => {
                this.clearUserCookie();
                this.$router.push({
                    name: LOGIN_PAGE_NAME
                });
            });
        },
        message() {
            this.$router.push({
                name: "message_page"
            });
        },
        handleClick(name) {
            switch (name) {
                case "logout":
                    this.logout();
                    break;
                case "message":
                    this.message();
                    break;
            }
        }
    }
};
</script>
