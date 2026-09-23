package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetWarningRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算预警记录Mapper接口
 * 
 * @author AI Agent
 * @date 2025-01-29
 */
@Mapper
public interface BudgetWarningRecordMapper extends BaseMapper<BudgetWarningRecord> {

    /**
     * 根据规则ID查询预警记录
     * 
     * @param ruleId 规则ID
     * @return 预警记录列表
     */
    List<BudgetWarningRecord> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据预算ID查询预警记录
     * 
     * @param budgetId 预算ID
     * @return 预警记录列表
     */
    List<BudgetWarningRecord> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据处理状态查询预警记录
     * 
     * @param handleStatus 处理状态
     * @return 预警记录列表
     */
    List<BudgetWarningRecord> selectByHandleStatus(@Param("handleStatus") String handleStatus);

    /**
     * 根据预警级别查询预警记录
     * 
     * @param warningLevel 预警级别
     * @return 预警记录列表
     */
    List<BudgetWarningRecord> selectByWarningLevel(@Param("warningLevel") String warningLevel);

    /**
     * 批量更新处理状态
     * 
     * @param recordIds 记录ID列表
     * @param handleStatus 处理状态
     * @param handleBy 处理人ID
     * @param handleNote 处理说明
     * @return 更新数量
     */
    int batchUpdateHandleStatus(@Param("recordIds") List<String> recordIds, 
                                 @Param("handleStatus") String handleStatus,
                                 @Param("handleBy") String handleBy,
                                 @Param("handleNote") String handleNote);

    /**
     * 统计未处理预警数量
     * 
     * @return 未处理预警数量
     */
    int countPendingWarnings();

    /**
     * 统计各级别预警数量
     * 
     * @return 统计结果
     */
    List<java.util.Map<String, Object>> countByWarningLevel();
}

