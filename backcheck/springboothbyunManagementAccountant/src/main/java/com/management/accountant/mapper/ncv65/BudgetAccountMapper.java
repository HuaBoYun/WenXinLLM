package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算科目数据访问接口
 * 
 * @description 预算科目数据访问层，提供预算科目的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetAccountMapper extends BaseMapper<BudgetAccount> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据科目编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_CODE = #{accountCode} AND IS_DELETED = 0")
    BudgetAccount selectByAccountCode(@Param("accountCode") String accountCode);

    /**
     * 根据科目类型查询科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_TYPE = #{accountType} AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectByAccountType(@Param("accountType") String accountType);

    /**
     * 根据科目分类查询科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_CATEGORY = #{accountCategory} AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectByAccountCategory(@Param("accountCategory") String accountCategory);

    /**
     * 根据科目性质查询科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_NATURE = #{accountNature} AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectByAccountNature(@Param("accountNature") String accountNature);

    /**
     * 根据科目级别查询科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_LEVEL = #{accountLevel} AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectByAccountLevel(@Param("accountLevel") Integer accountLevel);

    /**
     * 根据父科目ID查询子科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE PARENT_ACCOUNT_ID = #{parentAccountId} AND IS_DELETED = 0 ORDER BY SORT_ORDER, ACCOUNT_CODE")
    List<BudgetAccount> selectByParentAccountId(@Param("parentAccountId") String parentAccountId);

    /**
     * 根据科目状态查询科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_STATUS = #{accountStatus} AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectByAccountStatus(@Param("accountStatus") String accountStatus);

    /**
     * 根据预算控制类型查询科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE BUDGET_CONTROL_TYPE = #{budgetControlType} AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectByBudgetControlType(@Param("budgetControlType") String budgetControlType);

    /**
     * 查询叶子节点科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE IS_LEAF = 1 AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectLeafAccounts();

    /**
     * 查询可录入数据的科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ALLOW_DATA_ENTRY = 1 AND ACCOUNT_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectDataEntryAccounts();

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算科目
     */
    IPage<BudgetAccount> selectBudgetAccountPage(Page<BudgetAccount> page, @Param("params") Map<String, Object> params);

    /**
     * 查询科目树形结构
     */
    List<BudgetAccount> selectAccountTree(@Param("rootAccountId") String rootAccountId);

    /**
     * 查询激活的科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectActiveAccounts();

    /**
     * 查询系统科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE IS_SYSTEM = 1 AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectSystemAccounts();

    /**
     * 查询必填科目列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE IS_REQUIRED = 1 AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectRequiredAccounts();

    /**
     * 查询科目层次结构
     */
    List<BudgetAccount> selectAccountHierarchy(@Param("rootAccountId") String rootAccountId);

    /**
     * 查询科目及其所有子科目
     */
    List<BudgetAccount> selectAccountWithChildren(@Param("accountId") String accountId);

    /**
     * 查询科目及其所有父科目
     */
    List<BudgetAccount> selectAccountWithParents(@Param("accountId") String accountId);

    /**
     * 根据科目路径查询科目
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_PATH LIKE CONCAT(#{accountPath}, '%') AND IS_DELETED = 0 ORDER BY ACCOUNT_PATH")
    List<BudgetAccount> selectByAccountPath(@Param("accountPath") String accountPath);

    /**
     * 查询科目统计信息
     */
    Map<String, Object> selectAccountStatistics();

    /**
     * 根据关键字搜索科目
     */
    List<BudgetAccount> searchAccounts(@Param("keyword") String keyword);

    /**
     * 查询有预算控制的科目
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE BUDGET_CONTROL_TYPE != 'NONE' AND ACCOUNT_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectBudgetControlAccounts();

    /**
     * 查询有预警设置的科目
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE BUDGET_CONTROL_TYPE = 'WARNING' AND WARNING_THRESHOLD IS NOT NULL AND ACCOUNT_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectWarningAccounts();

    /**
     * 查询严格控制的科目
     */
    @Select("SELECT * FROM NCV65_BUDGET_ACCOUNT WHERE BUDGET_CONTROL_TYPE = 'STRICT' AND CONTROL_THRESHOLD IS NOT NULL AND ACCOUNT_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY ACCOUNT_CODE")
    List<BudgetAccount> selectStrictControlAccounts();

    // ==================== 业务操作方法 ====================

    /**
     * 激活科目
     */
    int activateAccount(@Param("accountId") String accountId, @Param("updateBy") String updateBy);

    /**
     * 停用科目
     */
    int deactivateAccount(@Param("accountId") String accountId, @Param("updateBy") String updateBy);

    /**
     * 锁定科目
     */
    int lockAccount(@Param("accountId") String accountId, @Param("updateBy") String updateBy);

    /**
     * 解锁科目
     */
    int unlockAccount(@Param("accountId") String accountId, @Param("updateBy") String updateBy);

    /**
     * 更新科目层次结构
     */
    int updateAccountHierarchy(@Param("accountId") String accountId, 
                              @Param("parentAccountId") String parentAccountId,
                              @Param("accountLevel") Integer accountLevel,
                              @Param("accountPath") String accountPath,
                              @Param("updateBy") String updateBy);

    /**
     * 更新叶子节点标记
     */
    int updateLeafFlag(@Param("accountId") String accountId, @Param("isLeaf") Boolean isLeaf, @Param("updateBy") String updateBy);

    /**
     * 批量更新科目状态
     */
    int batchUpdateAccountStatus(@Param("accountIds") List<String> accountIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量激活科目
     */
    int batchActivateAccounts(@Param("accountIds") List<String> accountIds, @Param("updateBy") String updateBy);

    /**
     * 批量停用科目
     */
    int batchDeactivateAccounts(@Param("accountIds") List<String> accountIds, @Param("updateBy") String updateBy);

    /**
     * 更新科目预算控制设置
     */
    int updateBudgetControlSettings(@Param("accountId") String accountId,
                                   @Param("budgetControlType") String budgetControlType,
                                   @Param("warningThreshold") Double warningThreshold,
                                   @Param("controlThreshold") Double controlThreshold,
                                   @Param("updateBy") String updateBy);

    /**
     * 批量更新预算控制类型
     */
    int batchUpdateBudgetControlType(@Param("accountIds") List<String> accountIds, @Param("budgetControlType") String budgetControlType, @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计科目总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE IS_DELETED = 0")
    int countTotalAccounts();

    /**
     * 按科目类型统计数量
     */
    List<Map<String, Object>> countAccountsByType();

    /**
     * 按科目分类统计数量
     */
    List<Map<String, Object>> countAccountsByCategory();

    /**
     * 按科目性质统计数量
     */
    List<Map<String, Object>> countAccountsByNature();

    /**
     * 按科目状态统计数量
     */
    List<Map<String, Object>> countAccountsByStatus();

    /**
     * 按科目级别统计数量
     */
    List<Map<String, Object>> countAccountsByLevel();

    /**
     * 按预算控制类型统计数量
     */
    List<Map<String, Object>> countAccountsByBudgetControlType();

    /**
     * 统计激活科目数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_STATUS = 'ACTIVE' AND IS_DELETED = 0")
    int countActiveAccounts();

    /**
     * 统计停用科目数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_STATUS = 'INACTIVE' AND IS_DELETED = 0")
    int countInactiveAccounts();

    /**
     * 统计叶子节点科目数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE IS_LEAF = 1 AND IS_DELETED = 0")
    int countLeafAccounts();

    /**
     * 统计系统科目数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE IS_SYSTEM = 1 AND IS_DELETED = 0")
    int countSystemAccounts();

    /**
     * 统计必填科目数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE IS_REQUIRED = 1 AND IS_DELETED = 0")
    int countRequiredAccounts();

    // ==================== 数据验证方法 ====================

    /**
     * 检查科目编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_CODE = #{accountCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkAccountCodeExists(@Param("accountCode") String accountCode, @Param("excludeId") String excludeId);

    /**
     * 检查科目名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE ACCOUNT_NAME = #{accountName} AND PARENT_ACCOUNT_ID = #{parentAccountId} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkAccountNameExists(@Param("accountName") String accountName, @Param("parentAccountId") String parentAccountId, @Param("excludeId") String excludeId);

    /**
     * 检查科目是否有子科目
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ACCOUNT WHERE PARENT_ACCOUNT_ID = #{accountId} AND IS_DELETED = 0")
    int checkAccountHasChildren(@Param("accountId") String accountId);

    /**
     * 检查科目是否可以删除
     */
    boolean checkAccountCanDelete(@Param("accountId") String accountId);

    /**
     * 检查科目是否可以修改
     */
    boolean checkAccountCanModify(@Param("accountId") String accountId);

    /**
     * 验证科目层次结构
     */
    boolean validateAccountHierarchy(@Param("accountId") String accountId, @Param("parentAccountId") String parentAccountId);

    /**
     * 检查科目是否在使用中
     */
    boolean checkAccountInUse(@Param("accountId") String accountId);

    /**
     * 验证科目编码格式
     */
    boolean validateAccountCodeFormat(@Param("accountCode") String accountCode, @Param("accountLevel") Integer accountLevel);
}
