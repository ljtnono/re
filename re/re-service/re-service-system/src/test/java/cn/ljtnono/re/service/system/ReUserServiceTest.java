package cn.ljtnono.re.service.system;

import cn.ljtnono.re.ReServiceSystemApplication;
import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.exception.UserValidateException;
import cn.ljtnono.re.dto.system.ReUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ljt
 * Date: 2020/8/9 22:56
 * Description: 用户服务类单元测试
 */
@SpringBootTest(classes = ReServiceSystemApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ReUserServiceTest {

    @Autowired
    private ReUserService reUserService;

    @Test
    public void addUser() {
//        null测试
        GlobalException nullTest = assertThrows(GlobalException.class, () -> reUserService.addUser(null));
        assertEquals(ReErrorEnum.REQUEST_PARAM_ERROR.getCode(), nullTest.getCode());
        assertEquals(ReErrorEnum.REQUEST_PARAM_ERROR.getMessage(), nullTest.getMessage());

//        用户名格式错误
        UserValidateException usernameException = assertThrows(UserValidateException.class, () -> {
            ReUserDTO reUserDTO = new ReUserDTO();
            String[] errorUsername = new String[]{
                    "ljt", "ljtnono@##",
                    null, "addeafgegeageadsfdafewafwf",
                    "dafewAAAAAAwWWWW999999",
                    null, "l",
                    "j", "a",
                    "b", null
            };
            String username = errorUsername[new Random().nextInt(errorUsername.length)];
            log.info("username=" + username);
            reUserDTO.setUsername(username);
            reUserService.addUser(reUserDTO);
        });
        assertEquals(ReErrorEnum.USERNAME_FORMAT_ERROR.getCode(), usernameException.getCode());
        assertEquals(ReErrorEnum.USERNAME_FORMAT_ERROR.getMessage(), usernameException.getMessage());

//        密码格式错误
        UserValidateException passwordException = assertThrows(UserValidateException.class, () -> {
            ReUserDTO reUserDTO = new ReUserDTO();
            String[] errorPassword = new String[]{
//                    "ljt", "ljtnono@##",
//                    null, "addeafgegeageadsfdafewafwf",
//                    "dafewAAAAAAwWWWW999999",
//                    null, "l",
//                    "j", "a",
//                    "b", null
            };
            String password = errorPassword[new Random().nextInt(errorPassword.length)];
            reUserDTO.setUsername("testUsername");
            reUserDTO.setPassword(password);
            reUserService.addUser(reUserDTO);
        });
        assertEquals(ReErrorEnum.PASSWORD_FORMAT_ERROR.getCode(), passwordException.getCode());
        assertEquals(ReErrorEnum.PASSWORD_FORMAT_ERROR.getMessage(), passwordException.getMessage());
    }
}