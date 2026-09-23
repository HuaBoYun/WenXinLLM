package com.huabo.contract.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

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
import com.huabo.contract.entity.DepositManagement;
import com.huabo.contract.service.DepositManagementService;
import com.huabo.contract.vo.DepositManagementQueryParam;
import com.huabo.contract.vo.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 保证金管理控制器
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Tag(name="保证金管理",description="保证金管理")
@RestController
@RequestMapping("/bidding/deposit")
@RequiredArgsConstructor
public class DepositManagementController {

    private final DepositManagementService depositManagementService;

    @Operation(summary = "分页查询保证金管理列表")
    @PostMapping("/page")
    public Result<IPage<DepositManagement>> getDepositManagementPage(@RequestBody DepositManagementQueryParam queryParam) {
        IPage<DepositManagement> page = depositManagementService.getDepositManagementPage(queryParam);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询保证金详情")
    @GetMapping("/{id}")
    public Result<DepositManagement> getDepositManagementById(@Parameter(description="保证金ID") @PathVariable Long id) {
        DepositManagement depositManagement = depositManagementService.getById(id);
        return Result.success(depositManagement);
    }

    @Operation(summary = "新增保证金")
    @PostMapping
    public Result<Boolean> saveDepositManagement(@Valid @RequestBody DepositManagement depositManagement) {
        // 验证保证金信息
        if (!depositManagementService.validateDepositInfo(depositManagement)) {
            return Result.fail("保证金信息验证失败");
        }
        
        boolean result = depositManagementService.save(depositManagement);
        return Result.success(result);
    }

    @Operation(summary = "修改保证金")
    @PutMapping
    public Result<Boolean> updateDepositManagement(@Valid @RequestBody DepositManagement depositManagement) {
        // 验证保证金信息
        if (!depositManagementService.validateDepositInfo(depositManagement)) {
            return Result.fail("保证金信息验证失败");
        }
        
        boolean result = depositManagementService.updateById(depositManagement);
        return Result.success(result);
    }

    @Operation(summary = "删除保证金")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDepositManagement(@Parameter(description="保证金ID") @PathVariable Long id) {
        boolean result = depositManagementService.removeById(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除保证金")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteDepositManagement(@RequestBody List<Long> ids) {
        boolean result = depositManagementService.removeByIds(ids);
        return Result.success(result);
    }

    @Operation(summary = "根据招投标项目ID查询保证金列表")
    @GetMapping("/bidding-project/{biddingProjectId}")
    public Result<List<DepositManagement>> getByBiddingProjectId(@Parameter(description="招投标项目ID") @PathVariable Long biddingProjectId) {
        List<DepositManagement> list = depositManagementService.getByBiddingProjectId(biddingProjectId);
        return Result.success(list);
    }

    @Operation(summary = "根据保证金状态查询保证金列表")
    @GetMapping("/status/{depositStatus}")
    public Result<List<DepositManagement>> getByDepositStatus(@Parameter(description="保证金状态") @PathVariable Integer depositStatus) {
        List<DepositManagement> list = depositManagementService.getByDepositStatus(depositStatus);
        return Result.success(list);
    }

    @Operation(summary = "查询即将到期的保证金列表")
    @GetMapping("/expiring-soon")
    public Result<List<DepositManagement>> getExpiringSoon() {
        List<DepositManagement> list = depositManagementService.getExpiringSoon();
        return Result.success(list);
    }

    @Operation(summary = "查询已逾期的保证金列表")
    @GetMapping("/overdue")
    public Result<List<DepositManagement>> getOverdue() {
        List<DepositManagement> list = depositManagementService.getOverdue();
        return Result.success(list);
    }

    @Operation(summary = "根据保证金类型统计保证金总额")
    @GetMapping("/sum-amount/type/{depositType}")
    public Result<BigDecimal> sumDepositAmountByType(@Parameter(description="保证金类型") @PathVariable Integer depositType) {
        BigDecimal amount = depositManagementService.sumDepositAmountByType(depositType);
        return Result.success(amount);
    }

    @Operation(summary = "根据招投标项目ID统计保证金总额")
    @GetMapping("/sum-amount/project/{biddingProjectId}")
    public Result<BigDecimal> sumDepositAmountByProject(@Parameter(description="招投标项目ID") @PathVariable Long biddingProjectId) {
        BigDecimal amount = depositManagementService.sumDepositAmountByProject(biddingProjectId);
        return Result.success(amount);
    }

    @Operation(summary = "根据保证金状态统计数量")
    @GetMapping("/count/status/{depositStatus}")
    public Result<Integer> countByDepositStatus(@Parameter(description="保证金状态") @PathVariable Integer depositStatus) {
        Integer count = depositManagementService.countByDepositStatus(depositStatus);
        return Result.success(count);
    }

    @Operation(summary = "根据保函编号查询保证金")
    @GetMapping("/guarantee/{guaranteeNo}")
    public Result<DepositManagement> getByGuaranteeNo(@Parameter(description="保函编号") @PathVariable String guaranteeNo) {
        DepositManagement depositManagement = depositManagementService.getByGuaranteeNo(guaranteeNo);
        return Result.success(depositManagement);
    }

    @Operation(summary = "批量更新保证金状态")
    @PutMapping("/batch-status")
    public Result<Integer> batchUpdateStatus(@RequestBody List<Long> ids, 
                                           @Parameter(description="新状态") @RequestParam Integer depositStatus,
                                           @Parameter(description="更新人") @RequestParam Long updateBy) {
        Integer count = depositManagementService.batchUpdateStatus(ids, depositStatus, updateBy);
        return Result.success(count);
    }

    @Operation(summary = "保证金退还")
    @PutMapping("/refund/{id}")
    public Result<Boolean> refundDeposit(@Parameter(description="保证金ID") @PathVariable Long id,
                                       @Parameter(description="退还金额") @RequestParam BigDecimal refundAmount,
                                       @Parameter(description="退还原因") @RequestParam String refundReason,
                                       @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = depositManagementService.refundDeposit(id, refundAmount, refundReason, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "保证金没收")
    @PutMapping("/confiscate/{id}")
    public Result<Boolean> confiscateDeposit(@Parameter(description="保证金ID") @PathVariable Long id,
                                           @Parameter(description="没收原因") @RequestParam String confiscationReason,
                                           @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = depositManagementService.confiscateDeposit(id, confiscationReason, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "保证金转履约")
    @PutMapping("/convert-performance/{id}")
    public Result<Boolean> convertToPerformance(@Parameter(description="保证金ID") @PathVariable Long id,
                                              @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = depositManagementService.convertToPerformance(id, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "查询保证金统计信息")
    @GetMapping("/statistics")
    public Result<List<DepositManagement>> getDepositStatistics() {
        List<DepositManagement> statistics = depositManagementService.getDepositStatistics();
        return Result.success(statistics);
    }

    @Operation(summary = "保证金到期提醒")
    @GetMapping("/expiry-reminders")
    public Result<List<DepositManagement>> getExpiryReminders() {
        List<DepositManagement> reminders = depositManagementService.getExpiryReminders();
        return Result.success(reminders);
    }
}
