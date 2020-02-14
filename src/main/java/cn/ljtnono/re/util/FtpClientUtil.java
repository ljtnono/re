package cn.ljtnono.re.util;

import cn.ljtnono.re.ftp.ReFtpClient;
import cn.ljtnono.re.ftp.ReFtpClientPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * ftp交互工具类
 * @author ljt
 * @date 2020/1/21
 * @see ReFtpClient
 * @see ReFtpClientPool
 * @version 1.0.2
 */
@Component
@Slf4j
public class FtpClientUtil {

    private ReFtpClientPool reFtpClientPool;

    @Autowired
    public FtpClientUtil(ReFtpClientPool reFtpClientPool) {
        this.reFtpClientPool = reFtpClientPool;
    }

    //################################### 上传方法 ###################################//

    /**
     * 文件上传方法
     *
     * @param filePath 上传的文件路径 例如 /images/abc.png  /abc.doc
     * @param fileName 存储在文件服务器中的文件名 例如 abc.png
     * @param b        上传的文件的字节数组
     * @return 上传成功返回true，上传失败返回false
     * @see ReFtpClient#uploadFile(String, String, byte[])
     */
    public boolean uploadFile(String filePath, final String fileName, final byte[] b) throws Exception {
        ReFtpClient reFtpClient = reFtpClientPool.borrowObject();
        boolean result = reFtpClient.uploadFile(filePath, fileName, new BufferedInputStream(new ByteArrayInputStream(b)));
        reFtpClientPool.returnObject(reFtpClient);
        return result;
    }


    /**
     * 文件上传方法
     *
     * @param multipartFile SpringMVC文件上传组件
     * @throws NullPointerException 当multipartFile输入流为null时抛出
     * @return 上传成功返回图片的url，上传失败返回null
     * @see ReFtpClient#uploadFile(MultipartFile)
     */
    public boolean uploadFile(MultipartFile multipartFile) throws Exception {
        ReFtpClient reFtpClient = reFtpClientPool.borrowObject();
        boolean result = reFtpClient.uploadFile(multipartFile);
        reFtpClientPool.returnObject(reFtpClient);
        return result;
    }

    /**
     * 基础文件上传方法
     *
     * @param filePath 上传的文件路径 例如 images abc
     * @param fileName 存储在文件服务器中的文件名 例如 abc.png
     * @param input    上传的文件输入流
     * @return 上传成功返回true，上传失败返回false
     * @throws IOException 当出现IO异常时抛出
     * @see ReFtpClient#uploadFile(String, String, InputStream)
     */
    public boolean uploadFile(String filePath, final String fileName, final InputStream input) throws Exception {
        ReFtpClient reFtpClient = reFtpClientPool.borrowObject();
        boolean result = reFtpClient.uploadFile(filePath, fileName, input);
        reFtpClientPool.returnObject(reFtpClient);
        return result;
    }


    //################################### 下载方法 ###################################//

    /**
     * 从远程ftp服务器下载文件到本地
     *
     * @param remotePath 要下载的文件路径 基础目录为reFtpClientConfig 中的ftpServerDirBase, 所以这里一般只需要填 /files 或者 /images
     * @param localPath  下载文件的本地路径
     * @param fileName  下载的文件名
     * @return 下载成功返回true, 失败返回false
     * @throws IOException IO异常
     * @see ReFtpClient#downloadFile(String, String, String)
     */
    public Map<String, Boolean> downloadFile(String remotePath, String localPath, String fileName)
            throws Exception {
        ReFtpClient reFtpClient = reFtpClientPool.borrowObject();
        Map<String, Boolean> result = reFtpClient.downloadFile(remotePath, localPath, new String[]{fileName});
        reFtpClientPool.returnObject(reFtpClient);
        return result;
    }


    /**
     * 从远程ftp服务器下载文件到本地
     *
     * @param remotePath 要下载的文件路径 基础目录为reFtpClientConfig 中的ftpServerDirBase, 所以这里一般只需要填 /files 或者 /images
     * @param localPath  下载文件的本地路径
     * @param fileNames  下载的文件名数组
     * @return 下载成功返回true, 失败返回false
     * @throws IOException IO异常
     * @see ReFtpClient#downloadFile(String, String, String[])
     */
    public Map<String, Boolean> downloadFile(String remotePath, String localPath, String[] fileNames) throws Exception {
        ReFtpClient reFtpClient = reFtpClientPool.borrowObject();
        Map<String, Boolean> result = reFtpClient.downloadFile(remotePath, localPath, fileNames);
        reFtpClientPool.returnObject(reFtpClient);
        return result;
    }

}
