package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.common.Result;
import com.management.accountant.entity.ncv65.BudgetDimension;
import com.management.accountant.service.ncv65.IBudgetDimensionService;
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
 * NCV65全面预算系统 - 预算维度控制器
 * 
 * @description 预算维度管理API接口，支持6-20个维度配置
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算维度管理")
@RestController
@RequestMapping("/budget/dimension")
@Validated
public class BudgetDimensionController {

    @Resource
    private IBudgetDimensionService budgetDimensionService;

    /**
     * 创建维度
     */
    @ApiOperation("创建维度")
    @PostMapping
    public Result<Boolean> createDimension(@Valid @RequestBody BudgetDimension dimension) {
        try {
            boolean result = budgetDimensionService.createDimension(dimension);
            return Result.success(result, "创建维度成功");
        } catch (Exception e) {
            log.error("创建维度失败：{}", e.getMessage(), e);
            return Result.error("创建维度失败：" + e.getMessage());
        }
    }

    /**
     * 更新维度
     */
    @ApiOperation("更新维度")
    @PutMapping("/{id}")
    public Result<Boolean> updateDimension(
            @ApiParam("维度ID") @PathVariable @NotBlank String id,
            @Valid @RequestBody BudgetDimension dimension) {
        try {
            dimension.setId(id);
            boolean result = budgetDimensionService.updateDimension(dimension);
            return Result.success(result, "更新维度成功");
        } catch (Exception e) {
            log.error("更新维度失败：{}", e.getMessage(), e);
            return Result.error("更新维度失败：" + e.getMessage());
        }
    }

    /**
     * 删除维度
     */
    @ApiOperation("删除维度")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDimension(@ApiParam("维度ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetDimensionService.deleteDimension(id);
            return Result.success(result, "删除维度成功");
        } catch (Exception e) {
            log.error("删除维度失败：{}", e.getMessage(), e);
            return Result.error("删除维度失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除维度
     */
    @ApiOperation("批量删除维度")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteDimensions(@RequestBody @NotEmpty List<String> ids) {
        try {
            boolean result = budgetDimensionService.batchDeleteDimensions(ids);
            return Result.success(result, "批量删除维度成功");
        } catch (Exception e) {
            log.error("批量删除维度失败：{}", e.getMessage(), e);
            return Result.error("批量删除维度失败：" + e.getMessage());
        }
    }

    /**
     * 查询维度详情
     */
    @ApiOperation("查询维度详情")
    @GetMapping("/{id}")
    public Result<BudgetDimension> getDimension(@ApiParam("维度ID") @PathVariable @NotBlank String id) {
        try {
            BudgetDimension dimension = budgetDimensionService.getDimensionById(id);
            return Result.success(dimension, "查询维度详情成功");
        } catch (Exception e) {
            log.error("查询维度详情失败：{}", e.getMessage(), e);
            return Result.error("查询维度详情失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询维度列表
     */
    @ApiOperation("分页查询维度列表")
    @PostMapping("/page")
    public Result<IPage<BudgetDimension>> getDimensionPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Long size,
            @RequestBody(required = false) Map<String, Object> params) {
        try {
            IPage<BudgetDimension> page = budgetDimensionService.getDimensionPage(current, size, params);
            return Result.success(page, "查询维度列表成功");
        } catch (Exception e) {
            log.error("查询维度列表失败：{}", e.getMessage(), e);
            return Result.error("查询维度列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据维度编码查询维度
     */
    @ApiOperation("根据维度编码查询维度")
    @GetMapping("/code/{dimensionCode}")
    public Result<BudgetDimension> getDimensionByCode(
            @ApiParam("维度编码") @PathVariable @NotBlank String dimensionCode) {
        try {
            BudgetDimension dimension = budgetDimensionService.getDimensionByCode(dimensionCode);
            return Result.success(dimension, "查询维度成功");
        } catch (Exception e) {
            log.error("根据编码查询维度失败：{}", e.getMessage(), e);
            return Result.error("根据编码查询维度失败：" + e.getMessage());
        }
    }

    /**
     * 根据维度类型查询维度列表
     */
    @ApiOperation("根据维度类型查询维度列表")
    @GetMapping("/type/{dimensionType}")
    public Result<List<BudgetDimension>> getDimensionsByType(
            @ApiParam("维度类型") @PathVariable @NotBlank String dimensionType) {
        try {
            List<BudgetDimension> dimensions = budgetDimensionService.getDimensionsByType(dimensionType);
            return Result.success(dimensions, "查询维度列表成功");
        } catch (Exception e) {
            log.error("根据类型查询维度列表失败：{}", e.getMessage(), e);
            return Result.error("根据类型查询维度列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询启用的维度列表
     */
    @ApiOperation("查询启用的维度列表")
    @GetMapping("/enabled")
    public Result<List<BudgetDimension>> getEnabledDimensions() {
        try {
            List<BudgetDimension> dimensions = budgetDimensionService.getEnabledDimensions();
            return Result.success(dimensions, "查询启用的维度列表成功");
        } catch (Exception e) {
            log.error("查询启用的维度列表失败：{}", e.getMessage(), e);
            return Result.error("查询启用的维度列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询必填维度列表
     */
    @ApiOperation("查询必填维度列表")
    @GetMapping("/required")
    public Result<List<BudgetDimension>> getRequiredDimensions() {
        try {
            List<BudgetDimension> dimensions = budgetDimensionService.getRequiredDimensions();
            return Result.success(dimensions, "查询必填维度列表成功");
        } catch (Exception e) {
            log.error("查询必填维度列表失败：{}", e.getMessage(), e);
            return Result.error("查询必填维度列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询系统维度列表
     */
    @ApiOperation("查询系统维度列表")
    @GetMapping("/system")
    public Result<List<BudgetDimension>> getSystemDimensions() {
        try {
            List<BudgetDimension> dimensions = budgetDimensionService.getSystemDimensions();
            return Result.success(dimensions, "查询系统维度列表成功");
        } catch (Exception e) {
            log.error("查询系统维度列表失败：{}", e.getMessage(), e);
            return Result.error("查询系统维度列表失败：" + e.getMessage());
        }
    }

    /**
     * 启用维度
     */
    @ApiOperation("启用维度")
    @PostMapping("/{id}/enable")
    public Result<Boolean> enableDimension(@ApiParam("维度ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetDimensionService.enableDimension(id);
            return Result.success(result, "启用维度成功");
        } catch (Exception e) {
            log.error("启用维度失败：{}", e.getMessage(), e);
            return Result.error("启用维度失败：" + e.getMessage());
        }
    }

    /**
     * 禁用维度
     */
    @ApiOperation("禁用维度")
    @PostMapping("/{id}/disable")
    public Result<Boolean> disableDimension(@ApiParam("维度ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetDimensionService.disableDimension(id);
            return Result.success(result, "禁用维度成功");
        } catch (Exception e) {
            log.error("禁用维度失败：{}", e.getMessage(), e);
            return Result.error("禁用维度失败：" + e.getMessage());
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
            
            boolean result = budgetDimensionService.batchUpdateStatus(ids, status);
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
            
            boolean result = budgetDimensionService.batchUpdateEnabled(ids, isEnabled);
            return Result.success(result, "批量更新启用状态成功");
        } catch (Exception e) {
            log.error("批量更新启用状态失败：{}", e.getMessage(), e);
            return Result.error("批量更新启用状态失败：" + e.getMessage());
        }
    }

    /**
     * 复制维度
     */
    @ApiOperation("复制维度")
    @PostMapping("/{id}/copy")
    public Result<BudgetDimension> copyDimension(
            @ApiParam("源维度ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, String> params) {
        try {
            String targetName = params.get("targetName");
            String targetCode = params.get("targetCode");
            
            BudgetDimension dimension = budgetDimensionService.copyDimension(id, targetName, targetCode);
            return Result.success(dimension, "复制维度成功");
        } catch (Exception e) {
            log.error("复制维度失败：{}", e.getMessage(), e);
            return Result.error("复制维度失败：" + e.getMessage());
        }
    }

    /**
     * 检查维度编码是否存在
     */
    @ApiOperation("检查维度编码是否存在")
    @GetMapping("/check/code")
    public Result<Boolean> checkDimensionCodeExists(
            @ApiParam("维度编码") @RequestParam @NotBlank String dimensionCode,
            @ApiParam("排除的ID") @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = budgetDimensionService.checkDimensionCodeExists(dimensionCode, excludeId);
            return Result.success(exists, "检查维度编码完成");
        } catch (Exception e) {
            log.error("检查维度编码失败：{}", e.getMessage(), e);
            return Result.error("检查维度编码失败：" + e.getMessage());
        }
    }

    /**
     * 获取维度树结构
     */
    @ApiOperation("获取维度树结构")
    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> getDimensionTree(
            @ApiParam("父维度ID") @RequestParam(required = false) String parentId) {
        try {
            List<Map<String, Object>> tree = budgetDimensionService.getDimensionTree(parentId);
            return Result.success(tree, "获取维度树结构成功");
        } catch (Exception e) {
            log.error("获取维度树结构失败：{}", e.getMessage(), e);
            return Result.error("获取维度树结构失败：" + e.getMessage());
        }
    }

    /**
     * 获取完整维度树结构
     */
    @ApiOperation("获取完整维度树结构")
    @GetMapping("/tree/full")
    public Result<List<Map<String, Object>>> getFullDimensionTree() {
        try {
            List<Map<String, Object>> tree = budgetDimensionService.getFullDimensionTree();
            return Result.success(tree, "获取完整维度树结构成功");
        } catch (Exception e) {
            log.error("获取完整维度树结构失败：{}", e.getMessage(), e);
            return Result.error("获取完整维度树结构失败：" + e.getMessage());
        }
    }

    /**
     * 统计各维度类型的数量
     */
    @ApiOperation("统计各维度类型的数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> countByDimensionType() {
        try {
            List<Map<String, Object>> statistics = budgetDimensionService.countByDimensionType();
            return Result.success(statistics, "统计维度类型数量成功");
        } catch (Exception e) {
            log.error("统计维度类型数量失败：{}", e.getMessage(), e);
            return Result.error("统计维度类型数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计各状态的数量
     */
    @ApiOperation("统计各状态的数量")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> countByStatus() {
        try {
            List<Map<String, Object>> statistics = budgetDimensionService.countByStatus();
            return Result.success(statistics, "统计状态数量成功");
        } catch (Exception e) {
            log.error("统计状态数量失败：{}", e.getMessage(), e);
            return Result.error("统计状态数量失败：" + e.getMessage());
        }
    }
}
