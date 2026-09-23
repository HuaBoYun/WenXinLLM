package com.huabo.cybermonitor.client;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 风控服务调用客户端
 * 通过 HttpClient 直连 hbyunRiskControl 服务
 */
@Component
public class RiskControlClient {

    private static final Logger log = LoggerFactory.getLogger(RiskControlClient.class);

    @Value("${risk-control.service.url:http://127.0.0.1:8763}")
    private String riskControlUrl;

    /**
     * 查询预警列表（按领域）
     */
    public JSONObject getWarningsByDomain(Map<String, Object> params) {
        try {
            HashMap<String, Object> fields = new HashMap<>(params);
            String result = HttpClient.httpPostClient(
                    riskControlUrl + "/riskControl/warning/listByDomain", fields, new HashMap<>(), 0);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用风控服务查询预警列表失败", e);
            return null;
        }
    }

    /**
     * 触发模型评估
     */
    public JSONObject triggerEvaluation(String modelId, String companyId) {
        try {
            HashMap<String, Object> fields = new HashMap<>();
            fields.put("modelId", modelId);
            fields.put("companyId", companyId);
            String result = HttpClient.httpPostClient(
                    riskControlUrl + "/riskControl/evaluation/trigger", fields, new HashMap<>(), 0);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用风控服务触发模型评估失败", e);
            return null;
        }
    }

    /**
     * 查询预警详情
     */
    public JSONObject getWarningDetail(String warningId) {
        try {
            HashMap<String, Object> fields = new HashMap<>();
            fields.put("warningId", warningId);
            String result = HttpClient.httpGetClient(
                    riskControlUrl + "/riskControl/warning/detail/" + warningId, fields, new HashMap<>());
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用风控服务查询预警详情失败, warningId={}", warningId, e);
            return null;
        }
    }

    /**
     * 查询预警统计数据
     */
    public JSONObject getWarningStatistics(Map<String, Object> params) {
        try {
            HashMap<String, Object> fields = new HashMap<>(params);
            String result = HttpClient.httpGetClient(
                    riskControlUrl + "/riskControl/warning/statistics", fields, new HashMap<>());
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用风控服务查询预警统计数据失败", e);
            return null;
        }
    }
}

