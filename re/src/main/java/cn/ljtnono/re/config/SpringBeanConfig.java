package cn.ljtnono.re.config;

import cn.ljtnono.re.ftp.ReFtpClientPool;
import cn.ljtnono.re.ftp.ReFtpClientPooledObjectFactory;
import cn.ljtnono.re.security.ReJwtAuthenticationTokenFilter;
import cn.ljtnono.re.util.JJWTUtil;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 配置相关需要注入的类
 * @author ljt
 * @date 2020/2/21
 * @version 1.0.1
 */
@SpringBootConfiguration
public class SpringBeanConfig {


    /**
     * 配置ftp连接池对象
     * @return ftp连接池
     */
    @Bean
    @Lazy
    public ReFtpClientPool reFtpClientObjectPool() {
        return new ReFtpClientPool(new ReFtpClientPooledObjectFactory());
    }

    /**
     * 配置JWT过滤器
     * @return JWT过滤器
     */
    @Bean
    @Lazy
    public ReJwtAuthenticationTokenFilter reJwtAuthenticationTokenFilter() {
        return new ReJwtAuthenticationTokenFilter();
    }

    @Bean
    @Lazy
    public JJWTUtil jjwtUtil() {
        return JJWTUtil.getInstance();
    }

    /**
     * 配置mybatis-plus 分页插件
     * @return PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 配置redisTemplate
     * @param lettuceConnectionFactory lettuceConnection工厂
     * @return 配置成功返回可以使用RedisTemplate<String, Object> 类型
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
