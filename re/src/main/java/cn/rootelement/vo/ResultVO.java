package cn.rootelement.vo;

import cn.rootelement.util.StringUtil;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果VO
 * @author ljt
 * @date 2020/6/27
 * @version 1.0.1
 */
@Data
@ToString(callSuper = true)
@Slf4j
public class ResultVO implements Serializable {

    private static final long serialVersionUID = -4080027627012913741L;

    /**
     * 所有字段的集合
     *
     */
    private Map<String, Object> fields;

    /**
     * 添加一个新字段
     * @param key 字段名
     * @param value 字段的值
     * @return 返回当前ResultVO对象
     */
    public ResultVO addField(String key, Object value) {
        if (!StringUtil.isEmpty(key)) {
            if (this.fields == null) {
                this.fields = new HashMap<>(5);
            }
            this.fields.put(key, value);
        }
        return this;
    }

    public static class Builder {
        private Map<String, Object> fields;

        public Builder() {
            this.fields = new HashMap<>(5);
        }

    }

}
