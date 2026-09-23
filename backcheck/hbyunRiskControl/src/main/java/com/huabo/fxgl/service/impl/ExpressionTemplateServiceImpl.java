package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.service.ExpressionTemplateService;
import com.huabo.fxgl.service.ChineseFormulaParsingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 表达式模板管理服务实现类
 * 
 * @author AI Assistant
 * @date 2025-01-21
 * @version 1.0
 */
@Service
public class ExpressionTemplateServiceImpl implements ExpressionTemplateService {
    
    private static final Logger log = LoggerFactory.getLogger(ExpressionTemplateServiceImpl.class);
    
    @Autowired
    private ChineseFormulaParsingService chineseFormulaParsingService;
    
    // 模拟数据存储，实际项目中应该使用数据库
    private static final List<Map<String, Object>> TEMPLATE_DATA = new ArrayList<>();
    
    static {
        // 初始化预置模板数据
        initializeTemplateData();
    }
    
    @Override
    public List<Map<String, Object>> getChineseFormulaTemplates(String category) {
        log.info("获取中文公式模板列表，分类: {}", category);
        
        List<Map<String, Object>> templates = new ArrayList<>(TEMPLATE_DATA);
        
        // 按分类过滤
        if (StringUtils.hasText(category)) {
            templates = templates.stream()
                    .filter(template -> category.equals(template.get("templateCategory")))
                    .collect(Collectors.toList());
        }
        
        // 按使用次数排序
        templates.sort((t1, t2) -> {
            Integer usage1 = (Integer) t1.getOrDefault("usageCount", 0);
            Integer usage2 = (Integer) t2.getOrDefault("usageCount", 0);
            return usage2.compareTo(usage1);
        });
        
        return templates;
    }
    
    @Override
    public Map<String, Object> getTemplateById(String templateId) {
        log.info("获取模板详情，ID: {}", templateId);
        
        return TEMPLATE_DATA.stream()
                .filter(template -> templateId.equals(template.get("templateId")))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public Map<String, Object> saveChineseFormulaTemplate(Map<String, Object> templateData) {
        log.info("保存中文公式模板: {}", templateData.get("templateName"));
        
        try {
            // 验证必填字段
            if (!validateTemplateData(templateData)) {
                return createErrorResult("模板数据验证失败");
            }
            
            // 生成模板ID
            String templateId = "TEMPLATE_" + System.currentTimeMillis();
            templateData.put("templateId", templateId);
            templateData.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            templateData.put("usageCount", 0);
            templateData.put("isEnabled", true);
            
            // 验证中文公式
            String chineseFormula = (String) templateData.get("chineseFormula");
            Map<String, Object> validationResult = chineseFormulaParsingService.validateChineseFormula(chineseFormula);
            
            if (!(Boolean) validationResult.get("valid")) {
                return createErrorResult("中文公式验证失败: " + validationResult.get("message"));
            }
            
            // 保存模板
            TEMPLATE_DATA.add(templateData);
            
            return createSuccessResult("模板保存成功", templateData);
            
        } catch (Exception e) {
            log.error("保存模板失败", e);
            return createErrorResult("保存模板失败: " + e.getMessage());
        }
    }
    
    @Override
    public void incrementTemplateUsage(String templateId) {
        log.info("增加模板使用次数，ID: {}", templateId);
        
        TEMPLATE_DATA.stream()
                .filter(template -> templateId.equals(template.get("templateId")))
                .findFirst()
                .ifPresent(template -> {
                    Integer currentUsage = (Integer) template.getOrDefault("usageCount", 0);
                    template.put("usageCount", currentUsage + 1);
                    template.put("lastUsedTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                });
    }
    
    @Override
    public List<Map<String, Object>> getPopularTemplates(int limit) {
        log.info("获取热门模板，限制数量: {}", limit);
        
        return TEMPLATE_DATA.stream()
                .filter(template -> (Boolean) template.getOrDefault("isEnabled", true))
                .sorted((t1, t2) -> {
                    Integer usage1 = (Integer) t1.getOrDefault("usageCount", 0);
                    Integer usage2 = (Integer) t2.getOrDefault("usageCount", 0);
                    return usage2.compareTo(usage1);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Map<String, Object>> searchTemplates(String keyword, String category) {
        log.info("搜索模板，关键词: {}, 分类: {}", keyword, category);
        
        return TEMPLATE_DATA.stream()
                .filter(template -> {
                    // 分类过滤
                    if (StringUtils.hasText(category) && !category.equals(template.get("templateCategory"))) {
                        return false;
                    }
                    
                    // 关键词过滤
                    if (StringUtils.hasText(keyword)) {
                        String templateName = (String) template.get("templateName");
                        String description = (String) template.get("description");
                        String chineseFormula = (String) template.get("chineseFormula");
                        
                        return (templateName != null && templateName.contains(keyword)) ||
                               (description != null && description.contains(keyword)) ||
                               (chineseFormula != null && chineseFormula.contains(keyword));
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean deleteTemplate(String templateId) {
        log.info("删除模板，ID: {}", templateId);
        
        return TEMPLATE_DATA.removeIf(template -> templateId.equals(template.get("templateId")));
    }
    
    @Override
    public boolean toggleTemplateStatus(String templateId, boolean enabled) {
        log.info("切换模板状态，ID: {}, 启用: {}", templateId, enabled);
        
        Optional<Map<String, Object>> templateOpt = TEMPLATE_DATA.stream()
                .filter(template -> templateId.equals(template.get("templateId")))
                .findFirst();
        
        if (templateOpt.isPresent()) {
            templateOpt.get().put("isEnabled", enabled);
            return true;
        }
        
        return false;
    }
    
    @Override
    public List<Map<String, Object>> getTemplateCategories() {
        log.info("获取模板分类列表");
        
        List<Map<String, Object>> categories = new ArrayList<>();
        categories.add(createCategory("FINANCIAL_AUDIT", "财务审计", "财务相关的审计规则模板"));
        categories.add(createCategory("RISK_CONTROL", "风险控制", "风险识别和控制模板"));
        categories.add(createCategory("COMPLIANCE_CHECK", "合规检查", "合规性检查模板"));
        categories.add(createCategory("DATA_VALIDATION", "数据验证", "数据质量验证模板"));
        
        return categories;
    }
    
    @Override
    public Map<String, Object> validateTemplate(String chineseFormula, String sqlTemplate) {
        log.info("验证模板公式");
        
        try {
            // 验证中文公式
            Map<String, Object> chineseValidation = chineseFormulaParsingService.validateChineseFormula(chineseFormula);
            
            if (!(Boolean) chineseValidation.get("valid")) {
                return createErrorResult("中文公式验证失败: " + chineseValidation.get("message"));
            }
            
            // 验证SQL模板（简单检查）
            if (!StringUtils.hasText(sqlTemplate)) {
                return createErrorResult("SQL模板不能为空");
            }
            
            return createSuccessResult("模板验证通过", null);
            
        } catch (Exception e) {
            log.error("模板验证失败", e);
            return createErrorResult("模板验证失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<String, Object> exportTemplates(List<String> templateIds) {
        log.info("导出模板，数量: {}", templateIds.size());
        
        List<Map<String, Object>> exportData = TEMPLATE_DATA.stream()
                .filter(template -> templateIds.contains(template.get("templateId")))
                .collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("templates", exportData);
        result.put("exportTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        result.put("count", exportData.size());
        
        return createSuccessResult("导出成功", result);
    }
    
    @Override
    public Map<String, Object> importTemplates(List<Map<String, Object>> templateData) {
        log.info("导入模板，数量: {}", templateData.size());
        
        int successCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Map<String, Object> template : templateData) {
            try {
                Map<String, Object> saveResult = saveChineseFormulaTemplate(template);
                if ((Boolean) saveResult.get("success")) {
                    successCount++;
                } else {
                    errors.add("模板 " + template.get("templateName") + " 导入失败: " + saveResult.get("message"));
                }
            } catch (Exception e) {
                errors.add("模板 " + template.get("templateName") + " 导入异常: " + e.getMessage());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("totalCount", templateData.size());
        result.put("errors", errors);
        
        return createSuccessResult("导入完成", result);
    }
    
    // 私有辅助方法
    private boolean validateTemplateData(Map<String, Object> templateData) {
        return StringUtils.hasText((String) templateData.get("templateName")) &&
               StringUtils.hasText((String) templateData.get("templateCategory")) &&
               StringUtils.hasText((String) templateData.get("chineseFormula"));
    }
    
    private Map<String, Object> createSuccessResult(String message, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);
        result.put("data", data);
        return result;
    }
    
    private Map<String, Object> createErrorResult(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        result.put("data", null);
        return result;
    }
    
    private Map<String, Object> createCategory(String code, String name, String description) {
        Map<String, Object> category = new HashMap<>();
        category.put("code", code);
        category.put("name", name);
        category.put("description", description);
        return category;
    }
    
    private static void initializeTemplateData() {
        // 财务审计模板
        Map<String, Object> template1 = new HashMap<>();
        template1.put("templateId", "TEMPLATE_FA_001");
        template1.put("templateCode", "FA_AMOUNT_THRESHOLD");
        template1.put("templateName", "金额异常检测");
        template1.put("templateCategory", "FINANCIAL_AUDIT");
        template1.put("businessScenario", "检测交易金额超过阈值的异常情况");
        template1.put("chineseFormula", "金额 大于 {threshold_amount}");
        template1.put("sqlTemplate", "AMOUNT > {threshold_amount}");
        template1.put("description", "用于检测大额交易或异常金额");
        template1.put("usageCount", 15);
        template1.put("isEnabled", true);
        template1.put("createTime", "2025-01-21T10:00:00");
        TEMPLATE_DATA.add(template1);
        
        // 风险控制模板
        Map<String, Object> template2 = new HashMap<>();
        template2.put("templateId", "TEMPLATE_RC_001");
        template2.put("templateCode", "RC_CREDIT_RISK");
        template2.put("templateName", "信用风险评估");
        template2.put("templateCategory", "RISK_CONTROL");
        template2.put("businessScenario", "评估客户信用风险等级");
        template2.put("chineseFormula", "信用评分 小于 {min_score} 或者 逾期次数 大于 {max_overdue}");
        template2.put("sqlTemplate", "CREDIT_SCORE < {min_score} OR OVERDUE_COUNT > {max_overdue}");
        template2.put("description", "综合评估客户信用风险");
        template2.put("usageCount", 8);
        template2.put("isEnabled", true);
        template2.put("createTime", "2025-01-21T10:30:00");
        TEMPLATE_DATA.add(template2);
        
        // 合规检查模板
        Map<String, Object> template3 = new HashMap<>();
        template3.put("templateId", "TEMPLATE_CC_001");
        template3.put("templateCode", "CC_REGULATORY_REPORT");
        template3.put("templateName", "监管报告验证");
        template3.put("templateCategory", "COMPLIANCE_CHECK");
        template3.put("businessScenario", "验证监管报告数据完整性");
        template3.put("chineseFormula", "报告状态 等于 已完成 并且 数据完整性 等于 完整 并且 提交时间 不为空");
        template3.put("sqlTemplate", "REPORT_STATUS = 'COMPLETED' AND DATA_COMPLETENESS = 'FULL' AND SUBMIT_TIME IS NOT NULL");
        template3.put("description", "确保监管报告符合要求");
        template3.put("usageCount", 12);
        template3.put("isEnabled", true);
        template3.put("createTime", "2025-01-21T11:00:00");
        TEMPLATE_DATA.add(template3);
    }

    @Override
    public List<Map<String, Object>> getTemplateList(Map<String, Object> params) {
        log.info("获取模板列表，参数: {}", params);

        try {
            List<Map<String, Object>> templates = new ArrayList<>(TEMPLATE_DATA);

            // 分类过滤
            String category = (String) params.get("category");
            if (StringUtils.hasText(category)) {
                templates = templates.stream()
                        .filter(template -> category.equals(template.get("templateCategory")))
                        .collect(Collectors.toList());
            }

            // 关键词搜索
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) {
                templates = templates.stream()
                        .filter(template -> {
                            String templateName = (String) template.get("templateName");
                            String description = (String) template.get("description");
                            String chineseFormula = (String) template.get("chineseFormula");

                            return (templateName != null && templateName.contains(keyword)) ||
                                   (description != null && description.contains(keyword)) ||
                                   (chineseFormula != null && chineseFormula.contains(keyword));
                        })
                        .collect(Collectors.toList());
            }

            // 状态过滤
            Boolean isEnabled = (Boolean) params.get("isEnabled");
            if (isEnabled != null) {
                templates = templates.stream()
                        .filter(template -> isEnabled.equals(template.get("isEnabled")))
                        .collect(Collectors.toList());
            }

            // 排序
            String sortBy = (String) params.getOrDefault("sortBy", "usageCount");
            String sortOrder = (String) params.getOrDefault("sortOrder", "desc");

            templates.sort((t1, t2) -> {
                Comparable value1 = getComparableValue(t1, sortBy);
                Comparable value2 = getComparableValue(t2, sortBy);

                if (value1 == null && value2 == null) return 0;
                if (value1 == null) return 1;
                if (value2 == null) return -1;

                int result = value1.compareTo(value2);
                return "desc".equals(sortOrder) ? -result : result;
            });

            // 分页
            Integer pageNum = (Integer) params.get("pageNum");
            Integer pageSize = (Integer) params.get("pageSize");

            if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0) {
                int start = (pageNum - 1) * pageSize;
                int end = Math.min(start + pageSize, templates.size());

                if (start < templates.size()) {
                    templates = templates.subList(start, end);
                } else {
                    templates = new ArrayList<>();
                }
            }

            return templates;

        } catch (Exception e) {
            log.error("获取模板列表失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取可比较的值用于排序
     */
    private Comparable getComparableValue(Map<String, Object> template, String sortBy) {
        Object value = template.get(sortBy);

        if (value == null) {
            return null;
        }

        if (value instanceof Comparable) {
            return (Comparable) value;
        }

        // 对于非Comparable类型，转换为字符串
        return value.toString();
    }
}
