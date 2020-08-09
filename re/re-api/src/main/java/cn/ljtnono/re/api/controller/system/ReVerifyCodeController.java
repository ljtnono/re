package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.util.UUIDUtil;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author ljt
 * Date: 2020/8/9 18:56
 * Description: 验证码
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/system/verifyCode")
public class ReVerifyCodeController {

    private final Producer captchaProducer;

    public ReVerifyCodeController(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    /**
     * 获取验证码
     * @param response 响应对象
     */
    @GetMapping("/getVerifyCode")
    public void getVerifyCode(HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "create_date-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        String codeId = "verifyCode:" + UUIDUtil.generateUUID();
        response.addHeader("VerifyCodeId", codeId);
        BufferedImage bi = captchaProducer.createImage(capText);
        try {
            ImageIO.write(bi, "jpg", response.getOutputStream());
//            redisCache.setCacheObject(codeId, capText, 5, TimeUnit.MINUTES);
        } catch (IOException e) {
            log.error("[pte-system->VerifyCodeController] 生成验证码图片失败, 错误信息: {}", e.getMessage());
            throw new GlobalException(ReErrorEnum.SYSTEM_ERROR);
        }
    }
}
