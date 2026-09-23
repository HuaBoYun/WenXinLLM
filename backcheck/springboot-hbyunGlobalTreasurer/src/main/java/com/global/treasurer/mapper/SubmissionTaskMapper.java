package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblSubmissionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报送任务Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface SubmissionTaskMapper extends BaseMapper<TblSubmissionTask> {

    /**
     * 分页查询报送任务列表
     *
     * @param params 查询参数
     * @return 报送任务列表
     */
    List<TblSubmissionTask> selectTaskList(Map<String, Object> params);

    /**
     * 根据ID查询报送任务详情
     *
     * @param taskId 任务ID
     * @return 报送任务
     */
    TblSubmissionTask selectTaskById(@Param("taskId") String taskId);

    /**
     * 根据任务代码查询
     *
     * @param taskCode 任务代码
     * @return 报送任务
     */
    TblSubmissionTask selectByTaskCode(@Param("taskCode") String taskCode);

    /**
     * 查询待执行的任务
     *
     * @return 报送任务列表
     */
    List<TblSubmissionTask> selectPendingTasks();

    /**
     * 批量删除报送任务（逻辑删除）
     *
     * @param taskIds 任务ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("taskIds") List<String> taskIds);

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param taskStatus 任务状态
     * @return 影响行数
     */
    int updateTaskStatus(@Param("taskId") String taskId, @Param("taskStatus") String taskStatus);
}

