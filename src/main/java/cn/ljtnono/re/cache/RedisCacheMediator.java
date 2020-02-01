package cn.ljtnono.re.cache;

import cn.ljtnono.re.util.RedisUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * redis缓存中介者
 * @author ljt
 * @date 2020/2/1
 * @version 1.0.1
 */
@Data
@Component
public class RedisCacheMediator implements CacheMediator{

    private RedisUtil redisUtil;

    @Autowired
    public RedisCacheMediator(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean flushAll() {
        return false;
    }
}
