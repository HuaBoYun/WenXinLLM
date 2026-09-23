package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetIntegrationMonitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 预算集成监控Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetIntegrationMonitorMapper extends BaseMapper<BudgetIntegrationMonitor> {

    /**
     * 根据集成类型查询监控记录列表
     * 
     * @param integrationType 集成类型
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByIntegrationType(@Param("integrationType") String integrationType);

    /**
     * 根据集成ID查询监控记录列表
     * 
     * @param integrationId 集成ID
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByIntegrationId(@Param("integrationId") String integrationId);

    /**
     * 根据执行状态查询监控记录列表
     * 
     * @param executionStatus 执行状态
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByExecutionStatus(@Param("executionStatus") String executionStatus);

    /**
     * 根据告警级别查询监控记录列表
     * 
     * @param alertLevel 告警级别
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByAlertLevel(@Param("alertLevel") String alertLevel);

    /**
     * 根据时间范围查询监控记录列表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByTimeRange(@Param("startTime") Date startTime,
                                                   @Param("endTime") Date endTime);

    /**
     * 查询未告警的错误记录
     * 
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listUnalertedErrors();

    /**
     * 统计集成执行情况
     * 
     * @param integrationId 集成ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<BudgetIntegrationMonitor> statisticsByIntegration(@Param("integrationId") String integrationId,
                                                           @Param("startTime") Date startTime,
                                                           @Param("endTime") Date endTime);
}

