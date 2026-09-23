package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAllocation;
import com.management.accountant.service.BudgetAllocationService;
import com.management.accountant.util.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = {"NCV65全面预算-分配管理"})
@RequestMapping(value = "/accountant/budget/allocation")
@Slf4j
public class BudgetAllocationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAllocationService allocationService;

    @Operation(summary = "创建分配")
    @ApiOperation("创建分配") @PostMapping("/create")
    public MyJsonBean<BudgetAllocation> create(@RequestBody @Validated BudgetAllocation data) {
        MyJsonBean<BudgetAllocation> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("创建成功"); r.setData(allocationService.create(data));
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("创建分配异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新分配")
    @ApiOperation("更新分配") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody @Validated BudgetAllocation data) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { data.setAllocationId(id); allocationService.update(data); r.setCode(1); r.setMsg("更新成功");
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("更新分配异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "删除分配")
    @ApiOperation("删除分配") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { allocationService.delete(id); r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) { log.error("删除分配异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "分页查询")
    @ApiOperation("分页查询") @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(allocationService.getPage(params));
        } catch (Exception e) { log.error("分页查询异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取统计")
    @ApiOperation("获取统计") @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(allocationService.getStats());
        } catch (Exception e) { log.error("获取统计异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取维度列表")
    @ApiOperation("获取维度列表") @GetMapping("/dimensions")
    public MyJsonBean<List<Map<String, Object>>> getDimensions() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(allocationService.getDimensions());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分配目标")
    @ApiOperation("获取分配目标") @GetMapping("/targets")
    public MyJsonBean<List<Map<String, Object>>> getTargets() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(allocationService.getTargets());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "按维度获取目标")
    @ApiOperation("按维度获取目标") @GetMapping("/targets/{dimension}")
    public MyJsonBean<List<Map<String, Object>>> getTargetsByDimension(@PathVariable String dimension) {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(allocationService.getTargetsByDimension(dimension));
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "确认分配")
    @ApiOperation("确认分配") @PostMapping("/{id}/confirm")
    public MyJsonBean<Void> confirm(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { allocationService.confirm(id); r.setCode(1); r.setMsg("确认成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("确认失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "批量确认")
    @ApiOperation("批量确认") @PostMapping("/batch-confirm")
    public MyJsonBean<Void> batchConfirm(@RequestBody List<String> ids) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { allocationService.batchConfirm(ids); r.setCode(1); r.setMsg("批量确认成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("批量确认失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "重新计算")
    @ApiOperation("重新计算") @PostMapping("/recalculate")
    public MyJsonBean<Void> recalculate() {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { allocationService.recalculate(); r.setCode(1); r.setMsg("重新计算成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("重新计算失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "导出分配数据")
    @ApiOperation("导出分配数据") @PostMapping("/export")
    public void exportAllocations(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_allocation.xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出分配数据异常", e); }
    }

    @Operation(summary = "导出单个分配")
    @ApiOperation("导出单个分配") @GetMapping("/{id}/export")
    public void exportSingle(@PathVariable String id, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_allocation_" + id + ".xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出单个分配异常", e); }
    }
}

