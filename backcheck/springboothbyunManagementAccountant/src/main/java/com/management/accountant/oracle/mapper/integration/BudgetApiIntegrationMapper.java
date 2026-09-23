package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetApiIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算API接口集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetApiIntegrationMapper extends BaseMapper<BudgetApiIntegration> {

    /**
     * 根据API类型查询API集成列表
     * 
     * @param apiType API类型
     * @return API集成列表
     */
    List<BudgetApiIntegration> listByApiType(@Param("apiType") String apiType);

    /**
     * 根据API状态查询API集成列表
     * 
     * @param apiStatus API状态
     * @return API集成列表
     */
    List<BudgetApiIntegration> listByApiStatus(@Param("apiStatus") String apiStatus);

    /**
     * 根据认证方式查询API集成列表
     * 
     * @param authType 认证方式
     * @return API集成列表
     */
    List<BudgetApiIntegration> listByAuthType(@Param("authType") String authType);

    /**
     * 查询启用的API集成列表
     * 
     * @return API集成列表
     */
    List<BudgetApiIntegration> listEnabled();

    /**
     * 更新API调用统计
     * 
     * @param apiId API集成ID
     * @param success 是否成功
     * @param responseTime 响应时间
     * @return 更新记录数
     */
    int updateCallStatistics(@Param("apiId") String apiId, 
                            @Param("success") boolean success, 
                            @Param("responseTime") Integer responseTime);
}

