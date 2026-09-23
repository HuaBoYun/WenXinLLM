package com.financial.sharing.dataCollection.controller;

import com.financial.sharing.dataCollection.dto.*;
import com.financial.sharing.dataCollection.service.CollectionMonitorService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.UserUtils;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 归集监控控制器
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@RestController
@RequestMapping("/financialSharing/dataCollection/monitor")
public class CollectionMonitorController {

    @Autowired
    private CollectionMonitorService collectionMonitorService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 获取整体监控统计
     *
     * @return 统计数据
     */
    @PostMapping("/getOverallStatistics")
    public MyJsonBean<CollectionMonitorStatistics> getOverallStatistics() {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            CollectionMonitorStatistics statistics = collectionMonitorService.getOverallStatistics(orgId);
            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取整体监控统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取任务执行趋势
     *
     * @param days 天数
     * @return 趋势数据列表
     */
    @PostMapping("/getTaskExecutionTrend")
    public MyJsonBean<List<TaskExecutionTrend>> getTaskExecutionTrend(@RequestParam(defaultValue = "7") Integer days) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            List<TaskExecutionTrend> trendList = collectionMonitorService.getTaskExecutionTrend(orgId, days);
            return MyJsonBean.successData("查询成功", trendList);
        } catch (Exception e) {
            log.error("获取任务执行趋势失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取任务状态分布
     *
     * @return 状态分布列表
     */
    @PostMapping("/getTaskStatusDistribution")
    public MyJsonBean<List<TaskStatusDistribution>> getTaskStatusDistribution() {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            List<TaskStatusDistribution> distributionList = collectionMonitorService.getTaskStatusDistribution(orgId);
            return MyJsonBean.successData("查询成功", distributionList);
        } catch (Exception e) {
            log.error("获取任务状态分布失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取失败任务TOP10
     *
     * @param days 统计天数
     * @return 失败任务列表
     */
    @PostMapping("/getTopFailedTasks")
    public MyJsonBean<List<FailedTaskStatistics>> getTopFailedTasks(@RequestParam(defaultValue = "30") Integer days) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            List<FailedTaskStatistics> statisticsList = collectionMonitorService.getTopFailedTasks(orgId, days);
            return MyJsonBean.successData("查询成功", statisticsList);
        } catch (Exception e) {
            log.error("获取失败任务TOP10失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取执行时长统计
     *
     * @return 执行时长统计列表
     */
    @PostMapping("/getExecutionDurationStatistics")
    public MyJsonBean<List<TaskExecutionTrend>> getExecutionDurationStatistics() {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            List<TaskExecutionTrend> statisticsList = collectionMonitorService.getExecutionDurationStatistics(orgId);
            return MyJsonBean.successData("查询成功", statisticsList);
        } catch (Exception e) {
            log.error("获取执行时长统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}

