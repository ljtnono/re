package cn.ljtnono.re.entity.security;

import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author ljt
 * Date: 2020/7/12 22:58
 * Description: security中对用户的抽象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReUserDetailsImpl extends ReUser implements UserDetails {

    private List<RePermission> authorities;

    public ReUserDetailsImpl() {

    }
    public ReUserDetailsImpl(List<RePermission> authorities, ReUser reUser) {
        this.authorities = authorities;
        super.setUsername(reUser.getUsername());
        super.setCreateDate(reUser.getCreateDate());
        super.setDel(reUser.getDel());
        super.setEmail(reUser.getEmail());
        super.setId(reUser.getId());
        super.setModifyDate(reUser.getModifyDate());
        super.setPassword(reUser.getPassword());
        super.setQq(reUser.getQq());
        super.setTel(reUser.getTel());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
