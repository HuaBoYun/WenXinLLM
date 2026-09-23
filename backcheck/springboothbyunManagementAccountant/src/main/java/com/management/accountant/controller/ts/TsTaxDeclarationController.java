package com.management.accountant.controller.ts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ts.TsTaxDeclaration;
import com.management.accountant.service.ts.TsTaxDeclarationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 税务申报控制器
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ts/tax-declaration")
@Api(tags = "税务申报管理")
public class TsTaxDeclarationController {

    @Autowired
    private TsTaxDeclarationService taxDeclarationService;

    // ==================== 基础CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建税务申报")
    public MyJsonBean createDeclaration(@RequestBody TsTaxDeclaration declaration) {
        try {
            TsTaxDeclaration result = taxDeclarationService.createDeclaration(declaration);
            return MyJsonBean.success("创建税务申报成功", result);
        } catch (Exception e) {
            log.error("创建税务申报失败", e);
            return MyJsonBean.error("创建税务申报失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新税务申报")
    public MyJsonBean updateDeclaration(@RequestBody TsTaxDeclaration declaration) {
        try {
            TsTaxDeclaration result = taxDeclarationService.updateDeclaration(declaration);
            return MyJsonBean.success("更新税务申报成功", result);
        } catch (Exception e) {
            log.error("更新税务申报失败", e);
            return MyJsonBean.error("更新税务申报失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{tenantId}/{declarationId}")
    @ApiOperation("删除税务申报")
    public MyJsonBean deleteDeclaration(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            boolean result = taxDeclarationService.deleteDeclaration(tenantId, declarationId);
            return result ? MyJsonBean.success("删除税务申报成功") : MyJsonBean.error("删除税务申报失败");
        } catch (Exception e) {
            log.error("删除税务申报失败", e);
            return MyJsonBean.error("删除税务申报失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{tenantId}/{declarationId}")
    @ApiOperation("获取税务申报详情")
    public MyJsonBean getDeclarationDetail(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            TsTaxDeclaration result = taxDeclarationService.getDeclarationById(tenantId, declarationId);
            return MyJsonBean.success("获取税务申报详情成功", result);
        } catch (Exception e) {
            log.error("获取税务申报详情失败", e);
            return MyJsonBean.error("获取税务申报详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @ApiOperation("分页查询税务申报列表")
    public MyJsonBean getDeclarationPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "20") Long size,
            @RequestParam Long tenantId,
            @RequestParam(required = false) String declarationCode,
            @RequestParam(required = false) String declarationName,
            @RequestParam(required = false) String taxType,
            @RequestParam(required = false) String declarationType,
            @RequestParam(required = false) String declarationStatus,
            @RequestParam(required = false) String taxpayerName,
            @RequestParam(required = false) String declarationPeriod,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(required = false) String businessCategory) {
        try {
            Page<TsTaxDeclaration> page = new Page<>(current, size);
            IPage<TsTaxDeclaration> result = taxDeclarationService.getDeclarationPage(page, tenantId, declarationCode, 
                    declarationName, taxType, declarationType, declarationStatus, taxpayerName, declarationPeriod, 
                    startDate, endDate, businessCategory);
            return MyJsonBean.success("查询税务申报列表成功", result);
        } catch (Exception e) {
            log.error("查询税务申报列表失败", e);
            return MyJsonBean.error("查询税务申报列表失败: " + e.getMessage());
        }
    }

    // ==================== 申报管理功能 ====================

    @PostMapping("/generate-plan")
    @ApiOperation("生成申报计划")
    public MyJsonBean generateDeclarationPlan(
            @RequestParam Long tenantId,
            @RequestParam String taxType,
            @RequestParam String period,
            @RequestParam Integer year) {
        try {
            List<TsTaxDeclaration> result = taxDeclarationService.generateDeclarationPlan(tenantId, taxType, period, year);
            return MyJsonBean.success("生成申报计划成功", result);
        } catch (Exception e) {
            log.error("生成申报计划失败", e);
            return MyJsonBean.error("生成申报计划失败: " + e.getMessage());
        }
    }

    @PostMapping("/auto-fill/{tenantId}/{declarationId}")
    @ApiOperation("自动填报申报表")
    public MyJsonBean autoFillDeclaration(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            boolean result = taxDeclarationService.autoFillDeclaration(tenantId, declarationId);
            return result ? MyJsonBean.success("自动填报成功") : MyJsonBean.error("自动填报失败");
        } catch (Exception e) {
            log.error("自动填报失败", e);
            return MyJsonBean.error("自动填报失败: " + e.getMessage());
        }
    }

    @PostMapping("/submit/{tenantId}/{declarationId}")
    @ApiOperation("提交申报")
    public MyJsonBean submitDeclaration(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            boolean result = taxDeclarationService.submitDeclaration(tenantId, declarationId);
            return result ? MyJsonBean.success("提交申报成功") : MyJsonBean.error("提交申报失败");
        } catch (Exception e) {
            log.error("提交申报失败", e);
            return MyJsonBean.error("提交申报失败: " + e.getMessage());
        }
    }

    @PostMapping("/withdraw/{tenantId}/{declarationId}")
    @ApiOperation("撤回申报")
    public MyJsonBean withdrawDeclaration(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            boolean result = taxDeclarationService.withdrawDeclaration(tenantId, declarationId);
            return result ? MyJsonBean.success("撤回申报成功") : MyJsonBean.error("撤回申报失败");
        } catch (Exception e) {
            log.error("撤回申报失败", e);
            return MyJsonBean.error("撤回申报失败: " + e.getMessage());
        }
    }

    @PostMapping("/review/{tenantId}/{declarationId}")
    @ApiOperation("审核申报")
    public MyJsonBean reviewDeclaration(
            @PathVariable Long tenantId, 
            @PathVariable Long declarationId,
            @RequestParam String reviewResult,
            @RequestParam(required = false) String reviewComment) {
        try {
            boolean result = taxDeclarationService.reviewDeclaration(tenantId, declarationId, reviewResult, reviewComment);
            return result ? MyJsonBean.success("审核申报成功") : MyJsonBean.error("审核申报失败");
        } catch (Exception e) {
            log.error("审核申报失败", e);
            return MyJsonBean.error("审核申报失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-submit")
    @ApiOperation("批量提交申报")
    public MyJsonBean batchSubmitDeclarations(
            @RequestParam Long tenantId,
            @RequestBody List<Long> declarationIds) {
        try {
            Map<String, Object> result = taxDeclarationService.batchSubmitDeclarations(tenantId, declarationIds);
            return MyJsonBean.success("批量提交申报完成", result);
        } catch (Exception e) {
            log.error("批量提交申报失败", e);
            return MyJsonBean.error("批量提交申报失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-review")
    @ApiOperation("批量审核申报")
    public MyJsonBean batchReviewDeclarations(
            @RequestParam Long tenantId,
            @RequestBody List<Long> declarationIds,
            @RequestParam String reviewResult,
            @RequestParam(required = false) String reviewComment) {
        try {
            Map<String, Object> result = taxDeclarationService.batchReviewDeclarations(tenantId, declarationIds, reviewResult, reviewComment);
            return MyJsonBean.success("批量审核申报完成", result);
        } catch (Exception e) {
            log.error("批量审核申报失败", e);
            return MyJsonBean.error("批量审核申报失败: " + e.getMessage());
        }
    }

    // ==================== 申报计算功能 ====================

    @PostMapping("/calculate-tax/{tenantId}/{declarationId}")
    @ApiOperation("计算税额")
    public MyJsonBean calculateTaxAmount(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            Map<String, Object> result = taxDeclarationService.calculateTaxAmount(tenantId, declarationId);
            return MyJsonBean.success("计算税额完成", result);
        } catch (Exception e) {
            log.error("计算税额失败", e);
            return MyJsonBean.error("计算税额失败: " + e.getMessage());
        }
    }

    @PostMapping("/recalculate-tax/{tenantId}/{declarationId}")
    @ApiOperation("重新计算税额")
    public MyJsonBean recalculateTaxAmount(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            boolean result = taxDeclarationService.recalculateTaxAmount(tenantId, declarationId);
            return result ? MyJsonBean.success("重新计算税额成功") : MyJsonBean.error("重新计算税额失败");
        } catch (Exception e) {
            log.error("重新计算税额失败", e);
            return MyJsonBean.error("重新计算税额失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate-data/{tenantId}/{declarationId}")
    @ApiOperation("验证申报数据")
    public MyJsonBean validateDeclarationData(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            Map<String, Object> result = taxDeclarationService.validateDeclarationData(tenantId, declarationId);
            return MyJsonBean.success("验证申报数据完成", result);
        } catch (Exception e) {
            log.error("验证申报数据失败", e);
            return MyJsonBean.error("验证申报数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/tax-base/{tenantId}/{declarationId}")
    @ApiOperation("获取计税依据")
    public MyJsonBean getTaxBase(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            Map<String, Object> result = taxDeclarationService.getTaxBase(tenantId, declarationId);
            return MyJsonBean.success("获取计税依据成功", result);
        } catch (Exception e) {
            log.error("获取计税依据失败", e);
            return MyJsonBean.error("获取计税依据失败: " + e.getMessage());
        }
    }

    // ==================== 申报跟踪功能 ====================

    @GetMapping("/progress/{tenantId}/{declarationId}")
    @ApiOperation("获取申报进度")
    public MyJsonBean getDeclarationProgress(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            Map<String, Object> result = taxDeclarationService.getDeclarationProgress(tenantId, declarationId);
            return MyJsonBean.success("获取申报进度成功", result);
        } catch (Exception e) {
            log.error("获取申报进度失败", e);
            return MyJsonBean.error("获取申报进度失败: " + e.getMessage());
        }
    }

    @PostMapping("/update-status/{tenantId}/{declarationId}")
    @ApiOperation("更新申报状态")
    public MyJsonBean updateDeclarationStatus(
            @PathVariable Long tenantId, 
            @PathVariable Long declarationId,
            @RequestParam String status,
            @RequestParam(required = false) String remark) {
        try {
            boolean result = taxDeclarationService.updateDeclarationStatus(tenantId, declarationId, status, remark);
            return result ? MyJsonBean.success("更新申报状态成功") : MyJsonBean.error("更新申报状态失败");
        } catch (Exception e) {
            log.error("更新申报状态失败", e);
            return MyJsonBean.error("更新申报状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/history/{tenantId}/{declarationId}")
    @ApiOperation("获取申报历史")
    public MyJsonBean getDeclarationHistory(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.getDeclarationHistory(tenantId, declarationId);
            return MyJsonBean.success("获取申报历史成功", result);
        } catch (Exception e) {
            log.error("获取申报历史失败", e);
            return MyJsonBean.error("获取申报历史失败: " + e.getMessage());
        }
    }

    @GetMapping("/logs/{tenantId}/{declarationId}")
    @ApiOperation("获取申报日志")
    public MyJsonBean getDeclarationLogs(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.getDeclarationLogs(tenantId, declarationId);
            return MyJsonBean.success("获取申报日志成功", result);
        } catch (Exception e) {
            log.error("获取申报日志失败", e);
            return MyJsonBean.error("获取申报日志失败: " + e.getMessage());
        }
    }

    // ==================== 申报提醒功能 ====================

    @GetMapping("/pending/{tenantId}")
    @ApiOperation("获取待申报列表")
    public MyJsonBean getPendingDeclarations(@PathVariable Long tenantId, @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<TsTaxDeclaration> result = taxDeclarationService.getPendingDeclarations(tenantId, limit);
            return MyJsonBean.success("获取待申报列表成功", result);
        } catch (Exception e) {
            log.error("获取待申报列表失败", e);
            return MyJsonBean.error("获取待申报列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending-review/{tenantId}")
    @ApiOperation("获取待审核申报列表")
    public MyJsonBean getPendingReviewDeclarations(@PathVariable Long tenantId, @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<TsTaxDeclaration> result = taxDeclarationService.getPendingReviewDeclarations(tenantId, limit);
            return MyJsonBean.success("获取待审核申报列表成功", result);
        } catch (Exception e) {
            log.error("获取待审核申报列表失败", e);
            return MyJsonBean.error("获取待审核申报列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/overdue/{tenantId}")
    @ApiOperation("获取逾期申报列表")
    public MyJsonBean getOverdueDeclarations(@PathVariable Long tenantId) {
        try {
            List<TsTaxDeclaration> result = taxDeclarationService.getOverdueDeclarations(tenantId);
            return MyJsonBean.success("获取逾期申报列表成功", result);
        } catch (Exception e) {
            log.error("获取逾期申报列表失败", e);
            return MyJsonBean.error("获取逾期申报列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/upcoming/{tenantId}")
    @ApiOperation("获取即将到期申报列表")
    public MyJsonBean getUpcomingDeclarations(@PathVariable Long tenantId, @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<TsTaxDeclaration> result = taxDeclarationService.getUpcomingDeclarations(tenantId, days);
            return MyJsonBean.success("获取即将到期申报列表成功", result);
        } catch (Exception e) {
            log.error("获取即将到期申报列表失败", e);
            return MyJsonBean.error("获取即将到期申报列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/send-reminder/{tenantId}/{declarationId}")
    @ApiOperation("发送申报提醒")
    public MyJsonBean sendDeclarationReminder(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            boolean result = taxDeclarationService.sendDeclarationReminder(tenantId, declarationId);
            return result ? MyJsonBean.success("发送申报提醒成功") : MyJsonBean.error("发送申报提醒失败");
        } catch (Exception e) {
            log.error("发送申报提醒失败", e);
            return MyJsonBean.error("发送申报提醒失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-send-reminders")
    @ApiOperation("批量发送申报提醒")
    public MyJsonBean batchSendDeclarationReminders(
            @RequestParam Long tenantId,
            @RequestBody List<Long> declarationIds) {
        try {
            Map<String, Object> result = taxDeclarationService.batchSendDeclarationReminders(tenantId, declarationIds);
            return MyJsonBean.success("批量发送申报提醒完成", result);
        } catch (Exception e) {
            log.error("批量发送申报提醒失败", e);
            return MyJsonBean.error("批量发送申报提醒失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析功能 ====================

    @GetMapping("/overview/{tenantId}")
    @ApiOperation("获取申报概览")
    public MyJsonBean getDeclarationOverview(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.getDeclarationOverview(tenantId, startDate, endDate);
            return MyJsonBean.success("获取申报概览成功", result);
        } catch (Exception e) {
            log.error("获取申报概览失败", e);
            return MyJsonBean.error("获取申报概览失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-status/{tenantId}")
    @ApiOperation("统计申报数量按状态分组")
    public MyJsonBean countDeclarationsByStatus(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.countDeclarationsByStatus(tenantId, startDate, endDate);
            return MyJsonBean.success("统计申报数量按状态分组成功", result);
        } catch (Exception e) {
            log.error("统计申报数量按状态分组失败", e);
            return MyJsonBean.error("统计申报数量按状态分组失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-tax-type/{tenantId}")
    @ApiOperation("统计申报数量按税种分组")
    public MyJsonBean countDeclarationsByTaxType(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.countDeclarationsByTaxType(tenantId, startDate, endDate);
            return MyJsonBean.success("统计申报数量按税种分组成功", result);
        } catch (Exception e) {
            log.error("统计申报数量按税种分组失败", e);
            return MyJsonBean.error("统计申报数量按税种分组失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-type/{tenantId}")
    @ApiOperation("统计申报数量按类型分组")
    public MyJsonBean countDeclarationsByType(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.countDeclarationsByType(tenantId, startDate, endDate);
            return MyJsonBean.success("统计申报数量按类型分组成功", result);
        } catch (Exception e) {
            log.error("统计申报数量按类型分组失败", e);
            return MyJsonBean.error("统计申报数量按类型分组失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/tax-amount-by-month/{tenantId}")
    @ApiOperation("统计申报金额按月份分组")
    public MyJsonBean sumTaxAmountByMonth(@PathVariable Long tenantId, @RequestParam Integer year) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.sumTaxAmountByMonth(tenantId, year);
            return MyJsonBean.success("统计申报金额按月份分组成功", result);
        } catch (Exception e) {
            log.error("统计申报金额按月份分组失败", e);
            return MyJsonBean.error("统计申报金额按月份分组失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/tax-amount-by-tax-type/{tenantId}")
    @ApiOperation("统计申报金额按税种分组")
    public MyJsonBean sumTaxAmountByTaxType(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.sumTaxAmountByTaxType(tenantId, startDate, endDate);
            return MyJsonBean.success("统计申报金额按税种分组成功", result);
        } catch (Exception e) {
            log.error("统计申报金额按税种分组失败", e);
            return MyJsonBean.error("统计申报金额按税种分组失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/timely-rate/{tenantId}")
    @ApiOperation("计算申报及时率")
    public MyJsonBean calculateTimelyRate(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.calculateTimelyRate(tenantId, startDate, endDate);
            return MyJsonBean.success("计算申报及时率成功", result);
        } catch (Exception e) {
            log.error("计算申报及时率失败", e);
            return MyJsonBean.error("计算申报及时率失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/success-rate/{tenantId}")
    @ApiOperation("计算申报成功率")
    public MyJsonBean calculateSuccessRate(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.calculateSuccessRate(tenantId, startDate, endDate);
            return MyJsonBean.success("计算申报成功率成功", result);
        } catch (Exception e) {
            log.error("计算申报成功率失败", e);
            return MyJsonBean.error("计算申报成功率失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/processing-time/{tenantId}")
    @ApiOperation("统计申报处理时长")
    public MyJsonBean calculateProcessingTimeStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.calculateProcessingTimeStats(tenantId, startDate, endDate);
            return MyJsonBean.success("统计申报处理时长成功", result);
        } catch (Exception e) {
            log.error("统计申报处理时长失败", e);
            return MyJsonBean.error("统计申报处理时长失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/declaration-trend/{tenantId}")
    @ApiOperation("获取申报趋势数据")
    public MyJsonBean getDeclarationTrend(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(defaultValue = "month") String groupBy) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.getDeclarationTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("获取申报趋势数据成功", result);
        } catch (Exception e) {
            log.error("获取申报趋势数据失败", e);
            return MyJsonBean.error("获取申报趋势数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/tax-amount-trend/{tenantId}")
    @ApiOperation("获取税额趋势数据")
    public MyJsonBean getTaxAmountTrend(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(defaultValue = "month") String groupBy) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.getTaxAmountTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("获取税额趋势数据成功", result);
        } catch (Exception e) {
            log.error("获取税额趋势数据失败", e);
            return MyJsonBean.error("获取税额趋势数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/efficiency/{tenantId}")
    @ApiOperation("获取申报效率统计")
    public MyJsonBean getDeclarationEfficiencyStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.getDeclarationEfficiencyStats(tenantId, startDate, endDate);
            return MyJsonBean.success("获取申报效率统计成功", result);
        } catch (Exception e) {
            log.error("获取申报效率统计失败", e);
            return MyJsonBean.error("获取申报效率统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/quality/{tenantId}")
    @ApiOperation("获取申报质量统计")
    public MyJsonBean getDeclarationQualityStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.getDeclarationQualityStats(tenantId, startDate, endDate);
            return MyJsonBean.success("获取申报质量统计成功", result);
        } catch (Exception e) {
            log.error("获取申报质量统计失败", e);
            return MyJsonBean.error("获取申报质量统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/compliance/{tenantId}")
    @ApiOperation("获取合规性统计")
    public MyJsonBean getComplianceStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.getComplianceStats(tenantId, startDate, endDate);
            return MyJsonBean.success("获取合规性统计成功", result);
        } catch (Exception e) {
            log.error("获取合规性统计失败", e);
            return MyJsonBean.error("获取合规性统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/risk-analysis/{tenantId}")
    @ApiOperation("获取风险分析")
    public MyJsonBean getRiskAnalysis(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.getRiskAnalysis(tenantId, startDate, endDate);
            return MyJsonBean.success("获取风险分析成功", result);
        } catch (Exception e) {
            log.error("获取风险分析失败", e);
            return MyJsonBean.error("获取风险分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/cost-analysis/{tenantId}")
    @ApiOperation("获取成本分析")
    public MyJsonBean getCostAnalysis(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = taxDeclarationService.getCostAnalysis(tenantId, startDate, endDate);
            return MyJsonBean.success("获取成本分析成功", result);
        } catch (Exception e) {
            log.error("获取成本分析失败", e);
            return MyJsonBean.error("获取成本分析失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作功能 ====================

    @PostMapping("/batch-update-status")
    @ApiOperation("批量更新申报状态")
    public MyJsonBean batchUpdateStatus(
            @RequestParam Long tenantId,
            @RequestBody List<Long> declarationIds,
            @RequestParam String status) {
        try {
            Map<String, Object> result = taxDeclarationService.batchUpdateStatus(tenantId, declarationIds, status);
            return MyJsonBean.success("批量更新申报状态完成", result);
        } catch (Exception e) {
            log.error("批量更新申报状态失败", e);
            return MyJsonBean.error("批量更新申报状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-delete")
    @ApiOperation("批量删除申报")
    public MyJsonBean batchDeleteDeclarations(
            @RequestParam Long tenantId,
            @RequestBody List<Long> declarationIds) {
        try {
            Map<String, Object> result = taxDeclarationService.batchDeleteDeclarations(tenantId, declarationIds);
            return MyJsonBean.success("批量删除申报完成", result);
        } catch (Exception e) {
            log.error("批量删除申报失败", e);
            return MyJsonBean.error("批量删除申报失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-import")
    @ApiOperation("批量导入申报")
    public MyJsonBean batchImportDeclarations(
            @RequestParam Long tenantId,
            @RequestBody List<Map<String, Object>> declarationData) {
        try {
            Map<String, Object> result = taxDeclarationService.batchImportDeclarations(tenantId, declarationData);
            return MyJsonBean.success("批量导入申报完成", result);
        } catch (Exception e) {
            log.error("批量导入申报失败", e);
            return MyJsonBean.error("批量导入申报失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-export")
    @ApiOperation("批量导出申报")
    public MyJsonBean batchExportDeclarations(
            @RequestParam Long tenantId,
            @RequestBody List<Long> declarationIds) {
        try {
            List<Map<String, Object>> result = taxDeclarationService.batchExportDeclarations(tenantId, declarationIds);
            return MyJsonBean.success("批量导出申报完成", result);
        } catch (Exception e) {
            log.error("批量导出申报失败", e);
            return MyJsonBean.error("批量导出申报失败: " + e.getMessage());
        }
    }

    // ==================== 查询功能 ====================

    @GetMapping("/by-code/{tenantId}/{declarationCode}")
    @ApiOperation("根据申报编号查询申报")
    public MyJsonBean getDeclarationByCode(@PathVariable Long tenantId, @PathVariable String declarationCode) {
        try {
            TsTaxDeclaration result = taxDeclarationService.getDeclarationByCode(tenantId, declarationCode);
            return MyJsonBean.success("根据申报编号查询申报成功", result);
        } catch (Exception e) {
            log.error("根据申报编号查询申报失败", e);
            return MyJsonBean.error("根据申报编号查询申报失败: " + e.getMessage());
        }
    }

    @GetMapping("/by-taxpayer/{tenantId}/{taxpayerId}")
    @ApiOperation("根据纳税人识别号查询申报列表")
    public MyJsonBean getDeclarationsByTaxpayerId(@PathVariable Long tenantId, @PathVariable String taxpayerId) {
        try {
            List<TsTaxDeclaration> result = taxDeclarationService.getDeclarationsByTaxpayerId(tenantId, taxpayerId);
            return MyJsonBean.success("根据纳税人识别号查询申报列表成功", result);
        } catch (Exception e) {
            log.error("根据纳税人识别号查询申报列表失败", e);
            return MyJsonBean.error("根据纳税人识别号查询申报列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/advanced-search")
    @ApiOperation("高级搜索申报")
    public MyJsonBean advancedSearchDeclarations(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "20") Long size,
            @RequestParam Long tenantId,
            @RequestBody Map<String, Object> searchParams) {
        try {
            Page<TsTaxDeclaration> page = new Page<>(current, size);
            IPage<TsTaxDeclaration> result = taxDeclarationService.advancedSearchDeclarations(page, tenantId, searchParams);
            return MyJsonBean.success("高级搜索申报成功", result);
        } catch (Exception e) {
            log.error("高级搜索申报失败", e);
            return MyJsonBean.error("高级搜索申报失败: " + e.getMessage());
        }
    }

    @GetMapping("/full-text-search")
    @ApiOperation("全文搜索申报")
    public MyJsonBean fullTextSearchDeclarations(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "20") Long size,
            @RequestParam Long tenantId,
            @RequestParam String keyword) {
        try {
            Page<TsTaxDeclaration> page = new Page<>(current, size);
            IPage<TsTaxDeclaration> result = taxDeclarationService.fullTextSearchDeclarations(page, tenantId, keyword);
            return MyJsonBean.success("全文搜索申报成功", result);
        } catch (Exception e) {
            log.error("全文搜索申报失败", e);
            return MyJsonBean.error("全文搜索申报失败: " + e.getMessage());
        }
    }

    // ==================== 系统维护功能 ====================

    @GetMapping("/abnormal/{tenantId}")
    @ApiOperation("获取异常申报列表")
    public MyJsonBean getAbnormalDeclarations(@PathVariable Long tenantId, @RequestParam List<String> errorTypes) {
        try {
            List<TsTaxDeclaration> result = taxDeclarationService.getAbnormalDeclarations(tenantId, errorTypes);
            return MyJsonBean.success("获取异常申报列表成功", result);
        } catch (Exception e) {
            log.error("获取异常申报列表失败", e);
            return MyJsonBean.error("获取异常申报列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/retry/{tenantId}")
    @ApiOperation("获取需要重试的申报列表")
    public MyJsonBean getRetryDeclarations(@PathVariable Long tenantId) {
        try {
            List<TsTaxDeclaration> result = taxDeclarationService.getRetryDeclarations(tenantId);
            return MyJsonBean.success("获取需要重试的申报列表成功", result);
        } catch (Exception e) {
            log.error("获取需要重试的申报列表失败", e);
            return MyJsonBean.error("获取需要重试的申报列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/retry/{tenantId}/{declarationId}")
    @ApiOperation("重试申报")
    public MyJsonBean retryDeclaration(@PathVariable Long tenantId, @PathVariable Long declarationId) {
        try {
            boolean result = taxDeclarationService.retryDeclaration(tenantId, declarationId);
            return result ? MyJsonBean.success("重试申报成功") : MyJsonBean.error("重试申报失败");
        } catch (Exception e) {
            log.error("重试申报失败", e);
            return MyJsonBean.error("重试申报失败: " + e.getMessage());
        }
    }

    @PostMapping("/clean-expired/{tenantId}")
    @ApiOperation("清理过期申报")
    public MyJsonBean cleanExpiredDeclarations(
            @PathVariable Long tenantId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expiredDate) {
        try {
            int result = taxDeclarationService.cleanExpiredDeclarations(tenantId, expiredDate);
            return MyJsonBean.success("清理过期申报完成，清理数量: " + result, result);
        } catch (Exception e) {
            log.error("清理过期申报失败", e);
            return MyJsonBean.error("清理过期申报失败: " + e.getMessage());
        }
    }

    @GetMapping("/health-check/{tenantId}")
    @ApiOperation("系统健康检查")
    public MyJsonBean healthCheck(@PathVariable Long tenantId) {
        try {
            Map<String, Object> result = taxDeclarationService.healthCheck(tenantId);
            return MyJsonBean.success("系统健康检查完成", result);
        } catch (Exception e) {
            log.error("系统健康检查失败", e);
            return MyJsonBean.error("系统健康检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/performance-metrics/{tenantId}")
    @ApiOperation("获取系统性能指标")
    public MyJsonBean getPerformanceMetrics(@PathVariable Long tenantId) {
        try {
            Map<String, Object> result = taxDeclarationService.getPerformanceMetrics(tenantId);
            return MyJsonBean.success("获取系统性能指标成功", result);
        } catch (Exception e) {
            log.error("获取系统性能指标失败", e);
            return MyJsonBean.error("获取系统性能指标失败: " + e.getMessage());
        }
    }
}
