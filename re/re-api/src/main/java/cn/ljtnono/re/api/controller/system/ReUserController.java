package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.util.rest.RestTemplateUtil;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.security.util.ReJwtUtil;
import cn.ljtnono.re.service.system.ReUserService;
import cn.ljtnono.re.vo.system.ReUserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

/**
 * @author ljt
 * Date 2020/7/16 1:19 上午
 * Description: 用户接口
 */
@RestController
@RequestMapping("/api/v1/system/user")
@Slf4j
public class ReUserController {

    private final AuthenticationManager authenticationManager;

    private final ReJwtUtil reJwtUtil;

    private final ReUserService reUserService;

    public ReUserController(AuthenticationManager authenticationManager, ReJwtUtil reJwtUtil, ReUserService reUserService, RestTemplateUtil restTemplateUtil) {
        this.authenticationManager = authenticationManager;
        this.reJwtUtil = reJwtUtil;
        this.reUserService = reUserService;
    }

    /**
     * 用户登录接口
     * @param reUserDTO 参数封装
     * @return ReJsonResultVO<ReUserLoginVO>
     */
    @PostMapping("/login")
    public ReJsonResultVO<ReUserLoginVO> login(@RequestBody ReUserDTO reUserDTO) {
        log.info("[re -> ReUserController] 用户登录，请求参数：{}", reUserDTO);
        return ReJsonResultVO.success(reUserService.login(reUserDTO));

    }

    /**
     * 新增用户接口
     * @param reUserDTO 前端参数封装
     * @return ReJsonResultVO<?>
     */
    @PostMapping
    public ReJsonResultVO<?> addUser(@RequestBody ReUserDTO reUserDTO) {
        log.info("[re -> ReUserController] 用户新增，请求参数：{}", reUserDTO);
        return reUserService.addUser(reUserDTO);
    }

    /**
     * 根据用户id获取用户信息
     * @param userId 用户id
     * @return ReJsonResultVO<ReUser>
     */
    @GetMapping("/{userId:\\d+}")
    public ReJsonResultVO<ReUser> getUserById(@PathVariable Integer userId) {
        log.info("[re -> ReUserController] 获取用户信息，请求参数：{}", userId);
        return ReJsonResultVO.success(reUserService.getUserById(userId));
    }
}
