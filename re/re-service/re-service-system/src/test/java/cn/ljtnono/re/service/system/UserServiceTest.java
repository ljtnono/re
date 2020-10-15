package cn.ljtnono.re.service.system;

import cn.ljtnono.re.ReServiceSystemApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/14 0:19
 * Description: 用户服务类单元测试
 */
@SpringBootTest(classes = ReServiceSystemApplication.class)
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private ReUserService reUserService;


}
