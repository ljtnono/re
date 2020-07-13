package cn.ljtnono.re.security.component;

import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.mapper.system.ReUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljt
 * Date: 2020/7/11 23:23 下午
 * Description: token过滤器
 */
@Component
public class ReUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private ReUserMapper reUserMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 根据用户名查出用户所有权限
        ReUserDTO reUserDTO = new ReUserDTO();
        List<RePermission> permission = reUserMapper.getPermissionList(reUserDTO);
        ReUser reUser = reUserMapper.getUser(reUserDTO);
        reUser.setAuthorities(permission);
        return reUser;
    }
}
