package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.annotation.LoginUser;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.common.annotation.PassToken;
import cn.ljtnono.re.service.system.ReUserService;
import cn.ljtnono.re.vo.system.ReUserLoginVO;
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
public class ReUserController {

    @Autowired
    private ReUserService reUserService;

    /**
     * 用户登录接口
     * @param reUserDTO 参数封装
     * @return ReJsonResultVO<ReUserLoginVO>
     * @author Ling, Jiatong
     */
    @PassToken
    @PostMapping("/login")
    public ReJsonResultVO<ReUserLoginVO> login(@RequestBody ReUserDTO reUserDTO) {
        log.info("[re-system -> ReUserController -> login()] 用户登录，登录参数：{}", reUserDTO);
        return ReJsonResultVO.success(reUserService.login(reUserDTO));
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
    public ReJsonResultVO<?> logout(@LoginUser ReUser reUser) {
        log.info("[re-system -> ReUserController -> logout()] 用户登出，参数：{}", reUser);
        reUserService.logout(reUser);
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
    public ReJsonResultVO<?> addUser(@RequestBody ReUserDTO reUserDTO) {
        log.info("[re-system -> ReUserController -> addUser()] 新增用户：{}", reUserDTO);
        reUserService.addUser(reUserDTO);
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
    public ReJsonResultVO<ReUser> getUserById(@PathVariable Integer id) {
        log.info("[re-system -> ReUserController -> getUserById()] 获取用户信息，用户id：{}", id);
        return ReJsonResultVO.success(reUserService.getUserById(id));
    }

    /**
     * 根据用户id删除用户，逻辑删除
     * @param id 用户id
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasAnyAuthority('system:user:delete')")
    public ReJsonResultVO<?> logicDeleteUser(@PathVariable Integer id) {
        log.info("[re-system -> ReUserController -> logicDeleteUser()] 逻辑删除用户，用户id：{}", id);
        reUserService.logicDeleteById(id);
        return ReJsonResultVO.success();
    }

    /**
     * 更新用户
     * @return ReJsonResultVO<?>
     * @author Ling, Jiatong
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:user:update')")
    public ReJsonResultVO<?> updateUser(@RequestBody ReUserDTO reUserDTO) {
        log.info("[re-system -> ReUserController -> updateUser()] 更新用户，参数：{}", reUserDTO);
        reUserService.updateUser(reUserDTO);
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
    public ReJsonResultVO<IPage<ReUser>> list(ReUserDTO reUserDTO) {
        log.info("[re-system -> ReUserController -> list()] 分页获取用户信息，参数：{}", reUserDTO);
        return ReJsonResultVO.success(reUserService.getUserListPage(reUserDTO));
    }

}
