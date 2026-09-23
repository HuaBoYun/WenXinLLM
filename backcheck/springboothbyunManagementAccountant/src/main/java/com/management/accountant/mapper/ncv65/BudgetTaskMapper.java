package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetTask;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算任务数据访问层接口
 * 
 * @description 预算任务数据访问层，支持任务的CRUD操作和复杂查询
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Mapper
public interface BudgetTaskMapper extends BaseMapper<BudgetTask> {

    /**
     * 根据任务编码查询任务
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE TASK_CODE = #{taskCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetTask selectByTaskCode(@Param("taskCode") String taskCode, @Param("tenantId") String tenantId);

    /**
     * 根据任务类型查询任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE TASK_TYPE = #{taskType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectByTaskType(@Param("taskType") String taskType, @Param("tenantId") String tenantId);

    /**
     * 根据任务状态查询任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE TASK_STATUS = #{taskStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectByTaskStatus(@Param("taskStatus") String taskStatus, @Param("tenantId") String tenantId);

    /**
     * 查询启用的任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE IS_ENABLED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectEnabledTasks(@Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE BUDGET_YEAR = #{budgetYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectByBudgetYear(@Param("budgetYear") Integer budgetYear, @Param("tenantId") String tenantId);

    /**
     * 根据组织体系ID查询任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE STRUCTURE_ID = #{structureId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectByStructureId(@Param("structureId") String structureId, @Param("tenantId") String tenantId);

    /**
     * 根据工作流ID查询任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE WORKFLOW_ID = #{workflowId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectByWorkflowId(@Param("workflowId") String workflowId, @Param("tenantId") String tenantId);

    /**
     * 查询待审批的任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE TASK_STATUS IN ('pending_approval', 'in_approval') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectPendingApprovalTasks(@Param("tenantId") String tenantId);

    /**
     * 查询已完成的任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE TASK_STATUS = 'completed' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY COMPLETE_TIME DESC")
    List<BudgetTask> selectCompletedTasks(@Param("tenantId") String tenantId);

    /**
     * 查询超期的任务列表
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE TASK_STATUS NOT IN ('completed', 'cancelled') AND END_DATE < NOW() AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY END_DATE ASC")
    List<BudgetTask> selectOverdueTasks(@Param("tenantId") String tenantId);

    /**
     * 分页查询任务列表（带条件）
     */
    @Select("<script>" +
            "SELECT * FROM BUDGET_TASK WHERE TENANT_ID = #{params.tenantId} AND IS_DELETED = 0" +
            "<if test='params.taskName != null and params.taskName != \"\"'>" +
            " AND TASK_NAME LIKE CONCAT('%', #{params.taskName}, '%')" +
            "</if>" +
            "<if test='params.taskCode != null and params.taskCode != \"\"'>" +
            " AND TASK_CODE LIKE CONCAT('%', #{params.taskCode}, '%')" +
            "</if>" +
            "<if test='params.taskType != null and params.taskType != \"\"'>" +
            " AND TASK_TYPE = #{params.taskType}" +
            "</if>" +
            "<if test='params.taskStatus != null and params.taskStatus != \"\"'>" +
            " AND TASK_STATUS = #{params.taskStatus}" +
            "</if>" +
            "<if test='params.budgetYear != null'>" +
            " AND BUDGET_YEAR = #{params.budgetYear}" +
            "</if>" +
            "<if test='params.structureId != null and params.structureId != \"\"'>" +
            " AND STRUCTURE_ID = #{params.structureId}" +
            "</if>" +
            "<if test='params.isEnabled != null'>" +
            " AND IS_ENABLED = #{params.isEnabled}" +
            "</if>" +
            "<if test='params.status != null and params.status != \"\"'>" +
            " AND STATUS = #{params.status}" +
            "</if>" +
            " ORDER BY CREATE_TIME DESC" +
            "</script>")
    IPage<BudgetTask> selectPageWithConditions(Page<BudgetTask> page, @Param("params") Map<String, Object> params);

    /**
     * 批量更新任务状态
     */
    @Update("<script>" +
            "UPDATE BUDGET_TASK SET TASK_STATUS = #{taskStatus}, UPDATE_TIME = NOW()" +
            " WHERE ID IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            " AND TENANT_ID = #{tenantId}" +
            "</script>")
    int batchUpdateTaskStatus(@Param("ids") List<String> ids, @Param("taskStatus") String taskStatus, @Param("tenantId") String tenantId);

    /**
     * 批量更新启用状态
     */
    @Update("<script>" +
            "UPDATE BUDGET_TASK SET IS_ENABLED = #{isEnabled}, UPDATE_TIME = NOW()" +
            " WHERE ID IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            " AND TENANT_ID = #{tenantId}" +
            "</script>")
    int batchUpdateEnabled(@Param("ids") List<String> ids, @Param("isEnabled") Boolean isEnabled, @Param("tenantId") String tenantId);

    /**
     * 统计各任务类型的数量
     */
    @Select("SELECT TASK_TYPE as type, COUNT(*) as count FROM BUDGET_TASK WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0 GROUP BY TASK_TYPE")
    List<Map<String, Object>> countByTaskType(@Param("tenantId") String tenantId);

    /**
     * 统计各任务状态的数量
     */
    @Select("SELECT TASK_STATUS as status, COUNT(*) as count FROM BUDGET_TASK WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0 GROUP BY TASK_STATUS")
    List<Map<String, Object>> countByTaskStatus(@Param("tenantId") String tenantId);

    /**
     * 统计各预算年度的任务数量
     */
    @Select("SELECT BUDGET_YEAR as year, COUNT(*) as count FROM BUDGET_TASK WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0 GROUP BY BUDGET_YEAR ORDER BY BUDGET_YEAR DESC")
    List<Map<String, Object>> countByBudgetYear(@Param("tenantId") String tenantId);

    /**
     * 检查任务编码是否存在
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM BUDGET_TASK WHERE TASK_CODE = #{taskCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0" +
            "<if test='excludeId != null and excludeId != \"\"'>" +
            " AND ID != #{excludeId}" +
            "</if>" +
            "</script>")
    int checkTaskCodeExists(@Param("taskCode") String taskCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 更新任务进度
     */
    @Update("UPDATE BUDGET_TASK SET PROGRESS = #{progress}, UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int updateTaskProgress(@Param("id") String id, @Param("progress") Integer progress, @Param("tenantId") String tenantId);

    /**
     * 启动任务
     */
    @Update("UPDATE BUDGET_TASK SET TASK_STATUS = 'in_progress', START_TIME = NOW(), UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int startTask(@Param("id") String id, @Param("tenantId") String tenantId);

    /**
     * 完成任务
     */
    @Update("UPDATE BUDGET_TASK SET TASK_STATUS = 'completed', COMPLETE_TIME = NOW(), PROGRESS = 100, UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int completeTask(@Param("id") String id, @Param("tenantId") String tenantId);

    /**
     * 取消任务
     */
    @Update("UPDATE BUDGET_TASK SET TASK_STATUS = 'cancelled', UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int cancelTask(@Param("id") String id, @Param("tenantId") String tenantId);

    /**
     * 重置任务
     */
    @Update("UPDATE BUDGET_TASK SET TASK_STATUS = 'draft', PROGRESS = 0, START_TIME = NULL, COMPLETE_TIME = NULL, UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int resetTask(@Param("id") String id, @Param("tenantId") String tenantId);

    /**
     * 分配任务
     */
    @Update("UPDATE BUDGET_TASK SET ASSIGNEE_ID = #{assigneeId}, ASSIGNEE_NAME = #{assigneeName}, TASK_STATUS = 'assigned', UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int assignTask(@Param("id") String id, @Param("assigneeId") String assigneeId, @Param("assigneeName") String assigneeName, @Param("tenantId") String tenantId);

    /**
     * 提交审批
     */
    @Update("UPDATE BUDGET_TASK SET TASK_STATUS = 'pending_approval', SUBMIT_TIME = NOW(), UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int submitForApproval(@Param("id") String id, @Param("tenantId") String tenantId);

    /**
     * 审批通过
     */
    @Update("UPDATE BUDGET_TASK SET TASK_STATUS = 'approved', APPROVAL_TIME = NOW(), UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int approveTask(@Param("id") String id, @Param("tenantId") String tenantId);

    /**
     * 审批拒绝
     */
    @Update("UPDATE BUDGET_TASK SET TASK_STATUS = 'rejected', APPROVAL_TIME = NOW(), UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int rejectTask(@Param("id") String id, @Param("tenantId") String tenantId);

    /**
     * 查询我的任务（创建的任务）
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE CREATE_BY = #{userId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectMyCreatedTasks(@Param("userId") String userId, @Param("tenantId") String tenantId);

    /**
     * 查询分配给我的任务
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE ASSIGNEE_ID = #{userId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectMyAssignedTasks(@Param("userId") String userId, @Param("tenantId") String tenantId);

    /**
     * 查询我参与的任务
     */
    @Select("SELECT * FROM BUDGET_TASK WHERE (CREATE_BY = #{userId} OR ASSIGNEE_ID = #{userId} OR PARTICIPANTS LIKE CONCAT('%', #{userId}, '%')) AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTask> selectMyParticipatedTasks(@Param("userId") String userId, @Param("tenantId") String tenantId);

    /**
     * 同步任务数据
     */
    @Update("UPDATE BUDGET_TASK SET SYNC_TIME = NOW(), SYNC_STATUS = #{syncStatus} WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int syncTaskData(@Param("id") String id, @Param("syncStatus") String syncStatus, @Param("tenantId") String tenantId);
}
