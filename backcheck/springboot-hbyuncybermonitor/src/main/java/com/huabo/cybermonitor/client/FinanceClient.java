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
 * 数据采集服务调用客户端
 * 通过 HttpClient 直连 hbyun-finance 服务
 */
@Component
public class FinanceClient {

    private static final Logger log = LoggerFactory.getLogger(FinanceClient.class);

    @Value("${finance.service.url:http://127.0.0.1:8763}")
    private String financeUrl;

    /**
     * 触发数据采集任务
     */
    public JSONObject triggerCollection(String templateId) {
        try {
            HashMap<String, Object> fields = new HashMap<>();
            fields.put("templateId", templateId);
            String result = HttpClient.httpPostClient(
                    financeUrl + "/finance/collection/trigger", fields, new HashMap<>(), 0);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用数据采集服务触发采集任务失败", e);
            return null;
        }
    }

    /**
     * 查询采集任务状态
     */
    public JSONObject getCollectionStatus(String taskId) {
        try {
            String result = HttpClient.httpGetClient(
                    financeUrl + "/finance/collection/status/" + taskId, new HashMap<>(), new HashMap<>());
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用数据采集服务查询任务状态失败", e);
            return null;
        }
    }

    /**
     * 查询财务数据汇总
     */
    public JSONObject getFinanceSummary(Map<String, Object> params) {
        try {
            HashMap<String, Object> fields = new HashMap<>(params);
            String result = HttpClient.httpGetClient(
                    financeUrl + "/finance/summary", fields, new HashMap<>());
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用数据采集服务查询财务数据汇总失败", e);
            return null;
        }
    }
}

