package cn.ljtnono.re.util;

import cn.ljtnono.re.ftp.ReFtpClient;
import cn.ljtnono.re.ftp.ReFtpClientPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 文件服务器交互工具
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.1
 */
@Component
@Slf4j
public class FtpClientUtil {

    private final ReFtpClientPool reFtpClientPool;

    @Autowired
    public FtpClientUtil(ReFtpClientPool reFtpClientPool) {
        this.reFtpClientPool = reFtpClientPool;
    }

    /**
     * 文件上传方法
     *
     * @param filePath 上传的文件路径 例如 /images/abc.png  /abc.doc
     * @param fileName 存储在文件服务器中的文件名 例如 abc.png
     * @param input    上传的文件输入流
     * @throws Exception 上传失败抛出异常
     * @return 上传成功返回文件的访问路径，验证失败返回null
     * @see ReFtpClient#uploadFile(String, String, InputStream)
     */
    public String uploadFile(final String filePath, final String fileName, InputStream input) throws Exception {
        ReFtpClient reFtpClient = reFtpClientPool.borrowObject();
        log.info("reFtpClient:" + reFtpClient.hashCode());
        String s = reFtpClient.uploadFile(filePath, fileName, input);
        reFtpClientPool.returnObject(reFtpClient);
        return s;
    }


    public String uploadFile(final String filePath, final String fileName, MultipartFile multipartFile) {

        return null;
    }
}
