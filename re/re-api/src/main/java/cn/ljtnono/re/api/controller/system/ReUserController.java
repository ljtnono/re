package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.util.rest.RestTemplateUtil;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.security.component.ReUserDetailsServiceImpl;
import cn.ljtnono.re.security.util.ReJwtUtil;
import cn.ljtnono.re.vo.ReJsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ljt
 * Date 2020/7/16 1:19 上午
 * Description: 用户接口
 */
@RestController
@RequestMapping("/api/v1/system/user")
@Slf4j
@Api(description = "用户接口")
public class ReUserController {

    private final AuthenticationManager authenticationManager;

    private final ReJwtUtil reJwtUtil;

    private final ReUserDetailsServiceImpl reUserDetailsServiceImpl;

    private final RestTemplateUtil restTemplateUtil;

    public ReUserController(AuthenticationManager authenticationManager, ReJwtUtil reJwtUtil, ReUserDetailsServiceImpl reUserDetailsServiceImpl, RestTemplateUtil restTemplateUtil) {
        this.authenticationManager = authenticationManager;
        this.reJwtUtil = reJwtUtil;
        this.reUserDetailsServiceImpl = reUserDetailsServiceImpl;
        this.restTemplateUtil = restTemplateUtil;
    }

    @GetMapping("/test")
    public void test() {
        RestTemplateUtil.Builder build = new RestTemplateUtil.Builder()
                .url("https://rootelement.cn/api/blog/{blogId}")
                .enablePathVariable(true)
                .addParam("blogId", 10043)
                .build();
        Map<String, Object> map = restTemplateUtil.getForObject(build, Map.class);
        System.out.println(map);

    }

    @PostMapping("/login")
    @ApiOperation(value = "用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    public ReJsonResultVO<?> login(@RequestBody ReUser reUser) {
        log.info("[re -> ReUserController] 用户登录，请求参数：{}", reUser);
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(reUser.getUsername(), reUser.getPassword());
        Authentication authenticate = authenticationManager.authenticate(upToken);
        UserDetails userDetails = reUserDetailsServiceImpl.loadUserByUsername(reUser.getUsername());
        String token = reJwtUtil.generateToken(userDetails);
        return ReJsonResultVO.success(token);
    }

    @GetMapping("/testRest")
    public ReJsonResultVO<?> testRest() {
        return ReJsonResultVO.success(null);
    }

}
