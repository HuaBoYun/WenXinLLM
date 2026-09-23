package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetCloudIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算云平台集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetCloudIntegrationMapper extends BaseMapper<BudgetCloudIntegration> {

    /**
     * 根据云平台类型查询集成列表
     * 
     * @param cloudType 云平台类型
     * @return 集成列表
     */
    List<BudgetCloudIntegration> listByCloudType(@Param("cloudType") String cloudType);

    /**
     * 根据服务类型查询集成列表
     * 
     * @param serviceType 服务类型
     * @return 集成列表
     */
    List<BudgetCloudIntegration> listByServiceType(@Param("serviceType") String serviceType);

    /**
     * 根据同步模式查询集成列表
     * 
     * @param syncMode 同步模式
     * @return 集成列表
     */
    List<BudgetCloudIntegration> listBySyncMode(@Param("syncMode") String syncMode);

    /**
     * 查询启用的云平台集成列表
     * 
     * @return 集成列表
     */
    List<BudgetCloudIntegration> listEnabled();

    /**
     * 更新同步统计
     * 
     * @param cloudId 云平台集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @param dataTransferMb 数据传输量(MB)
     * @return 更新记录数
     */
    int updateSyncStatistics(@Param("cloudId") String cloudId, 
                           @Param("success") boolean success, 
                           @Param("recordCount") Integer recordCount,
                           @Param("dataTransferMb") Long dataTransferMb);
}

