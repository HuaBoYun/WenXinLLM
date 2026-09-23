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
import com.huabo.cybermonitor.entity.FinancialStatementProcess;
import com.huabo.cybermonitor.service.IFinancialStatementProcessService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.FinancialStatementProcessQueryVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 财务报表编制流程控制器
 *
 * @author huabo
 * @since 2024-12-12
 */
@RestController
@RequestMapping("/v1/enterprise/financial/statement-process")
@Tag(name="财务报表编制流程管理",description="财务报表编制流程管理")
public class FinancialStatementProcessController {

	private static final Logger log = LoggerFactory.getLogger(FinancialStatementProcessController.class);

    @Autowired
    private IFinancialStatementProcessService financialStatementProcessService;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public R<IPage<FinancialStatementProcess>> getFinancialStatementProcessPage(@RequestBody FinancialStatementProcessQueryVo queryVo) {
        try {
            IPage<FinancialStatementProcess> page = financialStatementProcessService.getFinancialStatementProcessPage(queryVo);
            return R.success(page);
        } catch (Exception e) {
            log.error("分页查询财务报表编制流程失败", e);
            return R.fail("查询失败");
        }
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{processId}")
    public R<FinancialStatementProcess> getFinancialStatementProcessById(@Parameter(description="流程ID") @PathVariable String processId) {
        try {
            FinancialStatementProcess process = financialStatementProcessService.getById(processId);
            return R.success(process);
        } catch (Exception e) {
            log.error("获取财务报表编制流程详情失败", e);
            return R.fail("获取详情失败");
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Boolean> addFinancialStatementProcess(@RequestBody FinancialStatementProcess financialStatementProcess) {
        try {
            boolean result = financialStatementProcessService.addFinancialStatementProcess(financialStatementProcess);
            return result ? R.success(true, "新增成功") : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增财务报表编制流程失败", e);
            return R.fail("新增失败");
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/update")
    public R<Boolean> updateFinancialStatementProcess(@RequestBody FinancialStatementProcess financialStatementProcess) {
        try {
            boolean result = financialStatementProcessService.updateFinancialStatementProcess(financialStatementProcess);
            return result ? R.success(true, "修改成功") : R.fail("修改失败");
        } catch (Exception e) {
            log.error("修改财务报表编制流程失败", e);
            return R.fail("修改失败");
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{processId}")
    public R<Boolean> deleteFinancialStatementProcess(@Parameter(description="流程ID") @PathVariable String processId,
                                                       @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.deleteFinancialStatementProcess(processId, updateBy);
            return result ? R.success(true, "删除成功") : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除财务报表编制流程失败", e);
            return R.fail("删除失败");
        }
    }

    @Operation(summary = "批量操作")
    @DeleteMapping("/batchDelete")
    public R<Boolean> batchDeleteFinancialStatementProcess(@RequestBody List<String> processIds,
                                                            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.batchDeleteFinancialStatementProcess(processIds, updateBy);
            return result ? R.success(true, "批量删除成功") : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除财务报表编制流程失败", e);
            return R.fail("批量删除失败");
        }
    }

    @Operation(summary = "启用")
    @PutMapping("/start/{processId}")
    public R<Boolean> startCompilationProcess(@Parameter(description="流程ID") @PathVariable String processId,
                                              @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.startCompilationProcess(processId, updateBy);
            return result ? R.success(true, "启动成功") : R.fail("启动失败");
        } catch (Exception e) {
            log.error("启动编制流程失败", e);
            return R.fail("启动失败");
        }
    }

    @Operation(summary = "pauseCompilationProcess")
    @PutMapping("/pause/{processId}")
    public R<Boolean> pauseCompilationProcess(@Parameter(description="流程ID") @PathVariable String processId,
                                              @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.pauseCompilationProcess(processId, updateBy);
            return result ? R.success(true, "暂停成功") : R.fail("暂停失败");
        } catch (Exception e) {
            log.error("暂停编制流程失败", e);
            return R.fail("暂停失败");
        }
    }

    @Operation(summary = "resumeCompilationProcess")
    @PutMapping("/resume/{processId}")
    public R<Boolean> resumeCompilationProcess(@Parameter(description="流程ID") @PathVariable String processId,
                                               @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.resumeCompilationProcess(processId, updateBy);
            return result ? R.success(true, "恢复成功") : R.fail("恢复失败");
        } catch (Exception e) {
            log.error("恢复编制流程失败", e);
            return R.fail("恢复失败");
        }
    }

    @Operation(summary = "completeCompilationProcess")
    @PutMapping("/complete/{processId}")
    public R<Boolean> completeCompilationProcess(@Parameter(description="流程ID") @PathVariable String processId,
                                                 @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.completeCompilationProcess(processId, updateBy);
            return result ? R.success(true, "完成成功") : R.fail("完成失败");
        } catch (Exception e) {
            log.error("完成编制流程失败", e);
            return R.fail("完成失败");
        }
    }

    @Operation(summary = "撤销")
    @PutMapping("/cancel/{processId}")
    public R<Boolean> cancelCompilationProcess(@Parameter(description="流程ID") @PathVariable String processId,
                                               @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.cancelCompilationProcess(processId, updateBy);
            return result ? R.success(true, "取消成功") : R.fail("取消失败");
        } catch (Exception e) {
            log.error("取消编制流程失败", e);
            return R.fail("取消失败");
        }
    }

    @Operation(summary = "提交")
    @PutMapping("/submitForAudit/{processId}")
    public R<Boolean> submitForAudit(@Parameter(description="流程ID") @PathVariable String processId,
                                     @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.submitForAudit(processId, updateBy);
            return result ? R.success(true, "提交审核成功") : R.fail("提交审核失败");
        } catch (Exception e) {
            log.error("提交审核失败", e);
            return R.fail("提交审核失败");
        }
    }

    @Operation(summary = "firstAudit")
    @PutMapping("/firstAudit/{processId}")
    public R<Boolean> firstAudit(@Parameter(description="流程ID") @PathVariable String processId,
                                 @Parameter(description="审核人") @RequestParam String auditor,
                                 @Parameter(description="审核意见") @RequestParam String opinion,
                                 @Parameter(description="审核结果") @RequestParam String auditResult,
                                 @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.firstAudit(processId, auditor, opinion, auditResult, updateBy);
            return result ? R.success(true, "初审成功") : R.fail("初审失败");
        } catch (Exception e) {
            log.error("初审失败", e);
            return R.fail("初审失败");
        }
    }

    @Operation(summary = "secondAudit")
    @PutMapping("/secondAudit/{processId}")
    public R<Boolean> secondAudit(@Parameter(description="流程ID") @PathVariable String processId,
                                  @Parameter(description="审核人") @RequestParam String auditor,
                                  @Parameter(description="审核意见") @RequestParam String opinion,
                                  @Parameter(description="审核结果") @RequestParam String auditResult,
                                  @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.secondAudit(processId, auditor, opinion, auditResult, updateBy);
            return result ? R.success(true, "复审成功") : R.fail("复审失败");
        } catch (Exception e) {
            log.error("复审失败", e);
            return R.fail("复审失败");
        }
    }

    @Operation(summary = "finalAudit")
    @PutMapping("/finalAudit/{processId}")
    public R<Boolean> finalAudit(@Parameter(description="流程ID") @PathVariable String processId,
                                 @Parameter(description="审核人") @RequestParam String auditor,
                                 @Parameter(description="审核意见") @RequestParam String opinion,
                                 @Parameter(description="审核结果") @RequestParam String auditResult,
                                 @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.finalAudit(processId, auditor, opinion, auditResult, updateBy);
            return result ? R.success(true, "终审成功") : R.fail("终审失败");
        } catch (Exception e) {
            log.error("终审失败", e);
            return R.fail("终审失败");
        }
    }

    @Operation(summary = "signatureConfirmation")
    @PutMapping("/signatureConfirmation/{processId}")
    public R<Boolean> signatureConfirmation(@Parameter(description="流程ID") @PathVariable String processId,
                                            @Parameter(description="签字人") @RequestParam String signatory,
                                            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.signatureConfirmation(processId, signatory, updateBy);
            return result ? R.success(true, "签字确认成功") : R.fail("签字确认失败");
        } catch (Exception e) {
            log.error("签字确认失败", e);
            return R.fail("签字确认失败");
        }
    }

    @Operation(summary = "qualityControlCheck")
    @PutMapping("/qualityControlCheck/{processId}")
    public R<Boolean> qualityControlCheck(@Parameter(description="流程ID") @PathVariable String processId,
                                          @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.qualityControlCheck(processId, updateBy);
            return result ? R.success(true, "质量控制检查成功") : R.fail("质量控制检查失败");
        } catch (Exception e) {
            log.error("质量控制检查失败", e);
            return R.fail("质量控制检查失败");
        }
    }

    @Operation(summary = "issueCorrection")
    @PutMapping("/issueCorrection/{processId}")
    public R<Boolean> issueCorrection(@Parameter(description="流程ID") @PathVariable String processId,
                                      @Parameter(description="整改措施") @RequestParam String correctiveMeasures,
                                      @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.issueCorrection(processId, correctiveMeasures, updateBy);
            return result ? R.success(true, "问题整改成功") : R.fail("问题整改失败");
        } catch (Exception e) {
            log.error("问题整改失败", e);
            return R.fail("问题整改失败");
        }
    }

    @Operation(summary = "effectivenessVerification")
    @PutMapping("/effectivenessVerification/{processId}")
    public R<Boolean> effectivenessVerification(@Parameter(description="流程ID") @PathVariable String processId,
                                                @Parameter(description="效果验证") @RequestParam String effectivenessVerification,
                                                @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.effectivenessVerification(processId, effectivenessVerification, updateBy);
            return result ? R.success(true, "效果验证成功") : R.fail("效果验证失败");
        } catch (Exception e) {
            log.error("效果验证失败", e);
            return R.fail("效果验证失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateCompilationStatus")
    public R<Boolean> batchUpdateCompilationStatus(@RequestBody List<String> processIds,
                                                    @Parameter(description="状态") @RequestParam String status,
                                                    @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.batchUpdateCompilationStatus(processIds, status, updateBy);
            return result ? R.success(true, "批量更新编制状态成功") : R.fail("批量更新编制状态失败");
        } catch (Exception e) {
            log.error("批量更新编制状态失败", e);
            return R.fail("批量更新编制状态失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateAuditStatus")
    public R<Boolean> batchUpdateAuditStatus(@RequestBody List<String> processIds,
                                             @Parameter(description="状态") @RequestParam String status,
                                             @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.batchUpdateAuditStatus(processIds, status, updateBy);
            return result ? R.success(true, "批量更新审核状态成功") : R.fail("批量更新审核状态失败");
        } catch (Exception e) {
            log.error("批量更新审核状态失败", e);
            return R.fail("批量更新审核状态失败");
        }
    }

    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateQualityControlStatus")
    public R<Boolean> batchUpdateQualityControlStatus(@RequestBody List<String> processIds,
                                                       @Parameter(description="状态") @RequestParam String status,
                                                       @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = financialStatementProcessService.batchUpdateQualityControlStatus(processIds, status, updateBy);
            return result ? R.success(true, "批量更新质量控制状态成功") : R.fail("批量更新质量控制状态失败");
        } catch (Exception e) {
            log.error("批量更新质量控制状态失败", e);
            return R.fail("批量更新质量控制状态失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/statistics/{enterpriseId}")
    public R<Map<String, Object>> getStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> statistics = financialStatementProcessService.getStatisticsByEnterpriseId(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取财务报表编制流程统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/statementTypeDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getStatementTypeDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = financialStatementProcessService.getStatementTypeDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取报表类型分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/compilationStatusDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getCompilationStatusDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = financialStatementProcessService.getCompilationStatusDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取编制状态分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/auditStatusDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getAuditStatusDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = financialStatementProcessService.getAuditStatusDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取审核状态分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/qualityScoreDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getQualityScoreDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> distribution = financialStatementProcessService.getQualityScoreDistribution(enterpriseId);
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取质量评分分布失败", e);
            return R.fail("获取分布失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/compilationProgressTrend/{enterpriseId}")
    public R<List<Map<String, Object>>> getCompilationProgressTrend(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                                     @Parameter(description="月份数") @RequestParam(defaultValue = "12") Integer months) {
        try {
            List<Map<String, Object>> trend = financialStatementProcessService.getCompilationProgressTrend(enterpriseId, months);
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取编制进度趋势失败", e);
            return R.fail("获取趋势失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/qualityScoreTrend/{enterpriseId}")
    public R<List<Map<String, Object>>> getQualityScoreTrend(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                              @Parameter(description="月份数") @RequestParam(defaultValue = "12") Integer months) {
        try {
            List<Map<String, Object>> trend = financialStatementProcessService.getQualityScoreTrend(enterpriseId, months);
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取质量评分趋势失败", e);
            return R.fail("获取趋势失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/issueStatistics/{enterpriseId}")
    public R<Map<String, Object>> getIssueStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> statistics = financialStatementProcessService.getIssueStatistics(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取问题统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/issueTrend/{enterpriseId}")
    public R<List<Map<String, Object>>> getIssueTrend(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                       @Parameter(description="月份数") @RequestParam(defaultValue = "12") Integer months) {
        try {
            List<Map<String, Object>> trend = financialStatementProcessService.getIssueTrend(enterpriseId, months);
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取问题趋势失败", e);
            return R.fail("获取趋势失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/departmentCompilationStatistics/{enterpriseId}")
    public R<List<Map<String, Object>>> getDepartmentCompilationStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> statistics = financialStatementProcessService.getDepartmentCompilationStatistics(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取部门编制统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/managerCompilationStatistics/{enterpriseId}")
    public R<List<Map<String, Object>>> getManagerCompilationStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> statistics = financialStatementProcessService.getManagerCompilationStatistics(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取负责人编制统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/auditorStatistics/{enterpriseId}")
    public R<List<Map<String, Object>>> getAuditorStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> statistics = financialStatementProcessService.getAuditorStatistics(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取审核人员统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/templateVersionStatistics/{enterpriseId}")
    public R<List<Map<String, Object>>> getTemplateVersionStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> statistics = financialStatementProcessService.getTemplateVersionStatistics(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取模板版本使用统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/dataSourceStatistics/{enterpriseId}")
    public R<List<Map<String, Object>>> getDataSourceStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> statistics = financialStatementProcessService.getDataSourceStatistics(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取数据来源统计失败", e);
            return R.fail("获取统计失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/upcomingCompilationTasks/{enterpriseId}")
    public R<List<FinancialStatementProcess>> getUpcomingCompilationTasks(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                                           @Parameter(description="天数") @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<FinancialStatementProcess> tasks = financialStatementProcessService.getUpcomingCompilationTasks(enterpriseId, days);
            return R.success(tasks);
        } catch (Exception e) {
            log.error("获取即将到期的编制任务失败", e);
            return R.fail("获取任务失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/overdueCompilationTasks/{enterpriseId}")
    public R<List<FinancialStatementProcess>> getOverdueCompilationTasks(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<FinancialStatementProcess> tasks = financialStatementProcessService.getOverdueCompilationTasks(enterpriseId);
            return R.success(tasks);
        } catch (Exception e) {
            log.error("获取逾期的编制任务失败", e);
            return R.fail("获取任务失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/pendingAuditTasks/{enterpriseId}")
    public R<List<FinancialStatementProcess>> getPendingAuditTasks(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<FinancialStatementProcess> tasks = financialStatementProcessService.getPendingAuditTasks(enterpriseId);
            return R.success(tasks);
        } catch (Exception e) {
            log.error("获取待审核的编制任务失败", e);
            return R.fail("获取任务失败");
        }
    }

    @Operation(summary = "查询数据")
    @GetMapping("/pendingCorrectionTasks/{enterpriseId}")
    public R<List<FinancialStatementProcess>> getPendingCorrectionTasks(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<FinancialStatementProcess> tasks = financialStatementProcessService.getPendingCorrectionTasks(enterpriseId);
            return R.success(tasks);
        } catch (Exception e) {
            log.error("获取待整改的编制任务失败", e);
            return R.fail("获取任务失败");
        }
    }

    @Operation(summary = "")
    @PostMapping("/export")
    public R<Map<String, Object>> exportFinancialStatementProcess(@RequestBody FinancialStatementProcessQueryVo queryVo) {
        try {
            Map<String, Object> result = financialStatementProcessService.exportFinancialStatementProcess(queryVo);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出财务报表编制流程失败", e);
            return R.fail("导出失败");
        }
    }

    @Operation(summary = "")
    @GetMapping("/generateReport/{enterpriseId}")
    public R<Map<String, Object>> generateFinancialStatementProcessReport(@Parameter(description="企业ID") @PathVariable String enterpriseId,
                                                                           @Parameter(description="报告类型") @RequestParam String reportType) {
        try {
            Map<String, Object> result = financialStatementProcessService.generateFinancialStatementProcessReport(enterpriseId, reportType);
            return R.success(result);
        } catch (Exception e) {
            log.error("生成财务报表编制流程报告失败", e);
            return R.fail("生成报告失败");
        }
    }
}
