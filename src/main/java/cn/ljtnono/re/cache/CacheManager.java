package cn.ljtnono.re.cache;

/**
 * 缓存管理器
 * @author ljt
 * @date 2020/2/12
 * @version 1.0.1
 */
public interface CacheManager {

    /**
     * 刷新所有缓存
     * @return 刷新成功返回true，失败返回false
     */
    boolean flushAll();

    /**
     * 根据缓存的key删除缓存
     * @param key 缓存key
     * @return 删除成功返回true 删除失败返回false
     */
    boolean removeCache(String key);

    /**
     * 设置一个缓存
     * @param key 缓存的key
     * @param value 缓存的值
     */
    void setCache(String key, Object value);

    /**
     * 根据key值获取缓存对象
     * @param key 要获取的key值
     * @return 缓存不存在返回null, 存在返回该缓存对象
     */
    Object getCache(String key);

    /**
     * 更新缓存
     * @param key 缓存的key
     * @param value 缓存的值
     * @return 更新成功返回true, 失败返回false
     */
    boolean updateCache(String key, Object value);
}
