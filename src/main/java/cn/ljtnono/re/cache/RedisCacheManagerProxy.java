package cn.ljtnono.re.cache;

import cn.ljtnono.re.util.RedisUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * redis缓存管理代理类
 * @author ljt
 * @date 2020/2/1
 * @version 1.0.1
 */
@Data
@Component
public class RedisCacheManagerProxy implements CacheManagerProxy {

    private RedisUtil redisUtil;

    /** 具体调用的那个cacheManager */
    private CacheManager cacheManager;

    public RedisCacheManagerProxy(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public RedisCacheManagerProxy() {}

    @Autowired
    public RedisCacheManagerProxy(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
