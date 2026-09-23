package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算任务Mapper（新增方法）
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Mapper
public interface BudgetTaskMapperNew extends BaseMapper<BudgetTask> {

    /**
     * 根据预算ID查询任务列表
     */
    List<BudgetTask> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据负责人查询任务列表
     */
    List<BudgetTask> selectByAssignee(@Param("assignee") String assignee, @Param("status") String status);

    /**
     * 根据状态统计任务数量
     */
    int countByStatus(@Param("status") String status, @Param("companyId") String companyId);

    /**
     * 查询超期任务
     */
    List<BudgetTask> selectOverdueTasks();

    /**
     * 批量更新任务状态
     */
    int batchUpdateStatus(@Param("taskIds") List<String> taskIds, @Param("status") String status);
}

