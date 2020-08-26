package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.service.system.ReUserService;
import cn.ljtnono.re.vo.system.ReUserLoginVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
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

    private final ReUserService reUserService;

    public ReUserController(ReUserService reUserService) {
        this.reUserService = reUserService;
    }

    /**
     * 用户登录接口
     * @param reUserDTO 参数封装
     * @return ReJsonResultVO<ReUserLoginVO>
     */
    @PostMapping("/login")
    public ReJsonResultVO<ReUserLoginVO> login(@RequestBody ReUserDTO reUserDTO) {
        return ReJsonResultVO.success(reUserService.login(reUserDTO));

    }

    /**
     * 新增用户接口
     * @param reUserDTO 前端参数封装
     * @return ReJsonResultVO<?>
     */
    @PostMapping
    public ReJsonResultVO<?> addUser(@RequestBody ReUserDTO reUserDTO) {
        return reUserService.addUser(reUserDTO);
    }

    /**
     * 根据用户id获取用户信息
     * @param userId 用户id
     * @return ReJsonResultVO<ReUser>
     */
    @GetMapping("/{userId:\\d+}")
    public ReJsonResultVO<ReUser> getUserById(@PathVariable Integer userId) {
        return ReJsonResultVO.success(reUserService.getUserById(userId));
    }

    /**
     * 根据用户id删除用户
     * @param userId 用户id
     * @return ReJsonResultVO<?>
     */
    @DeleteMapping("/{userId:\\d+}")
    public ReJsonResultVO<?> deleteUserById(@PathVariable Integer userId) {
        reUserService.deleteUserById(userId);
        return ReJsonResultVO.success();
    }

    /**
     * 更新用户
     * @return ReJsonResultVO<?>
     */
    @PutMapping
    public ReJsonResultVO<?> updateUser(@RequestBody ReUserDTO reUserDTO) {
        reUserService.updateUser(reUserDTO);
        return ReJsonResultVO.success();
    }

    /**
     * 分页获取用户信息
     * @param reUserDTO 参数封装
     * @return ReJsonResultVO<IPage<ReUser>>
     */
    @GetMapping("/page")
    public ReJsonResultVO<IPage<ReUser>> page(ReUserDTO reUserDTO) {
        return ReJsonResultVO.success(reUserService.getUserListPage(reUserDTO));
    }

}
