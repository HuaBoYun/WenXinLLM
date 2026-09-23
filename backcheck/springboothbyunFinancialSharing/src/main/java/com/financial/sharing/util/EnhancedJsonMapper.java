package com.financial.sharing.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.hbfk.util.JsonBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 增强版JSON映射器
 * 专门解决LocalDateTime序列化问题，兼容JsonBean
 * 针对FastJSON 1.2.60版本的增强处理
 */
public class EnhancedJsonMapper {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 自定义LocalDateTime序列化器
     */
    public static class LocalDateTimeSerializer implements com.alibaba.fastjson.serializer.ObjectSerializer {
        @Override
        public void write(com.alibaba.fastjson.serializer.JSONSerializer serializer, Object object,
                         Object fieldName, java.lang.reflect.Type fieldType, int features) throws java.io.IOException {
            if (object == null) {
                serializer.writeNull();
                return;
            }
            LocalDateTime localDateTime = (LocalDateTime) object;
            serializer.write(localDateTime.format(DATETIME_FORMATTER));
        }
    }

    // 静态初始化序列化配置
    private static final SerializeConfig SERIALIZE_CONFIG = new SerializeConfig();

    static {
        // 配置Long类型序列化为字符串，解决JavaScript精度丢失问题
        SERIALIZE_CONFIG.put(Long.class, ToStringSerializer.instance);
        SERIALIZE_CONFIG.put(Long.TYPE, ToStringSerializer.instance);

        // 配置LocalDateTime序列化
        SERIALIZE_CONFIG.put(LocalDateTime.class, new LocalDateTimeSerializer());
    }

    /**
     * 将对象序列化为JSON字符串，支持LocalDateTime
     * @param object 要序列化的对象
     * @return JSON字符串
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }

        // 如果是JsonBean对象，使用特殊处理
        if (object instanceof JsonBean) {
            JsonBean jsonBean = (JsonBean) object;
            // 确保data不为null
            if (jsonBean.getData() == null) {
                jsonBean.setData(new Object());
            }
            return JSON.toJSONString(jsonBean, SERIALIZE_CONFIG,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat
            );
        }

        // 普通对象序列化
        return JSON.toJSONString(object, SERIALIZE_CONFIG,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.WriteDateUseDateFormat
        );
    }

    /**
     * 创建成功响应的JsonBean
     * @param data 返回数据
     * @return JSON字符串
     */
    public static String success(Object data) {
        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(1);
        jsonBean.setMsg("操作成功");
        jsonBean.setData(data);
        return toJson(jsonBean);
    }

    /**
     * 创建成功响应的JsonBean（带消息）
     * @param msg 响应消息
     * @param data 返回数据
     * @return JSON字符串
     */
    public static String success(String msg, Object data) {
        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(1);
        jsonBean.setMsg(msg);
        jsonBean.setData(data);
        return toJson(jsonBean);
    }

    /**
     * 创建错误响应的JsonBean
     * @param msg 错误消息
     * @return JSON字符串
     */
    public static String error(String msg) {
        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(0);
        jsonBean.setMsg(msg);
        return toJson(jsonBean);
    }

    /**
     * 创建错误响应的JsonBean（带状态码）
     * @param code 错误码
     * @param msg 错误消息
     * @return JSON字符串
     */
    public static String error(int code, String msg) {
        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(code);
        jsonBean.setMsg(msg);
        return toJson(jsonBean);
    }

    /**
     * 解析JSON字符串为对象
     * @param jsonString JSON字符串
     * @param clazz 目标类型
     * @return 解析后的对象
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return JSON.parseObject(jsonString, clazz);
    }
}