package com.financial.sharing.dataCollection.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.dataCollection.entity.TblMappingRule;
import com.financial.sharing.dataCollection.service.MappingRuleTestService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 映射规则测试Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@RestController
@RequestMapping("/financialSharing/mappingRuleTest/api")
public class MappingRuleTestController {

    @Autowired
    private MappingRuleTestService mappingRuleTestService;

    /**
     * 测试字段映射
     *
     * @param mappingRule 映射规则
     * @param testData 测试数据
     * @return 测试结果
     */
    @PostMapping("/testFieldMapping")
    public MyJsonBean testFieldMapping(@RequestBody Map<String, Object> params) {
        try {
            TblMappingRule mappingRule = (TblMappingRule) params.get("mappingRule");
            Map<String, Object> testData = (Map<String, Object>) params.get("testData");
            
            return mappingRuleTestService.testFieldMapping(mappingRule, testData);
        } catch (Exception e) {
            log.error("测试字段映射失败", e);
            return MyJsonBean.errorData("测试字段映射失败：" + e.getMessage());
        }
    }

    /**
     * 测试转换规则
     *
     * @param mappingRule 映射规则
     * @param testData 测试数据
     * @return 测试结果
     */
    @PostMapping("/testTransformRules")
    public MyJsonBean testTransformRules(@RequestBody Map<String, Object> params) {
        try {
            TblMappingRule mappingRule = (TblMappingRule) params.get("mappingRule");
            Map<String, Object> testData = (Map<String, Object>) params.get("testData");
            
            return mappingRuleTestService.testTransformRules(mappingRule, testData);
        } catch (Exception e) {
            log.error("测试转换规则失败", e);
            return MyJsonBean.errorData("测试转换规则失败：" + e.getMessage());
        }
    }

    /**
     * 测试校验规则
     *
     * @param mappingRule 映射规则
     * @param testData 测试数据
     * @return 测试结果
     */
    @PostMapping("/testValidationRules")
    public MyJsonBean testValidationRules(@RequestBody Map<String, Object> params) {
        try {
            TblMappingRule mappingRule = (TblMappingRule) params.get("mappingRule");
            Map<String, Object> testData = (Map<String, Object>) params.get("testData");
            
            return mappingRuleTestService.testValidationRules(mappingRule, testData);
        } catch (Exception e) {
            log.error("测试校验规则失败", e);
            return MyJsonBean.errorData("测试校验规则失败：" + e.getMessage());
        }
    }

    /**
     * 测试过滤条件
     *
     * @param mappingRule 映射规则
     * @param testData 测试数据
     * @return 测试结果
     */
    @PostMapping("/testFilterCondition")
    public MyJsonBean testFilterCondition(@RequestBody Map<String, Object> params) {
        try {
            TblMappingRule mappingRule = (TblMappingRule) params.get("mappingRule");
            Map<String, Object> testData = (Map<String, Object>) params.get("testData");
            
            return mappingRuleTestService.testFilterCondition(mappingRule, testData);
        } catch (Exception e) {
            log.error("测试过滤条件失败", e);
            return MyJsonBean.errorData("测试过滤条件失败：" + e.getMessage());
        }
    }

    /**
     * 完整规则测试
     *
     * @param ruleId 规则ID
     * @param testData 测试数据
     * @return 测试结果
     */
    @PostMapping("/testCompleteRule")
    public MyJsonBean testCompleteRule(@RequestBody Map<String, Object> params) {
        try {
            String ruleId = (String) params.get("ruleId");
            Map<String, Object> testData = (Map<String, Object>) params.get("testData");
            String orgId = UserUtils.getOrgId();
            
            return mappingRuleTestService.testCompleteRule(ruleId, orgId, testData);
        } catch (Exception e) {
            log.error("完整规则测试失败", e);
            return MyJsonBean.errorData("完整规则测试失败：" + e.getMessage());
        }
    }

    /**
     * 批量测试规则
     *
     * @param ruleIds 规则ID列表
     * @param testData 测试数据
     * @return 测试结果
     */
    @PostMapping("/batchTestRules")
    public MyJsonBean batchTestRules(@RequestBody Map<String, Object> params) {
        try {
            String[] ruleIds = (String[]) params.get("ruleIds");
            Map<String, Object> testData = (Map<String, Object>) params.get("testData");
            String orgId = UserUtils.getOrgId();
            
            return mappingRuleTestService.batchTestRules(ruleIds, orgId, testData);
        } catch (Exception e) {
            log.error("批量测试规则失败", e);
            return MyJsonBean.errorData("批量测试规则失败：" + e.getMessage());
        }
    }

    /**
     * 预览映射结果
     *
     * @param ruleId 规则ID
     * @param limit 预览记录数
     * @return 预览结果
     */
    @PostMapping("/previewMappingResult")
    public MyJsonBean previewMappingResult(@RequestBody Map<String, Object> params) {
        try {
            String ruleId = (String) params.get("ruleId");
            Integer limit = params.get("limit") != null ? (Integer) params.get("limit") : 10;
            String orgId = UserUtils.getOrgId();
            
            return mappingRuleTestService.previewMappingResult(ruleId, orgId, limit);
        } catch (Exception e) {
            log.error("预览映射结果失败", e);
            return MyJsonBean.errorData("预览映射结果失败：" + e.getMessage());
        }
    }
}

