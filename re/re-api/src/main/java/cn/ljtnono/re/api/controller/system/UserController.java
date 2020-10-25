package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.annotation.LoginUser;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.dto.system.UserDTO;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.common.annotation.PassToken;
import cn.ljtnono.re.service.system.UserService;
import cn.ljtnono.re.vo.system.UserLoginVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ling, Jiatong
 * Date 2020/7/16 1:19 上午
 * Description: 用户Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * @param reUserDTO 参数封装
     * @return ReJsonResultVO<ReUserLoginVO>
     * @author Ling, Jiatong
     */
    @PassToken
    @PostMapping("/login")
    public ReJsonResultVO<UserLoginVO> login(@RequestBody UserDTO reUserDTO) {
        log.info("[re-system -> ReUserController -> login()] 用户登录，登录参数：{}", reUserDTO);
        return ReJsonResultVO.success(userService.login(reUserDTO));
    }

    /**
     * 用户登出
     * @param reUser 当前用户
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     *
     */
    @PassToken
    @GetMapping("/logout")
    public ReJsonResultVO<?> logout(@LoginUser User reUser) {
        log.info("[re-system -> ReUserController -> logout()] 用户登出，参数：{}", reUser);
        userService.logout(reUser);
        return ReJsonResultVO.success();
    }

    /**
     * 新增用户接口
     * @param reUserDTO 前端参数封装
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:user:add')")
    public ReJsonResultVO<?> addUser(@RequestBody UserDTO reUserDTO) {
        log.info("[re-system -> ReUserController -> addUser()] 新增用户：{}", reUserDTO);
        userService.addUser(reUserDTO);
        return ReJsonResultVO.success();
    }

    /**
     * 根据用户id获取用户信息
     * @param id 用户id
     * @return ReJsonResultVO<ReUser>
     * @author Ling, Jiatong
     */
    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:user:view')")
    public ReJsonResultVO<User> getUserById(@PathVariable Integer id) {
        log.info("[re-system -> ReUserController -> getUserById()] 获取用户信息，用户id：{}", id);
        return ReJsonResultVO.success(userService.getUserById(id));
    }

    /**
     * 根据用户id删除用户，逻辑删除
     * @param id 用户id
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:user:delete')")
    public ReJsonResultVO<?> logicDeleteById(@PathVariable Integer id) {
        log.info("[re-system -> ReUserController -> logicDeleteById()] 逻辑删除用户，用户id：{}", id);
        userService.logicDeleteById(id);
        return ReJsonResultVO.success();
    }

    /**
     * 更新用户
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:user:update')")
    public ReJsonResultVO<?> updateUser(@RequestBody UserDTO reUserDTO) {
        log.info("[re-system -> ReUserController -> updateUser()] 更新用户，参数：{}", reUserDTO);
        userService.updateUser(reUserDTO);
        return ReJsonResultVO.success();
    }

    /**
     * 分页获取用户信息
     * @param reUserDTO 参数封装
     * @return ReJsonResultVO<IPage<ReUser>>
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
//    @PassToken
    @PreAuthorize("hasAnyAuthority('system:user:view')")
    public ReJsonResultVO<IPage<User>> list(UserDTO reUserDTO) {
        log.info("[re-system -> ReUserController -> list()] 分页获取用户信息，参数：{}", reUserDTO);
        return ReJsonResultVO.success(userService.getUserListPage(reUserDTO));
    }

}
