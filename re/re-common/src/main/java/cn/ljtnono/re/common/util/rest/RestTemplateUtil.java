package cn.ljtnono.re.common.util.rest;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/18 22:09 下午
 * Description: http请求客户端
 * {@link RestTemplate(Class)}
 */
@Component
@ConfigurationProperties(prefix = "rest")
public class RestTemplateUtil {

    /** 连接超时时间 单位 秒*/
    private int connectTimeout;

    /** 读取超时时间 单位 秒 */
    private int readTimeout;

    private final RestTemplate template;

    public RestTemplateUtil() {
        template= new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(connectTimeout * 1000))
                .setReadTimeout(Duration.ofMillis(readTimeout * 1000))
                .requestFactory(SimpleClientHttpRequestFactory.class)
                .build();
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // json 消息转换
        template.getMessageConverters().add(messageConverter);
        // html消息转换，解决中文乱码问题
        template.getMessageConverters()
                .set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public SimpleClientHttpRequestFactory getRequestFactory() {
        return (SimpleClientHttpRequestFactory) template.getRequestFactory();
    }

    public void setConnectTimeout(int connectTimeout) {
        SimpleClientHttpRequestFactory requestFactory = getRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout * 1000);
//        template.setRequestFactory(requestFactory);
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        SimpleClientHttpRequestFactory requestFactory = getRequestFactory();
        requestFactory.setReadTimeout(readTimeout * 1000);
//        template.setRequestFactory(requestFactory);
        this.readTimeout = readTimeout;
    }

    public RestTemplate getTemplate() {
        return template;
    }

    @Data
    @ToString
    public static class Builder {

        /** 请求url */
        private String url = "";

        /** 请求参数 */
        private Map<String, Object> params = new HashMap<>();

        /** 请求头 */
        private Map<String, Object> headers = new HashMap<>();

        /** 请求方式, 默认get */
        private Method method = Method.GET;

        /** 是否开启 PathVariable形式 */
        private boolean enablePathVariable = false;

        /** 是否开启使用手机User-Agent发送请求 */
        private boolean enableMobile = false;

        public enum Method {
            GET, POST, PUT, DELETE, OPTION
        }

        private void initBuilder() {
            // 初始化默认请求头
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Connection", "keep-alive");
            if (enableMobile) {
                headers.put("User-Agent", UserAgentPool.randomMobileUserAgent());
            }
            headers.put("User-Agent", UserAgentPool.randomUserAgent());
            headers.put("Agent", "*/*");
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder enableMobile(boolean enableMobile) {
            this.enableMobile = enableMobile;
            return this;
        }

        public Builder enablePathVariable(boolean enablePathVariable) {
            this.enablePathVariable = enablePathVariable;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder params(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder headers(Map<String, Object> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String name, Object value) {
            headers.put(name, value);
            return this;
        }

        public Builder addParam(String name, Object value) {
            params.put(name, value);
            return this;
        }

        public Builder build() {
            initBuilder();
            if (url.isEmpty()) {
                throw new IllegalArgumentException("param url is empty");
            }
            // TODO 校验url格式是否正确
            // 为get请求拼接参数
            if (params.size() != 0 && !enablePathVariable) {
                StringBuilder b = new StringBuilder();
                params.forEach((k, v) -> b.append(k).append("=").append(v).append("&"));
                url += "?" + b.toString().substring(0, b.toString().lastIndexOf("&"));
            }
            return this;
        }
    }


    //============================ 通用方法 =============================//

    /**
     * 基本参数校验方法
     * @param builder 请求构建器
     * @param type 响应类型
     * @param <T> 响应类型泛型
     */
    private <T> void baseCheckParam(Builder builder, Class<T> type) {
        if (builder == null) {
            throw new NullPointerException("param builder is null");
        }
        if (builder.getUrl().isEmpty()) {
            throw new IllegalArgumentException("param url is empty");
        }
        if (type == null) {
            throw new NullPointerException("param type is null");
        }
    }


    /**
     * 通用执行方法
     * @param builder 请求构建器
     * @param type 响应类型
     * @param enableWrap 响应结果是否使用ResponseEntity包装
     * @param <T> 响应泛型
     * @return T type类型对象
     */
    public <T> Object execute(Builder builder, Class<T> type, boolean enableWrap) {
        baseCheckParam(builder, type);
        if (builder.getMethod().equals(Builder.Method.GET)) {
            if (enableWrap) {
                return getForEntity(builder, type);
            }
            return getForObject(builder, type);
        } else if (builder.getMethod().equals(Builder.Method.POST)) {
            if (enableWrap) {

            }
            return postForObject(builder, type);
        } else if (builder.getMethod().equals(Builder.Method.PUT)) {

        } else if (builder.getMethod().equals(Builder.Method.DELETE)) {

        } else if (builder.getMethod().equals(Builder.Method.OPTION)) {

        }
        return null;
    }




    //============================ GET =============================//

    /**
     * 发送get请求，并将结果格式化为type类型
     * @param builder 请求建造器
     * @param type 响应类型
     * @param <T> 响应泛型
     * @see RestTemplate#getForObject(String, Class, Object...)
     * @return T type类型对象
     */
    public <T> T getForObject(Builder builder, Class<T> type) {
        baseCheckParam(builder, type);
        if (builder.enablePathVariable) {
            return template.getForObject(builder.getUrl(), type, builder.getParams());
        }
        return template.getForObject(builder.getUrl(), type);
    }

    /**
     * 发送get请求，并将结果格式化为ResponseEntity<T>类型
     * @param builder 请求建造器
     * @param type 响应类型
     * @param <T> 响应泛型
     * @see RestTemplate#getForEntity(String, Class, Object...)
     * @see ResponseEntity
     * @return T ResponseEntity对象
     */
    public <T> ResponseEntity<T> getForEntity(Builder builder, Class<T> type) {
        baseCheckParam(builder, type);
        if (builder.enablePathVariable) {
            return template.getForEntity(builder.getUrl(), type, builder.getParams());
        }
        return template.getForEntity(builder.getUrl(), type);
    }


    //============================ POST =============================//

    /**
     * 发送Post请求，并将结果格式化为指定对象类型
     * @param builder 请求构造器
     * @param type 响应类型
     * @param <T> 响应泛型
     * @return T type类型对象
     */
    public <T> T postForObject(Builder builder, Class<T> type) {
        baseCheckParam(builder, type);
        // TODO 待完成
        if (builder.enablePathVariable) {
            return template.postForObject(builder.getUrl(), null, type, builder.getParams());
        }
        return template.postForObject(builder.getUrl(), builder.getParams(), type);
    }


    //============================ PUT =============================//




    //============================ DELETE =============================//
}
