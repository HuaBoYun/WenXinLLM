package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetWorkflow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算工作流数据访问接口
 * 
 * @description 预算工作流数据访问层，提供预算工作流的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetWorkflowMapper extends BaseMapper<BudgetWorkflow> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据工作流编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CODE = #{workflowCode} AND IS_DELETED = 0")
    BudgetWorkflow selectByWorkflowCode(@Param("workflowCode") String workflowCode);

    /**
     * 根据工作流类型查询工作流列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_TYPE = #{workflowType} AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectByWorkflowType(@Param("workflowType") String workflowType);

    /**
     * 根据工作流分类查询工作流列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CATEGORY = #{workflowCategory} AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectByWorkflowCategory(@Param("workflowCategory") String workflowCategory);

    /**
     * 根据工作流状态查询工作流列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_STATUS = #{workflowStatus} AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectByWorkflowStatus(@Param("workflowStatus") String workflowStatus);

    /**
     * 根据适用组织ID查询工作流列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE APPLICABLE_ORGANIZATION_ID = #{organizationId} AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据适用范围查询工作流列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE APPLICABLE_SCOPE = #{applicableScope} AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectByApplicableScope(@Param("applicableScope") String applicableScope);

    /**
     * 根据优先级查询工作流列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE PRIORITY = #{priority} AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectByPriority(@Param("priority") String priority);

    /**
     * 查询默认工作流
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE IS_DEFAULT = 1 AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0")
    List<BudgetWorkflow> selectDefaultWorkflows();

    /**
     * 根据工作流类型查询默认工作流
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_TYPE = #{workflowType} AND IS_DEFAULT = 1 AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0")
    BudgetWorkflow selectDefaultWorkflowByType(@Param("workflowType") String workflowType);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算工作流
     */
    IPage<BudgetWorkflow> selectBudgetWorkflowPage(Page<BudgetWorkflow> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的工作流列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectActiveWorkflows();

    /**
     * 查询可用的工作流列表
     */
    List<BudgetWorkflow> selectAvailableWorkflows(@Param("workflowType") String workflowType, 
                                                 @Param("organizationId") String organizationId,
                                                 @Param("currentDate") LocalDateTime currentDate);

    /**
     * 根据条件匹配工作流
     */
    List<BudgetWorkflow> selectMatchingWorkflows(@Param("workflowType") String workflowType,
                                               @Param("organizationId") String organizationId,
                                               @Param("conditions") Map<String, Object> conditions);

    /**
     * 查询工作流版本列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CODE = #{workflowCode} AND IS_DELETED = 0 ORDER BY WORKFLOW_VERSION DESC")
    List<BudgetWorkflow> selectWorkflowVersions(@Param("workflowCode") String workflowCode);

    /**
     * 查询最新版本工作流
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CODE = #{workflowCode} AND IS_DELETED = 0 ORDER BY WORKFLOW_VERSION DESC LIMIT 1")
    BudgetWorkflow selectLatestWorkflowVersion(@Param("workflowCode") String workflowCode);

    /**
     * 查询即将过期的工作流
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE EXPIRY_DATE IS NOT NULL AND EXPIRY_DATE <= #{expiryDate} AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0")
    List<BudgetWorkflow> selectExpiringWorkflows(@Param("expiryDate") LocalDateTime expiryDate);

    /**
     * 查询已过期的工作流
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE EXPIRY_DATE IS NOT NULL AND EXPIRY_DATE < #{currentDate} AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0")
    List<BudgetWorkflow> selectExpiredWorkflows(@Param("currentDate") LocalDateTime currentDate);

    /**
     * 查询工作流统计信息
     */
    Map<String, Object> selectWorkflowStatistics();

    /**
     * 根据关键字搜索工作流
     */
    List<BudgetWorkflow> searchWorkflows(@Param("keyword") String keyword);

    /**
     * 查询高优先级工作流
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE PRIORITY = 'HIGH' AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectHighPriorityWorkflows();

    /**
     * 查询紧急工作流
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CATEGORY = 'EMERGENCY' AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectEmergencyWorkflows();

    /**
     * 查询自定义工作流
     */
    @Select("SELECT * FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CATEGORY = 'CUSTOM' AND WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, WORKFLOW_CODE")
    List<BudgetWorkflow> selectCustomWorkflows();

    // ==================== 业务操作方法 ====================

    /**
     * 激活工作流
     */
    int activateWorkflow(@Param("workflowId") String workflowId, @Param("updateBy") String updateBy);

    /**
     * 停用工作流
     */
    int deactivateWorkflow(@Param("workflowId") String workflowId, @Param("updateBy") String updateBy);

    /**
     * 归档工作流
     */
    int archiveWorkflow(@Param("workflowId") String workflowId, @Param("updateBy") String updateBy);

    /**
     * 发布工作流
     */
    int publishWorkflow(@Param("workflowId") String workflowId, 
                       @Param("publishedBy") String publishedBy,
                       @Param("publishedTime") LocalDateTime publishedTime,
                       @Param("updateBy") String updateBy);

    /**
     * 设置默认工作流
     */
    int setDefaultWorkflow(@Param("workflowId") String workflowId, @Param("workflowType") String workflowType, @Param("updateBy") String updateBy);

    /**
     * 清除默认工作流标记
     */
    int clearDefaultWorkflow(@Param("workflowType") String workflowType, @Param("updateBy") String updateBy);

    /**
     * 批量更新工作流状态
     */
    int batchUpdateWorkflowStatus(@Param("workflowIds") List<String> workflowIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量激活工作流
     */
    int batchActivateWorkflows(@Param("workflowIds") List<String> workflowIds, @Param("updateBy") String updateBy);

    /**
     * 批量停用工作流
     */
    int batchDeactivateWorkflows(@Param("workflowIds") List<String> workflowIds, @Param("updateBy") String updateBy);

    /**
     * 批量归档工作流
     */
    int batchArchiveWorkflows(@Param("workflowIds") List<String> workflowIds, @Param("updateBy") String updateBy);

    /**
     * 更新工作流配置
     */
    int updateWorkflowConfig(@Param("workflowId") String workflowId,
                            @Param("workflowDefinition") String workflowDefinition,
                            @Param("nodeConfig") String nodeConfig,
                            @Param("routingRules") String routingRules,
                            @Param("updateBy") String updateBy);

    /**
     * 更新工作流权限配置
     */
    int updateWorkflowPermissions(@Param("workflowId") String workflowId,
                                 @Param("permissionConfig") String permissionConfig,
                                 @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计工作流总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE IS_DELETED = 0")
    int countTotalWorkflows();

    /**
     * 按工作流类型统计数量
     */
    List<Map<String, Object>> countWorkflowsByType();

    /**
     * 按工作流分类统计数量
     */
    List<Map<String, Object>> countWorkflowsByCategory();

    /**
     * 按工作流状态统计数量
     */
    List<Map<String, Object>> countWorkflowsByStatus();

    /**
     * 按优先级统计数量
     */
    List<Map<String, Object>> countWorkflowsByPriority();

    /**
     * 按适用范围统计数量
     */
    List<Map<String, Object>> countWorkflowsByApplicableScope();

    /**
     * 统计激活工作流数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_STATUS = 'ACTIVE' AND IS_DELETED = 0")
    int countActiveWorkflows();

    /**
     * 统计停用工作流数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_STATUS = 'INACTIVE' AND IS_DELETED = 0")
    int countInactiveWorkflows();

    /**
     * 统计默认工作流数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE IS_DEFAULT = 1 AND IS_DELETED = 0")
    int countDefaultWorkflows();

    /**
     * 统计自定义工作流数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CATEGORY = 'CUSTOM' AND IS_DELETED = 0")
    int countCustomWorkflows();

    /**
     * 统计紧急工作流数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CATEGORY = 'EMERGENCY' AND IS_DELETED = 0")
    int countEmergencyWorkflows();

    // ==================== 数据验证方法 ====================

    /**
     * 检查工作流编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CODE = #{workflowCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkWorkflowCodeExists(@Param("workflowCode") String workflowCode, @Param("excludeId") String excludeId);

    /**
     * 检查工作流名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_NAME = #{workflowName} AND WORKFLOW_TYPE = #{workflowType} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkWorkflowNameExists(@Param("workflowName") String workflowName, @Param("workflowType") String workflowType, @Param("excludeId") String excludeId);

    /**
     * 检查是否存在默认工作流
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_TYPE = #{workflowType} AND IS_DEFAULT = 1 AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkDefaultWorkflowExists(@Param("workflowType") String workflowType, @Param("excludeId") String excludeId);

    /**
     * 检查工作流是否可以删除
     */
    boolean checkWorkflowCanDelete(@Param("workflowId") String workflowId);

    /**
     * 检查工作流是否可以修改
     */
    boolean checkWorkflowCanModify(@Param("workflowId") String workflowId);

    /**
     * 检查工作流是否在使用中
     */
    boolean checkWorkflowInUse(@Param("workflowId") String workflowId);

    /**
     * 验证工作流配置
     */
    boolean validateWorkflowConfig(@Param("workflowDefinition") String workflowDefinition);

    /**
     * 验证工作流权限配置
     */
    boolean validateWorkflowPermissions(@Param("permissionConfig") String permissionConfig);

    /**
     * 检查工作流版本冲突
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_WORKFLOW WHERE WORKFLOW_CODE = #{workflowCode} AND WORKFLOW_VERSION = #{workflowVersion} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkWorkflowVersionConflict(@Param("workflowCode") String workflowCode, @Param("workflowVersion") String workflowVersion, @Param("excludeId") String excludeId);
}
