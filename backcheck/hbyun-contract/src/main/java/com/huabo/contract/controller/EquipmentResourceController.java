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
import com.huabo.contract.entity.EquipmentResource;
import com.huabo.contract.service.EquipmentResourceService;
import com.huabo.contract.vo.EquipmentResourceQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 设备资源控制器
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Tag(name="设备资源管理",description="设备资源管理")
@RestController
@RequestMapping("/planning/equipment")
@RequiredArgsConstructor
@Slf4j
public class EquipmentResourceController {

    private final EquipmentResourceService equipmentResourceService;

    @Operation(summary = "分页查询设备资源列表")
    @PostMapping("/list")
    public String getEquipmentResourceList(@RequestBody EquipmentResourceQueryParam queryParam) {
        try {
            log.info("分页查询设备资源列表，参数：{}", queryParam);
            IPage<EquipmentResource> page = equipmentResourceService.getEquipmentResourcePage(queryParam);
            return JsonBean.success("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "分页查询设备资源列表")
    @PostMapping("/page")
    public String getEquipmentResourcePage(@RequestBody EquipmentResourceQueryParam queryParam) {
        try {
            log.info("分页查询设备资源列表，参数：{}", queryParam);
            IPage<EquipmentResource> page = equipmentResourceService.getEquipmentResourcePage(queryParam);
            return JsonBean.success("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询设备资源详情")
    @GetMapping("/{id}")
    public String getEquipmentResourceById(@Parameter(description="设备资源ID") @PathVariable Long id) {
        try {
            EquipmentResource equipmentResource = equipmentResourceService.getById(id);
            return JsonBean.success("操作成功", equipmentResource);
        } catch (Exception e) {
            log.error("查询设备资源详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增设备资源")
    @PostMapping("/create")
    public String saveEquipmentResource(@Valid @RequestBody EquipmentResource equipmentResource) {
        try {
            // 验证设备资源信息
            if (!equipmentResourceService.validateEquipmentInfo(equipmentResource)) {
                return JsonBean.error("设备资源信息验证失败");
            }

            boolean result = equipmentResourceService.save(equipmentResource);
            return result ? JsonBean.success("新增成功") : JsonBean.error("新增失败");
        } catch (Exception e) {
            log.error("新增设备资源失败", e);
            return JsonBean.error("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "修改设备资源")
    @PutMapping
    public String updateEquipmentResource(@Valid @RequestBody EquipmentResource equipmentResource) {
        try {
            // 验证设备资源信息
            if (!equipmentResourceService.validateEquipmentInfo(equipmentResource)) {
                return JsonBean.error("设备资源信息验证失败");
            }

            boolean result = equipmentResourceService.updateById(equipmentResource);
            return result ? JsonBean.success("修改成功") : JsonBean.error("修改失败");
        } catch (Exception e) {
            log.error("修改设备资源失败", e);
            return JsonBean.error("修改失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除设备资源")
    @DeleteMapping("/{id}")
    public String deleteEquipmentResource(@Parameter(description="设备资源ID") @PathVariable Long id) {
        try {
            boolean result = equipmentResourceService.removeById(id);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除设备资源失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除设备资源")
    @DeleteMapping("/batch")
    public String batchDeleteEquipmentResource(@RequestBody List<Long> ids) {
        try {
            boolean result = equipmentResourceService.removeByIds(ids);
            return result ? JsonBean.success("批量删除成功") : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除设备资源失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据项目策划ID查询设备资源列表")
    @GetMapping("/planning/{planningId}")
    public String getByPlanningId(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            List<EquipmentResource> list = equipmentResourceService.getByPlanningId(planningId);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据项目策划ID查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据设备类型查询设备资源列表")
    @GetMapping("/type/{equipmentType}")
    public String getByEquipmentType(@Parameter(description="设备类型") @PathVariable Integer equipmentType) {
        try {
            List<EquipmentResource> list = equipmentResourceService.getByEquipmentType(equipmentType);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据设备类型查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据分配状态查询设备资源列表")
    @GetMapping("/allocation-status/{allocationStatus}")
    public String getByAllocationStatus(@Parameter(description="分配状态") @PathVariable Integer allocationStatus) {
        try {
            List<EquipmentResource> list = equipmentResourceService.getByAllocationStatus(allocationStatus);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据分配状态查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据设备状态查询设备资源列表")
    @GetMapping("/equipment-status/{equipmentStatus}")
    public String getByEquipmentStatus(@Parameter(description="设备状态") @PathVariable Integer equipmentStatus) {
        try {
            List<EquipmentResource> list = equipmentResourceService.getByEquipmentStatus(equipmentStatus);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据设备状态查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据租赁方式查询设备资源列表")
    @GetMapping("/lease-type/{leaseType}")
    public String getByLeaseType(@Parameter(description="租赁方式") @PathVariable Integer leaseType) {
        try {
            List<EquipmentResource> list = equipmentResourceService.getByLeaseType(leaseType);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据租赁方式查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据供应商ID查询设备资源列表")
    @GetMapping("/supplier/{supplierId}")
    public String getBySupplierId(@Parameter(description="供应商ID") @PathVariable Long supplierId) {
        try {
            List<EquipmentResource> list = equipmentResourceService.getBySupplierId(supplierId);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据供应商ID查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据使用地点查询设备资源列表")
    @GetMapping("/location/{usageLocation}")
    public String getByUsageLocation(@Parameter(description="使用地点") @PathVariable String usageLocation) {
        try {
            List<EquipmentResource> list = equipmentResourceService.getByUsageLocation(usageLocation);
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("根据使用地点查询设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据项目策划ID统计预算成本总额")
    @GetMapping("/sum-budgeted-cost/planning/{planningId}")
    public String sumBudgetedCostByPlanning(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            BigDecimal cost = equipmentResourceService.sumBudgetedCostByPlanning(planningId);
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
            BigDecimal cost = equipmentResourceService.sumActualCostByPlanning(planningId);
            return JsonBean.success("操作成功", cost);
        } catch (Exception e) {
            log.error("统计实际成本总额失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据设备类型统计需求数量")
    @GetMapping("/sum-required-quantity/type/{equipmentType}")
    public String sumRequiredQuantityByType(@Parameter(description="设备类型") @PathVariable Integer equipmentType) {
        try {
            Integer quantity = equipmentResourceService.sumRequiredQuantityByType(equipmentType);
            return JsonBean.success("操作成功", quantity);
        } catch (Exception e) {
            log.error("统计需求数量失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据设备类型统计已分配数量")
    @GetMapping("/sum-allocated-quantity/type/{equipmentType}")
    public String sumAllocatedQuantityByType(@Parameter(description="设备类型") @PathVariable Integer equipmentType) {
        try {
            Integer quantity = equipmentResourceService.sumAllocatedQuantityByType(equipmentType);
            return JsonBean.success("操作成功", quantity);
        } catch (Exception e) {
            log.error("统计已分配数量失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询超预算的设备资源列表")
    @GetMapping("/over-budget")
    public String getOverBudget() {
        try {
            List<EquipmentResource> list = equipmentResourceService.getOverBudget();
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("查询超预算的设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询分配不足的设备资源列表")
    @GetMapping("/under-allocated")
    public String getUnderAllocated() {
        try {
            List<EquipmentResource> list = equipmentResourceService.getUnderAllocated();
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("查询分配不足的设备资源列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询故障设备列表")
    @GetMapping("/faulty")
    public String getFaultyEquipment() {
        try {
            List<EquipmentResource> list = equipmentResourceService.getFaultyEquipment();
            return JsonBean.success("操作成功", list);
        } catch (Exception e) {
            log.error("查询故障设备列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量更新分配状态")
    @PutMapping("/batch-allocation-status")
    public String batchUpdateAllocationStatus(@RequestBody List<Long> ids,
                                                     @Parameter(description="新状态") @RequestParam Integer allocationStatus,
                                                     @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Integer count = equipmentResourceService.batchUpdateAllocationStatus(ids, allocationStatus, updateBy);
            return JsonBean.success(count, "批量更新成功，共更新" + count + "条记录");
        } catch (Exception e) {
            log.error("批量更新分配状态失败", e);
            return JsonBean.error("批量更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量更新设备状态")
    @PutMapping("/batch-equipment-status")
    public String batchUpdateEquipmentStatus(@RequestBody List<Long> ids,
                                                    @Parameter(description="新状态") @RequestParam Integer equipmentStatus,
                                                    @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Integer count = equipmentResourceService.batchUpdateEquipmentStatus(ids, equipmentStatus, updateBy);
            return JsonBean.success(count, "批量更新成功，共更新" + count + "条记录");
        } catch (Exception e) {
            log.error("批量更新设备状态失败", e);
            return JsonBean.error("批量更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "设备分配")
    @PutMapping("/allocate/{id}")
    public String allocateEquipment(@Parameter(description="设备ID") @PathVariable Long id,
                                           @Parameter(description="分配数量") @RequestParam Integer allocatedQuantity,
                                           @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Boolean result = equipmentResourceService.allocateEquipment(id, allocatedQuantity, updateBy);
            return result ? JsonBean.success("设备分配成功") : JsonBean.error("设备分配失败");
        } catch (Exception e) {
            log.error("设备分配失败", e);
            return JsonBean.error("设备分配失败：" + e.getMessage());
        }
    }

    @Operation(summary = "设备归还")
    @PutMapping("/return/{id}")
    public String returnEquipment(@Parameter(description="设备ID") @PathVariable Long id,
                                         @Parameter(description="归还数量") @RequestParam Integer returnQuantity,
                                         @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Boolean result = equipmentResourceService.returnEquipment(id, returnQuantity, updateBy);
            return result ? JsonBean.success("设备归还成功") : JsonBean.error("设备归还失败");
        } catch (Exception e) {
            log.error("设备归还失败", e);
            return JsonBean.error("设备归还失败：" + e.getMessage());
        }
    }

    @Operation(summary = "设备维修")
    @PutMapping("/maintain/{id}")
    public String maintainEquipment(@Parameter(description="设备ID") @PathVariable Long id,
                                           @Parameter(description="维修原因") @RequestParam String maintenanceReason,
                                           @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Boolean result = equipmentResourceService.maintainEquipment(id, maintenanceReason, updateBy);
            return result ? JsonBean.success("设备维修成功") : JsonBean.error("设备维修失败");
        } catch (Exception e) {
            log.error("设备维修失败", e);
            return JsonBean.error("设备维修失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询设备资源统计信息")
    @GetMapping("/statistics")
    public String getEquipmentStatistics() {
        try {
            List<EquipmentResource> statistics = equipmentResourceService.getEquipmentStatistics();
            return JsonBean.success("操作成功", statistics);
        } catch (Exception e) {
            log.error("查询设备资源统计信息失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "设备资源优化建议")
    @GetMapping("/optimization-suggestions/{planningId}")
    public String getOptimizationSuggestions(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            List<String> suggestions = equipmentResourceService.getOptimizationSuggestions(planningId);
            return JsonBean.success("操作成功", suggestions);
        } catch (Exception e) {
            log.error("获取设备资源优化建议失败", e);
            return JsonBean.error("获取建议失败：" + e.getMessage());
        }
    }
}
