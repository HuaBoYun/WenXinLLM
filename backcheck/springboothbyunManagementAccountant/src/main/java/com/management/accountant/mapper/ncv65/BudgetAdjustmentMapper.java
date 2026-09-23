package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetAdjustment;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算调整Mapper接口
 * 
 * @description 预算调整数据访问层，支持预算调整的申请、审批和执行
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetAdjustmentMapper extends BaseMapper<BudgetAdjustment> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据调整编码查询预算调整
     * @param adjustmentCode 调整编码
     * @param tenantId 租户ID
     * @return 预算调整信息
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE ADJUSTMENT_CODE = #{adjustmentCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetAdjustment selectByAdjustmentCode(@Param("adjustmentCode") String adjustmentCode, @Param("tenantId") String tenantId);

    /**
     * 根据调整名称查询预算调整
     * @param adjustmentName 调整名称
     * @param tenantId 租户ID
     * @return 预算调整信息
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE ADJUSTMENT_NAME = #{adjustmentName} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetAdjustment selectByAdjustmentName(@Param("adjustmentName") String adjustmentName, @Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询调整列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAdjustment> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 根据调整类型查询调整列表
     * @param adjustmentType 调整类型
     * @param tenantId 租户ID
     * @return 调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE ADJUSTMENT_TYPE = #{adjustmentType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAdjustment> selectByAdjustmentType(@Param("adjustmentType") String adjustmentType, @Param("tenantId") String tenantId);

    /**
     * 根据调整分类查询调整列表
     * @param adjustmentCategory 调整分类
     * @param tenantId 租户ID
     * @return 调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE ADJUSTMENT_CATEGORY = #{adjustmentCategory} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAdjustment> selectByAdjustmentCategory(@Param("adjustmentCategory") String adjustmentCategory, @Param("tenantId") String tenantId);

    /**
     * 根据调整级别查询调整列表
     * @param adjustmentLevel 调整级别
     * @param tenantId 租户ID
     * @return 调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE ADJUSTMENT_LEVEL = #{adjustmentLevel} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAdjustment> selectByAdjustmentLevel(@Param("adjustmentLevel") String adjustmentLevel, @Param("tenantId") String tenantId);

    /**
     * 根据审批状态查询调整列表
     * @param approvalStatus 审批状态
     * @param tenantId 租户ID
     * @return 调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE APPROVAL_STATUS = #{approvalStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAdjustment> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus, @Param("tenantId") String tenantId);

    /**
     * 根据执行状态查询调整列表
     * @param executionStatus 执行状态
     * @param tenantId 租户ID
     * @return 调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE EXECUTION_STATUS = #{executionStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAdjustment> selectByExecutionStatus(@Param("executionStatus") String executionStatus, @Param("tenantId") String tenantId);

    /**
     * 根据组织ID查询调整列表
     * @param organizationId 组织ID
     * @param tenantId 租户ID
     * @return 调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE ORGANIZATION_ID = #{organizationId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAdjustment> selectByOrganizationId(@Param("organizationId") String organizationId, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算调整
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetAdjustment> selectBudgetAdjustmentPage(Page<BudgetAdjustment> page, @Param("params") Map<String, Object> params);

    /**
     * 查询我申请的调整列表
     * @param applicantId 申请人ID
     * @param tenantId 租户ID
     * @return 我申请的调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE APPLICANT_ID = #{applicantId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY APPLICATION_TIME DESC")
    List<BudgetAdjustment> selectMyApplications(@Param("applicantId") String applicantId, @Param("tenantId") String tenantId);

    /**
     * 查询待我审批的调整列表
     * @param approverId 审批人ID
     * @param tenantId 租户ID
     * @return 待我审批的调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE CURRENT_APPROVER_ID = #{approverId} AND APPROVAL_STATUS IN ('submitted', 'reviewing') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY APPLICATION_TIME DESC")
    List<BudgetAdjustment> selectPendingApprovals(@Param("approverId") String approverId, @Param("tenantId") String tenantId);

    /**
     * 查询我审批过的调整列表
     * @param approverId 审批人ID
     * @param tenantId 租户ID
     * @return 我审批过的调整列表
     */
    List<BudgetAdjustment> selectMyApprovals(@Param("approverId") String approverId, @Param("tenantId") String tenantId);

    /**
     * 查询待执行的调整列表
     * @param executorId 执行人ID
     * @param tenantId 租户ID
     * @return 待执行的调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE EXECUTOR_ID = #{executorId} AND EXECUTION_STATUS = 'pending' AND APPROVAL_STATUS = 'approved' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY APPROVAL_END_TIME DESC")
    List<BudgetAdjustment> selectPendingExecutions(@Param("executorId") String executorId, @Param("tenantId") String tenantId);

    /**
     * 查询紧急调整列表
     * @param tenantId 租户ID
     * @return 紧急调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE IS_URGENT = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY APPLICATION_TIME DESC")
    List<BudgetAdjustment> selectUrgentAdjustments(@Param("tenantId") String tenantId);

    /**
     * 查询批量调整列表
     * @param tenantId 租户ID
     * @return 批量调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE IS_BATCH = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY APPLICATION_TIME DESC")
    List<BudgetAdjustment> selectBatchAdjustments(@Param("tenantId") String tenantId);

    /**
     * 查询自动执行的调整列表
     * @param tenantId 租户ID
     * @return 自动执行的调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE IS_AUTO_EXECUTE = 1 AND APPROVAL_STATUS = 'approved' AND EXECUTION_STATUS = 'pending' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY APPROVAL_END_TIME DESC")
    List<BudgetAdjustment> selectAutoExecuteAdjustments(@Param("tenantId") String tenantId);

    /**
     * 查询执行失败的调整列表
     * @param tenantId 租户ID
     * @return 执行失败的调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE EXECUTION_STATUS = 'failed' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY EXECUTION_TIME DESC")
    List<BudgetAdjustment> selectFailedAdjustments(@Param("tenantId") String tenantId);

    /**
     * 查询需要回滚的调整列表
     * @param tenantId 租户ID
     * @return 需要回滚的调整列表
     */
    @Select("SELECT * FROM BUDGET_ADJUSTMENT WHERE IS_ROLLBACK_REQUIRED = 1 AND EXECUTION_STATUS = 'completed' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY EXECUTION_TIME DESC")
    List<BudgetAdjustment> selectRollbackRequiredAdjustments(@Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计调整总数
     * @param tenantId 租户ID
     * @return 调整总数
     */
    @Select("SELECT COUNT(*) FROM BUDGET_ADJUSTMENT WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countTotalAdjustments(@Param("tenantId") String tenantId);

    /**
     * 按调整类型统计数量
     * @param tenantId 租户ID
     * @return 各类型调整数量统计
     */
    List<Map<String, Object>> countAdjustmentsByType(@Param("tenantId") String tenantId);

    /**
     * 按调整分类统计数量
     * @param tenantId 租户ID
     * @return 各分类调整数量统计
     */
    List<Map<String, Object>> countAdjustmentsByCategory(@Param("tenantId") String tenantId);

    /**
     * 按审批状态统计数量
     * @param tenantId 租户ID
     * @return 各审批状态调整数量统计
     */
    List<Map<String, Object>> countAdjustmentsByApprovalStatus(@Param("tenantId") String tenantId);

    /**
     * 按执行状态统计数量
     * @param tenantId 租户ID
     * @return 各执行状态调整数量统计
     */
    List<Map<String, Object>> countAdjustmentsByExecutionStatus(@Param("tenantId") String tenantId);

    /**
     * 按年度统计调整数量
     * @param tenantId 租户ID
     * @return 各年度调整数量统计
     */
    List<Map<String, Object>> countAdjustmentsByYear(@Param("tenantId") String tenantId);

    /**
     * 按月份统计调整数量
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各月份调整数量统计
     */
    List<Map<String, Object>> countAdjustmentsByMonth(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计调整金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 调整金额统计
     */
    Map<String, Object> sumAdjustmentAmounts(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 获取调整统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectAdjustmentStatistics(@Param("tenantId") String tenantId);

    /**
     * 获取用户调整统计信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户调整统计信息
     */
    Map<String, Object> selectUserAdjustmentStatistics(@Param("userId") String userId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 提交审批
     * @param adjustmentId 调整ID
     * @param workflowId 审批流程ID
     * @param currentNodeId 当前节点ID
     * @param currentApproverId 当前审批人ID
     * @param currentApproverName 当前审批人姓名
     * @param approvalStartTime 审批开始时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ADJUSTMENT SET APPROVAL_STATUS = 'submitted', WORKFLOW_ID = #{workflowId}, CURRENT_NODE_ID = #{currentNodeId}, CURRENT_APPROVER_ID = #{currentApproverId}, CURRENT_APPROVER_NAME = #{currentApproverName}, APPROVAL_START_TIME = #{approvalStartTime} WHERE ID = #{adjustmentId}")
    int submitForApproval(@Param("adjustmentId") String adjustmentId, @Param("workflowId") String workflowId, 
                         @Param("currentNodeId") String currentNodeId, @Param("currentApproverId") String currentApproverId, 
                         @Param("currentApproverName") String currentApproverName, @Param("approvalStartTime") LocalDateTime approvalStartTime);

    /**
     * 审批通过
     * @param adjustmentId 调整ID
     * @param approvalComments 审批意见
     * @param approvalEndTime 审批结束时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ADJUSTMENT SET APPROVAL_STATUS = 'approved', APPROVAL_COMMENTS = #{approvalComments}, APPROVAL_END_TIME = #{approvalEndTime} WHERE ID = #{adjustmentId}")
    int approveAdjustment(@Param("adjustmentId") String adjustmentId, @Param("approvalComments") String approvalComments, @Param("approvalEndTime") LocalDateTime approvalEndTime);

    /**
     * 审批拒绝
     * @param adjustmentId 调整ID
     * @param approvalComments 审批意见
     * @param approvalEndTime 审批结束时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ADJUSTMENT SET APPROVAL_STATUS = 'rejected', APPROVAL_COMMENTS = #{approvalComments}, APPROVAL_END_TIME = #{approvalEndTime} WHERE ID = #{adjustmentId}")
    int rejectAdjustment(@Param("adjustmentId") String adjustmentId, @Param("approvalComments") String approvalComments, @Param("approvalEndTime") LocalDateTime approvalEndTime);

    /**
     * 开始执行
     * @param adjustmentId 调整ID
     * @param executorId 执行人ID
     * @param executorName 执行人姓名
     * @param executionTime 执行时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ADJUSTMENT SET EXECUTION_STATUS = 'executing', EXECUTOR_ID = #{executorId}, EXECUTOR_NAME = #{executorName}, EXECUTION_TIME = #{executionTime} WHERE ID = #{adjustmentId}")
    int startExecution(@Param("adjustmentId") String adjustmentId, @Param("executorId") String executorId, 
                      @Param("executorName") String executorName, @Param("executionTime") LocalDateTime executionTime);

    /**
     * 完成执行
     * @param adjustmentId 调整ID
     * @param executionResult 执行结果
     * @param executionTime 执行时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ADJUSTMENT SET EXECUTION_STATUS = 'completed', EXECUTION_RESULT = #{executionResult}, EXECUTION_TIME = #{executionTime} WHERE ID = #{adjustmentId}")
    int completeExecution(@Param("adjustmentId") String adjustmentId, @Param("executionResult") String executionResult, @Param("executionTime") LocalDateTime executionTime);

    /**
     * 执行失败
     * @param adjustmentId 调整ID
     * @param executionError 执行错误信息
     * @param executionTime 执行时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ADJUSTMENT SET EXECUTION_STATUS = 'failed', EXECUTION_ERROR = #{executionError}, EXECUTION_TIME = #{executionTime} WHERE ID = #{adjustmentId}")
    int failExecution(@Param("adjustmentId") String adjustmentId, @Param("executionError") String executionError, @Param("executionTime") LocalDateTime executionTime);

    /**
     * 取消调整
     * @param adjustmentId 调整ID
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ADJUSTMENT SET APPROVAL_STATUS = 'cancelled', STATUS = 'cancelled' WHERE ID = #{adjustmentId}")
    int cancelAdjustment(@Param("adjustmentId") String adjustmentId);

    /**
     * 批量更新调整状态
     * @param adjustmentIds 调整ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateAdjustmentStatus(@Param("adjustmentIds") List<String> adjustmentIds, @Param("status") String status, 
                                   @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理已取消的调整
     * @param days 取消天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupCancelledAdjustments(@Param("days") Integer days, @Param("tenantId") String tenantId);

    /**
     * 清理已完成的调整
     * @param days 完成天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupCompletedAdjustments(@Param("days") Integer days, @Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查调整编码是否存在
     * @param adjustmentCode 调整编码
     * @param excludeId 排除的调整ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_ADJUSTMENT WHERE ADJUSTMENT_CODE = #{adjustmentCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkAdjustmentCodeExists(@Param("adjustmentCode") String adjustmentCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查调整名称是否存在
     * @param adjustmentName 调整名称
     * @param excludeId 排除的调整ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_ADJUSTMENT WHERE ADJUSTMENT_NAME = #{adjustmentName} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkAdjustmentNameExists(@Param("adjustmentName") String adjustmentName, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);
}
