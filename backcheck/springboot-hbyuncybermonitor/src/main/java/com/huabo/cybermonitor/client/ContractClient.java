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
 * 内部合同模块调用客户端
 * 通过 HttpClient 直连 hbyunContractModule 服务（端口 8064），获取合同台账、履行、纠纷等数据
 */
@Component
public class ContractClient {

    private static final Logger log = LoggerFactory.getLogger(ContractClient.class);

    @Value("${contract.service.url:http://127.0.0.1:8064}")
    private String contractUrl;

    /**
     * 查询合同列表（分页）
     */
    public JSONObject getContractList(Map<String, Object> params) {
        try {
            HashMap<String, Object> fields = new HashMap<>(params);
            String result = HttpClient.request(contractUrl + "/contract/list", fields, null);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用合同模块查询合同列表失败", e);
            return null;
        }
    }

    /**
     * 查询合同详情
     */
    public JSONObject getContractDetail(String contractId) {
        try {
            HashMap<String, Object> fields = new HashMap<>();
            fields.put("contractId", contractId);
            String result = HttpClient.request(
                    contractUrl + "/contract/detail/" + contractId, fields, null);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用合同模块查询合同详情失败, contractId={}", contractId, e);
            return null;
        }
    }

    /**
     * 查询合同履行记录
     */
    public JSONObject getContractPerformance(String contractId) {
        try {
            HashMap<String, Object> fields = new HashMap<>();
            String result = HttpClient.request(
                    contractUrl + "/contract/performance/" + contractId, fields, null);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用合同模块查询合同履行记录失败", e);
            return null;
        }
    }

    /**
     * 查询合同统计数据
     */
    public JSONObject getContractStatistics(Map<String, Object> params) {
        try {
            HashMap<String, Object> fields = new HashMap<>(params);
            String result = HttpClient.request(contractUrl + "/contract/statistics", fields, null);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用合同模块查询合同统计数据失败", e);
            return null;
        }
    }

    /**
     * 查询合同项目关联信息
     */
    public JSONObject getContractProjects(Map<String, Object> params) {
        try {
            HashMap<String, Object> fields = new HashMap<>(params);
            String result = HttpClient.httpGetClient(
                    contractUrl + "/contractProject/list", fields, new HashMap<>());
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("调用合同模块查询合同项目关联信息失败", e);
            return null;
        }
    }
}

