package cn.ljtnono.re.common.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 18:59
 * Description: redis工具类
 *
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //*****************************String类型*****************************//

    /**
     * 设置string类型缓存
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取string类型的值
     * @param key 键
     * @return Object
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除一个键
     * @param key 要删除的键
     * @author Ling, Jiatong
     *
     */
    public void delete(final String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据正则表达式删除redis键
     * @param pattern 正则表达式
     */
    public void deleteByPattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
