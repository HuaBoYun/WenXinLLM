package com.management.accountant.controller.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ss.SsCostAllocation;
import com.management.accountant.service.ss.SsCostAllocationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 成本分摊控制器
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ss/cost-allocation")
@Api(tags = "成本分摊管理")
public class SsCostAllocationController {

    @Autowired
    private SsCostAllocationService costAllocationService;

    @GetMapping("/page")
    @ApiOperation("分页查询成本分摊列表")
    public MyJsonBean getCostAllocationPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("查询参数") @RequestParam Map<String, Object> params) {
        try {
            IPage<SsCostAllocation> page = costAllocationService.getCostAllocationPage(current, size, params);
            return MyJsonBean.success(page);
        } catch (Exception e) {
            log.error("分页查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取成本分摊详情")
    public MyJsonBean getCostAllocationById(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            SsCostAllocation costAllocation = costAllocationService.getCostAllocationById(id);
            if (costAllocation != null) {
                return MyJsonBean.success(costAllocation);
            } else {
                return MyJsonBean.error("成本分摊不存在");
            }
        } catch (Exception e) {
            log.error("获取成本分摊详情失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation("创建成本分摊")
    public MyJsonBean createCostAllocation(@ApiParam("成本分摊信息") @RequestBody SsCostAllocation costAllocation) {
        try {
            boolean success = costAllocationService.createCostAllocation(costAllocation);
            if (success) {
                return MyJsonBean.success("创建成功");
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建成本分摊失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation("更新成本分摊")
    public MyJsonBean updateCostAllocation(@ApiParam("成本分摊信息") @RequestBody SsCostAllocation costAllocation) {
        try {
            boolean success = costAllocationService.updateCostAllocation(costAllocation);
            if (success) {
                return MyJsonBean.success("更新成功");
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新成本分摊失败", e);
            return MyJsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除成本分摊")
    public MyJsonBean deleteCostAllocation(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            boolean success = costAllocationService.deleteCostAllocation(id);
            if (success) {
                return MyJsonBean.success("删除成功");
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除成本分摊失败", e);
            return MyJsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除成本分摊")
    public MyJsonBean batchDeleteCostAllocation(@ApiParam("分摊ID列表") @RequestBody List<Long> ids) {
        try {
            boolean success = costAllocationService.batchDeleteCostAllocation(ids);
            if (success) {
                return MyJsonBean.success("批量删除成功");
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除成本分摊失败", e);
            return MyJsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    @ApiOperation("根据状态查询成本分摊列表")
    public MyJsonBean getCostAllocationByStatus(@ApiParam("分摊状态") @PathVariable String status) {
        try {
            List<SsCostAllocation> list = costAllocationService.getCostAllocationByStatus(status);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据状态查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    @ApiOperation("根据类型查询成本分摊列表")
    public MyJsonBean getCostAllocationByType(@ApiParam("分摊类型") @PathVariable String type) {
        try {
            List<SsCostAllocation> list = costAllocationService.getCostAllocationByType(type);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据类型查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/cost-center/{costCenterId}")
    @ApiOperation("根据成本中心查询成本分摊列表")
    public MyJsonBean getCostAllocationByCostCenter(@ApiParam("成本中心ID") @PathVariable Long costCenterId) {
        try {
            List<SsCostAllocation> list = costAllocationService.getCostAllocationByCostCenter(costCenterId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据成本中心查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/method/{method}")
    @ApiOperation("根据分摊方法查询成本分摊列表")
    public MyJsonBean getCostAllocationByMethod(@ApiParam("分摊方法") @PathVariable String method) {
        try {
            List<SsCostAllocation> list = costAllocationService.getCostAllocationByMethod(method);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据分摊方法查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/period/{period}")
    @ApiOperation("根据分摊周期查询成本分摊列表")
    public MyJsonBean getCostAllocationByPeriod(@ApiParam("分摊周期") @PathVariable String period) {
        try {
            List<SsCostAllocation> list = costAllocationService.getCostAllocationByPeriod(period);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据分摊周期查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/time-range")
    @ApiOperation("根据时间范围查询成本分摊列表")
    public MyJsonBean getCostAllocationByTimeRange(
            @ApiParam("开始时间") @RequestParam LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam LocalDateTime endTime) {
        try {
            List<SsCostAllocation> list = costAllocationService.getCostAllocationByTimeRange(startTime, endTime);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据时间范围查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/amount-range")
    @ApiOperation("根据金额范围查询成本分摊列表")
    public MyJsonBean getCostAllocationByAmountRange(
            @ApiParam("最小金额") @RequestParam BigDecimal minAmount,
            @ApiParam("最大金额") @RequestParam BigDecimal maxAmount) {
        try {
            List<SsCostAllocation> list = costAllocationService.getCostAllocationByAmountRange(minAmount, maxAmount);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据金额范围查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/priority/{priority}")
    @ApiOperation("根据优先级查询成本分摊列表")
    public MyJsonBean getCostAllocationByPriority(@ApiParam("优先级") @PathVariable Integer priority) {
        try {
            List<SsCostAllocation> list = costAllocationService.getCostAllocationByPriority(priority);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据优先级查询成本分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending")
    @ApiOperation("查询待分摊的成本分摊列表")
    public MyJsonBean getPendingAllocation() {
        try {
            List<SsCostAllocation> list = costAllocationService.getPendingAllocation();
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询待分摊列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/active")
    @ApiOperation("查询进行中的成本分摊列表")
    public MyJsonBean getActiveAllocation() {
        try {
            List<SsCostAllocation> list = costAllocationService.getActiveAllocation();
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询进行中列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/completed")
    @ApiOperation("查询已完成的成本分摊列表")
    public MyJsonBean getCompletedAllocation() {
        try {
            List<SsCostAllocation> list = costAllocationService.getCompletedAllocation();
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询已完成列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/failed")
    @ApiOperation("查询失败的成本分摊列表")
    public MyJsonBean getFailedAllocation() {
        try {
            List<SsCostAllocation> list = costAllocationService.getFailedAllocation();
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询失败列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/start")
    @ApiOperation("开始分摊计算")
    public MyJsonBean startAllocationCalculation(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            boolean success = costAllocationService.startAllocationCalculation(id);
            if (success) {
                return MyJsonBean.success("开始计算成功");
            } else {
                return MyJsonBean.error("开始计算失败");
            }
        } catch (Exception e) {
            log.error("开始分摊计算失败", e);
            return MyJsonBean.error("开始计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/stop")
    @ApiOperation("停止分摊计算")
    public MyJsonBean stopAllocationCalculation(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            boolean success = costAllocationService.stopAllocationCalculation(id);
            if (success) {
                return MyJsonBean.success("停止计算成功");
            } else {
                return MyJsonBean.error("停止计算失败");
            }
        } catch (Exception e) {
            log.error("停止分摊计算失败", e);
            return MyJsonBean.error("停止计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/pause")
    @ApiOperation("暂停分摊计算")
    public MyJsonBean pauseAllocationCalculation(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            boolean success = costAllocationService.pauseAllocationCalculation(id);
            if (success) {
                return MyJsonBean.success("暂停计算成功");
            } else {
                return MyJsonBean.error("暂停计算失败");
            }
        } catch (Exception e) {
            log.error("暂停分摊计算失败", e);
            return MyJsonBean.error("暂停计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/resume")
    @ApiOperation("恢复分摊计算")
    public MyJsonBean resumeAllocationCalculation(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            boolean success = costAllocationService.resumeAllocationCalculation(id);
            if (success) {
                return MyJsonBean.success("恢复计算成功");
            } else {
                return MyJsonBean.error("恢复计算失败");
            }
        } catch (Exception e) {
            log.error("恢复分摊计算失败", e);
            return MyJsonBean.error("恢复计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/recalculate")
    @ApiOperation("重新计算分摊")
    public MyJsonBean recalculateAllocation(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            boolean success = costAllocationService.recalculateAllocation(id);
            if (success) {
                return MyJsonBean.success("重新计算成功");
            } else {
                return MyJsonBean.error("重新计算失败");
            }
        } catch (Exception e) {
            log.error("重新计算分摊失败", e);
            return MyJsonBean.error("重新计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/start")
    @ApiOperation("批量开始分摊计算")
    public MyJsonBean batchStartCalculation(@ApiParam("分摊ID列表") @RequestBody List<Long> ids) {
        try {
            boolean success = costAllocationService.batchStartCalculation(ids);
            if (success) {
                return MyJsonBean.success("批量开始计算成功");
            } else {
                return MyJsonBean.error("批量开始计算失败");
            }
        } catch (Exception e) {
            log.error("批量开始分摊计算失败", e);
            return MyJsonBean.error("批量开始计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/stop")
    @ApiOperation("批量停止分摊计算")
    public MyJsonBean batchStopCalculation(@ApiParam("分摊ID列表") @RequestBody List<Long> ids) {
        try {
            boolean success = costAllocationService.batchStopCalculation(ids);
            if (success) {
                return MyJsonBean.success("批量停止计算成功");
            } else {
                return MyJsonBean.error("批量停止计算失败");
            }
        } catch (Exception e) {
            log.error("批量停止分摊计算失败", e);
            return MyJsonBean.error("批量停止计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/submit-approval")
    @ApiOperation("提交审批")
    public MyJsonBean submitForApproval(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            boolean success = costAllocationService.submitForApproval(id);
            if (success) {
                return MyJsonBean.success("提交审批成功");
            } else {
                return MyJsonBean.error("提交审批失败");
            }
        } catch (Exception e) {
            log.error("提交审批失败", e);
            return MyJsonBean.error("提交审批失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/approve")
    @ApiOperation("审批通过")
    public MyJsonBean approveAllocation(
            @ApiParam("分摊ID") @PathVariable Long id,
            @ApiParam("审批意见") @RequestParam(required = false) String comments) {
        try {
            boolean success = costAllocationService.approveAllocation(id, comments);
            if (success) {
                return MyJsonBean.success("审批通过成功");
            } else {
                return MyJsonBean.error("审批通过失败");
            }
        } catch (Exception e) {
            log.error("审批通过失败", e);
            return MyJsonBean.error("审批通过失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/reject")
    @ApiOperation("审批拒绝")
    public MyJsonBean rejectAllocation(
            @ApiParam("分摊ID") @PathVariable Long id,
            @ApiParam("拒绝原因") @RequestParam String comments) {
        try {
            boolean success = costAllocationService.rejectAllocation(id, comments);
            if (success) {
                return MyJsonBean.success("审批拒绝成功");
            } else {
                return MyJsonBean.error("审批拒绝失败");
            }
        } catch (Exception e) {
            log.error("审批拒绝失败", e);
            return MyJsonBean.error("审批拒绝失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/approve")
    @ApiOperation("批量审批通过")
    public MyJsonBean batchApprove(
            @ApiParam("分摊ID列表") @RequestBody List<Long> ids,
            @ApiParam("审批意见") @RequestParam(required = false) String comments) {
        try {
            boolean success = costAllocationService.batchApprove(ids, comments);
            if (success) {
                return MyJsonBean.success("批量审批通过成功");
            } else {
                return MyJsonBean.error("批量审批通过失败");
            }
        } catch (Exception e) {
            log.error("批量审批通过失败", e);
            return MyJsonBean.error("批量审批通过失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/reject")
    @ApiOperation("批量审批拒绝")
    public MyJsonBean batchReject(
            @ApiParam("分摊ID列表") @RequestBody List<Long> ids,
            @ApiParam("拒绝原因") @RequestParam String comments) {
        try {
            boolean success = costAllocationService.batchReject(ids, comments);
            if (success) {
                return MyJsonBean.success("批量审批拒绝成功");
            } else {
                return MyJsonBean.error("批量审批拒绝失败");
            }
        } catch (Exception e) {
            log.error("批量审批拒绝失败", e);
            return MyJsonBean.error("批量审批拒绝失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/copy")
    @ApiOperation("复制成本分摊")
    public MyJsonBean copyCostAllocation(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            SsCostAllocation copy = costAllocationService.copyCostAllocation(id);
            if (copy != null) {
                return MyJsonBean.success(copy);
            } else {
                return MyJsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制成本分摊失败", e);
            return MyJsonBean.error("复制失败: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    @ApiOperation("导入成本分摊数据")
    public MyJsonBean importCostAllocationData(@ApiParam("成本分摊数据列表") @RequestBody List<SsCostAllocation> costAllocations) {
        try {
            boolean success = costAllocationService.importCostAllocationData(costAllocations);
            if (success) {
                return MyJsonBean.success("导入成功");
            } else {
                return MyJsonBean.error("导入失败");
            }
        } catch (Exception e) {
            log.error("导入成本分摊数据失败", e);
            return MyJsonBean.error("导入失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出成本分摊数据")
    public MyJsonBean exportCostAllocationData(@ApiParam("查询参数") @RequestParam Map<String, Object> params) {
        try {
            List<SsCostAllocation> list = costAllocationService.exportCostAllocationData(params);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("导出成本分摊数据失败", e);
            return MyJsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/report")
    @ApiOperation("生成分摊报告")
    public MyJsonBean generateAllocationReport(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            String report = costAllocationService.generateAllocationReport(id);
            if (report != null) {
                return MyJsonBean.success(report);
            } else {
                return MyJsonBean.error("生成报告失败");
            }
        } catch (Exception e) {
            log.error("生成分摊报告失败", e);
            return MyJsonBean.error("生成报告失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/report")
    @ApiOperation("生成批量分摊报告")
    public MyJsonBean generateBatchAllocationReport(@ApiParam("分摊ID列表") @RequestBody List<Long> ids) {
        try {
            String report = costAllocationService.generateBatchAllocationReport(ids);
            if (report != null) {
                return MyJsonBean.success(report);
            } else {
                return MyJsonBean.error("生成批量报告失败");
            }
        } catch (Exception e) {
            log.error("生成批量分摊报告失败", e);
            return MyJsonBean.error("生成批量报告失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/notify")
    @ApiOperation("发送分摊通知")
    public MyJsonBean sendAllocationNotification(
            @ApiParam("分摊ID") @PathVariable Long id,
            @ApiParam("通知类型") @RequestParam String notificationType) {
        try {
            boolean success = costAllocationService.sendAllocationNotification(id, notificationType);
            if (success) {
                return MyJsonBean.success("发送通知成功");
            } else {
                return MyJsonBean.error("发送通知失败");
            }
        } catch (Exception e) {
            log.error("发送分摊通知失败", e);
            return MyJsonBean.error("发送通知失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/notify")
    @ApiOperation("批量发送分摊通知")
    public MyJsonBean batchSendNotification(
            @ApiParam("分摊ID列表") @RequestBody List<Long> ids,
            @ApiParam("通知类型") @RequestParam String notificationType) {
        try {
            boolean success = costAllocationService.batchSendNotification(ids, notificationType);
            if (success) {
                return MyJsonBean.success("批量发送通知成功");
            } else {
                return MyJsonBean.error("批量发送通知失败");
            }
        } catch (Exception e) {
            log.error("批量发送分摊通知失败", e);
            return MyJsonBean.error("批量发送通知失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("查询成本分摊统计信息")
    public MyJsonBean getAllocationStatistics() {
        try {
            Map<String, Object> statistics = costAllocationService.getAllocationStatistics();
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询成本分摊统计信息失败", e);
            return MyJsonBean.error("查询统计信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/distribution/status")
    @ApiOperation("查询成本分摊状态分布")
    public MyJsonBean getStatusDistribution() {
        try {
            List<Map<String, Object>> distribution = costAllocationService.getStatusDistribution();
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询成本分摊状态分布失败", e);
            return MyJsonBean.error("查询状态分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/distribution/type")
    @ApiOperation("查询成本分摊类型分布")
    public MyJsonBean getTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = costAllocationService.getTypeDistribution();
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询成本分摊类型分布失败", e);
            return MyJsonBean.error("查询类型分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/distribution/method")
    @ApiOperation("查询成本分摊方法分布")
    public MyJsonBean getMethodDistribution() {
        try {
            List<Map<String, Object>> distribution = costAllocationService.getMethodDistribution();
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询成本分摊方法分布失败", e);
            return MyJsonBean.error("查询方法分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/trend")
    @ApiOperation("查询成本分摊趋势数据")
    public MyJsonBean getAllocationTrend(
            @ApiParam("开始日期") @RequestParam String startDate,
            @ApiParam("结束日期") @RequestParam String endDate) {
        try {
            List<Map<String, Object>> trend = costAllocationService.getAllocationTrend(startDate, endDate);
            return MyJsonBean.success(trend);
        } catch (Exception e) {
            log.error("查询成本分摊趋势数据失败", e);
            return MyJsonBean.error("查询趋势数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/ranking")
    @ApiOperation("查询成本分摊排行榜")
    public MyJsonBean getAllocationRanking(
            @ApiParam("排行类型") @RequestParam String rankType,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> ranking = costAllocationService.getAllocationRanking(rankType, limit);
            return MyJsonBean.success(ranking);
        } catch (Exception e) {
            log.error("查询成本分摊排行榜失败", e);
            return MyJsonBean.error("查询排行榜失败: " + e.getMessage());
        }
    }

    @GetMapping("/analysis/efficiency")
    @ApiOperation("查询成本分摊效率分析")
    public MyJsonBean getEfficiencyAnalysis() {
        try {
            List<Map<String, Object>> analysis = costAllocationService.getEfficiencyAnalysis();
            return MyJsonBean.success(analysis);
        } catch (Exception e) {
            log.error("查询成本分摊效率分析失败", e);
            return MyJsonBean.error("查询效率分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/analysis/quality")
    @ApiOperation("查询成本分摊质量分析")
    public MyJsonBean getQualityAnalysis() {
        try {
            List<Map<String, Object>> analysis = costAllocationService.getQualityAnalysis();
            return MyJsonBean.success(analysis);
        } catch (Exception e) {
            log.error("查询成本分摊质量分析失败", e);
            return MyJsonBean.error("查询质量分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/analysis/cost")
    @ApiOperation("查询成本分摊成本分析")
    public MyJsonBean getCostAnalysis() {
        try {
            List<Map<String, Object>> analysis = costAllocationService.getCostAnalysis();
            return MyJsonBean.success(analysis);
        } catch (Exception e) {
            log.error("查询成本分摊成本分析失败", e);
            return MyJsonBean.error("查询成本分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/analysis/risk")
    @ApiOperation("查询成本分摊风险分析")
    public MyJsonBean getRiskAnalysis() {
        try {
            List<Map<String, Object>> analysis = costAllocationService.getRiskAnalysis();
            return MyJsonBean.success(analysis);
        } catch (Exception e) {
            log.error("查询成本分摊风险分析失败", e);
            return MyJsonBean.error("查询风险分析失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/clean")
    @ApiOperation("清理过期数据")
    public MyJsonBean cleanExpiredData(@ApiParam("过期天数") @RequestParam(defaultValue = "30") Integer expiredDays) {
        try {
            boolean success = costAllocationService.cleanExpiredData(expiredDays);
            if (success) {
                return MyJsonBean.success("清理过期数据成功");
            } else {
                return MyJsonBean.error("清理过期数据失败");
            }
        } catch (Exception e) {
            log.error("清理过期数据失败", e);
            return MyJsonBean.error("清理过期数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/code")
    @ApiOperation("检查编码是否存在")
    public MyJsonBean checkCodeExists(
            @ApiParam("分摊编码") @RequestParam String allocationCode,
            @ApiParam("分摊ID") @RequestParam(required = false) Long allocationId) {
        try {
            boolean exists = costAllocationService.checkCodeExists(allocationCode, allocationId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查编码是否存在失败", e);
            return MyJsonBean.error("检查编码失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/name")
    @ApiOperation("检查名称是否存在")
    public MyJsonBean checkNameExists(
            @ApiParam("分摊名称") @RequestParam String allocationName,
            @ApiParam("分摊ID") @RequestParam(required = false) Long allocationId) {
        try {
            boolean exists = costAllocationService.checkNameExists(allocationName, allocationId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查名称是否存在失败", e);
            return MyJsonBean.error("检查名称失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/calculate")
    @ApiOperation("执行分摊计算")
    public MyJsonBean executeAllocationCalculation(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            SsCostAllocation allocation = costAllocationService.getCostAllocationById(id);
            if (allocation == null) {
                return MyJsonBean.error("成本分摊不存在");
            }
            
            Map<String, Object> result = costAllocationService.executeAllocationCalculation(allocation);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("执行分摊计算失败", e);
            return MyJsonBean.error("执行分摊计算失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/analyze")
    @ApiOperation("分摊结果分析")
    public MyJsonBean analyzeAllocationResult(@ApiParam("分摊ID") @PathVariable Long id) {
        try {
            Map<String, Object> analysis = costAllocationService.analyzeAllocationResult(id);
            return MyJsonBean.success(analysis);
        } catch (Exception e) {
            log.error("分摊结果分析失败", e);
            return MyJsonBean.error("分摊结果分析失败: " + e.getMessage());
        }
    }
}
