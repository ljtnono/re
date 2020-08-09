package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.util.rest.RestTemplateUtil;
import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.security.util.ReJwtUtil;
import cn.ljtnono.re.service.system.ReUserService;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final ReUserService reUserService;

    private final RestTemplateUtil restTemplateUtil;

    public ReUserController(AuthenticationManager authenticationManager, ReJwtUtil reJwtUtil, ReUserService reUserService, RestTemplateUtil restTemplateUtil) {
        this.authenticationManager = authenticationManager;
        this.reJwtUtil = reJwtUtil;
        this.reUserService = reUserService;
        this.restTemplateUtil = restTemplateUtil;
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    public ReJsonResultVO<?> login(@RequestBody ReUser reUser) {
        log.info("[re -> ReUserController] 用户登录，请求参数：{}", reUser);
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(reUser.getUsername(), reUser.getPassword());
        authenticationManager.authenticate(upToken);
        UserDetails userDetails = reUserService.loadUserByUsername(reUser.getUsername());
        String token = reJwtUtil.generateToken(userDetails);
        return ReJsonResultVO.success(token);
    }

    /**
     * 用户注册接口
     * @param reUserDTO 前端参数封装
     * @return ReJsonResultVO<?>
     */
    @PostMapping("/register")
    public ReJsonResultVO<?> register(@RequestBody ReUserDTO reUserDTO) {
        log.info("[re -> ReUserController] 用户注册，请求参数：{}", reUserDTO);
        return reUserService.register(reUserDTO);
    }

}
