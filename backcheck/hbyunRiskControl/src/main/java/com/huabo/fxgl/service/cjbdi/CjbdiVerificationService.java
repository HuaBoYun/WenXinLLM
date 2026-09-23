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
 * CJBDI 身份验证服务（接口01）
 * 身份证两要素校验：姓名 + 身份证号
 */
@Slf4j
@Service
public class CjbdiVerificationService {

    /** 身份验证接口地址 */
    private static final String VERIFICATION_URL = "http://192.0.2.200/qysstest/qyss/risk/verification";
    /** 身份验证ticket */
    private static final String VERIFICATION_TICKET = "TMFGgmJQvGSKWiKORwsntR1/ffHPZij6Mu2RXEK6KhYlWilalhmciKMacAEn1rba";
    /** 身份验证AES密钥 */
    private static final String VERIFICATION_AES_KEY = "5NFC8vTf3unS*REp";

    @Resource
    private CjbdiHttpClient httpClient;
    @Resource
    private CjbdiConfig cjbdiConfig;
    @Resource
    private CjbdiQueryService queryService;

    /**
     * 身份证两要素校验
     * @param name 姓名
     * @param idCard 身份证号
     * @return 验证结果 {identity: "验证一致"/"验证不一致", id: "姓名:身份证号"}
     */
    public Map<String, Object> verifyIdentity(String name, String idCard) {
        try {
            // 获取凭证（优先从类别表获取，回退到硬编码默认值）
            String ticket = VERIFICATION_TICKET;
            String aesKey = VERIFICATION_AES_KEY;
            String apiUrl = VERIFICATION_URL;

            CjbdiDataCategory category = queryService.getCategoryByApiCode("01");
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

            // 构建请求体：JSON数组 [{name, id, authorize}]
            JSONArray requestArray = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("name", name);
            item.put("id", idCard);
            item.put("authorize", 1);
            requestArray.add(item);

            // 调用接口
            JSONObject response = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, requestArray.toJSONString());

            // 解析响应
            int code = response.getIntValue("code");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", code);
            result.put("msg", response.getString("msg"));

            if (code == 1000) {
                JSONArray dataArr = response.getJSONArray("data");
                if (dataArr != null && !dataArr.isEmpty()) {
                    JSONObject dataItem = dataArr.getJSONObject(0);
                    result.put("identity", dataItem.getString("identity"));
                    result.put("id", dataItem.getString("id"));
                }
            }
            return result;
        } catch (Exception e) {
            log.error("身份验证失败 name={}", name, e);
            Map<String, Object> errorResult = new LinkedHashMap<>();
            errorResult.put("code", 3001);
            errorResult.put("msg", "身份验证请求失败: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 批量身份验证
     * @param verifyList 验证列表 [{name, idCard}]
     * @return 批量验证结果
     */
    public List<Map<String, Object>> batchVerifyIdentity(List<Map<String, String>> verifyList) {
        try {
            String ticket = VERIFICATION_TICKET;
            String aesKey = VERIFICATION_AES_KEY;
            String apiUrl = VERIFICATION_URL;

            CjbdiDataCategory category = queryService.getCategoryByApiCode("01");
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

            // 构建请求体
            JSONArray requestArray = new JSONArray();
            for (Map<String, String> item : verifyList) {
                JSONObject obj = new JSONObject();
                obj.put("name", item.get("name"));
                obj.put("id", item.get("idCard"));
                obj.put("authorize", 1);
                requestArray.add(obj);
            }

            JSONObject response = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, requestArray.toJSONString());

            int code = response.getIntValue("code");
            List<Map<String, Object>> results = new ArrayList<>();

            if (code == 1000) {
                JSONArray dataArr = response.getJSONArray("data");
                if (dataArr != null) {
                    for (int i = 0; i < dataArr.size(); i++) {
                        JSONObject dataItem = dataArr.getJSONObject(i);
                        Map<String, Object> r = new LinkedHashMap<>();
                        r.put("identity", dataItem.getString("identity"));
                        r.put("id", dataItem.getString("id"));
                        results.add(r);
                    }
                }
            } else {
                Map<String, Object> errorItem = new LinkedHashMap<>();
                errorItem.put("code", code);
                errorItem.put("msg", response.getString("msg"));
                results.add(errorItem);
            }
            return results;
        } catch (Exception e) {
            log.error("批量身份验证失败", e);
            List<Map<String, Object>> errorResults = new ArrayList<>();
            Map<String, Object> errorItem = new LinkedHashMap<>();
            errorItem.put("code", 3001);
            errorItem.put("msg", "批量身份验证请求失败: " + e.getMessage());
            errorResults.add(errorItem);
            return errorResults;
        }
    }

    /**
     * 不良记录查询（接口23 - apiCode=23）
     * 查询个人不良记录，请求体：[{name, id, authorize}]
     * 响应：[{id: "姓名:身份证", risk: "高风险/低风险/无风险..."}]
     *
     * @param name   姓名
     * @param idCard 身份证号
     * @return 查询结果 {id, risk, code, msg}
     */
    public Map<String, Object> queryBadRecord(String name, String idCard) {
        try {
            // 从类别表获取 apiCode=23 的凭证和地址
            CjbdiDataCategory category = queryService.getCategoryByApiCode("23");
            if (category == null) {
                Map<String, Object> err = new LinkedHashMap<>();
                err.put("code", 500);
                err.put("msg", "不良记录接口未配置（apiCode=23）");
                return err;
            }
            String ticket = category.getTicket() != null && !category.getTicket().isEmpty()
                    ? category.getTicket() : cjbdiConfig.getTicket();
            String aesKey = category.getAesKey() != null && !category.getAesKey().isEmpty()
                    ? category.getAesKey() : cjbdiConfig.getAesKey();
            String apiUrl = category.getApiUrl();
            if (apiUrl == null || apiUrl.isEmpty()) {
                apiUrl = cjbdiConfig.getBaseUrl() + category.getApiPath();
            }

            // 构建请求体：JSON数组 [{name, id, authorize}]
            JSONArray requestArray = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("name", name);
            item.put("id", idCard);
            item.put("authorize", 1);
            requestArray.add(item);

            log.info("不良记录查询 name={}", name);
            JSONObject response = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, requestArray.toJSONString());

            int code = response.getIntValue("code");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", code);
            result.put("msg", response.getString("msg"));

            if (code == 1000) {
                JSONArray dataArr = response.getJSONArray("data");
                if (dataArr != null && !dataArr.isEmpty()) {
                    JSONObject dataItem = dataArr.getJSONObject(0);
                    result.put("id",   dataItem.getString("id"));
                    result.put("risk", dataItem.getString("risk"));
                }
            }
            return result;
        } catch (Exception e) {
            log.error("不良记录查询失败 name={}", name, e);
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("code", 3001);
            err.put("msg", "不良记录查询失败: " + e.getMessage());
            return err;
        }
    }

    /**
     * 企业纠纷单次查询（接口24 - apiCode=24）
     * 允许前端指定 company 和 ajlx，直接查询一种案件类型
     * 请求体：JSON 对象（非数组），AES 加密后发送，与其他接口加密方式一致
     *
     * @param company 公司名称（可为空，为空时查账号绑定公司）
     * @param ajlx    案件类型枚举（msys/mses/mszs/xsys/xses/xszs/xzys/xzes/xzzs/
     *                sczx/hfzx/zxyy/ccbqzx/fsccbqsc/xzpcys/pcsqsc/msgx）
     * @return 查询结果 {code, msg, records: [...], total}
     */
    public Map<String, Object> queryDisputeCase(String company, String ajlx) {
        try {
            CjbdiDataCategory category = queryService.getCategoryByApiCode("24");
            if (category == null) {
                Map<String, Object> err = new LinkedHashMap<>();
                err.put("code", 500);
                err.put("msg", "企业纠纷接口未配置（apiCode=24）");
                return err;
            }
            String ticket = category.getTicket() != null && !category.getTicket().isEmpty()
                    ? category.getTicket() : cjbdiConfig.getTicket();
            String aesKey = category.getAesKey() != null && !category.getAesKey().isEmpty()
                    ? category.getAesKey() : cjbdiConfig.getAesKey();
            String apiUrl = category.getApiUrl();
            if (apiUrl == null || apiUrl.isEmpty()) {
                apiUrl = cjbdiConfig.getBaseUrl() + category.getApiPath();
            }

            String loginName = cjbdiConfig.getDisputeLoginName();
            String loginPwd  = cjbdiConfig.getDisputeLoginPassword();
            if (loginName == null || loginName.isEmpty()) {
                Map<String, Object> err = new LinkedHashMap<>();
                err.put("code", 500);
                err.put("msg", "企业纠纷接口账号密码未配置");
                return err;
            }

            // 构建请求体：JSON 对象（按文档规范）
            JSONObject params = new JSONObject();
            params.put("loginName",  loginName);
            params.put("passWord",   loginPwd);
            if (company != null && !company.trim().isEmpty()) {
                params.put("company", company.trim());
            }
            params.put("ajlx",       ajlx);
            params.put("pageNum",    1);
            params.put("pageSize",   50);
            params.put("caseStatus", 1);

            log.info("企业纠纷查询 company={} ajlx={}", company, ajlx);
            JSONObject response = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, params.toJSONString());

            int code = response.getIntValue("code");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", code);
            result.put("msg",  response.getString("msg"));

            if (code == 1000) {
                JSONObject data = response.getJSONObject("data");
                if (data != null) {
                    result.put("records", data.getJSONArray("records"));
                    result.put("total",   data.getIntValue("total"));
                    result.put("pages",   data.getIntValue("pages"));
                } else {
                    result.put("records", new JSONArray());
                    result.put("total",   0);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("企业纠纷查询失败 company={} ajlx={}", company, ajlx, e);
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("code", 3001);
            err.put("msg", "企业纠纷查询失败: " + e.getMessage());
            return err;
        }
    }
}

