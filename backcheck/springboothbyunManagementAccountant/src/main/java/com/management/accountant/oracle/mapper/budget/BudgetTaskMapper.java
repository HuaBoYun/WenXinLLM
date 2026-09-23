package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算任务Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetTaskMapper extends BaseMapper<BudgetTask> {

    /**
     * 根据任务编码查询任务
     * 
     * @param taskCode 任务编码
     * @return 预算任务
     */
    BudgetTask selectByTaskCode(@Param("taskCode") String taskCode);

    /**
     * 查询我创建的任务列表
     * 
     * @param creatorId 创建人ID
     * @return 任务列表
     */
    List<BudgetTask> selectMyCreatedTasks(@Param("creatorId") String creatorId);

    /**
     * 查询分配给我的任务列表
     * 
     * @param assigneeId 分配人ID
     * @return 任务列表
     */
    List<BudgetTask> selectMyAssignedTasks(@Param("assigneeId") String assigneeId);

    /**
     * 批量删除任务
     * 
     * @param taskIds 任务ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("taskIds") List<String> taskIds);

    /**
     * 更新任务状态
     * 
     * @param taskId 任务ID
     * @param taskStatus 任务状态
     * @return 更新数量
     */
    int updateTaskStatus(@Param("taskId") String taskId, @Param("taskStatus") String taskStatus);

    /**
     * 更新任务进度
     * 
     * @param taskId 任务ID
     * @param progress 进度
     * @return 更新数量
     */
    int updateTaskProgress(@Param("taskId") String taskId, @Param("progress") java.math.BigDecimal progress);
}

