package cn.lingjiatong.re.api.controller;

import cn.lingjiatong.re.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2024/2/1 15:36
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    private UserService userService;


    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    
    
}
