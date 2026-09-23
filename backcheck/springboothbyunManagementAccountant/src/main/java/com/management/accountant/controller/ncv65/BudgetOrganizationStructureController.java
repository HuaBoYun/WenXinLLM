package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.common.Result;
import com.management.accountant.entity.ncv65.BudgetOrganizationStructure;
import com.management.accountant.service.ncv65.IBudgetOrganizationStructureService;
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
 * NCV65全面预算系统 - 预算组织体系控制器
 * 
 * @description 预算组织体系管理API接口，支持5种组织体系类型
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算组织体系管理")
@RestController
@RequestMapping("/budget/organization/structure")
@Validated
public class BudgetOrganizationStructureController {

    @Resource
    private IBudgetOrganizationStructureService budgetOrganizationStructureService;

    /**
     * 创建组织体系
     */
    @ApiOperation("创建组织体系")
    @PostMapping
    public Result<Boolean> createOrganizationStructure(@Valid @RequestBody BudgetOrganizationStructure structure) {
        try {
            boolean result = budgetOrganizationStructureService.createStructure(structure);
            return Result.success(result, "创建组织体系成功");
        } catch (Exception e) {
            log.error("创建组织体系失败：{}", e.getMessage(), e);
            return Result.error("创建组织体系失败：" + e.getMessage());
        }
    }

    /**
     * 更新组织体系
     */
    @ApiOperation("更新组织体系")
    @PutMapping("/{id}")
    public Result<Boolean> updateOrganizationStructure(
            @ApiParam("体系ID") @PathVariable @NotBlank String id,
            @Valid @RequestBody BudgetOrganizationStructure structure) {
        try {
            structure.setId(id);
            boolean result = budgetOrganizationStructureService.updateStructure(structure);
            return Result.success(result, "更新组织体系成功");
        } catch (Exception e) {
            log.error("更新组织体系失败：{}", e.getMessage(), e);
            return Result.error("更新组织体系失败：" + e.getMessage());
        }
    }

    /**
     * 删除组织体系
     */
    @ApiOperation("删除组织体系")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteOrganizationStructure(@ApiParam("体系ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetOrganizationStructureService.deleteStructure(id);
            return Result.success(result, "删除组织体系成功");
        } catch (Exception e) {
            log.error("删除组织体系失败：{}", e.getMessage(), e);
            return Result.error("删除组织体系失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除组织体系
     */
    @ApiOperation("批量删除组织体系")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteOrganizationStructures(@RequestBody @NotEmpty List<String> ids) {
        try {
            boolean result = budgetOrganizationStructureService.batchDeleteStructures(ids);
            return Result.success(result, "批量删除组织体系成功");
        } catch (Exception e) {
            log.error("批量删除组织体系失败：{}", e.getMessage(), e);
            return Result.error("批量删除组织体系失败：" + e.getMessage());
        }
    }

    /**
     * 查询组织体系详情
     */
    @ApiOperation("查询组织体系详情")
    @GetMapping("/{id}")
    public Result<BudgetOrganizationStructure> getOrganizationStructure(@ApiParam("体系ID") @PathVariable @NotBlank String id) {
        try {
            BudgetOrganizationStructure structure = budgetOrganizationStructureService.getStructureById(id);
            return Result.success(structure, "查询组织体系详情成功");
        } catch (Exception e) {
            log.error("查询组织体系详情失败：{}", e.getMessage(), e);
            return Result.error("查询组织体系详情失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询组织体系列表
     */
    @ApiOperation("分页查询组织体系列表")
    @PostMapping("/page")
    public Result<IPage<BudgetOrganizationStructure>> getOrganizationStructurePage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Long size,
            @RequestBody(required = false) Map<String, Object> params) {
        try {
            IPage<BudgetOrganizationStructure> page = budgetOrganizationStructureService.getStructurePage(current, size, params);
            return Result.success(page, "查询组织体系列表成功");
        } catch (Exception e) {
            log.error("查询组织体系列表失败：{}", e.getMessage(), e);
            return Result.error("查询组织体系列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据体系编码查询组织体系
     */
    @ApiOperation("根据体系编码查询组织体系")
    @GetMapping("/code/{structureCode}")
    public Result<BudgetOrganizationStructure> getOrganizationStructureByCode(
            @ApiParam("体系编码") @PathVariable @NotBlank String structureCode) {
        try {
            BudgetOrganizationStructure structure = budgetOrganizationStructureService.getStructureByCode(structureCode);
            return Result.success(structure, "查询组织体系成功");
        } catch (Exception e) {
            log.error("根据编码查询组织体系失败：{}", e.getMessage(), e);
            return Result.error("根据编码查询组织体系失败：" + e.getMessage());
        }
    }

    /**
     * 根据体系类型查询组织体系列表
     */
    @ApiOperation("根据体系类型查询组织体系列表")
    @GetMapping("/type/{structureType}")
    public Result<List<BudgetOrganizationStructure>> getOrganizationStructuresByType(
            @ApiParam("体系类型") @PathVariable @NotBlank String structureType) {
        try {
            List<BudgetOrganizationStructure> structures = budgetOrganizationStructureService.getStructuresByType(structureType);
            return Result.success(structures, "查询组织体系列表成功");
        } catch (Exception e) {
            log.error("根据类型查询组织体系列表失败：{}", e.getMessage(), e);
            return Result.error("根据类型查询组织体系列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询启用的组织体系列表
     */
    @ApiOperation("查询启用的组织体系列表")
    @GetMapping("/enabled")
    public Result<List<BudgetOrganizationStructure>> getEnabledOrganizationStructures() {
        try {
            List<BudgetOrganizationStructure> structures = budgetOrganizationStructureService.getEnabledStructures();
            return Result.success(structures, "查询启用的组织体系列表成功");
        } catch (Exception e) {
            log.error("查询启用的组织体系列表失败：{}", e.getMessage(), e);
            return Result.error("查询启用的组织体系列表失败：" + e.getMessage());
        }
    }

    /**
     * 启用组织体系
     */
    @ApiOperation("启用组织体系")
    @PostMapping("/{id}/enable")
    public Result<Boolean> enableOrganizationStructure(@ApiParam("体系ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetOrganizationStructureService.enableStructure(id);
            return Result.success(result, "启用组织体系成功");
        } catch (Exception e) {
            log.error("启用组织体系失败：{}", e.getMessage(), e);
            return Result.error("启用组织体系失败：" + e.getMessage());
        }
    }

    /**
     * 禁用组织体系
     */
    @ApiOperation("禁用组织体系")
    @PostMapping("/{id}/disable")
    public Result<Boolean> disableOrganizationStructure(@ApiParam("体系ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetOrganizationStructureService.disableStructure(id);
            return Result.success(result, "禁用组织体系成功");
        } catch (Exception e) {
            log.error("禁用组织体系失败：{}", e.getMessage(), e);
            return Result.error("禁用组织体系失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新状态
     */
    @ApiOperation("批量更新状态")
    @PostMapping("/batch/status")
    public Result<Boolean> batchUpdateStatus(
            @RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            String status = (String) params.get("status");
            
            boolean result = budgetOrganizationStructureService.batchUpdateStatus(ids, status);
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
    public Result<Boolean> batchUpdateEnabled(
            @RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            Boolean isEnabled = (Boolean) params.get("isEnabled");
            
            boolean result = budgetOrganizationStructureService.batchUpdateEnabled(ids, isEnabled);
            return Result.success(result, "批量更新启用状态成功");
        } catch (Exception e) {
            log.error("批量更新启用状态失败：{}", e.getMessage(), e);
            return Result.error("批量更新启用状态失败：" + e.getMessage());
        }
    }

    /**
     * 复制组织体系
     */
    @ApiOperation("复制组织体系")
    @PostMapping("/{id}/copy")
    public Result<BudgetOrganizationStructure> copyOrganizationStructure(
            @ApiParam("源体系ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, String> params) {
        try {
            String targetName = params.get("targetName");
            String targetCode = params.get("targetCode");
            
            BudgetOrganizationStructure structure = budgetOrganizationStructureService.copyStructure(id, targetName, targetCode);
            return Result.success(structure, "复制组织体系成功");
        } catch (Exception e) {
            log.error("复制组织体系失败：{}", e.getMessage(), e);
            return Result.error("复制组织体系失败：" + e.getMessage());
        }
    }

    /**
     * 检查体系编码是否存在
     */
    @ApiOperation("检查体系编码是否存在")
    @GetMapping("/check/code")
    public Result<Boolean> checkStructureCodeExists(
            @ApiParam("体系编码") @RequestParam @NotBlank String structureCode,
            @ApiParam("排除的ID") @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = budgetOrganizationStructureService.checkStructureCodeExists(structureCode, excludeId);
            return Result.success(exists, "检查体系编码完成");
        } catch (Exception e) {
            log.error("检查体系编码失败：{}", e.getMessage(), e);
            return Result.error("检查体系编码失败：" + e.getMessage());
        }
    }

    /**
     * 统计各体系类型的数量
     */
    @ApiOperation("统计各体系类型的数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> countByStructureType() {
        try {
            List<Map<String, Object>> statistics = budgetOrganizationStructureService.countByStructureType();
            return Result.success(statistics, "统计体系类型数量成功");
        } catch (Exception e) {
            log.error("统计体系类型数量失败：{}", e.getMessage(), e);
            return Result.error("统计体系类型数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计各状态的数量
     */
    @ApiOperation("统计各状态的数量")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> countByStatus() {
        try {
            List<Map<String, Object>> statistics = budgetOrganizationStructureService.countByStatus();
            return Result.success(statistics, "统计状态数量成功");
        } catch (Exception e) {
            log.error("统计状态数量失败：{}", e.getMessage(), e);
            return Result.error("统计状态数量失败：" + e.getMessage());
        }
    }
}
