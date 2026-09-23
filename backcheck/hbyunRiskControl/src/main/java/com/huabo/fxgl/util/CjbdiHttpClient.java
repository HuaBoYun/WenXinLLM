package com.huabo.fxgl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huabo.fxgl.config.CjbdiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * CJBDI 平台 HTTP 调用封装
 * 协议：ticket放header + AES加密body(JSON) → POST → AES解密响应
 * 使用专用的 cjbdiRestTemplate（connectTimeout=15s, readTimeout=150s），
 * 不影响其他模块的通用 RestTemplate 配置
 */
@Slf4j
@Component
public class CjbdiHttpClient {

    @Resource
    private CjbdiConfig cjbdiConfig;

    /** 注入 CJBDI 专用 RestTemplate，超时独立配置，不影响全局 */
    @Resource
    @Qualifier("cjbdiRestTemplate")
    private RestTemplate restTemplate;

    /**
     * 调用 CJBDI 接口（使用全局配置的凭证 - 向后兼容）
     * @param apiPath 接口路径（如 /qyss/cr/queryAdPenaltyCc）
     * @param ticket  接口调用凭证
     * @param requestParams 请求参数Map
     * @return 解密后的 JSON 字符串
     */
    public String callApi(String apiPath, String ticket, Map<String, Object> requestParams) {
        return callApi(apiPath, ticket, cjbdiConfig.getAesKey(), requestParams);
    }

    /**
     * 调用 CJBDI 接口（指定独立凭证）
     * @param apiPath 接口路径
     * @param ticket  接口调用凭证
     * @param aesKey  AES加密密钥
     * @param requestParams 请求参数Map
     * @return 解密后的 JSON 字符串
     */
    public String callApi(String apiPath, String ticket, String aesKey, Map<String, Object> requestParams) {
        validateParams(ticket, aesKey);
        String url = buildUrl(apiPath);

        String requestJson = JSON.toJSONString(requestParams);
        log.info("CJBDI请求 [{}] 参数: {}", apiPath, requestJson);
        String encryptedBody = AesEncryptUtil.encrypt(requestJson, aesKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ticket", ticket);

        HttpEntity<String> entity = new HttpEntity<>(encryptedBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);
        String responseBody = response.getBody();

        if (responseBody == null || responseBody.isEmpty()) {
            log.error("CJBDI响应为空 [{}]", apiPath);
            throw new RuntimeException("CJBDI接口响应为空: " + apiPath);
        }

        try {
            String decrypted = AesEncryptUtil.decrypt(responseBody, aesKey);
            log.info("CJBDI响应 [{}] 长度: {}", apiPath, decrypted.length());
            return decrypted;
        } catch (Exception e) {
            log.warn("CJBDI响应解密失败，尝试直接解析 [{}]: {}", apiPath, e.getMessage());
            return responseBody;
        }
    }

    /**
     * 调用 CJBDI 接口（使用完整URL + 独立凭证）
     * 用于每个接口有独立URL的场景
     * @param fullUrl 完整接口URL
     * @param ticket  接口调用凭证
     * @param aesKey  AES加密密钥
     * @param rawJson 原始JSON字符串（可以是数组或对象）
     * @return 解密后的 JSON 字符串
     */
    public String callFullUrl(String fullUrl, String ticket, String aesKey, String rawJson) {
        validateParams(ticket, aesKey);
        log.info("CJBDI请求 [{}] 参数: {}", fullUrl, rawJson);
        String encryptedBody = AesEncryptUtil.encrypt(rawJson, aesKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ticket", ticket);

        HttpEntity<String> entity = new HttpEntity<>(encryptedBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                fullUrl, HttpMethod.POST, entity, String.class);
        String responseBody = response.getBody();

        if (responseBody == null || responseBody.isEmpty()) {
            log.error("CJBDI响应为空 [{}]", fullUrl);
            throw new RuntimeException("CJBDI接口响应为空: " + fullUrl);
        }

        try {
            String decrypted = AesEncryptUtil.decrypt(responseBody, aesKey);
            log.info("CJBDI响应 [{}] 长度: {}", fullUrl, decrypted.length());
            return decrypted;
        } catch (Exception e) {
            // 解密失败说明外部接口直接返回了明文（通常是错误JSON），打印原始内容便于排查
            log.warn("CJBDI响应解密失败，尝试直接解析 [{}]: {} | 原始响应: {}", fullUrl, e.getMessage(),
                    responseBody.length() > 500 ? responseBody.substring(0, 500) : responseBody);
            return responseBody;
        }
    }

    /**
     * 调用 CJBDI 接口（原始JSON字符串 - 向后兼容）
     */
    public String callApiWithRawJson(String apiPath, String ticket, String rawJson) {
        return callApiWithRawJson(apiPath, ticket, cjbdiConfig.getAesKey(), rawJson);
    }

    /**
     * 调用 CJBDI 接口（原始JSON字符串 + 独立凭证）
     * @param apiPath 接口路径
     * @param ticket  接口调用凭证
     * @param aesKey  AES加密密钥
     * @param rawJson 原始JSON字符串（可以是数组或对象）
     * @return 解密后的 JSON 字符串
     */
    public String callApiWithRawJson(String apiPath, String ticket, String aesKey, String rawJson) {
        validateParams(ticket, aesKey);
        String url = buildUrl(apiPath);

        log.info("CJBDI请求 [{}] 参数: {}", apiPath, rawJson);
        String encryptedBody = AesEncryptUtil.encrypt(rawJson, aesKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ticket", ticket);

        HttpEntity<String> entity = new HttpEntity<>(encryptedBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);
        String responseBody = response.getBody();

        if (responseBody == null || responseBody.isEmpty()) {
            log.error("CJBDI响应为空 [{}]", apiPath);
            throw new RuntimeException("CJBDI接口响应为空: " + apiPath);
        }

        try {
            String decrypted = AesEncryptUtil.decrypt(responseBody, aesKey);
            log.info("CJBDI响应 [{}] 长度: {}", apiPath, decrypted.length());
            return decrypted;
        } catch (Exception e) {
            log.warn("CJBDI响应解密失败，尝试直接解析 [{}]: {} | 原始响应: {}", apiPath, e.getMessage(),
                    responseBody.length() > 500 ? responseBody.substring(0, 500) : responseBody);
            return responseBody;
        }
    }

    /**
     * 调用并解析为JSONObject（Map参数 - 向后兼容）
     */
    public JSONObject callApiAsJson(String apiPath, String ticket, Map<String, Object> requestParams) {
        String result = callApi(apiPath, ticket, requestParams);
        return JSON.parseObject(result);
    }

    /**
     * 调用并解析为JSONObject（Map参数 + 独立凭证）
     */
    public JSONObject callApiAsJson(String apiPath, String ticket, String aesKey, Map<String, Object> requestParams) {
        String result = callApi(apiPath, ticket, aesKey, requestParams);
        return JSON.parseObject(result);
    }

    /**
     * 调用并解析为JSONObject（原始JSON - 向后兼容）
     */
    public JSONObject callApiAsJsonRaw(String apiPath, String ticket, String rawJson) {
        String result = callApiWithRawJson(apiPath, ticket, rawJson);
        return JSON.parseObject(result);
    }

    /**
     * 调用并解析为JSONObject（原始JSON + 独立凭证）
     */
    public JSONObject callApiAsJsonRaw(String apiPath, String ticket, String aesKey, String rawJson) {
        String result = callApiWithRawJson(apiPath, ticket, aesKey, rawJson);
        return JSON.parseObject(result);
    }

    /**
     * 调用完整URL并解析为JSONObject
     */
    public JSONObject callFullUrlAsJson(String fullUrl, String ticket, String aesKey, String rawJson) {
        String result = callFullUrl(fullUrl, ticket, aesKey, rawJson);
        return JSON.parseObject(result);
    }

    /**
     * 构建完整URL
     */
    private String buildUrl(String apiPath) {
        String baseUrl = cjbdiConfig.getBaseUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalStateException("CJBDI base-url未配置");
        }
        return baseUrl + apiPath;
    }

    /**
     * 校验调用参数
     */
    private void validateParams(String ticket, String aesKey) {
        if (aesKey == null || aesKey.isEmpty() || "待配置".equals(aesKey)) {
            throw new IllegalStateException("CJBDI aes-key未配置，请检查数据类别表或application-dev.yml配置");
        }
        if (ticket == null || ticket.isEmpty() || "待配置".equals(ticket)) {
            throw new IllegalStateException("CJBDI ticket未配置，请检查数据类别表或application-dev.yml配置");
        }
    }
}

