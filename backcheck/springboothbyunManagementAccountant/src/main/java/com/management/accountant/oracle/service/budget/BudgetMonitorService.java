package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetMonitor;

import java.util.List;

/**
 * 预算监控Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetMonitorService extends IService<BudgetMonitor> {

    /**
     * 根据监控编码查询监控
     */
    BudgetMonitor getByMonitorCode(String monitorCode);

    /**
     * 根据预算ID查询监控
     */
    BudgetMonitor getByBudgetId(String budgetId);

    /**
     * 根据监控状态查询监控列表
     */
    List<BudgetMonitor> listByMonitorStatus(String monitorStatus);

    /**
     * 查询启用的监控列表
     */
    List<BudgetMonitor> listEnabledMonitors();

    /**
     * 查询需要监控的列表
     */
    List<BudgetMonitor> listMonitorsToCheck();

    /**
     * 批量删除监控
     */
    boolean batchDeleteMonitors(List<String> monitorIds);

    /**
     * 保存或更新监控
     */
    boolean saveOrUpdateMonitor(BudgetMonitor monitor);

    /**
     * 执行预算监控检查
     */
    boolean executeMonitorCheck(String monitorId);
}

