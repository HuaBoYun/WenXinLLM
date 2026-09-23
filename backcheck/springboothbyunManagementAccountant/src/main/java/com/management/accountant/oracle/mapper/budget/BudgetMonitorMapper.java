package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetMonitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算监控Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetMonitorMapper extends BaseMapper<BudgetMonitor> {

    /**
     * 根据监控编码查询监控
     */
    BudgetMonitor selectByMonitorCode(@Param("monitorCode") String monitorCode);

    /**
     * 根据预算ID查询监控
     */
    BudgetMonitor selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据监控状态查询监控列表
     */
    List<BudgetMonitor> selectByMonitorStatus(@Param("monitorStatus") String monitorStatus);

    /**
     * 查询启用的监控列表
     */
    List<BudgetMonitor> selectEnabledMonitors();

    /**
     * 查询需要监控的列表
     */
    List<BudgetMonitor> selectMonitorsToCheck();

    /**
     * 批量删除监控
     */
    int batchDeleteMonitors(@Param("monitorIds") List<String> monitorIds);
}

