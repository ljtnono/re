package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.security.component.ReUserDetailsServiceImpl;
import cn.ljtnono.re.security.util.ReJwtUtil;
import cn.ljtnono.re.vo.ReJsonResultVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/user")
@Slf4j
@Api(description = "用户接口")
public class ReUserController {

    private final AuthenticationManager authenticationManager;

    private final ReJwtUtil reJwtUtil;

    private final ReUserDetailsServiceImpl reUserDetailsServiceImpl;

    public ReUserController(AuthenticationManager authenticationManager, ReJwtUtil reJwtUtil, ReUserDetailsServiceImpl reUserDetailsServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.reJwtUtil = reJwtUtil;
        this.reUserDetailsServiceImpl = reUserDetailsServiceImpl;
    }


    @GetMapping("/test")
    public void test() {
        log.info("ReUserController执行了");
    }

    @PostMapping("/login")
    public ReJsonResultVO<?> login(@RequestBody ReUser reUser) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(reUser.getUsername(), reUser.getPassword());
        Authentication authenticate = authenticationManager.authenticate(upToken);
        UserDetails userDetails = reUserDetailsServiceImpl.loadUserByUsername(reUser.getUsername());
        String token = reJwtUtil.generateToken(userDetails);
        return ReJsonResultVO.success(token);
    }
}
