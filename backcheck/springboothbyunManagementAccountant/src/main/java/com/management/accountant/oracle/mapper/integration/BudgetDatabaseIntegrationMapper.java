package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetDatabaseIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算数据库集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetDatabaseIntegrationMapper extends BaseMapper<BudgetDatabaseIntegration> {

    /**
     * 根据数据库类型查询集成列表
     * 
     * @param dbType 数据库类型
     * @return 集成列表
     */
    List<BudgetDatabaseIntegration> listByDbType(@Param("dbType") String dbType);

    /**
     * 根据同步模式查询集成列表
     * 
     * @param syncMode 同步模式
     * @return 集成列表
     */
    List<BudgetDatabaseIntegration> listBySyncMode(@Param("syncMode") String syncMode);

    /**
     * 根据同步频率查询集成列表
     * 
     * @param syncFrequency 同步频率
     * @return 集成列表
     */
    List<BudgetDatabaseIntegration> listBySyncFrequency(@Param("syncFrequency") String syncFrequency);

    /**
     * 查询启用的数据库集成列表
     * 
     * @return 集成列表
     */
    List<BudgetDatabaseIntegration> listEnabled();

    /**
     * 更新同步统计
     * 
     * @param dbId 数据库集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @return 更新记录数
     */
    int updateSyncStatistics(@Param("dbId") String dbId, 
                           @Param("success") boolean success, 
                           @Param("recordCount") Integer recordCount);
}

