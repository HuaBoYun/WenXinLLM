package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetIndicator;
import com.management.accountant.service.BudgetIndicatorService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 预算指标管理Controller
 * 
 * @description 预算指标管理接口，支持多种指标类型和计算方式
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-指标管理"})
@RequestMapping(value = "/accountant/budget/indicator")
@Slf4j
public class BudgetIndicatorController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIndicatorService indicatorService;

    /**
     * 创建指标
     */
    @Operation(summary = "创建指标")
    @ApiOperation("创建指标")
    @PostMapping("/create")
    public MyJsonBean<BudgetIndicator> create(@RequestBody @Validated BudgetIndicator indicator) {
        MyJsonBean<BudgetIndicator> result = new MyJsonBean<>();
        try {
            BudgetIndicator created = indicatorService.create(indicator);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建指标异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询指标详情
     */
    @Operation(summary = "查询指标详情")
    @ApiOperation("查询指标详情")
    @GetMapping("/detail/{indicatorId}")
    public MyJsonBean<BudgetIndicator> getDetail(
            @ApiParam(value = "指标ID", required = true) @PathVariable String indicatorId) {
        MyJsonBean<BudgetIndicator> result = new MyJsonBean<>();
        try {
            BudgetIndicator indicator = indicatorService.getById(indicatorId);
            if (indicator != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(indicator);
            } else {
                result.setCode(0);
                result.setMsg("指标不存在");
            }
        } catch (Exception e) {
            log.error("查询指标详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新指标
     */
    @Operation(summary = "更新指标")
    @ApiOperation("更新指标")
    @PutMapping("/update/{indicatorId}")
    public MyJsonBean<BudgetIndicator> update(
            @ApiParam(value = "指标ID", required = true) @PathVariable String indicatorId,
            @RequestBody @Validated BudgetIndicator indicator) {
        MyJsonBean<BudgetIndicator> result = new MyJsonBean<>();
        try {
            indicator.setIndicatorId(indicatorId);
            BudgetIndicator updated = indicatorService.update(indicator);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (ServiceException ex) {
            log.error("更新指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新指标异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除指标
     */
    @Operation(summary = "删除指标")
    @ApiOperation("删除指标")
    @DeleteMapping("/delete/{indicatorId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "指标ID", required = true) @PathVariable String indicatorId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            indicatorService.delete(indicatorId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除指标异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询指标列表
     */
    @Operation(summary = "分页查询指标列表")
    @ApiOperation("分页查询指标列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetIndicator>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetIndicator>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetIndicator> pageResult = indicatorService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询指标列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取指标树结构
     */
    @Operation(summary = "获取指标树结构")
    @ApiOperation("获取指标树结构")
    @GetMapping("/tree")
    public MyJsonBean<List<Map<String, Object>>> getTree() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> tree = indicatorService.getTree();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tree);
        } catch (Exception e) {
            log.error("获取指标树结构异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除指标
     */
    @Operation(summary = "批量删除指标")
    @ApiOperation("批量删除指标")
    @DeleteMapping("/batch-delete")
    public MyJsonBean<Void> batchDelete(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要删除的数据");
                return result;
            }
            indicatorService.batchDelete(ids);
            result.setCode(1);
            result.setMsg("批量删除成功");
        } catch (ServiceException ex) {
            log.error("批量删除指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量删除指标异常", e);
            result.setCode(0);
            result.setMsg("批量删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量启用指标
     */
    @Operation(summary = "批量启用指标")
    @ApiOperation("批量启用指标")
    @PostMapping("/batch-enable")
    public MyJsonBean<Void> batchEnable(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要启用的数据");
                return result;
            }
            indicatorService.batchEnable(ids);
            result.setCode(1);
            result.setMsg("批量启用成功");
        } catch (ServiceException ex) {
            log.error("批量启用指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量启用指标异常", e);
            result.setCode(0);
            result.setMsg("批量启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量禁用指标
     */
    @Operation(summary = "批量禁用指标")
    @ApiOperation("批量禁用指标")
    @PostMapping("/batch-disable")
    public MyJsonBean<Void> batchDisable(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要禁用的数据");
                return result;
            }
            indicatorService.batchDisable(ids);
            result.setCode(1);
            result.setMsg("批量禁用成功");
        } catch (ServiceException ex) {
            log.error("批量禁用指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量禁用指标异常", e);
            result.setCode(0);
            result.setMsg("批量禁用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用指标
     */
    @Operation(summary = "启用指标")
    @ApiOperation("启用指标")
    @PutMapping("/enable/{indicatorId}")
    public MyJsonBean<Void> enable(
            @ApiParam(value = "指标ID", required = true) @PathVariable String indicatorId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            indicatorService.enable(indicatorId);
            result.setCode(1);
            result.setMsg("启用成功");
        } catch (Exception e) {
            log.error("启用指标异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 禁用指标
     */
    @Operation(summary = "禁用指标")
    @ApiOperation("禁用指标")
    @PutMapping("/disable/{indicatorId}")
    public MyJsonBean<Void> disable(
            @ApiParam(value = "指标ID", required = true) @PathVariable String indicatorId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            indicatorService.disable(indicatorId);
            result.setCode(1);
            result.setMsg("禁用成功");
        } catch (Exception e) {
            log.error("禁用指标异常", e);
            result.setCode(0);
            result.setMsg("禁用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导入指标
     */
    @Operation(summary = "导入指标")
    @ApiOperation("导入指标")
    @PostMapping("/excel-import")
    public MyJsonBean<Map<String, Object>> importIndicators(@RequestParam("file") MultipartFile file) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> importResult = indicatorService.importIndicators(file);
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (ServiceException ex) {
            log.error("导入指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("导入指标异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出指标
     */
    @Operation(summary = "导出指标")
    @ApiOperation("导出指标")
    @GetMapping("/excel-export")
    public void exportIndicators(HttpServletResponse response) {
        try {
            indicatorService.exportIndicators(response);
        } catch (Exception e) {
            log.error("导出指标异常", e);
            throw new ServiceException("导出失败：" + e.getMessage());
        }
    }

    /**
     * 验证公式
     */
    @Operation(summary = "验证公式")
    @ApiOperation("验证公式")
    @PostMapping("/validate-formula")
    public MyJsonBean<Map<String, Object>> validateFormula(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String formula = (String) params.get("formula");
            Map<String, Object> validationResult = indicatorService.validateFormula(formula);
            result.setCode(1);
            result.setMsg("验证成功");
            result.setData(validationResult);
        } catch (ServiceException ex) {
            log.error("验证公式失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("验证公式异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 检查编码是否存在
     */
    @Operation(summary = "检查编码是否存在")
    @ApiOperation("检查编码是否存在")
    @GetMapping("/check-code/{code}")
    public MyJsonBean<Boolean> checkCode(
            @ApiParam(value = "指标编码", required = true) @PathVariable String code) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            boolean exists = indicatorService.checkCodeExists(code);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(exists);
        } catch (Exception e) {
            log.error("检查编码是否存在异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

