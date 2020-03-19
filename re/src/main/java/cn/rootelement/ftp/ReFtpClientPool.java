package cn.rootelement.ftp;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 包装连接池对象
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.3
 */
public class ReFtpClientPool extends GenericObjectPool<ReFtpClient> {

    public ReFtpClientPool(PooledObjectFactory<ReFtpClient> factory) {
        super(factory);
    }

    public ReFtpClientPool(PooledObjectFactory<ReFtpClient> factory, GenericObjectPoolConfig<ReFtpClient> config) {
        super(factory, config);
    }

    public ReFtpClientPool(PooledObjectFactory<ReFtpClient> factory, GenericObjectPoolConfig<ReFtpClient> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
