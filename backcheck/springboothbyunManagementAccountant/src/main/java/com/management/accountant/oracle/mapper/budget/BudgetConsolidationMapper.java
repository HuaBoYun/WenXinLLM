package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetConsolidation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算合并Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetConsolidationMapper extends BaseMapper<BudgetConsolidation> {

    /**
     * 根据合并编码查询合并
     */
    BudgetConsolidation selectByConsolidationCode(@Param("consolidationCode") String consolidationCode);

    /**
     * 根据合并类型查询合并列表
     */
    List<BudgetConsolidation> selectByConsolidationType(@Param("consolidationType") String consolidationType);

    /**
     * 根据预算年度查询合并列表
     */
    List<BudgetConsolidation> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /**
     * 根据合并状态查询合并列表
     */
    List<BudgetConsolidation> selectByConsolidationStatus(@Param("consolidationStatus") String consolidationStatus);

    /**
     * 批量删除合并
     */
    int batchDeleteConsolidations(@Param("consolidationIds") List<String> consolidationIds);
}

