package cn.ljtnono.re.ftp;

import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * 包装连接池对象
 * @author ljt
 * @date 2019/11/27
 * @version 1.0.2
 */
public class ReFtpClientPool {

    private GenericObjectPool<ReFtpClient> genericObjectPool;

    private ReFtpClientPooledObjectFactory reFtpClientPooledObjectFactory;

    public ReFtpClientPool(ReFtpClientPooledObjectFactory reFtpClientPooledObjectFactory) {
        this.reFtpClientPooledObjectFactory = reFtpClientPooledObjectFactory;
        genericObjectPool = new GenericObjectPool<>(reFtpClientPooledObjectFactory, new ReFtpClientConfig());
    }

    public GenericObjectPool<ReFtpClient> getGenericObjectPool() {
        return genericObjectPool;
    }

    public void setGenericObjectPool(GenericObjectPool<ReFtpClient> genericObjectPool) {
        this.genericObjectPool = genericObjectPool;
    }

    public ReFtpClientPooledObjectFactory getReFtpClientPooledObjectFactory() {
        return reFtpClientPooledObjectFactory;
    }

    public void setReFtpClientPooledObjectFactory(ReFtpClientPooledObjectFactory reFtpClientPooledObjectFactory) {
        this.reFtpClientPooledObjectFactory = reFtpClientPooledObjectFactory;
    }

    public ReFtpClient borrowObject() throws Exception {
        return genericObjectPool.borrowObject();
    }

    public void returnObject(ReFtpClient reFtpClient) {
        if (reFtpClient != null) {
            genericObjectPool.returnObject(reFtpClient);
        }
    }
}
