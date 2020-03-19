package cn.rootelement.controller;

import cn.rootelement.service.IReBlogService;
import cn.rootelement.util.JJWTUtil;
import cn.rootelement.vo.JsonResultVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 处理页面路由的Controller
 * @author ljt
 * @date 2019/12/28
 * @version 1.0.1
 */
@Slf4j
@Controller
@Api(value = "AppController", tags = {"页面路由"})
public class AppController {

    private IReBlogService iReBlogService;

    private JJWTUtil jjwtUtil;

    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;

    @Autowired
    public AppController(IReBlogService iReBlogService, JJWTUtil jjwtUtil, @Qualifier("reUserDetailService") UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.iReBlogService = iReBlogService;
        this.jjwtUtil = jjwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public JsonResultVO login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwt = jjwtUtil.generateToken(userDetails);
        JsonResultVO jsonResultVO = JsonResultVO.successForMessage("登陆成功", 200);
        jsonResultVO.addField("token", jwt);
        return jsonResultVO;
    }
}
