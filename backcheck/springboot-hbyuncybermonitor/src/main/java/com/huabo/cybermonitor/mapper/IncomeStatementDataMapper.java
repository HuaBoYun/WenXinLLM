package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.IncomeStatementData;
import com.huabo.cybermonitor.vo.IncomeStatementDataQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 利润表数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface IncomeStatementDataMapper extends BaseMapper<IncomeStatementData> {

    // ==================== 基础查询方法 ====================

    /**
     * 分页查询利润表数据列表
     */
    List<IncomeStatementData> selectIncomeStatementDataList(@Param("queryVO") IncomeStatementDataQueryVO queryVO);

    /**
     * 根据企业ID查询利润表数据
     */
    List<IncomeStatementData> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据报表年度查询
     */
    List<IncomeStatementData> selectByReportYear(@Param("reportYear") Integer reportYear);

    /**
     * 根据数据状态查询
     */
    List<IncomeStatementData> selectByDataStatus(@Param("dataStatus") String dataStatus);

    /**
     * 根据数据来源查询
     */
    List<IncomeStatementData> selectByDataSource(@Param("dataSource") String dataSource);

    /**
     * 根据审核状态查询
     */
    List<IncomeStatementData> selectByAuditStatus(@Param("auditStatus") String auditStatus);

    /**
     * 查询最新利润表数据
     */
    List<IncomeStatementData> selectLatestIncomeStatementData(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("limit") Integer limit);

    // ==================== 盈利能力分析方法 ====================

    /**
     * 盈利能力分析
     */
    List<Map<String, Object>> selectProfitabilityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 收入结构分析
     */
    List<Map<String, Object>> selectRevenueStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 成本结构分析
     */
    List<Map<String, Object>> selectCostStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 费用结构分析
     */
    List<Map<String, Object>> selectExpenseStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 利润结构分析
     */
    List<Map<String, Object>> selectProfitStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 毛利率分析
     */
    List<Map<String, Object>> selectGrossProfitMarginAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 净利润率分析
     */
    List<Map<String, Object>> selectNetProfitMarginAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    // ==================== 收入分析方法 ====================

    /**
     * 营业收入分析
     */
    List<Map<String, Object>> selectOperatingRevenueAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 收入增长分析
     */
    List<Map<String, Object>> selectRevenueGrowthAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 收入季节性分析
     */
    List<Map<String, Object>> selectRevenueSeasonalityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                               @Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    /**
     * 其他收益分析
     */
    List<Map<String, Object>> selectOtherIncomeAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                        @Param("startYear") Integer startYear, 
                                                        @Param("endYear") Integer endYear);

    /**
     * 投资收益分析
     */
    List<Map<String, Object>> selectInvestmentIncomeAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    // ==================== 成本费用分析方法 ====================

    /**
     * 营业成本分析
     */
    List<Map<String, Object>> selectOperatingCostAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 期间费用分析
     */
    List<Map<String, Object>> selectPeriodExpenseAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 销售费用分析
     */
    List<Map<String, Object>> selectSellingExpenseAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startYear") Integer startYear, 
                                                           @Param("endYear") Integer endYear);

    /**
     * 管理费用分析
     */
    List<Map<String, Object>> selectAdministrativeExpenseAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                  @Param("startYear") Integer startYear, 
                                                                  @Param("endYear") Integer endYear);

    /**
     * 研发费用分析
     */
    List<Map<String, Object>> selectRDExpenseAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                      @Param("startYear") Integer startYear, 
                                                      @Param("endYear") Integer endYear);

    /**
     * 财务费用分析
     */
    List<Map<String, Object>> selectFinancialExpenseAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 成本费用控制分析
     */
    List<Map<String, Object>> selectCostExpenseControlAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                               @Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    // ==================== 利润分析方法 ====================

    /**
     * 营业利润分析
     */
    List<Map<String, Object>> selectOperatingProfitAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 利润总额分析
     */
    List<Map<String, Object>> selectTotalProfitAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                        @Param("startYear") Integer startYear, 
                                                        @Param("endYear") Integer endYear);

    /**
     * 净利润分析
     */
    List<Map<String, Object>> selectNetProfitAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                      @Param("startYear") Integer startYear, 
                                                      @Param("endYear") Integer endYear);

    /**
     * 利润增长分析
     */
    List<Map<String, Object>> selectProfitGrowthAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 利润质量分析
     */
    List<Map<String, Object>> selectProfitQualityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 每股收益分析
     */
    List<Map<String, Object>> selectEarningsPerShareAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    // ==================== 其他综合收益分析方法 ====================

    /**
     * 其他综合收益分析
     */
    List<Map<String, Object>> selectOtherComprehensiveIncomeAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                     @Param("startYear") Integer startYear, 
                                                                     @Param("endYear") Integer endYear);

    /**
     * 综合收益总额分析
     */
    List<Map<String, Object>> selectTotalComprehensiveIncomeAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                     @Param("startYear") Integer startYear, 
                                                                     @Param("endYear") Integer endYear);

    // ==================== 财务比率分析方法 ====================

    /**
     * 盈利能力比率分析
     */
    List<Map<String, Object>> selectProfitabilityRatioAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                               @Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    /**
     * 费用率分析
     */
    List<Map<String, Object>> selectExpenseRatioAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 税负分析
     */
    List<Map<String, Object>> selectTaxBurdenAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                      @Param("startYear") Integer startYear, 
                                                      @Param("endYear") Integer endYear);

    // ==================== 趋势分析方法 ====================

    /**
     * 收入趋势分析
     */
    List<Map<String, Object>> selectRevenueTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 利润趋势分析
     */
    List<Map<String, Object>> selectProfitTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                        @Param("startYear") Integer startYear, 
                                                        @Param("endYear") Integer endYear);

    /**
     * 成本费用趋势分析
     */
    List<Map<String, Object>> selectCostExpenseTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 盈利能力趋势分析
     */
    List<Map<String, Object>> selectProfitabilityTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                               @Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    // ==================== 统计分析方法 ====================

    /**
     * 按数据状态统计
     */
    List<Map<String, Object>> selectDataStatusStatistics(@Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 按数据来源统计
     */
    List<Map<String, Object>> selectDataSourceStatistics(@Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 按审核状态统计
     */
    List<Map<String, Object>> selectAuditStatusStatistics(@Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 收入规模分布统计
     */
    List<Map<String, Object>> selectRevenueScaleDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                       @Param("endYear") Integer endYear);

    /**
     * 利润水平分布统计
     */
    List<Map<String, Object>> selectProfitLevelDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                      @Param("endYear") Integer endYear);

    /**
     * 盈利能力分布统计
     */
    List<Map<String, Object>> selectProfitabilityDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                        @Param("endYear") Integer endYear);

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新数据状态
     */
    int batchUpdateDataStatus(@Param("incomeStatementIds") List<String> incomeStatementIds, 
                              @Param("dataStatus") String dataStatus);

    /**
     * 批量更新审核状态
     */
    int batchUpdateAuditStatus(@Param("incomeStatementIds") List<String> incomeStatementIds, 
                               @Param("auditStatus") String auditStatus);

    /**
     * 批量计算财务比率
     */
    int batchCalculateFinancialRatios(@Param("enterpriseIds") List<String> enterpriseIds, 
                                      @Param("reportYear") Integer reportYear, 
                                      @Param("reportPeriod") Integer reportPeriod);

    // ==================== 数据维护方法 ====================

    /**
     * 删除过期利润表记录
     */
    int deleteExpiredIncomeStatementRecords(@Param("days") Integer days);

    /**
     * 获取利润表统计概览
     */
    Map<String, Object> selectIncomeStatementStatisticsOverview();

    /**
     * 导出利润表数据列表
     */
    List<Map<String, Object>> exportIncomeStatementDataList(@Param("queryVO") IncomeStatementDataQueryVO queryVO);

}
