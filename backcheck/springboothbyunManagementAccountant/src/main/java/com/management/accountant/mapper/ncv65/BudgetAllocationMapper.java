package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetAllocation;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算分配Mapper接口
 * 
 * @description 预算分配数据访问层，支持预算资源的分配、调配和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetAllocationMapper extends BaseMapper<BudgetAllocation> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据分配编码查询预算分配
     * @param allocationCode 分配编码
     * @param tenantId 租户ID
     * @return 预算分配信息
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE ALLOCATION_CODE = #{allocationCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetAllocation selectByAllocationCode(@Param("allocationCode") String allocationCode, @Param("tenantId") String tenantId);

    /**
     * 根据分配名称查询预算分配
     * @param allocationName 分配名称
     * @param tenantId 租户ID
     * @return 预算分配信息
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE ALLOCATION_NAME = #{allocationName} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetAllocation selectByAllocationName(@Param("allocationName") String allocationName, @Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询分配列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAllocation> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 根据分配类型查询分配列表
     * @param allocationType 分配类型
     * @param tenantId 租户ID
     * @return 分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE ALLOCATION_TYPE = #{allocationType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAllocation> selectByAllocationType(@Param("allocationType") String allocationType, @Param("tenantId") String tenantId);

    /**
     * 根据分配方式查询分配列表
     * @param allocationMethod 分配方式
     * @param tenantId 租户ID
     * @return 分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE ALLOCATION_METHOD = #{allocationMethod} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAllocation> selectByAllocationMethod(@Param("allocationMethod") String allocationMethod, @Param("tenantId") String tenantId);

    /**
     * 根据源组织ID查询分配列表
     * @param sourceOrganizationId 源组织ID
     * @param tenantId 租户ID
     * @return 分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE SOURCE_ORGANIZATION_ID = #{sourceOrganizationId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAllocation> selectBySourceOrganization(@Param("sourceOrganizationId") String sourceOrganizationId, @Param("tenantId") String tenantId);

    /**
     * 根据目标组织ID查询分配列表
     * @param targetOrganizationId 目标组织ID
     * @param tenantId 租户ID
     * @return 分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE TARGET_ORGANIZATION_ID = #{targetOrganizationId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAllocation> selectByTargetOrganization(@Param("targetOrganizationId") String targetOrganizationId, @Param("tenantId") String tenantId);

    /**
     * 根据指标ID查询分配列表
     * @param indicatorId 指标ID
     * @param tenantId 租户ID
     * @return 分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE INDICATOR_ID = #{indicatorId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAllocation> selectByIndicatorId(@Param("indicatorId") String indicatorId, @Param("tenantId") String tenantId);

    /**
     * 根据审批状态查询分配列表
     * @param approvalStatus 审批状态
     * @param tenantId 租户ID
     * @return 分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE APPROVAL_STATUS = #{approvalStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAllocation> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus, @Param("tenantId") String tenantId);

    /**
     * 根据执行状态查询分配列表
     * @param executionStatus 执行状态
     * @param tenantId 租户ID
     * @return 分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE EXECUTION_STATUS = #{executionStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetAllocation> selectByExecutionStatus(@Param("executionStatus") String executionStatus, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算分配
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetAllocation> selectBudgetAllocationPage(Page<BudgetAllocation> page, @Param("params") Map<String, Object> params);

    /**
     * 查询我分配的预算列表
     * @param allocatorId 分配人ID
     * @param tenantId 租户ID
     * @return 我分配的预算列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE ALLOCATOR_ID = #{allocatorId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY ALLOCATION_TIME DESC")
    List<BudgetAllocation> selectMyAllocations(@Param("allocatorId") String allocatorId, @Param("tenantId") String tenantId);

    /**
     * 查询待我审批的分配列表
     * @param approverId 审批人ID
     * @param tenantId 租户ID
     * @return 待我审批的分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE APPROVER_ID = #{approverId} AND APPROVAL_STATUS = 'submitted' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY ALLOCATION_TIME DESC")
    List<BudgetAllocation> selectPendingApprovals(@Param("approverId") String approverId, @Param("tenantId") String tenantId);

    /**
     * 查询待执行的分配列表
     * @param executorId 执行人ID
     * @param tenantId 租户ID
     * @return 待执行的分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE EXECUTOR_ID = #{executorId} AND EXECUTION_STATUS = 'pending' AND APPROVAL_STATUS = 'approved' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY APPROVAL_TIME DESC")
    List<BudgetAllocation> selectPendingExecutions(@Param("executorId") String executorId, @Param("tenantId") String tenantId);

    /**
     * 查询自动分配列表
     * @param tenantId 租户ID
     * @return 自动分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE IS_AUTO_ALLOCATION = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY ALLOCATION_TIME DESC")
    List<BudgetAllocation> selectAutoAllocations(@Param("tenantId") String tenantId);

    /**
     * 查询批量分配列表
     * @param tenantId 租户ID
     * @return 批量分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE IS_BATCH_ALLOCATION = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY ALLOCATION_TIME DESC")
    List<BudgetAllocation> selectBatchAllocations(@Param("tenantId") String tenantId);

    /**
     * 查询可撤销的分配列表
     * @param tenantId 租户ID
     * @return 可撤销的分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE IS_REVOCABLE = 1 AND EXECUTION_STATUS != 'completed' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY ALLOCATION_TIME DESC")
    List<BudgetAllocation> selectRevocableAllocations(@Param("tenantId") String tenantId);

    /**
     * 查询执行失败的分配列表
     * @param tenantId 租户ID
     * @return 执行失败的分配列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE EXECUTION_STATUS = 'failed' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY EXECUTION_TIME DESC")
    List<BudgetAllocation> selectFailedAllocations(@Param("tenantId") String tenantId);

    /**
     * 查询组织间的分配关系
     * @param sourceOrganizationId 源组织ID
     * @param targetOrganizationId 目标组织ID
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 分配关系列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE SOURCE_ORGANIZATION_ID = #{sourceOrganizationId} AND TARGET_ORGANIZATION_ID = #{targetOrganizationId} AND FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY ALLOCATION_TIME DESC")
    List<BudgetAllocation> selectAllocationsBetweenOrganizations(@Param("sourceOrganizationId") String sourceOrganizationId, 
                                                                @Param("targetOrganizationId") String targetOrganizationId, 
                                                                @Param("fiscalYear") Integer fiscalYear, 
                                                                @Param("tenantId") String tenantId);

    /**
     * 查询指标的分配历史
     * @param indicatorId 指标ID
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 分配历史列表
     */
    @Select("SELECT * FROM BUDGET_ALLOCATION WHERE INDICATOR_ID = #{indicatorId} AND FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY ALLOCATION_TIME DESC")
    List<BudgetAllocation> selectIndicatorAllocationHistory(@Param("indicatorId") String indicatorId, 
                                                           @Param("fiscalYear") Integer fiscalYear, 
                                                           @Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计分配总数
     * @param tenantId 租户ID
     * @return 分配总数
     */
    @Select("SELECT COUNT(*) FROM BUDGET_ALLOCATION WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countTotalAllocations(@Param("tenantId") String tenantId);

    /**
     * 按分配类型统计数量
     * @param tenantId 租户ID
     * @return 各类型分配数量统计
     */
    List<Map<String, Object>> countAllocationsByType(@Param("tenantId") String tenantId);

    /**
     * 按分配方式统计数量
     * @param tenantId 租户ID
     * @return 各方式分配数量统计
     */
    List<Map<String, Object>> countAllocationsByMethod(@Param("tenantId") String tenantId);

    /**
     * 按审批状态统计数量
     * @param tenantId 租户ID
     * @return 各审批状态分配数量统计
     */
    List<Map<String, Object>> countAllocationsByApprovalStatus(@Param("tenantId") String tenantId);

    /**
     * 按执行状态统计数量
     * @param tenantId 租户ID
     * @return 各执行状态分配数量统计
     */
    List<Map<String, Object>> countAllocationsByExecutionStatus(@Param("tenantId") String tenantId);

    /**
     * 按年度统计分配数量
     * @param tenantId 租户ID
     * @return 各年度分配数量统计
     */
    List<Map<String, Object>> countAllocationsByYear(@Param("tenantId") String tenantId);

    /**
     * 按月份统计分配数量
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各月份分配数量统计
     */
    List<Map<String, Object>> countAllocationsByMonth(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计分配金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 分配金额统计
     */
    Map<String, Object> sumAllocationAmounts(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按组织统计分配金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各组织分配金额统计
     */
    List<Map<String, Object>> sumAllocationAmountsByOrganization(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按指标统计分配金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各指标分配金额统计
     */
    List<Map<String, Object>> sumAllocationAmountsByIndicator(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 获取分配统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectAllocationStatistics(@Param("tenantId") String tenantId);

    /**
     * 获取用户分配统计信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户分配统计信息
     */
    Map<String, Object> selectUserAllocationStatistics(@Param("userId") String userId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 审批通过
     * @param allocationId 分配ID
     * @param approvalComments 审批意见
     * @param approvalTime 审批时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ALLOCATION SET APPROVAL_STATUS = 'approved', APPROVAL_COMMENTS = #{approvalComments}, APPROVAL_TIME = #{approvalTime} WHERE ID = #{allocationId}")
    int approveAllocation(@Param("allocationId") String allocationId, @Param("approvalComments") String approvalComments, @Param("approvalTime") LocalDateTime approvalTime);

    /**
     * 审批拒绝
     * @param allocationId 分配ID
     * @param approvalComments 审批意见
     * @param approvalTime 审批时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ALLOCATION SET APPROVAL_STATUS = 'rejected', APPROVAL_COMMENTS = #{approvalComments}, APPROVAL_TIME = #{approvalTime} WHERE ID = #{allocationId}")
    int rejectAllocation(@Param("allocationId") String allocationId, @Param("approvalComments") String approvalComments, @Param("approvalTime") LocalDateTime approvalTime);

    /**
     * 开始执行
     * @param allocationId 分配ID
     * @param executorId 执行人ID
     * @param executorName 执行人姓名
     * @param executionTime 执行时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ALLOCATION SET EXECUTION_STATUS = 'executing', EXECUTOR_ID = #{executorId}, EXECUTOR_NAME = #{executorName}, EXECUTION_TIME = #{executionTime} WHERE ID = #{allocationId}")
    int startExecution(@Param("allocationId") String allocationId, @Param("executorId") String executorId, 
                      @Param("executorName") String executorName, @Param("executionTime") LocalDateTime executionTime);

    /**
     * 完成执行
     * @param allocationId 分配ID
     * @param executionResult 执行结果
     * @param executionTime 执行时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ALLOCATION SET EXECUTION_STATUS = 'completed', EXECUTION_RESULT = #{executionResult}, EXECUTION_TIME = #{executionTime} WHERE ID = #{allocationId}")
    int completeExecution(@Param("allocationId") String allocationId, @Param("executionResult") String executionResult, @Param("executionTime") LocalDateTime executionTime);

    /**
     * 执行失败
     * @param allocationId 分配ID
     * @param executionError 执行错误信息
     * @param executionTime 执行时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ALLOCATION SET EXECUTION_STATUS = 'failed', EXECUTION_ERROR = #{executionError}, EXECUTION_TIME = #{executionTime} WHERE ID = #{allocationId}")
    int failExecution(@Param("allocationId") String allocationId, @Param("executionError") String executionError, @Param("executionTime") LocalDateTime executionTime);

    /**
     * 撤销分配
     * @param allocationId 分配ID
     * @param revocationInfo 撤销信息
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_ALLOCATION SET STATUS = 'cancelled', REVOCATION_INFO = #{revocationInfo} WHERE ID = #{allocationId}")
    int revokeAllocation(@Param("allocationId") String allocationId, @Param("revocationInfo") String revocationInfo);

    /**
     * 批量更新分配状态
     * @param allocationIds 分配ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateAllocationStatus(@Param("allocationIds") List<String> allocationIds, @Param("status") String status, 
                                   @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理已取消的分配
     * @param days 取消天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupCancelledAllocations(@Param("days") Integer days, @Param("tenantId") String tenantId);

    /**
     * 清理已完成的分配
     * @param days 完成天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupCompletedAllocations(@Param("days") Integer days, @Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查分配编码是否存在
     * @param allocationCode 分配编码
     * @param excludeId 排除的分配ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_ALLOCATION WHERE ALLOCATION_CODE = #{allocationCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkAllocationCodeExists(@Param("allocationCode") String allocationCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查分配名称是否存在
     * @param allocationName 分配名称
     * @param excludeId 排除的分配ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_ALLOCATION WHERE ALLOCATION_NAME = #{allocationName} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkAllocationNameExists(@Param("allocationName") String allocationName, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查分配冲突
     * @param sourceOrganizationId 源组织ID
     * @param targetOrganizationId 目标组织ID
     * @param indicatorId 指标ID
     * @param allocationPeriod 分配期间
     * @param excludeId 排除的分配ID
     * @param tenantId 租户ID
     * @return 是否存在冲突
     */
    @Select("SELECT COUNT(*) FROM BUDGET_ALLOCATION WHERE SOURCE_ORGANIZATION_ID = #{sourceOrganizationId} AND TARGET_ORGANIZATION_ID = #{targetOrganizationId} AND INDICATOR_ID = #{indicatorId} AND ALLOCATION_PERIOD = #{allocationPeriod} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkAllocationConflict(@Param("sourceOrganizationId") String sourceOrganizationId, 
                               @Param("targetOrganizationId") String targetOrganizationId, 
                               @Param("indicatorId") String indicatorId, 
                               @Param("allocationPeriod") String allocationPeriod, 
                               @Param("excludeId") String excludeId, 
                               @Param("tenantId") String tenantId);
}
