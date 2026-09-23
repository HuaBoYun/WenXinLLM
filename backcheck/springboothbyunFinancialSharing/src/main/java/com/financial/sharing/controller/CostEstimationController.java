package com.financial.sharing.controller;

import com.financial.sharing.service.CostEstimationService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 成本估算控制器
 * 处理估算方案、成本模型、预算编制、成本模拟、差异分析、估算报告等功能
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Slf4j
@Api(tags = "管理会计-成本估算模块")
@RestController
@RequestMapping("/ma/costestimate")
@CrossOrigin
public class CostEstimationController {

    @Autowired
    private CostEstimationService costEstimationService;

    // ==================== 估算首页统计概览 ====================

    @ApiOperation("成本估算首页统计概览")
    @PostMapping("/getSummary")
    public MyJsonBean<Map<String, Object>> getSummary(@RequestBody(required = false) Map<String, Object> param) {
        try {
            log.info("查询成本估算首页统计，参数: {}", param);
            return costEstimationService.getCostEstimateSummary(param == null ? new java.util.HashMap<>() : param);
        } catch (Exception e) {
            log.error("查询成本估算首页统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 估算方案管理 ====================

    @ApiOperation("分页查询估算方案列表")
    @PostMapping("/scheme/getList")
    public MyJsonBean<PageResult<Map<String, Object>>> getSchemeList(@RequestBody Map<String, Object> param) {
        try {
            log.info("查询估算方案列表，参数: {}", param);
            return costEstimationService.getSchemeList(param);
        } catch (Exception e) {
            log.error("查询估算方案列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新估算方案")
    @PostMapping("/scheme/saveOrUpdate")
    public MyJsonBean saveOrUpdateScheme(@RequestBody Map<String, Object> schemeData) {
        try {
            log.info("保存或更新估算方案，数据: {}", schemeData);
            return costEstimationService.saveOrUpdateScheme(schemeData);
        } catch (Exception e) {
            log.error("保存或更新估算方案失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除估算方案")
    @PostMapping("/scheme/delete")
    public MyJsonBean deleteScheme(@RequestBody Map<String, Object> param) {
        try {
            String schemeId = (String) param.get("schemeId");
            log.info("删除估算方案，ID: {}", schemeId);
            return costEstimationService.deleteScheme(schemeId);
        } catch (Exception e) {
            log.error("删除估算方案失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除估算方案")
    @PostMapping("/scheme/batchDelete")
    public MyJsonBean batchDeleteScheme(@RequestBody List<String> schemeIds) {
        try {
            log.info("批量删除估算方案，数量: {}", schemeIds.size());
            return costEstimationService.batchDeleteScheme(schemeIds);
        } catch (Exception e) {
            log.error("批量删除估算方案失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取估算方案详情")
    @GetMapping("/scheme/getById")
    public MyJsonBean<Map<String, Object>> getSchemeById(@RequestParam String schemeId) {
        try {
            log.info("查询估算方案详情，ID: {}", schemeId);
            return costEstimationService.getSchemeById(schemeId);
        } catch (Exception e) {
            log.error("查询估算方案详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/停用估算方案")
    @PostMapping("/scheme/activate")
    public MyJsonBean toggleSchemeStatus(@RequestBody Map<String, Object> param) {
        try {
            String schemeId = (String) param.get("schemeId");
            Integer isEnabled = (Integer) param.get("isEnabled");
            log.info("切换估算方案状态，ID: {}, 状态: {}", schemeId, isEnabled);
            return costEstimationService.toggleSchemeStatus(schemeId, isEnabled);
        } catch (Exception e) {
            log.error("切换估算方案状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    // ==================== 成本模型管理 ====================

    @ApiOperation("分页查询成本模型列表")
    @PostMapping("/model/getList")
    public MyJsonBean<PageResult<Map<String, Object>>> getModelList(@RequestBody Map<String, Object> param) {
        try {
            log.info("查询成本模型列表，参数: {}", param);
            return costEstimationService.getModelList(param);
        } catch (Exception e) {
            log.error("查询成本模型列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新成本模型")
    @PostMapping("/model/saveOrUpdate")
    public MyJsonBean saveOrUpdateModel(@RequestBody Map<String, Object> modelData) {
        try {
            log.info("保存或更新成本模型，数据: {}", modelData);
            return costEstimationService.saveOrUpdateModel(modelData);
        } catch (Exception e) {
            log.error("保存或更新成本模型失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除成本模型")
    @PostMapping("/model/delete")
    public MyJsonBean deleteModel(@RequestBody Map<String, Object> param) {
        try {
            String modelId = (String) param.get("modelId");
            log.info("删除成本模型，ID: {}", modelId);
            return costEstimationService.deleteModel(modelId);
        } catch (Exception e) {
            log.error("删除成本模型失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本模型详情")
    @GetMapping("/model/getById")
    public MyJsonBean<Map<String, Object>> getModelById(@RequestParam String modelId) {
        try {
            log.info("查询成本模型详情，ID: {}", modelId);
            return costEstimationService.getModelById(modelId);
        } catch (Exception e) {
            log.error("查询成本模型详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("测试成本模型")
    @PostMapping("/model/validate")
    public MyJsonBean testModel(@RequestBody Map<String, Object> param) {
        try {
            // 前端 testData 可能是 String（textarea 输入的 JSON 文本）也可能是 Map（结构化对象）
            // 不在 controller 强转，直接交 service 兼容处理
            String modelId = param.get("modelId") == null ? null : param.get("modelId").toString();
            Object testData = param.get("testData");
            log.info("测试成本模型，ID: {}, 测试数据: {}", modelId, testData);
            return costEstimationService.testModel(modelId, testData);
        } catch (Exception e) {
            log.error("测试成本模型失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取模型选项列表")
    @GetMapping("/model/options")
    public MyJsonBean<List<Map<String, Object>>> getModelOptions(@RequestParam String bookId,
                                                                  @RequestParam String tenantId) {
        try {
            log.info("查询模型选项列表，bookId: {}, tenantId: {}", bookId, tenantId);
            return costEstimationService.getModelOptions(bookId, tenantId);
        } catch (Exception e) {
            log.error("查询模型选项列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/停用成本模型（单字段更新，不要复用 saveOrUpdate）")
    @PostMapping("/model/status")
    public MyJsonBean toggleModelStatus(@RequestBody Map<String, Object> param) {
        try {
            String modelId = (String) param.get("modelId");
            Integer isEnabled = (Integer) param.get("isEnabled");
            log.info("切换成本模型状态，ID: {}, 状态: {}", modelId, isEnabled);
            return costEstimationService.toggleModelStatus(modelId, isEnabled);
        } catch (Exception e) {
            log.error("切换成本模型状态失败", e);
            return MyJsonBean.errorData("操作失败: " + e.getMessage());
        }
    }

    // ==================== 通用导入/导出 ====================

    @ApiOperation("批量导入估算数据（占位实现：JSON 列表逐条 saveOrUpdate）")
    @PostMapping("/batchImport")
    public MyJsonBean batchImport(@RequestBody(required = false) Map<String, Object> param) {
        try {
            log.info("批量导入估算数据，参数: {}", param);
            return costEstimationService.batchImportEstimateData(param);
        } catch (Exception e) {
            log.error("批量导入失败", e);
            return MyJsonBean.errorData("导入失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出估算数据（占位实现：返回 CSV 文件流）")
    @PostMapping("/export")
    public void exportEstimateData(@RequestBody(required = false) Map<String, Object> param,
                                    HttpServletResponse response) {
        try {
            log.info("导出估算数据，参数: {}", param);
            costEstimationService.exportEstimateData(param, response);
        } catch (Exception e) {
            log.error("导出失败", e);
        }
    }

    // ==================== 预算编制管理 ====================

    @ApiOperation("分页查询预算编制列表")
    @PostMapping("/budget/preparation/getList")
    public MyJsonBean<PageResult<Map<String, Object>>> getBudgetList(@RequestBody Map<String, Object> param) {
        try {
            log.info("查询预算编制列表，参数: {}", param);
            return costEstimationService.getBudgetList(param);
        } catch (Exception e) {
            log.error("查询预算编制列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新预算编制")
    @PostMapping("/budget/preparation/saveOrUpdate")
    public MyJsonBean saveOrUpdateBudget(@RequestBody Map<String, Object> budgetData) {
        try {
            log.info("保存或更新预算编制，数据: {}", budgetData);
            return costEstimationService.saveOrUpdateBudget(budgetData);
        } catch (Exception e) {
            log.error("保存或更新预算编制失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除预算编制")
    @PostMapping("/budget/delete")
    public MyJsonBean deleteBudget(@RequestBody Map<String, Object> param) {
        try {
            String budgetId = (String) param.get("budgetId");
            log.info("删除预算编制，ID: {}", budgetId);
            return costEstimationService.deleteBudget(budgetId);
        } catch (Exception e) {
            log.error("删除预算编制失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取预算编制详情")
    @GetMapping("/budget/getById")
    public MyJsonBean<Map<String, Object>> getBudgetById(@RequestParam String budgetId) {
        try {
            log.info("查询预算编制详情，ID: {}", budgetId);
            return costEstimationService.getBudgetById(budgetId);
        } catch (Exception e) {
            log.error("查询预算编制详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("提交预算审批")
    @PostMapping("/budget/preparation/submit")
    public MyJsonBean submitBudgetApproval(@RequestBody Map<String, Object> param) {
        try {
            String budgetId = (String) param.get("preparationId");
            if (budgetId == null) {
                budgetId = (String) param.get("budgetId");
            }
            log.info("提交预算审批，ID: {}", budgetId);
            return costEstimationService.submitBudgetApproval(budgetId);
        } catch (Exception e) {
            log.error("提交预算审批失败", e);
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }

    @ApiOperation("审批预算")
    @PostMapping("/budget/preparation/approve")
    public MyJsonBean approveBudget(@RequestBody Map<String, Object> approvalData) {
        try {
            String budgetId = (String) approvalData.get("preparationId");
            if (budgetId == null) {
                budgetId = (String) approvalData.get("budgetId");
            }
            log.info("审批预算，ID: {}, 审批数据: {}", budgetId, approvalData);
            return costEstimationService.approveBudget(budgetId, approvalData);
        } catch (Exception e) {
            log.error("审批预算失败", e);
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量审批预算")
    @PostMapping("/budget/batchApprove")
    public MyJsonBean batchApproveBudget(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<String> budgetIds = (List<String>) param.get("budgetIds");
            log.info("批量审批预算，数量: {}", budgetIds.size());
            return costEstimationService.batchApproveBudget(budgetIds, param);
        } catch (Exception e) {
            log.error("批量审批预算失败", e);
            return MyJsonBean.errorData("批量审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取预算统计数据")
    @GetMapping("/budget/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetStatistics(@RequestParam String budgetYear,
                                                               @RequestParam String bookId,
                                                               @RequestParam String tenantId) {
        try {
            log.info("获取预算统计数据，年度: {}, bookId: {}, tenantId: {}", budgetYear, bookId, tenantId);
            return costEstimationService.getBudgetStatistics(budgetYear, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取预算统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 成本模拟管理 ====================

    @ApiOperation("分页查询成本模拟列表")
    @PostMapping("/simulation/getList")
    public MyJsonBean<PageResult<Map<String, Object>>> getSimulationList(@RequestBody Map<String, Object> param) {
        try {
            log.info("查询成本模拟列表，参数: {}", param);
            return costEstimationService.getSimulationList(param);
        } catch (Exception e) {
            log.error("查询成本模拟列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行成本模拟")
    @PostMapping("/simulation/execute")
    public MyJsonBean executeSimulation(@RequestBody Map<String, Object> simulationData) {
        try {
            log.info("执行成本模拟，数据: {}", simulationData);
            return costEstimationService.executeSimulation(simulationData);
        } catch (Exception e) {
            log.error("执行成本模拟失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新成本模拟")
    @PostMapping("/simulation/saveOrUpdate")
    public MyJsonBean saveOrUpdateSimulation(@RequestBody Map<String, Object> simulationData) {
        try {
            log.info("保存或更新成本模拟，数据: {}", simulationData);
            return costEstimationService.executeSimulation(simulationData);
        } catch (Exception e) {
            log.error("保存或更新成本模拟失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("运行成本模拟")
    @PostMapping("/simulation/run")
    public MyJsonBean runSimulation(@RequestBody Map<String, Object> param) {
        try {
            String simulationId = (String) param.get("simulationId");
            log.info("运行成本模拟，ID: {}", simulationId);
            return costEstimationService.getSimulationResult(simulationId);
        } catch (Exception e) {
            log.error("运行成本模拟失败", e);
            return MyJsonBean.errorData("运行失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取模拟结果")
    @GetMapping("/simulation/result")
    public MyJsonBean<Map<String, Object>> getSimulationResult(@RequestParam String simulationId) {
        try {
            log.info("查询模拟结果，ID: {}", simulationId);
            return costEstimationService.getSimulationResult(simulationId);
        } catch (Exception e) {
            log.error("查询模拟结果失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除成本模拟")
    @PostMapping("/simulation/delete")
    public MyJsonBean deleteSimulation(@RequestBody Map<String, Object> param) {
        try {
            String simulationId = (String) param.get("simulationId");
            log.info("删除成本模拟，ID: {}", simulationId);
            return costEstimationService.deleteSimulation(simulationId);
        } catch (Exception e) {
            log.error("删除成本模拟失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    // ==================== 差异分析管理 ====================

    @ApiOperation("分页查询差异分析列表")
    @PostMapping("/variance/getList")
    public MyJsonBean<PageResult<Map<String, Object>>> getVarianceAnalysisList(@RequestBody Map<String, Object> param) {
        try {
            log.info("查询差异分析列表，参数: {}", param);
            return costEstimationService.getVarianceAnalysisList(param);
        } catch (Exception e) {
            log.error("查询差异分析列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行差异分析")
    @PostMapping("/variance/execute")
    public MyJsonBean executeVarianceAnalysis(@RequestBody Map<String, Object> analysisData) {
        try {
            log.info("执行差异分析，数据: {}", analysisData);
            return costEstimationService.executeVarianceAnalysis(analysisData);
        } catch (Exception e) {
            log.error("执行差异分析失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新差异分析")
    @PostMapping("/variance/saveOrUpdate")
    public MyJsonBean saveOrUpdateVarianceAnalysis(@RequestBody Map<String, Object> analysisData) {
        try {
            log.info("保存或更新差异分析，数据: {}", analysisData);
            return costEstimationService.executeVarianceAnalysis(analysisData);
        } catch (Exception e) {
            log.error("保存或更新差异分析失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取差异分析详情")
    @GetMapping("/variance/getById")
    public MyJsonBean<Map<String, Object>> getVarianceAnalysisById(@RequestParam String analysisId) {
        try {
            log.info("查询差异分析详情，ID: {}", analysisId);
            return costEstimationService.getVarianceAnalysisById(analysisId);
        } catch (Exception e) {
            log.error("查询差异分析详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取差异统计数据")
    @GetMapping("/variance/statistics")
    public MyJsonBean<Map<String, Object>> getVarianceStatistics(@RequestParam String analysisPeriod,
                                                                 @RequestParam String bookId,
                                                                 @RequestParam String tenantId) {
        try {
            log.info("获取差异统计数据，期间: {}, bookId: {}, tenantId: {}", analysisPeriod, bookId, tenantId);
            return costEstimationService.getVarianceStatistics(analysisPeriod, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取差异统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取差异趋势数据")
    @GetMapping("/variance/trend")
    public MyJsonBean<List<Map<String, Object>>> getVarianceTrend(@RequestParam String startPeriod,
                                                                   @RequestParam String endPeriod,
                                                                   @RequestParam String bookId,
                                                                   @RequestParam String tenantId) {
        try {
            log.info("获取差异趋势数据，开始期间: {}, 结束期间: {}, bookId: {}, tenantId: {}",
                    startPeriod, endPeriod, bookId, tenantId);
            return costEstimationService.getVarianceTrend(startPeriod, endPeriod, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取差异趋势数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 估算报告管理 ====================

    @ApiOperation("分页查询估算报告列表")
    @PostMapping("/report/getList")
    public MyJsonBean<PageResult<Map<String, Object>>> getReportList(@RequestBody Map<String, Object> param) {
        try {
            log.info("查询估算报告列表，参数: {}", param);
            return costEstimationService.getReportList(param);
        } catch (Exception e) {
            log.error("查询估算报告列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("生成估算报告")
    @PostMapping("/report/generate")
    public MyJsonBean generateReport(@RequestBody Map<String, Object> reportData) {
        try {
            log.info("生成估算报告，数据: {}", reportData);
            return costEstimationService.generateReport(reportData);
        } catch (Exception e) {
            log.error("生成估算报告失败", e);
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新估算报告")
    @PostMapping("/report/saveOrUpdate")
    public MyJsonBean saveOrUpdateReport(@RequestBody Map<String, Object> reportData) {
        try {
            log.info("保存或更新估算报告，数据: {}", reportData);
            return costEstimationService.generateReport(reportData);
        } catch (Exception e) {
            log.error("保存或更新估算报告失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取报告详情")
    @GetMapping("/report/getById")
    public MyJsonBean<Map<String, Object>> getReportById(@RequestParam String reportId) {
        try {
            log.info("查询估算报告详情，ID: {}", reportId);
            return costEstimationService.getReportById(reportId);
        } catch (Exception e) {
            log.error("查询估算报告详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("发布报告")
    @PostMapping("/report/publish")
    public MyJsonBean publishReport(@RequestBody Map<String, Object> param) {
        try {
            String reportId = (String) param.get("reportId");
            log.info("发布估算报告，ID: {}", reportId);
            return costEstimationService.publishReport(reportId);
        } catch (Exception e) {
            log.error("发布估算报告失败", e);
            return MyJsonBean.errorData("发布失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除报告")
    @PostMapping("/report/delete")
    public MyJsonBean deleteReport(@RequestBody Map<String, Object> param) {
        try {
            String reportId = param == null ? null : (param.get("reportId") == null ? null : param.get("reportId").toString());
            log.info("删除估算报告，ID: {}", reportId);
            return costEstimationService.deleteReport(reportId);
        } catch (Exception e) {
            log.error("删除估算报告失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出报告")
    @PostMapping("/report/export")
    public void exportReport(@RequestBody Map<String, Object> param, HttpServletResponse response) {
        try {
            String reportId = (String) param.get("reportId");
            log.info("导出估算报告，ID: {}", reportId);
            costEstimationService.exportReport(reportId, response);
        } catch (Exception e) {
            log.error("导出估算报告失败", e);
        }
    }
}
