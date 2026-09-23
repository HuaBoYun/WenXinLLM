package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetImprovementSuggestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算改进建议Mapper接口
 *
 * @author AI Agent
 * @date 2026-04-10
 */
@Mapper
public interface BudgetImprovementSuggestionMapper extends BaseMapper<BudgetImprovementSuggestion> {

    /**
     * 查询有效的改进建议列表（按优先级排序）
     */
    List<BudgetImprovementSuggestion> selectActiveSuggestions();

    /**
     * 根据优先级查询
     */
    List<BudgetImprovementSuggestion> selectByPriority(@Param("priority") String priority);
}
