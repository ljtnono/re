package cn.ljtnono.re.service.system;

import cn.ljtnono.re.ReServiceSystemApplication;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.dto.system.UserDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/14 0:19
 * Description: 用户服务类单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReServiceSystemApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void login() {
        // 判断是否能够
        GlobalException globalException = Assertions.assertThrows(GlobalException.class, () -> {
            UserDTO dto = new UserDTO();
            dto.setUsername("admin");
            dto.setPassword("ljtLJT715336");
            userService.login(dto);
        });
    }

    @Test
    public void logout() {

    }

    @Test
    public void list() {

    }

}
