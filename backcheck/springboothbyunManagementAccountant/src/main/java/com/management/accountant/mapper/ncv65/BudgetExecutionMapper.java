package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetExecution;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算执行监控Mapper接口
 * 
 * @description 预算执行监控数据访问层，支持预算执行情况的实时监控和分析
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetExecutionMapper extends BaseMapper<BudgetExecution> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据执行编码查询预算执行
     * @param executionCode 执行编码
     * @param tenantId 租户ID
     * @return 预算执行信息
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE EXECUTION_CODE = #{executionCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetExecution selectByExecutionCode(@Param("executionCode") String executionCode, @Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询执行列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 根据组织ID查询执行列表
     * @param organizationId 组织ID
     * @param tenantId 租户ID
     * @return 执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE ORGANIZATION_ID = #{organizationId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectByOrganizationId(@Param("organizationId") String organizationId, @Param("tenantId") String tenantId);

    /**
     * 根据指标ID查询执行列表
     * @param indicatorId 指标ID
     * @param tenantId 租户ID
     * @return 执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE INDICATOR_ID = #{indicatorId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectByIndicatorId(@Param("indicatorId") String indicatorId, @Param("tenantId") String tenantId);

    /**
     * 根据执行状态查询执行列表
     * @param executionStatus 执行状态
     * @param tenantId 租户ID
     * @return 执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE EXECUTION_STATUS = #{executionStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectByExecutionStatus(@Param("executionStatus") String executionStatus, @Param("tenantId") String tenantId);

    /**
     * 根据控制状态查询执行列表
     * @param controlStatus 控制状态
     * @param tenantId 租户ID
     * @return 执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE CONTROL_STATUS = #{controlStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectByControlStatus(@Param("controlStatus") String controlStatus, @Param("tenantId") String tenantId);

    /**
     * 根据预警级别查询执行列表
     * @param warningLevel 预警级别
     * @param tenantId 租户ID
     * @return 执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE WARNING_LEVEL = #{warningLevel} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY WARNING_TIME DESC")
    List<BudgetExecution> selectByWarningLevel(@Param("warningLevel") String warningLevel, @Param("tenantId") String tenantId);

    /**
     * 根据执行期间查询执行列表
     * @param executionPeriod 执行期间
     * @param tenantId 租户ID
     * @return 执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE EXECUTION_PERIOD = #{executionPeriod} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectByExecutionPeriod(@Param("executionPeriod") String executionPeriod, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算执行
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetExecution> selectBudgetExecutionPage(Page<BudgetExecution> page, @Param("params") Map<String, Object> params);

    /**
     * 查询我负责的执行监控列表
     * @param ownerId 负责人ID
     * @param tenantId 租户ID
     * @return 我负责的执行监控列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE OWNER_ID = #{ownerId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectMyExecutions(@Param("ownerId") String ownerId, @Param("tenantId") String tenantId);

    /**
     * 查询我监控的执行列表
     * @param monitorId 监控人ID
     * @param tenantId 租户ID
     * @return 我监控的执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE MONITOR_ID = #{monitorId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectMyMonitorings(@Param("monitorId") String monitorId, @Param("tenantId") String tenantId);

    /**
     * 查询预警执行列表
     * @param tenantId 租户ID
     * @return 预警执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE WARNING_LEVEL IN ('medium', 'high', 'critical') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY WARNING_TIME DESC")
    List<BudgetExecution> selectWarningExecutions(@Param("tenantId") String tenantId);

    /**
     * 查询超支执行列表
     * @param tenantId 租户ID
     * @return 超支执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE EXECUTION_STATUS = 'over' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectOverBudgetExecutions(@Param("tenantId") String tenantId);

    /**
     * 查询执行不足列表
     * @param tenantId 租户ID
     * @return 执行不足列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE EXECUTION_STATUS = 'under' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectUnderExecutions(@Param("tenantId") String tenantId);

    /**
     * 查询失控执行列表
     * @param tenantId 租户ID
     * @return 失控执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE CONTROL_STATUS = 'uncontrolled' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExecution> selectUncontrolledExecutions(@Param("tenantId") String tenantId);

    /**
     * 查询自动监控的执行列表
     * @param tenantId 租户ID
     * @return 自动监控的执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE IS_AUTO_MONITORING = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY NEXT_UPDATE_TIME ASC")
    List<BudgetExecution> selectAutoMonitoringExecutions(@Param("tenantId") String tenantId);

    /**
     * 查询需要更新的执行列表
     * @param currentTime 当前时间
     * @param tenantId 租户ID
     * @return 需要更新的执行列表
     */
    @Select("SELECT * FROM BUDGET_EXECUTION WHERE NEXT_UPDATE_TIME <= #{currentTime} AND IS_AUTO_MONITORING = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY NEXT_UPDATE_TIME ASC")
    List<BudgetExecution> selectExecutionsNeedUpdate(@Param("currentTime") LocalDateTime currentTime, @Param("tenantId") String tenantId);

    /**
     * 查询执行趋势数据
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param tenantId 租户ID
     * @return 执行趋势数据
     */
    List<BudgetExecution> selectExecutionTrend(@Param("organizationId") String organizationId, 
                                              @Param("indicatorId") String indicatorId, 
                                              @Param("startPeriod") String startPeriod, 
                                              @Param("endPeriod") String endPeriod, 
                                              @Param("tenantId") String tenantId);

    /**
     * 查询执行对比数据
     * @param organizationIds 组织ID列表
     * @param indicatorId 指标ID
     * @param executionPeriod 执行期间
     * @param tenantId 租户ID
     * @return 执行对比数据
     */
    List<BudgetExecution> selectExecutionComparison(@Param("organizationIds") List<String> organizationIds, 
                                                   @Param("indicatorId") String indicatorId, 
                                                   @Param("executionPeriod") String executionPeriod, 
                                                   @Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计执行总数
     * @param tenantId 租户ID
     * @return 执行总数
     */
    @Select("SELECT COUNT(*) FROM BUDGET_EXECUTION WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countTotalExecutions(@Param("tenantId") String tenantId);

    /**
     * 按执行状态统计数量
     * @param tenantId 租户ID
     * @return 各执行状态数量统计
     */
    List<Map<String, Object>> countExecutionsByStatus(@Param("tenantId") String tenantId);

    /**
     * 按控制状态统计数量
     * @param tenantId 租户ID
     * @return 各控制状态数量统计
     */
    List<Map<String, Object>> countExecutionsByControlStatus(@Param("tenantId") String tenantId);

    /**
     * 按预警级别统计数量
     * @param tenantId 租户ID
     * @return 各预警级别数量统计
     */
    List<Map<String, Object>> countExecutionsByWarningLevel(@Param("tenantId") String tenantId);

    /**
     * 按年度统计执行数量
     * @param tenantId 租户ID
     * @return 各年度执行数量统计
     */
    List<Map<String, Object>> countExecutionsByYear(@Param("tenantId") String tenantId);

    /**
     * 按月份统计执行数量
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各月份执行数量统计
     */
    List<Map<String, Object>> countExecutionsByMonth(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计执行金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 执行金额统计
     */
    Map<String, Object> sumExecutionAmounts(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按组织统计执行金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各组织执行金额统计
     */
    List<Map<String, Object>> sumExecutionAmountsByOrganization(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按指标统计执行金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各指标执行金额统计
     */
    List<Map<String, Object>> sumExecutionAmountsByIndicator(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计执行率分布
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 执行率分布统计
     */
    List<Map<String, Object>> countExecutionRateDistribution(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计差异率分布
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 差异率分布统计
     */
    List<Map<String, Object>> countVarianceRateDistribution(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 获取执行统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectExecutionStatistics(@Param("tenantId") String tenantId);

    /**
     * 获取用户执行统计信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户执行统计信息
     */
    Map<String, Object> selectUserExecutionStatistics(@Param("userId") String userId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 更新执行数据
     * @param executionId 执行ID
     * @param actualAmount 实际金额
     * @param usedAmount 已使用金额
     * @param executionRate 执行率
     * @param varianceAmount 差异金额
     * @param varianceRate 差异率
     * @param lastUpdateTime 最后更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_EXECUTION SET ACTUAL_AMOUNT = #{actualAmount}, USED_AMOUNT = #{usedAmount}, EXECUTION_RATE = #{executionRate}, VARIANCE_AMOUNT = #{varianceAmount}, VARIANCE_RATE = #{varianceRate}, LAST_UPDATE_TIME = #{lastUpdateTime} WHERE ID = #{executionId}")
    int updateExecutionData(@Param("executionId") String executionId, 
                           @Param("actualAmount") BigDecimal actualAmount, 
                           @Param("usedAmount") BigDecimal usedAmount, 
                           @Param("executionRate") BigDecimal executionRate, 
                           @Param("varianceAmount") BigDecimal varianceAmount, 
                           @Param("varianceRate") BigDecimal varianceRate, 
                           @Param("lastUpdateTime") LocalDateTime lastUpdateTime);

    /**
     * 更新执行状态
     * @param executionId 执行ID
     * @param executionStatus 执行状态
     * @param controlStatus 控制状态
     * @param warningLevel 预警级别
     * @param warningReason 预警原因
     * @param warningTime 预警时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_EXECUTION SET EXECUTION_STATUS = #{executionStatus}, CONTROL_STATUS = #{controlStatus}, WARNING_LEVEL = #{warningLevel}, WARNING_REASON = #{warningReason}, WARNING_TIME = #{warningTime} WHERE ID = #{executionId}")
    int updateExecutionStatus(@Param("executionId") String executionId, 
                             @Param("executionStatus") String executionStatus, 
                             @Param("controlStatus") String controlStatus, 
                             @Param("warningLevel") String warningLevel, 
                             @Param("warningReason") String warningReason, 
                             @Param("warningTime") LocalDateTime warningTime);

    /**
     * 更新下次更新时间
     * @param executionId 执行ID
     * @param nextUpdateTime 下次更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_EXECUTION SET NEXT_UPDATE_TIME = #{nextUpdateTime} WHERE ID = #{executionId}")
    int updateNextUpdateTime(@Param("executionId") String executionId, @Param("nextUpdateTime") LocalDateTime nextUpdateTime);

    /**
     * 批量更新执行状态
     * @param executionIds 执行ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateExecutionStatus(@Param("executionIds") List<String> executionIds, @Param("status") String status, 
                                  @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量启用/停用自动监控
     * @param executionIds 执行ID列表
     * @param isAutoMonitoring 是否自动监控
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateAutoMonitoring(@Param("executionIds") List<String> executionIds, @Param("isAutoMonitoring") Boolean isAutoMonitoring, 
                                 @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理历史执行数据
     * @param days 保留天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupHistoricalExecutions(@Param("days") Integer days, @Param("tenantId") String tenantId);

    /**
     * 清理停用的执行监控
     * @param days 停用天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupInactiveExecutions(@Param("days") Integer days, @Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查执行编码是否存在
     * @param executionCode 执行编码
     * @param excludeId 排除的执行ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_EXECUTION WHERE EXECUTION_CODE = #{executionCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkExecutionCodeExists(@Param("executionCode") String executionCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查执行监控是否存在
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param executionPeriod 执行期间
     * @param excludeId 排除的执行ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_EXECUTION WHERE ORGANIZATION_ID = #{organizationId} AND INDICATOR_ID = #{indicatorId} AND EXECUTION_PERIOD = #{executionPeriod} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkExecutionExists(@Param("organizationId") String organizationId, 
                            @Param("indicatorId") String indicatorId, 
                            @Param("executionPeriod") String executionPeriod, 
                            @Param("excludeId") String excludeId, 
                            @Param("tenantId") String tenantId);
}
