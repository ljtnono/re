package cn.rootelement.ftp;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


/**
 * ftpClient连接池工厂
 * @author ljt
 * @date 2020/1/6
 * @version 1.0.2
 */
public class ReFtpClientPooledObjectFactory extends BasePooledObjectFactory<ReFtpClient> {

    @Override
    public void destroyObject(PooledObject<ReFtpClient> p) {
        p.getObject().destroy();
    }


    @Override
    public ReFtpClient create() throws Exception {
        // 创建ReFtpClient对象
        ReFtpClient reFtpClient = new ReFtpClient();
        // 初始化
        reFtpClient.init();
        return reFtpClient;
    }

    @Override
    public PooledObject<ReFtpClient> wrap(ReFtpClient obj) {
        // 包装ReFtpClient对象
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public boolean validateObject(PooledObject<ReFtpClient> p) {
        ReFtpClient reFtpClient = p.getObject();
        // 验证对象是否存活
        return reFtpClient.isActive();
    }

    @Override
    public void activateObject(PooledObject<ReFtpClient> p) throws Exception {
        // 重新初始化新的ftpClient对象
        p.getObject().connect();
    }

    @Override
    public void passivateObject(PooledObject<ReFtpClient> p) throws Exception {
        // 断开连接
        p.getObject().disConnect();
    }
}
