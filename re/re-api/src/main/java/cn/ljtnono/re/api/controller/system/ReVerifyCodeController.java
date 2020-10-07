package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.system.SystemException;
import cn.ljtnono.re.common.util.UUIDUtil;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 18:56
 * Description: 验证码Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/verifyCode")
public class ReVerifyCodeController {

    @Autowired
    private Producer captchaProducer;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取验证码
     * @param response 响应对象
     */
    @GetMapping
    public void getVerifyCode(HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "create_date-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        String codeId = "verifyCodeId:" + UUIDUtil.generateUUID();
        response.addCookie(new Cookie("VerifyCodeId", codeId));
        BufferedImage bi = captchaProducer.createImage(capText);
        try {
            redisUtil.set(codeId, capText, 5, TimeUnit.MINUTES);
            ImageIO.write(bi, "jpg", response.getOutputStream());
        } catch (IOException e) {
            log.error("[re-system -> ReVerifyCodeController -> getVerifyCode()] 生成验证码图片失败, 错误信息: {}", e.getMessage());
            throw new SystemException(ReErrorEnum.SYSTEM_ERROR);
        }
    }
}
