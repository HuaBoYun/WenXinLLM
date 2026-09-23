package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetScenario;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算场景Mapper接口
 * 
 * @description 预算场景数据访问层，支持多场景预算分析和对比
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetScenarioMapper extends BaseMapper<BudgetScenario> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据场景编码查询预算场景
     * @param scenarioCode 场景编码
     * @param tenantId 租户ID
     * @return 预算场景信息
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE SCENARIO_CODE = #{scenarioCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetScenario selectByScenarioCode(@Param("scenarioCode") String scenarioCode, @Param("tenantId") String tenantId);

    /**
     * 根据场景名称查询预算场景
     * @param scenarioName 场景名称
     * @param tenantId 租户ID
     * @return 预算场景信息
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE SCENARIO_NAME = #{scenarioName} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetScenario selectByScenarioName(@Param("scenarioName") String scenarioName, @Param("tenantId") String tenantId);

    /**
     * 根据场景类型查询预算场景列表
     * @param scenarioType 场景类型
     * @param tenantId 租户ID
     * @return 预算场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE SCENARIO_TYPE = #{scenarioType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectByScenarioType(@Param("scenarioType") String scenarioType, @Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询预算场景列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 预算场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 根据预算模型ID查询预算场景列表
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 预算场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE MODEL_ID = #{modelId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectByModelId(@Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 根据组织体系ID查询预算场景列表
     * @param structureId 组织体系ID
     * @param tenantId 租户ID
     * @return 预算场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE STRUCTURE_ID = #{structureId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectByStructureId(@Param("structureId") String structureId, @Param("tenantId") String tenantId);

    /**
     * 根据审批状态查询预算场景列表
     * @param approvalStatus 审批状态
     * @param tenantId 租户ID
     * @return 预算场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE APPROVAL_STATUS = #{approvalStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus, @Param("tenantId") String tenantId);

    /**
     * 根据计算状态查询预算场景列表
     * @param calculationStatus 计算状态
     * @param tenantId 租户ID
     * @return 预算场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE CALCULATION_STATUS = #{calculationStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectByCalculationStatus(@Param("calculationStatus") String calculationStatus, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算场景
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetScenario> selectBudgetScenarioPage(Page<BudgetScenario> page, @Param("params") Map<String, Object> params);

    /**
     * 查询默认场景
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 默认场景
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE FISCAL_YEAR = #{fiscalYear} AND MODEL_ID = #{modelId} AND IS_DEFAULT = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetScenario selectDefaultScenario(@Param("fiscalYear") Integer fiscalYear, @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 查询基准场景
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 基准场景
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE FISCAL_YEAR = #{fiscalYear} AND MODEL_ID = #{modelId} AND IS_BASELINE = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetScenario selectBaselineScenario(@Param("fiscalYear") Integer fiscalYear, @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 查询已发布的场景列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 已发布的场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE FISCAL_YEAR = #{fiscalYear} AND IS_PUBLISHED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY PUBLISH_TIME DESC")
    List<BudgetScenario> selectPublishedScenarios(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 查询启用的场景列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 启用的场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE FISCAL_YEAR = #{fiscalYear} AND IS_ENABLED = 1 AND STATUS = 'active' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectEnabledScenarios(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 查询锁定的场景列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 锁定的场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE FISCAL_YEAR = #{fiscalYear} AND IS_LOCKED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY LOCK_TIME DESC")
    List<BudgetScenario> selectLockedScenarios(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 查询我创建的场景列表
     * @param createBy 创建人ID
     * @param tenantId 租户ID
     * @return 我创建的场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE CREATE_BY = #{createBy} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectMyScenarios(@Param("createBy") String createBy, @Param("tenantId") String tenantId);

    /**
     * 查询待审批的场景列表
     * @param approverId 审批人ID
     * @param tenantId 租户ID
     * @return 待审批的场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE APPROVER_ID = #{approverId} AND APPROVAL_STATUS = 'submitted' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetScenario> selectPendingApprovalScenarios(@Param("approverId") String approverId, @Param("tenantId") String tenantId);

    /**
     * 查询计算中的场景列表
     * @param tenantId 租户ID
     * @return 计算中的场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE CALCULATION_STATUS = 'calculating' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CALCULATION_START_TIME DESC")
    List<BudgetScenario> selectCalculatingScenarios(@Param("tenantId") String tenantId);

    /**
     * 查询计算失败的场景列表
     * @param tenantId 租户ID
     * @return 计算失败的场景列表
     */
    @Select("SELECT * FROM BUDGET_SCENARIO WHERE CALCULATION_STATUS = 'failed' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CALCULATION_END_TIME DESC")
    List<BudgetScenario> selectFailedScenarios(@Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计场景总数
     * @param tenantId 租户ID
     * @return 场景总数
     */
    @Select("SELECT COUNT(*) FROM BUDGET_SCENARIO WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countTotalScenarios(@Param("tenantId") String tenantId);

    /**
     * 按场景类型统计数量
     * @param tenantId 租户ID
     * @return 各类型场景数量统计
     */
    List<Map<String, Object>> countScenariosByType(@Param("tenantId") String tenantId);

    /**
     * 按审批状态统计数量
     * @param tenantId 租户ID
     * @return 各审批状态场景数量统计
     */
    List<Map<String, Object>> countScenariosByApprovalStatus(@Param("tenantId") String tenantId);

    /**
     * 按计算状态统计数量
     * @param tenantId 租户ID
     * @return 各计算状态场景数量统计
     */
    List<Map<String, Object>> countScenariosByCalculationStatus(@Param("tenantId") String tenantId);

    /**
     * 按年度统计场景数量
     * @param tenantId 租户ID
     * @return 各年度场景数量统计
     */
    List<Map<String, Object>> countScenariosByYear(@Param("tenantId") String tenantId);

    /**
     * 获取场景统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectScenarioStatistics(@Param("tenantId") String tenantId);

    /**
     * 获取用户场景统计信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户场景统计信息
     */
    Map<String, Object> selectUserScenarioStatistics(@Param("userId") String userId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 发布场景
     * @param scenarioId 场景ID
     * @param publishedBy 发布人ID
     * @param publishedByName 发布人姓名
     * @param publishTime 发布时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_SCENARIO SET IS_PUBLISHED = 1, PUBLISHED_BY = #{publishedBy}, PUBLISHED_BY_NAME = #{publishedByName}, PUBLISH_TIME = #{publishTime} WHERE ID = #{scenarioId}")
    int publishScenario(@Param("scenarioId") String scenarioId, @Param("publishedBy") String publishedBy, 
                       @Param("publishedByName") String publishedByName, @Param("publishTime") LocalDateTime publishTime);

    /**
     * 取消发布场景
     * @param scenarioId 场景ID
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_SCENARIO SET IS_PUBLISHED = 0, PUBLISHED_BY = NULL, PUBLISHED_BY_NAME = NULL, PUBLISH_TIME = NULL WHERE ID = #{scenarioId}")
    int unpublishScenario(@Param("scenarioId") String scenarioId);

    /**
     * 锁定场景
     * @param scenarioId 场景ID
     * @param lockedBy 锁定人ID
     * @param lockedByName 锁定人姓名
     * @param lockTime 锁定时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_SCENARIO SET IS_LOCKED = 1, LOCKED_BY = #{lockedBy}, LOCKED_BY_NAME = #{lockedByName}, LOCK_TIME = #{lockTime} WHERE ID = #{scenarioId}")
    int lockScenario(@Param("scenarioId") String scenarioId, @Param("lockedBy") String lockedBy, 
                    @Param("lockedByName") String lockedByName, @Param("lockTime") LocalDateTime lockTime);

    /**
     * 解锁场景
     * @param scenarioId 场景ID
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_SCENARIO SET IS_LOCKED = 0, LOCKED_BY = NULL, LOCKED_BY_NAME = NULL, LOCK_TIME = NULL WHERE ID = #{scenarioId}")
    int unlockScenario(@Param("scenarioId") String scenarioId);

    /**
     * 设置默认场景
     * @param scenarioId 场景ID
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 更新数量
     */
    int setDefaultScenario(@Param("scenarioId") String scenarioId, @Param("fiscalYear") Integer fiscalYear, 
                          @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 设置基准场景
     * @param scenarioId 场景ID
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 更新数量
     */
    int setBaselineScenario(@Param("scenarioId") String scenarioId, @Param("fiscalYear") Integer fiscalYear, 
                           @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 更新计算状态
     * @param scenarioId 场景ID
     * @param calculationStatus 计算状态
     * @param calculationStartTime 计算开始时间
     * @param calculationEndTime 计算结束时间
     * @param calculationError 计算错误信息
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_SCENARIO SET CALCULATION_STATUS = #{calculationStatus}, CALCULATION_START_TIME = #{calculationStartTime}, CALCULATION_END_TIME = #{calculationEndTime}, CALCULATION_ERROR = #{calculationError} WHERE ID = #{scenarioId}")
    int updateCalculationStatus(@Param("scenarioId") String scenarioId, @Param("calculationStatus") String calculationStatus, 
                               @Param("calculationStartTime") LocalDateTime calculationStartTime, @Param("calculationEndTime") LocalDateTime calculationEndTime, 
                               @Param("calculationError") String calculationError);

    /**
     * 批量更新场景状态
     * @param scenarioIds 场景ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateScenarioStatus(@Param("scenarioIds") List<String> scenarioIds, @Param("status") String status, 
                                 @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量更新场景启用状态
     * @param scenarioIds 场景ID列表
     * @param isEnabled 是否启用
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateScenarioEnabled(@Param("scenarioIds") List<String> scenarioIds, @Param("isEnabled") Boolean isEnabled, 
                                  @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理过期的场景
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupExpiredScenarios(@Param("tenantId") String tenantId);

    /**
     * 归档已完成的场景
     * @param days 完成天数
     * @param tenantId 租户ID
     * @return 归档数量
     */
    int archiveCompletedScenarios(@Param("days") Integer days, @Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查场景编码是否存在
     * @param scenarioCode 场景编码
     * @param excludeId 排除的场景ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_SCENARIO WHERE SCENARIO_CODE = #{scenarioCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkScenarioCodeExists(@Param("scenarioCode") String scenarioCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查场景名称是否存在
     * @param scenarioName 场景名称
     * @param excludeId 排除的场景ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_SCENARIO WHERE SCENARIO_NAME = #{scenarioName} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkScenarioNameExists(@Param("scenarioName") String scenarioName, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查场景是否被使用
     * @param scenarioId 场景ID
     * @param tenantId 租户ID
     * @return 是否被使用
     */
    int checkScenarioInUse(@Param("scenarioId") String scenarioId, @Param("tenantId") String tenantId);
}
