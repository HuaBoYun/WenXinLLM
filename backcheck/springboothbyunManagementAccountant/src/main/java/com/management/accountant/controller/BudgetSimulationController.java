package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetSimulationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算模拟Controller
 * 
 * @description 预算模拟接口，支持场景模拟、压力测试、蒙特卡洛模拟等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算模拟"})
@RequestMapping(value = "/accountant/advanced/simulation")
@Slf4j
public class BudgetSimulationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetSimulationService simulationService;

    // /create 端点已在 BudgetAdvancedFeaturesController 中实现

    /**
     * 执行模拟
     */
    @Operation(summary = "执行模拟")
    @ApiOperation("执行模拟")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeSimulation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> simulationResult = simulationService.executeSimulation(params);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(simulationResult);
        } catch (ServiceException ex) {
            log.error("执行模拟失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行模拟异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 压力测试
     */
    @Operation(summary = "压力测试")
    @ApiOperation("压力测试")
    @PostMapping("/stress-test")
    public MyJsonBean<Map<String, Object>> stressTest(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> testResult = simulationService.stressTest(params);
            result.setCode(1);
            result.setMsg("测试成功");
            result.setData(testResult);
        } catch (ServiceException ex) {
            log.error("压力测试失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("压力测试异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 蒙特卡洛模拟
     */
    @Operation(summary = "蒙特卡洛模拟")
    @ApiOperation("蒙特卡洛模拟")
    @PostMapping("/monte-carlo")
    public MyJsonBean<Map<String, Object>> monteCarloSimulation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> mcResult = simulationService.monteCarloSimulation(params);
            result.setCode(1);
            result.setMsg("模拟成功");
            result.setData(mcResult);
        } catch (ServiceException ex) {
            log.error("蒙特卡洛模拟失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("蒙特卡洛模拟异常", e);
            result.setCode(0);
            result.setMsg("模拟失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取模拟报告
     */
    @Operation(summary = "获取模拟报告")
    @ApiOperation("获取模拟报告")
    @PostMapping("/report")
    public MyJsonBean<Map<String, Object>> getSimulationReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = simulationService.getSimulationReport(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("获取模拟报告失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取模拟报告异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // list/stats/update/delete/run/stop/copy/export/scenarios/logs 端点已在 BudgetAdvancedFeaturesController 中实现
}
