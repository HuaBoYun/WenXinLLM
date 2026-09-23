package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.DataSubmitTask;
import com.huabo.cybermonitor.vo.DataSubmitTaskQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 数据报送任务 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface DataSubmitTaskMapper extends BaseMapper<DataSubmitTask> {

    /**
     * 分页查询数据报送任务列表
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<DataSubmitTask> selectTaskList(IPage<DataSubmitTask> page, @Param("query") DataSubmitTaskQueryVO query);

    /**
     * 根据任务ID查询任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    @Select("SELECT * FROM DATA_SUBMIT_TASK WHERE TASK_ID = #{taskId}")
    DataSubmitTask selectTaskDetail(@Param("taskId") String taskId);

    /**
     * 根据任务状态查询任务列表
     *
     * @param taskStatus 任务状态
     * @return 任务列表
     */
    @Select("SELECT * FROM DATA_SUBMIT_TASK WHERE TASK_STATUS = #{taskStatus} ORDER BY CREATE_TIME DESC")
    List<DataSubmitTask> selectTasksByStatus(@Param("taskStatus") String taskStatus);

    /**
     * 根据任务类型查询任务列表
     *
     * @param taskType 任务类型
     * @return 任务列表
     */
    @Select("SELECT * FROM DATA_SUBMIT_TASK WHERE TASK_TYPE = #{taskType} ORDER BY CREATE_TIME DESC")
    List<DataSubmitTask> selectTasksByType(@Param("taskType") String taskType);

    /**
     * 根据报送周期查询任务列表
     *
     * @param submitCycle 报送周期
     * @return 任务列表
     */
    @Select("SELECT * FROM DATA_SUBMIT_TASK WHERE SUBMIT_CYCLE = #{submitCycle} AND TASK_STATUS = 'ACTIVE' ORDER BY CREATE_TIME DESC")
    List<DataSubmitTask> selectTasksByCycle(@Param("submitCycle") String submitCycle);

    /**
     * 查询活跃的任务列表
     *
     * @return 活跃任务列表
     */
    @Select("SELECT * FROM DATA_SUBMIT_TASK WHERE TASK_STATUS = 'ACTIVE' AND START_DATE <= CURDATE() AND (END_DATE IS NULL OR END_DATE >= CURDATE()) ORDER BY CREATE_TIME DESC")
    List<DataSubmitTask> selectActiveTasks();

    /**
     * 查询即将到期的任务列表
     *
     * @param days 天数
     * @return 即将到期的任务列表
     */
    @Select("SELECT * FROM DATA_SUBMIT_TASK WHERE TASK_STATUS = 'ACTIVE' AND END_DATE IS NOT NULL AND DATEDIFF(END_DATE, CURDATE()) <= #{days} AND DATEDIFF(END_DATE, CURDATE()) >= 0 ORDER BY END_DATE ASC")
    List<DataSubmitTask> selectExpiringTasks(@Param("days") Integer days);

    /**
     * 获取任务统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalCount, " +
            "SUM(CASE WHEN TASK_STATUS = 'ACTIVE' THEN 1 ELSE 0 END) as activeCount, " +
            "SUM(CASE WHEN TASK_STATUS = 'COMPLETED' THEN 1 ELSE 0 END) as completedCount, " +
            "SUM(CASE WHEN TASK_STATUS = 'CANCELLED' THEN 1 ELSE 0 END) as cancelledCount " +
            "FROM DATA_SUBMIT_TASK")
    Map<String, Object> selectTaskStatistics();

    /**
     * 根据任务类型获取统计分布
     *
     * @return 类型分布统计
     */
    @Select("SELECT TASK_TYPE as taskType, COUNT(*) as count FROM DATA_SUBMIT_TASK GROUP BY TASK_TYPE ORDER BY count DESC")
    List<Map<String, Object>> selectTaskTypeDistribution();

    /**
     * 根据任务分类获取统计分布
     *
     * @return 分类分布统计
     */
    @Select("SELECT TASK_CATEGORY as taskCategory, COUNT(*) as count FROM DATA_SUBMIT_TASK WHERE TASK_CATEGORY IS NOT NULL GROUP BY TASK_CATEGORY ORDER BY count DESC")
    List<Map<String, Object>> selectTaskCategoryDistribution();

    /**
     * 根据报送周期获取统计分布
     *
     * @return 周期分布统计
     */
    @Select("SELECT SUBMIT_CYCLE as submitCycle, COUNT(*) as count FROM DATA_SUBMIT_TASK WHERE SUBMIT_CYCLE IS NOT NULL GROUP BY SUBMIT_CYCLE ORDER BY count DESC")
    List<Map<String, Object>> selectTaskCycleDistribution();

    /**
     * 批量插入任务
     *
     * @param taskList 任务列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<DataSubmitTask> taskList);

    /**
     * 根据条件导出任务列表
     *
     * @param query 查询条件
     * @return 任务列表
     */
    List<DataSubmitTask> selectTaskListForExport(@Param("query") DataSubmitTaskQueryVO query);

    /**
     * 根据任务名称查询任务
     *
     * @param taskName  任务名称
     * @param excludeId 排除的任务ID
     * @return 任务信息
     */
    @Select("SELECT * FROM DATA_SUBMIT_TASK WHERE TASK_NAME = #{taskName} " +
            "AND (#{excludeId} IS NULL OR TASK_ID != #{excludeId}) LIMIT 1")
    DataSubmitTask selectByTaskName(@Param("taskName") String taskName, @Param("excludeId") String excludeId);

    /**
     * 更新任务状态
     *
     * @param taskId     任务ID
     * @param taskStatus 任务状态
     * @return 更新数量
     */
    @Select("UPDATE DATA_SUBMIT_TASK SET TASK_STATUS = #{taskStatus}, UPDATE_TIME = NOW() WHERE TASK_ID = #{taskId}")
    int updateTaskStatus(@Param("taskId") String taskId, @Param("taskStatus") String taskStatus);

    /**
     * 获取任务的报送记录数量
     *
     * @param taskId 任务ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM DATA_SUBMIT_RECORD WHERE TASK_ID = #{taskId}")
    Integer countSubmitRecords(@Param("taskId") String taskId);
}
