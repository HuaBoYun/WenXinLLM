package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetRule;
import com.management.accountant.service.BudgetRuleService;
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
@Api(tags = {"NCV65全面预算-规则配置"})
@RequestMapping(value = "/accountant/budget/rule")
@Slf4j
public class BudgetRuleController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetRuleService ruleService;

    @Operation(summary = "创建规则")
    @ApiOperation("创建规则") @PostMapping("/create")
    public MyJsonBean<BudgetRule> create(@RequestBody @Validated BudgetRule data) {
        MyJsonBean<BudgetRule> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("创建成功"); r.setData(ruleService.create(data));
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("创建规则异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取规则详情")
    @ApiOperation("获取规则详情") @GetMapping("/detail/{id}")
    public MyJsonBean<BudgetRule> detail(@PathVariable String id) {
        MyJsonBean<BudgetRule> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(ruleService.getById(id));
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新规则")
    @ApiOperation("更新规则") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody @Validated BudgetRule data) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { data.setRuleId(id); ruleService.update(data); r.setCode(1); r.setMsg("更新成功");
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("更新规则异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "删除规则")
    @ApiOperation("删除规则") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { ruleService.delete(id); r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) { log.error("删除规则异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "分页查询")
    @ApiOperation("分页查询") @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(ruleService.getPage(params));
        } catch (Exception e) { log.error("分页查询异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分类树")
    @ApiOperation("获取分类树") @GetMapping("/category/tree")
    public MyJsonBean<List<Map<String, Object>>> getCategoryTree() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(ruleService.getCategoryTree());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分类列表")
    @ApiOperation("获取分类列表") @GetMapping("/categories")
    public MyJsonBean<List<Map<String, Object>>> getCategories() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(ruleService.getCategories());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新状态")
    @ApiOperation("更新状态") @PutMapping("/{id}/status")
    public MyJsonBean<Void> updateStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { ruleService.updateStatus(id, body.get("ruleStatus")); r.setCode(1); r.setMsg("更新成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "验证条件")
    @ApiOperation("验证条件") @PostMapping("/validate/condition")
    public MyJsonBean<Map<String, Object>> validateCondition(@RequestBody Map<String, String> body) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("验证完成"); r.setData(ruleService.validateCondition(body.get("condition")));
        } catch (Exception e) { r.setCode(0); r.setMsg("验证失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "测试规则")
    @ApiOperation("测试规则") @PostMapping("/test")
    public MyJsonBean<Map<String, Object>> testRule(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("测试完成"); r.setData(ruleService.testRule(params));
        } catch (Exception e) { r.setCode(0); r.setMsg("测试失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "验证规则")
    @ApiOperation("验证规则") @PostMapping("/{id}/validate")
    public MyJsonBean<Map<String, Object>> validate(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("验证完成"); r.setData(ruleService.validate(id));
        } catch (Exception e) { r.setCode(0); r.setMsg("验证失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "批量验证")
    @ApiOperation("批量验证") @PostMapping("/batch-validate")
    public MyJsonBean<Map<String, Object>> batchValidate(@RequestBody List<String> ids) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("验证完成"); r.setData(ruleService.batchValidate(ids));
        } catch (Exception e) { r.setCode(0); r.setMsg("验证失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "导出规则数据")
    @ApiOperation("导出规则数据") @PostMapping("/export")
    public void exportRules(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_rule.xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出规则数据异常", e); }
    }

    @Operation(summary = "导出单个规则")
    @ApiOperation("导出单个规则") @GetMapping("/{id}/export")
    public void exportSingle(@PathVariable String id, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_rule_" + id + ".xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出单个规则异常", e); }
    }

    @Operation(summary = "获取规则统计")
    @ApiOperation("获取规则统计") @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(ruleService.getStats());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }
}

