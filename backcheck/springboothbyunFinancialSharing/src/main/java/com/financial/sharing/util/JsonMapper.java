package com.financial.sharing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hbfk.util.JsonBean;

/**
 * JSON工具类
 */
public class JsonMapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 将Long类型序列化为字符串，解决JavaScript精度丢失问题
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        // 添加JavaTimeModule以支持LocalDateTime等Java 8时间类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        mapper.registerModule(simpleModule);
        mapper.registerModule(javaTimeModule);
    }

    public static ObjectMapper nonNullMapper() {
        return mapper;
    }

    /**
     * 将对象序列化为JSON字符串
     * @param object 要序列化的对象
     * @return JSON字符串
     */
    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    /**
     * 构建失败的JSON响应
     * @param message 错误消息
     * @return JSON字符串
     */
    public static String buildFalseJson(String message) {
        return MyJsonBean.error(message);
    }

    /**
     * 构建成功的JSON响应
     * @param message 成功消息
     * @return JSON字符串
     */
    public static String buildTrueJson(String message) {
        return MyJsonBean.success(message);
    }

    /**
     * 构建成功的JSON响应（带数据）
     * @param data 数据对象
     * @return JSON字符串
     */
    public static String buildSuccessJson(Object data) {
        return toJson(MyJsonBean.successData(data));
    }
}