package com.financial.sharing.dataCollection.service;

import com.financial.sharing.dataCollection.entity.TblMappingRule;
import com.financial.sharing.util.MyJsonBean;

import java.util.Map;

/**
 * 映射规则测试Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface MappingRuleTestService {

    /**
     * 测试字段映射
     *
     * @param mappingRule 映射规则
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean testFieldMapping(TblMappingRule mappingRule, Map<String, Object> testData);

    /**
     * 测试转换规则
     *
     * @param mappingRule 映射规则
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean testTransformRules(TblMappingRule mappingRule, Map<String, Object> testData);

    /**
     * 测试校验规则
     *
     * @param mappingRule 映射规则
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean testValidationRules(TblMappingRule mappingRule, Map<String, Object> testData);

    /**
     * 测试过滤条件
     *
     * @param mappingRule 映射规则
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean testFilterCondition(TblMappingRule mappingRule, Map<String, Object> testData);

    /**
     * 完整规则测试
     *
     * @param ruleId 规则ID
     * @param orgId 组织ID
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean testCompleteRule(String ruleId, String orgId, Map<String, Object> testData);

    /**
     * 批量测试规则
     *
     * @param ruleIds 规则ID列表
     * @param orgId 组织ID
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean batchTestRules(String[] ruleIds, String orgId, Map<String, Object> testData);

    /**
     * 预览映射结果
     *
     * @param ruleId 规则ID
     * @param orgId 组织ID
     * @param limit 预览记录数
     * @return 预览结果
     */
    MyJsonBean previewMappingResult(String ruleId, String orgId, Integer limit);
}

