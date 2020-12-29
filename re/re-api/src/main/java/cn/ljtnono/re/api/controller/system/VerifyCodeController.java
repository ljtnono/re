package cn.ljtnono.re.api.controller.system;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.system.SystemException;
import cn.ljtnono.re.common.util.SpringBeanUtil;
import cn.ljtnono.re.common.util.UUIDUtil;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码模块接口
 *
 * @author Ling, Jiatong
 * Date: 2020/8/9 18:56
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/verifyCode")
@Api(value = "/api/v1/system/verifyCode", tags = "验证码模块接口")
public class VerifyCodeController {

    private final Producer captchaProducer;
    private final RedisUtil redisUtil;
    public VerifyCodeController(Producer captchaProducer, RedisUtil redisUtil) {
        this.captchaProducer = captchaProducer;
        this.redisUtil = redisUtil;
    }

    /**
     * 获取验证码
     */
    @GetMapping
    @ApiOperation("获取验证码图片")
    public void getVerifyCode() {
        HttpServletResponse response = SpringBeanUtil.getCurrentRes();
        log.info("[re-system -> VerifyCodeController -> getVerifyCode()] 获取验证码");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        String codeId = "verifyCodeId:" + UUIDUtil.generateUUID();
        response.addHeader("VerifyCodeId", codeId);
        BufferedImage bi = captchaProducer.createImage(capText);
        try {
            redisUtil.set(codeId, capText, 5, TimeUnit.MINUTES);
            ImageIO.write(bi, "jpg", response.getOutputStream());
        } catch (IOException e) {
            log.error("[re-system -> VerifyCodeController -> getVerifyCode()] 生成验证码图片失败, 错误信息: {}", e.getMessage());
            throw new SystemException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }
}
