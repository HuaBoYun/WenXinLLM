package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算规则数据访问接口
 * 
 * @description 预算规则数据访问层，提供预算规则的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetRuleMapper extends BaseMapper<BudgetRule> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据规则编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_CODE = #{ruleCode} AND IS_DELETED = 0")
    BudgetRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 根据规则类型查询规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_TYPE = #{ruleType} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectByRuleType(@Param("ruleType") String ruleType);

    /**
     * 根据规则分类查询规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_CATEGORY = #{ruleCategory} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectByRuleCategory(@Param("ruleCategory") String ruleCategory);

    /**
     * 根据规则级别查询规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_LEVEL = #{ruleLevel} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectByRuleLevel(@Param("ruleLevel") String ruleLevel);

    /**
     * 根据规则状态查询规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_STATUS = #{ruleStatus} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectByRuleStatus(@Param("ruleStatus") String ruleStatus);

    /**
     * 根据规则优先级查询规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_PRIORITY = #{rulePriority} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectByRulePriority(@Param("rulePriority") String rulePriority);

    /**
     * 根据适用组织ID查询规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE APPLICABLE_ORGANIZATION_ID = #{organizationId} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据适用范围查询规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE APPLICABLE_SCOPE = #{applicableScope} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectByApplicableScope(@Param("applicableScope") String applicableScope);

    /**
     * 根据执行顺序查询规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE EXECUTION_ORDER = #{executionOrder} AND IS_DELETED = 0 ORDER BY RULE_CODE")
    List<BudgetRule> selectByExecutionOrder(@Param("executionOrder") Integer executionOrder);

    /**
     * 查询必须执行的规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE IS_MANDATORY = 1 AND RULE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectMandatoryRules();

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算规则
     */
    IPage<BudgetRule> selectBudgetRulePage(Page<BudgetRule> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectActiveRules();

    /**
     * 查询我创建的规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE CREATE_BY = #{userId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetRule> selectMyRules(@Param("userId") String userId);

    /**
     * 查询待我审批的规则列表
     */
    List<BudgetRule> selectPendingApprovals(@Param("userId") String userId);

    /**
     * 查询已发布的规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE PUBLISHED_BY IS NOT NULL AND RULE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY PUBLISHED_TIME DESC")
    List<BudgetRule> selectPublishedRules();

    /**
     * 查询系统级规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_LEVEL = 'SYSTEM' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectSystemRules();

    /**
     * 查询组织级规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_LEVEL = 'ORGANIZATION' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectOrganizationRules();

    /**
     * 查询用户级规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_LEVEL = 'USER' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectUserRules();

    /**
     * 查询可用的规则列表
     */
    List<BudgetRule> selectAvailableRules(@Param("ruleType") String ruleType, 
                                         @Param("ruleCategory") String ruleCategory,
                                         @Param("organizationId") String organizationId,
                                         @Param("userId") String userId);

    /**
     * 查询有效期内的规则列表
     */
    List<BudgetRule> selectEffectiveRules(@Param("effectiveDate") LocalDateTime effectiveDate);

    /**
     * 查询即将过期的规则列表
     */
    List<BudgetRule> selectExpiringRules(@Param("days") Integer days);

    /**
     * 查询已过期的规则列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE EXPIRY_DATE < NOW() AND IS_DELETED = 0 ORDER BY EXPIRY_DATE DESC")
    List<BudgetRule> selectExpiredRules();

    /**
     * 查询规则统计信息
     */
    Map<String, Object> selectRuleStatistics();

    /**
     * 根据关键字搜索规则
     */
    List<BudgetRule> searchRules(@Param("keyword") String keyword);

    /**
     * 查询高优先级规则
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE RULE_PRIORITY = 'HIGH' AND RULE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, RULE_CODE")
    List<BudgetRule> selectHighPriorityRules();

    /**
     * 查询执行频率最高的规则
     */
    @Select("SELECT * FROM NCV65_BUDGET_RULE WHERE EXECUTION_COUNT > 0 AND IS_DELETED = 0 ORDER BY EXECUTION_COUNT DESC LIMIT #{limit}")
    List<BudgetRule> selectMostExecutedRules(@Param("limit") Integer limit);

    // ==================== 业务操作方法 ====================

    /**
     * 激活规则
     */
    int activateRule(@Param("ruleId") String ruleId, @Param("updateBy") String updateBy);

    /**
     * 停用规则
     */
    int deactivateRule(@Param("ruleId") String ruleId, @Param("updateBy") String updateBy);

    /**
     * 归档规则
     */
    int archiveRule(@Param("ruleId") String ruleId, @Param("updateBy") String updateBy);

    /**
     * 审批通过
     */
    int approveRule(@Param("ruleId") String ruleId, @Param("approvedBy") String approvedBy);

    /**
     * 审批拒绝
     */
    int rejectRule(@Param("ruleId") String ruleId, @Param("approvedBy") String approvedBy);

    /**
     * 发布规则
     */
    int publishRule(@Param("ruleId") String ruleId, @Param("publishedBy") String publishedBy);

    /**
     * 更新执行统计
     */
    int updateExecutionStatistics(@Param("ruleId") String ruleId, 
                                 @Param("executionTime") Long executionTime,
                                 @Param("isSuccess") Boolean isSuccess);

    /**
     * 重置执行统计
     */
    int resetExecutionStatistics(@Param("ruleId") String ruleId, @Param("updateBy") String updateBy);

    /**
     * 批量更新规则状态
     */
    int batchUpdateRuleStatus(@Param("ruleIds") List<String> ruleIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量激活规则
     */
    int batchActivateRules(@Param("ruleIds") List<String> ruleIds, @Param("updateBy") String updateBy);

    /**
     * 批量停用规则
     */
    int batchDeactivateRules(@Param("ruleIds") List<String> ruleIds, @Param("updateBy") String updateBy);

    /**
     * 批量审批规则
     */
    int batchApproveRules(@Param("ruleIds") List<String> ruleIds, @Param("approvedBy") String approvedBy);

    /**
     * 批量发布规则
     */
    int batchPublishRules(@Param("ruleIds") List<String> ruleIds, @Param("publishedBy") String publishedBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计规则总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE IS_DELETED = 0")
    int countTotalRules();

    /**
     * 按规则类型统计数量
     */
    List<Map<String, Object>> countRulesByType();

    /**
     * 按规则分类统计数量
     */
    List<Map<String, Object>> countRulesByCategory();

    /**
     * 按规则级别统计数量
     */
    List<Map<String, Object>> countRulesByLevel();

    /**
     * 按规则状态统计数量
     */
    List<Map<String, Object>> countRulesByStatus();

    /**
     * 按规则优先级统计数量
     */
    List<Map<String, Object>> countRulesByPriority();

    /**
     * 按适用范围统计数量
     */
    List<Map<String, Object>> countRulesByApplicableScope();

    /**
     * 统计执行次数汇总
     */
    Map<String, Object> sumRuleExecutionCount();

    /**
     * 统计成功率汇总
     */
    Map<String, Object> sumRuleSuccessRate();

    /**
     * 统计平均执行时间
     */
    Map<String, Object> avgRuleExecutionTime();

    /**
     * 统计必须执行规则数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE IS_MANDATORY = 1 AND IS_DELETED = 0")
    int countMandatoryRules();

    /**
     * 统计可跳过规则数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE IS_SKIPPABLE = 1 AND IS_DELETED = 0")
    int countSkippableRules();

    /**
     * 统计系统级规则数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE RULE_LEVEL = 'SYSTEM' AND IS_DELETED = 0")
    int countSystemRules();

    /**
     * 统计组织级规则数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE RULE_LEVEL = 'ORGANIZATION' AND IS_DELETED = 0")
    int countOrganizationRules();

    /**
     * 统计用户级规则数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE RULE_LEVEL = 'USER' AND IS_DELETED = 0")
    int countUserRules();

    // ==================== 数据验证方法 ====================

    /**
     * 检查规则编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE RULE_CODE = #{ruleCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkRuleCodeExists(@Param("ruleCode") String ruleCode, @Param("excludeId") String excludeId);

    /**
     * 检查规则名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE RULE_NAME = #{ruleName} AND RULE_TYPE = #{ruleType} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkRuleNameExists(@Param("ruleName") String ruleName, @Param("ruleType") String ruleType, @Param("excludeId") String excludeId);

    /**
     * 检查规则是否可以删除
     */
    boolean checkRuleCanDelete(@Param("ruleId") String ruleId);

    /**
     * 检查规则是否可以修改
     */
    boolean checkRuleCanModify(@Param("ruleId") String ruleId);

    /**
     * 检查规则是否在使用中
     */
    boolean checkRuleInUse(@Param("ruleId") String ruleId);

    /**
     * 验证规则表达式
     */
    boolean validateRuleExpression(@Param("ruleExpression") String ruleExpression);

    /**
     * 验证规则脚本
     */
    boolean validateRuleScript(@Param("ruleScript") String ruleScript);

    /**
     * 验证规则参数
     */
    boolean validateRuleParameters(@Param("ruleParameters") String ruleParameters);

    /**
     * 检查执行顺序冲突
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_RULE WHERE EXECUTION_ORDER = #{executionOrder} AND RULE_TYPE = #{ruleType} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkExecutionOrderConflict(@Param("executionOrder") Integer executionOrder, @Param("ruleType") String ruleType, @Param("excludeId") String excludeId);

    /**
     * 检查规则权限
     */
    boolean checkRulePermission(@Param("ruleId") String ruleId, @Param("userId") String userId, @Param("operation") String operation);

    /**
     * 验证适用条件
     */
    boolean validateApplicableConditions(@Param("applicableConditions") String applicableConditions);
}
