package cn.ljtnono.re.common.enumeration.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作日志详情枚举类
 *
 * @author Ling, Jiatong
 * Date: 2021/1/1 10:51
 */
@Getter
@AllArgsConstructor
public enum LogDetailEnum {

    //******************** 用户模块 ********************//
    USER_LOGIN_SUCCESS("USER_LOGIN_SUCCESS", "用户登陆，用户名：{username}，ip：{ip}"),
    USER_LOGIN_FAILED("USER_LOGIN_FAILED", "用户登陆失败，原因：{cause}"),
    USER_LOGOUT("USER_LOGOUT", "用户注销，用户名：{username}"),
    
    //******************** 角色模块 ********************//


    //******************** 权限模块 ********************//

    ;


    /**
     * 操作名
     */
    private final String opName;

    /**
     * 操作详情
     */
    private final String opDetail;

}
