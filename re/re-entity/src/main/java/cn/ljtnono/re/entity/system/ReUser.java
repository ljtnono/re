package cn.ljtnono.re.entity.system;

import cn.ljtnono.re.entity.base.ReBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ljt
 * Date: 2020/7/12 22:23 下午
 * Description: 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReUser extends ReBaseEntity implements Serializable, UserDetails {

    private static final long serialVersionUID = 4057606458753697276L;

    /** 用户名 */
    private String username;

    /** 用户密码 */
    private String password;

    /** 手机号码 */
    private String phone;

    /** 用户邮箱 */
    private String email;

    /** 角色id */
    @TableField(exist = false)
    private Integer roleId;

    /** 角色名 */
    @TableField(exist = false)
    private String roleName;

    /** 是否删除 1 删除 0 正常 */
    @TableField("is_deleted")
    private Integer deleted;

    /** 用户权限列表 */
    @TableField(exist = false)
    private List<RePermission> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 根据Permission列表来构造
        List<SimpleGrantedAuthority> au = new ArrayList<>();
        if (authorities != null) {
            // 根据 expression字段构造权限
            authorities.forEach(a -> au.add(new SimpleGrantedAuthority(a.getExpression())));
        }
        return au;
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
