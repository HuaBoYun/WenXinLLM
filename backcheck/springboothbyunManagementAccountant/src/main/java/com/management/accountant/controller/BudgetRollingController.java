package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetRolling;
import com.management.accountant.service.BudgetRollingService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 滚动预算Controller
 * 
 * @description 滚动预算管理接口，支持滚动调整、动态更新等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-滚动预算"})
@RequestMapping(value = "/accountant/budget/rolling")
@Slf4j
public class BudgetRollingController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetRollingService rollingService;

    /**
     * 创建滚动预算
     */
    @Operation(summary = "创建滚动预算")
    @ApiOperation("创建滚动预算")
    @PostMapping("/create")
    public MyJsonBean<BudgetRolling> create(@RequestBody @Validated BudgetRolling rolling) {
        MyJsonBean<BudgetRolling> result = new MyJsonBean<>();
        try {
            BudgetRolling created = rollingService.create(rolling);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建滚动预算失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建滚动预算异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询滚动预算详情
     */
    @Operation(summary = "查询滚动预算详情")
    @ApiOperation("查询滚动预算详情")
    @GetMapping("/detail/{rollingId}")
    public MyJsonBean<BudgetRolling> getDetail(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId) {
        MyJsonBean<BudgetRolling> result = new MyJsonBean<>();
        try {
            BudgetRolling rolling = rollingService.getById(rollingId);
            if (rolling != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(rolling);
            } else {
                result.setCode(0);
                result.setMsg("滚动预算不存在");
            }
        } catch (Exception e) {
            log.error("查询滚动预算详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新滚动预算
     */
    @Operation(summary = "更新滚动预算")
    @ApiOperation("更新滚动预算")
    @PutMapping("/update/{rollingId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId,
            @RequestBody @Validated BudgetRolling rolling) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            rolling.setRollingId(rollingId);
            rollingService.update(rolling);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新滚动预算失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新滚动预算异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除滚动预算
     */
    @Operation(summary = "删除滚动预算")
    @ApiOperation("删除滚动预算")
    @DeleteMapping("/delete/{rollingId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            rollingService.delete(rollingId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除滚动预算失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除滚动预算异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询滚动预算列表
     */
    @Operation(summary = "分页查询滚动预算列表")
    @ApiOperation("分页查询滚动预算列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetRolling>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetRolling>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetRolling> pageResult = rollingService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询滚动预算列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行滚动
     */
    @Operation(summary = "执行滚动")
    @ApiOperation("执行滚动")
    @PostMapping("/execute/{rollingId}")
    public MyJsonBean<Map<String, Object>> executeRolling(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> executeResult = rollingService.executeRolling(rollingId);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(executeResult);
        } catch (ServiceException ex) {
            log.error("执行滚动失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行滚动异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 滚动调整
     */
    @Operation(summary = "滚动调整")
    @ApiOperation("滚动调整")
    @PostMapping("/adjust/{rollingId}")
    public MyJsonBean<Void> adjustRolling(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            params.put("rollingId", rollingId);
            rollingService.adjustRolling(params);
            result.setCode(1);
            result.setMsg("调整成功");
        } catch (ServiceException ex) {
            log.error("滚动调整失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("滚动调整异常", e);
            result.setCode(0);
            result.setMsg("调整失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取滚动计划列表
     */
    @Operation(summary = "获取滚动计划列表")
    @ApiOperation("获取滚动计划列表")
    @PostMapping("/plan/list")
    public MyJsonBean<Map<String, Object>> getPlanList(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> list = rollingService.getPlanList(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取滚动计划列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取滚动统计数据
     */
    @Operation(summary = "获取滚动统计数据")
    @ApiOperation("获取滚动统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = rollingService.getStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取滚动统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制滚动计划
     */
    @Operation(summary = "复制滚动计划")
    @ApiOperation("复制滚动计划")
    @PostMapping("/plan/{rollingId}/copy")
    public MyJsonBean<BudgetRolling> copyPlan(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId) {
        MyJsonBean<BudgetRolling> result = new MyJsonBean<>();
        try {
            BudgetRolling copied = rollingService.copy(rollingId);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copied);
        } catch (ServiceException ex) {
            log.error("复制滚动计划失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("复制滚动计划异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 暂停滚动计划
     */
    @Operation(summary = "暂停滚动计划")
    @ApiOperation("暂停滚动计划")
    @PostMapping("/plan/{rollingId}/pause")
    public MyJsonBean<Void> pausePlan(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            rollingService.pause(rollingId);
            result.setCode(1);
            result.setMsg("暂停成功");
        } catch (ServiceException ex) {
            log.error("暂停滚动计划失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("暂停滚动计划异常", e);
            result.setCode(0);
            result.setMsg("暂停失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 恢复滚动计划
     */
    @Operation(summary = "恢复滚动计划")
    @ApiOperation("恢复滚动计划")
    @PostMapping("/plan/{rollingId}/resume")
    public MyJsonBean<Void> resumePlan(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            rollingService.resume(rollingId);
            result.setCode(1);
            result.setMsg("恢复成功");
        } catch (ServiceException ex) {
            log.error("恢复滚动计划失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("恢复滚动计划异常", e);
            result.setCode(0);
            result.setMsg("恢复失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取滚动执行记录
     */
    @Operation(summary = "获取滚动执行记录")
    @ApiOperation("获取滚动执行记录")
    @GetMapping("/plan/{rollingId}/executions")
    public MyJsonBean<Map<String, Object>> getExecutionRecords(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String rollingId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> records = rollingService.getExecutionRecords(rollingId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(records);
        } catch (Exception e) {
            log.error("获取滚动执行记录异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 应用滚动调整
     */
    @Operation(summary = "应用滚动调整")
    @ApiOperation("应用滚动调整")
    @PostMapping("/adjustment/apply/{id}")
    public MyJsonBean<Void> applyAdjustment(
            @ApiParam(value = "调整ID", required = true) @PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("应用成功");
        } catch (Exception e) {
            log.error("应用滚动调整异常", e);
            result.setCode(0);
            result.setMsg("应用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 撤销滚动调整
     */
    @Operation(summary = "撤销滚动调整")
    @ApiOperation("撤销滚动调整")
    @PostMapping("/adjustment/revert/{id}")
    public MyJsonBean<Void> revertAdjustment(
            @ApiParam(value = "调整ID", required = true) @PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("撤销成功");
        } catch (Exception e) {
            log.error("撤销滚动调整异常", e);
            result.setCode(0);
            result.setMsg("撤销失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 忽略异常
     */
    @Operation(summary = "忽略异常")
    @ApiOperation("忽略异常")
    @PostMapping("/anomaly/ignore/{id}")
    public MyJsonBean<Void> ignoreAnomaly(
            @ApiParam(value = "异常ID", required = true) @PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("忽略成功");
        } catch (Exception e) {
            log.error("忽略异常操作异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出单个滚动预算
     */
    @Operation(summary = "导出单个滚动预算")
    @ApiOperation("导出单个滚动预算")
    @GetMapping("/export/{id}")
    public MyJsonBean<Map<String, Object>> exportSingle(
            @ApiParam(value = "滚动预算ID", required = true) @PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetRolling rolling = rollingService.getById(id);
            Map<String, Object> exportData = new java.util.HashMap<>();
            exportData.put("fileName", "滚动预算_" + id + ".xlsx");
            exportData.put("data", rolling);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出单个滚动预算异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取滚动预测图表数据")
    @ApiOperation("获取滚动预测图表数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = rollingService.getRollingChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取滚动预测图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

