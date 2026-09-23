package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算参数数据访问接口
 * 
 * @description 预算参数数据访问层，提供预算参数的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetParameterMapper extends BaseMapper<BudgetParameter> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据参数编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_CODE = #{parameterCode} AND IS_DELETED = 0")
    BudgetParameter selectByParameterCode(@Param("parameterCode") String parameterCode);

    /**
     * 根据参数类型查询参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_TYPE = #{parameterType} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectByParameterType(@Param("parameterType") String parameterType);

    /**
     * 根据参数分类查询参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_CATEGORY = #{parameterCategory} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectByParameterCategory(@Param("parameterCategory") String parameterCategory);

    /**
     * 根据数据类型查询参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE DATA_TYPE = #{dataType} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectByDataType(@Param("dataType") String dataType);

    /**
     * 根据参数状态查询参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_STATUS = #{parameterStatus} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectByParameterStatus(@Param("parameterStatus") String parameterStatus);

    /**
     * 根据参数级别查询参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_LEVEL = #{parameterLevel} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectByParameterLevel(@Param("parameterLevel") String parameterLevel);

    /**
     * 根据适用组织ID查询参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE APPLICABLE_ORGANIZATION_ID = #{organizationId} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据适用用户ID查询参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE APPLICABLE_USER_ID = #{userId} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectByUserId(@Param("userId") String userId);

    /**
     * 根据参数组查询参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_GROUP = #{parameterGroup} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectByParameterGroup(@Param("parameterGroup") String parameterGroup);

    /**
     * 查询必填参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE IS_REQUIRED = 1 AND PARAMETER_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectRequiredParameters();

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算参数
     */
    IPage<BudgetParameter> selectBudgetParameterPage(Page<BudgetParameter> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectActiveParameters();

    /**
     * 查询我创建的参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE CREATE_BY = #{userId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetParameter> selectMyParameters(@Param("userId") String userId);

    /**
     * 查询待我审批的参数列表
     */
    List<BudgetParameter> selectPendingApprovals(@Param("userId") String userId);

    /**
     * 查询系统级参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_LEVEL = 'SYSTEM' AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectSystemParameters();

    /**
     * 查询组织级参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_LEVEL = 'ORGANIZATION' AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectOrganizationParameters();

    /**
     * 查询用户级参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_LEVEL = 'USER' AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectUserParameters();

    /**
     * 查询可修改的参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE IS_MODIFIABLE = 1 AND PARAMETER_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectModifiableParameters();

    /**
     * 查询敏感参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE IS_SENSITIVE = 1 AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectSensitiveParameters();

    /**
     * 查询加密参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE IS_ENCRYPTED = 1 AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectEncryptedParameters();

    /**
     * 查询可继承的参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE IS_INHERITABLE = 1 AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectInheritableParameters();

    /**
     * 查询子参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE PARENT_PARAMETER_ID = #{parentParameterId} AND IS_DELETED = 0 ORDER BY SORT_ORDER, PARAMETER_CODE")
    List<BudgetParameter> selectChildParameters(@Param("parentParameterId") String parentParameterId);

    /**
     * 查询可用的参数列表
     */
    List<BudgetParameter> selectAvailableParameters(@Param("parameterType") String parameterType, 
                                                   @Param("parameterCategory") String parameterCategory,
                                                   @Param("organizationId") String organizationId,
                                                   @Param("userId") String userId);

    /**
     * 查询有效期内的参数列表
     */
    List<BudgetParameter> selectEffectiveParameters(@Param("effectiveDate") LocalDateTime effectiveDate);

    /**
     * 查询即将过期的参数列表
     */
    List<BudgetParameter> selectExpiringParameters(@Param("days") Integer days);

    /**
     * 查询已过期的参数列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE EXPIRY_DATE < NOW() AND IS_DELETED = 0 ORDER BY EXPIRY_DATE DESC")
    List<BudgetParameter> selectExpiredParameters();

    /**
     * 查询参数统计信息
     */
    Map<String, Object> selectParameterStatistics();

    /**
     * 根据关键字搜索参数
     */
    List<BudgetParameter> searchParameters(@Param("keyword") String keyword);

    /**
     * 查询访问频率最高的参数
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE ACCESS_COUNT > 0 AND IS_DELETED = 0 ORDER BY ACCESS_COUNT DESC LIMIT #{limit}")
    List<BudgetParameter> selectMostAccessedParameters(@Param("limit") Integer limit);

    /**
     * 查询最近修改的参数
     */
    @Select("SELECT * FROM NCV65_BUDGET_PARAMETER WHERE LAST_MODIFIED_TIME IS NOT NULL AND IS_DELETED = 0 ORDER BY LAST_MODIFIED_TIME DESC LIMIT #{limit}")
    List<BudgetParameter> selectRecentlyModifiedParameters(@Param("limit") Integer limit);

    // ==================== 业务操作方法 ====================

    /**
     * 激活参数
     */
    int activateParameter(@Param("parameterId") String parameterId, @Param("updateBy") String updateBy);

    /**
     * 停用参数
     */
    int deactivateParameter(@Param("parameterId") String parameterId, @Param("updateBy") String updateBy);

    /**
     * 审批通过
     */
    int approveParameter(@Param("parameterId") String parameterId, @Param("approvedBy") String approvedBy);

    /**
     * 审批拒绝
     */
    int rejectParameter(@Param("parameterId") String parameterId, @Param("approvedBy") String approvedBy);

    /**
     * 更新参数值
     */
    int updateParameterValue(@Param("parameterId") String parameterId, 
                            @Param("parameterValue") String parameterValue,
                            @Param("updateBy") String updateBy);

    /**
     * 增加访问次数
     */
    int incrementAccessCount(@Param("parameterId") String parameterId);

    /**
     * 更新最后访问时间
     */
    int updateLastAccessTime(@Param("parameterId") String parameterId);

    /**
     * 复制参数
     */
    int copyParameter(@Param("sourceParameterId") String sourceParameterId, @Param("newParameterCode") String newParameterCode, @Param("newParameterName") String newParameterName, @Param("createBy") String createBy);

    /**
     * 批量更新参数状态
     */
    int batchUpdateParameterStatus(@Param("parameterIds") List<String> parameterIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量激活参数
     */
    int batchActivateParameters(@Param("parameterIds") List<String> parameterIds, @Param("updateBy") String updateBy);

    /**
     * 批量停用参数
     */
    int batchDeactivateParameters(@Param("parameterIds") List<String> parameterIds, @Param("updateBy") String updateBy);

    /**
     * 批量审批参数
     */
    int batchApproveParameters(@Param("parameterIds") List<String> parameterIds, @Param("approvedBy") String approvedBy);

    /**
     * 批量更新参数值
     */
    int batchUpdateParameterValues(@Param("parameterUpdates") List<Map<String, Object>> parameterUpdates, @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计参数总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE IS_DELETED = 0")
    int countTotalParameters();

    /**
     * 按参数类型统计数量
     */
    List<Map<String, Object>> countParametersByType();

    /**
     * 按参数分类统计数量
     */
    List<Map<String, Object>> countParametersByCategory();

    /**
     * 按数据类型统计数量
     */
    List<Map<String, Object>> countParametersByDataType();

    /**
     * 按参数状态统计数量
     */
    List<Map<String, Object>> countParametersByStatus();

    /**
     * 按参数级别统计数量
     */
    List<Map<String, Object>> countParametersByLevel();

    /**
     * 按参数组统计数量
     */
    List<Map<String, Object>> countParametersByGroup();

    /**
     * 统计访问次数汇总
     */
    Map<String, Object> sumParameterAccessCount();

    /**
     * 统计必填参数数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE IS_REQUIRED = 1 AND IS_DELETED = 0")
    int countRequiredParameters();

    /**
     * 统计可修改参数数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE IS_MODIFIABLE = 1 AND IS_DELETED = 0")
    int countModifiableParameters();

    /**
     * 统计敏感参数数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE IS_SENSITIVE = 1 AND IS_DELETED = 0")
    int countSensitiveParameters();

    /**
     * 统计加密参数数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE IS_ENCRYPTED = 1 AND IS_DELETED = 0")
    int countEncryptedParameters();

    /**
     * 统计可继承参数数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE IS_INHERITABLE = 1 AND IS_DELETED = 0")
    int countInheritableParameters();

    /**
     * 统计系统级参数数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_LEVEL = 'SYSTEM' AND IS_DELETED = 0")
    int countSystemParameters();

    /**
     * 统计组织级参数数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_LEVEL = 'ORGANIZATION' AND IS_DELETED = 0")
    int countOrganizationParameters();

    /**
     * 统计用户级参数数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_LEVEL = 'USER' AND IS_DELETED = 0")
    int countUserParameters();

    // ==================== 数据验证方法 ====================

    /**
     * 检查参数编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_CODE = #{parameterCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkParameterCodeExists(@Param("parameterCode") String parameterCode, @Param("excludeId") String excludeId);

    /**
     * 检查参数名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PARAMETER WHERE PARAMETER_NAME = #{parameterName} AND PARAMETER_TYPE = #{parameterType} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkParameterNameExists(@Param("parameterName") String parameterName, @Param("parameterType") String parameterType, @Param("excludeId") String excludeId);

    /**
     * 检查参数是否可以删除
     */
    boolean checkParameterCanDelete(@Param("parameterId") String parameterId);

    /**
     * 检查参数是否可以修改
     */
    boolean checkParameterCanModify(@Param("parameterId") String parameterId);

    /**
     * 检查参数是否在使用中
     */
    boolean checkParameterInUse(@Param("parameterId") String parameterId);

    /**
     * 验证参数值
     */
    boolean validateParameterValue(@Param("parameterValue") String parameterValue, @Param("dataType") String dataType, @Param("validationRules") String validationRules);

    /**
     * 验证验证规则
     */
    boolean validateValidationRules(@Param("validationRules") String validationRules);

    /**
     * 验证格式规则
     */
    boolean validateFormatRules(@Param("formatRules") String formatRules);

    /**
     * 检查参数权限
     */
    boolean checkParameterPermission(@Param("parameterId") String parameterId, @Param("userId") String userId, @Param("operation") String operation);

    /**
     * 验证适用条件
     */
    boolean validateApplicableConditions(@Param("applicableConditions") String applicableConditions);

    /**
     * 检查父参数关系
     */
    boolean checkParentParameterRelation(@Param("parameterId") String parameterId, @Param("parentParameterId") String parentParameterId);

    /**
     * 验证参数标签
     */
    boolean validateParameterTags(@Param("parameterTags") String parameterTags);
}
