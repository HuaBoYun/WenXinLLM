package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetBiIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算BI系统集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetBiIntegrationMapper extends BaseMapper<BudgetBiIntegration> {

    /**
     * 根据BI系统类型查询集成列表
     * 
     * @param biType BI系统类型
     * @return 集成列表
     */
    List<BudgetBiIntegration> listByBiType(@Param("biType") String biType);

    /**
     * 根据连接方式查询集成列表
     * 
     * @param connectionType 连接方式
     * @return 集成列表
     */
    List<BudgetBiIntegration> listByConnectionType(@Param("connectionType") String connectionType);

    /**
     * 根据同步模式查询集成列表
     * 
     * @param syncMode 同步模式
     * @return 集成列表
     */
    List<BudgetBiIntegration> listBySyncMode(@Param("syncMode") String syncMode);

    /**
     * 查询启用的BI集成列表
     * 
     * @return 集成列表
     */
    List<BudgetBiIntegration> listEnabled();

    /**
     * 更新同步统计
     * 
     * @param biId BI集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @return 更新记录数
     */
    int updateSyncStatistics(@Param("biId") String biId, 
                           @Param("success") boolean success, 
                           @Param("recordCount") Integer recordCount);
}

