package com.huabo.fxgl.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.ChineseFormulaDTO;
import com.huabo.fxgl.dto.ExpressionQueryDTO;
import com.huabo.fxgl.dto.ExpressionSaveDTO;
import com.huabo.fxgl.dto.ExpressionTemplateDTO;
import com.huabo.fxgl.service.ChineseFormulaParsingService;
import com.huabo.fxgl.service.ExpressionManagementService;
import com.huabo.fxgl.service.ExpressionTemplateService;
import com.huabo.fxgl.vo.ExpressionVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 表达式管理控制器
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
@Slf4j
@RestController
@RequestMapping("/model/expression")
@Tag(name="表达式管理",description="表达式管理")
public class ExpressionManagementController {

    @Autowired
    private ExpressionManagementService expressionManagementService;

    @Autowired
    private ChineseFormulaParsingService chineseFormulaParsingService;

    @Autowired
    private ExpressionTemplateService expressionTemplateService;

    @PostMapping("/list")
    @Operation(summary = "分页查询表达式列表", description = "支持多条件查询和分页")
    public String getExpressionList(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "20") Integer pageSize,
                                   @RequestParam(required = false) String expressionName,
                                   @RequestParam(required = false) String expressionType,
                                   @RequestParam(required = false) String expressionCategory,
                                   @RequestParam(required = false) String isEnabled,
                                   @RequestParam(required = false) String isTemplate,
                                   @RequestParam(required = false) String createUser,
                                   @RequestParam(required = false) String startTime,
                                   @RequestParam(required = false) String endTime) {
        try {
            // 构建查询DTO
            ExpressionQueryDTO queryDTO = new ExpressionQueryDTO();
            queryDTO.setPageNum(pageNum);
            queryDTO.setPageSize(pageSize);
            queryDTO.setExpressionName(expressionName);
            queryDTO.setExpressionType(expressionType);
            queryDTO.setExpressionCategory(expressionCategory);
            queryDTO.setIsEnabled(isEnabled);
            queryDTO.setIsTemplate(isTemplate);
            queryDTO.setCreateUser(createUser);
            queryDTO.setStartTime(startTime);
            queryDTO.setEndTime(endTime);

            IPage<ExpressionVO> pageResult = expressionManagementService.getExpressionPage(queryDTO);
            return JsonBean.success(pageResult, pageResult.getRecords());
        } catch (Exception e) {
            log.error("查询表达式列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{id}")
    @Operation(summary = "查询表达式详情", description = "根据ID查询表达式详细信息")
    public String getExpressionDetail(@PathVariable("id") String expressionId) {
        try {
            ExpressionVO expression = expressionManagementService.getExpressionById(expressionId);
            if (expression == null) {
                return JsonBean.error("表达式不存在");
            }
            return JsonBean.success(null, expression);
        } catch (Exception e) {
            log.error("查询表达式详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @Operation(summary = "保存表达式", description = "新增或更新表达式")
    public String saveExpression(@RequestBody ExpressionSaveDTO saveDTO, HttpServletRequest request) {
        try {
            // TODO: 从token中获取当前用户，这里暂时使用固定值
            String currentUser = "SYSTEM";

            String expressionId = expressionManagementService.saveExpression(saveDTO, currentUser);
            return JsonBean.success(null, expressionId);
        } catch (Exception e) {
            log.error("保存表达式失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除表达式", description = "根据ID删除表达式")
    public String deleteExpression(@PathVariable("id") String expressionId) {
        try {
            boolean success = expressionManagementService.deleteExpression(expressionId);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除表达式失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/toggle/{id}")
    @Operation(summary = "启用/禁用表达式", description = "切换表达式启用状态")
    public String toggleExpressionStatus(@PathVariable("id") String expressionId, 
                                       @RequestParam String isEnabled,
                                       HttpServletRequest request) {
        try {
            // TODO: 从token中获取当前用户
            String currentUser = "SYSTEM";
            
            boolean success = expressionManagementService.toggleExpressionStatus(expressionId, isEnabled, currentUser);
            if (success) {
                return JsonBean.success("操作成功");
            } else {
                return JsonBean.error("操作失败");
            }
        } catch (Exception e) {
            log.error("切换表达式状态失败", e);
            return JsonBean.error("操作失败: " + e.getMessage());
        }
    }

    @PostMapping("/dataSources")
    @Operation(summary = "获取表达式可用数据源", description = "获取表达式管理可用的数据源列表")
    public String getDataSourcesForExpression() {
        try {
            List<Map<String, Object>> dataSourceList = expressionManagementService.getDataSourceList();
            return JsonBean.success(null, dataSourceList);
        } catch (Exception e) {
            log.error("查询数据源列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/dataSources/{dataSourceId}/tables")
    @Operation(summary = "获取数据源下的表列表", description = "根据数据源ID获取表列表")
    public String getTablesForExpression(@PathVariable("dataSourceId") String dataSourceId) {
        try {
            List<Map<String, Object>> tableList = expressionManagementService.getTableList(dataSourceId);
            return JsonBean.success(null, tableList);
        } catch (Exception e) {
            log.error("查询表列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/tableFields")
    @Operation(summary = "获取表字段信息", description = "根据数据源ID和表名获取字段列表")
    public String getTableFieldsForExpression(@RequestBody Map<String, String> params) {
        try {
            String dataSourceId = params.get("dataSourceId");
            String tableName = params.get("tableName");

            List<Map<String, Object>> fieldList = expressionManagementService.getFieldList(dataSourceId, tableName);
            return JsonBean.success(null, fieldList);
        } catch (Exception e) {
            log.error("查询字段列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/datasource/list")
    @Operation(summary = "获取数据源列表", description = "获取可用的数据源列表")
    public String getDataSourceList() {
        try {
            List<Map<String, Object>> dataSourceList = expressionManagementService.getDataSourceList();
            return JsonBean.success(null, dataSourceList);
        } catch (Exception e) {
            log.error("查询数据源列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/datasource/tables/{dataSourceId}")
    @Operation(summary = "获取数据源表列表", description = "根据数据源ID获取表列表")
    public String getTableList(@PathVariable("dataSourceId") String dataSourceId) {
        try {
            List<Map<String, Object>> tableList = expressionManagementService.getTableList(dataSourceId);
            return JsonBean.success(null, tableList);
        } catch (Exception e) {
            log.error("查询表列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/datasource/fields")
    @Operation(summary = "获取表字段列表", description = "根据数据源ID和表名获取字段列表")
    public String getFieldList(@RequestBody Map<String, String> params) {
        try {
            String dataSourceId = params.get("dataSourceId");
            String tableName = params.get("tableName");
            
            List<Map<String, Object>> fieldList = expressionManagementService.getFieldList(dataSourceId, tableName);
            return JsonBean.success(null, fieldList);
        } catch (Exception e) {
            log.error("查询字段列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "验证表达式", description = "验证字段比对表达式语法")
    public String validateExpression(@RequestBody Map<String, String> params) {
        try {
            String expressionContent = params.get("expressionContent");
            String sourceDataConfig = params.get("sourceDataConfig");
            String targetDataConfig = params.get("targetDataConfig");
            String comparisonRules = params.get("comparisonRules");
            
            Map<String, Object> result = expressionManagementService.validateFieldComparisonExpression(
                    expressionContent, sourceDataConfig, targetDataConfig, comparisonRules);

            return JsonBean.success(null, result);
        } catch (Exception e) {
            log.error("验证表达式失败", e);
            return JsonBean.error("验证失败: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    @Operation(summary = "测试表达式", description = "测试字段比对表达式执行")
    public String testExpression(@RequestBody Map<String, String> params) {
        try {
            String expressionContent = params.get("expressionContent");
            String testData = params.get("testData");

            Map<String, Object> result = expressionManagementService.testFieldComparisonExpression(
                    expressionContent, testData);

            return JsonBean.success(null, result);
        } catch (Exception e) {
            log.error("测试表达式失败", e);
            return JsonBean.error("测试失败: " + e.getMessage());
        }
    }

    @PostMapping("/executeTest")
    @Operation(summary = "执行表达式测试", description = "执行完整的表达式测试")
    public String executeExpressionTest(@RequestBody Map<String, Object> params) {
        try {
            String expressionId = (String) params.get("expressionId");
            String testMode = (String) params.get("testMode");
            String dataSourceId = (String) params.get("dataSourceId");

            Map<String, Object> result = expressionManagementService.executeExpressionTest(params);
            return JsonBean.success(null, result);
        } catch (Exception e) {
            log.error("执行表达式测试失败", e);
            return JsonBean.error("测试失败: " + e.getMessage());
        }
    }

    @PostMapping("/validateSyntax")
    @Operation(summary = "校验表达式语法", description = "校验表达式语法正确性")
    public String validateExpressionSyntax(@RequestBody Map<String, Object> params) {
        try {
            String expressionContent = (String) params.get("expressionContent");

            Map<String, Object> result = expressionManagementService.validateExpressionSyntax(expressionContent);
            return JsonBean.success(null, result);
        } catch (Exception e) {
            log.error("校验表达式语法失败", e);
            return JsonBean.error("校验失败: " + e.getMessage());
        }
    }

    @PostMapping("/saveTestReport")
    @Operation(summary = "保存测试报告", description = "保存表达式测试报告")
    public String saveExpressionTestReport(@RequestBody Map<String, Object> params) {
        try {
            String expressionId = (String) params.get("expressionId");
            Map<String, Object> testResults = (Map<String, Object>) params.get("testResults");
            Map<String, Object> testParams = (Map<String, Object>) params.get("testParams");

            String reportId = expressionManagementService.saveTestReport(expressionId, testResults, testParams);
            return JsonBean.success("保存成功", reportId);
        } catch (Exception e) {
            log.error("保存测试报告失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/generateSQL")
    @Operation(summary = "生成SQL", description = "根据比对规则生成SQL语句")
    public String generateSQL(@RequestBody Map<String, String> params) {
        try {
            String sourceDataConfig = params.get("sourceDataConfig");
            String targetDataConfig = params.get("targetDataConfig");
            String comparisonRules = params.get("comparisonRules");
            
            Map<String, Object> result = expressionManagementService.generateFieldComparisonSQL(
                    sourceDataConfig, targetDataConfig, comparisonRules);

            return JsonBean.success(null, result);
        } catch (Exception e) {
            log.error("生成SQL失败", e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }

    @PostMapping("/statistics")
    @Operation(summary = "获取统计信息", description = "获取表达式管理统计信息")
    public String getStatistics() {
        try {
            Map<String, Object> statistics = expressionManagementService.getExpressionStatistics();
            return JsonBean.success(null, statistics);
        } catch (Exception e) {
            log.error("查询统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/recentActivity")
    @Operation(summary = "获取最近活动", description = "获取表达式管理最近活动记录")
    public String getRecentActivity() {
        try {
            List<Map<String, Object>> activities = expressionManagementService.getRecentActivity();
            return JsonBean.success(null, activities);
        } catch (Exception e) {
            log.error("查询最近活动失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/toggleStatus/{id}")
    @Operation(summary = "切换表达式状态", description = "启用或禁用表达式")
    public String toggleExpressionStatus(@PathVariable("id") String expressionId) {
        try {
            String currentUser = "SYSTEM";
            boolean success = expressionManagementService.toggleExpressionStatus(expressionId, null, currentUser);
            if (success) {
                return JsonBean.success("操作成功");
            } else {
                return JsonBean.error("操作失败");
            }
        } catch (Exception e) {
            log.error("切换表达式状态失败", e);
            return JsonBean.error("操作失败: " + e.getMessage());
        }
    }

    @PostMapping("/health")
    @Operation(summary = "健康检查", description = "测试表达式管理接口是否正常")
    public String testApi() {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "表达式管理接口正常");
            result.put("timestamp", new Date());
            return JsonBean.success(null, result);
        } catch (Exception e) {
            log.error("测试接口失败", e);
            return JsonBean.error("测试失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/delete")
    @Operation(summary = "批量删除表达式", description = "批量删除多个表达式")
    public String batchDeleteExpressions(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        try {
            @SuppressWarnings("unchecked")
            List<String> expressionIds = (List<String>) params.get("expressionIds");

            if (expressionIds == null || expressionIds.isEmpty()) {
                return JsonBean.error("请选择要删除的表达式");
            }

            // TODO: 从token中获取当前用户
            String currentUser = "SYSTEM";

            boolean success = expressionManagementService.batchDeleteExpressions(expressionIds, currentUser);
            if (success) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除表达式失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/status")
    @Operation(summary = "批量更新表达式状态", description = "批量启用或禁用表达式")
    public String batchUpdateExpressionStatus(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        try {
            @SuppressWarnings("unchecked")
            List<String> expressionIds = (List<String>) params.get("expressionIds");
            String isEnabled = (String) params.get("isEnabled");

            if (expressionIds == null || expressionIds.isEmpty()) {
                return JsonBean.error("请选择要操作的表达式");
            }

            // TODO: 从token中获取当前用户
            String currentUser = "SYSTEM";

            boolean success = expressionManagementService.batchUpdateExpressionStatus(expressionIds, isEnabled, currentUser);
            if (success) {
                return JsonBean.success("批量更新成功");
            } else {
                return JsonBean.error("批量更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新表达式状态失败", e);
            return JsonBean.error("批量更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/copy")
    @Operation(summary = "复制表达式", description = "复制现有表达式创建新表达式")
    public String copyExpression(@RequestBody Map<String, String> params, HttpServletRequest request) {
        try {
            String expressionId = params.get("expressionId");
            String newName = params.get("newName");
            String newCode = params.get("newCode");

            if (!StringUtils.hasText(expressionId) || !StringUtils.hasText(newName) || !StringUtils.hasText(newCode)) {
                return JsonBean.error("参数不完整");
            }

            // TODO: 从token中获取当前用户
            String currentUser = "SYSTEM";

            String newExpressionId = expressionManagementService.copyExpression(expressionId, newName, newCode, currentUser);
            return JsonBean.success("复制成功", newExpressionId);
        } catch (Exception e) {
            log.error("复制表达式失败", e);
            return JsonBean.error("复制失败: " + e.getMessage());
        }
    }

    @PostMapping("/template/{id}")
    @Operation(summary = "设置表达式模板状态", description = "将表达式设置为模板或取消模板")
    public String setExpressionAsTemplate(@PathVariable("id") String expressionId,
                                        @RequestParam String isTemplate,
                                        HttpServletRequest request) {
        try {
            // TODO: 从token中获取当前用户
            String currentUser = "SYSTEM";

            boolean success = expressionManagementService.setExpressionAsTemplate(expressionId, isTemplate, currentUser);
            if (success) {
                return JsonBean.success("操作成功");
            } else {
                return JsonBean.error("操作失败");
            }
        } catch (Exception e) {
            log.error("设置表达式模板状态失败", e);
            return JsonBean.error("操作失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 中文公式解析相关API
    // =====================================================

    @PostMapping("/chinese/parse")
    @Operation(summary = "解析中文公式", description = "将中文公式转换为SQL表达式")
    public String parseChineseFormula(@RequestBody ChineseFormulaDTO formulaDTO) {
        try {
            if (!StringUtils.hasText(formulaDTO.getChineseFormula())) {
                return JsonBean.error("中文公式不能为空");
            }

            log.info("解析中文公式: {}", formulaDTO.getChineseFormula());

            Map<String, Object> result = chineseFormulaParsingService.parseChineseFormula(
                    formulaDTO.getChineseFormula(),
                    formulaDTO.getSourceDataConfig(),
                    formulaDTO.getTargetDataConfig());

            return JsonBean.success("解析成功", result);
        } catch (Exception e) {
            log.error("解析中文公式失败: {}", formulaDTO.getChineseFormula(), e);
            return JsonBean.error("解析失败: " + e.getMessage());
        }
    }

    @PostMapping("/chinese/validate")
    @Operation(summary = "验证中文公式", description = "验证中文公式语法正确性")
    public String validateChineseFormula(@RequestBody ChineseFormulaDTO formulaDTO) {
        try {
            if (!StringUtils.hasText(formulaDTO.getChineseFormula())) {
                return JsonBean.error("中文公式不能为空");
            }

            log.info("验证中文公式: {}", formulaDTO.getChineseFormula());

            Map<String, Object> result = chineseFormulaParsingService.validateChineseFormula(formulaDTO.getChineseFormula());

            String message = (Boolean) result.get("valid") ? "验证通过" : "验证失败";
            return JsonBean.success(message, result);
        } catch (Exception e) {
            log.error("验证中文公式失败: {}", formulaDTO.getChineseFormula(), e);
            return JsonBean.error("验证失败: " + e.getMessage());
        }
    }

    @PostMapping("/chineseOperators")
    @Operation(summary = "获取中文运算符列表", description = "获取系统支持的中文运算符列表")
    public String getChineseOperators() {
        try {
            List<Map<String, Object>> operators = chineseFormulaParsingService.getSupportedOperators();
            return JsonBean.success(null, operators);
        } catch (Exception e) {
            log.error("获取中文运算符列表失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/validateChineseFormula")
    @Operation(summary = "校验中文公式", description = "校验中文公式语法并生成SQL")
    public String validateChineseFormula(@RequestBody Map<String, Object> params) {
        try {
            String chineseFormula = (String) params.get("chineseFormula");
            Map<String, Object> dataSourceConfig = (Map<String, Object>) params.get("dataSourceConfig");

            Map<String, Object> result = chineseFormulaParsingService.validateAndParseChineseFormula(
                    chineseFormula, dataSourceConfig);
            return JsonBean.success(null, result);
        } catch (Exception e) {
            log.error("校验中文公式失败", e);
            return JsonBean.error("校验失败: " + e.getMessage());
        }
    }

    @GetMapping("/chinese/operators")
    @Operation(summary = "获取支持的运算符", description = "获取系统支持的中文运算符列表")
    public String getSupportedOperators() {
        try {
            List<Map<String, Object>> operators = chineseFormulaParsingService.getSupportedOperators();
            return JsonBean.success(null, operators);
        } catch (Exception e) {
            log.error("获取运算符列表失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/chinese/templates")
    @Operation(summary = "获取公式模板", description = "获取常用的中文公式模板")
    public String getFormulaTemplates(@RequestParam(required = false) String category) {
        try {
            List<Map<String, Object>> templates = chineseFormulaParsingService.getFormulaTemplates(category);
            return JsonBean.success(null, templates);
        } catch (Exception e) {
            log.error("获取公式模板失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/chinese/suggestions")
    @Operation(summary = "获取公式建议", description = "根据输入内容提供智能建议")
    public String getFormulaSuggestions(@RequestBody Map<String, Object> params) {
        try {
            String partialFormula = (String) params.get("partialFormula");
            @SuppressWarnings("unchecked")
            List<String> availableFields = (List<String>) params.get("availableFields");

            if (availableFields == null) {
                availableFields = new ArrayList<>();
            }

            List<Map<String, Object>> suggestions = chineseFormulaParsingService.getFormulaSuggestions(
                    partialFormula, availableFields);

            return JsonBean.success(null, suggestions);
        } catch (Exception e) {
            log.error("获取公式建议失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/chinese/format")
    @Operation(summary = "格式化中文公式", description = "格式化中文公式的显示格式")
    public String formatChineseFormula(@RequestBody Map<String, String> params) {
        try {
            String chineseFormula = params.get("chineseFormula");

            if (!StringUtils.hasText(chineseFormula)) {
                return JsonBean.error("中文公式不能为空");
            }

            String formattedFormula = chineseFormulaParsingService.formatChineseFormula(chineseFormula);

            Map<String, Object> result = new HashMap<>();
            result.put("originalFormula", chineseFormula);
            result.put("formattedFormula", formattedFormula);

            return JsonBean.success(null, result);
        } catch (Exception e) {
            log.error("格式化中文公式失败", e);
            return JsonBean.error("格式化失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 模板管理相关接口
    // =====================================================

    @PostMapping("/templates/list")
    @Operation(summary = "获取模板列表", description = "获取表达式模板列表，支持分页和过滤")
    public String getTemplateList(@RequestBody(required = false) Map<String, Object> params) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            List<Map<String, Object>> templates = expressionTemplateService.getTemplateList(params);
            return JsonBean.success(null, templates);
        } catch (Exception e) {
            log.error("获取模板列表失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/templates")
    @Operation(summary = "获取中文公式模板列表", description = "获取中文公式模板列表，支持按分类过滤")
    public String getChineseFormulaTemplates(@RequestParam(required = false) String category) {
        try {
            List<Map<String, Object>> templates = expressionTemplateService.getChineseFormulaTemplates(category);
            return JsonBean.success(null, templates);
        } catch (Exception e) {
            log.error("获取模板列表失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/templates/{templateId}")
    @Operation(summary = "获取模板详情", description = "根据ID获取模板详细信息")
    public String getTemplateById(@PathVariable String templateId) {
        try {
            Map<String, Object> template = expressionTemplateService.getTemplateById(templateId);
            if (template == null) {
                return JsonBean.error("模板不存在");
            }
            return JsonBean.success(null, template);
        } catch (Exception e) {
            log.error("获取模板详情失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/templates/save")
    @Operation(summary = "保存中文公式模板", description = "新增或更新中文公式模板")
    public String saveChineseFormulaTemplate(@RequestBody ExpressionTemplateDTO templateDTO, HttpServletRequest request) {
        try {
            // TODO: 从token中获取当前用户
            String currentUser = "SYSTEM";
            templateDTO.setCreateUser(currentUser);

            log.info("保存中文公式模板: {}", templateDTO.getTemplateName());

            // 转换DTO为Map（临时方案，实际项目中应该直接使用DTO）
            Map<String, Object> templateData = convertTemplateDTO(templateDTO);

            Map<String, Object> result = expressionTemplateService.saveChineseFormulaTemplate(templateData);

            if ((Boolean) result.get("success")) {
                return JsonBean.success(result.get("message").toString(), result.get("data"));
            } else {
                return JsonBean.error(result.get("message").toString());
            }
        } catch (Exception e) {
            log.error("保存模板失败: {}", templateDTO.getTemplateName(), e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/templates/{templateId}/use")
    @Operation(summary = "使用模板", description = "增加模板使用次数")
    public String useTemplate(@PathVariable String templateId) {
        try {
            expressionTemplateService.incrementTemplateUsage(templateId);
            return JsonBean.success("使用成功");
        } catch (Exception e) {
            log.error("使用模板失败", e);
            return JsonBean.error("使用失败: " + e.getMessage());
        }
    }

    @GetMapping("/templates/popular")
    @Operation(summary = "获取热门模板", description = "获取使用次数最多的模板")
    public String getPopularTemplates(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Map<String, Object>> templates = expressionTemplateService.getPopularTemplates(limit);
            return JsonBean.success(null, templates);
        } catch (Exception e) {
            log.error("获取热门模板失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/templates/search")
    @Operation(summary = "搜索模板", description = "根据关键词和分类搜索模板")
    public String searchTemplates(@RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) String category) {
        try {
            List<Map<String, Object>> templates = expressionTemplateService.searchTemplates(keyword, category);
            return JsonBean.success(null, templates);
        } catch (Exception e) {
            log.error("搜索模板失败", e);
            return JsonBean.error("搜索失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/templates/{templateId}")
    @Operation(summary = "删除模板", description = "删除指定的模板")
    public String deleteTemplate(@PathVariable String templateId) {
        try {
            boolean success = expressionTemplateService.deleteTemplate(templateId);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("模板不存在或删除失败");
            }
        } catch (Exception e) {
            log.error("删除模板失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/templates/{templateId}/toggle")
    @Operation(summary = "启用/禁用模板", description = "切换模板的启用状态")
    public String toggleTemplateStatus(@PathVariable String templateId,
                                      @RequestParam boolean enabled) {
        try {
            boolean success = expressionTemplateService.toggleTemplateStatus(templateId, enabled);
            if (success) {
                return JsonBean.success(enabled ? "启用成功" : "禁用成功");
            } else {
                return JsonBean.error("模板不存在或操作失败");
            }
        } catch (Exception e) {
            log.error("切换模板状态失败", e);
            return JsonBean.error("操作失败: " + e.getMessage());
        }
    }

    @GetMapping("/templates/categories")
    @Operation(summary = "获取模板分类", description = "获取所有可用的模板分类")
    public String getTemplateCategories() {
        try {
            List<Map<String, Object>> categories = expressionTemplateService.getTemplateCategories();
            return JsonBean.success(null, categories);
        } catch (Exception e) {
            log.error("获取模板分类失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 私有辅助方法
    // =====================================================

    /**
     * 转换模板DTO为Map对象
     *
     * @param templateDTO 模板DTO
     * @return Map对象
     */
    private Map<String, Object> convertTemplateDTO(ExpressionTemplateDTO templateDTO) {
        Map<String, Object> templateData = new HashMap<>();

        if (StringUtils.hasText(templateDTO.getTemplateId())) {
            templateData.put("templateId", templateDTO.getTemplateId());
        }
        templateData.put("templateCode", templateDTO.getTemplateCode());
        templateData.put("templateName", templateDTO.getTemplateName());
        templateData.put("templateCategory", templateDTO.getTemplateCategory());
        templateData.put("businessScenario", templateDTO.getBusinessScenario());
        templateData.put("chineseFormula", templateDTO.getChineseFormula());
        templateData.put("sqlTemplate", templateDTO.getSqlTemplate());
        templateData.put("parameterConfig", templateDTO.getParameterConfig());
        templateData.put("description", templateDTO.getDescription());
        templateData.put("exampleUsage", templateDTO.getExampleUsage());
        templateData.put("isSystemTemplate", templateDTO.getIsSystemTemplate() != null ? templateDTO.getIsSystemTemplate() : false);
        templateData.put("isEnabled", templateDTO.getIsEnabled() != null ? templateDTO.getIsEnabled() : true);
        templateData.put("createUser", templateDTO.getCreateUser());

        return templateData;
    }
}
