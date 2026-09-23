package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetParameter;
import com.management.accountant.oracle.entity.budget.BudgetParameterExportDTO;
import com.management.accountant.service.BudgetParameterService;
import com.management.accountant.util.ExcelUtil;
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
@Api(tags = {"NCV65全面预算-参数设置"})
@RequestMapping(value = "/accountant/budget/parameter")
@Slf4j
public class BudgetParameterController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetParameterService parameterService;

    @Operation(summary = "创建参数")
    @ApiOperation("创建参数") @PostMapping("/create")
    public MyJsonBean<BudgetParameter> create(@RequestBody @Validated BudgetParameter data) {
        MyJsonBean<BudgetParameter> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("创建成功"); r.setData(parameterService.create(data));
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("创建参数异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取参数详情")
    @ApiOperation("获取参数详情") @GetMapping("/detail/{id}")
    public MyJsonBean<BudgetParameter> detail(@PathVariable String id) {
        MyJsonBean<BudgetParameter> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(parameterService.getById(id));
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新参数")
    @ApiOperation("更新参数") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody @Validated BudgetParameter data) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { data.setParameterId(id); parameterService.update(data); r.setCode(1); r.setMsg("更新成功");
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("更新参数异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "删除参数")
    @ApiOperation("删除参数") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { parameterService.delete(id); r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) { log.error("删除参数异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "分页查询")
    @ApiOperation("分页查询") @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(parameterService.getPage(params));
        } catch (Exception e) { log.error("分页查询异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分类树")
    @ApiOperation("获取分类树") @GetMapping("/category/tree")
    public MyJsonBean<List<Map<String, Object>>> getCategoryTree() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(parameterService.getCategoryTree());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分类列表")
    @ApiOperation("获取分类列表") @GetMapping("/categories")
    public MyJsonBean<List<Map<String, Object>>> getCategories() {
        MyJsonBean<List<Map<String, Object>>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(parameterService.getCategories());
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新状态")
    @ApiOperation("更新状态") @PutMapping("/{id}/status")
    public MyJsonBean<Void> updateStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            // 前端传的字段名是 parameterStatus
            String status = body.get("parameterStatus") != null ? body.get("parameterStatus") : body.get("status");
            parameterService.updateStatus(id, status);
            r.setCode(1); r.setMsg("更新成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "验证参数")
    @ApiOperation("验证参数") @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validateParameter(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("验证完成"); r.setData(parameterService.validateParameter(params));
        } catch (Exception e) { r.setCode(0); r.setMsg("验证失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "重置参数")
    @ApiOperation("重置参数") @PostMapping("/{id}/reset")
    public MyJsonBean<Void> resetParameter(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { parameterService.resetParameter(id); r.setCode(1); r.setMsg("重置成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("重置失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "批量验证")
    @ApiOperation("批量验证") @PostMapping("/batch-validate")
    public MyJsonBean<Map<String, Object>> batchValidate(@RequestBody List<String> ids) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("验证完成"); r.setData(parameterService.batchValidate(ids));
        } catch (Exception e) { r.setCode(0); r.setMsg("验证失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "导出参数数据")
    @ApiOperation("导出参数数据") @PostMapping("/export")
    public void exportParameters(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetParameter> data = parameterService.getExportData(params);
            List<BudgetParameterExportDTO> exportList = new java.util.ArrayList<>();
            for (BudgetParameter p : data) { exportList.add(BudgetParameterExportDTO.fromEntity(p)); }
            ExcelUtil.exportExcel(exportList, BudgetParameterExportDTO.class, "预算参数", response);
        } catch (Exception e) {
            log.error("导出参数数据异常", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    @Operation(summary = "导出单个参数")
    @ApiOperation("导出单个参数") @GetMapping("/{id}/export")
    public void exportSingle(@PathVariable String id, HttpServletResponse response) {
        try {
            BudgetParameter p = parameterService.getByIdDirect(id);
            List<BudgetParameterExportDTO> exportList = new java.util.ArrayList<>();
            if (p != null) { exportList.add(BudgetParameterExportDTO.fromEntity(p)); }
            ExcelUtil.exportExcel(exportList, BudgetParameterExportDTO.class, "预算参数_" + id, response);
        } catch (Exception e) {
            log.error("导出单个参数异常", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    @Operation(summary = "下载导入模板")
    @ApiOperation("下载导入模板") @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            ExcelUtil.downloadTemplate(BudgetParameterExportDTO.class, "预算参数导入模板", response);
        } catch (Exception e) {
            log.error("下载模板异常", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"下载模板失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }
}

