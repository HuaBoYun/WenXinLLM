package com.huabo.fxgl.service.cjbdi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.fxgl.config.CjbdiConfig;
import com.huabo.fxgl.entity.cjbdi.*;
import com.huabo.fxgl.mapper.cjbdi.*;
import com.huabo.fxgl.util.CjbdiHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * CJBDI 统一查询调度服务
 * 根据 categoryId 路由到对应接口，解析响应并落库
 */
@Slf4j
@Service
public class CjbdiQueryService {

    /** 需要发送JSON数组请求体的API编码集合 (20涉诉/21失信/22限高/23不良记录) */
    private static final Set<String> ARRAY_BODY_API_CODES = new HashSet<>(Arrays.asList("20", "21", "22", "23"));

    /** 并行查询线程池：外部接口响应慢，用固定线程池并发处理多个类别 */
    private static final ExecutorService QUERY_EXECUTOR = Executors.newFixedThreadPool(10);

    @Resource
    private CjbdiConfig cjbdiConfig;
    @Resource
    private CjbdiHttpClient httpClient;
    @Resource
    private CjbdiDataCategoryMapper categoryMapper;
    @Resource
    private CjbdiQueryResultMapper queryResultMapper;
    @Resource
    private CjbdiBusinessInfoMapper businessInfoMapper;
    @Resource
    private CjbdiInvestmentMapper investmentMapper;
    @Resource
    private CjbdiBiddingMapper biddingMapper;
    @Resource
    private CjbdiPublicOpinionMapper publicOpinionMapper;
    @Resource
    private CjbdiAdminPenaltyMapper adminPenaltyMapper;
    @Resource
    private CjbdiLitigationMapper litigationMapper;
    @Resource
    private CjbdiDishonestyMapper dishonestyMapper;
    @Resource
    private CjbdiRiskDataMapper riskDataMapper;

    /**
     * 获取所有启用的数据类别
     */
    public List<CjbdiDataCategory> getEnabledCategories() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<CjbdiDataCategory>()
                        .eq(CjbdiDataCategory::getIsEnabled, 1)
                        .orderByAsc(CjbdiDataCategory::getSortOrder));
    }

    /**
     * 按分组获取数据类别
     */
    public Map<String, List<CjbdiDataCategory>> getCategoriesByGroup() {
        List<CjbdiDataCategory> all = getEnabledCategories();
        Map<String, List<CjbdiDataCategory>> grouped = new LinkedHashMap<>();
        for (CjbdiDataCategory cat : all) {
            grouped.computeIfAbsent(cat.getCategoryGroup(), k -> new ArrayList<>()).add(cat);
        }
        return grouped;
    }

    /**
     * 执行单个类别的查询
     * <p>
     * 采用「快速返回 + 异步落库」策略：
     * 1. 调用外部接口获取数据
     * 2. 立即将主记录（含 rawJson）写入数据库，并从 rawJson 解析 detailItems 直接返回给前端
     * 3. 后台异步线程继续将明细数据 INSERT 子表，不阻塞前端响应
     * <p>
     * 这样即使子表 INSERT 数量很大（如限高消费 200+ 条），前端也能立即拿到数据展示。
     *
     * @param companyId   监控企业ID（本地YyCompany的ID）
     * @param companyName 企业名称
     * @param creditCode  统一社会信用代码
     * @param categoryId  数据类别ID
     * @param staffId     操作人ID
     * @param orgId       操作人机构ID
     * @return 查询结果主记录（detailItems 已从 rawJson 解析填充）
     */
    public CjbdiQueryResult queryByCategory(Long companyId, String companyName,
                                             String creditCode, Long categoryId,
                                             Long staffId, Long orgId) {
        CjbdiDataCategory category = categoryMapper.selectById(categoryId);
        if (category == null) {
            throw new RuntimeException("数据类别不存在: " + categoryId);
        }

        // 构建查询结果主记录
        CjbdiQueryResult result = new CjbdiQueryResult();
        result.setCompanyId(companyId != null ? companyId : 0L);
        result.setCompanyName(companyName);
        result.setCreditCode(creditCode);
        result.setCategoryId(categoryId);
        result.setQueryTime(new Timestamp(System.currentTimeMillis()));
        result.setStaffId(staffId);
        result.setOrgId(orgId);

        try {
            // 优先使用类别表中配置的独立凭证，否则回退到全局配置
            String ticket = getTicketForCategory(category);
            String aesKey = getAesKeyForCategory(category);
            String apiCode = category.getApiCode();
            JSONObject response;

            // ── 企业纠纷（apiCode=24）走专用路径 ─────────────────────────────────────
            // 该接口需要 loginName/passWord，且每次只能查一种 ajlx，需循环合并
            if ("24".equals(apiCode)) {
                return queryDisputeCase(category, companyName, result);
            }

            // API 20/21/22/23 需要发送JSON数组请求体
            if (ARRAY_BODY_API_CODES.contains(apiCode)) {
                String arrayJson = buildArrayRequestBody(apiCode, companyName, creditCode);
                if (category.getApiUrl() != null && !category.getApiUrl().isEmpty()) {
                    response = httpClient.callFullUrlAsJson(category.getApiUrl(), ticket, aesKey, arrayJson);
                } else {
                    response = httpClient.callApiAsJsonRaw(category.getApiPath(), ticket, aesKey, arrayJson);
                }
            } else {
                Map<String, Object> params = buildRequestParams(category, companyName, creditCode);
                if (category.getApiUrl() != null && !category.getApiUrl().isEmpty()) {
                    response = httpClient.callFullUrlAsJson(category.getApiUrl(), ticket, aesKey, JSON.toJSONString(params));
                } else {
                    response = httpClient.callApiAsJson(category.getApiPath(), ticket, aesKey, params);
                }
            }

            // 检查响应状态
            int code = response.getIntValue("code");
            if (code != 1000) {
                result.setStatus(0);
                result.setErrorMsg(response.getString("msg"));
                result.setDataCount(0);
                saveResultSync(result);
                return result;
            }

            // ── 阶段1：保存主记录（含 rawJson），立即提交 ──────────────────────────
            result.setRawJson(response.toJSONString());
            result.setStatus(1);
            result.setDataCount(0);
            saveResultSync(result);   // 独立事务，立即提交，result.getResultId() 此时已有值

            // ── 阶段2：从 rawJson 直接解析 detailItems，立即填充返回值 ─────────────
            List<Object> detailCollector = new ArrayList<>();
            parseToCollectorOnly(category, response, detailCollector);
            if (!detailCollector.isEmpty()) {
                result.setDetailItems(detailCollector.size() == 1
                        ? detailCollector.get(0) : detailCollector);
            }

            // ── 阶段3：异步落库子表，不阻塞当前线程 ─────────────────────────────────
            final CjbdiQueryResult resultSnapshot = result;
            final JSONObject responseSnapshot = response;
            final CjbdiDataCategory categorySnapshot = category;
            QUERY_EXECUTOR.submit(() -> {
                try {
                    asyncSaveDetailItems(categorySnapshot, responseSnapshot, resultSnapshot);
                } catch (Exception ex) {
                    log.error("异步落库子表失败 [{}] resultId={}", categorySnapshot.getCategoryName(),
                            resultSnapshot.getResultId(), ex);
                }
            });

            return result;

        } catch (Exception e) {
            log.error("CJBDI查询失败 [{}] 企业={}", category.getCategoryName(), companyName, e);
            result.setStatus(-1);
            String errMsg = e.getMessage();
            if (errMsg != null && errMsg.length() > 500) {
                errMsg = errMsg.substring(0, 500);
            }
            result.setErrorMsg(errMsg);
            result.setDataCount(0);
            if (result.getResultId() != null) {
                queryResultMapper.updateById(result);
            } else {
                saveResultSync(result);
            }
            return result;
        }
    }

    /**
     * 在独立事务中保存主记录，确保立即提交（不受外层事务影响）
     */
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW,
                   rollbackFor = Exception.class)
    public void saveResultSync(CjbdiQueryResult result) {
        if (result.getResultId() != null) {
            queryResultMapper.updateById(result);
        } else {
            queryResultMapper.insert(result);
        }
    }

    /**
     * 仅解析响应数据到 detailCollector，不做任何数据库操作
     * 用于快速填充返回给前端的 detailItems
     */
    private void parseToCollectorOnly(CjbdiDataCategory category, JSONObject response,
                                       List<Object> detailCollector) {
        try {
            // 复用 parseAndSave 的解析逻辑，但传入一个不做 INSERT 的空 result
            // 实际上 parseAndSave 内部会调用 mapper.insert，所以这里单独实现轻量解析
            parseResponseToCollector(category, response, detailCollector);
        } catch (Exception e) {
            log.warn("快速解析 detailItems 失败 [{}]，前端将从 rawJson 自行解析",
                    category.getCategoryName(), e);
        }
    }

    /**
     * 异步落库：将明细数据 INSERT 子表，并更新主记录的 dataCount
     * 在独立事务中执行，与主流程完全解耦
     */
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW,
                   rollbackFor = Exception.class)
    public void asyncSaveDetailItems(CjbdiDataCategory category, JSONObject response,
                                      CjbdiQueryResult result) {
        List<Object> detailCollector = new ArrayList<>();
        int dataCount = parseAndSave(category, response, result, detailCollector);
        if (dataCount > 0) {
            result.setDataCount(dataCount);
            queryResultMapper.updateById(result);
            log.info("异步落库完成 [{}] resultId={} count={}", category.getCategoryName(),
                    result.getResultId(), dataCount);
        }
    }

    /**
     * 轻量解析：只解析响应数据到 collector，不做 INSERT
     * 字段映射与 parseAndSave 保持一致，确保前端拿到的数据结构相同
     */
    private void parseResponseToCollector(CjbdiDataCategory category, JSONObject response,
                                           List<Object> collector) {
        String apiCode = category.getApiCode();
        switch (apiCode) {
            case "20": // 涉诉信息
                parseLitigationToCollector(response, collector);
                break;
            case "21": // 失信记录
                parseDishonestyToCollector(response, collector);
                break;
            case "22": // 限制高消费
            case "24": // 企业纠纷
            default:
                parseRiskDataToCollector(category, response, collector);
                break;
        }
    }

    /** 涉诉信息：轻量解析到 collector（不 INSERT） */
    private void parseLitigationToCollector(JSONObject response, List<Object> collector) {
        Object dataObj = response.get("data");
        JSONArray dataArr = (dataObj instanceof JSONArray) ? (JSONArray) dataObj : null;
        if (dataArr == null || dataArr.isEmpty()) return;
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject entity = dataArr.getJSONObject(i);
            JSONObject detail = entity.getJSONObject("detail");
            if (detail == null) continue;
            String[] caseTypes = {"civil","criminal","administrative","preservation",
                    "implement","bankrupt","jurisdict","compensate"};
            String[] caseTypeNames = {"民事","刑事","行政","非诉保全","执行","破产","管辖","赔偿"};
            for (int t = 0; t < caseTypes.length; t++) {
                JSONObject section = detail.getJSONObject(caseTypes[t]);
                if (section == null) continue;
                JSONArray cases = section.getJSONArray("cases");
                if (cases == null || cases.isEmpty()) continue;
                for (int j = 0; j < cases.size(); j++) {
                    JSONObject c = cases.getJSONObject(j);
                    CjbdiLitigation lit = new CjbdiLitigation();
                    lit.setCaseType(caseTypeNames[t]);
                    lit.setCaseNo(getFirstNonNull(c, "c_ah", "ah"));
                    lit.setCaseReason(getFirstNonNull(c, "n_laay", "claaymc"));
                    lit.setCourtName(getFirstNonNull(c, "n_jbfy", "cfymc"));
                    lit.setJudgeDate(getFirstNonNull(c, "d_larq", "dsarq"));
                    lit.setRoleType(getFirstNonNull(c, "n_ssdw", "cssdw"));
                    lit.setContent(c.toJSONString());
                    collector.add(lit);
                }
            }
        }
    }

    /** 失信记录：轻量解析到 collector（不 INSERT） */
    private void parseDishonestyToCollector(JSONObject response, List<Object> collector) {
        Object dataObj = response.get("data");
        JSONArray dataArr = (dataObj instanceof JSONArray) ? (JSONArray) dataObj : null;
        if (dataArr == null || dataArr.isEmpty()) return;
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject item = dataArr.getJSONObject(i);
            CjbdiDishonesty dis = new CjbdiDishonesty();
            dis.setCaseNo(item.getString("ah"));
            dis.setCourtName(item.getString("zxfy"));
            dis.setAreaName(item.getString("sf"));
            dis.setDuty(item.getString("yw"));
            dis.setPerformance(item.getString("lxqk"));
            dis.setDishonestyType(item.getString("xwqx"));
            dis.setPublishDate(item.getString("fbrq"));
            dis.setRegDate(item.getString("larq"));
            collector.add(dis);
        }
    }

    /** 通用风险数据：轻量解析到 collector（不 INSERT） */
    private void parseRiskDataToCollector(CjbdiDataCategory category, JSONObject response,
                                           List<Object> collector) {
        // 尝试从 data.records / data.items / data（直接数组）提取列表
        JSONArray items = null;
        Object dataObj = response.get("data");
        if (dataObj instanceof JSONArray) {
            items = (JSONArray) dataObj;
        } else if (dataObj instanceof JSONObject) {
            JSONObject data = (JSONObject) dataObj;
            if (data.containsKey("records")) items = data.getJSONArray("records");
            else if (data.containsKey("items")) items = data.getJSONArray("items");
        }
        if (items == null || items.isEmpty()) return;

        String apiCode = category.getApiCode();
        for (int i = 0; i < items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item == null || item.isEmpty()) continue;
            CjbdiRiskData rd = new CjbdiRiskData();
            rd.setCategoryId(category.getCategoryId());
            mapRiskDataFields(rd, item, apiCode);
            rd.setItemDetail(item.toJSONString());
            collector.add(rd);
        }
    }

    /**
     * 批量查询多个类别 —— 并行执行，互不阻塞
     * 每个类别独立发起外部请求，最长等待 150 秒（单接口 readTimeout=120s + 余量）
     */
    @Transactional(rollbackFor = Exception.class)
    public List<CjbdiQueryResult> queryMultipleCategories(Long companyId, String companyName,
                                                           String creditCode, List<Long> categoryIds,
                                                           Long staffId, Long orgId) {
        // 为每个 categoryId 提交一个异步任务
        List<CompletableFuture<CjbdiQueryResult>> futures = categoryIds.stream()
                .map(catId -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return queryByCategory(companyId, companyName, creditCode, catId, staffId, orgId);
                    } catch (Exception e) {
                        log.error("并行查询类别[{}]失败", catId, e);
                        CjbdiQueryResult err = new CjbdiQueryResult();
                        err.setCompanyId(companyId != null ? companyId : 0L);
                        err.setCompanyName(companyName);
                        err.setCategoryId(catId);
                        err.setStatus(-1);
                        err.setErrorMsg(e.getMessage() != null && e.getMessage().length() > 200
                                ? e.getMessage().substring(0, 200) : e.getMessage());
                        return err;
                    }
                }, QUERY_EXECUTOR))
                .collect(Collectors.toList());

        // 等待所有任务完成，最长 150 秒
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .get(150, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("部分类别查询超时或异常: {}", e.getMessage());
        }

        // 收集已完成的结果（超时的任务返回 null，过滤掉）
        return futures.stream()
                .map(f -> {
                    try {
                        return f.isDone() ? f.get() : null;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 构建Map类型请求参数（非数组体API）
     */
    private Map<String, Object> buildRequestParams(CjbdiDataCategory category,
                                                    String companyName, String creditCode) {
        Map<String, Object> params = new HashMap<>();
        String apiCode = category.getApiCode();

        switch (apiCode) {
            case "02": // 企业名录: key, page, size, authorize
                params.put("key", companyName);
                params.put("page", 1);
                params.put("size", 20);
                params.put("authorize", 1);
                break;
            case "03": // 工商基础信息: name/creditcode, authorize
                if (creditCode != null && !creditCode.isEmpty()) {
                    params.put("creditcode", creditCode);
                } else {
                    params.put("name", companyName);
                }
                params.put("authorize", 1);
                break;
            case "08": // 一般纳税人
            case "09": // 投融资事件
            case "14": // 欠税公告
            case "15": // 动产抵押
            case "16": // 股权出质
            case "17": // 股权冻结
                // companyName/creditCode二选一 + authorize
                if (creditCode != null && !creditCode.isEmpty()) {
                    params.put("creditCode", creditCode);
                } else {
                    params.put("companyName", companyName);
                }
                params.put("authorize", 1);
                break;
            case "10": // 招投标信息: companyName/creditCode + pageIndex + authorize
            case "11": // 舆情信息: companyName/creditCode + pageIndex + authorize
                if (creditCode != null && !creditCode.isEmpty()) {
                    params.put("creditCode", creditCode);
                } else {
                    params.put("companyName", companyName);
                }
                params.put("pageIndex", 1);
                params.put("authorize", 1);
                break;
            case "12": // 行政处罚: DB配置的URL是/modelTag/administrativePunishments/list
            case "13": // 环保处罚: DB配置的URL是/modelTag/environmentalPunishments/list
            case "25": // 行政处罚(根目录版)
                // /modelTag/*/list 端点统一使用 creditCode + authorize
                if (creditCode != null && !creditCode.isEmpty()) {
                    params.put("creditCode", creditCode);
                } else {
                    params.put("creditCode", companyName);
                }
                params.put("authorize", 1);
                break;
            case "18": // 军采暂停黑名单: supplyCn (文档明确只需supplyCn)
                params.put("supplyCn", companyName);
                break;
            case "19": // 政采黑名单: name
                params.put("name", companyName);
                break;
            // case "24" 企业纠纷：已在 queryByCategory 中提前拦截，走 queryDisputeCase 专用路径，不会到达此处
            default:
                // 兜底：companyName + authorize
                params.put("companyName", companyName);
                params.put("authorize", 1);
                break;
        }
        return params;
    }

    /**
     * 构建JSON数组请求体（API 20/21/22/23）
     */
    private String buildArrayRequestBody(String apiCode, String companyName, String creditCode) {
        Map<String, Object> item = new HashMap<>();
        switch (apiCode) {
            case "20": // 涉诉: [{name, creditCode, authorize, type}]
                item.put("name", companyName);
                if (creditCode != null && !creditCode.isEmpty()) {
                    item.put("creditCode", creditCode);
                }
                item.put("authorize", 1);
                item.put("type", "0"); // 0=企业查询
                break;
            case "21": // 失信: [{name, authorize}]
            case "22": // 限高: [{name, authorize}]
                item.put("name", companyName);
                item.put("authorize", 1);
                break;
            case "23": // 不良记录: [{name, id, authorize}]
                item.put("name", companyName);
                item.put("id", "");
                item.put("authorize", 1);
                break;
            default:
                item.put("name", companyName);
                item.put("authorize", 1);
                break;
        }
        List<Map<String, Object>> array = new ArrayList<>();
        array.add(item);
        return JSON.toJSONString(array);
    }

    /**
     * 按类别解析响应并落库
     * @param detailCollector 收集解析后的明细对象，供前端直接使用
     */
    private int parseAndSave(CjbdiDataCategory category, JSONObject response,
                              CjbdiQueryResult result, List<Object> detailCollector) {
        String apiCode = category.getApiCode();
        switch (apiCode) {
            case "03":
                return parseBusinessInfo(response, result, detailCollector);
            case "09":
                return parseInvestment(response, result, detailCollector);
            case "10":
                return parseBidding(response, result, detailCollector);
            case "11":
                return parsePublicOpinion(response, result, detailCollector);
            case "12":
            case "25":
                return parseAdminPenalty(response, result, detailCollector);
            case "20":
                return parseLitigation(response, result, detailCollector);
            case "21":
                return parseDishonesty(response, result, detailCollector);
            case "22":
                return parseLimitConsumption(category, response, result, detailCollector);
            case "24":
                return parseDisputeCase(category, response, result, detailCollector);
            default:
                return parseGenericRiskData(category, response, result, detailCollector);
        }
    }

    // ========== 各接口解析方法 ==========

    /** API 03: 工商基础信息 - data.BASIC 子对象 */
    private int parseBusinessInfo(JSONObject response, CjbdiQueryResult result, List<Object> detailCollector) {
        JSONObject data = response.getJSONObject("data");
        if (data == null) return 0;

        JSONObject basic = data.getJSONObject("BASIC");
        JSONObject source = (basic != null) ? basic : data;

        CjbdiBusinessInfo info = new CjbdiBusinessInfo();
        info.setResultId(result.getResultId());
        info.setCompanyId(result.getCompanyId());
        info.setCreditCode(source.getString("CREDITCODE"));
        info.setEntName(source.getString("ENTNAME"));
        info.setLegalPerson(source.getString("FRNAME") != null ? source.getString("FRNAME") : source.getString("NAME"));
        info.setRegCapital(source.getString("REGCAP"));
        info.setEstablishDate(source.getString("ESDATE"));
        info.setEntStatus(source.getString("ENTSTATUS"));
        info.setEntType(source.getString("ENTTYPE"));
        info.setIndustry(source.getString("CEICATEGORYNAME1"));
        info.setAddress(source.getString("DOM"));
        info.setBusinessScope(source.getString("ZSOPSCOPE") != null ? source.getString("ZSOPSCOPE") : source.getString("OPSCOPE"));
        info.setRegAuthority(source.getString("REGORG"));
        info.setApprovalDate(source.getString("APPRDATE"));
        info.setBusinessFrom(source.getString("OPFROM"));
        info.setBusinessTo(source.getString("OPTO"));
        info.setDataJson(data.toJSONString());
        info.setCreateTime(new Timestamp(System.currentTimeMillis()));
        businessInfoMapper.insert(info);
        detailCollector.add(info);
        return 1;
    }

    /** API 09: 投融资事件 - data.items[] */
    private int parseInvestment(JSONObject response, CjbdiQueryResult result, List<Object> detailCollector) {
        JSONArray items = extractItems(response);
        if (items == null || items.isEmpty()) return 0;

        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            CjbdiInvestment inv = new CjbdiInvestment();
            inv.setResultId(result.getResultId());
            inv.setCompanyId(result.getCompanyId());
            inv.setInvestRound(item.getString("round"));
            inv.setInvestAmount(item.getString("amount"));
            inv.setInvestDate(item.getString("invest_date"));
            inv.setInvestor(item.getString("invest_company"));
            inv.setValuation(item.getString("currency"));
            inv.setRatio(item.getString("round_type"));
            inv.setCreateTime(new Timestamp(System.currentTimeMillis()));
            investmentMapper.insert(inv);
            detailCollector.add(inv);
            count++;
        }
        return count;
    }

    /** API 10: 招投标信息 - data.records[] */
    private int parseBidding(JSONObject response, CjbdiQueryResult result, List<Object> detailCollector) {
        JSONArray records = extractRecords(response);
        if (records == null || records.isEmpty()) return 0;

        int count = 0;
        for (int i = 0; i < records.size(); i++) {
            JSONObject item = records.getJSONObject(i);
            CjbdiBidding bid = new CjbdiBidding();
            bid.setResultId(result.getResultId());
            bid.setCompanyId(result.getCompanyId());
            bid.setTitle(item.getString("title"));
            bid.setPublishDate(item.getString("publish_time"));
            bid.setBidType(item.getString("notice_type_major"));
            bid.setRegion(item.getString("notice_type_sub"));
            // winner_company 是数组: [{name, bid_money}]
            Object winner = item.get("winner_company");
            if (winner instanceof JSONArray) {
                JSONArray winnerArr = (JSONArray) winner;
                bid.setPurchaser(winnerArr.toJSONString());
                // 提取第一个中标金额
                if (!winnerArr.isEmpty()) {
                    JSONObject firstWinner = winnerArr.getJSONObject(0);
                    bid.setAmount(firstWinner.getString("bid_money"));
                }
            } else {
                bid.setPurchaser(item.getString("winner_company"));
            }
            // content_text是完整HTML公告内容，CONTENT_URL字段已改为CLOB，直接存储
            bid.setContentUrl(item.getString("content_text"));
            bid.setCreateTime(new Timestamp(System.currentTimeMillis()));
            biddingMapper.insert(bid);
            detailCollector.add(bid);
            count++;
        }
        return count;
    }

    /** API 11: 舆情信息 - data.records[] */
    private int parsePublicOpinion(JSONObject response, CjbdiQueryResult result, List<Object> detailCollector) {
        JSONArray records = extractRecords(response);
        if (records == null || records.isEmpty()) return 0;

        int count = 0;
        for (int i = 0; i < records.size(); i++) {
            JSONObject item = records.getJSONObject(i);
            CjbdiPublicOpinion op = new CjbdiPublicOpinion();
            op.setResultId(result.getResultId());
            op.setCompanyId(result.getCompanyId());
            op.setTitle(item.getString("title"));
            op.setSource(item.getString("source"));
            op.setPublishDate(item.getString("pub_time"));
            op.setSentiment(item.getString("sentiment"));
            // content可能是新闻全文，SUMMARY字段已改为CLOB，直接存储
            op.setSummary(item.getString("content"));
            op.setContentUrl(item.getString("url"));
            op.setCreateTime(new Timestamp(System.currentTimeMillis()));
            publicOpinionMapper.insert(op);
            detailCollector.add(op);
            count++;
        }
        return count;
    }

    /** API 12/25: 行政处罚 - data.items[] */
    private int parseAdminPenalty(JSONObject response, CjbdiQueryResult result, List<Object> detailCollector) {
        JSONArray items = extractItems(response);
        if (items == null || items.isEmpty()) return 0;

        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            CjbdiAdminPenalty penalty = new CjbdiAdminPenalty();
            penalty.setResultId(result.getResultId());
            penalty.setCompanyId(result.getCompanyId());
            penalty.setPenaltyNo(item.getString("punishNumber"));
            // type为空时取punishName作为处罚类型
            String penaltyType = item.getString("type");
            if (penaltyType == null || penaltyType.isEmpty()) {
                penaltyType = item.getString("punishName");
            }
            penalty.setPenaltyType(penaltyType);
            // reason为空时取evidence作为处罚事由
            String penaltyReason = item.getString("reason");
            if (penaltyReason == null || penaltyReason.isEmpty()) {
                penaltyReason = item.getString("evidence");
            }
            penalty.setPenaltyReason(penaltyReason);
            penalty.setPenaltyResult(item.getString("content"));
            penalty.setPenaltyAuthority(item.getString("departmentName"));
            penalty.setPenaltyDate(item.getString("decisionDate"));
            penalty.setPenaltyAmount(item.getString("punishAmount"));
            penalty.setCreateTime(new Timestamp(System.currentTimeMillis()));
            adminPenaltyMapper.insert(penalty);
            detailCollector.add(penalty);
            count++;
        }
        return count;
    }

    /**
     * API 20: 法眼涉诉 - data是数组，每个元素有detail.cases_tree嵌套结构
     * 响应: data: [{id, detail: {cases_tree: {criminal: [...], civil: [...], ...}}}]
     */
    private int parseLitigation(JSONObject response, CjbdiQueryResult result, List<Object> detailCollector) {
        JSONArray dataArr = response.getJSONArray("data");
        if (dataArr == null || dataArr.isEmpty()) return 0;

        int count = 0;
        for (int d = 0; d < dataArr.size(); d++) {
            JSONObject dataItem = dataArr.getJSONObject(d);
            JSONObject detail = dataItem.getJSONObject("detail");
            if (detail == null) continue;

            JSONObject casesTree = detail.getJSONObject("cases_tree");
            if (casesTree == null) continue;

            for (String caseType : casesTree.keySet()) {
                Object val = casesTree.get(caseType);
                if (!(val instanceof JSONArray)) continue;
                JSONArray cases = (JSONArray) val;
                for (int i = 0; i < cases.size(); i++) {
                    JSONObject caseItem = cases.getJSONObject(i);
                    CjbdiLitigation lit = new CjbdiLitigation();
                    lit.setResultId(result.getResultId());
                    lit.setCompanyId(result.getCompanyId());
                    lit.setCaseType(caseType);
                    lit.setCaseNo(getFirstNonNull(caseItem, "ah", "caseNo", "case_no"));
                    lit.setCaseReason(getFirstNonNull(caseItem, "ay", "caseReason", "case_reason"));
                    lit.setCourtName(getFirstNonNull(caseItem, "fymc", "courtName", "court_name"));
                    lit.setJudgeDate(getFirstNonNull(caseItem, "jarq", "judgeDate", "judge_date"));
                    lit.setRoleType(getFirstNonNull(caseItem, "ssdw", "roleType", "role_type"));
                    lit.setContent(caseItem.toJSONString());
                    lit.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    litigationMapper.insert(lit);
                    detailCollector.add(lit);
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * API 21: 失信记录 - data直接是数组
     * 响应: data: [{zxfy, ah, fbrq, lxqk, xwqx, yw, ...}]
     */
    private int parseDishonesty(JSONObject response, CjbdiQueryResult result, List<Object> detailCollector) {
        Object dataObj = response.get("data");
        JSONArray dataArr = (dataObj instanceof JSONArray) ? (JSONArray) dataObj : null;
        if (dataArr == null || dataArr.isEmpty()) return 0;

        int count = 0;
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject item = dataArr.getJSONObject(i);
            CjbdiDishonesty dis = new CjbdiDishonesty();
            dis.setResultId(result.getResultId());
            dis.setCompanyId(result.getCompanyId());
            dis.setCaseNo(item.getString("ah"));
            dis.setCourtName(item.getString("zxfy"));
            dis.setAreaName(item.getString("sf"));
            dis.setDuty(item.getString("yw"));
            dis.setPerformance(item.getString("lxqk"));
            dis.setDishonestyType(item.getString("xwqx"));
            dis.setPublishDate(item.getString("fbrq"));
            dis.setRegDate(item.getString("larq"));
            dis.setCreateTime(new Timestamp(System.currentTimeMillis()));
            dishonestyMapper.insert(dis);
            detailCollector.add(dis);
            count++;
        }
        return count;
    }

    /** API 22: 限制高消费 */
    private int parseLimitConsumption(CjbdiDataCategory category, JSONObject response,
                                       CjbdiQueryResult result, List<Object> detailCollector) {
        Object dataObj = response.get("data");
        JSONArray dataArr = (dataObj instanceof JSONArray) ? (JSONArray) dataObj : null;
        if (dataArr == null || dataArr.isEmpty()) return 0;
        return saveRiskDataFromArray(category, dataArr, result, detailCollector);
    }

    /** API 24: 企业纠纷 - 解析单次 ajlx 响应并落库 */
    private int parseDisputeCase(CjbdiDataCategory category, JSONObject response,
                                   CjbdiQueryResult result, List<Object> detailCollector) {
        JSONArray records = extractRecords(response);
        if (records == null || records.isEmpty()) return 0;
        return saveRiskDataFromArray(category, records, result, detailCollector);
    }

    /**
     * 企业纠纷专用查询入口（apiCode=24）
     * <p>
     * 接口特殊性（参考《企业纠纷管理查询接口说明文档-202603》）：
     * 1. 请求体为 JSON 对象（非数组），包含 loginName/passWord/company/ajlx/pageNum/pageSize/caseStatus
     * 2. 加密方式与其他接口相同：AES/ECB/PKCS5Padding，ticket 放 Header
     * 3. ajlx 每次只能传一种案件类型，需循环 17 种类型逐一查询后合并
     * 4. company 可选：传公司名时查该公司案件；不传时查账号绑定公司及下级公司的所有案件
     * 5. 响应结构：data.records 分页数组，各 ajlx 返回字段略有差异，但共同字段为：
     *    cbaah(案号)、dsarq(立案时间)、djarq(结案时间)、cfymc(法院)、
     *    claaymc(案由)、cssdw(诉讼地位)、cjafs(结案方式)、nbdse(标的数额)
     */
    private CjbdiQueryResult queryDisputeCase(CjbdiDataCategory category,
                                               String companyName,
                                               CjbdiQueryResult result) {
        // 从配置读取账号密码
        String loginName = cjbdiConfig.getDisputeLoginName();
        String loginPwd  = cjbdiConfig.getDisputeLoginPassword();
        if (loginName == null || loginName.isEmpty() || loginPwd == null || loginPwd.isEmpty()) {
            result.setStatus(0);
            result.setErrorMsg("企业纠纷接口账号密码未配置，请在 application.yml 中设置 cjbdi.dispute-login-name 和 cjbdi.dispute-login-password");
            result.setDataCount(0);
            saveResultSync(result);
            return result;
        }

        String ticket = getTicketForCategory(category);
        String aesKey  = getAesKeyForCategory(category);
        String apiUrl  = category.getApiUrl();

        // 文档定义的全部案件类型枚举
        String[] ajlxList = {
            "msys",    // 民事一审
            "mses",    // 民事二审
            "mszs",    // 民事再审
            "xsys",    // 刑事一审
            "xses",    // 刑事二审
            "xszs",    // 刑事再审
            "xzys",    // 行政一审
            "xzes",    // 行政二审
            "xzzs",    // 行政再审
            "sczx",    // 首次执行
            "hfzx",    // 恢复执行
            "zxyy",    // 执行异议
            "ccbqzx",  // 财产保全执行
            "fsccbqsc",// 非诉财产保全审查
            "xzpcys",  // 行政赔偿一审
            "pcsqsc",  // 破产申请审查
            "msgx"     // 民事管辖
        };

        // 合并所有 ajlx 的 records
        JSONArray allRecords = new JSONArray();
        // 记录是否遇到权限类错误（3001），遇到后终止后续循环
        boolean permissionDenied = false;
        String permissionErrMsg  = null;

        for (String ajlx : ajlxList) {
            if (permissionDenied) break;
            try {
                // 按文档构建请求体（JSON 对象，AES 加密后发送，与其他接口加密方式一致）
                JSONObject params = new JSONObject();
                params.put("loginName",  loginName);
                params.put("passWord",   loginPwd);
                // company 固定为测试公司名（TODO: 待接口权限开通后改为传入 companyName）
                params.put("company", "案件同步测试有限公司");
                params.put("ajlx",       ajlx);
                params.put("pageNum",    1);
                params.put("pageSize",   50);
                params.put("caseStatus", 1);  // 0=删除 1=正常

                // 请求方式与其他接口保持一致：AES 加密 body + ticket 放 Header
                JSONObject resp = httpClient.callFullUrlAsJson(apiUrl, ticket, aesKey, params.toJSONString());

                int code = resp.getIntValue("code");
                if (code == 1000) {
                    JSONArray records = extractRecords(resp);
                    if (records != null && !records.isEmpty()) {
                        // 给每条记录补充 _ajlx 字段，方便前端区分案件类型
                        for (int i = 0; i < records.size(); i++) {
                            JSONObject rec = records.getJSONObject(i);
                            if (rec != null) rec.put("_ajlx", ajlx);
                        }
                        allRecords.addAll(records);
                        log.info("企业纠纷 ajlx={} 查询成功，本次 {} 条，累计 {} 条",
                                ajlx, records.size(), allRecords.size());
                    }
                } else if (code == 3001) {
                    permissionDenied = true;
                    permissionErrMsg = String.format(
                            "企业纠纷接口账号[%s]无权查询公司[%s]（code=3001）：%s",
                            loginName, companyName, resp.getString("msg"));
                    log.warn("企业纠纷 ajlx={} 返回3001，终止后续查询. company={}", ajlx, companyName);
                } else {
                    log.warn("企业纠纷 ajlx={} 查询失败: code={} msg={}", ajlx, code, resp.getString("msg"));
                }
            } catch (Exception e) {
                log.warn("企业纠纷 ajlx={} 查询异常: {}", ajlx, e.getMessage());
            }
        }

        // 权限不足：直接返回失败，不保存空记录
        if (permissionDenied && allRecords.isEmpty()) {
            result.setStatus(0);
            result.setErrorMsg(permissionErrMsg);
            result.setDataCount(0);
            saveResultSync(result);
            return result;
        }

        // 构造合并后的响应 JSON（allRecords 可能为空，表示该公司无纠纷记录）
        JSONObject mergedResponse = new JSONObject();
        mergedResponse.put("code", 1000);
        mergedResponse.put("msg", "查询完成");
        JSONObject mergedData = new JSONObject();
        mergedData.put("records", allRecords);
        mergedData.put("total", allRecords.size());
        mergedResponse.put("data", mergedData);

        // ── 阶段1：保存主记录（含合并后的 rawJson），立即提交 ──────────────────
        result.setRawJson(mergedResponse.toJSONString());
        result.setStatus(1);
        result.setDataCount(0);
        saveResultSync(result);

        // ── 阶段2：从合并结果直接解析 detailItems，立即填充返回值 ──────────────
        List<Object> detailCollector = new ArrayList<>();
        parseRiskDataToCollector(category, mergedResponse, detailCollector);
        if (!detailCollector.isEmpty()) {
            result.setDetailItems(detailCollector.size() == 1
                    ? detailCollector.get(0) : detailCollector);
        }

        // ── 阶段3：异步落库子表 ────────────────────────────────────────────────
        final CjbdiQueryResult resultSnapshot = result;
        final JSONObject responseSnapshot = mergedResponse;
        final CjbdiDataCategory categorySnapshot = category;
        QUERY_EXECUTOR.submit(() -> {
            try {
                asyncSaveDetailItems(categorySnapshot, responseSnapshot, resultSnapshot);
            } catch (Exception ex) {
                log.error("企业纠纷异步落库失败 resultId={}", resultSnapshot.getResultId(), ex);
            }
        });

        return result;
    }

    /** 通用解析：items/records/data数组 → RiskData宽表 */
    private int parseGenericRiskData(CjbdiDataCategory category, JSONObject response,
                                      CjbdiQueryResult result, List<Object> detailCollector) {
        JSONArray items = extractItems(response);
        if (items != null && !items.isEmpty()) {
            return saveRiskDataFromArray(category, items, result, detailCollector);
        }
        JSONArray records = extractRecords(response);
        if (records != null && !records.isEmpty()) {
            return saveRiskDataFromArray(category, records, result, detailCollector);
        }
        // data可能是JSONArray（直接数组）或JSONObject（带分页包装），需要类型判断
        Object dataObj = response.get("data");
        if (dataObj instanceof JSONArray) {
            JSONArray dataArr = (JSONArray) dataObj;
            if (!dataArr.isEmpty()) {
                return saveRiskDataFromArray(category, dataArr, result, detailCollector);
            }
        }
        return 0;
    }

    private int saveRiskDataFromArray(CjbdiDataCategory category, JSONArray items,
                                       CjbdiQueryResult result, List<Object> detailCollector) {
        int count = 0;
        String apiCode = category.getApiCode();
        for (int i = 0; i < items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            // 跳过空对象 - CJBDI平台无数据时可能返回[{}]而非[]
            if (item == null || item.isEmpty()) {
                continue;
            }
            CjbdiRiskData rd = new CjbdiRiskData();
            rd.setResultId(result.getResultId());
            rd.setCompanyId(result.getCompanyId());
            rd.setCategoryId(category.getCategoryId());
            // 按API编码做精确字段映射
            mapRiskDataFields(rd, item, apiCode);
            rd.setItemDetail(item.toJSONString());
            rd.setCreateTime(new Timestamp(System.currentTimeMillis()));
            riskDataMapper.insert(rd);
            detailCollector.add(rd);
            count++;
        }
        return count;
    }

    /** 按API编码精确映射RiskData宽表字段 */
    private void mapRiskDataFields(CjbdiRiskData rd, JSONObject item, String apiCode) {
        switch (apiCode) {
            case "08": // 一般纳税人
                rd.setItemTitle(item.getString("name"));
                rd.setItemDate(item.getString("judge_date"));
                rd.setItemStatus(item.getString("taxpayer_status"));
                rd.setField1(item.getString("tax_num"));
                break;
            case "13": // 环保处罚
                rd.setItemTitle(item.getString("DOCUMENT_NUM"));
                rd.setItemDate(item.getString("PUNISH_DATE"));
                rd.setItemAmount(item.getString("PENALTY_AMOUNT"));
                rd.setItemAuthority(item.getString("PUNISH_ORG"));
                rd.setField1(item.getString("ENTNAME"));
                rd.setField2(item.getString("REASON_FOR_VIOLATION"));
                break;
            case "14": // 欠税公告
                rd.setItemTitle(item.getString("companyname"));
                rd.setItemDate(item.getString("publish_date"));
                rd.setItemAmount(item.getString("overdue_amount"));
                rd.setItemAuthority(item.getString("publish_department"));
                rd.setField1(item.getString("overdue_type"));
                break;
            case "15": // 动产抵押 - 实际字段: number, date/public_date, amount/debit_amount/gua_amount, department, status
                rd.setItemTitle(item.getString("number"));
                rd.setItemDate(getFirstNonNull(item, "date", "public_date"));
                rd.setItemAmount(getFirstNonNull(item, "debit_amount", "gua_amount", "amount"));
                rd.setItemStatus(item.getString("status"));
                rd.setItemAuthority(item.getString("department"));
                // 抵押权人从mortgagees数组取第一个
                JSONArray mortgagees = item.getJSONArray("mortgagees");
                if (mortgagees != null && !mortgagees.isEmpty()) {
                    rd.setField1(mortgagees.getJSONObject(0).getString("name"));
                }
                break;
            case "16": // 股权出质 - 实际字段: register_number, record_date, pledgor_amount, status, pledgor/pawnee
                rd.setItemTitle(getFirstNonNull(item, "register_number", "number"));
                rd.setItemDate(getFirstNonNull(item, "record_date", "public_date", "date"));
                rd.setItemAmount(getFirstNonNull(item, "pledgor_amount", "equity_amount", "amount"));
                rd.setItemStatus(item.getString("status"));
                rd.setItemAuthority(getFirstNonNull(item, "department", "register_department"));
                rd.setField1(getFirstNonNull(item, "pledgor", "pawnee"));
                break;
            case "17": // 股权冻结
                rd.setItemTitle(item.getString("object_company"));
                rd.setItemDate(item.getString("freeze_start_date"));
                rd.setItemAmount(item.getString("equity_amount"));
                rd.setItemStatus(item.getString("status"));
                rd.setItemAuthority(item.getString("court"));
                rd.setField1(item.getString("doc_number"));
                break;
            case "18": // 军采黑名单
                rd.setItemTitle(item.getString("supplyCn"));
                rd.setItemDate(item.getString("suspendAt"));
                rd.setItemStatus(item.getString("suspendStatus"));
                rd.setItemAuthority(item.getString("suspendOrg"));
                rd.setField1(item.getString("suspendCause"));
                break;
            case "19": // 政采黑名单
                rd.setItemTitle(item.getString("name"));
                rd.setItemDate(item.getString("punishAt"));
                rd.setItemAuthority(item.getString("enforceUnit"));
                rd.setField1(item.getString("illegalAct"));
                rd.setField2(item.getString("punishResult"));
                break;
            case "22": // 限制高消费
                rd.setItemTitle(item.getString("id"));
                rd.setItemDate(item.getString("fbrq"));
                rd.setItemAuthority(item.getString("zxfy"));
                rd.setField1(item.getString("ah"));
                break;
            case "23": // 不良记录
                rd.setItemTitle(item.getString("id"));
                rd.setItemStatus(item.getString("risk"));
                break;
            case "24": // 企业纠纷（按文档各 ajlx 共同字段映射）
                // cbaah=案号, dsarq=立案时间, cfymc=法院名称, claaymc=案由名称
                // cssdw=诉讼地位, cjafs=结案方式, nbdse=标的数额, _ajlx=案件类型（自补充）
                rd.setItemTitle(item.getString("cbaah"));
                rd.setItemDate(item.getString("dsarq"));
                rd.setItemAuthority(item.getString("cfymc"));
                rd.setField1(item.getString("claaymc"));
                rd.setField2(item.getString("cssdw"));
                rd.setField3(item.getString("cjafs"));
                rd.setItemAmount(item.getString("nbdse") != null ? item.getString("nbdse") : null);
                rd.setItemStatus(item.getString("_ajlx")); // 案件类型枚举
                break;
            default:
                rd.setItemTitle(getFirstNonNull(item, "title", "name", "punishNumber", "caseNo"));
                rd.setItemDate(getFirstNonNull(item, "date", "decisionDate", "publishDate", "regDate"));
                rd.setItemAmount(getFirstNonNull(item, "amount", "punishAmount", "taxAmount"));
                rd.setItemStatus(getFirstNonNull(item, "status", "punishStatus"));
                rd.setItemAuthority(getFirstNonNull(item, "authority", "departmentName", "courtName"));
                break;
        }
    }

    // ========== 辅助方法 ==========

    /** 从响应中提取 data.items[] */
    private JSONArray extractItems(JSONObject response) {
        Object dataObj = response.get("data");
        if (dataObj == null) return null;
        // data可能是JSONArray（直接数组）而非JSONObject（带分页包装）
        if (dataObj instanceof JSONArray) return null;
        if (!(dataObj instanceof JSONObject)) return null;
        JSONObject data = (JSONObject) dataObj;
        return data.getJSONArray("items");
    }

    /** 从响应中提取 data.records[] */
    private JSONArray extractRecords(JSONObject response) {
        Object dataObj = response.get("data");
        if (dataObj == null) return null;
        if (dataObj instanceof JSONArray) return null;
        if (!(dataObj instanceof JSONObject)) return null;
        JSONObject data = (JSONObject) dataObj;
        return data.getJSONArray("records");
    }

    /**
     * 从JSONObject中取第一个非空字段值
     */
    private String getFirstNonNull(JSONObject obj, String... keys) {
        for (String key : keys) {
            String val = obj.getString(key);
            if (val != null && !val.isEmpty()) return val;
        }
        return null;
    }

    /**
     * 获取企业最近一次查询结果
     */
    public CjbdiQueryResult getLatestResult(Long companyId, Long categoryId) {
        return queryResultMapper.selectOne(
                new LambdaQueryWrapper<CjbdiQueryResult>()
                        .eq(CjbdiQueryResult::getCompanyId, companyId)
                        .eq(CjbdiQueryResult::getCategoryId, categoryId)
                        .eq(CjbdiQueryResult::getStatus, 1)
                        .orderByDesc(CjbdiQueryResult::getQueryTime)
                        .last("FETCH FIRST 1 ROWS ONLY"));
    }

    /**
     * 获取企业某类别的历史查询记录
     */
    public List<CjbdiQueryResult> getQueryHistory(Long companyId, Long categoryId) {
        LambdaQueryWrapper<CjbdiQueryResult> wrapper = new LambdaQueryWrapper<CjbdiQueryResult>()
                .eq(CjbdiQueryResult::getCompanyId, companyId);
        if (categoryId != null) {
            wrapper.eq(CjbdiQueryResult::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(CjbdiQueryResult::getQueryTime);
        return queryResultMapper.selectList(wrapper);
    }

    /**
     * 企业名录搜索（调用CJBDI 02接口）
     * 返回企业基础列表，不调用03接口（03接口由前端按需触发）
     */
    public List<Map<String, Object>> searchEnterprise(String keyword) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("key", keyword);
            params.put("page", 1);
            params.put("size", 20);
            params.put("authorize", 1);

            // 获取企业名录类别(02)的独立凭证
            CjbdiDataCategory category02 = getCategoryByApiCode("02");
            String ticket02;
            String aesKey02;
            String apiUrl02;
            if (category02 != null && category02.getTicket() != null && !category02.getTicket().isEmpty()) {
                ticket02 = category02.getTicket();
                aesKey02 = category02.getAesKey();
                apiUrl02 = category02.getApiUrl();
            } else {
                ticket02 = cjbdiConfig.getTicket();
                aesKey02 = cjbdiConfig.getAesKey();
                apiUrl02 = null;
            }

            JSONObject response;
            if (apiUrl02 != null && !apiUrl02.isEmpty()) {
                response = httpClient.callFullUrlAsJson(apiUrl02, ticket02, aesKey02, JSON.toJSONString(params));
            } else {
                response = httpClient.callApiAsJson("/qyss/company/enterpriseListBasic", ticket02, aesKey02, params);
            }

            int code = response.getIntValue("code");
            if (code != 1000) {
                log.warn("企业名录搜索失败: {}", response.getString("msg"));
                return Collections.emptyList();
            }

            // 解析企业列表（02接口只有ENTNAME/CREDITCODE/NAME/ENTID/AREACODE）
            JSONArray dataArr = response.getJSONArray("data");
            if (dataArr == null) {
                JSONObject dataObj = response.getJSONObject("data");
                if (dataObj != null) {
                    dataArr = dataObj.getJSONArray("items");
                }
            }
            if (dataArr == null || dataArr.isEmpty()) {
                return Collections.emptyList();
            }

            List<Map<String, Object>> result = new ArrayList<>();
            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject item = dataArr.getJSONObject(i);
                Map<String, Object> enterprise = new LinkedHashMap<>();
                enterprise.put("entName", item.getString("ENTNAME"));
                enterprise.put("creditCode", item.getString("CREDITCODE"));
                enterprise.put("legalPerson", item.getString("NAME"));
                enterprise.put("entId", item.getString("ENTID"));
                result.add(enterprise);
            }
            return result;
        } catch (Exception e) {
            log.error("企业名录搜索异常 keyword={}", keyword, e);
            return Collections.emptyList();
        }
    }

    /**
     * 查询单个企业的工商基础信息（调用CJBDI 03接口）
     * 前端点击"工商数据"按钮时调用
     */
    public Map<String, Object> queryBusinessInfo(String companyName, String creditCode) {
        try {
            // 获取工商基础信息类别(03)的独立凭证
            CjbdiDataCategory category03 = getCategoryByApiCode("03");
            if (category03 == null) {
                log.warn("03工商基础信息类别未配置");
                return null;
            }
            String ticket03 = getTicketForCategory(category03);
            String aesKey03 = getAesKeyForCategory(category03);
            String apiUrl03 = category03.getApiUrl();

            // 构建请求参数
            Map<String, Object> reqParams = new HashMap<>();
            if (creditCode != null && !creditCode.isEmpty()) {
                reqParams.put("creditcode", creditCode);
            } else if (companyName != null && !companyName.isEmpty()) {
                reqParams.put("name", companyName);
            } else {
                return null;
            }
            reqParams.put("authorize", 1);

            // 调用03接口
            JSONObject resp;
            if (apiUrl03 != null && !apiUrl03.isEmpty()) {
                resp = httpClient.callFullUrlAsJson(apiUrl03, ticket03, aesKey03, JSON.toJSONString(reqParams));
            } else {
                resp = httpClient.callApiAsJson("/qyss/cr/queryEntInfo", ticket03, aesKey03, reqParams);
            }

            if (resp == null || resp.getIntValue("code") != 1000) {
                log.warn("查询工商基础信息失败: {}", resp != null ? resp.getString("msg") : "响应为空");
                return null;
            }

            // 解析完整响应数据
            JSONObject data = resp.getJSONObject("data");
            if (data == null) return null;

            // 解析BASIC子对象
            JSONObject basic = data.getJSONObject("BASIC");

            Map<String, Object> result = new LinkedHashMap<>();
            // 企业基本信息
            if (basic != null) {
                result.put("entName", basic.getString("ENTNAME"));
                result.put("creditCode", basic.getString("CREDITCODE"));
                result.put("legalPerson", basic.getString("FRNAME"));
                result.put("regCapital", basic.getString("REGCAP"));
                result.put("establishDate", basic.getString("ESDATE"));
                result.put("entStatus", basic.getString("ENTSTATUS"));
                result.put("entType", basic.getString("ENTTYPE"));
                result.put("industry", basic.getString("CEICATEGORYNAME1"));
                result.put("address", basic.getString("DOM"));
                result.put("businessScope", basic.getString("ZSOPSCOPE"));
                result.put("regAuthority", basic.getString("REGORG"));
                result.put("approvalDate", basic.getString("APPRDATE"));
                result.put("businessFrom", basic.getString("OPFROM"));
                result.put("businessTo", basic.getString("OPTO"));
                result.put("province", basic.getString("REGORGPROVINCE"));
                result.put("city", basic.getString("REGORGCITY"));
                result.put("district", basic.getString("REGORGDISTRICT"));
                result.put("orgCode", basic.getString("ORGCODES"));
                result.put("regNo", basic.getString("REGNO"));
                result.put("realCapital", basic.getString("CEIREALCAPITAL"));
                result.put("enName", basic.getString("CEIENNAME"));
                result.put("oldName", basic.getString("ENTNAME_OLD"));
            }

            // 主要管理人员
            JSONArray personArr = data.getJSONArray("PERSON");
            if (personArr != null && !personArr.isEmpty()) {
                List<Map<String, Object>> persons = new ArrayList<>();
                for (int i = 0; i < personArr.size(); i++) {
                    JSONObject p = personArr.getJSONObject(i);
                    Map<String, Object> person = new LinkedHashMap<>();
                    person.put("name", p.getString("PERNAME"));
                    person.put("position", p.getString("POSITION"));
                    person.put("startDate", p.getString("CEIINCLUDEDATE"));
                    person.put("endDate", p.getString("CEIREMOVEDATE"));
                    persons.add(person);
                }
                result.put("persons", persons);
            }

            // 股东及出资信息
            JSONArray shareholderArr = data.getJSONArray("SHAREHOLDER");
            if (shareholderArr != null && !shareholderArr.isEmpty()) {
                List<Map<String, Object>> shareholders = new ArrayList<>();
                for (int i = 0; i < shareholderArr.size(); i++) {
                    JSONObject s = shareholderArr.getJSONObject(i);
                    Map<String, Object> sh = new LinkedHashMap<>();
                    sh.put("name", s.getString("SHANAME"));
                    sh.put("type", s.getString("INVTYPE"));
                    sh.put("subAmount", s.getString("SUBCONAM"));
                    sh.put("actualAmount", s.getString("ACCONAM"));
                    sh.put("ratio", s.getString("FUNDEDRATIO"));
                    shareholders.add(sh);
                }
                result.put("shareholders", shareholders);
            }

            // 变更信息
            JSONArray alterArr = data.getJSONArray("ALTER");
            if (alterArr != null && !alterArr.isEmpty()) {
                List<Map<String, Object>> alters = new ArrayList<>();
                for (int i = 0; i < Math.min(alterArr.size(), 10); i++) {
                    JSONObject a = alterArr.getJSONObject(i);
                    Map<String, Object> alt = new LinkedHashMap<>();
                    alt.put("item", a.getString("ALTITEM"));
                    alt.put("date", a.getString("ALTDATE"));
                    alt.put("before", a.getString("ALTBE"));
                    alt.put("after", a.getString("ALTAF"));
                    alters.add(alt);
                }
                result.put("alters", alters);
            }

            // 对外投资
            JSONArray entInvArr = data.getJSONArray("ENTINV");
            if (entInvArr != null && !entInvArr.isEmpty()) {
                List<Map<String, Object>> investments = new ArrayList<>();
                for (int i = 0; i < entInvArr.size(); i++) {
                    JSONObject inv = entInvArr.getJSONObject(i);
                    Map<String, Object> investment = new LinkedHashMap<>();
                    investment.put("entName", inv.getString("ENTJGNAME"));
                    investment.put("legalPerson", inv.getString("NAME"));
                    investment.put("regCapital", inv.getString("REGCAP"));
                    investment.put("status", inv.getString("ENTSTATUS"));
                    investment.put("investAmount", inv.getString("SUBCONAM"));
                    investment.put("ratio", inv.getString("FUNDEDRATIO"));
                    investment.put("esDate", inv.getString("ESDATE"));
                    investments.add(investment);
                }
                result.put("investments", investments);
            }

            // 分支机构
            JSONArray filiationArr = data.getJSONArray("FILIATION");
            if (filiationArr != null && !filiationArr.isEmpty()) {
                List<Map<String, Object>> branches = new ArrayList<>();
                for (int i = 0; i < filiationArr.size(); i++) {
                    JSONObject f = filiationArr.getJSONObject(i);
                    Map<String, Object> branch = new LinkedHashMap<>();
                    branch.put("name", f.getString("BRNAME"));
                    branch.put("creditCode", f.getString("BRN_CREDIT_CODE"));
                    branch.put("regOrg", f.getString("BRN_REG_ORG"));
                    branch.put("status", f.getString("CEIREGISTERSTATUS"));
                    branch.put("esDate", f.getString("CEIESTABLISHDATE"));
                    branches.add(branch);
                }
                result.put("branches", branches);
            }

            // 经营异常信息
            JSONArray abnormalArr = data.getJSONArray("ABNORMAL");
            if (abnormalArr != null && !abnormalArr.isEmpty()) {
                List<Map<String, Object>> abnormals = new ArrayList<>();
                for (int i = 0; i < abnormalArr.size(); i++) {
                    JSONObject ab = abnormalArr.getJSONObject(i);
                    Map<String, Object> abnormal = new LinkedHashMap<>();
                    abnormal.put("putReason", ab.getString("putReason"));
                    abnormal.put("putDate", ab.getString("putDate"));
                    abnormal.put("putDepartment", ab.getString("putDepartment"));
                    abnormal.put("removeReason", ab.getString("removeReason"));
                    abnormal.put("removeDate", ab.getString("removeDate"));
                    abnormals.add(abnormal);
                }
                result.put("abnormals", abnormals);
            }

            // 严重违法信息
            JSONArray illegalArr = data.getJSONArray("ILLEGAL");
            if (illegalArr != null && !illegalArr.isEmpty()) {
                List<Map<String, Object>> illegals = new ArrayList<>();
                for (int i = 0; i < illegalArr.size(); i++) {
                    JSONObject il = illegalArr.getJSONObject(i);
                    Map<String, Object> illegal = new LinkedHashMap<>();
                    illegal.put("type", il.getString("illegalType"));
                    illegal.put("putReason", il.getString("putReason"));
                    illegal.put("putDate", il.getString("putDate"));
                    illegal.put("putDepartment", il.getString("putDepartment"));
                    illegal.put("removeReason", il.getString("removeReason"));
                    illegal.put("removeDate", il.getString("removeDate"));
                    illegals.add(illegal);
                }
                result.put("illegals", illegals);
            }

            return result;
        } catch (Exception e) {
            log.error("查询工商基础信息异常 company={}", companyName, e);
            return null;
        }
    }

    // ========== 凭证管理辅助方法 ==========

    /**
     * 获取类别对应的ticket（优先类别表配置，回退全局配置）
     */
    private String getTicketForCategory(CjbdiDataCategory category) {
        if (category.getTicket() != null && !category.getTicket().isEmpty()) {
            return category.getTicket();
        }
        return cjbdiConfig.getTicket();
    }

    /**
     * 获取类别对应的AES密钥（优先类别表配置，回退全局配置）
     */
    private String getAesKeyForCategory(CjbdiDataCategory category) {
        if (category.getAesKey() != null && !category.getAesKey().isEmpty()) {
            return category.getAesKey();
        }
        return cjbdiConfig.getAesKey();
    }

    /**
     * 根据API编码获取数据类别
     */
    public CjbdiDataCategory getCategoryByApiCode(String apiCode) {
        return categoryMapper.selectOne(
                new LambdaQueryWrapper<CjbdiDataCategory>()
                        .eq(CjbdiDataCategory::getApiCode, apiCode)
                        .eq(CjbdiDataCategory::getIsEnabled, 1)
                        .last("FETCH FIRST 1 ROWS ONLY"));
    }
}

