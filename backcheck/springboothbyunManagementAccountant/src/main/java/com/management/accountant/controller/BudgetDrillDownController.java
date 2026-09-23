package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetDrillDownService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算穿透查询Controller
 * 
 * @description 预算穿透查询接口，支持多维度穿透、明细追溯等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-穿透查询"})
@RequestMapping(value = "/accountant/budget/drilldown")
@Slf4j
public class BudgetDrillDownController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetDrillDownService drillDownService;

    /**
     * 执行穿透查询
     */
    @Operation(summary = "执行穿透查询")
    @ApiOperation("执行穿透查询")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeDrillDown(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> drillDownResult = drillDownService.executeDrillDown(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(drillDownResult);
        } catch (ServiceException ex) {
            log.error("执行穿透查询失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行穿透查询异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取穿透路径
     */
    @Operation(summary = "获取穿透路径")
    @ApiOperation("获取穿透路径")
    @PostMapping("/path")
    public MyJsonBean<Map<String, Object>> getDrillDownPath(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> pathData = drillDownService.getDrillDownPath(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pathData);
        } catch (Exception e) {
            log.error("获取穿透路径异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取明细数据
     */
    @Operation(summary = "获取明细数据")
    @ApiOperation("获取明细数据")
    @PostMapping("/detail")
    public MyJsonBean<Map<String, Object>> getDetailData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> detailData = drillDownService.getDetailData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(detailData);
        } catch (Exception e) {
            log.error("获取明细数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出穿透数据
     */
    @Operation(summary = "导出穿透数据")
    @ApiOperation("导出穿透数据")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportDrillDownData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = drillDownService.exportDrillDownData(params);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (ServiceException ex) {
            log.error("导出穿透数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("导出穿透数据异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取穿透查询列表
     */
    @Operation(summary = "获取穿透查询列表")
    @ApiOperation("获取穿透查询列表")
    @PostMapping("/list")
    public MyJsonBean<Map<String, Object>> getDrillDownList(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> list = drillDownService.getDrillDownList(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取穿透查询列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取穿透查询统计数据
     */
    @Operation(summary = "获取穿透查询统计数据")
    @ApiOperation("获取穿透查询统计数据")
    @PostMapping("/stats")
    public MyJsonBean<Map<String, Object>> getDrillDownStats(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = drillDownService.getDrillDownStats(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取穿透查询统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取可用维度列表
     */
    @Operation(summary = "获取可用维度列表")
    @ApiOperation("获取可用维度列表")
    @PostMapping("/dimensions")
    public MyJsonBean<Map<String, Object>> getAvailableDimensions(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> dimensions = drillDownService.getAvailableDimensions(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(dimensions);
        } catch (Exception e) {
            log.error("获取可用维度列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 创建穿透查询
     */
    @Operation(summary = "创建穿透查询")
    @ApiOperation("创建穿透查询")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createDrillDown(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> created = drillDownService.createDrillDown(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建穿透查询失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建穿透查询异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新穿透查询
     */
    @Operation(summary = "更新穿透查询")
    @ApiOperation("更新穿透查询")
    @PutMapping("/update/{queryId}")
    public MyJsonBean<Void> updateDrillDown(
            @PathVariable String queryId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            params.put("queryId", queryId);
            drillDownService.updateDrillDown(params);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新穿透查询失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新穿透查询异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除穿透查询
     */
    @Operation(summary = "删除穿透查询")
    @ApiOperation("删除穿透查询")
    @DeleteMapping("/delete/{queryId}")
    public MyJsonBean<Void> deleteDrillDown(@PathVariable String queryId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            drillDownService.deleteDrillDown(queryId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除穿透查询失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除穿透查询异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制穿透查询
     */
    @Operation(summary = "复制穿透查询")
    @ApiOperation("复制穿透查询")
    @PostMapping("/copy/{queryId}")
    public MyJsonBean<Map<String, Object>> copyDrillDown(@PathVariable String queryId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> copyResult = drillDownService.copyDrillDown(queryId);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copyResult);
        } catch (Exception e) {
            log.error("复制穿透查询异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行穿透查询（按ID）
     */
    @Operation(summary = "执行穿透查询（按ID）")
    @ApiOperation("执行穿透查询（按ID）")
    @PostMapping("/run/{queryId}")
    public MyJsonBean<Map<String, Object>> runDrillDown(@PathVariable String queryId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> runResult = drillDownService.runDrillDown(queryId);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(runResult);
        } catch (Exception e) {
            log.error("执行穿透查询异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 停止穿透查询
     */
    @Operation(summary = "停止穿透查询")
    @ApiOperation("停止穿透查询")
    @PostMapping("/stop/{queryId}")
    public MyJsonBean<Void> stopDrillDown(@PathVariable String queryId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            drillDownService.stopDrillDown(queryId);
            result.setCode(1);
            result.setMsg("停止成功");
        } catch (Exception e) {
            log.error("停止穿透查询异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 优化穿透查询
     */
    @Operation(summary = "优化穿透查询")
    @ApiOperation("优化穿透查询")
    @PostMapping("/optimize/{queryId}")
    public MyJsonBean<Map<String, Object>> optimizeDrillDown(@PathVariable String queryId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> optimizeResult = drillDownService.optimizeDrillDown(queryId);
            result.setCode(1);
            result.setMsg("优化成功");
            result.setData(optimizeResult);
        } catch (Exception e) {
            log.error("优化穿透查询异常", e);
            result.setCode(0);
            result.setMsg("优化失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出穿透查询结果（按ID）
     */
    @Operation(summary = "导出穿透查询结果（按ID）")
    @ApiOperation("导出穿透查询结果（按ID）")
    @GetMapping("/export/{queryId}")
    public MyJsonBean<Map<String, Object>> exportDrillDownById(@PathVariable String queryId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = drillDownService.exportDrillDownById(queryId);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (Exception e) {
            log.error("导出穿透查询结果异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取穿透查询结果
     */
    @Operation(summary = "获取穿透查询结果")
    @ApiOperation("获取穿透查询结果")
    @GetMapping("/result/{queryId}")
    public MyJsonBean<Map<String, Object>> getDrillDownResult(@PathVariable String queryId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> queryResult = drillDownService.getDrillDownResult(queryId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(queryResult);
        } catch (Exception e) {
            log.error("获取穿透查询结果异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取穿透查询日志
     */
    @Operation(summary = "获取穿透查询日志")
    @ApiOperation("获取穿透查询日志")
    @GetMapping("/logs/{queryId}")
    public MyJsonBean<Map<String, Object>> getDrillDownLogs(@PathVariable String queryId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> logs = drillDownService.getDrillDownLogs(queryId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(logs);
        } catch (Exception e) {
            log.error("获取穿透查询日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

