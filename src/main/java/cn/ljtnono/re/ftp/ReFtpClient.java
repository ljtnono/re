package cn.ljtnono.re.ftp;

import cn.ljtnono.re.enumeration.CommonFileTypeEnum;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 封装ftpClient的对象
 * 说明:
 * 1.ftpClient的基础路径是ftp服务器配置的基本路径，在使用{@link ReFtpClient#connect()} 之后会自动切换到{@link ReFtpClientConfig} ftpServerDirBase 目录下
 * 切换目录不存在时，会自动进行创建目录
 *
 * @author ljt
 * @version 1.0.3
 * @date 2020/1/21
 */
@Slf4j
@Data
@ToString
public class ReFtpClient {

    private ReFtpClientConfig reFtpClientConfig;

    private FTPClient ftpClient;

    public ReFtpClient(ReFtpClientConfig reFtpClientConfig) {
        this.reFtpClientConfig = reFtpClientConfig == null ? new ReFtpClientConfig() : reFtpClientConfig;
        this.ftpClient = new FTPClient();
    }

    public ReFtpClient() {
        this(null);
    }

    //################################### 生命周期方法 ###################################//

    /**
     * 初始化FTPClient, 设置各种参数
     * 必须执行此函数之后才能够进行其他操作
     */
    public void init() {
        ftpClient.setBufferSize(reFtpClientConfig.getBufferSize());
        // 设置编码格式为UTF8
        if (reFtpClientConfig.isPassiveMode()) {
            ftpClient.enterLocalPassiveMode();
        }
        ftpClient.setControlEncoding(reFtpClientConfig.getControlEncoding());
        ftpClient.setBufferSize(reFtpClientConfig.getBufferSize());
        ftpClient.setControlEncoding(reFtpClientConfig.getControlEncoding());
        ftpClient.setDataTimeout(reFtpClientConfig.getDataTimeout());
    }

    /**
     * 连接ftp服务器并且登陆
     * 如果没有进行初始化, 那么会自动进行初始化
     * @throws IOException 出现IOException 抛出
     */
    public void connect() throws IOException {
        log.info("FTPClient未初始化，进行初始化");
        if (ftpClient == null) {
            ftpClient = new FTPClient();
        }
        if (reFtpClientConfig == null) {
            reFtpClientConfig = new ReFtpClientConfig();
            init();
        }
        log.info("初始化完成");
        log.info("连接ftp服务器: " + reFtpClientConfig.getFtpServerAddr());
        // 连接FTP服务器
        ftpClient.connect(reFtpClientConfig.getFtpServerAddr(), reFtpClientConfig.getFtpServerPort());
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            log.info("连接ftp服务器失败, 原因: {}", ftpClient.getReplyString());
            return;
        }
        log.info("登陆ftp服务器, user = " + reFtpClientConfig.getFtpServerUser() + " password = " + reFtpClientConfig.getFtpServerPassword());
        ftpClient.login(reFtpClientConfig.getFtpServerUser(), reFtpClientConfig.getFtpServerPassword());
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            log.info("登陆ftp服务器失败, 原因: {}", ftpClient.getReplyString());
            ftpClient.disconnect();
            log.info("断开连接");
        } else {
            // 连接之后直接切换到工作目录
            log.info("连接ftp服务器成功");
            changeWorkingDirectory("");
        }
    }

    /**
     * 判断ftpClient是否是活跃的，这里判断的依据是该ftpClient是否连接到服务器
     *
     * @return 如果是，返回true，如果不是返回false
     */
    public boolean isActive() {
        return ftpClient != null && ftpClient.isConnected();
    }

    /**
     * 关闭ftp连接并且退出登陆
     * 先退出登陆后断开连接
     *
     * @throws IOException 关闭失败时抛出IO异常
     */
    public void disConnect() throws IOException {
        if (isActive()) {
            log.info("退出登陆, user = " + reFtpClientConfig.getFtpServerUser() + " password = " + reFtpClientConfig.getFtpServerPassword());
            ftpClient.logout();
            log.info("断开连接");
            ftpClient.disconnect();
        }
    }

    /**
     * 销毁这个对象
     */
    public void destroy() {
        if (ftpClient != null && reFtpClientConfig != null) {
            log.info("销毁ftpClient对象: {}", ftpClient.hashCode());
            ftpClient = null;
            reFtpClientConfig = null;
        }
    }

    //################################### 上传方法 ###################################//

    /**
     * 检查上传文件的参数的正确性
     *
     * @param filePath 上传文件的
     * @param fileName 文件名（存储在ftp服务器上的文件名）
     * @param input    文件的输入流
     * @throws IllegalArgumentException 文件格式错误时抛出该异常
     * @throws NullPointerException filePath、fileName、input为null时抛出
     */
    private void validateUploadFileParameters(String filePath, String fileName, final InputStream input) {
        Objects.requireNonNull(filePath, "filePath不能为null");
        Objects.requireNonNull(input, "fileName不能为null");
        Objects.requireNonNull(input, "input不能为null");
        String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
        boolean flag = false;
        for (CommonFileTypeEnum file : CommonFileTypeEnum.values()) {
            if (file.getValue().equalsIgnoreCase(fileExtName)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            log.error("不支持上传 {} 格式的文件", fileExtName);
            throw new IllegalArgumentException("文件格式错误");
        }
    }

    /**
     * 文件上传方法
     *
     * @param filePath 上传的文件路径 例如 /images/abc.png  /abc.doc
     * @param fileName 存储在文件服务器中的文件名 例如 abc.png
     * @param b        上传的文件的字节数组
     * @return 上传成功返回true，上传失败返回false
     */
    public boolean uploadFile(String filePath, final String fileName, final byte[] b) throws IOException {
        return uploadFile(filePath, fileName, new BufferedInputStream(new ByteArrayInputStream(b)));
    }

    /**
     * 文件上传方法
     *
     * @param multipartFile SpringMVC文件上传组件
     * @throws NullPointerException 当multipartFile输入流为null时抛出
     * @return 上传成功返回图片的url，上传失败返回null
     */
    public boolean uploadFile(MultipartFile multipartFile) throws IOException {
        Objects.requireNonNull(multipartFile);
        InputStream input = new BufferedInputStream(multipartFile.getInputStream());
        String filePath = "";
        String originalFilename = multipartFile.getOriginalFilename();
        Objects.requireNonNull(originalFilename);
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        for (CommonFileTypeEnum commonFileTypeEnum : CommonFileTypeEnum.values()) {
            if (extName.equalsIgnoreCase(commonFileTypeEnum.getValue())) {
                int type = commonFileTypeEnum.getType();
                switch (type) {
                    case 1 :
                        filePath = "images";
                        break;
                    case 2 :
                        filePath = "music";
                        break;
                    case 3 :
                        filePath = "documentation";
                        break;
                    case 4 :
                        filePath = "video";
                        break;
                    case 5 :
                        filePath = "pdf";
                        break;
                    case 6 :
                        filePath = "markdown";
                        break;
                    case 7 :
                        filePath = "code";
                        break;
                    default:
                        filePath = "other";
                }
            }
        }
        return uploadFile(filePath, originalFilename, input);
    }

    /**
     * 基础文件上传方法
     *
     * @param filePath 上传的文件路径 例如 images abc
     * @param fileName 存储在文件服务器中的文件名 例如 abc.png
     * @param input    上传的文件输入流
     * @return 上传成功返回true，上传失败返回false
     * @throws IOException 当出现IO异常时抛出
     */
    public boolean uploadFile(String filePath, final String fileName, final InputStream input) throws IOException {
        try {
            validateUploadFileParameters(filePath, fileName, input);
        } catch (Exception e) {
            log.error("上传文件: {} 失败, 原因: {}", fileName, e.getMessage());
            return false;
        }
        if (!isActive()) {
            connect();
        }
        filePath = "/" + filePath;
        //切换到上传目录 这里只允许传到re目录下
        changeWorkingDirectory(filePath);
        //设置二进制形式上传
        ftpClient.setFileType(reFtpClientConfig.getFileType());
        //上传文件
        if (reFtpClientConfig.isPassiveMode()) {
            ftpClient.enterLocalPassiveMode();
        }
        ftpClient.storeFile(fileName, input);
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            log.error("上传文件: {} 失败, 原因: {}", fileName, ftpClient.getReplyString());
            disConnect();
            return false;
        }
        disConnect();
        input.close();
        return true;
    }

    //################################### 下载方法 ###################################//

    /**
     * 检验下载函数的参数是否正确
     *
     * @param remotePath 要下载的文件路径 基础目录为reFtpClientConfig 中的ftpServerDirBase, 所以这里一般只需要填 /files 或者 /images
     * @param localPath  下载文件的本地路径
     * @param fileNames  下载的文件名数组
     * @throws NullPointerException remotePath、localPath、fileNames为null时抛出
     */
    private void validateDownloadFileParameters(String remotePath, String localPath, String[] fileNames) {
        Objects.requireNonNull(remotePath, "remotePath不能为null");
        Objects.requireNonNull(localPath, "localPath不能为null");
        Objects.requireNonNull(fileNames, "fileNames不能为null");
    }

    /**
     * 从远程ftp服务器下载文件到本地
     *
     * @param remotePath 要下载的文件路径 基础目录为reFtpClientConfig 中的ftpServerDirBase, 所以这里一般只需要填 /files 或者 /images
     * @param localPath  下载文件的本地路径
     * @param fileName  下载的文件名
     * @return 下载成功返回true, 失败返回false
     * @throws IOException IO异常
     */
    public Map<String, Boolean> downloadFile(String remotePath, String localPath, String fileName)
            throws IOException {
        return downloadFile(remotePath, localPath, new String[]{fileName});
    }


    /**
     * 从远程ftp服务器下载文件到本地
     *
     * @param remotePath 要下载的文件路径 基础目录为reFtpClientConfig 中的ftpServerDirBase, 所以这里一般只需要填 /files 或者 /images
     * @param localPath  下载文件的本地路径
     * @param fileNames  下载的文件名数组
     * @return 下载成功返回true, 失败返回false
     * @throws IOException IO异常
     */
    public Map<String, Boolean> downloadFile(String remotePath, String localPath, String[] fileNames) throws IOException {
        // 切换至文件所在目录
        try {
            validateDownloadFileParameters(remotePath, localPath, fileNames);
        } catch (Exception e) {
            log.error("下载失败, 原因: {}", e.getMessage());
            return Maps.newHashMap();
        }
        // 默认是linux下的分隔符
        remotePath = "/" + remotePath;
        localPath = localPath + "\\";
        if (!isActive()) {
            connect();
        }
        changeWorkingDirectory(remotePath);
        Map<String, Boolean> results = Maps.newHashMap();
        OutputStream out;
        if (fileNames.length == 0) {
            return Maps.newHashMap();
        } else if (fileNames.length == 1) {
            out = new BufferedOutputStream(new FileOutputStream(localPath + fileNames[0]));
            boolean b = ftpClient.retrieveFile(fileNames[0], out);
            log.info("文件 {} 下载{}", fileNames[0], b ? "成功" : "失败");
            results.put(fileNames[0], b ? Boolean.TRUE : Boolean.FALSE);
            out.close();
        } else {
            for (String fileName : fileNames) {
                out = new BufferedOutputStream(new FileOutputStream(localPath + fileName));
                boolean b = ftpClient.retrieveFile(fileName, out);
                log.info("文件 {} 下载{}", fileName, b ? "成功" : "失败");
                results.put(fileName, b ? Boolean.TRUE : Boolean.FALSE);
                out.close();
            }
        }
        return results;
    }

    //################################### 操作目录方法 ###################################//

    /**
     * 获取当前工作目录路径
     * @throws IOException IO异常
     * @throws IllegalArgumentException 未连接时抛出该异常
     * @return 当前工作目录路径
     */
    public String getCurrentDirectoryPath() throws IOException {
        if (!isActive()) {
            throw new IllegalArgumentException("ftp服务器没有连接, 请先连接再进行操作");
        }
        return ftpClient.printWorkingDirectory();
    }

    /**
     * 切换到上传文件的目录下，最终切换的路径为config的基础路径 + path, 如果目录不存在, 那么进行创建
     * 如果没有进行连接，会自动进行连接和初始化工作
     * @param path 切换的路径
     * @throws IOException IO异常
     */
    public void changeWorkingDirectory(final String path) throws IOException {
        if (!isActive()) {
            connect();
        }
        log.info("切换目录: {}", reFtpClientConfig.getFtpServerDirBase() + path);
        if (!ftpClient.changeWorkingDirectory(reFtpClientConfig.getFtpServerDirBase() + path)) {
            //如果目录不存在创建目录
            log.info("目录不存在，进行创建: {}", reFtpClientConfig.getFtpServerDirBase() + path);
            String[] dirs = path.split("/");
            String tempPath = reFtpClientConfig.getFtpServerDirBase();
            log.info("循环创建: {}", reFtpClientConfig.getFtpServerDirBase() + path);
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) {
                    continue;
                }
                tempPath += "/" + dir;
                if (!ftpClient.changeWorkingDirectory(tempPath)) {
                    if (!ftpClient.makeDirectory(tempPath)) {
                        log.info("循环创建: {}", reFtpClientConfig.getFtpServerDirBase() + path + "成功");
                        break;
                    } else {
                        ftpClient.changeWorkingDirectory(tempPath);
                    }
                }
            }
        }
    }

    /**
     * 切换去上一级目录
     *
     * @return 切换成功返回true, 切换失败返回false, 未连接返回false
     * @throws IOException IO异常
     * @throws IllegalArgumentException 未连接时抛出该异常
     */
    public boolean changeToParentDirectory() throws IOException {
        if (!isActive()) {
            throw new IllegalArgumentException("ftp服务器没有连接, 请先连接再进行操作");
        }
        return ftpClient.changeToParentDirectory();
    }

    /**
     * 在当前目录下创建一个目录
     *
     * @param dirName 目录名字
     * @return 创建成功返回true, 创建失败返回false
     * @throws IOException IO异常
     * @throws IllegalArgumentException 未连接时抛出该异常
     */
    public boolean makeDirectory(String dirName) throws IOException {
        if (!isActive()) {
            throw new IllegalArgumentException("ftp服务器没有连接, 请先连接再进行操作");
        }
        return ftpClient.makeDirectory(dirName);
    }

    //################################### 操作文件方法 ###################################//

    /**
     * 获取当前目录下一个文件的大小
     *
     * @param fileName 文件名
     * @return 文件的大小（字节）, 如果当前目录中没有该文件的话返回OL
     * @throws IOException IO异常
     * @throws IllegalArgumentException 未连接时抛出该异常
     */
    public long getFileSize(final String fileName) throws IOException {
        if (!isActive()) {
            throw new IllegalArgumentException("ftp服务器没有连接, 请先连接再进行操作");
        }
        long size = 0L;
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.getName().equalsIgnoreCase(fileName)) {
                size = ftpFile.getSize();
            }
        }
        return size;
    }

    /**
     * 重命名一个文件，必须是当前工作目录下已经存在的文件
     *
     * @param from 文件的原名
     * @param to   文件的新名字
     * @return 成功返回true, 失败返回false, 文件不存在也返回false
     * @throws IOException IO异常
     * @throws IllegalArgumentException 未连接时抛出该异常
     */
    public boolean renameCurrentFile(final String from, final String to) throws IOException {
        if (!isActive()) {
            throw new IllegalArgumentException("ftp服务器没有连接, 请先连接再进行操作");
        }
        return ftpClient.rename(from, to);
    }

    /**
     * 获取当前目录下所有文件的名字列表
     *
     * @return 当前目录下所有文件名字列表
     * @throws IOException IO异常
     * @throws IllegalArgumentException 未连接时抛出该异常
     */
    public List<String> listCurrentDirectoryFileNames() throws IOException {
        if (!isActive()) {
            throw new IllegalArgumentException("ftp服务器没有连接, 请先连接再进行操作");
        }
        List<String> fileList = new ArrayList<>();
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
            FTPFile ftpFile = ftpFiles[i];
            if (ftpFile.isFile()) {
                fileList.add(ftpFile.getName());
            }
        }
        return fileList;
    }

    /**
     * 获取当前文件夹下所有文件的FTPFile对象
     *
     * @return list 文件的FTPFile对象集合
     * @throws IOException IO异常
     * @throws IllegalArgumentException 未连接时抛出该异常
     */
    public List<FTPFile> listCurrentDirectoryFiles() throws IOException {
        if (!isActive()) {
            throw new IllegalArgumentException("ftp服务器没有连接, 请先连接再进行操作");
        }

        ftpClient.enterLocalPassiveMode();
        FTPFile[] ftpFiles = ftpClient.listFiles();
        if (ftpFiles != null) {
            return Arrays.asList(ftpFiles);
        } else {
            return Collections.emptyList();
        }
    }
}
