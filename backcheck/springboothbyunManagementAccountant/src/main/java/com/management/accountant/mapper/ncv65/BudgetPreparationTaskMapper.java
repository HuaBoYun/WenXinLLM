package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetPreparationTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算编制任务Mapper接口
 * 
 * @description 预算编制任务数据访问层接口，兼容达梦数据库和MySQL数据库
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetPreparationTaskMapper extends BaseMapper<BudgetPreparationTask> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据任务编码查询编制任务
     * @param taskCode 任务编码
     * @param tenantId 租户ID
     * @return 编制任务信息
     */
    @Select("SELECT * FROM BUDGET_PREPARATION_TASK WHERE TASK_CODE = #{taskCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetPreparationTask selectByTaskCode(@Param("taskCode") String taskCode, @Param("tenantId") String tenantId);

    /**
     * 根据任务名称查询编制任务
     * @param taskName 任务名称
     * @param tenantId 租户ID
     * @return 编制任务列表
     */
    @Select("SELECT * FROM BUDGET_PREPARATION_TASK WHERE TASK_NAME LIKE CONCAT('%', #{taskName}, '%') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetPreparationTask> selectByTaskName(@Param("taskName") String taskName, @Param("tenantId") String tenantId);

    /**
     * 根据任务类型查询编制任务
     * @param taskType 任务类型
     * @param tenantId 租户ID
     * @return 编制任务列表
     */
    @Select("SELECT * FROM BUDGET_PREPARATION_TASK WHERE TASK_TYPE = #{taskType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetPreparationTask> selectByTaskType(@Param("taskType") String taskType, @Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询编制任务
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 编制任务列表
     */
    @Select("SELECT * FROM BUDGET_PREPARATION_TASK WHERE FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetPreparationTask> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 根据任务状态查询编制任务
     * @param taskStatus 任务状态
     * @param tenantId 租户ID
     * @return 编制任务列表
     */
    @Select("SELECT * FROM BUDGET_PREPARATION_TASK WHERE TASK_STATUS = #{taskStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetPreparationTask> selectByTaskStatus(@Param("taskStatus") String taskStatus, @Param("tenantId") String tenantId);

    /**
     * 根据负责人查询编制任务
     * @param ownerId 负责人ID
     * @param tenantId 租户ID
     * @return 编制任务列表
     */
    @Select("SELECT * FROM BUDGET_PREPARATION_TASK WHERE OWNER_ID = #{ownerId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetPreparationTask> selectByOwnerId(@Param("ownerId") String ownerId, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询编制任务
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetPreparationTask> selectPreparationTaskPage(Page<BudgetPreparationTask> page, @Param("params") Map<String, Object> params);

    /**
     * 查询我的编制任务（负责人或参与人）
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 编制任务列表
     */
    List<BudgetPreparationTask> selectMyPreparationTasks(@Param("userId") String userId, @Param("tenantId") String tenantId);

    /**
     * 查询待处理的编制任务
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 编制任务列表
     */
    List<BudgetPreparationTask> selectPendingPreparationTasks(@Param("userId") String userId, @Param("tenantId") String tenantId);

    /**
     * 查询即将到期的编制任务
     * @param days 天数
     * @param tenantId 租户ID
     * @return 即将到期的编制任务列表
     */
    List<BudgetPreparationTask> selectExpiringPreparationTasks(@Param("days") Integer days, @Param("tenantId") String tenantId);

    /**
     * 查询已过期的编制任务
     * @param tenantId 租户ID
     * @return 已过期的编制任务列表
     */
    List<BudgetPreparationTask> selectExpiredPreparationTasks(@Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计编制任务数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    Map<String, Object> selectPreparationTaskStatistics(@Param("tenantId") String tenantId);

    /**
     * 按状态统计编制任务数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectPreparationTaskCountByStatus(@Param("tenantId") String tenantId);

    /**
     * 按类型统计编制任务数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectPreparationTaskCountByType(@Param("tenantId") String tenantId);

    /**
     * 按年度统计编制任务数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectPreparationTaskCountByYear(@Param("tenantId") String tenantId);

    /**
     * 统计用户的编制任务数量
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    Map<String, Object> selectUserPreparationTaskStatistics(@Param("userId") String userId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 批量更新任务状态
     * @param taskIds 任务ID列表
     * @param taskStatus 新状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateTaskStatus(@Param("taskIds") List<String> taskIds, 
                             @Param("taskStatus") String taskStatus,
                             @Param("updateBy") String updateBy,
                             @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量更新任务进度
     * @param taskIds 任务ID列表
     * @param progress 进度
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateTaskProgress(@Param("taskIds") List<String> taskIds,
                               @Param("progress") Integer progress,
                               @Param("updateBy") String updateBy,
                               @Param("updateTime") LocalDateTime updateTime);

    /**
     * 启动编制任务
     * @param taskId 任务ID
     * @param actualStartTime 实际开始时间
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_PREPARATION_TASK SET TASK_STATUS = 'in_progress', ACTUAL_START_TIME = #{actualStartTime}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{taskId}")
    int startPreparationTask(@Param("taskId") String taskId,
                            @Param("actualStartTime") LocalDateTime actualStartTime,
                            @Param("updateBy") String updateBy,
                            @Param("updateTime") LocalDateTime updateTime);

    /**
     * 完成编制任务
     * @param taskId 任务ID
     * @param actualEndTime 实际结束时间
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_PREPARATION_TASK SET TASK_STATUS = 'completed', ACTUAL_END_TIME = #{actualEndTime}, PROGRESS = 100, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{taskId}")
    int completePreparationTask(@Param("taskId") String taskId,
                               @Param("actualEndTime") LocalDateTime actualEndTime,
                               @Param("updateBy") String updateBy,
                               @Param("updateTime") LocalDateTime updateTime);

    /**
     * 取消编制任务
     * @param taskId 任务ID
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_PREPARATION_TASK SET TASK_STATUS = 'cancelled', UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{taskId}")
    int cancelPreparationTask(@Param("taskId") String taskId,
                             @Param("updateBy") String updateBy,
                             @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理过期的编制任务
     * @param expiryTime 过期时间
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupExpiredPreparationTasks(@Param("expiryTime") LocalDateTime expiryTime, @Param("tenantId") String tenantId);

    /**
     * 归档已完成的编制任务
     * @param archiveTime 归档时间
     * @param tenantId 租户ID
     * @return 归档数量
     */
    int archiveCompletedPreparationTasks(@Param("archiveTime") LocalDateTime archiveTime, @Param("tenantId") String tenantId);
}
