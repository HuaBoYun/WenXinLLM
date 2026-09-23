package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetScenarioAnalysisService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算场景分析Controller
 * 
 * @description 预算场景分析接口，支持多场景对比、假设分析等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-场景分析"})
@RequestMapping(value = "/accountant/budget/scenario/analysis")
@Slf4j
public class BudgetScenarioAnalysisController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetScenarioAnalysisService scenarioAnalysisService;

    /**
     * 创建场景分析
     */
    @Operation(summary = "创建场景分析")
    @ApiOperation("创建场景分析")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createScenario(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> scenario = scenarioAnalysisService.createScenario(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(scenario);
        } catch (ServiceException ex) {
            log.error("创建场景分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建场景分析异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行场景分析
     */
    @Operation(summary = "执行场景分析")
    @ApiOperation("执行场景分析")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeScenarioAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysisResult = scenarioAnalysisService.executeScenarioAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysisResult);
        } catch (ServiceException ex) {
            log.error("执行场景分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行场景分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 对比场景
     */
    @Operation(summary = "对比场景")
    @ApiOperation("对比场景")
    @PostMapping("/compare")
    public MyJsonBean<Map<String, Object>> compareScenarios(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> compareResult = scenarioAnalysisService.compareScenarios(params);
            result.setCode(1);
            result.setMsg("对比成功");
            result.setData(compareResult);
        } catch (ServiceException ex) {
            log.error("对比场景失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("对比场景异常", e);
            result.setCode(0);
            result.setMsg("对比失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询场景分析列表
     */
    @Operation(summary = "分页查询场景分析列表")
    @ApiOperation("分页查询场景分析列表")
    @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> pageResult = scenarioAnalysisService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询场景分析列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出场景分析
     */
    @Operation(summary = "导出场景分析")
    @ApiOperation("导出场景分析")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportScenario(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportData = new java.util.HashMap<>();
            exportData.put("fileName", "场景分析.xlsx");
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出场景分析异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制场景
     */
    @Operation(summary = "复制场景")
    @ApiOperation("复制场景")
    @PostMapping("/copy/{id}")
    public MyJsonBean<Map<String, Object>> copyScenario(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> copyResult = scenarioAnalysisService.copyScenario(id);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copyResult);
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("复制场景异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新场景分析
     */
    @Operation(summary = "更新场景分析")
    @ApiOperation("更新场景分析")
    @PutMapping("/update")
    public MyJsonBean<Map<String, Object>> updateScenario(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> updateResult = scenarioAnalysisService.updateScenario(params);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updateResult);
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新场景分析异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取组织选项列表
     */
    @Operation(summary = "获取组织选项列表")
    @ApiOperation("获取组织选项列表")
    @GetMapping("/organizations")
    public MyJsonBean<Object> getOrganizations() {
        MyJsonBean<Object> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(scenarioAnalysisService.getOrganizations());
        } catch (Exception e) {
            log.error("获取组织列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出单个场景分析
     */
    @Operation(summary = "导出单个场景分析")
    @ApiOperation("导出单个场景分析")
    @GetMapping("/export/{id}")
    public MyJsonBean<Map<String, Object>> exportSingle(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportData = new java.util.HashMap<>();
            exportData.put("fileName", "场景分析_" + id + ".xlsx");
            exportData.put("id", id);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出单个场景分析异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除场景
     */
    @Operation(summary = "删除场景")
    @ApiOperation("删除场景")
    @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> deleteScenario(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            scenarioAnalysisService.deleteScenario(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除场景异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取场景分析统计数据")
    @ApiOperation("获取场景分析统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = scenarioAnalysisService.getScenarioStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取场景分析统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取场景分析图表数据")
    @ApiOperation("获取场景分析图表数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = scenarioAnalysisService.getScenarioChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取场景分析图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

