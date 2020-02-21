package cn.ljtnono.re.service.common;

import java.util.List;

/**
 * 统一处理Cache的接口
 * @author ljt
 * @date 2020/2/20
 * @version 1.0.1
 * @param <T>
 */
public interface IReEntityCacheManager <T> {

    default void setCache(T value) {
        // 子类复写
    }

    default void setCache(List<T> cache) {
        // 子类复写
    }

    default void lSetCache(String key, Object value) {
        // 子类复写
    }

    default void deleteCache(String key) {
        // 子类复写
    }

    default void deleteCacheByPattern(String pattern) {
        // 子类复写
    }

    default void deleteCacheByPattern(String[] patterns) {
        // 子类复写
    }

    default void deleteCacheAll() {
        // 子类复写
    }
}
