package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.common.Result;
import com.management.accountant.entity.ncv65.BudgetIndicator;
import com.management.accountant.service.ncv65.IBudgetIndicatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算指标控制器
 * 
 * @description 预算指标管理API接口，支持8种指标类型管理
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算指标管理")
@RestController
@RequestMapping("/budget/indicator")
@Validated
public class BudgetIndicatorController {

    @Resource
    private IBudgetIndicatorService budgetIndicatorService;

    /**
     * 创建指标
     */
    @ApiOperation("创建指标")
    @PostMapping
    public Result<Boolean> createIndicator(@Valid @RequestBody BudgetIndicator indicator) {
        try {
            boolean result = budgetIndicatorService.createIndicator(indicator);
            return Result.success(result, "创建指标成功");
        } catch (Exception e) {
            log.error("创建指标失败：{}", e.getMessage(), e);
            return Result.error("创建指标失败：" + e.getMessage());
        }
    }

    /**
     * 更新指标
     */
    @ApiOperation("更新指标")
    @PutMapping("/{id}")
    public Result<Boolean> updateIndicator(
            @ApiParam("指标ID") @PathVariable @NotBlank String id,
            @Valid @RequestBody BudgetIndicator indicator) {
        try {
            indicator.setId(id);
            boolean result = budgetIndicatorService.updateIndicator(indicator);
            return Result.success(result, "更新指标成功");
        } catch (Exception e) {
            log.error("更新指标失败：{}", e.getMessage(), e);
            return Result.error("更新指标失败：" + e.getMessage());
        }
    }

    /**
     * 删除指标
     */
    @ApiOperation("删除指标")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteIndicator(@ApiParam("指标ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetIndicatorService.deleteIndicator(id);
            return Result.success(result, "删除指标成功");
        } catch (Exception e) {
            log.error("删除指标失败：{}", e.getMessage(), e);
            return Result.error("删除指标失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除指标
     */
    @ApiOperation("批量删除指标")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteIndicators(@RequestBody @NotEmpty List<String> ids) {
        try {
            boolean result = budgetIndicatorService.batchDeleteIndicators(ids);
            return Result.success(result, "批量删除指标成功");
        } catch (Exception e) {
            log.error("批量删除指标失败：{}", e.getMessage(), e);
            return Result.error("批量删除指标失败：" + e.getMessage());
        }
    }

    /**
     * 查询指标详情
     */
    @ApiOperation("查询指标详情")
    @GetMapping("/{id}")
    public Result<BudgetIndicator> getIndicator(@ApiParam("指标ID") @PathVariable @NotBlank String id) {
        try {
            BudgetIndicator indicator = budgetIndicatorService.getIndicatorById(id);
            return Result.success(indicator, "查询指标详情成功");
        } catch (Exception e) {
            log.error("查询指标详情失败：{}", e.getMessage(), e);
            return Result.error("查询指标详情失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询指标列表
     */
    @ApiOperation("分页查询指标列表")
    @PostMapping("/page")
    public Result<IPage<BudgetIndicator>> getIndicatorPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Long size,
            @RequestBody(required = false) Map<String, Object> params) {
        try {
            IPage<BudgetIndicator> page = budgetIndicatorService.getIndicatorPage(current, size, params);
            return Result.success(page, "查询指标列表成功");
        } catch (Exception e) {
            log.error("查询指标列表失败：{}", e.getMessage(), e);
            return Result.error("查询指标列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据指标编码查询指标
     */
    @ApiOperation("根据指标编码查询指标")
    @GetMapping("/code/{indicatorCode}")
    public Result<BudgetIndicator> getIndicatorByCode(
            @ApiParam("指标编码") @PathVariable @NotBlank String indicatorCode) {
        try {
            BudgetIndicator indicator = budgetIndicatorService.getIndicatorByCode(indicatorCode);
            return Result.success(indicator, "查询指标成功");
        } catch (Exception e) {
            log.error("根据编码查询指标失败：{}", e.getMessage(), e);
            return Result.error("根据编码查询指标失败：" + e.getMessage());
        }
    }

    /**
     * 根据指标类型查询指标列表
     */
    @ApiOperation("根据指标类型查询指标列表")
    @GetMapping("/type/{indicatorType}")
    public Result<List<BudgetIndicator>> getIndicatorsByType(
            @ApiParam("指标类型") @PathVariable @NotBlank String indicatorType) {
        try {
            List<BudgetIndicator> indicators = budgetIndicatorService.getIndicatorsByType(indicatorType);
            return Result.success(indicators, "查询指标列表成功");
        } catch (Exception e) {
            log.error("根据类型查询指标列表失败：{}", e.getMessage(), e);
            return Result.error("根据类型查询指标列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询启用的指标列表
     */
    @ApiOperation("查询启用的指标列表")
    @GetMapping("/enabled")
    public Result<List<BudgetIndicator>> getEnabledIndicators() {
        try {
            List<BudgetIndicator> indicators = budgetIndicatorService.getEnabledIndicators();
            return Result.success(indicators, "查询启用的指标列表成功");
        } catch (Exception e) {
            log.error("查询启用的指标列表失败：{}", e.getMessage(), e);
            return Result.error("查询启用的指标列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询必填指标列表
     */
    @ApiOperation("查询必填指标列表")
    @GetMapping("/required")
    public Result<List<BudgetIndicator>> getRequiredIndicators() {
        try {
            List<BudgetIndicator> indicators = budgetIndicatorService.getRequiredIndicators();
            return Result.success(indicators, "查询必填指标列表成功");
        } catch (Exception e) {
            log.error("查询必填指标列表失败：{}", e.getMessage(), e);
            return Result.error("查询必填指标列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询系统指标列表
     */
    @ApiOperation("查询系统指标列表")
    @GetMapping("/system")
    public Result<List<BudgetIndicator>> getSystemIndicators() {
        try {
            List<BudgetIndicator> indicators = budgetIndicatorService.getSystemIndicators();
            return Result.success(indicators, "查询系统指标列表成功");
        } catch (Exception e) {
            log.error("查询系统指标列表失败：{}", e.getMessage(), e);
            return Result.error("查询系统指标列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询可计算指标列表
     */
    @ApiOperation("查询可计算指标列表")
    @GetMapping("/calculated")
    public Result<List<BudgetIndicator>> getCalculatedIndicators() {
        try {
            List<BudgetIndicator> indicators = budgetIndicatorService.getCalculatedIndicators();
            return Result.success(indicators, "查询可计算指标列表成功");
        } catch (Exception e) {
            log.error("查询可计算指标列表失败：{}", e.getMessage(), e);
            return Result.error("查询可计算指标列表失败：" + e.getMessage());
        }
    }

    /**
     * 启用指标
     */
    @ApiOperation("启用指标")
    @PostMapping("/{id}/enable")
    public Result<Boolean> enableIndicator(@ApiParam("指标ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetIndicatorService.enableIndicator(id);
            return Result.success(result, "启用指标成功");
        } catch (Exception e) {
            log.error("启用指标失败：{}", e.getMessage(), e);
            return Result.error("启用指标失败：" + e.getMessage());
        }
    }

    /**
     * 禁用指标
     */
    @ApiOperation("禁用指标")
    @PostMapping("/{id}/disable")
    public Result<Boolean> disableIndicator(@ApiParam("指标ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetIndicatorService.disableIndicator(id);
            return Result.success(result, "禁用指标成功");
        } catch (Exception e) {
            log.error("禁用指标失败：{}", e.getMessage(), e);
            return Result.error("禁用指标失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新状态
     */
    @ApiOperation("批量更新状态")
    @PostMapping("/batch/status")
    public Result<Boolean> batchUpdateStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            String status = (String) params.get("status");
            
            boolean result = budgetIndicatorService.batchUpdateStatus(ids, status);
            return Result.success(result, "批量更新状态成功");
        } catch (Exception e) {
            log.error("批量更新状态失败：{}", e.getMessage(), e);
            return Result.error("批量更新状态失败：" + e.getMessage());
        }
    }

    /**
     * 批量启用/禁用
     */
    @ApiOperation("批量启用/禁用")
    @PostMapping("/batch/enabled")
    public Result<Boolean> batchUpdateEnabled(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            Boolean isEnabled = (Boolean) params.get("isEnabled");
            
            boolean result = budgetIndicatorService.batchUpdateEnabled(ids, isEnabled);
            return Result.success(result, "批量更新启用状态成功");
        } catch (Exception e) {
            log.error("批量更新启用状态失败：{}", e.getMessage(), e);
            return Result.error("批量更新启用状态失败：" + e.getMessage());
        }
    }

    /**
     * 复制指标
     */
    @ApiOperation("复制指标")
    @PostMapping("/{id}/copy")
    public Result<BudgetIndicator> copyIndicator(
            @ApiParam("源指标ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, String> params) {
        try {
            String targetName = params.get("targetName");
            String targetCode = params.get("targetCode");
            
            BudgetIndicator indicator = budgetIndicatorService.copyIndicator(id, targetName, targetCode);
            return Result.success(indicator, "复制指标成功");
        } catch (Exception e) {
            log.error("复制指标失败：{}", e.getMessage(), e);
            return Result.error("复制指标失败：" + e.getMessage());
        }
    }

    /**
     * 检查指标编码是否存在
     */
    @ApiOperation("检查指标编码是否存在")
    @GetMapping("/check/code")
    public Result<Boolean> checkIndicatorCodeExists(
            @ApiParam("指标编码") @RequestParam @NotBlank String indicatorCode,
            @ApiParam("排除的ID") @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = budgetIndicatorService.checkIndicatorCodeExists(indicatorCode, excludeId);
            return Result.success(exists, "检查指标编码完成");
        } catch (Exception e) {
            log.error("检查指标编码失败：{}", e.getMessage(), e);
            return Result.error("检查指标编码失败：" + e.getMessage());
        }
    }

    /**
     * 获取指标树结构
     */
    @ApiOperation("获取指标树结构")
    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> getIndicatorTree(
            @ApiParam("父指标ID") @RequestParam(required = false) String parentId) {
        try {
            List<Map<String, Object>> tree = budgetIndicatorService.getIndicatorTree(parentId);
            return Result.success(tree, "获取指标树结构成功");
        } catch (Exception e) {
            log.error("获取指标树结构失败：{}", e.getMessage(), e);
            return Result.error("获取指标树结构失败：" + e.getMessage());
        }
    }

    /**
     * 获取完整指标树结构
     */
    @ApiOperation("获取完整指标树结构")
    @GetMapping("/tree/full")
    public Result<List<Map<String, Object>>> getFullIndicatorTree() {
        try {
            List<Map<String, Object>> tree = budgetIndicatorService.getFullIndicatorTree();
            return Result.success(tree, "获取完整指标树结构成功");
        } catch (Exception e) {
            log.error("获取完整指标树结构失败：{}", e.getMessage(), e);
            return Result.error("获取完整指标树结构失败：" + e.getMessage());
        }
    }

    /**
     * 移动指标
     */
    @ApiOperation("移动指标")
    @PostMapping("/{id}/move")
    public Result<Boolean> moveIndicator(
            @ApiParam("指标ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, String> params) {
        try {
            String newParentId = params.get("newParentId");
            boolean result = budgetIndicatorService.moveIndicator(id, newParentId);
            return Result.success(result, "移动指标成功");
        } catch (Exception e) {
            log.error("移动指标失败：{}", e.getMessage(), e);
            return Result.error("移动指标失败：" + e.getMessage());
        }
    }

    /**
     * 调整指标排序
     */
    @ApiOperation("调整指标排序")
    @PostMapping("/{id}/sort")
    public Result<Boolean> adjustIndicatorSort(
            @ApiParam("指标ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, Integer> params) {
        try {
            Integer sortOrder = params.get("sortOrder");
            boolean result = budgetIndicatorService.adjustIndicatorSort(id, sortOrder);
            return Result.success(result, "调整指标排序成功");
        } catch (Exception e) {
            log.error("调整指标排序失败：{}", e.getMessage(), e);
            return Result.error("调整指标排序失败：" + e.getMessage());
        }
    }

    /**
     * 验证指标公式
     */
    @ApiOperation("验证指标公式")
    @PostMapping("/validate/formula")
    public Result<Boolean> validateFormula(@RequestBody Map<String, String> params) {
        try {
            String formula = params.get("formula");
            boolean valid = budgetIndicatorService.validateFormula(formula);
            return Result.success(valid, valid ? "公式验证通过" : "公式格式错误");
        } catch (Exception e) {
            log.error("验证指标公式失败：{}", e.getMessage(), e);
            return Result.error("验证指标公式失败：" + e.getMessage());
        }
    }

    /**
     * 计算指标值
     */
    @ApiOperation("计算指标值")
    @PostMapping("/{id}/calculate")
    public Result<Map<String, Object>> calculateIndicatorValue(
            @ApiParam("指标ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = budgetIndicatorService.calculateIndicatorValue(id, params);
            return Result.success(result, "计算指标值成功");
        } catch (Exception e) {
            log.error("计算指标值失败：{}", e.getMessage(), e);
            return Result.error("计算指标值失败：" + e.getMessage());
        }
    }

    /**
     * 统计各指标类型的数量
     */
    @ApiOperation("统计各指标类型的数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> countByIndicatorType() {
        try {
            List<Map<String, Object>> statistics = budgetIndicatorService.countByIndicatorType();
            return Result.success(statistics, "统计指标类型数量成功");
        } catch (Exception e) {
            log.error("统计指标类型数量失败：{}", e.getMessage(), e);
            return Result.error("统计指标类型数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计各数据类型的数量
     */
    @ApiOperation("统计各数据类型的数量")
    @GetMapping("/statistics/data-type")
    public Result<List<Map<String, Object>>> countByDataType() {
        try {
            List<Map<String, Object>> statistics = budgetIndicatorService.countByDataType();
            return Result.success(statistics, "统计数据类型数量成功");
        } catch (Exception e) {
            log.error("统计数据类型数量失败：{}", e.getMessage(), e);
            return Result.error("统计数据类型数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计各状态的数量
     */
    @ApiOperation("统计各状态的数量")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> countByStatus() {
        try {
            List<Map<String, Object>> statistics = budgetIndicatorService.countByStatus();
            return Result.success(statistics, "统计状态数量成功");
        } catch (Exception e) {
            log.error("统计状态数量失败：{}", e.getMessage(), e);
            return Result.error("统计状态数量失败：" + e.getMessage());
        }
    }

    /**
     * 获取指标层级路径
     */
    @ApiOperation("获取指标层级路径")
    @GetMapping("/{id}/path")
    public Result<String> getIndicatorPath(@ApiParam("指标ID") @PathVariable @NotBlank String id) {
        try {
            String path = budgetIndicatorService.getIndicatorPath(id);
            return Result.success(path, "获取指标层级路径成功");
        } catch (Exception e) {
            log.error("获取指标层级路径失败：{}", e.getMessage(), e);
            return Result.error("获取指标层级路径失败：" + e.getMessage());
        }
    }
}
