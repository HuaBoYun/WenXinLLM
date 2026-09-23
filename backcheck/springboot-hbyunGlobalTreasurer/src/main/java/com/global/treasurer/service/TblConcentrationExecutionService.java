package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblConcentrationExecution;

import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;

/**
 * 归集执行记录Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblConcentrationExecutionService {

    /**
     * 分页查询执行记录列表
     */
    PageInfo<TblConcentrationExecution> getExecutionPage(Integer pageNum, Integer pageSize,
                                                         String executionNo, String executionStatus,
                                                         String planId, String startDate);

    /**
     * 根据ID查询执行记录
     */
    TblConcentrationExecution getExecutionById(Long executionId);

    /**
     * 保存执行记录
     */
    TblConcentrationExecution saveExecution(TblConcentrationExecution execution);

    /**
     * 更新执行记录
     */
    void updateExecution(TblConcentrationExecution execution);

    /**
     * 暂停执行
     */
    void pauseExecution(Long executionId);

    /**
     * 恢复执行
     */
    void resumeExecution(Long executionId);

    /**
     * 重试执行
     */
    String retryExecution(Long executionId);

    /**
     * 获取执行监控数据
     */
    Map<String, Object> getMonitoringData();

    /**
     * 获取执行统计
     */
    Map<String, Object> getExecutionStatistics(String startDate, String endDate);

    /**
     * 获取告警列表
     */
    List<Map<String, Object>> getAlertList();

    /**
     * 标记告警为已处理
     */
    void markAlertAsHandled(String alertId);

    /**
     * 批量标记告警为已处理
     */
    void batchMarkAlertAsHandled(List<String> alertIds);

    /**
     * 统计今日执行次数
     */
    long countTodayExecutions();

    /**
     * 统计今日成功执行次数
     */
    long countTodaySuccessExecutions();

    /**
     * 获取今日归集总金额
     */
    java.math.BigDecimal getTodayTotalAmount();

    /**
     * 获取本月归集总金额
     */
    java.math.BigDecimal getMonthTotalAmount();

    /**
     * 统计待处理预警数量
     */
    long countPendingAlerts();

    /**
     * 获取资金流向分析
     */
    Map<String, Object> getFlowAnalysis(String startDate, String endDate);

    /**
     * 导出执行监控
     */
    void exportMonitor(String executionStatus, String planId, ServletOutputStream outputStream) throws Exception;
}

