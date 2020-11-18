package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.annotation.LoginUser;
import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.system.UserDTO;
import cn.ljtnono.re.dto.system.UserListQueryDTO;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.common.annotation.PassToken;
import cn.ljtnono.re.service.system.UserService;
import cn.ljtnono.re.vo.system.UserListVO;
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
     * @param userDTO 参数封装
     * @return ReJsonResultVO<ReUserLoginVO>
     * @author Ling, Jiatong
     */
    @PassToken
    @PostMapping("/login")
    public JsonResultVO<UserLoginVO> login(@RequestBody UserDTO userDTO) {
        log.info("[re-system -> UserController -> login()] 用户登录，登录参数：{}", userDTO);
        return JsonResultVO.success(userService.login(userDTO));
    }

    /**
     * 用户登出
     * @param user 当前用户
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     *
     */
    @PassToken
    @GetMapping("/logout")
    public JsonResultVO<?> logout(@LoginUser User user) {
        log.info("[re-system -> UserController -> logout()] 用户登出，参数：{}", user);
        userService.logout(user);
        return JsonResultVO.success();
    }

    /**
     * 新增用户接口
     * @param reUserDTO 前端参数封装
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:user:add')")
    public JsonResultVO<?> addUser(@RequestBody UserDTO reUserDTO) {
        log.info("[re-system -> UserController -> addUser()] 新增用户：{}", reUserDTO);
        userService.addUser(reUserDTO);
        return JsonResultVO.success();
    }

    /**
     * 根据用户id获取用户信息
     * @param id 用户id
     * @return ReJsonResultVO<ReUser>
     * @author Ling, Jiatong
     */
    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:user:view')")
    public JsonResultVO<User> getUserById(@PathVariable Integer id) {
        log.info("[re-system -> UserController -> getUserById()] 获取用户信息，用户id：{}", id);
        return JsonResultVO.success(userService.getUserById(id));
    }

    /**
     * 根据用户id删除用户，逻辑删除
     * @param id 用户id
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:user:delete')")
    public JsonResultVO<?> logicDeleteById(@PathVariable Integer id) {
        log.info("[re-system -> UserController -> logicDeleteById()] 逻辑删除用户，用户id：{}", id);
        userService.logicDeleteById(id);
        return JsonResultVO.success();
    }

    /**
     * 更新用户
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:user:update')")
    public JsonResultVO<?> updateUser(@RequestBody UserDTO userDTO) {
        log.info("[re-system -> UserController -> updateUser()] 更新用户，参数：{}", userDTO);
        userService.updateUser(userDTO);
        return JsonResultVO.success();
    }

    /**
     * 分页获取用户信息
     * @param dto 参数封装
     * @return ReJsonResultVO<IPage<ReUser>>
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('system:user:view')")
    public JsonResultVO<IPage<UserListVO>> list(UserListQueryDTO dto) {
        log.info("[re-system -> UserController -> list()] 分页获取用户信息，参数：{}", dto);
        return JsonResultVO.success(userService.getList(dto));
    }

}
