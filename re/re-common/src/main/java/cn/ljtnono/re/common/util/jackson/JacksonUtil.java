package cn.ljtnono.re.common.util.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Jackson工具类
 *
 * @author Ling, Jiatong
 * Date: 2020/8/2 0:53
 */
@Component
public class JacksonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    /**
     * 对象转string
     *
     * @param object 对象
     * @return string
     */
    public static <T> String objectToString(T object) {
        if (object == null) {
            return null;
        }
        try {
            return object instanceof String ? (String) object : objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转对象
     *
     * @param string string
     * @param clazz  对象
     * @return 对象
     */
    public static <T> T stringToObject(String string, Class<T> clazz) {
        if (StringUtils.isEmpty(string) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) string : objectMapper.readValue(string, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转List集合对象
     *
     * @param string        string
     * @param typeReference List集合类型
     * @return List集合对象
     */
    public static <T> T stringToObject(String string, TypeReference typeReference) {
        if (StringUtils.isEmpty(string) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? string : objectMapper.readValue(string, typeReference));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转List集合对象
     *
     * @param string          string
     * @param collectionClass List集合类型
     * @param elementClasses  集合元素类型
     * @return List集合对象
     */
    public static <T> T stringToObject(String string, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(string, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
