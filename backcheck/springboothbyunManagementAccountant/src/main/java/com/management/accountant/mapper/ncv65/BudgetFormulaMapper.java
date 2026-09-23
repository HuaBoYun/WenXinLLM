package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetFormula;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算公式数据访问接口
 * 
 * @description 预算公式数据访问层，提供预算公式的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetFormulaMapper extends BaseMapper<BudgetFormula> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据公式编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_CODE = #{formulaCode} AND IS_DELETED = 0")
    BudgetFormula selectByFormulaCode(@Param("formulaCode") String formulaCode);

    /**
     * 根据公式类型查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_TYPE = #{formulaType} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByFormulaType(@Param("formulaType") String formulaType);

    /**
     * 根据公式分类查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_CATEGORY = #{formulaCategory} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByFormulaCategory(@Param("formulaCategory") String formulaCategory);

    /**
     * 根据公式级别查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_LEVEL = #{formulaLevel} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByFormulaLevel(@Param("formulaLevel") String formulaLevel);

    /**
     * 根据公式状态查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_STATUS = #{formulaStatus} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByFormulaStatus(@Param("formulaStatus") String formulaStatus);

    /**
     * 根据公式版本查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_VERSION = #{formulaVersion} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByFormulaVersion(@Param("formulaVersion") String formulaVersion);

    /**
     * 根据适用组织ID查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE APPLICABLE_ORGANIZATION_ID = #{organizationId} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据适用科目ID查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE APPLICABLE_ACCOUNT_ID = #{accountId} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByAccountId(@Param("accountId") String accountId);

    /**
     * 根据适用指标ID查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE APPLICABLE_INDICATOR_ID = #{indicatorId} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByIndicatorId(@Param("indicatorId") String indicatorId);

    /**
     * 根据公式语言查询公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_LANGUAGE = #{formulaLanguage} AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectByFormulaLanguage(@Param("formulaLanguage") String formulaLanguage);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算公式
     */
    IPage<BudgetFormula> selectBudgetFormulaPage(Page<BudgetFormula> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectActiveFormulas();

    /**
     * 查询我创建的公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE CREATE_BY = #{userId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetFormula> selectMyFormulas(@Param("userId") String userId);

    /**
     * 查询待我审批的公式列表
     */
    List<BudgetFormula> selectPendingApprovals(@Param("userId") String userId);

    /**
     * 查询已发布的公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE PUBLISHED_BY IS NOT NULL AND FORMULA_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY PUBLISHED_TIME DESC")
    List<BudgetFormula> selectPublishedFormulas();

    /**
     * 查询基础公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_LEVEL = 'BASIC' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectBasicFormulas();

    /**
     * 查询高级公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_LEVEL = 'ADVANCED' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectAdvancedFormulas();

    /**
     * 查询自定义公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_LEVEL = 'CUSTOM' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectCustomFormulas();

    /**
     * 查询可用的公式列表
     */
    List<BudgetFormula> selectAvailableFormulas(@Param("formulaType") String formulaType, 
                                               @Param("formulaCategory") String formulaCategory,
                                               @Param("organizationId") String organizationId,
                                               @Param("accountId") String accountId);

    /**
     * 查询自动执行的公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE IS_AUTO_EXECUTE = 1 AND FORMULA_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY EXECUTION_ORDER, FORMULA_CODE")
    List<BudgetFormula> selectAutoExecuteFormulas();

    /**
     * 查询定期执行的公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE EXECUTION_FREQUENCY != 'REAL_TIME' AND FORMULA_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY NEXT_EXECUTION_TIME")
    List<BudgetFormula> selectPeriodicFormulas();

    /**
     * 查询需要执行的公式列表
     */
    List<BudgetFormula> selectFormulasToExecute();

    /**
     * 查询公式版本列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_CODE = #{formulaCode} AND IS_DELETED = 0 ORDER BY FORMULA_VERSION DESC")
    List<BudgetFormula> selectFormulaVersions(@Param("formulaCode") String formulaCode);

    /**
     * 查询最新版本公式
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_CODE = #{formulaCode} AND IS_DELETED = 0 ORDER BY FORMULA_VERSION DESC LIMIT 1")
    BudgetFormula selectLatestFormulaVersion(@Param("formulaCode") String formulaCode);

    /**
     * 查询有效期内的公式列表
     */
    List<BudgetFormula> selectEffectiveFormulas(@Param("effectiveDate") LocalDateTime effectiveDate);

    /**
     * 查询即将过期的公式列表
     */
    List<BudgetFormula> selectExpiringFormulas(@Param("days") Integer days);

    /**
     * 查询已过期的公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE EXPIRY_DATE < NOW() AND IS_DELETED = 0 ORDER BY EXPIRY_DATE DESC")
    List<BudgetFormula> selectExpiredFormulas();

    /**
     * 查询公式统计信息
     */
    Map<String, Object> selectFormulaStatistics();

    /**
     * 根据关键字搜索公式
     */
    List<BudgetFormula> searchFormulas(@Param("keyword") String keyword);

    /**
     * 查询执行频率最高的公式
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE EXECUTION_COUNT > 0 AND IS_DELETED = 0 ORDER BY EXECUTION_COUNT DESC LIMIT #{limit}")
    List<BudgetFormula> selectMostExecutedFormulas(@Param("limit") Integer limit);

    /**
     * 查询测试中的公式列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_FORMULA WHERE FORMULA_STATUS = 'TESTING' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetFormula> selectTestingFormulas();

    // ==================== 业务操作方法 ====================

    /**
     * 激活公式
     */
    int activateFormula(@Param("formulaId") String formulaId, @Param("updateBy") String updateBy);

    /**
     * 停用公式
     */
    int deactivateFormula(@Param("formulaId") String formulaId, @Param("updateBy") String updateBy);

    /**
     * 设置为测试状态
     */
    int setTestingStatus(@Param("formulaId") String formulaId, @Param("updateBy") String updateBy);

    /**
     * 审批通过
     */
    int approveFormula(@Param("formulaId") String formulaId, @Param("approvedBy") String approvedBy);

    /**
     * 审批拒绝
     */
    int rejectFormula(@Param("formulaId") String formulaId, @Param("approvedBy") String approvedBy);

    /**
     * 发布公式
     */
    int publishFormula(@Param("formulaId") String formulaId, @Param("publishedBy") String publishedBy);

    /**
     * 更新执行统计
     */
    int updateExecutionStatistics(@Param("formulaId") String formulaId, 
                                 @Param("executionTime") Long executionTime,
                                 @Param("isSuccess") Boolean isSuccess);

    /**
     * 重置执行统计
     */
    int resetExecutionStatistics(@Param("formulaId") String formulaId, @Param("updateBy") String updateBy);

    /**
     * 复制公式
     */
    int copyFormula(@Param("sourceFormulaId") String sourceFormulaId, @Param("newFormulaCode") String newFormulaCode, @Param("newFormulaName") String newFormulaName, @Param("createBy") String createBy);

    /**
     * 批量更新公式状态
     */
    int batchUpdateFormulaStatus(@Param("formulaIds") List<String> formulaIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量激活公式
     */
    int batchActivateFormulas(@Param("formulaIds") List<String> formulaIds, @Param("updateBy") String updateBy);

    /**
     * 批量停用公式
     */
    int batchDeactivateFormulas(@Param("formulaIds") List<String> formulaIds, @Param("updateBy") String updateBy);

    /**
     * 批量审批公式
     */
    int batchApproveFormulas(@Param("formulaIds") List<String> formulaIds, @Param("approvedBy") String approvedBy);

    /**
     * 批量发布公式
     */
    int batchPublishFormulas(@Param("formulaIds") List<String> formulaIds, @Param("publishedBy") String publishedBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计公式总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE IS_DELETED = 0")
    int countTotalFormulas();

    /**
     * 按公式类型统计数量
     */
    List<Map<String, Object>> countFormulasByType();

    /**
     * 按公式分类统计数量
     */
    List<Map<String, Object>> countFormulasByCategory();

    /**
     * 按公式级别统计数量
     */
    List<Map<String, Object>> countFormulasByLevel();

    /**
     * 按公式状态统计数量
     */
    List<Map<String, Object>> countFormulasByStatus();

    /**
     * 按公式语言统计数量
     */
    List<Map<String, Object>> countFormulasByLanguage();

    /**
     * 按执行频率统计数量
     */
    List<Map<String, Object>> countFormulasByExecutionFrequency();

    /**
     * 统计执行次数汇总
     */
    Map<String, Object> sumFormulaExecutionCount();

    /**
     * 统计成功率汇总
     */
    Map<String, Object> sumFormulaSuccessRate();

    /**
     * 统计平均执行时间
     */
    Map<String, Object> avgFormulaExecutionTime();

    /**
     * 统计自动执行公式数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE IS_AUTO_EXECUTE = 1 AND IS_DELETED = 0")
    int countAutoExecuteFormulas();

    /**
     * 统计基础公式数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE FORMULA_LEVEL = 'BASIC' AND IS_DELETED = 0")
    int countBasicFormulas();

    /**
     * 统计高级公式数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE FORMULA_LEVEL = 'ADVANCED' AND IS_DELETED = 0")
    int countAdvancedFormulas();

    /**
     * 统计自定义公式数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE FORMULA_LEVEL = 'CUSTOM' AND IS_DELETED = 0")
    int countCustomFormulas();

    // ==================== 数据验证方法 ====================

    /**
     * 检查公式编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE FORMULA_CODE = #{formulaCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkFormulaCodeExists(@Param("formulaCode") String formulaCode, @Param("excludeId") String excludeId);

    /**
     * 检查公式名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE FORMULA_NAME = #{formulaName} AND FORMULA_TYPE = #{formulaType} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkFormulaNameExists(@Param("formulaName") String formulaName, @Param("formulaType") String formulaType, @Param("excludeId") String excludeId);

    /**
     * 检查公式是否可以删除
     */
    boolean checkFormulaCanDelete(@Param("formulaId") String formulaId);

    /**
     * 检查公式是否可以修改
     */
    boolean checkFormulaCanModify(@Param("formulaId") String formulaId);

    /**
     * 检查公式是否在使用中
     */
    boolean checkFormulaInUse(@Param("formulaId") String formulaId);

    /**
     * 验证公式表达式
     */
    boolean validateFormulaExpression(@Param("formulaExpression") String formulaExpression);

    /**
     * 验证公式脚本
     */
    boolean validateFormulaScript(@Param("formulaScript") String formulaScript);

    /**
     * 验证输入参数
     */
    boolean validateInputParameters(@Param("inputParameters") String inputParameters);

    /**
     * 验证输出参数
     */
    boolean validateOutputParameters(@Param("outputParameters") String outputParameters);

    /**
     * 检查执行顺序冲突
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE EXECUTION_ORDER = #{executionOrder} AND FORMULA_TYPE = #{formulaType} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkExecutionOrderConflict(@Param("executionOrder") Integer executionOrder, @Param("formulaType") String formulaType, @Param("excludeId") String excludeId);

    /**
     * 检查公式权限
     */
    boolean checkFormulaPermission(@Param("formulaId") String formulaId, @Param("userId") String userId, @Param("operation") String operation);

    /**
     * 验证依赖公式
     */
    boolean validateDependentFormulas(@Param("dependentFormulas") String dependentFormulas);

    /**
     * 检查公式版本冲突
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_FORMULA WHERE FORMULA_CODE = #{formulaCode} AND FORMULA_VERSION = #{formulaVersion} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkFormulaVersionConflict(@Param("formulaCode") String formulaCode, @Param("formulaVersion") String formulaVersion, @Param("excludeId") String excludeId);
}
