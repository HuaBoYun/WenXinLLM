package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.CollectionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采集任务Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-10-23
 */
@Mapper
public interface CollectionTaskMapper extends BaseMapper<CollectionTask> {

    /**
     * 更新任务进度
     *
     * @param taskId 任务ID
     * @param progress 进度
     * @param recordCount 记录数
     */
    @Update("UPDATE TBL_COLLECTION_TASK SET PROGRESS = #{progress}, RECORD_COUNT = #{recordCount}, UPDATE_TIME = SYSDATE WHERE TASK_ID = #{taskId}")
    void updateProgress(@Param("taskId") String taskId, @Param("progress") BigDecimal progress, @Param("recordCount") Long recordCount);

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param status 状态
     */
    @Update("UPDATE TBL_COLLECTION_TASK SET TASK_STATUS = #{status}, UPDATE_TIME = SYSDATE WHERE TASK_ID = #{taskId}")
    void updateStatus(@Param("taskId") String taskId, @Param("status") String status);

    /**
     * 查询运行中的任务
     *
     * @return 运行中的任务列表
     */
    @Select("SELECT * FROM TBL_COLLECTION_TASK WHERE TASK_STATUS = 'RUNNING' ORDER BY CREATE_TIME DESC")
    List<CollectionTask> selectRunningTasks();

    /**
     * 根据方案ID查询任务
     *
     * @param planId 方案ID
     * @return 任务列表
     */
    @Select("SELECT * FROM TBL_COLLECTION_TASK WHERE PLAN_ID = #{planId} ORDER BY CREATE_TIME DESC")
    List<CollectionTask> selectByPlanId(@Param("planId") String planId);

    /**
     * 分页查询采集任务列表
     *
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 任务列表
     */
    @Select("SELECT * FROM TBL_COLLECTION_TASK ORDER BY CREATE_TIME DESC OFFSET #{offset} ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<CollectionTask> selectPageList(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 查询采集任务总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(1) FROM TBL_COLLECTION_TASK")
    long selectTotalCount();

    /**
     * 分页查询采集任务列表(带条件)
     *
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @param taskName 任务名称
     * @param taskStatus 任务状态
     * @return 任务列表
     */
    @Select("<script>" +
            "SELECT * FROM TBL_COLLECTION_TASK " +
            "WHERE 1=1 " +
            "<if test='taskName != null and taskName != \"\"'>" +
            "  AND TASK_NAME LIKE CONCAT('%', #{taskName}, '%') " +
            "</if>" +
            "<if test='taskStatus != null and taskStatus != \"\"'>" +
            "  AND TASK_STATUS = #{taskStatus} " +
            "</if>" +
            "ORDER BY CREATE_TIME DESC " +
            "OFFSET #{offset} ROWS FETCH NEXT #{pageSize} ROWS ONLY" +
            "</script>")
    List<CollectionTask> selectPageListWithFilter(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("taskName") String taskName,
            @Param("taskStatus") String taskStatus);

    /**
     * 查询采集任务总数(带条件)
     *
     * @param taskName 任务名称
     * @param taskStatus 任务状态
     * @return 总数
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM TBL_COLLECTION_TASK " +
            "WHERE 1=1 " +
            "<if test='taskName != null and taskName != \"\"'>" +
            "  AND TASK_NAME LIKE CONCAT('%', #{taskName}, '%') " +
            "</if>" +
            "<if test='taskStatus != null and taskStatus != \"\"'>" +
            "  AND TASK_STATUS = #{taskStatus} " +
            "</if>" +
            "</script>")
    long selectTotalCountWithFilter(
            @Param("taskName") String taskName,
            @Param("taskStatus") String taskStatus);
}

