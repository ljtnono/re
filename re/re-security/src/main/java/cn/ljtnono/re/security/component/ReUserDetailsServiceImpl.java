package cn.ljtnono.re.security.component;

import cn.ljtnono.re.entity.security.ReUserDetailsImpl;
import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ljt
 * Date: 2020/7/11 23:23 下午
 * Description: token过滤器
 */
@Component
public class ReUserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        ReUser reUser = new ReUser();
        reUser.setUsername("ljtnono");
        reUser.setPassword("80cea81e681679a81634e2b1846e6cb8");
        reUser.setQq("935188400");
        reUser.setEmail("935188400@qq.com");
        reUser.setId(1);
        reUser.setCreateDate(new Date());
        reUser.setModifyDate(new Date());
        reUser.setTel("15337106753");
        List<RePermission> rePermissions = new ArrayList<>();
        return new ReUserDetailsImpl(rePermissions, reUser);
    }
}
