package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.LoanMonitoringDTO;
import com.global.treasurer.entity.TblLoanMonitoring;

import java.util.List;

/**
 * 贷款监控Service接口
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
public interface LoanMonitoringService {

    /**
     * 分页查询监控预警列表
     */
    PageInfo<TblLoanMonitoring> getMonitoringList(LoanMonitoringDTO dto);

    /**
     * 根据贷款ID查询监控预警
     */
    List<TblLoanMonitoring> getMonitoringByLoanId(String loanId);

    /**
     * 根据ID查询监控预警详情
     */
    TblLoanMonitoring getMonitoringById(Long monitoringId);

    /**
     * 新增预警
     */
    TblLoanMonitoring addAlert(LoanMonitoringDTO dto);

    /**
     * 修改预警
     */
    TblLoanMonitoring updateAlert(LoanMonitoringDTO dto);

    /**
     * 处理预警
     * @param monitoringId 预警ID
     * @param handlerId 处理人ID
     * @param handlerName 处理人姓名
     * @param handleOpinion 处理意见
     * @param status 处理后状态
     */
    void handleAlert(Long monitoringId, Long handlerId, String handlerName,
                     String handleOpinion, String status);

    /**
     * 查询待处理预警
     */
    List<TblLoanMonitoring> getPendingAlerts(String loanId);

    /**
     * 统计待处理预警数量
     */
    int countPendingAlerts(Long loanId);

    /**
     * 删除监控预警
     */
    void deleteMonitoring(Long monitoringId);

    /**
     * 检查并生成到期预警（定时任务调用）
     */
    void checkAndGenerateMaturityAlerts();

    /**
     * 检查并生成逾期预警（定时任务调用）
     */
    void checkAndGenerateOverdueAlerts();
}

