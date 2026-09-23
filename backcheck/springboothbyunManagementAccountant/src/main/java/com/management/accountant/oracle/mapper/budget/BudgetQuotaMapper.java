package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetQuota;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算配额Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetQuotaMapper extends BaseMapper<BudgetQuota> {

    /**
     * 根据配额编码查询配额
     */
    BudgetQuota selectByQuotaCode(@Param("quotaCode") String quotaCode);

    /**
     * 根据预算ID查询配额列表
     */
    List<BudgetQuota> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据配额状态查询配额列表
     */
    List<BudgetQuota> selectByQuotaStatus(@Param("quotaStatus") String quotaStatus);

    /**
     * 查询启用的配额列表
     */
    List<BudgetQuota> selectEnabledQuotas();

    /**
     * 批量删除配额
     */
    int batchDeleteQuotas(@Param("quotaIds") List<String> quotaIds);
}

