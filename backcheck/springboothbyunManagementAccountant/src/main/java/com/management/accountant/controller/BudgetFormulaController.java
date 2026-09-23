package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetFormula;
import com.management.accountant.service.BudgetFormulaService;
import com.management.accountant.util.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 预算公式Controller
 * 
 * @description 预算公式管理接口，支持公式定义、计算、验证等功能
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-公式管理"})
@RequestMapping(value = "/accountant/budget/formula")
@Slf4j
public class BudgetFormulaController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetFormulaService formulaService;

    /**
     * 创建公式
     */
    @Operation(summary = "创建公式")
    @ApiOperation("创建公式")
    @PostMapping("")
    public MyJsonBean<BudgetFormula> create(@RequestBody @Validated BudgetFormula formula) {
        MyJsonBean<BudgetFormula> result = new MyJsonBean<>();
        try {
            BudgetFormula created = formulaService.create(formula);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建公式失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建公式异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询公式详情
     */
    @Operation(summary = "查询公式详情")
    @ApiOperation("查询公式详情")
    @GetMapping("/detail/{formulaId}")
    public MyJsonBean<BudgetFormula> getDetail(
            @ApiParam(value = "公式ID", required = true) @PathVariable String formulaId) {
        MyJsonBean<BudgetFormula> result = new MyJsonBean<>();
        try {
            BudgetFormula formula = formulaService.getById(formulaId);
            if (formula != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(formula);
            } else {
                result.setCode(0);
                result.setMsg("公式不存在");
            }
        } catch (Exception e) {
            log.error("查询公式详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新公式
     */
    @Operation(summary = "更新公式")
    @ApiOperation("更新公式")
    @PutMapping("/{formulaId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "公式ID", required = true) @PathVariable String formulaId,
            @RequestBody @Validated BudgetFormula formula) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            formula.setFormulaId(formulaId);
            formulaService.update(formula);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新公式失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新公式异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除公式
     */
    @Operation(summary = "删除公式")
    @ApiOperation("删除公式")
    @DeleteMapping("/{formulaId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "公式ID", required = true) @PathVariable String formulaId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            formulaService.delete(formulaId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除公式失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除公式异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询公式列表
     */
    @Operation(summary = "分页查询公式列表")
    @ApiOperation("分页查询公式列表")
    @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(formulaService.getPage(params));
        } catch (Exception e) {
            log.error("分页查询公式列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 验证公式表达式
     */
    @Operation(summary = "验证公式表达式")
    @ApiOperation("验证公式表达式")
    @PostMapping("/validate/expression")
    public MyJsonBean<Map<String, Object>> validateExpression(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validateResult = formulaService.validateFormula(params);
            result.setCode(1);
            result.setMsg("验证完成");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("验证公式异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 计算公式
     */
    @Operation(summary = "计算公式")
    @ApiOperation("计算公式")
    @PostMapping("/calculate")
    public MyJsonBean<Map<String, Object>> calculate(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> calculateResult = formulaService.calculateFormula(params);
            result.setCode(1);
            result.setMsg("计算成功");
            result.setData(calculateResult);
        } catch (Exception e) {
            log.error("计算公式异常", e);
            result.setCode(0);
            result.setMsg("计算失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取公式分类树
     */
    @Operation(summary = "获取公式分类树")
    @ApiOperation("获取公式分类树")
    @GetMapping("/category/tree")
    public MyJsonBean<List<Map<String, Object>>> getCategoryTree() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> tree = formulaService.getCategoryTree();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tree);
        } catch (Exception e) {
            log.error("获取公式分类树异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取公式分类列表
     */
    @Operation(summary = "获取公式分类列表")
    @ApiOperation("获取公式分类列表")
    @GetMapping("/categories")
    public MyJsonBean<List<Map<String, Object>>> getCategories() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> categories = formulaService.getCategories();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(categories);
        } catch (Exception e) {
            log.error("获取公式分类列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 验证单个公式
     */
    @Operation(summary = "验证单个公式")
    @ApiOperation("验证单个公式")
    @PostMapping("/{formulaId}/validate")
    public MyJsonBean<Map<String, Object>> validateFormula(
            @ApiParam(value = "公式ID", required = true) @PathVariable String formulaId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validateResult = formulaService.validateById(formulaId);
            result.setCode(1);
            result.setMsg("验证完成");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("验证公式异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量验证公式
     */
    @Operation(summary = "批量验证公式")
    @ApiOperation("批量验证公式")
    @PostMapping("/batch/validate")
    public MyJsonBean<Map<String, Object>> batchValidate(@RequestBody List<String> ids) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validateResult = formulaService.batchValidate(ids);
            result.setCode(1);
            result.setMsg("批量验证完成");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("批量验证公式异常", e);
            result.setCode(0);
            result.setMsg("批量验证失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出公式
     */
    @Operation(summary = "导出公式")
    @ApiOperation("导出公式")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportFormulas(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = formulaService.exportFormulas(params);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (Exception e) {
            log.error("导出公式异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出单个公式
     */
    @Operation(summary = "导出单个公式")
    @ApiOperation("导出单个公式")
    @GetMapping("/{formulaId}/export")
    public MyJsonBean<Map<String, Object>> exportSingle(
            @ApiParam(value = "公式ID", required = true) @PathVariable String formulaId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = formulaService.exportById(formulaId);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (Exception e) {
            log.error("导出公式异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取公式统计")
    @ApiOperation("获取公式统计")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("查询成功"); result.setData(formulaService.getStats());
        } catch (Exception e) { result.setCode(0); result.setMsg("查询失败：" + e.getMessage()); }
        return result;
    }

    /**
     * 导入公式
     */
    @Operation(summary = "导入公式(JSON)")
    @ApiOperation("导入公式(JSON)")
    @PostMapping("/import/batch")
    public MyJsonBean<Map<String, Object>> importFormulas(@RequestBody List<BudgetFormula> formulas) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> importResult = formulaService.importFormulas(formulas);
            result.setCode(1);
            result.setMsg("导入完成");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("导入公式异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }
}

