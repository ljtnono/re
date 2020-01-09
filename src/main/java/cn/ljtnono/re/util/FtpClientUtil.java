package cn.ljtnono.re.util;

import cn.ljtnono.re.ftp.ReFtpClient;
import cn.ljtnono.re.ftp.ReFtpClientPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * 单例模式FtpClientUtil，文件服务器交互工具
 * @author ljt
 * @date 2019/10/15
 * @version 1.0
 */
@Component
public class FtpClientUtil {

    @Autowired
    private ReFtpClientPool reFtpClientPool;

    /** 实例 */
    private volatile static FtpClientUtil instance = null;

    /** 单例模式 */
    private FtpClientUtil(){}

    /**
     * 单例模式获取实例
     * @return 返回工具类实例
     */
    public static FtpClientUtil getInstance() {
        if (instance == null) {
            synchronized (FtpClientUtil.class) {
                if (instance == null) {
                    instance =  new FtpClientUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 文件上传方法
     *
     * @param filePath 上传的文件路径 例如 /images/abc.png  /abc.doc
     * @param fileName 存储在文件服务器中的文件名 例如 abc.png
     * @param input    上传的文件输入流
     * @return 上传成功返回true，上传失败返回false
     * @see ReFtpClient#uploadFile(String, String, InputStream)
     */
    public String uploadFile(String filePath, String fileName, InputStream input) throws Exception {
        ReFtpClient reFtpClient = reFtpClientPool.borrowObject();
        String s = reFtpClient.uploadFile("/files", "demo.txt", new FileInputStream("C:\\Users\\ljt\\Desktop\\demo.txt"));
        reFtpClientPool.returnObject(reFtpClient);
        return s;
    }

}
