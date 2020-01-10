package cn.ljtnono.re.ftp;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import java.io.Serializable;

/**
 * ftpClient服务器相关配置
 * @author ljt
 * @date 2019/11/2
 * @version 1.0.2
 */
public class ReFtpClientConfig extends GenericObjectPoolConfig<ReFtpClient> implements Serializable {

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

    public ReFtpClientConfig() {}

    private ReFtpClientConfig(Builder builder) {
        setFtpServerAddr(builder.ftpServerAddr);
        setFtpServerPort(builder.ftpServerPort);
        setFtpServerUser(builder.ftpServerUser);
        setFtpServerPassword(builder.ftpServerPassword);
        setFtpServerDirBase(builder.ftpServerDirBase);
        setConnectTimeOut(builder.connectTimeOut);
        setControlEncoding(builder.controlEncoding);
        setBufferSize(builder.bufferSize);
        setFileType(builder.fileType);
        setDataTimeout(builder.dataTimeout);
        setPassiveMode(builder.passiveMode);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(ReFtpClientConfig copy) {
        Builder builder = new Builder();
        builder.ftpServerAddr = copy.getFtpServerAddr();
        builder.ftpServerPort = copy.getFtpServerPort();
        builder.ftpServerUser = copy.getFtpServerUser();
        builder.ftpServerPassword = copy.getFtpServerPassword();
        builder.ftpServerDirBase = copy.getFtpServerDirBase();
        builder.connectTimeOut = copy.getConnectTimeOut();
        builder.controlEncoding = copy.getControlEncoding();
        builder.bufferSize = copy.getBufferSize();
        builder.fileType = copy.getFileType();
        builder.dataTimeout = copy.getDataTimeout();
        builder.passiveMode = copy.getPassiveMode();
        return builder;
    }

    public String getFtpServerAddr() {
        return ftpServerAddr;
    }

    public void setFtpServerAddr(String ftpServerAddr) {
        this.ftpServerAddr = ftpServerAddr;
    }

    public int getFtpServerPort() {
        return ftpServerPort;
    }

    public void setFtpServerPort(int ftpServerPort) {
        this.ftpServerPort = ftpServerPort;
    }

    public String getFtpServerUser() {
        return ftpServerUser;
    }

    public void setFtpServerUser(String ftpServerUser) {
        this.ftpServerUser = ftpServerUser;
    }

    public String getFtpServerPassword() {
        return ftpServerPassword;
    }

    public void setFtpServerPassword(String ftpServerPassword) {
        this.ftpServerPassword = ftpServerPassword;
    }

    public String getFtpServerDirBase() {
        return ftpServerDirBase;
    }

    public void setFtpServerDirBase(String ftpServerDirBase) {
        this.ftpServerDirBase = ftpServerDirBase;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public String getControlEncoding() {
        return controlEncoding;
    }

    public void setControlEncoding(String controlEncoding) {
        this.controlEncoding = controlEncoding;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getDataTimeout() {
        return dataTimeout;
    }

    public void setDataTimeout(int dataTimeout) {
        this.dataTimeout = dataTimeout;
    }

    public boolean getPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }


    public static final class Builder {
        private String ftpServerAddr;
        private int ftpServerPort;
        private String ftpServerUser;
        private String ftpServerPassword;
        private String ftpServerDirBase;
        private int connectTimeOut;
        private String controlEncoding;
        private int bufferSize;
        private int fileType;
        private int dataTimeout;
        private boolean passiveMode;

        private Builder() {
        }

        public Builder ftpServerAddr(String val) {
            ftpServerAddr = val;
            return this;
        }

        public Builder ftpServerPort(int val) {
            ftpServerPort = val;
            return this;
        }

        public Builder ftpServerUser(String val) {
            ftpServerUser = val;
            return this;
        }

        public Builder ftpServerPassword(String val) {
            ftpServerPassword = val;
            return this;
        }

        public Builder ftpServerDirBase(String val) {
            ftpServerDirBase = val;
            return this;
        }

        public Builder connectTimeOut(int val) {
            connectTimeOut = val;
            return this;
        }

        public Builder controlEncoding(String val) {
            controlEncoding = val;
            return this;
        }

        public Builder bufferSize(int val) {
            bufferSize = val;
            return this;
        }

        public Builder fileType(int val) {
            fileType = val;
            return this;
        }

        public Builder dataTimeout(int val) {
            dataTimeout = val;
            return this;
        }

        public Builder passiveMode(boolean val) {
            passiveMode = val;
            return this;
        }

        public ReFtpClientConfig build() {
            return new ReFtpClientConfig(this);
        }
    }
}
