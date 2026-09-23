package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetForecast;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算预测Mapper接口
 * 
 * @description 预算预测数据访问层，支持滚动预测、智能预测和多场景预测
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetForecastMapper extends BaseMapper<BudgetForecast> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据预测编码查询预算预测
     * @param forecastCode 预测编码
     * @param tenantId 租户ID
     * @return 预算预测信息
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE FORECAST_CODE = #{forecastCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetForecast selectByForecastCode(@Param("forecastCode") String forecastCode, @Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询预测列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 根据组织ID查询预测列表
     * @param organizationId 组织ID
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE ORGANIZATION_ID = #{organizationId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByOrganizationId(@Param("organizationId") String organizationId, @Param("tenantId") String tenantId);

    /**
     * 根据指标ID查询预测列表
     * @param indicatorId 指标ID
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE INDICATOR_ID = #{indicatorId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByIndicatorId(@Param("indicatorId") String indicatorId, @Param("tenantId") String tenantId);

    /**
     * 根据预测类型查询预测列表
     * @param forecastType 预测类型
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE FORECAST_TYPE = #{forecastType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByForecastType(@Param("forecastType") String forecastType, @Param("tenantId") String tenantId);

    /**
     * 根据预测方法查询预测列表
     * @param forecastMethod 预测方法
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE FORECAST_METHOD = #{forecastMethod} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByForecastMethod(@Param("forecastMethod") String forecastMethod, @Param("tenantId") String tenantId);

    /**
     * 根据审批状态查询预测列表
     * @param approvalStatus 审批状态
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE APPROVAL_STATUS = #{approvalStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus, @Param("tenantId") String tenantId);

    /**
     * 根据发布状态查询预测列表
     * @param publishStatus 发布状态
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE PUBLISH_STATUS = #{publishStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByPublishStatus(@Param("publishStatus") String publishStatus, @Param("tenantId") String tenantId);

    /**
     * 根据预测期间查询预测列表
     * @param forecastPeriod 预测期间
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE FORECAST_PERIOD = #{forecastPeriod} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByForecastPeriod(@Param("forecastPeriod") String forecastPeriod, @Param("tenantId") String tenantId);

    /**
     * 根据场景ID查询预测列表
     * @param scenarioId 场景ID
     * @param tenantId 租户ID
     * @return 预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE SCENARIO_ID = #{scenarioId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectByScenarioId(@Param("scenarioId") String scenarioId, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算预测
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetForecast> selectBudgetForecastPage(Page<BudgetForecast> page, @Param("params") Map<String, Object> params);

    /**
     * 查询我负责的预测列表
     * @param forecasterId 预测师ID
     * @param tenantId 租户ID
     * @return 我负责的预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE FORECASTER_ID = #{forecasterId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectMyForecasts(@Param("forecasterId") String forecasterId, @Param("tenantId") String tenantId);

    /**
     * 查询待审批的预测列表
     * @param approverId 审批人ID
     * @param tenantId 租户ID
     * @return 待审批的预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE APPROVER_ID = #{approverId} AND APPROVAL_STATUS = 'submitted' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY FORECAST_TIME DESC")
    List<BudgetForecast> selectPendingApprovals(@Param("approverId") String approverId, @Param("tenantId") String tenantId);

    /**
     * 查询已发布的预测列表
     * @param tenantId 租户ID
     * @return 已发布的预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE PUBLISH_STATUS = 'published' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY PUBLISH_TIME DESC")
    List<BudgetForecast> selectPublishedForecasts(@Param("tenantId") String tenantId);

    /**
     * 查询滚动预测列表
     * @param tenantId 租户ID
     * @return 滚动预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE FORECAST_TYPE = 'rolling' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetForecast> selectRollingForecasts(@Param("tenantId") String tenantId);

    /**
     * 查询自动预测列表
     * @param tenantId 租户ID
     * @return 自动预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE IS_AUTO_FORECAST = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY NEXT_FORECAST_TIME ASC")
    List<BudgetForecast> selectAutoForecasts(@Param("tenantId") String tenantId);

    /**
     * 查询需要预测的列表
     * @param currentTime 当前时间
     * @param tenantId 租户ID
     * @return 需要预测的列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE NEXT_FORECAST_TIME <= #{currentTime} AND IS_AUTO_FORECAST = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY NEXT_FORECAST_TIME ASC")
    List<BudgetForecast> selectForecastsNeedProcess(@Param("currentTime") LocalDateTime currentTime, @Param("tenantId") String tenantId);

    /**
     * 查询高准确度预测列表
     * @param minAccuracy 最小准确度
     * @param tenantId 租户ID
     * @return 高准确度预测列表
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE FORECAST_ACCURACY >= #{minAccuracy} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY FORECAST_ACCURACY DESC")
    List<BudgetForecast> selectHighAccuracyForecasts(@Param("minAccuracy") BigDecimal minAccuracy, @Param("tenantId") String tenantId);

    /**
     * 查询预测趋势数据
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param tenantId 租户ID
     * @return 预测趋势数据
     */
    List<BudgetForecast> selectForecastTrend(@Param("organizationId") String organizationId, 
                                            @Param("indicatorId") String indicatorId, 
                                            @Param("startPeriod") String startPeriod, 
                                            @Param("endPeriod") String endPeriod, 
                                            @Param("tenantId") String tenantId);

    /**
     * 查询预测对比数据
     * @param organizationIds 组织ID列表
     * @param indicatorId 指标ID
     * @param forecastPeriod 预测期间
     * @param tenantId 租户ID
     * @return 预测对比数据
     */
    List<BudgetForecast> selectForecastComparison(@Param("organizationIds") List<String> organizationIds, 
                                                 @Param("indicatorId") String indicatorId, 
                                                 @Param("forecastPeriod") String forecastPeriod, 
                                                 @Param("tenantId") String tenantId);

    /**
     * 查询场景预测数据
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param forecastPeriod 预测期间
     * @param tenantId 租户ID
     * @return 场景预测数据
     */
    List<BudgetForecast> selectScenarioForecasts(@Param("organizationId") String organizationId, 
                                                @Param("indicatorId") String indicatorId, 
                                                @Param("forecastPeriod") String forecastPeriod, 
                                                @Param("tenantId") String tenantId);

    /**
     * 查询最新预测版本
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param forecastType 预测类型
     * @param tenantId 租户ID
     * @return 最新预测版本
     */
    @Select("SELECT * FROM BUDGET_FORECAST WHERE ORGANIZATION_ID = #{organizationId} AND INDICATOR_ID = #{indicatorId} AND FORECAST_TYPE = #{forecastType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY FORECAST_TIME DESC LIMIT 1")
    BudgetForecast selectLatestForecast(@Param("organizationId") String organizationId, 
                                       @Param("indicatorId") String indicatorId, 
                                       @Param("forecastType") String forecastType, 
                                       @Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计预测总数
     * @param tenantId 租户ID
     * @return 预测总数
     */
    @Select("SELECT COUNT(*) FROM BUDGET_FORECAST WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countTotalForecasts(@Param("tenantId") String tenantId);

    /**
     * 按预测类型统计数量
     * @param tenantId 租户ID
     * @return 各预测类型数量统计
     */
    List<Map<String, Object>> countForecastsByType(@Param("tenantId") String tenantId);

    /**
     * 按预测方法统计数量
     * @param tenantId 租户ID
     * @return 各预测方法数量统计
     */
    List<Map<String, Object>> countForecastsByMethod(@Param("tenantId") String tenantId);

    /**
     * 按审批状态统计数量
     * @param tenantId 租户ID
     * @return 各审批状态数量统计
     */
    List<Map<String, Object>> countForecastsByApprovalStatus(@Param("tenantId") String tenantId);

    /**
     * 按发布状态统计数量
     * @param tenantId 租户ID
     * @return 各发布状态数量统计
     */
    List<Map<String, Object>> countForecastsByPublishStatus(@Param("tenantId") String tenantId);

    /**
     * 按年度统计预测数量
     * @param tenantId 租户ID
     * @return 各年度预测数量统计
     */
    List<Map<String, Object>> countForecastsByYear(@Param("tenantId") String tenantId);

    /**
     * 按月份统计预测数量
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各月份预测数量统计
     */
    List<Map<String, Object>> countForecastsByMonth(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计预测值
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 预测值统计
     */
    Map<String, Object> sumForecastValues(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按组织统计预测值
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各组织预测值统计
     */
    List<Map<String, Object>> sumForecastValuesByOrganization(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按指标统计预测值
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各指标预测值统计
     */
    List<Map<String, Object>> sumForecastValuesByIndicator(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计预测准确度分布
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 预测准确度分布统计
     */
    List<Map<String, Object>> countForecastAccuracyDistribution(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按预测方法统计准确度
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各预测方法准确度统计
     */
    List<Map<String, Object>> avgForecastAccuracyByMethod(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 获取预测统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectForecastStatistics(@Param("tenantId") String tenantId);

    /**
     * 获取用户预测统计信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户预测统计信息
     */
    Map<String, Object> selectUserForecastStatistics(@Param("userId") String userId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 审批通过
     * @param forecastId 预测ID
     * @param approvalComments 审批意见
     * @param approvalTime 审批时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_FORECAST SET APPROVAL_STATUS = 'approved', APPROVAL_COMMENTS = #{approvalComments}, APPROVAL_TIME = #{approvalTime} WHERE ID = #{forecastId}")
    int approveForecast(@Param("forecastId") String forecastId, @Param("approvalComments") String approvalComments, @Param("approvalTime") LocalDateTime approvalTime);

    /**
     * 审批拒绝
     * @param forecastId 预测ID
     * @param approvalComments 审批意见
     * @param approvalTime 审批时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_FORECAST SET APPROVAL_STATUS = 'rejected', APPROVAL_COMMENTS = #{approvalComments}, APPROVAL_TIME = #{approvalTime} WHERE ID = #{forecastId}")
    int rejectForecast(@Param("forecastId") String forecastId, @Param("approvalComments") String approvalComments, @Param("approvalTime") LocalDateTime approvalTime);

    /**
     * 发布预测
     * @param forecastId 预测ID
     * @param publisherId 发布人ID
     * @param publisherName 发布人姓名
     * @param publishTime 发布时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_FORECAST SET PUBLISH_STATUS = 'published', PUBLISHER_ID = #{publisherId}, PUBLISHER_NAME = #{publisherName}, PUBLISH_TIME = #{publishTime} WHERE ID = #{forecastId}")
    int publishForecast(@Param("forecastId") String forecastId, @Param("publisherId") String publisherId, 
                       @Param("publisherName") String publisherName, @Param("publishTime") LocalDateTime publishTime);

    /**
     * 取消发布
     * @param forecastId 预测ID
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_FORECAST SET PUBLISH_STATUS = 'unpublished' WHERE ID = #{forecastId}")
    int unpublishForecast(@Param("forecastId") String forecastId);

    /**
     * 更新预测准确度
     * @param forecastId 预测ID
     * @param forecastAccuracy 预测准确度
     * @param accuracyUpdateTime 准确度更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_FORECAST SET FORECAST_ACCURACY = #{forecastAccuracy}, ACCURACY_UPDATE_TIME = #{accuracyUpdateTime} WHERE ID = #{forecastId}")
    int updateForecastAccuracy(@Param("forecastId") String forecastId, 
                              @Param("forecastAccuracy") BigDecimal forecastAccuracy, 
                              @Param("accuracyUpdateTime") LocalDateTime accuracyUpdateTime);

    /**
     * 更新下次预测时间
     * @param forecastId 预测ID
     * @param nextForecastTime 下次预测时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_FORECAST SET NEXT_FORECAST_TIME = #{nextForecastTime} WHERE ID = #{forecastId}")
    int updateNextForecastTime(@Param("forecastId") String forecastId, @Param("nextForecastTime") LocalDateTime nextForecastTime);

    /**
     * 批量更新预测状态
     * @param forecastIds 预测ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateForecastStatus(@Param("forecastIds") List<String> forecastIds, @Param("status") String status, 
                                 @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量启用/停用自动预测
     * @param forecastIds 预测ID列表
     * @param isAutoForecast 是否自动预测
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateAutoForecast(@Param("forecastIds") List<String> forecastIds, @Param("isAutoForecast") Boolean isAutoForecast, 
                               @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理历史预测数据
     * @param days 保留天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupHistoricalForecasts(@Param("days") Integer days, @Param("tenantId") String tenantId);

    /**
     * 清理低准确度预测
     * @param maxAccuracy 最大准确度阈值
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupLowAccuracyForecasts(@Param("maxAccuracy") BigDecimal maxAccuracy, @Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查预测编码是否存在
     * @param forecastCode 预测编码
     * @param excludeId 排除的预测ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_FORECAST WHERE FORECAST_CODE = #{forecastCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkForecastCodeExists(@Param("forecastCode") String forecastCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查预测是否存在
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param forecastPeriod 预测期间
     * @param forecastType 预测类型
     * @param excludeId 排除的预测ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_FORECAST WHERE ORGANIZATION_ID = #{organizationId} AND INDICATOR_ID = #{indicatorId} AND FORECAST_PERIOD = #{forecastPeriod} AND FORECAST_TYPE = #{forecastType} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkForecastExists(@Param("organizationId") String organizationId, 
                           @Param("indicatorId") String indicatorId, 
                           @Param("forecastPeriod") String forecastPeriod, 
                           @Param("forecastType") String forecastType, 
                           @Param("excludeId") String excludeId, 
                           @Param("tenantId") String tenantId);
}
