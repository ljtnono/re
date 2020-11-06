package cn.ljtnono.re.security.component;

import cn.ljtnono.re.common.util.EncryptUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/7 20:36 下午
 * Description: 加密器
 */
@Component
public class PasswordEncoderImpl implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return EncryptUtil.getInstance().getMd5LowerCase((String) charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(EncryptUtil.getInstance().getMd5LowerCase((String) charSequence));
    }
}
