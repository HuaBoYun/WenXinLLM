package com.huabo.contract.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.BudgetDetail;
import com.huabo.contract.service.BudgetDetailService;
import com.huabo.contract.vo.BudgetDetailQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 预算明细控制器
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Tag(name="预算明细管理",description="预算明细管理")
@RestController
@RequestMapping("/budget/detail")
@RequiredArgsConstructor
public class BudgetDetailController {

    private final BudgetDetailService budgetDetailService;

    @Operation(summary = "分页查询预算明细列表")
    @PostMapping("/page")
    public String getBudgetDetailPage(@RequestBody BudgetDetailQueryParam queryParam) {
        try {
            IPage<BudgetDetail> page = budgetDetailService.getBudgetDetailPage(queryParam);
            return JsonBean.success(page, page.getRecords());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询预算明细列表")
    @PostMapping("/list")
    public String getBudgetDetailList(@RequestBody BudgetDetailQueryParam queryParam) {
        try {
            IPage<BudgetDetail> page = budgetDetailService.getBudgetDetailPage(queryParam);
            return JsonBean.success(page, page.getRecords());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询预算明细详情")
    @GetMapping("/{id}")
    public String getBudgetDetailById(@Parameter(description="预算明细ID") @PathVariable Long id) {
        try {
            BudgetDetail budgetDetail = budgetDetailService.getById(id);
            return JsonBean.success(budgetDetail, budgetDetail);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增预算明细")
    @PostMapping({"/create", "/save"})
    public String saveBudgetDetail(@Valid @RequestBody BudgetDetail budgetDetail) {
        try {
            // 验证预算明细信息
            if (!budgetDetailService.validateBudgetDetailInfo(budgetDetail)) {
                return JsonBean.error("预算明细信息验证失败");
            }

            boolean result = budgetDetailService.save(budgetDetail);
            if (result) {
                return JsonBean.success("预算明细保存成功");
            } else {
                return JsonBean.error("预算明细保存失败");
            }
        } catch (Exception e) {
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改预算明细")
    @PutMapping
    public String updateBudgetDetail(@Valid @RequestBody BudgetDetail budgetDetail) {
        try {
            // 验证预算明细信息
            if (!budgetDetailService.validateBudgetDetailInfo(budgetDetail)) {
                return JsonBean.error("预算明细信息验证失败");
            }

            boolean result = budgetDetailService.updateById(budgetDetail);
            if (result) {
                return JsonBean.success("预算明细更新成功");
            } else {
                return JsonBean.error("预算明细更新失败");
            }
        } catch (Exception e) {
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除预算明细")
    @DeleteMapping("/{id}")
    public String deleteBudgetDetail(@Parameter(description="预算明细ID") @PathVariable Long id) {
        try {
            boolean result = budgetDetailService.removeById(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除预算明细")
    @DeleteMapping("/batch")
    public String batchDeleteBudgetDetail(@RequestBody List<Long> ids) {
        try {
            boolean result = budgetDetailService.removeByIds(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量创建预算明细")
    @PostMapping("/batch")
    public String batchSaveBudgetDetail(@Valid @RequestBody List<BudgetDetail> budgetDetails) {
        try {
            // 验证每个预算明细信息
            for (BudgetDetail budgetDetail : budgetDetails) {
                if (!budgetDetailService.validateBudgetDetailInfo(budgetDetail)) {
                    return JsonBean.error("预算明细信息验证失败");
                }
            }

            boolean result = budgetDetailService.saveBatch(budgetDetails);
            if (result) {
                return JsonBean.success("批量保存成功");
            } else {
                return JsonBean.error("批量保存失败");
            }
        } catch (Exception e) {
            return JsonBean.error("批量保存失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据项目预算ID查询预算明细列表")
    @GetMapping("/budget/{budgetId}")
    public String getByBudgetId(@Parameter(description="项目预算ID") @PathVariable Long budgetId) {
        try {
            List<BudgetDetail> list = budgetDetailService.getByBudgetId(budgetId);
            return JsonBean.success(list, list);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据成本类别查询预算明细列表")
    @GetMapping("/cost-category/{costCategory}")
    public String getByCostCategory(@Parameter(description="成本类别") @PathVariable Integer costCategory) {
        try {
            List<BudgetDetail> list = budgetDetailService.getByCostCategory(costCategory);
            return JsonBean.success(list, list);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }


}
