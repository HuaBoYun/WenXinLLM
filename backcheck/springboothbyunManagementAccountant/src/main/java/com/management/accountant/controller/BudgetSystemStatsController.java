package com.management.accountant.controller;

import com.management.accountant.service.BudgetSystemStatsService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.vo.result.BudgetSystemActivityVO;
import com.management.accountant.vo.result.BudgetSystemHealthVO;
import com.management.accountant.vo.result.BudgetSystemModuleVO;
import com.management.accountant.vo.result.BudgetSystemStatsVO;
import com.management.accountant.vo.result.BudgetSystemMatrixVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预算体系统计Controller
 * 
 * @description 预算体系管理统计数据接口
 * @author AI Assistant
 * @date 2025-01-30
 */
@RestController
@Api(tags = {"NCV65全面预算-预算体系统计"})
@RequestMapping(value = "/accountant/budget/system")
@Slf4j
public class BudgetSystemStatsController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetSystemStatsService budgetSystemStatsService;

    /**
     * 获取预算体系统计数据
     */
    @Operation(summary = "获取预算体系统计数据")
    @ApiOperation("获取预算体系统计数据")
    @GetMapping("/stats")
    public MyJsonBean<BudgetSystemStatsVO> getSystemStats() {
        MyJsonBean<BudgetSystemStatsVO> result = new MyJsonBean<>();
        try {
            BudgetSystemStatsVO stats = budgetSystemStatsService.getSystemStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取预算体系统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预算体系模块列表
     */
    @Operation(summary = "获取预算体系模块列表")
    @ApiOperation("获取预算体系模块列表")
    @GetMapping("/modules")
    public MyJsonBean<List<BudgetSystemModuleVO>> getSystemModules() {
        MyJsonBean<List<BudgetSystemModuleVO>> result = new MyJsonBean<>();
        try {
            List<BudgetSystemModuleVO> modules = budgetSystemStatsService.getSystemModules();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(modules);
        } catch (Exception e) {
            log.error("获取预算体系模块列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预算体系健康度
     */
    @Operation(summary = "获取预算体系健康度")
    @ApiOperation("获取预算体系健康度")
    @GetMapping("/health")
    public MyJsonBean<BudgetSystemHealthVO> getSystemHealth() {
        MyJsonBean<BudgetSystemHealthVO> result = new MyJsonBean<>();
        try {
            BudgetSystemHealthVO health = budgetSystemStatsService.getSystemHealth();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(health);
        } catch (Exception e) {
            log.error("获取预算体系健康度失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取最近活动记录
     */
    @Operation(summary = "获取最近活动记录")
    @ApiOperation("获取最近活动记录")
    @GetMapping("/activities")
    public MyJsonBean<List<BudgetSystemActivityVO>> getRecentActivities(
            @ApiParam(value = "限制数量", required = false) @RequestParam(required = false, defaultValue = "10") Integer limit) {
        MyJsonBean<List<BudgetSystemActivityVO>> result = new MyJsonBean<>();
        try {
            List<BudgetSystemActivityVO> activities = budgetSystemStatsService.getRecentActivities(limit);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(activities);
        } catch (Exception e) {
            log.error("获取最近活动记录失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预算体系矩阵数据
     */
    @Operation(summary = "获取预算体系矩阵数据")
    @ApiOperation("获取预算体系矩阵数据")
    @GetMapping("/matrix")
    public MyJsonBean<BudgetSystemMatrixVO> getSystemMatrix() {
        MyJsonBean<BudgetSystemMatrixVO> result = new MyJsonBean<>();
        try {
            BudgetSystemMatrixVO matrix = budgetSystemStatsService.getSystemMatrix();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(matrix);
        } catch (Exception e) {
            log.error("获取预算体系矩阵数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}