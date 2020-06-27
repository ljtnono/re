package cn.rootelement.controller;

import cn.rootelement.service.IReBlogService;
import cn.rootelement.util.JJWTUtil;
import cn.rootelement.util.RedisUtil;
import cn.rootelement.vo.JsonResultVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    private final IReBlogService iReBlogService;

    private final JJWTUtil jjwtUtil;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final RedisUtil redisUtil;

    @Autowired
    public AppController(IReBlogService iReBlogService, JJWTUtil jjwtUtil, @Qualifier("reUserDetailService") UserDetailsService userDetailsService, AuthenticationManager authenticationManager, RedisUtil redisUtil) {
        this.iReBlogService = iReBlogService;
        this.jjwtUtil = jjwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.redisUtil = redisUtil;
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public JsonResultVO login(String username, String password) {
        // TODO 首先可以从数据库中查询是否有这个用户名或者密码，然后根据结果返回错误
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwt = jjwtUtil.generateToken(userDetails);
        JsonResultVO jsonResultVO = JsonResultVO.forMessage("登陆成功", 200);
        jsonResultVO.addField("token", jwt);
        return jsonResultVO;
    }

    @GetMapping("/adminIndexData")
    @ResponseBody
    public JsonResultVO adminIndexData() {
        Integer comment = iReBlogService.countComment();
        Integer view = iReBlogService.countView();
        Map<String, Object> data = Maps.newHashMap();
        data.put("commentCount", comment);
        data.put("viewCount", view);
        return JsonResultVO.newBuilder()
                .data(null)
                .fields(data)
                .request("success")
                .message("请求成功")
                .status(HttpStatus.OK.value())
                .build();
    }
}
