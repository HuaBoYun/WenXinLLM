package com.huabo.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 星光问心AI大模型 - 与 WenxinAgent 服务交互的核心服务。
 * WenxinAgent token 不做本地静态配置，使用时通过 hbyun token 调用 WenxinAgent SSO 换取。
 */
@Service
public class StarlightService {

    @Value("${wenxinAgent.api.base-url:https://www.huabao.example.com}")
    private String wenxinAgentBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 创建 WenxinAgent 应用。
     *
     * @param name 应用名称
     * @param description 应用描述
     * @param mode 应用模式（chat、agent-chat、advanced-chat、workflow、completion）
     * @param hbyunToken 华博云登录 token
     * @return 创建成功的应用 ID
     */
    public String createApp(String name, String description, String mode, String hbyunToken) {
        String wenxinAgentAccessToken = exchangeWenxinAgentAccessToken(hbyunToken);
        String createAppUrl = buildWenxinAgentUrl("/console/api/apps");

        Map<String, Object> appBody = new HashMap<String, Object>();
        appBody.put("name", name);
        appBody.put("description", description == null ? "" : description);
        appBody.put("mode", isBlank(mode) ? "chat" : mode.trim());
        appBody.put("icon_type", "emoji");
        appBody.put("icon", "🤖");
        appBody.put("icon_background", "#FFEAD5");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + wenxinAgentAccessToken);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(appBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(createAppUrl, HttpMethod.POST, requestEntity, Map.class);
        if (!isSuccessStatus(response.getStatusCode())) {
            throw new RuntimeException("WenxinAgent 创建应用失败，HTTP状态码：" + response.getStatusCodeValue());
        }

        Map responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("WenxinAgent 创建应用接口返回为空");
        }
        Object appId = responseBody.get("id");
        if (appId == null || isBlank(String.valueOf(appId))) {
            throw new RuntimeException("WenxinAgent 创建应用接口未返回应用ID");
        }
        return String.valueOf(appId);
    }

    /**
     * 使用华博云 token 交换 WenxinAgent access_token。
     */
    private String exchangeWenxinAgentAccessToken(String hbyunToken) {
        if (isBlank(hbyunToken)) {
            throw new RuntimeException("华博云登录 token 不能为空");
        }

        String tokenExchangeUrl = buildWenxinAgentUrl("/console/api/sso/token-exchange");
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("token", hbyunToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenExchangeUrl, HttpMethod.POST, requestEntity, Map.class);
        if (!isSuccessStatus(response.getStatusCode())) {
            throw new RuntimeException("WenxinAgent SSO token 交换失败，HTTP状态码：" + response.getStatusCodeValue());
        }

        Map responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("WenxinAgent SSO token 交换接口返回为空");
        }
        Object result = responseBody.get("result");
        Object data = responseBody.get("data");
        if (!"success".equals(String.valueOf(result)) || !(data instanceof Map)) {
            throw new RuntimeException("WenxinAgent SSO token 交换失败：" + responseBody);
        }

        Object accessToken = ((Map) data).get("access_token");
        if (accessToken == null || isBlank(String.valueOf(accessToken))) {
            throw new RuntimeException("WenxinAgent SSO token 交换接口未返回 access_token");
        }
        return String.valueOf(accessToken);
    }

    private String buildWenxinAgentUrl(String path) {
        String baseUrl = wenxinAgentBaseUrl == null ? "" : wenxinAgentBaseUrl.trim();
        while (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl + path;
    }

    private boolean isSuccessStatus(HttpStatus status) {
        return status != null && status.is2xxSuccessful();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
}
