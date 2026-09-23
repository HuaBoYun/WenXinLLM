package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetWebServiceIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算Web服务集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetWebServiceIntegrationMapper extends BaseMapper<BudgetWebServiceIntegration> {

    /**
     * 根据Web服务类型查询集成列表
     * 
     * @param wsType Web服务类型
     * @return 集成列表
     */
    List<BudgetWebServiceIntegration> listByWsType(@Param("wsType") String wsType);

    /**
     * 根据认证方式查询集成列表
     * 
     * @param authType 认证方式
     * @return 集成列表
     */
    List<BudgetWebServiceIntegration> listByAuthType(@Param("authType") String authType);

    /**
     * 根据调用频率查询集成列表
     * 
     * @param callFrequency 调用频率
     * @return 集成列表
     */
    List<BudgetWebServiceIntegration> listByCallFrequency(@Param("callFrequency") String callFrequency);

    /**
     * 查询启用的Web服务集成列表
     * 
     * @return 集成列表
     */
    List<BudgetWebServiceIntegration> listEnabled();

    /**
     * 更新调用统计
     * 
     * @param wsId Web服务集成ID
     * @param success 是否成功
     * @param responseTime 响应时间
     * @return 更新记录数
     */
    int updateCallStatistics(@Param("wsId") String wsId, 
                           @Param("success") boolean success, 
                           @Param("responseTime") Integer responseTime);
}

