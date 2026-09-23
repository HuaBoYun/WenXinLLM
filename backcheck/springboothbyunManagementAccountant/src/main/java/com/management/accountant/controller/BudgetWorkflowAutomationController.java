package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetWorkflowAutomationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算自动化工作流Controller
 * 
 * @description 预算自动化工作流接口，支持流程定义、自动执行、任务调度、流程监控、流程优化
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-自动化工作流"})
@RequestMapping(value = "/accountant/advanced/workflow")
@Slf4j
public class BudgetWorkflowAutomationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetWorkflowAutomationService workflowAutomationService;

    /**
     * 定义工作流
     */
    @Operation(summary = "定义工作流")
    @ApiOperation("定义工作流")
    @PostMapping("/define")
    public MyJsonBean<Map<String, Object>> defineWorkflow(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> workflow = workflowAutomationService.defineWorkflow(params);
            result.setCode(1);
            result.setMsg("定义成功");
            result.setData(workflow);
        } catch (ServiceException ex) {
            log.error("定义工作流失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("定义工作流异常", e);
            result.setCode(0);
            result.setMsg("定义失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行工作流
     */
    @Operation(summary = "执行工作流")
    @ApiOperation("执行工作流")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeWorkflow(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> execution = workflowAutomationService.executeWorkflow(params);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(execution);
        } catch (ServiceException ex) {
            log.error("执行工作流失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行工作流异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 任务调度
     */
    @Operation(summary = "任务调度")
    @ApiOperation("任务调度")
    @PostMapping("/schedule")
    public MyJsonBean<Map<String, Object>> scheduleTask(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> schedule = workflowAutomationService.scheduleTask(params);
            result.setCode(1);
            result.setMsg("调度成功");
            result.setData(schedule);
        } catch (ServiceException ex) {
            log.error("任务调度失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("任务调度异常", e);
            result.setCode(0);
            result.setMsg("调度失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 流程监控
     */
    @Operation(summary = "流程监控")
    @ApiOperation("流程监控")
    @PostMapping("/monitor")
    public MyJsonBean<Map<String, Object>> monitorWorkflow(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> monitoring = workflowAutomationService.monitorWorkflow(params);
            result.setCode(1);
            result.setMsg("监控成功");
            result.setData(monitoring);
        } catch (ServiceException ex) {
            log.error("流程监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("流程监控异常", e);
            result.setCode(0);
            result.setMsg("监控失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 流程优化
     */
    @Operation(summary = "流程优化")
    @ApiOperation("流程优化")
    @PostMapping("/optimize")
    public MyJsonBean<Map<String, Object>> optimizeWorkflow(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> optimization = workflowAutomationService.optimizeWorkflow(params);
            result.setCode(1);
            result.setMsg("优化成功");
            result.setData(optimization);
        } catch (ServiceException ex) {
            log.error("流程优化失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("流程优化异常", e);
            result.setCode(0);
            result.setMsg("优化失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 停止工作流
     */
    @Operation(summary = "停止工作流")
    @ApiOperation("停止工作流")
    @PostMapping("/stop")
    public MyJsonBean<Map<String, Object>> stopWorkflow(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stopResult = workflowAutomationService.stopWorkflow(params);
            result.setCode(1);
            result.setMsg("停止成功");
            result.setData(stopResult);
        } catch (ServiceException ex) {
            log.error("停止工作流失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("停止工作流异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    // list/stats/run/stop/copy/export/delete 端点已在 BudgetAdvancedFeaturesController 中实现，此处不再重复注册
}

