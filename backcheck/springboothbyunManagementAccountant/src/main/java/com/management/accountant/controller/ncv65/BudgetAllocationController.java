package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ncv65.BudgetAllocation;
import com.management.accountant.service.ncv65.IBudgetAllocationService;
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
 * NCV65全面预算系统 - 预算分配控制器
 * 
 * @description 预算分配管理API接口，支持预算资源的分配、调配和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算分配管理")
@RestController
@RequestMapping("/budget/allocation")
@Validated
public class BudgetAllocationController {

    @Resource
    private IBudgetAllocationService budgetAllocationService;

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建预算分配
     */
    @ApiOperation("创建预算分配")
    @PostMapping
    public MyJsonBean<String> createBudgetAllocation(@Valid @RequestBody BudgetAllocation allocation) {
        try {
            boolean result = budgetAllocationService.createBudgetAllocation(allocation);
            if (result) {
                return MyJsonBean.successData(allocation.getId(),  "预算分配创建成功");
            } else {
                return MyJsonBean.error("预算分配创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("创建预算分配失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算分配
     */
    @ApiOperation("更新预算分配")
    @PutMapping("/{id}")
    public MyJsonBean<Boolean> updateBudgetAllocation(
            @ApiParam("分配ID") @PathVariable String id,
            @Valid @RequestBody BudgetAllocation allocation) {
        try {
            allocation.setId(id);
            boolean result = budgetAllocationService.updateBudgetAllocation(allocation);
            if (result) {
                return MyJsonBean.successData(true,  "预算分配更新成功");
            } else {
                return MyJsonBean.error("预算分配更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("更新预算分配失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算分配
     */
    @ApiOperation("删除预算分配")
    @DeleteMapping("/{id}")
    public MyJsonBean<Boolean> deleteBudgetAllocation(@ApiParam("分配ID") @PathVariable String id) {
        try {
            boolean result = budgetAllocationService.deleteBudgetAllocation(id);
            if (result) {
                return MyJsonBean.successData(true,  "预算分配删除成功");
            } else {
                return MyJsonBean.error("预算分配删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("删除预算分配失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算分配
     */
    @ApiOperation("批量删除预算分配")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetAllocations(@RequestBody List<String> ids) {
        try {
            boolean result = budgetAllocationService.batchDeleteBudgetAllocations(ids);
            if (result) {
                return MyJsonBean.successData(true,  "批量删除预算分配成功");
            } else {
                return MyJsonBean.error("批量删除预算分配失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量删除预算分配失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算分配
     */
    @ApiOperation("根据ID查询预算分配")
    @GetMapping("/{id}")
    public MyJsonBean<BudgetAllocation> getBudgetAllocationById(@ApiParam("分配ID") @PathVariable String id) {
        try {
            BudgetAllocation allocation = budgetAllocationService.getBudgetAllocationById(id);
            return MyJsonBean.successData(allocation,  "查询成功");
        } catch (Exception e) {
            log.error("查询预算分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询预算分配失败：" + e.getMessage());
        }
    }

    /**
     * 根据编码查询预算分配
     */
    @ApiOperation("根据编码查询预算分配")
    @GetMapping("/code/{allocationCode}")
    public MyJsonBean<BudgetAllocation> getBudgetAllocationByCode(@ApiParam("分配编码") @PathVariable String allocationCode) {
        try {
            BudgetAllocation allocation = budgetAllocationService.getBudgetAllocationByCode(allocationCode);
            return MyJsonBean.successData(allocation,  "查询成功");
        } catch (Exception e) {
            log.error("根据编码查询预算分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据编码查询预算分配失败：" + e.getMessage());
        }
    }

    // ==================== 查询操作 ====================

    /**
     * 分页查询预算分配
     */
    @ApiOperation("分页查询预算分配")
    @PostMapping("/page")
    public MyJsonBean<IPage<BudgetAllocation>> getBudgetAllocationPage(@RequestBody Map<String, Object> params) {
        try {
            Integer current = (Integer) params.getOrDefault("current", 1);
            Integer size = (Integer) params.getOrDefault("size", 10);
            IPage<BudgetAllocation> page = budgetAllocationService.getBudgetAllocationPage(current, size, params);
            return MyJsonBean.successData(page,  "查询成功");
        } catch (Exception e) {
            log.error("分页查询预算分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分页查询预算分配失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算年度查询分配列表
     */
    @ApiOperation("根据预算年度查询分配列表")
    @GetMapping("/fiscal-year/{fiscalYear}")
    public MyJsonBean<List<BudgetAllocation>> getBudgetAllocationsByFiscalYear(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            List<BudgetAllocation> allocations = budgetAllocationService.getBudgetAllocationsByFiscalYear(fiscalYear);
            return MyJsonBean.successData(allocations,  "查询成功");
        } catch (Exception e) {
            log.error("根据预算年度查询分配列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据预算年度查询分配列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据分配类型查询分配列表
     */
    @ApiOperation("根据分配类型查询分配列表")
    @GetMapping("/type/{allocationType}")
    public MyJsonBean<List<BudgetAllocation>> getBudgetAllocationsByType(@ApiParam("分配类型") @PathVariable String allocationType) {
        try {
            List<BudgetAllocation> allocations = budgetAllocationService.getBudgetAllocationsByType(allocationType);
            return MyJsonBean.successData(allocations,  "查询成功");
        } catch (Exception e) {
            log.error("根据分配类型查询分配列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据分配类型查询分配列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询我分配的预算列表
     */
    @ApiOperation("查询我分配的预算列表")
    @GetMapping("/my-allocations")
    public MyJsonBean<List<BudgetAllocation>> getMyAllocations(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetAllocation> allocations = budgetAllocationService.getMyAllocations(userId);
            return MyJsonBean.successData(allocations,  "查询成功");
        } catch (Exception e) {
            log.error("查询我分配的预算列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询我分配的预算列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询待我审批的分配列表
     */
    @ApiOperation("查询待我审批的分配列表")
    @GetMapping("/pending-approvals")
    public MyJsonBean<List<BudgetAllocation>> getPendingApprovals(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetAllocation> allocations = budgetAllocationService.getPendingApprovals(userId);
            return MyJsonBean.successData(allocations,  "查询成功");
        } catch (Exception e) {
            log.error("查询待我审批的分配列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询待我审批的分配列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询待执行的分配列表
     */
    @ApiOperation("查询待执行的分配列表")
    @GetMapping("/pending-executions")
    public MyJsonBean<List<BudgetAllocation>> getPendingExecutions(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetAllocation> allocations = budgetAllocationService.getPendingExecutions(userId);
            return MyJsonBean.successData(allocations,  "查询成功");
        } catch (Exception e) {
            log.error("查询待执行的分配列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询待执行的分配列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询执行失败的分配列表
     */
    @ApiOperation("查询执行失败的分配列表")
    @GetMapping("/failed")
    public MyJsonBean<List<BudgetAllocation>> getFailedAllocations() {
        try {
            List<BudgetAllocation> allocations = budgetAllocationService.getFailedAllocations();
            return MyJsonBean.successData(allocations,  "查询成功");
        } catch (Exception e) {
            log.error("查询执行失败的分配列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询执行失败的分配列表失败：" + e.getMessage());
        }
    }

    // ==================== 业务操作 ====================

    /**
     * 审批通过
     */
    @ApiOperation("审批通过")
    @PostMapping("/{id}/approve")
    public MyJsonBean<Boolean> approveAllocation(
            @ApiParam("分配ID") @PathVariable String id,
            @ApiParam("审批意见") @RequestParam(required = false) String approvalComments) {
        try {
            boolean result = budgetAllocationService.approveAllocation(id, approvalComments);
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
    public MyJsonBean<Boolean> rejectAllocation(
            @ApiParam("分配ID") @PathVariable String id,
            @ApiParam("审批意见") @RequestParam String approvalComments) {
        try {
            boolean result = budgetAllocationService.rejectAllocation(id, approvalComments);
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
    public MyJsonBean<Boolean> startExecution(@ApiParam("分配ID") @PathVariable String id) {
        try {
            boolean result = budgetAllocationService.startExecution(id);
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
            @ApiParam("分配ID") @PathVariable String id,
            @ApiParam("执行结果") @RequestParam(required = false) String executionResult) {
        try {
            boolean result = budgetAllocationService.completeExecution(id, executionResult);
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
     * 撤销分配
     */
    @ApiOperation("撤销分配")
    @PostMapping("/{id}/revoke")
    public MyJsonBean<Boolean> revokeAllocation(
            @ApiParam("分配ID") @PathVariable String id,
            @ApiParam("撤销信息") @RequestParam(required = false) String revocationInfo) {
        try {
            boolean result = budgetAllocationService.revokeAllocation(id, revocationInfo);
            if (result) {
                return MyJsonBean.successData(true,  "撤销分配成功");
            } else {
                return MyJsonBean.error("撤销分配失败");
            }
        } catch (Exception e) {
            log.error("撤销分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("撤销分配失败：" + e.getMessage());
        }
    }

    /**
     * 复制分配
     */
    @ApiOperation("复制分配")
    @PostMapping("/{id}/copy")
    public MyJsonBean<String> copyAllocation(
            @ApiParam("分配ID") @PathVariable String id,
            @ApiParam("新分配编码") @RequestParam String newAllocationCode,
            @ApiParam("新分配名称") @RequestParam String newAllocationName) {
        try {
            String newAllocationId = budgetAllocationService.copyAllocation(id, newAllocationCode, newAllocationName);
            if (newAllocationId != null) {
                return MyJsonBean.successData(newAllocationId,  "复制分配成功");
            } else {
                return MyJsonBean.error("复制分配失败");
            }
        } catch (Exception e) {
            log.error("复制分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("复制分配失败：" + e.getMessage());
        }
    }

    /**
     * 批量审批
     */
    @ApiOperation("批量审批")
    @PostMapping("/batch-approve")
    public MyJsonBean<Integer> batchApproveAllocations(
            @RequestBody List<String> allocationIds,
            @ApiParam("审批意见") @RequestParam(required = false) String approvalComments,
            @ApiParam("是否通过") @RequestParam boolean isApproved) {
        try {
            int count = budgetAllocationService.batchApproveAllocations(allocationIds, approvalComments, isApproved);
            return MyJsonBean.successData(count,  "批量审批成功，处理了" + count + "个分配");
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
    public MyJsonBean<Integer> batchExecuteAllocations(@RequestBody List<String> allocationIds) {
        try {
            int count = budgetAllocationService.batchExecuteAllocations(allocationIds);
            return MyJsonBean.successData(count,  "批量执行成功，处理了" + count + "个分配");
        } catch (Exception e) {
            log.error("批量执行失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量执行失败：" + e.getMessage());
        }
    }

    // ==================== 分配策略 ====================

    /**
     * 自动分配
     */
    @ApiOperation("自动分配")
    @PostMapping("/auto-allocate")
    public MyJsonBean<Map<String, Object>> autoAllocate(
            @ApiParam("组织ID") @RequestParam String organizationId,
            @ApiParam("指标ID") @RequestParam String indicatorId,
            @ApiParam("预算年度") @RequestParam Integer fiscalYear,
            @ApiParam("分配策略") @RequestParam String allocationStrategy) {
        try {
            Map<String, Object> result = budgetAllocationService.autoAllocate(organizationId, indicatorId, fiscalYear, allocationStrategy);
            return MyJsonBean.successData(result,  "自动分配成功");
        } catch (Exception e) {
            log.error("自动分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("自动分配失败：" + e.getMessage());
        }
    }

    /**
     * 按比例分配
     */
    @ApiOperation("按比例分配")
    @PostMapping("/allocate-by-ratio")
    public MyJsonBean<Map<String, Object>> allocateByRatio(@RequestBody Map<String, Object> params) {
        try {
            String sourceOrganizationId = (String) params.get("sourceOrganizationId");
            List<String> targetOrganizationIds = (List<String>) params.get("targetOrganizationIds");
            String indicatorId = (String) params.get("indicatorId");
            String totalAmount = (String) params.get("totalAmount");
            List<String> ratios = (List<String>) params.get("ratios");
            
            Map<String, Object> result = budgetAllocationService.allocateByRatio(sourceOrganizationId, targetOrganizationIds, indicatorId, totalAmount, ratios);
            return MyJsonBean.successData(result,  "按比例分配成功");
        } catch (Exception e) {
            log.error("按比例分配失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按比例分配失败：" + e.getMessage());
        }
    }

    // ==================== 分配分析 ====================

    /**
     * 分析分配影响
     */
    @ApiOperation("分析分配影响")
    @PostMapping("/{id}/analyze-impact")
    public MyJsonBean<Map<String, Object>> analyzeAllocationImpact(@ApiParam("分配ID") @PathVariable String id) {
        try {
            Map<String, Object> result = budgetAllocationService.analyzeAllocationImpact(id);
            return MyJsonBean.successData(result,  "影响分析成功");
        } catch (Exception e) {
            log.error("分析分配影响失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分析分配影响失败：" + e.getMessage());
        }
    }

    /**
     * 评估分配合理性
     */
    @ApiOperation("评估分配合理性")
    @PostMapping("/{id}/assess-rationality")
    public MyJsonBean<Map<String, Object>> assessAllocationRationality(@ApiParam("分配ID") @PathVariable String id) {
        try {
            Map<String, Object> result = budgetAllocationService.assessAllocationRationality(id);
            return MyJsonBean.successData(result,  "合理性评估成功");
        } catch (Exception e) {
            log.error("评估分配合理性失败：{}", e.getMessage(), e);
            return MyJsonBean.error("评估分配合理性失败：" + e.getMessage());
        }
    }

    /**
     * 生成分配建议
     */
    @ApiOperation("生成分配建议")
    @PostMapping("/generate-suggestions")
    public MyJsonBean<Map<String, Object>> generateAllocationSuggestions(
            @ApiParam("组织ID") @RequestParam String organizationId,
            @ApiParam("指标ID") @RequestParam String indicatorId,
            @ApiParam("预算年度") @RequestParam Integer fiscalYear) {
        try {
            Map<String, Object> result = budgetAllocationService.generateAllocationSuggestions(organizationId, indicatorId, fiscalYear);
            return MyJsonBean.successData(result,  "生成分配建议成功");
        } catch (Exception e) {
            log.error("生成分配建议失败：{}", e.getMessage(), e);
            return MyJsonBean.error("生成分配建议失败：" + e.getMessage());
        }
    }

    // ==================== 统计分析 ====================

    /**
     * 获取分配统计信息
     */
    @ApiOperation("获取分配统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetAllocationStatistics() {
        try {
            Map<String, Object> statistics = budgetAllocationService.getBudgetAllocationStatistics();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("获取分配统计信息失败：{}", e.getMessage(), e);
            return MyJsonBean.error("获取分配统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 按分配类型统计数量
     */
    @ApiOperation("按分配类型统计数量")
    @GetMapping("/statistics/by-type")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAllocationCountByType() {
        try {
            List<Map<String, Object>> statistics = budgetAllocationService.getBudgetAllocationCountByType();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("按分配类型统计数量失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按分配类型统计数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计分配金额
     */
    @ApiOperation("统计分配金额")
    @GetMapping("/statistics/amount/{fiscalYear}")
    public MyJsonBean<Map<String, Object>> getBudgetAllocationAmountSummary(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            Map<String, Object> statistics = budgetAllocationService.getBudgetAllocationAmountSummary(fiscalYear);
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("统计分配金额失败：{}", e.getMessage(), e);
            return MyJsonBean.error("统计分配金额失败：" + e.getMessage());
        }
    }

    // ==================== 数据导入导出 ====================

    /**
     * 导出分配数据
     */
    @ApiOperation("导出分配数据")
    @PostMapping("/export")
    public MyJsonBean<String> exportAllocations(@RequestBody List<String> allocationIds) {
        try {
            String filePath = budgetAllocationService.exportAllocations(allocationIds);
            return MyJsonBean.successData(filePath,  "导出成功");
        } catch (Exception e) {
            log.error("导出分配数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("导出分配数据失败：" + e.getMessage());
        }
    }

    /**
     * 导入分配数据
     */
    @ApiOperation("导入分配数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importAllocations(@ApiParam("文件路径") @RequestParam String filePath) {
        try {
            Map<String, Object> result = budgetAllocationService.importAllocations(filePath);
            return MyJsonBean.successData(result,  "导入成功");
        } catch (Exception e) {
            log.error("导入分配数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("导入分配数据失败：" + e.getMessage());
        }
    }
}
