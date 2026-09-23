package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetFormulaTrackingService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算公式追踪Controller
 *
 * @description 预算公式追踪接口，支持公式依赖分析、影响范围追踪等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-公式追踪"})
@RequestMapping(value = "/accountant/budget/formula/tracking")
@Slf4j
public class BudgetFormulaTrackingController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetFormulaTrackingService formulaTrackingService;

    /**
     * 创建追踪任务
     */
    @Operation(summary = "创建追踪任务")
    @ApiOperation("创建追踪任务")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> create(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> task = formulaTrackingService.create(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(task);
        } catch (ServiceException ex) {
            log.error("创建追踪任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建追踪任务异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新追踪任务
     */
    @Operation(summary = "更新追踪任务")
    @ApiOperation("更新追踪任务")
    @PutMapping("/update/{taskId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            formulaTrackingService.update(taskId, params);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新追踪任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新追踪任务异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除追踪任务
     */
    @Operation(summary = "删除追踪任务")
    @ApiOperation("删除追踪任务")
    @DeleteMapping("/delete/{taskId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            formulaTrackingService.delete(taskId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除追踪任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除追踪任务异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询追踪任务
     */
    @Operation(summary = "分页查询追踪任务")
    @ApiOperation("分页查询追踪任务")
    @PostMapping("/page")
    public MyJsonBean<PageResult<Map<String, Object>>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<PageResult<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            PageResult<Map<String, Object>> pageResult = formulaTrackingService.page(current, size, params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询追踪任务异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行追踪任务
     */
    @Operation(summary = "执行追踪任务")
    @ApiOperation("执行追踪任务")
    @PostMapping("/execute/{taskId}")
    public MyJsonBean<Map<String, Object>> execute(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> execResult = formulaTrackingService.execute(taskId);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(execResult);
        } catch (ServiceException ex) {
            log.error("执行追踪任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行追踪任务异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 停止追踪任务
     */
    @Operation(summary = "停止追踪任务")
    @ApiOperation("停止追踪任务")
    @PostMapping("/stop/{taskId}")
    public MyJsonBean<Void> stop(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            formulaTrackingService.stop(taskId);
            result.setCode(1);
            result.setMsg("停止成功");
        } catch (ServiceException ex) {
            log.error("停止追踪任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("停止追踪任务异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制追踪任务
     */
    @Operation(summary = "复制追踪任务")
    @ApiOperation("复制追踪任务")
    @PostMapping("/copy/{taskId}")
    public MyJsonBean<Map<String, Object>> copy(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> task = formulaTrackingService.copy(taskId);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(task);
        } catch (ServiceException ex) {
            log.error("复制追踪任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("复制追踪任务异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取追踪任务详情
     */
    @Operation(summary = "获取追踪任务详情")
    @ApiOperation("获取追踪任务详情")
    @GetMapping("/detail/{taskId}")
    public MyJsonBean<Map<String, Object>> getDetail(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> detail = formulaTrackingService.getDetail(taskId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(detail);
        } catch (ServiceException ex) {
            log.error("获取追踪任务详情失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取追踪任务详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取追踪任务日志
     */
    @Operation(summary = "获取追踪任务日志")
    @ApiOperation("获取追踪任务日志")
    @GetMapping("/logs/{taskId}")
    public MyJsonBean<Map<String, Object>> getLogs(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> logs = formulaTrackingService.getLogs(taskId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(logs);
        } catch (ServiceException ex) {
            log.error("获取追踪任务日志失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取追踪任务日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 追踪公式依赖
     */
    @Operation(summary = "追踪公式依赖")
    @ApiOperation("追踪公式依赖")
    @GetMapping("/dependencies/{formulaId}")
    public MyJsonBean<Map<String, Object>> trackDependencies(
            @ApiParam(value = "公式ID", required = true) @PathVariable String formulaId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> dependencies = formulaTrackingService.trackDependencies(formulaId);
            result.setCode(1);
            result.setMsg("追踪成功");
            result.setData(dependencies);
        } catch (ServiceException ex) {
            log.error("追踪公式依赖失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("追踪公式依赖异常", e);
            result.setCode(0);
            result.setMsg("追踪失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分析影响范围
     */
    @Operation(summary = "分析影响范围")
    @ApiOperation("分析影响范围")
    @PostMapping("/impact/analysis")
    public MyJsonBean<Map<String, Object>> analyzeImpact(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> impactAnalysis = formulaTrackingService.analyzeImpact(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(impactAnalysis);
        } catch (ServiceException ex) {
            log.error("分析影响范围失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("分析影响范围异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取公式链路
     */
    @Operation(summary = "获取公式链路")
    @ApiOperation("获取公式链路")
    @GetMapping("/chain/{formulaId}")
    public MyJsonBean<Map<String, Object>> getFormulaChain(
            @ApiParam(value = "公式ID", required = true) @PathVariable String formulaId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chain = formulaTrackingService.getFormulaChain(formulaId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chain);
        } catch (ServiceException ex) {
            log.error("获取公式链路失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取公式链路异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 验证公式循环依赖
     */
    @Operation(summary = "验证公式循环依赖")
    @ApiOperation("验证公式循环依赖")
    @PostMapping("/validate/circular")
    public MyJsonBean<Map<String, Object>> validateCircularDependency(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validation = formulaTrackingService.validateCircularDependency(params);
            result.setCode(1);
            result.setMsg("验证成功");
            result.setData(validation);
        } catch (ServiceException ex) {
            log.error("验证公式循环依赖失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("验证公式循环依赖异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成依赖关系图
     */
    @Operation(summary = "生成依赖关系图")
    @ApiOperation("生成依赖关系图")
    @PostMapping("/dependency/graph")
    public MyJsonBean<Map<String, Object>> generateDependencyGraph(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> graph = formulaTrackingService.generateDependencyGraph(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(graph);
        } catch (ServiceException ex) {
            log.error("生成依赖关系图失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成依赖关系图异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取公式追踪统计数据
     */
    @Operation(summary = "获取公式追踪统计数据")
    @ApiOperation("获取公式追踪统计数据")
    @PostMapping("/dependencies/stats")
    public MyJsonBean<Map<String, Object>> getDependenciesStats(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = formulaTrackingService.getDependenciesStats(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取公式追踪统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取可用公式列表（POST方法）
     */
    @Operation(summary = "获取可用公式列表")
    @ApiOperation("获取可用公式列表")
    @PostMapping("/chain")
    public MyJsonBean<Map<String, Object>> getFormulaChainPost(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chain = formulaTrackingService.getAvailableFormulas(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chain);
        } catch (Exception e) {
            log.error("获取可用公式列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取任务公式列表
     */
    @Operation(summary = "获取任务公式列表")
    @ApiOperation("获取任务公式列表")
    @GetMapping("/formulas/{taskId}")
    public MyJsonBean<Map<String, Object>> getTaskFormulas(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> formulas = formulaTrackingService.getTaskFormulas(taskId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(formulas);
        } catch (Exception e) {
            log.error("获取任务公式列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

