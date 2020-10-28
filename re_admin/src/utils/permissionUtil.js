import Vue from "vue";
import iView from "iview";

/**
 * 判断当前用户是否有权限进入相应的页面
 * @param page 要跳转的页面
 */
export const canTurnToPage = (page) => {
    if (page == null) {
        // 页面不存在
        return false;
    } else {
        // TODO 判断各种权限页面是否能够跳转
        return true;
    }
};
