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
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.MaterialRequirement;
import com.huabo.contract.service.MaterialRequirementService;
import com.huabo.contract.vo.MaterialRequirementQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 材料需求控制器
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Tag(name="材料需求管理",description="材料需求管理")
@RestController
@RequestMapping("/planning/material")
@RequiredArgsConstructor
@Slf4j
public class MaterialRequirementController {

    private final MaterialRequirementService materialRequirementService;

    @Operation(summary = "分页查询材料需求列表")
    @PostMapping("/list")
    public String getMaterialRequirementList(@RequestBody MaterialRequirementQueryParam queryParam) {
        try {
            log.info("分页查询材料需求列表，参数：{}", queryParam);
            IPage<MaterialRequirement> page = materialRequirementService.getMaterialRequirementPage(queryParam);
            return JsonBean.success("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "分页查询材料需求列表")
    @PostMapping("/page")
    public String getMaterialRequirementPage(@RequestBody MaterialRequirementQueryParam queryParam) {
        try {
            log.info("分页查询材料需求列表，参数：{}", queryParam);
            IPage<MaterialRequirement> page = materialRequirementService.getMaterialRequirementPage(queryParam);
            return JsonBean.success("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询材料需求详情")
    @GetMapping("/{id}")
    public String getMaterialRequirementById(@Parameter(description="材料需求ID") @PathVariable Long id) {
        try {
            MaterialRequirement materialRequirement = materialRequirementService.getById(id);
            return JsonBean.success("操作成功", materialRequirement);
        } catch (Exception e) {
            log.error("查询材料需求详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增材料需求")
    @PostMapping("/create")
    public String saveMaterialRequirement(@Valid @RequestBody MaterialRequirement materialRequirement) {
        try {
            // 验证材料需求信息
            if (!materialRequirementService.validateMaterialInfo(materialRequirement)) {
                return JsonBean.error("材料需求信息验证失败");
            }

            boolean result = materialRequirementService.save(materialRequirement);
            return result ? JsonBean.success("新增成功") : JsonBean.error("新增失败");
        } catch (Exception e) {
            log.error("新增材料需求失败", e);
            return JsonBean.error("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "修改材料需求")
    @PutMapping
    public String updateMaterialRequirement(@Valid @RequestBody MaterialRequirement materialRequirement) {
        try {
            // 验证材料需求信息
            if (!materialRequirementService.validateMaterialInfo(materialRequirement)) {
                return JsonBean.error("材料需求信息验证失败");
            }

            boolean result = materialRequirementService.updateById(materialRequirement);
            return result ? JsonBean.success("修改成功") : JsonBean.error("修改失败");
        } catch (Exception e) {
            log.error("修改材料需求失败", e);
            return JsonBean.error("修改失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除材料需求")
    @DeleteMapping("/{id}")
    public String deleteMaterialRequirement(@Parameter(description="材料需求ID") @PathVariable Long id) {
        try {
            boolean result = materialRequirementService.removeById(id);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除材料需求失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除材料需求")
    @DeleteMapping("/batch")
    public String batchDeleteMaterialRequirement(@RequestBody List<Long> ids) {
        try {
            boolean result = materialRequirementService.removeByIds(ids);
            return result ? JsonBean.success("批量删除成功") : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除材料需求失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据项目策划ID查询材料需求列表")
    @GetMapping("/planning/{planningId}")
    public String getByPlanningId(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getByPlanningId(planningId);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据项目策划ID查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据材料类型查询材料需求列表")
    @GetMapping("/type/{materialType}")
    public String getByMaterialType(@Parameter(description="材料类型") @PathVariable Integer materialType) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getByMaterialType(materialType);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据材料类型查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据采购状态查询材料需求列表")
    @GetMapping("/procurement-status/{procurementStatus}")
    public String getByProcurementStatus(@Parameter(description="采购状态") @PathVariable Integer procurementStatus) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getByProcurementStatus(procurementStatus);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据采购状态查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据优先级查询材料需求列表")
    @GetMapping("/priority/{priority}")
    public String getByPriority(@Parameter(description="优先级") @PathVariable Integer priority) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getByPriority(priority);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据优先级查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据供应商ID查询材料需求列表")
    @GetMapping("/supplier/{supplierId}")
    public String getBySupplierId(@Parameter(description="供应商ID") @PathVariable Long supplierId) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getBySupplierId(supplierId);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据供应商ID查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据使用地点查询材料需求列表")
    @GetMapping("/location/{usageLocation}")
    public String getByUsageLocation(@Parameter(description="使用地点") @PathVariable String usageLocation) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getByUsageLocation(usageLocation);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据使用地点查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据采购人员查询材料需求列表")
    @GetMapping("/purchaser/{purchaser}")
    public String getByPurchaser(@Parameter(description="采购人员") @PathVariable String purchaser) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getByPurchaser(purchaser);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据采购人员查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据验收人员查询材料需求列表")
    @GetMapping("/inspector/{inspector}")
    public String getByInspector(@Parameter(description="验收人员") @PathVariable String inspector) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getByInspector(inspector);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据验收人员查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据项目策划ID统计预算成本总额")
    @GetMapping("/sum-budgeted-cost/planning/{planningId}")
    public String sumBudgetedCostByPlanning(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            BigDecimal cost = materialRequirementService.sumBudgetedCostByPlanning(planningId);
            return JsonBean.success("操作成功", cost);
        } catch (Exception e) {
            log.error("统计预算成本总额失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据项目策划ID统计实际成本总额")
    @GetMapping("/sum-actual-cost/planning/{planningId}")
    public String sumActualCostByPlanning(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            BigDecimal cost = materialRequirementService.sumActualCostByPlanning(planningId);
            return JsonBean.success("操作成功", cost);
        } catch (Exception e) {
            log.error("统计实际成本总额失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据材料类型统计需求数量")
    @GetMapping("/sum-required-quantity/type/{materialType}")
    public String sumRequiredQuantityByType(@Parameter(description="材料类型") @PathVariable Integer materialType) {
        try {
            BigDecimal quantity = materialRequirementService.sumRequiredQuantityByType(materialType);
            return JsonBean.success("操作成功", quantity);
        } catch (Exception e) {
            log.error("统计需求数量失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据材料类型统计已采购数量")
    @GetMapping("/sum-purchased-quantity/type/{materialType}")
    public String sumPurchasedQuantityByType(@Parameter(description="材料类型") @PathVariable Integer materialType) {
        try {
            BigDecimal quantity = materialRequirementService.sumPurchasedQuantityByType(materialType);
            return JsonBean.success("操作成功", quantity);
        } catch (Exception e) {
            log.error("统计已采购数量失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据采购状态统计数量")
    @GetMapping("/count/procurement-status/{procurementStatus}")
    public String countByProcurementStatus(@Parameter(description="采购状态") @PathVariable Integer procurementStatus) {
        try {
            Integer count = materialRequirementService.countByProcurementStatus(procurementStatus);
            return JsonBean.success("操作成功", count);
        } catch (Exception e) {
            log.error("根据采购状态统计数量失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询超预算的材料需求列表")
    @GetMapping("/over-budget")
    public String getOverBudget() {
        try {
            List<MaterialRequirement> list = materialRequirementService.getOverBudget();
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("查询超预算的材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询紧急需求的材料列表")
    @GetMapping("/urgent")
    public String getUrgentRequirements() {
        try {
            List<MaterialRequirement> list = materialRequirementService.getUrgentRequirements();
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("查询紧急需求的材料列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询采购不足的材料需求列表")
    @GetMapping("/under-procured")
    public String getUnderProcured() {
        try {
            List<MaterialRequirement> list = materialRequirementService.getUnderProcured();
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("查询采购不足的材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量更新采购状态")
    @PutMapping("/batch-status")
    public String batchUpdateStatus(@RequestBody List<Long> ids,
                                           @Parameter(description="新状态") @RequestParam Integer procurementStatus,
                                           @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Integer count = materialRequirementService.batchUpdateStatus(ids, procurementStatus, updateBy);
            return JsonBean.success(count, "批量更新成功，共更新" + count + "条记录");
        } catch (Exception e) {
            log.error("批量更新采购状态失败", e);
            return JsonBean.error("批量更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据品牌查询材料需求列表")
    @GetMapping("/brand/{brand}")
    public String getByBrand(@Parameter(description="品牌") @PathVariable String brand) {
        try {
            List<MaterialRequirement> list = materialRequirementService.getByBrand(brand);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据品牌查询材料需求列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "材料采购")
    @PutMapping("/procure/{id}")
    public String procureMaterial(@Parameter(description="材料需求ID") @PathVariable Long id,
                                         @Parameter(description="采购数量") @RequestParam BigDecimal purchasedQuantity,
                                         @Parameter(description="实际单价") @RequestParam BigDecimal actualUnitPrice,
                                         @Parameter(description="供应商ID") @RequestParam Long supplierId,
                                         @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Boolean result = materialRequirementService.procureMaterial(id, purchasedQuantity, actualUnitPrice, supplierId, updateBy);
            return result ? JsonBean.success("材料采购成功") : JsonBean.error("材料采购失败");
        } catch (Exception e) {
            log.error("材料采购失败", e);
            return JsonBean.error("材料采购失败：" + e.getMessage());
        }
    }

    @Operation(summary = "材料验收")
    @PutMapping("/inspect/{id}")
    public String inspectMaterial(@Parameter(description="材料需求ID") @PathVariable Long id,
                                         @Parameter(description="验收人员") @RequestParam String inspector,
                                         @Parameter(description="验收结果") @RequestParam String inspectionResult,
                                         @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Boolean result = materialRequirementService.inspectMaterial(id, inspector, inspectionResult, updateBy);
            return result ? JsonBean.success("材料验收成功") : JsonBean.error("材料验收失败");
        } catch (Exception e) {
            log.error("材料验收失败", e);
            return JsonBean.error("材料验收失败：" + e.getMessage());
        }
    }

    @Operation(summary = "材料入库")
    @PutMapping("/store/{id}")
    public String storeMaterial(@Parameter(description="材料需求ID") @PathVariable Long id,
                                       @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Boolean result = materialRequirementService.storeMaterial(id, updateBy);
            return result ? JsonBean.success("材料入库成功") : JsonBean.error("材料入库失败");
        } catch (Exception e) {
            log.error("材料入库失败", e);
            return JsonBean.error("材料入库失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询材料需求统计信息")
    @GetMapping("/statistics")
    public String getMaterialStatistics() {
        try {
            List<MaterialRequirement> statistics = materialRequirementService.getMaterialStatistics();
            return JsonBean.success("操作成功", statistics);
        } catch (Exception e) {
            log.error("查询材料需求统计信息失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "材料需求优化建议")
    @GetMapping("/optimization-suggestions/{planningId}")
    public String getOptimizationSuggestions(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            List<String> suggestions = materialRequirementService.getOptimizationSuggestions(planningId);
            return JsonBean.success("操作成功", suggestions);
        } catch (Exception e) {
            log.error("获取材料需求优化建议失败", e);
            return JsonBean.error("获取建议失败：" + e.getMessage());
        }
    }
}
