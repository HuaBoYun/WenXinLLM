package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetDataStreamIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算数据流集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetDataStreamIntegrationMapper extends BaseMapper<BudgetDataStreamIntegration> {

    /**
     * 根据数据流类型查询集成列表
     * 
     * @param streamType 数据流类型
     * @return 集成列表
     */
    List<BudgetDataStreamIntegration> listByStreamType(@Param("streamType") String streamType);

    /**
     * 根据数据源类型查询集成列表
     * 
     * @param sourceType 数据源类型
     * @return 集成列表
     */
    List<BudgetDataStreamIntegration> listBySourceType(@Param("sourceType") String sourceType);

    /**
     * 根据处理模式查询集成列表
     * 
     * @param processMode 处理模式
     * @return 集成列表
     */
    List<BudgetDataStreamIntegration> listByProcessMode(@Param("processMode") String processMode);

    /**
     * 查询启用的数据流集成列表
     * 
     * @return 集成列表
     */
    List<BudgetDataStreamIntegration> listEnabled();

    /**
     * 更新处理统计
     * 
     * @param streamId 数据流集成ID
     * @param totalRecords 总记录数
     * @param successRecords 成功记录数
     * @param failureRecords 失败记录数
     * @return 更新记录数
     */
    int updateProcessStatistics(@Param("streamId") String streamId, 
                               @Param("totalRecords") Long totalRecords,
                               @Param("successRecords") Long successRecords,
                               @Param("failureRecords") Long failureRecords);
}

