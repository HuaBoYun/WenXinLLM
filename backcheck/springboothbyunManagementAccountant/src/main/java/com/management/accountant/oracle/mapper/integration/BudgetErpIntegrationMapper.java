package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetErpIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算ERP系统集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetErpIntegrationMapper extends BaseMapper<BudgetErpIntegration> {

    /**
     * 根据ERP系统类型查询集成列表
     * 
     * @param erpType ERP系统类型
     * @return 集成列表
     */
    List<BudgetErpIntegration> listByErpType(@Param("erpType") String erpType);

    /**
     * 根据连接方式查询集成列表
     * 
     * @param connectionType 连接方式
     * @return 集成列表
     */
    List<BudgetErpIntegration> listByConnectionType(@Param("connectionType") String connectionType);

    /**
     * 根据同步频率查询集成列表
     * 
     * @param syncFrequency 同步频率
     * @return 集成列表
     */
    List<BudgetErpIntegration> listBySyncFrequency(@Param("syncFrequency") String syncFrequency);

    /**
     * 查询启用的ERP集成列表
     * 
     * @return 集成列表
     */
    List<BudgetErpIntegration> listEnabled();

    /**
     * 更新同步统计
     * 
     * @param erpId ERP集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @return 更新记录数
     */
    int updateSyncStatistics(@Param("erpId") String erpId, 
                           @Param("success") boolean success, 
                           @Param("recordCount") Integer recordCount);
}

