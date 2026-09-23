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
import com.huabo.contract.entity.PersonnelAllocation;
import com.huabo.contract.service.PersonnelAllocationService;
import com.huabo.contract.vo.PersonnelAllocationQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 人员配置控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Tag(name="人员配置管理",description="人员配置管理")
@RestController
@RequestMapping("/planning/personnel")
@RequiredArgsConstructor
@Slf4j
public class PersonnelAllocationController {

    private final PersonnelAllocationService personnelAllocationService;

    @Operation(summary = "分页查询人员配置列表")
    @PostMapping("/list")
    public String getPersonnelAllocationList(@RequestBody PersonnelAllocationQueryParam queryParam) {
        try {
            log.info("分页查询人员配置列表，参数：{}", queryParam);
            IPage<PersonnelAllocation> page = personnelAllocationService.getPersonnelAllocationPage(queryParam);
            return JsonBean.success("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "分页查询人员配置列表")
    @PostMapping("/page")
    public String getPersonnelAllocationPage(@RequestBody PersonnelAllocationQueryParam queryParam) {
        try {
            log.info("分页查询人员配置列表，参数：{}", queryParam);
            IPage<PersonnelAllocation> page = personnelAllocationService.getPersonnelAllocationPage(queryParam);
            return JsonBean.success("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询人员配置详情")
    @GetMapping("/{id}")
    public String getPersonnelAllocationById(@Parameter(description="人员配置ID") @PathVariable Long id) {
        try {
            log.info("根据ID查询人员配置详情，ID：{}", id);
            PersonnelAllocation personnelAllocation = personnelAllocationService.getById(id);
            if (personnelAllocation == null) {
                return JsonBean.error("人员配置不存在");
            }
            return JsonBean.success("查询成功", personnelAllocation);
        } catch (Exception e) {
            log.error("根据ID查询人员配置详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增人员配置")
    @PostMapping
    public String savePersonnelAllocation(@Valid @RequestBody PersonnelAllocation personnelAllocation) {
        try {
            log.info("新增人员配置，配置：{}", personnelAllocation);
            // 验证人员配置信息
            if (!personnelAllocationService.validateAllocationInfo(personnelAllocation)) {
                return JsonBean.error("人员配置信息验证失败");
            }

            boolean result = personnelAllocationService.save(personnelAllocation);
            if (result) {
                return JsonBean.success("新增成功");
            } else {
                return JsonBean.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增人员配置失败", e);
            return JsonBean.error("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "修改人员配置")
    @PutMapping
    public String updatePersonnelAllocation(@Valid @RequestBody PersonnelAllocation personnelAllocation) {
        try {
            // 验证人员配置信息
            if (!personnelAllocationService.validateAllocationInfo(personnelAllocation)) {
                return JsonBean.error("人员配置信息验证失败");
            }

            boolean result = personnelAllocationService.updateById(personnelAllocation);
            return result ? JsonBean.success("修改成功") : JsonBean.error("修改失败");
        } catch (Exception e) {
            log.error("修改人员配置失败", e);
            return JsonBean.error("修改失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除人员配置")
    @DeleteMapping("/{id}")
    public String deletePersonnelAllocation(@Parameter(description="人员配置ID") @PathVariable Long id) {
        try {
            boolean result = personnelAllocationService.removeById(id);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除人员配置失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除人员配置")
    @DeleteMapping("/batch")
    public String batchDeletePersonnelAllocation(@RequestBody List<Long> ids) {
        try {
            boolean result = personnelAllocationService.removeByIds(ids);
            return result ? JsonBean.success("批量删除成功") : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除人员配置失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据项目策划ID查询人员配置列表")
    @GetMapping("/planning/{planningId}")
    public String getByPlanningId(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            List<PersonnelAllocation> list = personnelAllocationService.getByPlanningId(planningId);
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            log.error("查询人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据配置状态查询人员配置列表")
    @GetMapping("/status/{allocationStatus}")
    public String getByAllocationStatus(@Parameter(description="配置状态") @PathVariable Integer allocationStatus) {
        try {
            List<PersonnelAllocation> list = personnelAllocationService.getByAllocationStatus(allocationStatus);
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            log.error("根据配置状态查询人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据岗位类型查询人员配置列表")
    @GetMapping("/position-type/{positionType}")
    public String getByPositionType(@Parameter(description="岗位类型") @PathVariable Integer positionType) {
        try {
            List<PersonnelAllocation> list = personnelAllocationService.getByPositionType(positionType);
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            log.error("根据岗位类型查询人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据优先级查询人员配置列表")
    @GetMapping("/priority/{priority}")
    public String getByPriority(@Parameter(description="优先级") @PathVariable Integer priority) {
        try {
            List<PersonnelAllocation> list = personnelAllocationService.getByPriority(priority);
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            log.error("根据优先级查询人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据项目策划ID统计预算成本总额")
    @GetMapping("/sum-budgeted-cost/planning/{planningId}")
    public String sumBudgetedCostByPlanning(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            BigDecimal cost = personnelAllocationService.sumBudgetedCostByPlanning(planningId);
            return JsonBean.success("统计成功", cost);
        } catch (Exception e) {
            log.error("统计预算成本总额失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据项目策划ID统计实际成本总额")
    @GetMapping("/sum-actual-cost/planning/{planningId}")
    public String sumActualCostByPlanning(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            BigDecimal cost = personnelAllocationService.sumActualCostByPlanning(planningId);
            return JsonBean.success("统计成功", cost);
        } catch (Exception e) {
            log.error("统计实际成本总额失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据岗位类型统计需求人数")
    @GetMapping("/sum-required-count/position-type/{positionType}")
    public String sumRequiredCountByPositionType(@Parameter(description="岗位类型") @PathVariable Integer positionType) {
        try {
            Integer count = personnelAllocationService.sumRequiredCountByPositionType(positionType);
            return JsonBean.success("统计成功", count);
        } catch (Exception e) {
            log.error("统计需求人数失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据岗位类型统计已分配人数")
    @GetMapping("/sum-allocated-count/position-type/{positionType}")
    public String sumAllocatedCountByPositionType(@Parameter(description="岗位类型") @PathVariable Integer positionType) {
        try {
            Integer count = personnelAllocationService.sumAllocatedCountByPositionType(positionType);
            return JsonBean.success("统计成功", count);
        } catch (Exception e) {
            log.error("统计已分配人数失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据配置状态统计数量")
    @GetMapping("/count/status/{allocationStatus}")
    public String countByAllocationStatus(@Parameter(description="配置状态") @PathVariable Integer allocationStatus) {
        try {
            Integer count = personnelAllocationService.countByAllocationStatus(allocationStatus);
            return JsonBean.success("统计成功", count);
        } catch (Exception e) {
            log.error("根据配置状态统计数量失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询超预算的人员配置列表")
    @GetMapping("/over-budget")
    public String getOverBudget() {
        try {
            List<PersonnelAllocation> list = personnelAllocationService.getOverBudget();
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            log.error("查询超预算的人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询分配不足的人员配置列表")
    @GetMapping("/under-allocated")
    public String getUnderAllocated() {
        try {
            List<PersonnelAllocation> list = personnelAllocationService.getUnderAllocated();
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            log.error("查询分配不足的人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量更新配置状态")
    @PutMapping("/batch-status")
    public String batchUpdateStatus(@RequestBody List<Long> ids,
                                           @Parameter(description="新状态") @RequestParam Integer allocationStatus,
                                           @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Integer count = personnelAllocationService.batchUpdateStatus(ids, allocationStatus, updateBy);
            return JsonBean.success(count, "批量更新成功，共更新" + count + "条记录");
        } catch (Exception e) {
            log.error("批量更新配置状态失败", e);
            return JsonBean.error("批量更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据工作地点查询人员配置列表")
    @GetMapping("/work-location/{workLocation}")
    public String getByWorkLocation(@Parameter(description="工作地点") @PathVariable String workLocation) {
        try {
            List<PersonnelAllocation> list = personnelAllocationService.getByWorkLocation(workLocation);
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            log.error("根据工作地点查询人员配置列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "人员分配")
    @PutMapping("/allocate/{id}")
    public String allocatePersonnel(@Parameter(description="配置ID") @PathVariable Long id,
                                           @Parameter(description="人员列表") @RequestParam String personnelList,
                                           @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Boolean result = personnelAllocationService.allocatePersonnel(id, personnelList, updateBy);
            return result ? JsonBean.success("人员分配成功") : JsonBean.error("人员分配失败");
        } catch (Exception e) {
            log.error("人员分配失败", e);
            return JsonBean.error("人员分配失败：" + e.getMessage());
        }
    }

    @Operation(summary = "人员调整")
    @PutMapping("/adjust/{id}")
    public String adjustPersonnel(@Parameter(description="配置ID") @PathVariable Long id,
                                         @Parameter(description="新人员列表") @RequestParam String newPersonnelList,
                                         @Parameter(description="更新人") @RequestParam Long updateBy) {
        try {
            Boolean result = personnelAllocationService.adjustPersonnel(id, newPersonnelList, updateBy);
            return result ? JsonBean.success("人员调整成功") : JsonBean.error("人员调整失败");
        } catch (Exception e) {
            log.error("人员调整失败", e);
            return JsonBean.error("人员调整失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询人员配置统计信息")
    @GetMapping("/statistics")
    public String getAllocationStatistics() {
        try {
            List<PersonnelAllocation> statistics = personnelAllocationService.getAllocationStatistics();
            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("查询人员配置统计信息失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "人员配置优化建议")
    @GetMapping("/optimization-suggestions/{planningId}")
    public String getOptimizationSuggestions(@Parameter(description="项目策划ID") @PathVariable Long planningId) {
        try {
            List<String> suggestions = personnelAllocationService.getOptimizationSuggestions(planningId);
            return JsonBean.success("获取成功", suggestions);
        } catch (Exception e) {
            log.error("获取人员配置优化建议失败", e);
            return JsonBean.error("获取建议失败：" + e.getMessage());
        }
    }
}
