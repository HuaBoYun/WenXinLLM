package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算执行Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetExecutionMapper extends BaseMapper<BudgetExecution> {

    /**
     * 根据预算ID查询执行记录
     * 
     * @param budgetId 预算ID
     * @return 执行列表
     */
    List<BudgetExecution> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据业务类型和业务ID查询执行记录
     * 
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 执行列表
     */
    List<BudgetExecution> selectByBusinessId(@Param("businessType") String businessType, @Param("businessId") String businessId);

    /**
     * 根据规则ID查询执行记录
     * 
     * @param ruleId 规则ID
     * @return 执行列表
     */
    List<BudgetExecution> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 批量删除执行记录（物理删除）
     * 
     * @param executionIds 执行ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("executionIds") List<String> executionIds);

    /**
     * 按执行用户分组统计，返回排名数据
     * 返回: EXECUTION_USER, EXECUTION_USER_NAME, EXEC_COUNT, TOTAL_AMOUNT, AVG_AMOUNT
     */
    List<Map<String, Object>> selectUserRanking();
}

