package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 调度Mapper接口
 */
public interface FinanceScheduledMapper {
    
    /**
     * 查询调度任务列表
     */
    List<Map<String, Object>> selectScheduledList(@Param("params") Map<String, Object> params);
    
    /**
     * 根据ID查询调度任务详情
     */
    Map<String, Object> selectScheduledById(@Param("scheduledId") String scheduledId);
    
    /**
     * 插入调度任务
     */
    int insertScheduled(@Param("scheduled") Map<String, Object> scheduled);
    
    /**
     * 更新调度任务
     */
    int updateScheduled(@Param("scheduled") Map<String, Object> scheduled);
    
    /**
     * 删除调度任务
     */
    int deleteScheduled(@Param("scheduledId") String scheduledId);
    
    /**
     * 更新调度任务状态
     */
    int updateScheduledStatus(@Param("params") Map<String, Object> params);
    
    /**
     * 查询调度任务执行历史
     */
    List<Map<String, Object>> selectScheduledHistory(@Param("params") Map<String, Object> params);

    // ==================== 定时任务管理相关方法 ====================

    /**
     * 查询定时任务列表
     */
    List<Map<String, Object>> selectScheduledTaskList(@Param("params") Map<String, Object> params);

    /**
     * 统计定时任务总数
     */
    int countScheduledTaskList(@Param("params") Map<String, Object> params);

    /**
     * 删除定时任务
     */
    int deleteScheduledTask(@Param("params") Map<String, Object> params);

    /**
     * 更新定时任务状态
     */
    int updateScheduledTaskStatus(@Param("data") Map<String, Object> data);

    /**
     * 插入定时任务
     */
    int insertScheduledTask(@Param("data") Map<String, Object> data);

    /**
     * 更新定时任务信息
     */
    int updateScheduledTaskInfo(@Param("data") Map<String, Object> data);

    /**
     * 查询定时任务详情
     */
    Map<String, Object> selectScheduledTaskDetail(@Param("params") Map<String, Object> params);
}