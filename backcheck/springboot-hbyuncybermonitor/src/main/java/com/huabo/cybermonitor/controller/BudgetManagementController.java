package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.BudgetManagement;
import com.huabo.cybermonitor.service.IBudgetManagementService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.BudgetManagementQueryVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 预算管理控制器
 *
 * @author huabo
 * @since 2024-12-12
 */
@RestController
@RequestMapping("/v1/enterprise/financial/budget")
@Tag(name="预算管理",description="预算管理")
public class BudgetManagementController {

	private static final Logger log = LoggerFactory.getLogger(BudgetManagementController.class);

    @Autowired
    private IBudgetManagementService budgetManagementService;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public R<IPage<BudgetManagement>> getBudgetManagementPage(@RequestBody BudgetManagementQueryVo queryVo) {
        try {
            IPage<BudgetManagement> page = budgetManagementService.getBudgetManagementPage(queryVo);
            return R.success(page);
        } catch (Exception e) {
            log.error("分页查询预算管理失败", e);
            return R.fail("查询失败");
        }
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{budgetId}")
    public R<BudgetManagement> getBudgetManagementById(@Parameter(description="预算ID") @PathVariable String budgetId) {
        try {
            BudgetManagement budget = budgetManagementService.getById(budgetId);
            return R.success(budget);
        } catch (Exception e) {
            log.error("获取预算管理详情失败", e);
            return R.fail("获取详情失败");
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Boolean> addBudgetManagement(@RequestBody BudgetManagement budgetManagement) {
        try {
            boolean result = budgetManagementService.addBudgetManagement(budgetManagement);
            return result ? R.success(true, "新增成功") : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增预算管理失败", e);
            return R.fail("新增失败");
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/update")
    public R<Boolean> updateBudgetManagement(@RequestBody BudgetManagement budgetManagement) {
        try {
            boolean result = budgetManagementService.updateBudgetManagement(budgetManagement);
            return result ? R.success(true, "修改成功") : R.fail("修改失败");
        } catch (Exception e) {
            log.error("修改预算管理失败", e);
            return R.fail("修改失败");
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{budgetId}")
    public R<Boolean> deleteBudgetManagement(@Parameter(description="预算ID") @PathVariable String budgetId,
                                            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.deleteBudgetManagement(budgetId, updateBy);
            return result ? R.success(true, "删除成功") : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除预算管理失败", e);
            return R.fail("删除失败");
        }
    }

    @Operation(summary = "批量操作")
    @DeleteMapping("/batchDelete")
    public R<Boolean> batchDeleteBudgetManagement(@RequestBody List<String> budgetIds,
                                                  @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.batchDeleteBudgetManagement(budgetIds, updateBy);
            return result ? R.success(true, "批量删除成功") : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除预算管理失败", e);
            return R.fail("批量删除失败");
        }
    }

    @Operation(summary = "启用")
    @PutMapping("/startCompilation/{budgetId}")
    public R<Boolean> startBudgetCompilation(@Parameter(description="预算ID") @PathVariable String budgetId,
                                            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.startBudgetCompilation(budgetId, updateBy);
            return result ? R.success(true, "启动成功") : R.fail("启动失败");
        } catch (Exception e) {
            log.error("启动预算编制失败", e);
            return R.fail("启动失败");
        }
    }

    @Operation(summary = "pauseBudgetCompilation")
    @PutMapping("/pauseCompilation/{budgetId}")
    public R<Boolean> pauseBudgetCompilation(@Parameter(description="预算ID") @PathVariable String budgetId,
                                            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.pauseBudgetCompilation(budgetId, updateBy);
            return result ? R.success(true, "暂停成功") : R.fail("暂停失败");
        } catch (Exception e) {
            log.error("暂停预算编制失败", e);
            return R.fail("暂停失败");
        }
    }

    @Operation(summary = "resumeBudgetCompilation")
    @PutMapping("/resumeCompilation/{budgetId}")
    public R<Boolean> resumeBudgetCompilation(@Parameter(description="预算ID") @PathVariable String budgetId,
                                             @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.resumeBudgetCompilation(budgetId, updateBy);
            return result ? R.success(true, "恢复成功") : R.fail("恢复失败");
        } catch (Exception e) {
            log.error("恢复预算编制失败", e);
            return R.fail("恢复失败");
        }
    }

    @Operation(summary = "completeBudgetCompilation")
    @PutMapping("/completeCompilation/{budgetId}")
    public R<Boolean> completeBudgetCompilation(@Parameter(description="预算ID") @PathVariable String budgetId,
                                               @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.completeBudgetCompilation(budgetId, updateBy);
            return result ? R.success(true, "完成成功") : R.fail("完成失败");
        } catch (Exception e) {
            log.error("完成预算编制失败", e);
            return R.fail("完成失败");
        }
    }

    @Operation(summary = "撤销")
    @PutMapping("/cancelCompilation/{budgetId}")
    public R<Boolean> cancelBudgetCompilation(@Parameter(description="预算ID") @PathVariable String budgetId,
                                             @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.cancelBudgetCompilation(budgetId, updateBy);
            return result ? R.success(true, "取消成功") : R.fail("取消失败");
        } catch (Exception e) {
            log.error("取消预算编制失败", e);
            return R.fail("取消失败");
        }
    }

    @Operation(summary = "提交")
    @PutMapping("/submitForApproval/{budgetId}")
    public R<Boolean> submitForApproval(@Parameter(description="预算ID") @PathVariable String budgetId,
                                       @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.submitForApproval(budgetId, updateBy);
            return result ? R.success(true, "提交审批成功") : R.fail("提交审批失败");
        } catch (Exception e) {
            log.error("提交审批失败", e);
            return R.fail("提交审批失败");
        }
    }

    @Operation(summary = "firstApproval")
    @PutMapping("/firstApproval/{budgetId}")
    public R<Boolean> firstApproval(@Parameter(description="预算ID") @PathVariable String budgetId,
                                   @Parameter(description="审批人") @RequestParam String approver,
                                   @Parameter(description="审批意见") @RequestParam String opinion,
                                   @Parameter(description="审批结果") @RequestParam String approvalResult,
                                   @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.firstApproval(budgetId, approver, opinion, approvalResult, updateBy);
            return result ? R.success(true, "初审成功") : R.fail("初审失败");
        } catch (Exception e) {
            log.error("初审失败", e);
            return R.fail("初审失败");
        }
    }

    @Operation(summary = "finalApproval")
    @PutMapping("/finalApproval/{budgetId}")
    public R<Boolean> finalApproval(@Parameter(description="预算ID") @PathVariable String budgetId,
                                   @Parameter(description="审批人") @RequestParam String approver,
                                   @Parameter(description="审批意见") @RequestParam String opinion,
                                   @Parameter(description="审批结果") @RequestParam String approvalResult,
                                   @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.finalApproval(budgetId, approver, opinion, approvalResult, updateBy);
            return result ? R.success(true, "终审成功") : R.fail("终审失败");
        } catch (Exception e) {
            log.error("终审失败", e);
            return R.fail("终审失败");
        }
    }

    @Operation(summary = "启用")
    @PutMapping("/startExecution/{budgetId}")
    public R<Boolean> startBudgetExecution(@Parameter(description="预算ID") @PathVariable String budgetId,
                                          @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.startBudgetExecution(budgetId, updateBy);
            return result ? R.success(true, "启动执行成功") : R.fail("启动执行失败");
        } catch (Exception e) {
            log.error("启动预算执行失败", e);
            return R.fail("启动执行失败");
        }
    }

    @Operation(summary = "pauseBudgetExecution")
    @PutMapping("/pauseExecution/{budgetId}")
    public R<Boolean> pauseBudgetExecution(@Parameter(description="预算ID") @PathVariable String budgetId,
                                          @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.pauseBudgetExecution(budgetId, updateBy);
            return result ? R.success(true, "暂停执行成功") : R.fail("暂停执行失败");
        } catch (Exception e) {
            log.error("暂停预算执行失败", e);
            return R.fail("暂停执行失败");
        }
    }

    @Operation(summary = "resumeBudgetExecution")
    @PutMapping("/resumeExecution/{budgetId}")
    public R<Boolean> resumeBudgetExecution(@Parameter(description="预算ID") @PathVariable String budgetId,
                                           @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.resumeBudgetExecution(budgetId, updateBy);
            return result ? R.success(true, "恢复执行成功") : R.fail("恢复执行失败");
        } catch (Exception e) {
            log.error("恢复预算执行失败", e);
            return R.fail("恢复执行失败");
        }
    }

    @Operation(summary = "completeBudgetExecution")
    @PutMapping("/completeExecution/{budgetId}")
    public R<Boolean> completeBudgetExecution(@Parameter(description="预算ID") @PathVariable String budgetId,
                                             @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.completeBudgetExecution(budgetId, updateBy);
            return result ? R.success(true, "完成执行成功") : R.fail("完成执行失败");
        } catch (Exception e) {
            log.error("完成预算执行失败", e);
            return R.fail("完成执行失败");
        }
    }

    @Operation(summary = "adjustBudget")
    @PutMapping("/adjust/{budgetId}")
    public R<Boolean> adjustBudget(@Parameter(description="预算ID") @PathVariable String budgetId,
                                  @Parameter(description="调整原因") @RequestParam String adjustmentReason,
                                  @Parameter(description="调整内容") @RequestParam String adjustmentContent,
                                  @Parameter(description="调整金额") @RequestParam BigDecimal adjustmentAmount,
                                  @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.adjustBudget(budgetId, adjustmentReason, adjustmentContent, adjustmentAmount, updateBy);
            return result ? R.success(true, "预算调整成功") : R.fail("预算调整失败");
        } catch (Exception e) {
            log.error("预算调整失败", e);
            return R.fail("预算调整失败");
        }
    }

    @Operation(summary = "monitorBudget")
    @PutMapping("/monitor/{budgetId}")
    public R<Boolean> monitorBudget(@Parameter(description="预算ID") @PathVariable String budgetId,
                                   @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.monitorBudget(budgetId, updateBy);
            return result ? R.success(true, "预算监控成功") : R.fail("预算监控失败");
        } catch (Exception e) {
            log.error("预算监控失败", e);
            return R.fail("预算监控失败");
        }
    }

    @Operation(summary = "evaluatePerformance")
    @PutMapping("/evaluatePerformance/{budgetId}")
    public R<Boolean> evaluatePerformance(@Parameter(description="预算ID") @PathVariable String budgetId,
                                         @Parameter(description="绩效评估结果") @RequestParam String performanceEvaluationResult,
                                         @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.evaluatePerformance(budgetId, performanceEvaluationResult, updateBy);
            return result ? R.success(true, "绩效评估成功") : R.fail("绩效评估失败");
        } catch (Exception e) {
            log.error("绩效评估失败", e);
            return R.fail("绩效评估失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateBudgetStatus")
    public R<Boolean> batchUpdateBudgetStatus(@RequestBody List<String> budgetIds,
                                             @Parameter(description="状态") @RequestParam String status,
                                             @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.batchUpdateBudgetStatus(budgetIds, status, updateBy);
            return result ? R.success(true, "批量更新预算状态成功") : R.fail("批量更新预算状态失败");
        } catch (Exception e) {
            log.error("批量更新预算状态失败", e);
            return R.fail("批量更新预算状态失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateApprovalStatus")
    public R<Boolean> batchUpdateApprovalStatus(@RequestBody List<String> budgetIds,
                                               @Parameter(description="状态") @RequestParam String status,
                                               @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.batchUpdateApprovalStatus(budgetIds, status, updateBy);
            return result ? R.success(true, "批量更新审批状态成功") : R.fail("批量更新审批状态失败");
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            return R.fail("批量更新审批状态失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateExecutionStatus")
    public R<Boolean> batchUpdateExecutionStatus(@RequestBody List<String> budgetIds,
                                                 @Parameter(description="状态") @RequestParam String status,
                                                 @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = budgetManagementService.batchUpdateBudgetStatus(budgetIds, status, updateBy);
            return result ? R.success(true, "批量更新执行状态成功") : R.fail("批量更新执行状态失败");
        } catch (Exception e) {
            log.error("批量更新执行状态失败", e);
            return R.fail("批量更新执行状态失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/statistics/{enterpriseId}")
    public R<Map<String, Object>> getStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> statistics = budgetManagementService.getStatisticsByEnterpriseId(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取预算管理统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/budgetTypeDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getBudgetTypeDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = budgetManagementService.getBudgetTypeDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取预算类型分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/budgetStatusDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getBudgetStatusDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = budgetManagementService.getBudgetStatusDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取预算状态分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/upcomingBudgets/{enterpriseId}")
    public R<List<BudgetManagement>> getUpcomingBudgets(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                        @Parameter(description="天数") @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<BudgetManagement> budgets = budgetManagementService.getUpcomingBudgets(enterpriseId, days);
            return R.success(budgets);
        } catch (Exception e) {
            log.error("获取即将到期的预算失败", e);
            return R.fail("获取预算失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/overdueBudgets/{enterpriseId}")
    public R<List<BudgetManagement>> getOverdueBudgets(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<BudgetManagement> budgets = budgetManagementService.getOverdueBudgets(enterpriseId);
            return R.success(budgets);
        } catch (Exception e) {
            log.error("获取逾期的预算失败", e);
            return R.fail("获取预算失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/pendingApprovalBudgets/{enterpriseId}")
    public R<List<BudgetManagement>> getPendingApprovalBudgets(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<BudgetManagement> budgets = budgetManagementService.getPendingApprovalBudgets(enterpriseId);
            return R.success(budgets);
        } catch (Exception e) {
            log.error("获取待审批的预算失败", e);
            return R.fail("获取预算失败");
        }
    }

    @Operation(summary = "")
    @PostMapping("/export")
    public R<Map<String, Object>> exportBudgetManagement(@RequestBody BudgetManagementQueryVo queryVo) {
        try {
            Map<String, Object> result = budgetManagementService.exportBudgetManagement(queryVo);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出预算管理失败", e);
            return R.fail("导出失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/generateReport/{enterpriseId}")
    public R<Map<String, Object>> generateBudgetManagementReport(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                                 @Parameter(description="报告类型") @RequestParam String reportType) {
        try {
            Map<String, Object> result = budgetManagementService.generateBudgetManagementReport(enterpriseId, reportType);
            return R.success(result);
        } catch (Exception e) {
            log.error("生成预算管理报告失败", e);
            return R.fail("生成报告失败");
        }
    }
}
