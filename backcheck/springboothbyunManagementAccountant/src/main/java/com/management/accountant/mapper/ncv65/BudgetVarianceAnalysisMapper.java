package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetVarianceAnalysis;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算差异分析Mapper接口
 * 
 * @description 预算差异分析数据访问层，支持预算与实际的差异分析和原因分析
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetVarianceAnalysisMapper extends BaseMapper<BudgetVarianceAnalysis> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据分析编码查询差异分析
     * @param analysisCode 分析编码
     * @param tenantId 租户ID
     * @return 差异分析信息
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE ANALYSIS_CODE = #{analysisCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetVarianceAnalysis selectByAnalysisCode(@Param("analysisCode") String analysisCode, @Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询分析列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 根据组织ID查询分析列表
     * @param organizationId 组织ID
     * @param tenantId 租户ID
     * @return 分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE ORGANIZATION_ID = #{organizationId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectByOrganizationId(@Param("organizationId") String organizationId, @Param("tenantId") String tenantId);

    /**
     * 根据指标ID查询分析列表
     * @param indicatorId 指标ID
     * @param tenantId 租户ID
     * @return 分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE INDICATOR_ID = #{indicatorId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectByIndicatorId(@Param("indicatorId") String indicatorId, @Param("tenantId") String tenantId);

    /**
     * 根据分析类型查询分析列表
     * @param analysisType 分析类型
     * @param tenantId 租户ID
     * @return 分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE ANALYSIS_TYPE = #{analysisType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectByAnalysisType(@Param("analysisType") String analysisType, @Param("tenantId") String tenantId);

    /**
     * 根据分析维度查询分析列表
     * @param analysisDimension 分析维度
     * @param tenantId 租户ID
     * @return 分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE ANALYSIS_DIMENSION = #{analysisDimension} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectByAnalysisDimension(@Param("analysisDimension") String analysisDimension, @Param("tenantId") String tenantId);

    /**
     * 根据差异级别查询分析列表
     * @param varianceLevel 差异级别
     * @param tenantId 租户ID
     * @return 分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE VARIANCE_LEVEL = #{varianceLevel} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY VARIANCE_AMOUNT DESC")
    List<BudgetVarianceAnalysis> selectByVarianceLevel(@Param("varianceLevel") String varianceLevel, @Param("tenantId") String tenantId);

    /**
     * 根据差异状态查询分析列表
     * @param varianceStatus 差异状态
     * @param tenantId 租户ID
     * @return 分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE VARIANCE_STATUS = #{varianceStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectByVarianceStatus(@Param("varianceStatus") String varianceStatus, @Param("tenantId") String tenantId);

    /**
     * 根据分析期间查询分析列表
     * @param analysisPeriod 分析期间
     * @param tenantId 租户ID
     * @return 分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE ANALYSIS_PERIOD = #{analysisPeriod} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectByAnalysisPeriod(@Param("analysisPeriod") String analysisPeriod, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询差异分析
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetVarianceAnalysis> selectBudgetVarianceAnalysisPage(Page<BudgetVarianceAnalysis> page, @Param("params") Map<String, Object> params);

    /**
     * 查询我负责的分析列表
     * @param analystId 分析师ID
     * @param tenantId 租户ID
     * @return 我负责的分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE ANALYST_ID = #{analystId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectMyAnalyses(@Param("analystId") String analystId, @Param("tenantId") String tenantId);

    /**
     * 查询待分析的列表
     * @param tenantId 租户ID
     * @return 待分析的列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE ANALYSIS_STATUS = 'pending' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME ASC")
    List<BudgetVarianceAnalysis> selectPendingAnalyses(@Param("tenantId") String tenantId);

    /**
     * 查询重大差异列表
     * @param tenantId 租户ID
     * @return 重大差异列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE VARIANCE_LEVEL IN ('high', 'critical') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY VARIANCE_AMOUNT DESC")
    List<BudgetVarianceAnalysis> selectSignificantVariances(@Param("tenantId") String tenantId);

    /**
     * 查询异常差异列表
     * @param tenantId 租户ID
     * @return 异常差异列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE VARIANCE_STATUS = 'abnormal' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVarianceAnalysis> selectAbnormalVariances(@Param("tenantId") String tenantId);

    /**
     * 查询自动分析列表
     * @param tenantId 租户ID
     * @return 自动分析列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE IS_AUTO_ANALYSIS = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY NEXT_ANALYSIS_TIME ASC")
    List<BudgetVarianceAnalysis> selectAutoAnalyses(@Param("tenantId") String tenantId);

    /**
     * 查询需要分析的列表
     * @param currentTime 当前时间
     * @param tenantId 租户ID
     * @return 需要分析的列表
     */
    @Select("SELECT * FROM BUDGET_VARIANCE_ANALYSIS WHERE NEXT_ANALYSIS_TIME <= #{currentTime} AND IS_AUTO_ANALYSIS = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY NEXT_ANALYSIS_TIME ASC")
    List<BudgetVarianceAnalysis> selectAnalysesNeedProcess(@Param("currentTime") LocalDateTime currentTime, @Param("tenantId") String tenantId);

    /**
     * 查询差异趋势数据
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param tenantId 租户ID
     * @return 差异趋势数据
     */
    List<BudgetVarianceAnalysis> selectVarianceTrend(@Param("organizationId") String organizationId, 
                                                    @Param("indicatorId") String indicatorId, 
                                                    @Param("startPeriod") String startPeriod, 
                                                    @Param("endPeriod") String endPeriod, 
                                                    @Param("tenantId") String tenantId);

    /**
     * 查询差异对比数据
     * @param organizationIds 组织ID列表
     * @param indicatorId 指标ID
     * @param analysisPeriod 分析期间
     * @param tenantId 租户ID
     * @return 差异对比数据
     */
    List<BudgetVarianceAnalysis> selectVarianceComparison(@Param("organizationIds") List<String> organizationIds, 
                                                         @Param("indicatorId") String indicatorId, 
                                                         @Param("analysisPeriod") String analysisPeriod, 
                                                         @Param("tenantId") String tenantId);

    /**
     * 查询根本原因分析
     * @param varianceCauseType 差异原因类型
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 根本原因分析数据
     */
    List<BudgetVarianceAnalysis> selectRootCauseAnalysis(@Param("varianceCauseType") String varianceCauseType, 
                                                        @Param("fiscalYear") Integer fiscalYear, 
                                                        @Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计分析总数
     * @param tenantId 租户ID
     * @return 分析总数
     */
    @Select("SELECT COUNT(*) FROM BUDGET_VARIANCE_ANALYSIS WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countTotalAnalyses(@Param("tenantId") String tenantId);

    /**
     * 按分析类型统计数量
     * @param tenantId 租户ID
     * @return 各分析类型数量统计
     */
    List<Map<String, Object>> countAnalysesByType(@Param("tenantId") String tenantId);

    /**
     * 按分析维度统计数量
     * @param tenantId 租户ID
     * @return 各分析维度数量统计
     */
    List<Map<String, Object>> countAnalysesByDimension(@Param("tenantId") String tenantId);

    /**
     * 按差异级别统计数量
     * @param tenantId 租户ID
     * @return 各差异级别数量统计
     */
    List<Map<String, Object>> countAnalysesByVarianceLevel(@Param("tenantId") String tenantId);

    /**
     * 按差异状态统计数量
     * @param tenantId 租户ID
     * @return 各差异状态数量统计
     */
    List<Map<String, Object>> countAnalysesByVarianceStatus(@Param("tenantId") String tenantId);

    /**
     * 按年度统计分析数量
     * @param tenantId 租户ID
     * @return 各年度分析数量统计
     */
    List<Map<String, Object>> countAnalysesByYear(@Param("tenantId") String tenantId);

    /**
     * 按月份统计分析数量
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各月份分析数量统计
     */
    List<Map<String, Object>> countAnalysesByMonth(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计差异金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 差异金额统计
     */
    Map<String, Object> sumVarianceAmounts(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按组织统计差异金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各组织差异金额统计
     */
    List<Map<String, Object>> sumVarianceAmountsByOrganization(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按指标统计差异金额
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各指标差异金额统计
     */
    List<Map<String, Object>> sumVarianceAmountsByIndicator(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 统计差异率分布
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 差异率分布统计
     */
    List<Map<String, Object>> countVarianceRateDistribution(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 按原因类型统计差异
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 各原因类型差异统计
     */
    List<Map<String, Object>> countVariancesByCauseType(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 获取分析统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectAnalysisStatistics(@Param("tenantId") String tenantId);

    /**
     * 获取用户分析统计信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户分析统计信息
     */
    Map<String, Object> selectUserAnalysisStatistics(@Param("userId") String userId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 更新分析状态
     * @param analysisId 分析ID
     * @param analysisStatus 分析状态
     * @param analysisResult 分析结果
     * @param analysisTime 分析时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_VARIANCE_ANALYSIS SET ANALYSIS_STATUS = #{analysisStatus}, ANALYSIS_RESULT = #{analysisResult}, ANALYSIS_TIME = #{analysisTime} WHERE ID = #{analysisId}")
    int updateAnalysisStatus(@Param("analysisId") String analysisId, 
                            @Param("analysisStatus") String analysisStatus, 
                            @Param("analysisResult") String analysisResult, 
                            @Param("analysisTime") LocalDateTime analysisTime);

    /**
     * 更新差异状态
     * @param analysisId 分析ID
     * @param varianceStatus 差异状态
     * @param varianceLevel 差异级别
     * @param varianceTrend 差异趋势
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_VARIANCE_ANALYSIS SET VARIANCE_STATUS = #{varianceStatus}, VARIANCE_LEVEL = #{varianceLevel}, VARIANCE_TREND = #{varianceTrend} WHERE ID = #{analysisId}")
    int updateVarianceStatus(@Param("analysisId") String analysisId, 
                            @Param("varianceStatus") String varianceStatus, 
                            @Param("varianceLevel") String varianceLevel, 
                            @Param("varianceTrend") String varianceTrend);

    /**
     * 更新下次分析时间
     * @param analysisId 分析ID
     * @param nextAnalysisTime 下次分析时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_VARIANCE_ANALYSIS SET NEXT_ANALYSIS_TIME = #{nextAnalysisTime} WHERE ID = #{analysisId}")
    int updateNextAnalysisTime(@Param("analysisId") String analysisId, @Param("nextAnalysisTime") LocalDateTime nextAnalysisTime);

    /**
     * 批量更新分析状态
     * @param analysisIds 分析ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateAnalysisStatus(@Param("analysisIds") List<String> analysisIds, @Param("status") String status, 
                                 @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量启用/停用自动分析
     * @param analysisIds 分析ID列表
     * @param isAutoAnalysis 是否自动分析
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateAutoAnalysis(@Param("analysisIds") List<String> analysisIds, @Param("isAutoAnalysis") Boolean isAutoAnalysis, 
                               @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理历史分析数据
     * @param days 保留天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupHistoricalAnalyses(@Param("days") Integer days, @Param("tenantId") String tenantId);

    /**
     * 清理已完成的分析
     * @param days 完成天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupCompletedAnalyses(@Param("days") Integer days, @Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查分析编码是否存在
     * @param analysisCode 分析编码
     * @param excludeId 排除的分析ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_VARIANCE_ANALYSIS WHERE ANALYSIS_CODE = #{analysisCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkAnalysisCodeExists(@Param("analysisCode") String analysisCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查分析是否存在
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param analysisPeriod 分析期间
     * @param analysisType 分析类型
     * @param excludeId 排除的分析ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_VARIANCE_ANALYSIS WHERE ORGANIZATION_ID = #{organizationId} AND INDICATOR_ID = #{indicatorId} AND ANALYSIS_PERIOD = #{analysisPeriod} AND ANALYSIS_TYPE = #{analysisType} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkAnalysisExists(@Param("organizationId") String organizationId, 
                           @Param("indicatorId") String indicatorId, 
                           @Param("analysisPeriod") String analysisPeriod, 
                           @Param("analysisType") String analysisType, 
                           @Param("excludeId") String excludeId, 
                           @Param("tenantId") String tenantId);
}
