package com.huabo.cybermonitor.controller;

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
import com.huabo.cybermonitor.entity.OperationPlan;
import com.huabo.cybermonitor.service.IOperationPlanService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.OperationPlanQueryVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 经营计划管理控制器
 *
 * @author huabo
 * @since 2024-12-12
 */
@RestController
@RequestMapping("/v1/enterprise/operation/plan")
@Tag(name="经营计划管理",description="经营计划管理")
public class OperationPlanController {

	private static final Logger log = LoggerFactory.getLogger(OperationPlanController.class);

    @Autowired
    private IOperationPlanService operationPlanService;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public R<IPage<OperationPlan>> getOperationPlanPage(@RequestBody OperationPlanQueryVo queryVo) {
        try {
            IPage<OperationPlan> page = operationPlanService.getOperationPlanPage(queryVo);
            return R.success(page);
        } catch (Exception e) {
            log.error("分页查询经营计划管理失败", e);
            return R.fail("查询失败");
        }
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{planId}")
    public R<OperationPlan> getOperationPlanById(@Parameter(description="计划ID") @PathVariable String planId) {
        try {
            OperationPlan plan = operationPlanService.getById(planId);
            return R.success(plan);
        } catch (Exception e) {
            log.error("获取经营计划管理详情失败", e);
            return R.fail("获取详情失败");
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Boolean> addOperationPlan(@RequestBody OperationPlan operationPlan) {
        try {
            boolean result = operationPlanService.addOperationPlan(operationPlan);
            return result ? R.success(true, "新增成功") : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增经营计划管理失败", e);
            return R.fail("新增失败");
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/update")
    public R<Boolean> updateOperationPlan(@RequestBody OperationPlan operationPlan) {
        try {
            boolean result = operationPlanService.updateOperationPlan(operationPlan);
            return result ? R.success(true, "修改成功") : R.fail("修改失败");
        } catch (Exception e) {
            log.error("修改经营计划管理失败", e);
            return R.fail("修改失败");
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{planId}")
    public R<Boolean> deleteOperationPlan(@Parameter(description="计划ID") @PathVariable String planId,
                                          @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.deleteOperationPlan(planId, updateBy);
            return result ? R.success(true, "删除成功") : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除经营计划管理失败", e);
            return R.fail("删除失败");
        }
    }

    @Operation(summary = "批量操作")
    @DeleteMapping("/batchDelete")
    public R<Boolean> batchDeleteOperationPlan(@RequestBody List<String> planIds,
                                               @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.batchDeleteOperationPlan(planIds, updateBy);
            return result ? R.success(true, "批量删除成功") : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除经营计划管理失败", e);
            return R.fail("批量删除失败");
        }
    }

    @Operation(summary = "启用")
    @PutMapping("/startFormulation/{planId}")
    public R<Boolean> startPlanFormulation(@Parameter(description="计划ID") @PathVariable String planId,
                                          @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.startPlanFormulation(planId, updateBy);
            return result ? R.success(true, "启动成功") : R.fail("启动失败");
        } catch (Exception e) {
            log.error("启动计划制定失败", e);
            return R.fail("启动失败");
        }
    }

    @Operation(summary = "pausePlanFormulation")
    @PutMapping("/pauseFormulation/{planId}")
    public R<Boolean> pausePlanFormulation(@Parameter(description="计划ID") @PathVariable String planId,
                                          @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.pausePlanFormulation(planId, updateBy);
            return result ? R.success(true, "暂停成功") : R.fail("暂停失败");
        } catch (Exception e) {
            log.error("暂停计划制定失败", e);
            return R.fail("暂停失败");
        }
    }

    @Operation(summary = "resumePlanFormulation")
    @PutMapping("/resumeFormulation/{planId}")
    public R<Boolean> resumePlanFormulation(@Parameter(description="计划ID") @PathVariable String planId,
                                           @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.resumePlanFormulation(planId, updateBy);
            return result ? R.success(true, "恢复成功") : R.fail("恢复失败");
        } catch (Exception e) {
            log.error("恢复计划制定失败", e);
            return R.fail("恢复失败");
        }
    }

    @Operation(summary = "completePlanFormulation")
    @PutMapping("/completeFormulation/{planId}")
    public R<Boolean> completePlanFormulation(@Parameter(description="计划ID") @PathVariable String planId,
                                             @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.completePlanFormulation(planId, updateBy);
            return result ? R.success(true, "完成成功") : R.fail("完成失败");
        } catch (Exception e) {
            log.error("完成计划制定失败", e);
            return R.fail("完成失败");
        }
    }

    @Operation(summary = "撤销")
    @PutMapping("/cancelFormulation/{planId}")
    public R<Boolean> cancelPlanFormulation(@Parameter(description="计划ID") @PathVariable String planId,
                                           @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.cancelPlanFormulation(planId, updateBy);
            return result ? R.success(true, "取消成功") : R.fail("取消失败");
        } catch (Exception e) {
            log.error("取消计划制定失败", e);
            return R.fail("取消失败");
        }
    }

    @Operation(summary = "提交")
    @PutMapping("/submitForApproval/{planId}")
    public R<Boolean> submitForApproval(@Parameter(description="计划ID") @PathVariable String planId,
                                       @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.submitForApproval(planId, updateBy);
            return result ? R.success(true, "提交审批成功") : R.fail("提交审批失败");
        } catch (Exception e) {
            log.error("提交审批失败", e);
            return R.fail("提交审批失败");
        }
    }

    @Operation(summary = "firstApproval")
    @PutMapping("/firstApproval/{planId}")
    public R<Boolean> firstApproval(@Parameter(description="计划ID") @PathVariable String planId,
                                   @Parameter(description="审批人") @RequestParam String approver,
                                   @Parameter(description="审批意见") @RequestParam String opinion,
                                   @Parameter(description="审批结果") @RequestParam String approvalResult,
                                   @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.firstApproval(planId, approver, opinion, approvalResult, updateBy);
            return result ? R.success(true, "初审成功") : R.fail("初审失败");
        } catch (Exception e) {
            log.error("初审失败", e);
            return R.fail("初审失败");
        }
    }

    @Operation(summary = "finalApproval")
    @PutMapping("/finalApproval/{planId}")
    public R<Boolean> finalApproval(@Parameter(description="计划ID") @PathVariable String planId,
                                   @Parameter(description="审批人") @RequestParam String approver,
                                   @Parameter(description="审批意见") @RequestParam String opinion,
                                   @Parameter(description="审批结果") @RequestParam String approvalResult,
                                   @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.finalApproval(planId, approver, opinion, approvalResult, updateBy);
            return result ? R.success(true, "终审成功") : R.fail("终审失败");
        } catch (Exception e) {
            log.error("终审失败", e);
            return R.fail("终审失败");
        }
    }

    @Operation(summary = "启用")
    @PutMapping("/startExecution/{planId}")
    public R<Boolean> startPlanExecution(@Parameter(description="计划ID") @PathVariable String planId,
                                        @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.startPlanExecution(planId, updateBy);
            return result ? R.success(true, "启动执行成功") : R.fail("启动执行失败");
        } catch (Exception e) {
            log.error("启动计划执行失败", e);
            return R.fail("启动执行失败");
        }
    }

    @Operation(summary = "pausePlanExecution")
    @PutMapping("/pauseExecution/{planId}")
    public R<Boolean> pausePlanExecution(@Parameter(description="计划ID") @PathVariable String planId,
                                        @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.pausePlanExecution(planId, updateBy);
            return result ? R.success(true, "暂停执行成功") : R.fail("暂停执行失败");
        } catch (Exception e) {
            log.error("暂停计划执行失败", e);
            return R.fail("暂停执行失败");
        }
    }

    @Operation(summary = "resumePlanExecution")
    @PutMapping("/resumeExecution/{planId}")
    public R<Boolean> resumePlanExecution(@Parameter(description="计划ID") @PathVariable String planId,
                                         @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.resumePlanExecution(planId, updateBy);
            return result ? R.success(true, "恢复执行成功") : R.fail("恢复执行失败");
        } catch (Exception e) {
            log.error("恢复计划执行失败", e);
            return R.fail("恢复执行失败");
        }
    }

    @Operation(summary = "completePlanExecution")
    @PutMapping("/completeExecution/{planId}")
    public R<Boolean> completePlanExecution(@Parameter(description="计划ID") @PathVariable String planId,
                                           @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.completePlanExecution(planId, updateBy);
            return result ? R.success(true, "完成执行成功") : R.fail("完成执行失败");
        } catch (Exception e) {
            log.error("完成计划执行失败", e);
            return R.fail("完成执行失败");
        }
    }

    @Operation(summary = "adjustPlan")
    @PutMapping("/adjust/{planId}")
    public R<Boolean> adjustPlan(@Parameter(description="计划ID") @PathVariable String planId,
                                @Parameter(description="调整原因") @RequestParam String adjustmentReason,
                                @Parameter(description="调整内容") @RequestParam String adjustmentContent,
                                @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.adjustPlan(planId, adjustmentReason, adjustmentContent, updateBy);
            return result ? R.success(true, "计划调整成功") : R.fail("计划调整失败");
        } catch (Exception e) {
            log.error("计划调整失败", e);
            return R.fail("计划调整失败");
        }
    }

    @Operation(summary = "monitorPlan")
    @PutMapping("/monitor/{planId}")
    public R<Boolean> monitorPlan(@Parameter(description="计划ID") @PathVariable String planId,
                                 @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.monitorPlan(planId, updateBy);
            return result ? R.success(true, "计划监控成功") : R.fail("计划监控失败");
        } catch (Exception e) {
            log.error("计划监控失败", e);
            return R.fail("计划监控失败");
        }
    }

    @Operation(summary = "evaluateEffectiveness")
    @PutMapping("/evaluateEffectiveness/{planId}")
    public R<Boolean> evaluateEffectiveness(@Parameter(description="计划ID") @PathVariable String planId,
                                           @Parameter(description="效果评估") @RequestParam String effectivenessEvaluation,
                                           @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.evaluateEffectiveness(planId, effectivenessEvaluation, updateBy);
            return result ? R.success(true, "效果评估成功") : R.fail("效果评估失败");
        } catch (Exception e) {
            log.error("效果评估失败", e);
            return R.fail("效果评估失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdatePlanStatus")
    public R<Boolean> batchUpdatePlanStatus(@RequestBody List<String> planIds,
                                           @Parameter(description="状态") @RequestParam String status,
                                           @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.batchUpdatePlanStatus(planIds, status, updateBy);
            return result ? R.success(true, "批量更新计划状态成功") : R.fail("批量更新计划状态失败");
        } catch (Exception e) {
            log.error("批量更新计划状态失败", e);
            return R.fail("批量更新计划状态失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateApprovalStatus")
    public R<Boolean> batchUpdateApprovalStatus(@RequestBody List<String> planIds,
                                               @Parameter(description="状态") @RequestParam String status,
                                               @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.batchUpdateApprovalStatus(planIds, status, updateBy);
            return result ? R.success(true, "批量更新审批状态成功") : R.fail("批量更新审批状态失败");
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            return R.fail("批量更新审批状态失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateExecutionStatus")
    public R<Boolean> batchUpdateExecutionStatus(@RequestBody List<String> planIds,
                                                 @Parameter(description="状态") @RequestParam String status,
                                                 @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = operationPlanService.batchUpdateExecutionStatus(planIds, status, updateBy);
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
            Map<String, Object> statistics = operationPlanService.getStatisticsByEnterpriseId(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取经营计划管理统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/planTypeDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getPlanTypeDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = operationPlanService.getPlanTypeDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取计划类型分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/planStatusDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getPlanStatusDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = operationPlanService.getPlanStatusDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取计划状态分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/approvalStatusDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getApprovalStatusDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = operationPlanService.getApprovalStatusDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取审批状态分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/executionStatusDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getExecutionStatusDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = operationPlanService.getExecutionStatusDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取执行状态分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/upcomingPlans/{enterpriseId}")
    public R<List<OperationPlan>> getUpcomingPlans(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                   @Parameter(description="天数") @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<OperationPlan> plans = operationPlanService.getUpcomingPlans(enterpriseId, days);
            return R.success(plans);
        } catch (Exception e) {
            log.error("获取即将到期的计划失败", e);
            return R.fail("获取计划失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/overduePlans/{enterpriseId}")
    public R<List<OperationPlan>> getOverduePlans(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<OperationPlan> plans = operationPlanService.getOverduePlans(enterpriseId);
            return R.success(plans);
        } catch (Exception e) {
            log.error("获取逾期的计划失败", e);
            return R.fail("获取计划失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/pendingApprovalPlans/{enterpriseId}")
    public R<List<OperationPlan>> getPendingApprovalPlans(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<OperationPlan> plans = operationPlanService.getPendingApprovalPlans(enterpriseId);
            return R.success(plans);
        } catch (Exception e) {
            log.error("获取待审批的计划失败", e);
            return R.fail("获取计划失败");
        }
    }

    @Operation(summary = "")
    @PostMapping("/export")
    public R<Map<String, Object>> exportOperationPlan(@RequestBody OperationPlanQueryVo queryVo) {
        try {
            Map<String, Object> result = operationPlanService.exportOperationPlan(queryVo);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出经营计划管理失败", e);
            return R.fail("导出失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/generateReport/{enterpriseId}")
    public R<Map<String, Object>> generateOperationPlanReport(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                              @Parameter(description="报告类型") @RequestParam String reportType) {
        try {
            Map<String, Object> result = operationPlanService.generateOperationPlanReport(enterpriseId, reportType);
            return R.success(result);
        } catch (Exception e) {
            log.error("生成经营计划管理报告失败", e);
            return R.fail("生成报告失败");
        }
    }
}
