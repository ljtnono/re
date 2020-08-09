package cn.ljtnono.re.service.system;

import cn.ljtnono.re.ReServiceSystemApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ljt
 * Date: 2020/8/9 22:56
 * Description: 用户服务类单元测试
 */
@SpringBootTest(classes = ReServiceSystemApplication.class)
@RunWith(SpringRunner.class)
public class ReUserServiceTest {

    @Autowired
    private ReUserService reUserService;

    @Test
    public void addUser() {

    }
}