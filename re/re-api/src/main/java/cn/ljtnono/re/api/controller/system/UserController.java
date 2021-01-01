package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.annotation.LoginUser;
import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.system.UserDTO;
import cn.ljtnono.re.dto.system.UserListQueryDTO;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.service.system.UserService;
import cn.ljtnono.re.vo.system.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户模块接口
 *
 * @author Ling, Jiatong
 * Date 2020/7/16 1:19 上午
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/user")
@Api(value = "/api/v1/system/user", tags = "用户模块接口")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录接口
     *
     * @param dto 通用用户DTO对象 {@link UserDTO}
     * @return 用户登录VO对象 {@link UserLoginVO}
     * @author Ling, Jiatong
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public JsonResultVO<UserLoginVO> login(@RequestBody @ApiParam(required = true) UserDTO dto) {
        log.info("[re-system -> UserController -> login()] 用户登录，登录参数：{}", dto);
        return JsonResultVO.success(userService.login(dto));
    }

    /**
     * 用户登出（需要用户处于登陆状态）
     *
     * @param user 当前用户 {@link User}
     * @return 成功返回普通成功消息，失败抛出异常消息
     * @author Ling, Jiatong
     */
    @GetMapping("/logout")
    @ApiOperation("用户登出")
    public JsonResultVO<?> logout(@LoginUser User user) {
        log.info("[re-system -> UserController -> logout()] 用户登出，参数：{}", user);
        userService.logout(user);
        return JsonResultVO.success();
    }

    /**
     * 新增用户接口
     *
     * @param dto 通用用户DTO对象 {@link UserDTO}
     * @return 通用返回VO对象
     * @author Ling, Jiatong
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:user:add')")
    @ApiOperation("新增用户")
    public JsonResultVO<?> addUser(@RequestBody @ApiParam(required = true) UserDTO dto) {
        log.info("[re-system -> UserController -> addUser()] 新增用户：{}", dto);
        userService.addUser(dto);
        return JsonResultVO.success();
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return 通用返回VO对象
     * @author Ling, Jiatong
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation("根据id获取用户")
    @PreAuthorize("hasAnyAuthority('system:user', 'system:user:view')")
    public JsonResultVO<UserVO> getUserById(@PathVariable Integer id) {
        log.info("[re-system -> UserController -> getUserById()] 获取用户信息，用户id：{}", id);
        return JsonResultVO.success(userService.getUserById(id));
    }

    /**
     * 根据用户id列表批量删除用户
     * 逻辑删除
     *
     * @param dto 通用用户DTO对象 {@link UserDTO}
     * @return 通用返回VO对象
     * @author Ling, Jiatong
     */
    @DeleteMapping("/logic")
    @ApiOperation("根据id逻辑删除用户")
    @PreAuthorize("hasAnyAuthority('system:user:delete')")
    public JsonResultVO<?> logicDeleteById(@RequestBody UserDTO dto) {
        log.info("[re-system -> UserController -> logicDeleteById()] 逻辑删除用户，用户id：{}", dto);
        userService.logicDelete(dto);
        return JsonResultVO.success();
    }

    /**
     * 更新用户信息
     *
     * @param dto 通用用户DTO对象 {@link UserDTO}
     * @return 通用返回VO对象
     * @author Ling, Jiatong
     */
    @PutMapping
    @ApiOperation("更新用户")
    @PreAuthorize("hasAnyAuthority('system:user:update')")
    public JsonResultVO<?> updateUser(@RequestBody UserDTO dto) {
        log.info("[re-system -> UserController -> updateUser()] 更新用户，参数：{}", dto);
        userService.updateUser(dto);
        return JsonResultVO.success();
    }

    /**
     * 分页获取用户信息
     *
     * @param dto 通用用户DTO对象 {@link UserDTO}
     * @return 用户列表查询VO对象 {@link UserListVO}
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
    @ApiOperation("分页获取用户")
    @PreAuthorize("hasAnyAuthority('system:user', 'system:user:view')")
    public JsonResultVO<IPage<UserListVO>> list(UserListQueryDTO dto) {
        log.info("[re-system -> UserController -> list()] 分页获取用户信息，参数：{}", dto);
        return JsonResultVO.success(userService.getList(dto));
    }

    /**
     * 获取用户根据角色分类统计饼状图
     *
     * @return 用户根据角色分类统计饼状图VO对象列表 {@link UserRoleNumPieVO}
     * @author Ling, Jiatong
     */
    @GetMapping("/roleNumPie")
    @ApiOperation("获取用户根据角色分类统计饼状图")
    @PreAuthorize("hasAnyAuthority('system:user', 'system:user:view')")
    public JsonResultVO<List<UserRoleNumPieVO>> roleNumPie() {
        log.info("[re-system -> UserController -> roleNumPie()] 获取用户根据角色分类统计饼状图");
        return JsonResultVO.success(userService.roleNumPie());
    }

    /**
     * 上传用户头像
     *
     * @param multipartFile 用户头像文件对象
     * @param id 用户id
     * @author Ling, Jiatong
     */
    @ApiOperation("上传用户头像")
    @PostMapping("/{id:\\d+}/uploadAvatar")
    @PreAuthorize("hasAnyAuthority('system:user:update')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "multipartFile", paramType = "form", dataType = "org.springframework.web.multipart.MultipartFile"),
            @ApiImplicitParam(name = "id", required = true, paramType = "path", dataType = "java.lang.Integer")
    })
    public JsonResultVO<?> uploadAvatarImage(MultipartFile multipartFile, @PathVariable("id") Integer id) {
        log.info("[re-system -> UserController -> uploadAvatarImage()] 上传用户头像，用户id：{}", id);
        userService.uploadAvatarImage(multipartFile, id);
        return JsonResultVO.success();
    }

    /**
     * 获取在线用户统计信息
     *
     * @author Ling, Jiatong
     */
    @GetMapping("/onlineNumPie")
    @ApiOperation("获取在线用户统计信息")
    public JsonResultVO<UserOnlineVO> online() {
        log.info("[re-system -> UserController -> online()] 获取在线用户统计");
        return JsonResultVO.success(userService.online());
    }
}
