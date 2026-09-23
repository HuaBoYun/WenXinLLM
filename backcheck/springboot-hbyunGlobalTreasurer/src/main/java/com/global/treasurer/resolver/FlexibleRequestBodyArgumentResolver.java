package com.global.treasurer.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 灵活的请求体参数解析器
 * 自动识别 Content-Type 并使用相应的解析方式:
 * - application/json: 使用 Jackson 解析
 * - application/x-www-form-urlencoded: 使用 Spring 数据绑定
 * - multipart/form-data: 使用 Spring 数据绑定
 * 
 * @author 华博云开发团队
 * @since 2026-02-12
 */
public class FlexibleRequestBodyArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(FlexibleRequestBodyArgumentResolver.class);

    private final ObjectMapper objectMapper;

    public FlexibleRequestBodyArgumentResolver(ObjectMapper objectMapper) {
        // 配置 ObjectMapper 以兼容布尔值到整数的转换
        this.objectMapper = objectMapper.copy();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        // 注册 JavaTimeModule，支持 LocalDate / LocalDateTime 的序列化与反序列化
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.objectMapper.registerModule(javaTimeModule);
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 支持 java.util.Date 的 "yyyy-MM-dd" 格式反序列化
        this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean supports = parameter.hasParameterAnnotation(FlexibleRequestBody.class);
        log.info("[DEBUG-FlexibleRequestBody] supportsParameter: {}, hasAnnotation: {}",
            parameter.getParameter().getName(), supports);
        return supports;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        System.out.println("========== [FlexibleRequestBody] resolveArgument 开始 ==========");

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            log.warn("[DEBUG-FlexibleRequestBody] HttpServletRequest 为 null");
            return null;
        }

        String contentType = request.getContentType();
        System.out.println("[DEBUG-FlexibleRequestBody] ContentType: " + contentType);
        log.info("[DEBUG-FlexibleRequestBody] ContentType: {}", contentType);

        Class<?> parameterType = parameter.getParameterType();
        System.out.println("[DEBUG-FlexibleRequestBody] 参数类型: " + parameterType.getName());
        log.info("[DEBUG-FlexibleRequestBody] 参数类型: {}", parameterType.getName());

        // 读取请求体内容用于检测格式
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        System.out.println("[DEBUG-FlexibleRequestBody] 请求体内容: " + body);
        log.info("[DEBUG-FlexibleRequestBody] 请求体内容: {}", body);

        // 智能检测：即使 Content-Type 是 x-www-form-urlencoded，如果内容是 JSON 格式，也使用 JSON 解析器
        boolean isJsonContent = body != null && body.trim().startsWith("{") && body.trim().endsWith("}");

        // 打印所有可用的参数名（用于调试）
        java.util.Enumeration<String> paramNames = request.getParameterNames();
        System.out.println("[DEBUG-FlexibleRequestBody] request.getParameterNames():");
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String[] values = request.getParameterValues(name);
            System.out.println("  " + name + " = " + (values != null && values.length > 0 ? values[0] : "null"));
        }

        // 处理 JSON 格式（包括智能检测到的 JSON 内容）
        if ((contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) || isJsonContent) {
            System.out.println("[DEBUG-FlexibleRequestBody] 使用 JSON 解析器");
            log.info("[DEBUG-FlexibleRequestBody] 使用 JSON 解析器");
            // 重新读取 InputStream 并解析
            try {
                // 预处理 JSON：将布尔值转换为整数
                String processedBody = convertBooleanToInteger(body);
                Object result = objectMapper.readValue(processedBody, parameterType);
                log.info("[DEBUG-FlexibleRequestBody] 解析结果: {}", result);
                return result;
            } catch (Exception e) {
                log.error("[DEBUG-FlexibleRequestBody] JSON 解析失败", e);
                throw e;
            }
        }

        // 处理 form-data 格式
        System.out.println("[DEBUG-FlexibleRequestBody] 使用 FormData 解析器");
        log.info("[DEBUG-FlexibleRequestBody] 使用 FormData 解析器");
        return parseFormData(request, parameter, binderFactory, body);
    }

    /**
     * 将 JSON 中的布尔值转换为整数
     * 处理 "isEnabled": true -> "isEnabled": 1
     */
    private String convertBooleanToInteger(String body) {
        if (body == null || body.isEmpty()) {
            return body;
        }
        // 将 "isEnabled":true 替换为 "isEnabled":1
        String result = body.replaceAll("\"isEnabled\"\\s*:\\s*true", "\"isEnabled\":1");
        result = result.replaceAll("\"isEnabled\"\\s*:\\s*false", "\"isEnabled\":0");
        // 同样处理 deleteFlag
        result = result.replaceAll("\"deleteFlag\"\\s*:\\s*true", "\"deleteFlag\":1");
        result = result.replaceAll("\"deleteFlag\"\\s*:\\s*false", "\"deleteFlag\":0");
        return result;
    }

    /**
     * 解析 JSON 格式的请求体
     */
    private Object parseJson(HttpServletRequest request, Class<?> targetClass) throws IOException {
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        log.info("[DEBUG-FlexibleRequestBody] 请求体内容: {}", body);
        log.info("[DEBUG-FlexibleRequestBody] 目标类型: {}", targetClass.getName());

        if (body == null || body.trim().isEmpty()) {
            log.warn("[DEBUG-FlexibleRequestBody] 请求体为空,返回新实例");
            return BeanUtils.instantiateClass(targetClass);
        }

        Object result = objectMapper.readValue(body, targetClass);
        log.info("[DEBUG-FlexibleRequestBody] 解析结果: {}", result);

        return result;
    }

    /**
     * 解析 form-data 格式的请求参数
     */
    private Object parseFormData(HttpServletRequest request, MethodParameter parameter,
                                  WebDataBinderFactory binderFactory, String body) throws Exception {

        Class<?> targetClass = parameter.getParameterType();
        log.info("[DEBUG-FlexibleRequestBody] 目标类型: {}", targetClass.getName());

        // 创建目标对象实例
        Object target;
        if (Map.class.isAssignableFrom(targetClass)) {
            // 如果目标类型是 Map 或其子类，创建 HashMap 实例
            target = new HashMap<>();
            log.info("[DEBUG-FlexibleRequestBody] 目标类型是Map，创建HashMap实例");
        } else {
            // 否则使用 BeanUtils 创建实例
            target = BeanUtils.instantiateClass(targetClass);
        }

        // 检查是否是 multipart/form-data 格式
        String contentType = request.getContentType();
        boolean isMultipart = contentType != null && contentType.contains("multipart/form-data");
        log.info("[DEBUG-FlexibleRequestBody] 是否为multipart格式: {}", isMultipart);

        if (isMultipart) {
            // 对于 multipart/form-data，直接使用 getParameter 获取参数
            // Spring Boot 会自动处理 multipart 请求
            log.info("[DEBUG-FlexibleRequestBody] 开始解析 multipart/form-data (使用getParameter方式)");

            // 获取所有可能的参数名（从目标类的字段中推断）
            java.lang.reflect.Field[] fields = targetClass.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                String fieldName = field.getName();
                // 跳过静态字段和serialVersionUID
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }

                // 使用 getParameter 获取参数值
                String value = request.getParameter(fieldName);
                log.info("[DEBUG-FlexibleRequestBody] getParameter({}) = {}", fieldName, value);

                if (value != null && !value.isEmpty()) {
                    try {
                        field.setAccessible(true);
                        log.info("[DEBUG-FlexibleRequestBody] 找到字段: {} (类型: {})", field.getName(), field.getType().getName());
                        // 根据字段类型进行转换
                        Object convertedValue = convertValue(value, field.getType());
                        log.info("[DEBUG-FlexibleRequestBody] 转换后的值: {} (类型: {})", convertedValue, convertedValue != null ? convertedValue.getClass().getName() : "null");
                        field.set(target, convertedValue);
                        log.info("[DEBUG-FlexibleRequestBody] 字段设置成功: {} = {}", field.getName(), convertedValue);
                    } catch (Exception e) {
                        log.error("[DEBUG-FlexibleRequestBody] 设置字段 {} 失败", fieldName, e);
                    }
                }
            }

            log.info("[DEBUG-FlexibleRequestBody] 解析结果: {}", target);
            return target;
        }

        // 处理 application/x-www-form-urlencoded 格式
        log.info("[DEBUG-FlexibleRequestBody] 开始处理 application/x-www-form-urlencoded 格式");

        // 判断目标类型是否为 Map
        boolean isMapType = Map.class.isAssignableFrom(targetClass);

        // 尝试从直接读取的表单数据
        try {
            log.info("[DEBUG-FlexibleRequestBody] 请求体内容: {}", body);

            if (body != null && !body.trim().isEmpty()) {
                // 解析表单数据：key1=value1&key2=value2
                String[] pairs = body.split("&");
                log.info("[DEBUG-FlexibleRequestBody] 解析到 {} 个参数对", pairs.length);

                for (String pair : pairs) {
                    String[] keyValue = pair.split("=", 2);
                    if (keyValue.length == 2) {
                        String key = java.net.URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                        String value = java.net.URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                        log.info("[DEBUG-FlexibleRequestBody] 处理参数: key={}, value={}", key, value);

                        if (isMapType) {
                            // 如果目标类型是 Map，直接放入 Map
                            ((Map<String, Object>) target).put(key, value);
                            log.info("[DEBUG-FlexibleRequestBody] 添加到Map: {} = {}", key, value);
                        } else {
                            // 使用反射设置属性
                            try {
                                java.lang.reflect.Field field = findField(targetClass, key);
                                if (field != null) {
                                    field.setAccessible(true);
                                    log.info("[DEBUG-FlexibleRequestBody] 找到字段: {} (类型: {})", field.getName(), field.getType().getName());
                                    // 根据字段类型进行转换
                                    Object convertedValue = convertValue(value, field.getType());
                                    log.info("[DEBUG-FlexibleRequestBody] 转换后的值: {} (类型: {})", convertedValue, convertedValue != null ? convertedValue.getClass().getName() : "null");
                                    field.set(target, convertedValue);
                                    log.info("[DEBUG-FlexibleRequestBody] 字段设置成功: {} = {}", field.getName(), convertedValue);
                                } else {
                                    log.warn("[DEBUG-FlexibleRequestBody] 未找到字段: {}", key);
                                }
                            } catch (Exception e) {
                                log.error("[DEBUG-FlexibleRequestBody] 设置字段 {} 失败", key, e);
                            }
                        }
                    }
                }

                log.info("[DEBUG-FlexibleRequestBody] 解析结果: {}", target);
                return target;
            }
        } catch (IOException e) {
            log.error("[DEBUG-FlexibleRequestBody] 读取请求体失败", e);
        }

        // 如果读取失败，尝试使用getParameterMap
        log.warn("[DEBUG-FlexibleRequestBody] 从InputStream读取失败，尝试使用getParameterMap");
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("[DEBUG-FlexibleRequestBody] 参数数量: {}", parameterMap.size());

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            String value = (values != null && values.length > 0) ? values[0] : null;
            log.info("[DEBUG-FlexibleRequestBody] 处理参数: key={}, value={}", key, value);

            if (isMapType) {
                // 如果目标类型是 Map，直接放入 Map
                ((Map<String, Object>) target).put(key, value);
                log.info("[DEBUG-FlexibleRequestBody] 添加到Map: {} = {}", key, value);
            } else {
                // 使用反射设置属性
                try {
                    java.lang.reflect.Field field = findField(targetClass, key);
                    if (field != null) {
                        field.setAccessible(true);
                        log.info("[DEBUG-FlexibleRequestBody] 找到字段: {} (类型: {})", field.getName(), field.getType().getName());
                        // 根据字段类型进行转换
                        Object convertedValue = convertValue(value, field.getType());
                        log.info("[DEBUG-FlexibleRequestBody] 转换后的值: {} (类型: {})", convertedValue, convertedValue != null ? convertedValue.getClass().getName() : "null");
                        field.set(target, convertedValue);
                        log.info("[DEBUG-FlexibleRequestBody] 字段设置成功: {} = {}", field.getName(), convertedValue);
                    } else {
                        log.warn("[DEBUG-FlexibleRequestBody] 未找到字段: {}", key);
                    }
                } catch (Exception e) {
                    log.error("[DEBUG-FlexibleRequestBody] 设置字段 {} 失败", key, e);
                }
            }
        }

        log.info("[DEBUG-FlexibleRequestBody] 解析结果: {}", target);
        return target;
    }

    /**
     * 查找字段(支持驼峰命名)
     */
    private java.lang.reflect.Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // 尝试父类
            if (clazz.getSuperclass() != null) {
                return findField(clazz.getSuperclass(), fieldName);
            }
            return null;
        }
    }

    /**
     * 转换值类型
     */
    private Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return value == null || value.isEmpty() ? null : Integer.parseInt(value);
        } else if (targetType == Long.class || targetType == long.class) {
            return value == null || value.isEmpty() ? null : Long.parseLong(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (targetType == BigDecimal.class) {
            return value == null || value.isEmpty() ? null : new BigDecimal(value);
        } else if (targetType == Double.class || targetType == double.class) {
            return value == null || value.isEmpty() ? null : Double.parseDouble(value);
        } else if (targetType == Float.class || targetType == float.class) {
            return value == null || value.isEmpty() ? null : Float.parseFloat(value);
        } else if (targetType == java.util.Date.class || targetType == java.sql.Date.class) {
            // 处理日期类型：支持时间戳（毫秒）和日期字符串
            try {
                long timestamp = Long.parseLong(value);
                return new java.util.Date(timestamp);
            } catch (NumberFormatException e) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return sdf.parse(value);
                } catch (Exception ex) {
                    try {
                        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        return sdf2.parse(value);
                    } catch (Exception ex2) {
                        throw new RuntimeException("无法解析日期: " + value, ex2);
                    }
                }
            }
        } else if (targetType == LocalDate.class) {
            if (value == null || value.isEmpty()) return null;
            // 如果包含 T，截取日期部分
            String dateStr = value.contains("T") ? value.substring(0, value.indexOf("T")) : value;
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else if (targetType == LocalDateTime.class) {
            if (value == null || value.isEmpty()) return null;
            try {
                // 支持 ISO 格式 yyyy-MM-ddTHH:mm:ss
                return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e1) {
                try {
                    return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e2) {
                    // 只有日期，补充时间为 00:00:00
                    return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                }
            }
        }
        return value;
    }
}

