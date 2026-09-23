package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntegrationMonitorService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算集成监控Controller
 * 
 * @description 预算集成监控接口，支持集成状态监控、性能监控、告警管理
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-集成监控"})
@RequestMapping(value = "/accountant/integration")
@Slf4j
public class BudgetIntegrationMonitorController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIntegrationMonitorService integrationMonitorService;

    /**
     * 获取集成状态概览
     */
    @ApiOperation("获取集成状态概览")
    @GetMapping("/status/overview")
    public MyJsonBean<List<Map<String, Object>>> getIntegrationStatusOverview(
            @RequestParam(required = false) String timeRange) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            if (timeRange != null) {
                params.put("timeRange", timeRange);
            }
            Map<String, Object> overview = integrationMonitorService.getMonitorOverview(params);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> statusList = (List<Map<String, Object>>) overview.get("statusList");
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statusList != null ? statusList : new ArrayList<>());
        } catch (ServiceException ex) {
            log.error("获取集成状态概览失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取集成状态概览异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取监控统计
     */
    @ApiOperation("获取监控统计")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getMonitorStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = integrationMonitorService.getMonitorOverview(new HashMap<>());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (ServiceException ex) {
            log.error("获取监控统计失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取监控统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询集成日志（告警列表）
     */
    @ApiOperation("分页查询集成日志")
    @GetMapping("/log/page")
    public MyJsonBean<List<Map<String, Object>>> getIntegrationLogPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "20") Integer size) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("current", current);
            params.put("size", size);
            Map<String, Object> alert = integrationMonitorService.manageAlert(params);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> alertList = (List<Map<String, Object>>) alert.get("alertList");
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(alertList != null ? alertList : new ArrayList<>());
        } catch (ServiceException ex) {
            log.error("查询集成日志失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("查询集成日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取性能指标
     */
    @ApiOperation("获取性能指标")
    @GetMapping("/performance-metrics")
    public MyJsonBean<Map<String, Object>> getPerformanceMetrics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> performance = integrationMonitorService.monitorPerformance(new HashMap<>());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(performance);
        } catch (ServiceException ex) {
            log.error("获取性能指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取性能指标异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 以下为支持 /accountant/integration/log 路径的接口 ====================

    /**
     * 获取集成日志详情 (integration路径)
     */
    @ApiOperation("获取集成日志详情")
    @GetMapping("/log/{logId}")
    public MyJsonBean<Map<String, Object>> getIntegrationLogDetail(@PathVariable String logId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> logDetail = integrationMonitorService.getLogDetail(logId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(logDetail);
        } catch (ServiceException ex) {
            log.error("获取集成日志详情失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取集成日志详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

