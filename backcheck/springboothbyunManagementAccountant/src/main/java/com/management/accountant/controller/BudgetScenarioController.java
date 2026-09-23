package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetScenario;
import com.management.accountant.service.BudgetScenarioService;
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
@Api(tags = {"NCV65全面预算-场景分析"})
@RequestMapping(value = "/accountant/budget/scenario")
@Slf4j
public class BudgetScenarioController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetScenarioService scenarioService;

    @Operation(summary = "创建场景")
    @ApiOperation("创建场景") @PostMapping("/create")
    public MyJsonBean<BudgetScenario> create(@RequestBody @Validated BudgetScenario data) {
        MyJsonBean<BudgetScenario> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("创建成功"); r.setData(scenarioService.create(data));
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("创建场景异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取场景详情")
    @ApiOperation("获取场景详情") @GetMapping("/detail/{id}")
    public MyJsonBean<BudgetScenario> detail(@PathVariable String id) {
        MyJsonBean<BudgetScenario> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(scenarioService.getById(id));
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新场景")
    @ApiOperation("更新场景") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody @Validated BudgetScenario data) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { data.setScenarioId(id); scenarioService.update(data); r.setCode(1); r.setMsg("更新成功");
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("更新场景异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "删除场景")
    @ApiOperation("删除场景") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { scenarioService.delete(id); r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) { log.error("删除场景异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "分页查询")
    @ApiOperation("分页查询") @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(scenarioService.getPage(params));
        } catch (Exception e) { log.error("分页查询异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取基准场景")
    @ApiOperation("获取基准场景") @GetMapping("/baselines")
    public MyJsonBean<List<BudgetScenario>> getBaselineScenarios() {
        MyJsonBean<List<BudgetScenario>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(scenarioService.getBaselineScenarios());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取科目选项")
    @ApiOperation("获取科目选项") @GetMapping("/account/options")
    public MyJsonBean<List<Map<String, Object>>> getAccountOptions() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(scenarioService.getAccountOptions());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "计算场景")
    @ApiOperation("计算场景") @PostMapping("/calculate")
    public MyJsonBean<Map<String, Object>> calculateScenario(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("计算完成"); r.setData(scenarioService.calculateScenario(params));
        } catch (Exception e) { r.setCode(0); r.setMsg("计算失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "对比场景")
    @ApiOperation("对比场景") @PostMapping("/compare")
    public MyJsonBean<Map<String, Object>> compareScenarios(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("对比完成"); r.setData(scenarioService.compareScenarios(params));
        } catch (Exception e) { r.setCode(0); r.setMsg("对比失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "设为基准")
    @ApiOperation("设为基准") @PostMapping("/{id}/baseline")
    public MyJsonBean<Void> setBaseline(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { scenarioService.setBaseline(id); r.setCode(1); r.setMsg("设置成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("设置失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "提交审批")
    @ApiOperation("提交审批") @PostMapping("/{id}/submit")
    public MyJsonBean<Void> submitApproval(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { scenarioService.submitApproval(id); r.setCode(1); r.setMsg("提交成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("提交失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "导出对比结果")
    @ApiOperation("导出对比结果") @PostMapping("/compare/export")
    public void exportCompare(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_scenario_compare.xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出对比结果异常", e); }
    }

    @Operation(summary = "导出场景数据")
    @ApiOperation("导出场景数据") @PostMapping("/export")
    public void exportScenarios(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_scenario.xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出场景数据异常", e); }
    }

    @Operation(summary = "导出单个场景")
    @ApiOperation("导出单个场景") @GetMapping("/{id}/export")
    public void exportSingle(@PathVariable String id, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_scenario_" + id + ".xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出单个场景异常", e); }
    }
}

