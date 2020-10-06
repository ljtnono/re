package cn.ljtnono.re.common.util.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ljt
 * Date: 2020/8/9 18:59
 * Description: redis工具类
 *
 */
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //*****************************String类型*****************************//

    /**
     * 设置string类型缓存
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.boundValueOps(key).set(value, timeout, unit);
    }

    /**
     * 获取string类型的值
     * @param key 键
     * @return Object
     */
    public Object get(String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    /**
     * 删除一个键
     * @param key 要删除的键
     * @author Ling, Jiatong
     *
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
