package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetWorkflow;
import com.management.accountant.service.BudgetWorkflowService;
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
@Api(tags = {"NCV65全面预算-工作流管理"})
@RequestMapping(value = "/accountant/budget/workflow")
@Slf4j
public class BudgetWorkflowController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetWorkflowService workflowService;

    @Operation(summary = "创建工作流")
    @ApiOperation("创建工作流") @PostMapping("/create")
    public MyJsonBean<BudgetWorkflow> create(@RequestBody @Validated BudgetWorkflow data) {
        MyJsonBean<BudgetWorkflow> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("创建成功"); r.setData(workflowService.create(data));
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("创建工作流异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取工作流详情")
    @ApiOperation("获取工作流详情") @GetMapping("/detail/{id}")
    public MyJsonBean<BudgetWorkflow> detail(@PathVariable String id) {
        MyJsonBean<BudgetWorkflow> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(workflowService.getById(id));
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新工作流")
    @ApiOperation("更新工作流") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody @Validated BudgetWorkflow data) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { data.setWorkflowId(id); workflowService.update(data); r.setCode(1); r.setMsg("更新成功");
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("更新工作流异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "删除工作流")
    @ApiOperation("删除工作流") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { workflowService.delete(id); r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) { log.error("删除工作流异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "分页查询")
    @ApiOperation("分页查询") @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(workflowService.getPage(params));
        } catch (Exception e) { log.error("分页查询异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分类树")
    @ApiOperation("获取分类树") @GetMapping("/category/tree")
    public MyJsonBean<List<Map<String, Object>>> getCategoryTree() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(workflowService.getCategoryTree());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分类列表")
    @ApiOperation("获取分类列表") @GetMapping("/categories")
    public MyJsonBean<List<Map<String, Object>>> getCategories() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(workflowService.getCategories());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "部署工作流")
    @ApiOperation("部署工作流") @PostMapping("/{id}/deploy")
    public MyJsonBean<Void> deploy(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { workflowService.deploy(id); r.setCode(1); r.setMsg("部署成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("部署失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "批量部署")
    @ApiOperation("批量部署") @PostMapping("/batch-deploy")
    public MyJsonBean<Void> batchDeploy(@RequestBody List<String> ids) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { workflowService.batchDeploy(ids); r.setCode(1); r.setMsg("批量部署成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("批量部署失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "导出工作流数据")
    @ApiOperation("导出工作流数据") @PostMapping("/export")
    public void exportWorkflows(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_workflow.xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出工作流数据异常", e); }
    }

    @Operation(summary = "导出单个工作流")
    @ApiOperation("导出单个工作流") @GetMapping("/{id}/export")
    public void exportSingle(@PathVariable String id, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_workflow_" + id + ".xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出单个工作流异常", e); }
    }

    @Operation(summary = "获取工作流统计")
    @ApiOperation("获取工作流统计") @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(workflowService.getStats());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }
}

