package cn.ljtnono.re.ftp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * ftpClient服务器相关配置
 * @author ljt
 * @date 2019/11/2
 * @version 1.0.2
 */
@Data
@ToString
@NoArgsConstructor
public class ReFtpClientConfig implements Serializable {

    private static final long serialVersionUID = 7050205319517825883L;

    /** ftp服务器ip地址 */
    private String ftpServerAddr = "139.9.73.191";

    /** ftp服务器端口，默认21端口 */
    private int ftpServerPort = 21;

    /** ftp服务器用户名 */
    private String ftpServerUser = "ftpadmin";

    /** ftp服务器密码 */
    private String ftpServerPassword = "ljtLJT715336";

    /** ftp服务器基础目录 默认是/home/ftpadmin/re 目录 这里对于不同项目的文件精确分类*/
    private String ftpServerDirBase = "/home/ftpadmin/re";

    /** ftp连接超时时间 毫秒 默认5000毫秒*/
    private int connectTimeOut = 5000;

    /** ftp上传文件的编码 默认UTF-8*/
    private String controlEncoding = "UTF-8";

    /** 上传的缓冲区大小 10M*/
    private int bufferSize = 1024 * 1024 * 10;

    /** 上传以二进制文件的方式上传  */
    private int fileType = 2;

    /** 上传超时时间 */
    private int dataTimeout = 120000;

    /** 是否开启被动模式，默认开启 */
    private boolean passiveMode = true;

}
