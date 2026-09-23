package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetApprovalFlow;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算审批流程Mapper接口
 * 
 * @description 预算审批流程数据访问层接口，兼容达梦数据库和MySQL数据库
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetApprovalFlowMapper extends BaseMapper<BudgetApprovalFlow> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据流程编码查询审批流程
     * @param flowCode 流程编码
     * @param tenantId 租户ID
     * @return 审批流程信息
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE FLOW_CODE = #{flowCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetApprovalFlow selectByFlowCode(@Param("flowCode") String flowCode, @Param("tenantId") String tenantId);

    /**
     * 根据流程名称查询审批流程
     * @param flowName 流程名称
     * @param tenantId 租户ID
     * @return 审批流程列表
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE FLOW_NAME LIKE CONCAT('%', #{flowName}, '%') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetApprovalFlow> selectByFlowName(@Param("flowName") String flowName, @Param("tenantId") String tenantId);

    /**
     * 根据流程类型查询审批流程
     * @param flowType 流程类型
     * @param tenantId 租户ID
     * @return 审批流程列表
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE FLOW_TYPE = #{flowType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetApprovalFlow> selectByFlowType(@Param("flowType") String flowType, @Param("tenantId") String tenantId);

    /**
     * 根据适用组织查询审批流程
     * @param applicableOrgId 适用组织ID
     * @param tenantId 租户ID
     * @return 审批流程列表
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE APPLICABLE_ORG_ID = #{applicableOrgId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetApprovalFlow> selectByApplicableOrgId(@Param("applicableOrgId") String applicableOrgId, @Param("tenantId") String tenantId);

    /**
     * 根据状态查询审批流程
     * @param status 状态
     * @param tenantId 租户ID
     * @return 审批流程列表
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE STATUS = #{status} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetApprovalFlow> selectByStatus(@Param("status") String status, @Param("tenantId") String tenantId);

    /**
     * 查询默认审批流程
     * @param flowType 流程类型
     * @param tenantId 租户ID
     * @return 默认审批流程
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE FLOW_TYPE = #{flowType} AND IS_DEFAULT = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 LIMIT 1")
    BudgetApprovalFlow selectDefaultFlow(@Param("flowType") String flowType, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询审批流程
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetApprovalFlow> selectApprovalFlowPage(Page<BudgetApprovalFlow> page, @Param("params") Map<String, Object> params);

    /**
     * 查询已发布的审批流程
     * @param tenantId 租户ID
     * @return 已发布的审批流程列表
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE STATUS = 'published' AND IS_ENABLED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetApprovalFlow> selectPublishedFlows(@Param("tenantId") String tenantId);

    /**
     * 查询可用的审批流程
     * @param flowType 流程类型
     * @param applicableOrgId 适用组织ID
     * @param tenantId 租户ID
     * @return 可用的审批流程列表
     */
    List<BudgetApprovalFlow> selectAvailableFlows(@Param("flowType") String flowType,
                                                 @Param("applicableOrgId") String applicableOrgId,
                                                 @Param("tenantId") String tenantId);

    /**
     * 查询流程版本列表
     * @param flowCode 流程编码
     * @param tenantId 租户ID
     * @return 流程版本列表
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE FLOW_CODE = #{flowCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY FLOW_VERSION DESC")
    List<BudgetApprovalFlow> selectFlowVersions(@Param("flowCode") String flowCode, @Param("tenantId") String tenantId);

    /**
     * 查询最新版本的流程
     * @param flowCode 流程编码
     * @param tenantId 租户ID
     * @return 最新版本的流程
     */
    @Select("SELECT * FROM BUDGET_APPROVAL_FLOW WHERE FLOW_CODE = #{flowCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY FLOW_VERSION DESC LIMIT 1")
    BudgetApprovalFlow selectLatestFlowVersion(@Param("flowCode") String flowCode, @Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计审批流程数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    Map<String, Object> selectApprovalFlowStatistics(@Param("tenantId") String tenantId);

    /**
     * 按类型统计审批流程数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectApprovalFlowCountByType(@Param("tenantId") String tenantId);

    /**
     * 按状态统计审批流程数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectApprovalFlowCountByStatus(@Param("tenantId") String tenantId);

    /**
     * 统计流程使用情况
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectFlowUsageStatistics(@Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 发布审批流程
     * @param flowId 流程ID
     * @param publishTime 发布时间
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_APPROVAL_FLOW SET STATUS = 'published', PUBLISH_TIME = #{publishTime}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{flowId}")
    int publishApprovalFlow(@Param("flowId") String flowId,
                           @Param("publishTime") LocalDateTime publishTime,
                           @Param("updateBy") String updateBy,
                           @Param("updateTime") LocalDateTime updateTime);

    /**
     * 归档审批流程
     * @param flowId 流程ID
     * @param archiveTime 归档时间
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_APPROVAL_FLOW SET STATUS = 'archived', ARCHIVE_TIME = #{archiveTime}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{flowId}")
    int archiveApprovalFlow(@Param("flowId") String flowId,
                           @Param("archiveTime") LocalDateTime archiveTime,
                           @Param("updateBy") String updateBy,
                           @Param("updateTime") LocalDateTime updateTime);

    /**
     * 启用审批流程
     * @param flowId 流程ID
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_APPROVAL_FLOW SET IS_ENABLED = 1, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{flowId}")
    int enableApprovalFlow(@Param("flowId") String flowId,
                          @Param("updateBy") String updateBy,
                          @Param("updateTime") LocalDateTime updateTime);

    /**
     * 禁用审批流程
     * @param flowId 流程ID
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_APPROVAL_FLOW SET IS_ENABLED = 0, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{flowId}")
    int disableApprovalFlow(@Param("flowId") String flowId,
                           @Param("updateBy") String updateBy,
                           @Param("updateTime") LocalDateTime updateTime);

    /**
     * 设置默认审批流程
     * @param flowId 流程ID
     * @param flowType 流程类型
     * @param tenantId 租户ID
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int setDefaultApprovalFlow(@Param("flowId") String flowId,
                              @Param("flowType") String flowType,
                              @Param("tenantId") String tenantId,
                              @Param("updateBy") String updateBy,
                              @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量更新流程状态
     * @param flowIds 流程ID列表
     * @param status 新状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateFlowStatus(@Param("flowIds") List<String> flowIds,
                             @Param("status") String status,
                             @Param("updateBy") String updateBy,
                             @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理草稿状态的流程
     * @param expiryTime 过期时间
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupDraftFlows(@Param("expiryTime") LocalDateTime expiryTime, @Param("tenantId") String tenantId);

    /**
     * 清理已归档的流程
     * @param archiveTime 归档时间
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupArchivedFlows(@Param("archiveTime") LocalDateTime archiveTime, @Param("tenantId") String tenantId);
}
