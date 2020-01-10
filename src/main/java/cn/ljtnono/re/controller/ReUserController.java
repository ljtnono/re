package cn.ljtnono.re.controller;


import cn.ljtnono.re.entity.ReUser;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collections;

/**
 * 用户controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class ReUserController {


    private final IReUserService iReUserService;

    @Autowired
    public ReUserController(IReUserService iReUserService) {
        this.iReUserService = iReUserService;
    }

    @GetMapping("/{id:\\d+}")
    public JsonResult getUserById(@PathVariable Integer id, HttpSession session) {
        ReUser byId = iReUserService.getById(id);
        session.setAttribute("user", byId);
        return JsonResult.success(Collections.singletonList(byId), 1);
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        session.setAttribute("user", username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        return "back/index";
    }


    @GetMapping("/logout")
    public JsonResult logout() {

        return null;
    }

    public JsonResult listEntityAll() {
        return null;
    }

    public JsonResult saveEntity(ReUser entity) {
        return null;
    }

    public JsonResult updateEntityById(Serializable id, ReUser entity) {
        return null;
    }

    public JsonResult deleteEntityById(Serializable id) {
        return null;
    }

    public JsonResult getEntityById(Serializable id) {
        return null;
    }
}
