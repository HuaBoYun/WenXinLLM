package com.huabo.fxgl.service.sync;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DataModelSyncHelper 单元测试
 * 覆盖：字段映射、跳过每组最后一个指标、来源类型判定、幂等键生成。
 *
 * @author 华博云
 * @since 2026-08-01 (#TASK-2026-08-01-DATA-MODEL-SYNC)
 */
@DisplayName("组合指标 → 数据模型 同步 Helper 测试")
class DataModelSyncHelperTest {

    // ================ 1. 字段映射测试 ================

    @Test
    @DisplayName("mapIndicatorToDataModel: 常规字段应原封不动映射")
    void testMapIndicatorToDataModel_basicFields() {
        Map<String, Object> indicator = new HashMap<>();
        indicator.put("configId", "CFG001");
        indicator.put("indicatorId", "IND001");
        indicator.put("indicatorCode", "PROC_CHECK");
        indicator.put("indicatorName", "采购金额稽核");
        indicator.put("sqlContent", "SELECT * FROM PO_ORDER WHERE AMOUNT > 100000");
        indicator.put("description", "月度采购金额异常检查");
        indicator.put("dataSourceId", "DS001");
        indicator.put("isEnabled", Boolean.TRUE);

        Map<String, Object> combination = new HashMap<>();
        combination.put("combinationId", "COMB001");
        combination.put("category", "FINANCE");

        Map<String, Object> model = DataModelSyncHelper.mapIndicatorToDataModel(indicator, combination);

        assertEquals("PROC_CHECK", model.get("modelCode"));
        assertEquals("采购金额稽核", model.get("modelName"));
        assertEquals("SELECT * FROM PO_ORDER WHERE AMOUNT > 100000", model.get("sqlStatement"));
        assertEquals("月度采购金额异常检查", model.get("businessMeaning"));
        assertEquals("DS001", model.get("dataSourceId"));
        assertEquals("Y", model.get("isEnabled"));
        assertEquals("COMBINATION_SYNC", model.get("sourceType"));
        assertEquals("COMB001", model.get("sourceCombinationId"));
        assertEquals("CFG001", model.get("sourceIndicatorConfigId"));
        assertEquals("IND001", model.get("sourceIndicatorId"));
    }

    @Test
    @DisplayName("mapIndicatorToDataModel: category=FINANCE 映射为 FINANCIAL")
    void testMapIndicatorToDataModel_modelType_finance() {
        Map<String, Object> ind = baseIndicator();
        Map<String, Object> comb = new HashMap<>();
        comb.put("combinationId", "C1");
        comb.put("category", "FINANCE");
        assertEquals("FINANCIAL", DataModelSyncHelper.mapIndicatorToDataModel(ind, comb).get("modelType"));
    }

    @Test
    @DisplayName("mapIndicatorToDataModel: category=RISK 保持为 RISK")
    void testMapIndicatorToDataModel_modelType_risk() {
        Map<String, Object> ind = baseIndicator();
        Map<String, Object> comb = new HashMap<>();
        comb.put("combinationId", "C1");
        comb.put("category", "RISK");
        assertEquals("RISK", DataModelSyncHelper.mapIndicatorToDataModel(ind, comb).get("modelType"));
    }

    @Test
    @DisplayName("mapIndicatorToDataModel: 其他 category 映射为 BUSINESS")
    void testMapIndicatorToDataModel_modelType_default() {
        Map<String, Object> ind = baseIndicator();
        Map<String, Object> comb = new HashMap<>();
        comb.put("combinationId", "C1");
        comb.put("category", "PROCUREMENT");
        assertEquals("BUSINESS", DataModelSyncHelper.mapIndicatorToDataModel(ind, comb).get("modelType"));
    }

    @Test
    @DisplayName("mapIndicatorToDataModel: parameterMapping 应被序列化为 JSON 字符串")
    void testMapIndicatorToDataModel_parameterMappingSerialization() {
        Map<String, Object> ind = baseIndicator();
        Map<String, Object> pm = new LinkedHashMap<>();
        pm.put("param1", "value1");
        pm.put("param2", 100);
        ind.put("parameterMapping", pm);

        Map<String, Object> comb = new HashMap<>();
        comb.put("combinationId", "C1");
        comb.put("category", "FINANCE");

        Object result = DataModelSyncHelper.mapIndicatorToDataModel(ind, comb).get("parameterConfig");
        assertNotNull(result);
        JSONObject parsed = JSON.parseObject(result.toString());
        assertEquals("value1", parsed.getString("param1"));
        assertEquals(Integer.valueOf(100), parsed.getInteger("param2"));
    }

    @Test
    @DisplayName("mapIndicatorToDataModel: isEnabled=false 映射为 'N'")
    void testMapIndicatorToDataModel_isEnabledFalse() {
        Map<String, Object> ind = baseIndicator();
        ind.put("isEnabled", Boolean.FALSE);
        Map<String, Object> comb = new HashMap<>();
        comb.put("combinationId", "C1");
        comb.put("category", "FINANCE");
        assertEquals("N", DataModelSyncHelper.mapIndicatorToDataModel(ind, comb).get("isEnabled"));
    }

    @Test
    @DisplayName("mapIndicatorToDataModel: 同步后默认状态为 DRAFT")
    void testMapIndicatorToDataModel_defaultDraft() {
        Map<String, Object> ind = baseIndicator();
        Map<String, Object> comb = new HashMap<>();
        comb.put("combinationId", "C1");
        comb.put("category", "FINANCE");
        assertEquals("DRAFT", DataModelSyncHelper.mapIndicatorToDataModel(ind, comb).get("status"));
    }

    // ================ 2. "跳过每组最后一个指标" 关键规则 ================

    @Test
    @DisplayName("filterSyncableIndicators: 3 个指标应保留前 2 个")
    void testFilterSyncable_threeIndicators() {
        List<Map<String, Object>> indicators = buildIndicators(3);
        List<Map<String, Object>> result = DataModelSyncHelper.filterSyncableIndicators(indicators);
        assertEquals(2, result.size());
        assertEquals("CFG_1", result.get(0).get("configId"));
        assertEquals("CFG_2", result.get(1).get("configId"));
    }

    @Test
    @DisplayName("filterSyncableIndicators: 只有 1 个指标应返回空列表")
    void testFilterSyncable_singleIndicator() {
        List<Map<String, Object>> indicators = buildIndicators(1);
        assertTrue(DataModelSyncHelper.filterSyncableIndicators(indicators).isEmpty());
    }

    @Test
    @DisplayName("filterSyncableIndicators: 空列表应返回空列表")
    void testFilterSyncable_emptyList() {
        assertTrue(DataModelSyncHelper.filterSyncableIndicators(new ArrayList<>()).isEmpty());
    }

    @Test
    @DisplayName("filterSyncableIndicators: null 应安全返回空列表")
    void testFilterSyncable_null() {
        assertTrue(DataModelSyncHelper.filterSyncableIndicators(null).isEmpty());
    }

    @Test
    @DisplayName("filterSyncableIndicators: 应按 executionOrder 升序后跳过尾部")
    void testFilterSyncable_orderRespected() {
        List<Map<String, Object>> indicators = new ArrayList<>();
        indicators.add(indicator("CFG_3", 3));
        indicators.add(indicator("CFG_1", 1));
        indicators.add(indicator("CFG_2", 2));
        List<Map<String, Object>> result = DataModelSyncHelper.filterSyncableIndicators(indicators);
        assertEquals(2, result.size());
        // 应按顺序保留 order=1、order=2，丢弃 order=3
        assertEquals("CFG_1", result.get(0).get("configId"));
        assertEquals("CFG_2", result.get(1).get("configId"));
    }

    // ================ 3. 来源类型判定 ================

    @Test
    @DisplayName("resolveSourceType: templateId 有值时返回 TEMPLATE")
    void testResolveSourceType_template() {
        assertEquals("TEMPLATE", DataModelSyncHelper.resolveSourceType("TPL001", null));
    }

    @Test
    @DisplayName("resolveSourceType: combinationConfigId 有值时返回 COMBINATION_SYNC")
    void testResolveSourceType_combination() {
        assertEquals("COMBINATION_SYNC", DataModelSyncHelper.resolveSourceType(null, "CFG001"));
    }

    @Test
    @DisplayName("resolveSourceType: 都为空时返回 MANUAL")
    void testResolveSourceType_manual() {
        assertEquals("MANUAL", DataModelSyncHelper.resolveSourceType(null, null));
        assertEquals("MANUAL", DataModelSyncHelper.resolveSourceType("", ""));
    }

    // ================ 4. modelCode 长度截断 ================

    @Test
    @DisplayName("normalizeModelCode: 超长 code 应截断到 50 字符")
    void testNormalizeModelCode_truncate() {
        String longCode = "AVERYLONGINDICATORCODENAME_ABCDEFGHIJKLMNOPQRSTUVWXYZ_1234567890";
        String result = DataModelSyncHelper.normalizeModelCode(longCode);
        assertTrue(result.length() <= 50);
        assertTrue(longCode.startsWith(result.substring(0, Math.min(20, result.length()))));
    }

    @Test
    @DisplayName("normalizeModelCode: 正常长度 code 保持不变")
    void testNormalizeModelCode_keep() {
        assertEquals("PROC_CHECK", DataModelSyncHelper.normalizeModelCode("PROC_CHECK"));
    }

    // ================ helpers ================

    private Map<String, Object> baseIndicator() {
        Map<String, Object> m = new HashMap<>();
        m.put("configId", "CFG_X");
        m.put("indicatorId", "IND_X");
        m.put("indicatorCode", "CODE_X");
        m.put("indicatorName", "指标X");
        m.put("sqlContent", "SELECT 1 FROM DUAL");
        m.put("isEnabled", Boolean.TRUE);
        return m;
    }

    private List<Map<String, Object>> buildIndicators(int count) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(indicator("CFG_" + i, i));
        }
        return list;
    }

    private Map<String, Object> indicator(String configId, int order) {
        Map<String, Object> m = new HashMap<>();
        m.put("configId", configId);
        m.put("indicatorCode", "CODE_" + configId);
        m.put("indicatorName", "指标" + configId);
        m.put("sqlContent", "SELECT 1 FROM DUAL");
        m.put("executionOrder", order);
        m.put("isEnabled", Boolean.TRUE);
        return m;
    }
}
