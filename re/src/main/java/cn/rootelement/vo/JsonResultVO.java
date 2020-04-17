package cn.rootelement.vo;

import cn.rootelement.util.StringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** 请求JSON数据的模板
 *  @author ljt
 *  @date 2019/12/19
 *  @version 1.0.2
*/
public class JsonResultVO implements Serializable {

    /** 持久化UID */
    private static final long serialVersionUID = 8098847541612269530L;

    /** 请求条数 */
    private Integer totalCount;

    /** 请求状态 success 代表成功 fail 代表失败 */
    private String request;

    /** 请求状态码 */
    private Integer status;

    /** 结果信息 */
    private String message;

    /** 数据集合 */
    private Collection<?> data;

    /** 自定义字段 */
    private Map<String, Object> fields;

    /** 无参构造函数方便new对象 */
    public JsonResultVO() {}

    private JsonResultVO(Builder builder) {
        setTotalCount(builder.totalCount);
        setRequest(builder.request);
        setStatus(builder.status);
        setMessage(builder.message);
        setData(builder.data);
        setFields(builder.fields);
    }

    /**
     * 为JsonResult对象添加一个新字段
     * @param key 字段名
     * @param value 字段的值
     * @return 返回当前JsonResult对象
     */
    public JsonResultVO addField(String key, Object value) {
        if (!StringUtil.isEmpty(key)) {
            if (this.fields == null) {
                this.fields = new HashMap<>(5);
            }
            this.fields.put(key, value);
        }
        return this;
    }

    /**
     * 通用的success类型
     * @param data 数据集合
     * @param totalCount 数据的条数
     * @return 返回通用的成功JsonResult模板
     */
    public static JsonResultVO success(Collection<?> data, Integer totalCount) {
        JsonResultVO success = new JsonResultVO();
        success.setRequest("success");
        success.setStatus(200);
        success.setData(data);
        if (data.size() == 0) {
            success.setMessage("没有数据！");
        } else {
            success.setMessage("请求成功！");
        }
        success.setFields(null);
        success.setTotalCount(totalCount);
        return success;
    }

    /**
     * 通用的fail类型
     * @param status 请求返回的状态响应码
     * @return 返回通用的失败JsonResult模板
     */
    public static JsonResultVO fail(Integer status) {
        JsonResultVO fail = new JsonResultVO();
        fail.setRequest("fail");
        fail.setStatus(status);
        fail.setMessage("请求失败！");
        fail.setData(null);
        fail.setTotalCount(null);
        fail.setFields(null);
        return fail;
    }

    /**
     * 通用success 类型 ，不带数据，只是提供提示信息
     * @param message 提示信息
     * @param status 响应状态码
     * @return 返回通用的成功消息JsonResult模板
     */
    public static JsonResultVO forMessage(String message, Integer status) {
        JsonResultVO success = new JsonResultVO();
        success.setRequest("success");
        success.setStatus(status);
        success.setMessage(message);
        success.setData(null);
        success.setTotalCount(null);
        success.setFields(null);
        return success;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(JsonResultVO copy) {
        Builder builder = new Builder();
        builder.totalCount = copy.getTotalCount();
        builder.request = copy.getRequest();
        builder.status = copy.getStatus();
        builder.message = copy.getMessage();
        builder.data = copy.getData();
        builder.fields = copy.getFields();
        return builder;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Collection<?> getData() {
        return data;
    }

    public void setData(Collection<?> data) {
        this.data = data;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }


    public static final class Builder {
        private Integer totalCount;
        private String request;
        private Integer status;
        private String message;
        private Collection<?> data;
        private Map<String, Object> fields;

        private Builder() {
        }

        public Builder totalCount(Integer val) {
            totalCount = val;
            return this;
        }

        public Builder request(String val) {
            request = val;
            return this;
        }

        public Builder status(Integer val) {
            status = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public Builder data(Collection<?> val) {
            data = val;
            return this;
        }

        public Builder fields(Map<String, Object> val) {
            fields = val;
            return this;
        }

        public JsonResultVO build() {
            return new JsonResultVO(this);
        }
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "totalCount=" + totalCount +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", fields=" + fields +
                '}';
    }
}
