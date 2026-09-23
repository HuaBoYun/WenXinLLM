package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ncv65.BudgetExecution;
import com.management.accountant.service.ncv65.IBudgetExecutionService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算执行监控控制器
 * 
 * @description 预算执行监控管理API接口，支持预算执行情况的实时监控和分析
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算执行监控管理")
@RestController
@RequestMapping("/budget/execution")
@Validated
public class BudgetExecutionController {

    @Resource
    private IBudgetExecutionService budgetExecutionService;

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建预算执行监控
     */
    @ApiOperation("创建预算执行监控")
    @PostMapping
    public MyJsonBean<String> createBudgetExecution(@Valid @RequestBody BudgetExecution execution) {
        try {
            boolean result = budgetExecutionService.createBudgetExecution(execution);
            if (result) {
                return MyJsonBean.successData(execution.getId(),  "预算执行监控创建成功");
            } else {
                return MyJsonBean.error("预算执行监控创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算执行监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("创建预算执行监控失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算执行监控
     */
    @ApiOperation("更新预算执行监控")
    @PutMapping("/{id}")
    public MyJsonBean<Boolean> updateBudgetExecution(
            @ApiParam("执行监控ID") @PathVariable String id,
            @Valid @RequestBody BudgetExecution execution) {
        try {
            execution.setId(id);
            boolean result = budgetExecutionService.updateBudgetExecution(execution);
            if (result) {
                return MyJsonBean.successData(true,  "预算执行监控更新成功");
            } else {
                return MyJsonBean.error("预算执行监控更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算执行监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("更新预算执行监控失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算执行监控
     */
    @ApiOperation("删除预算执行监控")
    @DeleteMapping("/{id}")
    public MyJsonBean<Boolean> deleteBudgetExecution(@ApiParam("执行监控ID") @PathVariable String id) {
        try {
            boolean result = budgetExecutionService.deleteBudgetExecution(id);
            if (result) {
                return MyJsonBean.successData(true,  "预算执行监控删除成功");
            } else {
                return MyJsonBean.error("预算执行监控删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算执行监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("删除预算执行监控失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算执行监控
     */
    @ApiOperation("批量删除预算执行监控")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetExecutions(@RequestBody List<String> ids) {
        try {
            boolean result = budgetExecutionService.batchDeleteBudgetExecutions(ids);
            if (result) {
                return MyJsonBean.successData(true,  "批量删除预算执行监控成功");
            } else {
                return MyJsonBean.error("批量删除预算执行监控失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算执行监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量删除预算执行监控失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算执行监控
     */
    @ApiOperation("根据ID查询预算执行监控")
    @GetMapping("/{id}")
    public MyJsonBean<BudgetExecution> getBudgetExecutionById(@ApiParam("执行监控ID") @PathVariable String id) {
        try {
            BudgetExecution execution = budgetExecutionService.getBudgetExecutionById(id);
            return MyJsonBean.successData(execution,  "查询成功");
        } catch (Exception e) {
            log.error("查询预算执行监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询预算执行监控失败：" + e.getMessage());
        }
    }

    /**
     * 根据编码查询预算执行监控
     */
    @ApiOperation("根据编码查询预算执行监控")
    @GetMapping("/code/{executionCode}")
    public MyJsonBean<BudgetExecution> getBudgetExecutionByCode(@ApiParam("执行编码") @PathVariable String executionCode) {
        try {
            BudgetExecution execution = budgetExecutionService.getBudgetExecutionByCode(executionCode);
            return MyJsonBean.successData(execution,  "查询成功");
        } catch (Exception e) {
            log.error("根据编码查询预算执行监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据编码查询预算执行监控失败：" + e.getMessage());
        }
    }

    // ==================== 查询操作 ====================

    /**
     * 分页查询预算执行监控
     */
    @ApiOperation("分页查询预算执行监控")
    @PostMapping("/page")
    public MyJsonBean<IPage<BudgetExecution>> getBudgetExecutionPage(@RequestBody Map<String, Object> params) {
        try {
            Integer current = (Integer) params.getOrDefault("current", 1);
            Integer size = (Integer) params.getOrDefault("size", 10);
            IPage<BudgetExecution> page = budgetExecutionService.getBudgetExecutionPage(current, size, params);
            return MyJsonBean.successData(page,  "查询成功");
        } catch (Exception e) {
            log.error("分页查询预算执行监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分页查询预算执行监控失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算年度查询执行列表
     */
    @ApiOperation("根据预算年度查询执行列表")
    @GetMapping("/fiscal-year/{fiscalYear}")
    public MyJsonBean<List<BudgetExecution>> getBudgetExecutionsByFiscalYear(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            List<BudgetExecution> executions = budgetExecutionService.getBudgetExecutionsByFiscalYear(fiscalYear);
            return MyJsonBean.successData(executions,  "查询成功");
        } catch (Exception e) {
            log.error("根据预算年度查询执行列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据预算年度查询执行列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据组织ID查询执行列表
     */
    @ApiOperation("根据组织ID查询执行列表")
    @GetMapping("/organization/{organizationId}")
    public MyJsonBean<List<BudgetExecution>> getBudgetExecutionsByOrganizationId(@ApiParam("组织ID") @PathVariable String organizationId) {
        try {
            List<BudgetExecution> executions = budgetExecutionService.getBudgetExecutionsByOrganizationId(organizationId);
            return MyJsonBean.successData(executions,  "查询成功");
        } catch (Exception e) {
            log.error("根据组织ID查询执行列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据组织ID查询执行列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询我负责的执行监控列表
     */
    @ApiOperation("查询我负责的执行监控列表")
    @GetMapping("/my-executions")
    public MyJsonBean<List<BudgetExecution>> getMyExecutions(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetExecution> executions = budgetExecutionService.getMyExecutions(userId);
            return MyJsonBean.successData(executions,  "查询成功");
        } catch (Exception e) {
            log.error("查询我负责的执行监控列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询我负责的执行监控列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询预警执行列表
     */
    @ApiOperation("查询预警执行列表")
    @GetMapping("/warnings")
    public MyJsonBean<List<BudgetExecution>> getWarningExecutions() {
        try {
            List<BudgetExecution> executions = budgetExecutionService.getWarningExecutions();
            return MyJsonBean.successData(executions,  "查询成功");
        } catch (Exception e) {
            log.error("查询预警执行列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询预警执行列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询超支执行列表
     */
    @ApiOperation("查询超支执行列表")
    @GetMapping("/over-budget")
    public MyJsonBean<List<BudgetExecution>> getOverBudgetExecutions() {
        try {
            List<BudgetExecution> executions = budgetExecutionService.getOverBudgetExecutions();
            return MyJsonBean.successData(executions,  "查询成功");
        } catch (Exception e) {
            log.error("查询超支执行列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询超支执行列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询失控执行列表
     */
    @ApiOperation("查询失控执行列表")
    @GetMapping("/uncontrolled")
    public MyJsonBean<List<BudgetExecution>> getUncontrolledExecutions() {
        try {
            List<BudgetExecution> executions = budgetExecutionService.getUncontrolledExecutions();
            return MyJsonBean.successData(executions,  "查询成功");
        } catch (Exception e) {
            log.error("查询失控执行列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询失控执行列表失败：" + e.getMessage());
        }
    }

    // ==================== 执行监控操作 ====================

    /**
     * 更新执行数据
     */
    @ApiOperation("更新执行数据")
    @PostMapping("/{id}/update-data")
    public MyJsonBean<Boolean> updateExecutionData(
            @ApiParam("执行ID") @PathVariable String id,
            @ApiParam("实际金额") @RequestParam BigDecimal actualAmount,
            @ApiParam("已使用金额") @RequestParam BigDecimal usedAmount) {
        try {
            boolean result = budgetExecutionService.updateExecutionData(id, actualAmount, usedAmount);
            if (result) {
                return MyJsonBean.successData(true,  "更新执行数据成功");
            } else {
                return MyJsonBean.error("更新执行数据失败");
            }
        } catch (Exception e) {
            log.error("更新执行数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("更新执行数据失败：" + e.getMessage());
        }
    }

    /**
     * 更新执行状态
     */
    @ApiOperation("更新执行状态")
    @PostMapping("/{id}/update-status")
    public MyJsonBean<Boolean> updateExecutionStatus(
            @ApiParam("执行ID") @PathVariable String id,
            @ApiParam("执行状态") @RequestParam String executionStatus,
            @ApiParam("控制状态") @RequestParam(required = false) String controlStatus,
            @ApiParam("预警级别") @RequestParam(required = false) String warningLevel,
            @ApiParam("预警原因") @RequestParam(required = false) String warningReason) {
        try {
            boolean result = budgetExecutionService.updateExecutionStatus(id, executionStatus, controlStatus, warningLevel, warningReason);
            if (result) {
                return MyJsonBean.successData(true,  "更新执行状态成功");
            } else {
                return MyJsonBean.error("更新执行状态失败");
            }
        } catch (Exception e) {
            log.error("更新执行状态失败：{}", e.getMessage(), e);
            return MyJsonBean.error("更新执行状态失败：" + e.getMessage());
        }
    }

    /**
     * 启用自动监控
     */
    @ApiOperation("启用自动监控")
    @PostMapping("/{id}/enable-auto-monitoring")
    public MyJsonBean<Boolean> enableAutoMonitoring(@ApiParam("执行ID") @PathVariable String id) {
        try {
            boolean result = budgetExecutionService.enableAutoMonitoring(id);
            if (result) {
                return MyJsonBean.successData(true,  "启用自动监控成功");
            } else {
                return MyJsonBean.error("启用自动监控失败");
            }
        } catch (Exception e) {
            log.error("启用自动监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("启用自动监控失败：" + e.getMessage());
        }
    }

    /**
     * 停用自动监控
     */
    @ApiOperation("停用自动监控")
    @PostMapping("/{id}/disable-auto-monitoring")
    public MyJsonBean<Boolean> disableAutoMonitoring(@ApiParam("执行ID") @PathVariable String id) {
        try {
            boolean result = budgetExecutionService.disableAutoMonitoring(id);
            if (result) {
                return MyJsonBean.successData(true,  "停用自动监控成功");
            } else {
                return MyJsonBean.error("停用自动监控失败");
            }
        } catch (Exception e) {
            log.error("停用自动监控失败：{}", e.getMessage(), e);
            return MyJsonBean.error("停用自动监控失败：" + e.getMessage());
        }
    }

    /**
     * 手动刷新执行数据
     */
    @ApiOperation("手动刷新执行数据")
    @PostMapping("/{id}/refresh")
    public MyJsonBean<Boolean> refreshExecutionData(@ApiParam("执行ID") @PathVariable String id) {
        try {
            boolean result = budgetExecutionService.refreshExecutionData(id);
            if (result) {
                return MyJsonBean.successData(true,  "刷新执行数据成功");
            } else {
                return MyJsonBean.error("刷新执行数据失败");
            }
        } catch (Exception e) {
            log.error("刷新执行数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("刷新执行数据失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新执行状态
     */
    @ApiOperation("批量更新执行状态")
    @PostMapping("/batch-update-status")
    public MyJsonBean<Integer> batchUpdateExecutionStatus(
            @RequestBody List<String> executionIds,
            @ApiParam("状态") @RequestParam String status) {
        try {
            int count = budgetExecutionService.batchUpdateExecutionStatus(executionIds, status);
            return MyJsonBean.successData(count,  "批量更新执行状态成功，处理了" + count + "个执行监控");
        } catch (Exception e) {
            log.error("批量更新执行状态失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量更新执行状态失败：" + e.getMessage());
        }
    }

    // ==================== 执行分析 ====================

    /**
     * 分析执行趋势
     */
    @ApiOperation("分析执行趋势")
    @PostMapping("/analyze-trend")
    public MyJsonBean<Map<String, Object>> analyzeExecutionTrend(
            @ApiParam("组织ID") @RequestParam String organizationId,
            @ApiParam("指标ID") @RequestParam String indicatorId,
            @ApiParam("开始期间") @RequestParam String startPeriod,
            @ApiParam("结束期间") @RequestParam String endPeriod) {
        try {
            Map<String, Object> result = budgetExecutionService.analyzeExecutionTrend(organizationId, indicatorId, startPeriod, endPeriod);
            return MyJsonBean.successData(result,  "执行趋势分析成功");
        } catch (Exception e) {
            log.error("分析执行趋势失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分析执行趋势失败：" + e.getMessage());
        }
    }

    /**
     * 执行对比分析
     */
    @ApiOperation("执行对比分析")
    @PostMapping("/compare")
    public MyJsonBean<Map<String, Object>> compareExecutions(@RequestBody Map<String, Object> params) {
        try {
            List<String> organizationIds = (List<String>) params.get("organizationIds");
            String indicatorId = (String) params.get("indicatorId");
            String executionPeriod = (String) params.get("executionPeriod");
            
            Map<String, Object> result = budgetExecutionService.compareExecutions(organizationIds, indicatorId, executionPeriod);
            return MyJsonBean.successData(result,  "执行对比分析成功");
        } catch (Exception e) {
            log.error("执行对比分析失败：{}", e.getMessage(), e);
            return MyJsonBean.error("执行对比分析失败：" + e.getMessage());
        }
    }

    /**
     * 执行偏差分析
     */
    @ApiOperation("执行偏差分析")
    @PostMapping("/{id}/analyze-deviation")
    public MyJsonBean<Map<String, Object>> analyzeExecutionDeviation(@ApiParam("执行ID") @PathVariable String id) {
        try {
            Map<String, Object> result = budgetExecutionService.analyzeExecutionDeviation(id);
            return MyJsonBean.successData(result,  "执行偏差分析成功");
        } catch (Exception e) {
            log.error("执行偏差分析失败：{}", e.getMessage(), e);
            return MyJsonBean.error("执行偏差分析失败：" + e.getMessage());
        }
    }

    /**
     * 执行风险评估
     */
    @ApiOperation("执行风险评估")
    @PostMapping("/{id}/assess-risk")
    public MyJsonBean<Map<String, Object>> assessExecutionRisk(@ApiParam("执行ID") @PathVariable String id) {
        try {
            Map<String, Object> result = budgetExecutionService.assessExecutionRisk(id);
            return MyJsonBean.successData(result,  "执行风险评估成功");
        } catch (Exception e) {
            log.error("执行风险评估失败：{}", e.getMessage(), e);
            return MyJsonBean.error("执行风险评估失败：" + e.getMessage());
        }
    }

    // ==================== 预警管理 ====================

    /**
     * 设置预警规则
     */
    @ApiOperation("设置预警规则")
    @PostMapping("/{id}/set-warning-rules")
    public MyJsonBean<Boolean> setWarningRules(
            @ApiParam("执行ID") @PathVariable String id,
            @ApiParam("预警规则") @RequestParam String warningRules) {
        try {
            boolean result = budgetExecutionService.setWarningRules(id, warningRules);
            if (result) {
                return MyJsonBean.successData(true,  "设置预警规则成功");
            } else {
                return MyJsonBean.error("设置预警规则失败");
            }
        } catch (Exception e) {
            log.error("设置预警规则失败：{}", e.getMessage(), e);
            return MyJsonBean.error("设置预警规则失败：" + e.getMessage());
        }
    }

    /**
     * 触发预警
     */
    @ApiOperation("触发预警")
    @PostMapping("/{id}/trigger-warning")
    public MyJsonBean<Boolean> triggerWarning(
            @ApiParam("执行ID") @PathVariable String id,
            @ApiParam("预警级别") @RequestParam String warningLevel,
            @ApiParam("预警原因") @RequestParam String warningReason) {
        try {
            boolean result = budgetExecutionService.triggerWarning(id, warningLevel, warningReason);
            if (result) {
                return MyJsonBean.successData(true,  "触发预警成功");
            } else {
                return MyJsonBean.error("触发预警失败");
            }
        } catch (Exception e) {
            log.error("触发预警失败：{}", e.getMessage(), e);
            return MyJsonBean.error("触发预警失败：" + e.getMessage());
        }
    }

    /**
     * 解除预警
     */
    @ApiOperation("解除预警")
    @PostMapping("/{id}/clear-warning")
    public MyJsonBean<Boolean> clearWarning(@ApiParam("执行ID") @PathVariable String id) {
        try {
            boolean result = budgetExecutionService.clearWarning(id);
            if (result) {
                return MyJsonBean.successData(true,  "解除预警成功");
            } else {
                return MyJsonBean.error("解除预警失败");
            }
        } catch (Exception e) {
            log.error("解除预警失败：{}", e.getMessage(), e);
            return MyJsonBean.error("解除预警失败：" + e.getMessage());
        }
    }

    /**
     * 获取预警统计
     */
    @ApiOperation("获取预警统计")
    @GetMapping("/warning-statistics/{fiscalYear}")
    public MyJsonBean<Map<String, Object>> getWarningStatistics(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            Map<String, Object> statistics = budgetExecutionService.getWarningStatistics(fiscalYear);
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("获取预警统计失败：{}", e.getMessage(), e);
            return MyJsonBean.error("获取预警统计失败：" + e.getMessage());
        }
    }

    // ==================== 控制管理 ====================

    /**
     * 冻结预算
     */
    @ApiOperation("冻结预算")
    @PostMapping("/{id}/freeze")
    public MyJsonBean<Boolean> freezeBudget(
            @ApiParam("执行ID") @PathVariable String id,
            @ApiParam("冻结原因") @RequestParam String freezeReason) {
        try {
            boolean result = budgetExecutionService.freezeBudget(id, freezeReason);
            if (result) {
                return MyJsonBean.successData(true,  "冻结预算成功");
            } else {
                return MyJsonBean.error("冻结预算失败");
            }
        } catch (Exception e) {
            log.error("冻结预算失败：{}", e.getMessage(), e);
            return MyJsonBean.error("冻结预算失败：" + e.getMessage());
        }
    }

    /**
     * 解冻预算
     */
    @ApiOperation("解冻预算")
    @PostMapping("/{id}/unfreeze")
    public MyJsonBean<Boolean> unfreezeBudget(@ApiParam("执行ID") @PathVariable String id) {
        try {
            boolean result = budgetExecutionService.unfreezeBudget(id);
            if (result) {
                return MyJsonBean.successData(true,  "解冻预算成功");
            } else {
                return MyJsonBean.error("解冻预算失败");
            }
        } catch (Exception e) {
            log.error("解冻预算失败：{}", e.getMessage(), e);
            return MyJsonBean.error("解冻预算失败：" + e.getMessage());
        }
    }

    // ==================== 统计分析 ====================

    /**
     * 获取执行统计信息
     */
    @ApiOperation("获取执行统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetExecutionStatistics() {
        try {
            Map<String, Object> statistics = budgetExecutionService.getBudgetExecutionStatistics();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("获取执行统计信息失败：{}", e.getMessage(), e);
            return MyJsonBean.error("获取执行统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 按执行状态统计数量
     */
    @ApiOperation("按执行状态统计数量")
    @GetMapping("/statistics/by-status")
    public MyJsonBean<List<Map<String, Object>>> getBudgetExecutionCountByStatus() {
        try {
            List<Map<String, Object>> statistics = budgetExecutionService.getBudgetExecutionCountByStatus();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("按执行状态统计数量失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按执行状态统计数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计执行金额
     */
    @ApiOperation("统计执行金额")
    @GetMapping("/statistics/amount/{fiscalYear}")
    public MyJsonBean<Map<String, Object>> getBudgetExecutionAmountSummary(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            Map<String, Object> statistics = budgetExecutionService.getBudgetExecutionAmountSummary(fiscalYear);
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("统计执行金额失败：{}", e.getMessage(), e);
            return MyJsonBean.error("统计执行金额失败：" + e.getMessage());
        }
    }

    // ==================== 数据导入导出 ====================

    /**
     * 导出执行数据
     */
    @ApiOperation("导出执行数据")
    @PostMapping("/export")
    public MyJsonBean<String> exportExecutions(@RequestBody List<String> executionIds) {
        try {
            String filePath = budgetExecutionService.exportExecutions(executionIds);
            return MyJsonBean.successData(filePath,  "导出成功");
        } catch (Exception e) {
            log.error("导出执行数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("导出执行数据失败：" + e.getMessage());
        }
    }

    /**
     * 导入执行数据
     */
    @ApiOperation("导入执行数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importExecutions(@ApiParam("文件路径") @RequestParam String filePath) {
        try {
            Map<String, Object> result = budgetExecutionService.importExecutions(filePath);
            return MyJsonBean.successData(result,  "导入成功");
        } catch (Exception e) {
            log.error("导入执行数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("导入执行数据失败：" + e.getMessage());
        }
    }
}
