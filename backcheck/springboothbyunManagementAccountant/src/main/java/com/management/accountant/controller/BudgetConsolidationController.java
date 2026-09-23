package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetConsolidation;
import com.management.accountant.service.BudgetConsolidationService;
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
@Api(tags = {"NCV65全面预算-合并管理"})
@RequestMapping(value = "/accountant/budget/consolidation")
@Slf4j
public class BudgetConsolidationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetConsolidationService consolidationService;

    @Operation(summary = "创建合并")
    @ApiOperation("创建合并") @PostMapping("/create")
    public MyJsonBean<BudgetConsolidation> create(@RequestBody @Validated BudgetConsolidation data) {
        MyJsonBean<BudgetConsolidation> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("创建成功"); r.setData(consolidationService.create(data));
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("创建合并异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新合并")
    @ApiOperation("更新合并") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody @Validated BudgetConsolidation data) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { data.setConsolidationId(id); consolidationService.update(data); r.setCode(1); r.setMsg("更新成功");
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("更新合并异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "删除合并")
    @ApiOperation("删除合并") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { consolidationService.delete(id); r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) { log.error("删除合并异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "分页查询")
    @ApiOperation("分页查询") @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(consolidationService.getPage(params));
        } catch (Exception e) { log.error("分页查询异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取统计")
    @ApiOperation("获取统计") @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(consolidationService.getStats());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "执行合并")
    @ApiOperation("执行合并") @PostMapping("/{id}/execute")
    public MyJsonBean<Void> execute(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { consolidationService.execute(id); r.setCode(1); r.setMsg("执行成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("执行失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "自动合并")
    @ApiOperation("自动合并") @PostMapping("/auto")
    public MyJsonBean<Void> autoConsolidate() {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { consolidationService.autoConsolidate(); r.setCode(1); r.setMsg("自动合并成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("自动合并失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "验证合并")
    @ApiOperation("验证合并") @PostMapping("/{id}/validate")
    public MyJsonBean<Map<String, Object>> validate(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("验证完成"); r.setData(consolidationService.validate(id));
        } catch (Exception e) { r.setCode(0); r.setMsg("验证失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "批量验证")
    @ApiOperation("批量验证") @PostMapping("/batch-validate")
    public MyJsonBean<Map<String, Object>> batchValidate(@RequestBody List<String> ids) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("验证完成"); r.setData(consolidationService.batchValidate(ids));
        } catch (Exception e) { r.setCode(0); r.setMsg("验证失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "导出合并数据")
    @ApiOperation("导出合并数据") @PostMapping("/export")
    public void exportConsolidations(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_consolidation.xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出合并数据异常", e); }
    }

    @Operation(summary = "导出单个合并")
    @ApiOperation("导出单个合并") @GetMapping("/{id}/export")
    public void exportSingle(@PathVariable String id, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_consolidation_" + id + ".xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出单个合并异常", e); }
    }
}

