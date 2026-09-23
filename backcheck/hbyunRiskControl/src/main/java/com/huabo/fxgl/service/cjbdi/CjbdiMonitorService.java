package com.huabo.fxgl.service.cjbdi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huabo.fxgl.config.CjbdiConfig;
import com.huabo.fxgl.entity.cjbdi.CjbdiDataCategory;
import com.huabo.fxgl.util.CjbdiHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * CJBDI 企业监控名单管理服务（接口04-07）
 * 04: 监控名单添加
 * 05: 监控名单查询
 * 06: 监控名单删除
 * 07: 监控信息获取
 */
@Slf4j
@Service
public class CjbdiMonitorService {

    /** 监控名单添加接口 */
    private static final String MONITOR_ADD_PATH = "/qyss/monitor/addcompany";
    /** 监控名单查询接口 */
    private static final String MONITOR_QUERY_PATH = "/qyss/monitor/query";
    /** 监控名单删除接口 */
    private static final String MONITOR_DEL_PATH = "/qyss/monitor/delcompany";
    /** 监控信息获取接口 */
    private static final String MONITOR_CASES_PATH = "/qyss/dsk/queryMonitorCases";

    @Resource
    private CjbdiHttpClient httpClient;
    @Resource
    private CjbdiConfig cjbdiConfig;
    @Resource
    private CjbdiQueryService queryService;

    /**
     * 04-添加企业到监控名单
     * @param companyNames 企业名称列表
     * @return 操作结果
     */
    public Map<String, Object> addMonitorCompanies(List<String> companyNames) {
        try {
            // 构建请求体: [{name: "公司名称1"}, {name: "公司名称2"}]
            JSONArray requestArray = new JSONArray();
            for (String name : companyNames) {
                JSONObject item = new JSONObject();
                item.put("name", name);
                requestArray.add(item);
            }

            // 获取凭证
            String[] credentials = getMonitorCredentials("04");
            String ticket = credentials[0];
            String aesKey = credentials[1];
            String apiUrl = credentials[2];

            JSONObject response;
            if (apiUrl != null && !apiUrl.isEmpty()) {
                response = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, requestArray.toJSONString());
            } else {
                response = httpClient.callApiAsJsonRaw(MONITOR_ADD_PATH, ticket, aesKey, requestArray.toJSONString());
            }

            return buildResult(response);
        } catch (Exception e) {
            log.error("添加监控名单失败 companies={}", companyNames, e);
            return buildErrorResult("添加监控名单失败: " + e.getMessage());
        }
    }

    /**
     * 05-查询监控名单
     * @param pageIndex 当前页数（默认1）
     * @param pageSize 每页条数（默认1000，最大1000）
     * @return 监控名单列表
     */
    public Map<String, Object> queryMonitorList(int pageIndex, int pageSize) {
        try {
            // 构建请求体: {pageSize, pageIndex}
            JSONObject requestBody = new JSONObject();
            requestBody.put("pageSize", Math.min(pageSize, 1000));
            requestBody.put("pageIndex", pageIndex);

            String[] credentials = getMonitorCredentials("05");
            String ticket = credentials[0];
            String aesKey = credentials[1];
            String apiUrl = credentials[2];

            JSONObject response;
            if (apiUrl != null && !apiUrl.isEmpty()) {
                response = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, requestBody.toJSONString());
            } else {
                response = httpClient.callApiAsJsonRaw(MONITOR_QUERY_PATH, ticket, aesKey, requestBody.toJSONString());
            }

            Map<String, Object> result = buildResult(response);
            // 解析data中的list和total
            if (response.getIntValue("code") == 1000) {
                JSONObject data = response.getJSONObject("data");
                if (data != null) {
                    result.put("total", data.getIntValue("total"));
                    JSONArray list = data.getJSONArray("list");
                    if (list != null) {
                        List<String> names = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            names.add(item.getString("name"));
                        }
                        result.put("list", names);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            log.error("查询监控名单失败", e);
            return buildErrorResult("查询监控名单失败: " + e.getMessage());
        }
    }

    /**
     * 06-从监控名单中删除企业
     * @param companyNames 企业名称列表
     * @return 操作结果
     */
    public Map<String, Object> deleteMonitorCompanies(List<String> companyNames) {
        try {
            // 构建请求体: [{name: "公司名称1"}, {name: "公司名称2"}]
            JSONArray requestArray = new JSONArray();
            for (String name : companyNames) {
                JSONObject item = new JSONObject();
                item.put("name", name);
                requestArray.add(item);
            }

            String[] credentials = getMonitorCredentials("06");
            String ticket = credentials[0];
            String aesKey = credentials[1];
            String apiUrl = credentials[2];

            JSONObject response;
            if (apiUrl != null && !apiUrl.isEmpty()) {
                response = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, requestArray.toJSONString());
            } else {
                response = httpClient.callApiAsJsonRaw(MONITOR_DEL_PATH, ticket, aesKey, requestArray.toJSONString());
            }

            return buildResult(response);
        } catch (Exception e) {
            log.error("删除监控名单失败 companies={}", companyNames, e);
            return buildErrorResult("删除监控名单失败: " + e.getMessage());
        }
    }

    /**
     * 07-获取监控信息（通过日期获取监控案件变动）
     * @param queryDate 查询日期（格式：yyyy-MM-dd）
     * @param pageIndex 当前页数
     * @param pageSize 每页条数（最大1000）
     * @return 监控案件信息
     */
    public Map<String, Object> getMonitorCases(String queryDate, int pageIndex, int pageSize) {
        try {
            // 构建请求体: {queryDate, pageIndex, pageSize}
            JSONObject requestBody = new JSONObject();
            requestBody.put("queryDate", queryDate);
            requestBody.put("pageIndex", pageIndex);
            requestBody.put("pageSize", Math.min(pageSize, 1000));

            String[] credentials = getMonitorCredentials("07");
            String ticket = credentials[0];
            String aesKey = credentials[1];
            String apiUrl = credentials[2];

            JSONObject response;
            if (apiUrl != null && !apiUrl.isEmpty()) {
                response = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, requestBody.toJSONString());
            } else {
                response = httpClient.callApiAsJsonRaw(MONITOR_CASES_PATH, ticket, aesKey, requestBody.toJSONString());
            }

            Map<String, Object> result = buildResult(response);
            // 解析分页数据
            if (response.getIntValue("code") == 1000) {
                JSONObject data = response.getJSONObject("data");
                if (data != null) {
                    result.put("total", data.getIntValue("total"));
                    result.put("current", data.getIntValue("current"));
                    result.put("size", data.getIntValue("size"));
                    result.put("records", data.getJSONArray("records"));
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取监控信息失败 queryDate={}", queryDate, e);
            return buildErrorResult("获取监控信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取监控接口凭证
     * @param apiCode API编码（04/05/06/07）
     * @return [ticket, aesKey, apiUrl]
     */
    private String[] getMonitorCredentials(String apiCode) {
        CjbdiDataCategory category = queryService.getCategoryByApiCode(apiCode);
        String ticket = cjbdiConfig.getTicket();
        String aesKey = cjbdiConfig.getAesKey();
        String apiUrl = null;

        if (category != null) {
            if (category.getTicket() != null && !category.getTicket().isEmpty()) {
                ticket = category.getTicket();
            }
            if (category.getAesKey() != null && !category.getAesKey().isEmpty()) {
                aesKey = category.getAesKey();
            }
            if (category.getApiUrl() != null && !category.getApiUrl().isEmpty()) {
                apiUrl = category.getApiUrl();
            }
        }
        return new String[]{ticket, aesKey, apiUrl};
    }

    private Map<String, Object> buildResult(JSONObject response) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", response.getIntValue("code"));
        result.put("msg", response.getString("msg"));
        return result;
    }

    private Map<String, Object> buildErrorResult(String msg) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 3001);
        result.put("msg", msg);
        return result;
    }
}

