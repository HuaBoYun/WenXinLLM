package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblBudgetExecution;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 预算执行记录Mapper
 */
public interface TblBudgetExecutionMapper extends BaseMapper<TblBudgetExecution> {

    /**
     * 根据规则ID查询
     */
    List<TblBudgetExecution> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据预算ID查询
     */
    List<TblBudgetExecution> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据业务类型和业务ID查询
     */
    List<TblBudgetExecution> selectByBusiness(@Param("businessType") String businessType, @Param("businessId") String businessId);

    /**
     * 根据执行时间范围查询
     */
    List<TblBudgetExecution> selectByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据执行人查询
     */
    List<TblBudgetExecution> selectByExecutionUser(@Param("executionUser") String executionUser);
}
