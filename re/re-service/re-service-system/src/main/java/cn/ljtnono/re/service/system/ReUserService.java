package cn.ljtnono.re.service.system;

import cn.ljtnono.re.mapper.system.ReUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ljt
 * Date: 2020/8/2 0:50
 * Description:
 */
@Service
@Slf4j
public class ReUserService implements UserDetailsService {

    @Resource
    private ReUserMapper reUserMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
