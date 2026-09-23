package com.management.accountant.controller;

import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算分析图表Controller
 */
@RestController
@Api(tags = {"NCV65全面预算-分析图表"})
@RequestMapping(value = "/accountant/budget/analysis/chart")
@Slf4j
public class BudgetAnalysisChartController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private com.management.accountant.service.BudgetAnalysisChartService chartService;

    @Operation(summary = "获取图表统计数据")
    @ApiOperation("获取图表统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = chartService.getChartStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取图表统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "分页查询图表列表")
    @ApiOperation("分页查询图表列表")
    @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> pageData = chartService.getChartPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageData);
        } catch (Exception e) {
            log.error("分页查询图表列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取图表类型列表")
    @ApiOperation("获取图表类型列表")
    @GetMapping("/types")
    public MyJsonBean<Map<String, Object>> getTypes() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> typesData = new HashMap<>();
            typesData.put("types", chartService.getChartTypes());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(typesData);
        } catch (Exception e) {
            log.error("获取图表类型列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取图表库")
    @ApiOperation("获取图表库")
    @GetMapping("/library")
    public MyJsonBean<Map<String, Object>> getLibrary() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> libraryData = new HashMap<>();
            libraryData.put("charts", chartService.getChartLibrary());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(libraryData);
        } catch (Exception e) {
            log.error("获取图表库失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出图表")
    @ApiOperation("导出图表")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportChart(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportData = new HashMap<>();
            exportData.put("fileName", "chart_export.xlsx");
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出图表失败", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除图表")
    @ApiOperation("删除图表")
    @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> deleteChart(@ApiParam(value = "图表ID", required = true) @PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            chartService.deleteChart(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除图表失败", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建图表")
    @ApiOperation("创建图表")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createChart(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = chartService.createChart(data);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("创建图表失败", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新图表")
    @ApiOperation("更新图表")
    @PutMapping("/update")
    public MyJsonBean<Map<String, Object>> updateChart(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = chartService.updateChart(data);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("更新图表失败", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取图表详情")
    @ApiOperation("获取图表详情")
    @GetMapping("/detail/{id}")
    public MyJsonBean<Map<String, Object>> getChartDetail(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> detail = chartService.getChartDetail(id);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(detail);
        } catch (Exception e) {
            log.error("获取图表详情失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取图表渲染数据")
    @ApiOperation("获取图表渲染数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = chartService.getChartData(params != null ? params : new HashMap<>());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}
