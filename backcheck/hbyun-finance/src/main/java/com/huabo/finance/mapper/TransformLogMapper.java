package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.TransformLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 转化日志Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface TransformLogMapper extends BaseMapper<TransformLog> {

    /**
     * 根据任务ID查询日志列表
     * 
     * @param taskId 任务ID
     * @return 日志列表
     */
    @Select("SELECT * FROM TBL_TRANSFORM_LOG WHERE TASK_ID = #{taskId} ORDER BY CREATE_TIME DESC")
    List<TransformLog> selectByTaskId(@Param("taskId") String taskId);

    /**
     * 根据任务ID和日志级别查询日志列表
     * 
     * @param taskId 任务ID
     * @param logLevel 日志级别
     * @return 日志列表
     */
    @Select("SELECT * FROM TBL_TRANSFORM_LOG WHERE TASK_ID = #{taskId} AND LOG_LEVEL = #{logLevel} ORDER BY CREATE_TIME DESC")
    List<TransformLog> selectByTaskIdAndLevel(@Param("taskId") String taskId, @Param("logLevel") String logLevel);
}

