package cn.ljtnono.re.cache;

/**
 * 缓存中介者
 * @author ljt
 * @date 2020/1/31
 * @version 1.0.1
 */
public interface CacheMediator {

    /**
     * 刷新所有缓存
     * @return 刷新成功返回true，失败返回false
     */
    boolean flushAll();

}
