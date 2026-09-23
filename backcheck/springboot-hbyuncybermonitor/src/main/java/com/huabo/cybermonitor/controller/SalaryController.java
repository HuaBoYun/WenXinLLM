package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.TblSalaryTotal;
import com.huabo.cybermonitor.service.ITblSalaryTotalService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "薪酬分配穿透式监管")
@RestController
@RequestMapping("/v1/supervision/salary/total")
@Slf4j
public class SalaryController {

    @Autowired
    private ITblSalaryTotalService salaryTotalService;

    @Operation(summary = "分页查询工资总额列表")
    @PostMapping("/list")
    public R<PageResult<TblSalaryTotal>> list(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            String companyName = (String) params.get("companyName");
            String reportYear = (String) params.get("reportYear");
            String companyId = (String) params.get("companyId");
            String status = (String) params.get("status");

            LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) {
                wrapper.like(TblSalaryTotal::getCompanyName, companyName);
            }
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(TblSalaryTotal::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(reportYear)) {
                wrapper.eq(TblSalaryTotal::getReportYear, reportYear);
            }
            // status是派生字段：OVER_BUDGET = 实际总额 > 核准总额，NORMAL = 实际总额 <= 核准总额
            if ("OVER_BUDGET".equals(status)) {
                wrapper.isNotNull(TblSalaryTotal::getBudgetTotal);
                wrapper.apply("ACTUAL_TOTAL > BUDGET_TOTAL");
            } else if ("NORMAL".equals(status)) {
                wrapper.and(w -> w.isNull(TblSalaryTotal::getBudgetTotal)
                        .or().apply("ACTUAL_TOTAL <= BUDGET_TOTAL"));
            }
            wrapper.orderByDesc(TblSalaryTotal::getCreateTime);

            Page<TblSalaryTotal> page = new Page<>(pageNum, pageSize);
            Page<TblSalaryTotal> result = this.salaryTotalService.page(page, wrapper);

            PageResult<TblSalaryTotal> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(result.getRecords());
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询工资总额列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询工资总额详情")
    @GetMapping("/{id}")
    public R<TblSalaryTotal> detail(@PathVariable String id) {
        try {
            return R.success(salaryTotalService.getById(id));
        } catch (Exception e) {
            log.error("查询工资总额详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增工资总额")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblSalaryTotal record) {
        try {
            return salaryTotalService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增工资总额失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新工资总额")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblSalaryTotal record) {
        try {
            if (StringUtils.isEmpty(record.getSalaryId())) {
                return R.fail("工资ID不能为空");
            }
            return salaryTotalService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新工资总额失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除工资总额")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return salaryTotalService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除工资总额失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除工资总额")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return salaryTotalService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除工资总额失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取薪酬统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestParam(required = false) String companyId) {
        try {
            return R.success(salaryTotalService.getStatistics(companyId));
        } catch (Exception e) {
            log.error("获取薪酬统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }
}

