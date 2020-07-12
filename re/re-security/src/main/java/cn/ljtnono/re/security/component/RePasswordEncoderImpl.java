package cn.ljtnono.re.security.component;

import cn.ljtnono.re.common.util.Md5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author ljt
 * Date: 2020/7/7 20:36 下午
 * Description: 加密器
 */
@Component
public class RePasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return Md5Util.getInstance().getMd5LowerCase((String) charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(Md5Util.getInstance().getMd5LowerCase((String) charSequence));
    }
}
