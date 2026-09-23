package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ncv65.BudgetAdjustment;
import com.management.accountant.service.ncv65.IBudgetAdjustmentService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算调整控制器
 * 
 * @description 预算调整管理API接口，支持预算调整的申请、审批和执行
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算调整管理")
@RestController
@RequestMapping("/budget/adjustment")
@Validated
public class BudgetAdjustmentController {

    @Resource
    private IBudgetAdjustmentService budgetAdjustmentService;

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建预算调整
     */
    @ApiOperation("创建预算调整")
    @PostMapping
    public MyJsonBean<String> createBudgetAdjustment(@Valid @RequestBody BudgetAdjustment adjustment) {
        try {
            boolean result = budgetAdjustmentService.createBudgetAdjustment(adjustment);
            if (result) {
                return MyJsonBean.successData(adjustment.getId(),  "预算调整创建成功");
            } else {
                return MyJsonBean.error("预算调整创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("创建预算调整失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算调整
     */
    @ApiOperation("更新预算调整")
    @PutMapping("/{id}")
    public MyJsonBean<Boolean> updateBudgetAdjustment(
            @ApiParam("调整ID") @PathVariable String id,
            @Valid @RequestBody BudgetAdjustment adjustment) {
        try {
            adjustment.setId(id);
            boolean result = budgetAdjustmentService.updateBudgetAdjustment(adjustment);
            if (result) {
                return MyJsonBean.successData(true,  "预算调整更新成功");
            } else {
                return MyJsonBean.error("预算调整更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("更新预算调整失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算调整
     */
    @ApiOperation("删除预算调整")
    @DeleteMapping("/{id}")
    public MyJsonBean<Boolean> deleteBudgetAdjustment(@ApiParam("调整ID") @PathVariable String id) {
        try {
            boolean result = budgetAdjustmentService.deleteBudgetAdjustment(id);
            if (result) {
                return MyJsonBean.successData(true,  "预算调整删除成功");
            } else {
                return MyJsonBean.error("预算调整删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("删除预算调整失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算调整
     */
    @ApiOperation("批量删除预算调整")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetAdjustments(@RequestBody List<String> ids) {
        try {
            boolean result = budgetAdjustmentService.batchDeleteBudgetAdjustments(ids);
            if (result) {
                return MyJsonBean.successData(true,  "批量删除预算调整成功");
            } else {
                return MyJsonBean.error("批量删除预算调整失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量删除预算调整失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算调整
     */
    @ApiOperation("根据ID查询预算调整")
    @GetMapping("/{id}")
    public MyJsonBean<BudgetAdjustment> getBudgetAdjustmentById(@ApiParam("调整ID") @PathVariable String id) {
        try {
            BudgetAdjustment adjustment = budgetAdjustmentService.getBudgetAdjustmentById(id);
            return MyJsonBean.successData(adjustment,  "查询成功");
        } catch (Exception e) {
            log.error("查询预算调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询预算调整失败：" + e.getMessage());
        }
    }

    /**
     * 根据编码查询预算调整
     */
    @ApiOperation("根据编码查询预算调整")
    @GetMapping("/code/{adjustmentCode}")
    public MyJsonBean<BudgetAdjustment> getBudgetAdjustmentByCode(@ApiParam("调整编码") @PathVariable String adjustmentCode) {
        try {
            BudgetAdjustment adjustment = budgetAdjustmentService.getBudgetAdjustmentByCode(adjustmentCode);
            return MyJsonBean.successData(adjustment,  "查询成功");
        } catch (Exception e) {
            log.error("根据编码查询预算调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据编码查询预算调整失败：" + e.getMessage());
        }
    }

    // ==================== 查询操作 ====================

    /**
     * 分页查询预算调整
     */
    @ApiOperation("分页查询预算调整")
    @PostMapping("/page")
    public MyJsonBean<IPage<BudgetAdjustment>> getBudgetAdjustmentPage(@RequestBody Map<String, Object> params) {
        try {
            Integer current = (Integer) params.getOrDefault("current", 1);
            Integer size = (Integer) params.getOrDefault("size", 10);
            IPage<BudgetAdjustment> page = budgetAdjustmentService.getBudgetAdjustmentPage(current, size, params);
            return MyJsonBean.successData(page,  "查询成功");
        } catch (Exception e) {
            log.error("分页查询预算调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分页查询预算调整失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算年度查询调整列表
     */
    @ApiOperation("根据预算年度查询调整列表")
    @GetMapping("/fiscal-year/{fiscalYear}")
    public MyJsonBean<List<BudgetAdjustment>> getBudgetAdjustmentsByFiscalYear(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            List<BudgetAdjustment> adjustments = budgetAdjustmentService.getBudgetAdjustmentsByFiscalYear(fiscalYear);
            return MyJsonBean.successData(adjustments,  "查询成功");
        } catch (Exception e) {
            log.error("根据预算年度查询调整列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据预算年度查询调整列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据调整类型查询调整列表
     */
    @ApiOperation("根据调整类型查询调整列表")
    @GetMapping("/type/{adjustmentType}")
    public MyJsonBean<List<BudgetAdjustment>> getBudgetAdjustmentsByType(@ApiParam("调整类型") @PathVariable String adjustmentType) {
        try {
            List<BudgetAdjustment> adjustments = budgetAdjustmentService.getBudgetAdjustmentsByType(adjustmentType);
            return MyJsonBean.successData(adjustments,  "查询成功");
        } catch (Exception e) {
            log.error("根据调整类型查询调整列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据调整类型查询调整列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询我申请的调整列表
     */
    @ApiOperation("查询我申请的调整列表")
    @GetMapping("/my-applications")
    public MyJsonBean<List<BudgetAdjustment>> getMyApplications(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetAdjustment> adjustments = budgetAdjustmentService.getMyApplications(userId);
            return MyJsonBean.successData(adjustments,  "查询成功");
        } catch (Exception e) {
            log.error("查询我申请的调整列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询我申请的调整列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询待我审批的调整列表
     */
    @ApiOperation("查询待我审批的调整列表")
    @GetMapping("/pending-approvals")
    public MyJsonBean<List<BudgetAdjustment>> getPendingApprovals(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetAdjustment> adjustments = budgetAdjustmentService.getPendingApprovals(userId);
            return MyJsonBean.successData(adjustments,  "查询成功");
        } catch (Exception e) {
            log.error("查询待我审批的调整列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询待我审批的调整列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询待执行的调整列表
     */
    @ApiOperation("查询待执行的调整列表")
    @GetMapping("/pending-executions")
    public MyJsonBean<List<BudgetAdjustment>> getPendingExecutions(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetAdjustment> adjustments = budgetAdjustmentService.getPendingExecutions(userId);
            return MyJsonBean.successData(adjustments,  "查询成功");
        } catch (Exception e) {
            log.error("查询待执行的调整列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询待执行的调整列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询紧急调整列表
     */
    @ApiOperation("查询紧急调整列表")
    @GetMapping("/urgent")
    public MyJsonBean<List<BudgetAdjustment>> getUrgentAdjustments() {
        try {
            List<BudgetAdjustment> adjustments = budgetAdjustmentService.getUrgentAdjustments();
            return MyJsonBean.successData(adjustments,  "查询成功");
        } catch (Exception e) {
            log.error("查询紧急调整列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询紧急调整列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询执行失败的调整列表
     */
    @ApiOperation("查询执行失败的调整列表")
    @GetMapping("/failed")
    public MyJsonBean<List<BudgetAdjustment>> getFailedAdjustments() {
        try {
            List<BudgetAdjustment> adjustments = budgetAdjustmentService.getFailedAdjustments();
            return MyJsonBean.successData(adjustments,  "查询成功");
        } catch (Exception e) {
            log.error("查询执行失败的调整列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询执行失败的调整列表失败：" + e.getMessage());
        }
    }

    // ==================== 业务操作 ====================

    /**
     * 提交审批
     */
    @ApiOperation("提交审批")
    @PostMapping("/{id}/submit")
    public MyJsonBean<Boolean> submitForApproval(
            @ApiParam("调整ID") @PathVariable String id,
            @ApiParam("审批流程ID") @RequestParam String workflowId) {
        try {
            boolean result = budgetAdjustmentService.submitForApproval(id, workflowId);
            if (result) {
                return MyJsonBean.successData(true,  "提交审批成功");
            } else {
                return MyJsonBean.error("提交审批失败");
            }
        } catch (Exception e) {
            log.error("提交审批失败：{}", e.getMessage(), e);
            return MyJsonBean.error("提交审批失败：" + e.getMessage());
        }
    }

    /**
     * 审批通过
     */
    @ApiOperation("审批通过")
    @PostMapping("/{id}/approve")
    public MyJsonBean<Boolean> approveAdjustment(
            @ApiParam("调整ID") @PathVariable String id,
            @ApiParam("审批意见") @RequestParam(required = false) String approvalComments) {
        try {
            boolean result = budgetAdjustmentService.approveAdjustment(id, approvalComments);
            if (result) {
                return MyJsonBean.successData(true,  "审批通过成功");
            } else {
                return MyJsonBean.error("审批通过失败");
            }
        } catch (Exception e) {
            log.error("审批通过失败：{}", e.getMessage(), e);
            return MyJsonBean.error("审批通过失败：" + e.getMessage());
        }
    }

    /**
     * 审批拒绝
     */
    @ApiOperation("审批拒绝")
    @PostMapping("/{id}/reject")
    public MyJsonBean<Boolean> rejectAdjustment(
            @ApiParam("调整ID") @PathVariable String id,
            @ApiParam("审批意见") @RequestParam String approvalComments) {
        try {
            boolean result = budgetAdjustmentService.rejectAdjustment(id, approvalComments);
            if (result) {
                return MyJsonBean.successData(true,  "审批拒绝成功");
            } else {
                return MyJsonBean.error("审批拒绝失败");
            }
        } catch (Exception e) {
            log.error("审批拒绝失败：{}", e.getMessage(), e);
            return MyJsonBean.error("审批拒绝失败：" + e.getMessage());
        }
    }

    /**
     * 开始执行
     */
    @ApiOperation("开始执行")
    @PostMapping("/{id}/execute")
    public MyJsonBean<Boolean> startExecution(@ApiParam("调整ID") @PathVariable String id) {
        try {
            boolean result = budgetAdjustmentService.startExecution(id);
            if (result) {
                return MyJsonBean.successData(true,  "开始执行成功");
            } else {
                return MyJsonBean.error("开始执行失败");
            }
        } catch (Exception e) {
            log.error("开始执行失败：{}", e.getMessage(), e);
            return MyJsonBean.error("开始执行失败：" + e.getMessage());
        }
    }

    /**
     * 完成执行
     */
    @ApiOperation("完成执行")
    @PostMapping("/{id}/complete")
    public MyJsonBean<Boolean> completeExecution(
            @ApiParam("调整ID") @PathVariable String id,
            @ApiParam("执行结果") @RequestParam(required = false) String executionResult) {
        try {
            boolean result = budgetAdjustmentService.completeExecution(id, executionResult);
            if (result) {
                return MyJsonBean.successData(true,  "完成执行成功");
            } else {
                return MyJsonBean.error("完成执行失败");
            }
        } catch (Exception e) {
            log.error("完成执行失败：{}", e.getMessage(), e);
            return MyJsonBean.error("完成执行失败：" + e.getMessage());
        }
    }

    /**
     * 取消调整
     */
    @ApiOperation("取消调整")
    @PostMapping("/{id}/cancel")
    public MyJsonBean<Boolean> cancelAdjustment(@ApiParam("调整ID") @PathVariable String id) {
        try {
            boolean result = budgetAdjustmentService.cancelAdjustment(id);
            if (result) {
                return MyJsonBean.successData(true,  "取消调整成功");
            } else {
                return MyJsonBean.error("取消调整失败");
            }
        } catch (Exception e) {
            log.error("取消调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("取消调整失败：" + e.getMessage());
        }
    }

    /**
     * 复制调整
     */
    @ApiOperation("复制调整")
    @PostMapping("/{id}/copy")
    public MyJsonBean<String> copyAdjustment(
            @ApiParam("调整ID") @PathVariable String id,
            @ApiParam("新调整编码") @RequestParam String newAdjustmentCode,
            @ApiParam("新调整名称") @RequestParam String newAdjustmentName) {
        try {
            String newAdjustmentId = budgetAdjustmentService.copyAdjustment(id, newAdjustmentCode, newAdjustmentName);
            if (newAdjustmentId != null) {
                return MyJsonBean.successData(newAdjustmentId,  "复制调整成功");
            } else {
                return MyJsonBean.error("复制调整失败");
            }
        } catch (Exception e) {
            log.error("复制调整失败：{}", e.getMessage(), e);
            return MyJsonBean.error("复制调整失败：" + e.getMessage());
        }
    }

    /**
     * 批量审批
     */
    @ApiOperation("批量审批")
    @PostMapping("/batch-approve")
    public MyJsonBean<Integer> batchApproveAdjustments(
            @RequestBody List<String> adjustmentIds,
            @ApiParam("审批意见") @RequestParam(required = false) String approvalComments,
            @ApiParam("是否通过") @RequestParam boolean isApproved) {
        try {
            int count = budgetAdjustmentService.batchApproveAdjustments(adjustmentIds, approvalComments, isApproved);
            return MyJsonBean.successData(count,  "批量审批成功，处理了" + count + "个调整");
        } catch (Exception e) {
            log.error("批量审批失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量审批失败：" + e.getMessage());
        }
    }

    /**
     * 批量执行
     */
    @ApiOperation("批量执行")
    @PostMapping("/batch-execute")
    public MyJsonBean<Integer> batchExecuteAdjustments(@RequestBody List<String> adjustmentIds) {
        try {
            int count = budgetAdjustmentService.batchExecuteAdjustments(adjustmentIds);
            return MyJsonBean.successData(count,  "批量执行成功，处理了" + count + "个调整");
        } catch (Exception e) {
            log.error("批量执行失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量执行失败：" + e.getMessage());
        }
    }

    // ==================== 调整分析 ====================

    /**
     * 分析调整影响
     */
    @ApiOperation("分析调整影响")
    @PostMapping("/{id}/analyze-impact")
    public MyJsonBean<Map<String, Object>> analyzeAdjustmentImpact(@ApiParam("调整ID") @PathVariable String id) {
        try {
            Map<String, Object> result = budgetAdjustmentService.analyzeAdjustmentImpact(id);
            return MyJsonBean.successData(result,  "影响分析成功");
        } catch (Exception e) {
            log.error("分析调整影响失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分析调整影响失败：" + e.getMessage());
        }
    }

    /**
     * 评估调整风险
     */
    @ApiOperation("评估调整风险")
    @PostMapping("/{id}/assess-risk")
    public MyJsonBean<Map<String, Object>> assessAdjustmentRisk(@ApiParam("调整ID") @PathVariable String id) {
        try {
            Map<String, Object> result = budgetAdjustmentService.assessAdjustmentRisk(id);
            return MyJsonBean.successData(result,  "风险评估成功");
        } catch (Exception e) {
            log.error("评估调整风险失败：{}", e.getMessage(), e);
            return MyJsonBean.error("评估调整风险失败：" + e.getMessage());
        }
    }

    /**
     * 生成调整建议
     */
    @ApiOperation("生成调整建议")
    @PostMapping("/generate-suggestions")
    public MyJsonBean<Map<String, Object>> generateAdjustmentSuggestions(
            @ApiParam("组织ID") @RequestParam String organizationId,
            @ApiParam("指标ID") @RequestParam String indicatorId,
            @ApiParam("预算年度") @RequestParam Integer fiscalYear) {
        try {
            Map<String, Object> result = budgetAdjustmentService.generateAdjustmentSuggestions(organizationId, indicatorId, fiscalYear);
            return MyJsonBean.successData(result,  "生成调整建议成功");
        } catch (Exception e) {
            log.error("生成调整建议失败：{}", e.getMessage(), e);
            return MyJsonBean.error("生成调整建议失败：" + e.getMessage());
        }
    }

    // ==================== 统计分析 ====================

    /**
     * 获取调整统计信息
     */
    @ApiOperation("获取调整统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetAdjustmentStatistics() {
        try {
            Map<String, Object> statistics = budgetAdjustmentService.getBudgetAdjustmentStatistics();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("获取调整统计信息失败：{}", e.getMessage(), e);
            return MyJsonBean.error("获取调整统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 按调整类型统计数量
     */
    @ApiOperation("按调整类型统计数量")
    @GetMapping("/statistics/by-type")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAdjustmentCountByType() {
        try {
            List<Map<String, Object>> statistics = budgetAdjustmentService.getBudgetAdjustmentCountByType();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("按调整类型统计数量失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按调整类型统计数量失败：" + e.getMessage());
        }
    }

    /**
     * 按年度统计调整数量
     */
    @ApiOperation("按年度统计调整数量")
    @GetMapping("/statistics/by-year")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAdjustmentCountByYear() {
        try {
            List<Map<String, Object>> statistics = budgetAdjustmentService.getBudgetAdjustmentCountByYear();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("按年度统计调整数量失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按年度统计调整数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计调整金额
     */
    @ApiOperation("统计调整金额")
    @GetMapping("/statistics/amount/{fiscalYear}")
    public MyJsonBean<Map<String, Object>> getBudgetAdjustmentAmountSummary(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            Map<String, Object> statistics = budgetAdjustmentService.getBudgetAdjustmentAmountSummary(fiscalYear);
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("统计调整金额失败：{}", e.getMessage(), e);
            return MyJsonBean.error("统计调整金额失败：" + e.getMessage());
        }
    }

    // ==================== 数据导入导出 ====================

    /**
     * 导出调整数据
     */
    @ApiOperation("导出调整数据")
    @PostMapping("/export")
    public MyJsonBean<String> exportAdjustments(@RequestBody List<String> adjustmentIds) {
        try {
            String filePath = budgetAdjustmentService.exportAdjustments(adjustmentIds);
            return MyJsonBean.successData(filePath,  "导出成功");
        } catch (Exception e) {
            log.error("导出调整数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("导出调整数据失败：" + e.getMessage());
        }
    }

    /**
     * 导入调整数据
     */
    @ApiOperation("导入调整数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importAdjustments(@ApiParam("文件路径") @RequestParam String filePath) {
        try {
            Map<String, Object> result = budgetAdjustmentService.importAdjustments(filePath);
            return MyJsonBean.successData(result,  "导入成功");
        } catch (Exception e) {
            log.error("导入调整数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("导入调整数据失败：" + e.getMessage());
        }
    }
}
