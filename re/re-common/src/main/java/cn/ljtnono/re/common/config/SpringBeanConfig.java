package cn.ljtnono.re.common.config;

import cn.ljtnono.re.entity.system.User;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.MySqlDialect;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 18:25
 * Description: bean配置类
 */
@Configuration
public class SpringBeanConfig {

    /**
     * mybatis-plus 分页拦截器
     *
     * @return PaginationInterceptor 分页拦截器
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDbType(DbType.MYSQL);
        paginationInterceptor.setDialect(new MySqlDialect());
        return paginationInterceptor;
    }

    /**
     * 验证码配置
     *
     * @return DefaultKaptcha
     */
    @Bean
    public DefaultKaptcha defaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // kaptcha.border	图片边框，合法值：yes , no	yes
        // kaptcha.border.color	边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.	black
        // kaptcha.image.width	图片宽	200
        // kaptcha.image.height	图片高	50
        // kaptcha.producer.impl	图片实现类	com.google.code.kaptcha.impl.DefaultKaptcha
        // kaptcha.textproducer.impl	文本实现类	com.google.code.kaptcha.text.impl.DefaultTextCreator
        // kaptcha.textproducer.char.string	文本集合，验证码值从此集合中获取	abcde2345678gfynmnpwx
        // kaptcha.textproducer.char.length	验证码长度	5
        // kaptcha.textproducer.font.names	字体	Arial, Courier
        // kaptcha.textproducer.font.size	字体大小	40px.
        // kaptcha.textproducer.font.color	字体颜色，合法值： r,g,b 或者 white,black,blue.	black
        // kaptcha.textproducer.char.space	文字间隔	2
        // kaptcha.noise.impl	干扰实现类	com.google.code.kaptcha.impl.DefaultNoise
        // kaptcha.noise.color	干扰 颜色，合法值： r,g,b 或者 white,black,blue.	black
        // kaptcha.obscurificator.impl	图片样式：<br />水纹 com.google.code.kaptcha.impl.WaterRipple <br /> 鱼眼 com.google.code.kaptcha.impl.FishEyeGimpy <br /> 阴影 com.google.code.kaptcha.impl.ShadowGimpy	com.google.code.kaptcha.impl.WaterRipple
        // kaptcha.background.impl	背景实现类	com.google.code.kaptcha.impl.DefaultBackground
        // kaptcha.background.clear.from	背景颜色渐变，开始颜色	light grey
        // kaptcha.background.clear.to	背景颜色渐变， 结束颜色	white
        // kaptcha.word.impl	文字渲染器	com.google.code.kaptcha.text.impl.DefaultWordRenderer
        // kaptcha.session.key	session key	KAPTCHA_SESSION_KEY
        // kaptcha.session.date	session date	KAPTCHA_SESSION_DATE
        properties.setProperty(Constants.KAPTCHA_BORDER, "yes");
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "220,220,220");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "38,29,12");
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "90");
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "34");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "25");
        properties.setProperty(Constants.KAPTCHA_SESSION_KEY, "code");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial");
        properties.setProperty(Constants.KAPTCHA_NOISE_COLOR, "164,128,55");
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    /**
     * redisTemplate配置
     *
     * @return RedisTemplate<String, Object>
     */
    @Bean
    @Lazy
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置redis连接工厂
        redisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        RedisSerializer<String> serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        // 开启redis事务会导致连接不释放
//        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    /**
     * 编程用线程池
     *
     * @return ExecutorService
     * @author Ling, Jiatong
     */
    @Bean
    @Lazy
    public ExecutorService commonThreadPool() {
        Runtime runtime = Runtime.getRuntime();
        int processors = runtime.availableProcessors();
        int coreSize = processors + 1;
        int maxSize = coreSize * 2;
        int queueSize = 10000;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(queueSize);
        ThreadPoolExecutor.CallerRunsPolicy policy = new ThreadPoolExecutor.CallerRunsPolicy();
        return new ThreadPoolExecutor(coreSize, maxSize, 1000L, TimeUnit.DAYS, queue, Executors.defaultThreadFactory(), policy);
    }

    @Bean(name = "websocketOnlineMap")
    public ConcurrentHashMap<String, User> webSocketOnlineMap() {
        // 在线列表，key为用户名，User为用户对象
        return new ConcurrentHashMap<>(10);
    }
}
