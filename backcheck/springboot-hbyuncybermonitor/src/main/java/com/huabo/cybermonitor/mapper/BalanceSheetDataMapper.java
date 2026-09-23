package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.BalanceSheetData;
import com.huabo.cybermonitor.vo.BalanceSheetDataQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资产负债表数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface BalanceSheetDataMapper extends BaseMapper<BalanceSheetData> {

    // ==================== 基础查询方法 ====================

    /**
     * 分页查询资产负债表数据列表
     */
    List<BalanceSheetData> selectBalanceSheetDataList(@Param("queryVO") BalanceSheetDataQueryVO queryVO);

    /**
     * 根据企业ID查询资产负债表数据
     */
    List<BalanceSheetData> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据报表年度查询
     */
    List<BalanceSheetData> selectByReportYear(@Param("reportYear") Integer reportYear);

    /**
     * 根据数据状态查询
     */
    List<BalanceSheetData> selectByDataStatus(@Param("dataStatus") String dataStatus);

    /**
     * 根据数据来源查询
     */
    List<BalanceSheetData> selectByDataSource(@Param("dataSource") String dataSource);

    /**
     * 根据审核状态查询
     */
    List<BalanceSheetData> selectByAuditStatus(@Param("auditStatus") String auditStatus);

    /**
     * 查询最新资产负债表数据
     */
    List<BalanceSheetData> selectLatestBalanceSheetData(@Param("enterpriseId") String enterpriseId, 
                                                        @Param("limit") Integer limit);

    // ==================== 资产结构分析方法 ====================

    /**
     * 资产结构分析
     */
    List<Map<String, Object>> selectAssetStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startYear") Integer startYear, 
                                                           @Param("endYear") Integer endYear);

    /**
     * 流动资产分析
     */
    List<Map<String, Object>> selectCurrentAssetAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 非流动资产分析
     */
    List<Map<String, Object>> selectNonCurrentAssetAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 固定资产分析
     */
    List<Map<String, Object>> selectFixedAssetAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                       @Param("startYear") Integer startYear, 
                                                       @Param("endYear") Integer endYear);

    /**
     * 无形资产分析
     */
    List<Map<String, Object>> selectIntangibleAssetAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 投资性资产分析
     */
    List<Map<String, Object>> selectInvestmentAssetAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    // ==================== 负债结构分析方法 ====================

    /**
     * 负债结构分析
     */
    List<Map<String, Object>> selectLiabilityStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                               @Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    /**
     * 流动负债分析
     */
    List<Map<String, Object>> selectCurrentLiabilityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 非流动负债分析
     */
    List<Map<String, Object>> selectNonCurrentLiabilityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                @Param("startYear") Integer startYear, 
                                                                @Param("endYear") Integer endYear);

    /**
     * 借款结构分析
     */
    List<Map<String, Object>> selectBorrowingStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                               @Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    /**
     * 债务期限结构分析
     */
    List<Map<String, Object>> selectDebtMaturityStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                  @Param("startYear") Integer startYear, 
                                                                  @Param("endYear") Integer endYear);

    // ==================== 所有者权益分析方法 ====================

    /**
     * 所有者权益结构分析
     */
    List<Map<String, Object>> selectOwnersEquityStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                  @Param("startYear") Integer startYear, 
                                                                  @Param("endYear") Integer endYear);

    /**
     * 股本结构分析
     */
    List<Map<String, Object>> selectShareCapitalStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                  @Param("startYear") Integer startYear, 
                                                                  @Param("endYear") Integer endYear);

    /**
     * 留存收益分析
     */
    List<Map<String, Object>> selectRetainedEarningsAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 少数股东权益分析
     */
    List<Map<String, Object>> selectMinorityInterestAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    // ==================== 偿债能力分析方法 ====================

    /**
     * 偿债能力分析
     */
    List<Map<String, Object>> selectSolvencyAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                     @Param("startYear") Integer startYear, 
                                                     @Param("endYear") Integer endYear);

    /**
     * 短期偿债能力分析
     */
    List<Map<String, Object>> selectShortTermSolvencyAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 长期偿债能力分析
     */
    List<Map<String, Object>> selectLongTermSolvencyAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 流动性风险分析
     */
    List<Map<String, Object>> selectLiquidityRiskAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 资本结构分析
     */
    List<Map<String, Object>> selectCapitalStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    // ==================== 财务比率分析方法 ====================

    /**
     * 财务比率分析
     */
    List<Map<String, Object>> selectFinancialRatioAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startYear") Integer startYear, 
                                                           @Param("endYear") Integer endYear);

    /**
     * 资产负债率趋势分析
     */
    List<Map<String, Object>> selectAssetLiabilityRatioTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                     @Param("startYear") Integer startYear, 
                                                                     @Param("endYear") Integer endYear);

    /**
     * 流动比率趋势分析
     */
    List<Map<String, Object>> selectCurrentRatioTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 速动比率趋势分析
     */
    List<Map<String, Object>> selectQuickRatioTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 权益乘数趋势分析
     */
    List<Map<String, Object>> selectEquityMultiplierTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                  @Param("startYear") Integer startYear, 
                                                                  @Param("endYear") Integer endYear);

    // ==================== 资产质量分析方法 ====================

    /**
     * 资产质量分析
     */
    List<Map<String, Object>> selectAssetQualityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 应收账款质量分析
     */
    List<Map<String, Object>> selectReceivableQualityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 存货质量分析
     */
    List<Map<String, Object>> selectInventoryQualityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 固定资产质量分析
     */
    List<Map<String, Object>> selectFixedAssetQualityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 投资资产质量分析
     */
    List<Map<String, Object>> selectInvestmentAssetQualityAnalysis(@Param("enterpriseId") String enterpriseId, 
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
     * 资产规模分布统计
     */
    List<Map<String, Object>> selectAssetScaleDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                     @Param("endYear") Integer endYear);

    /**
     * 负债水平分布统计
     */
    List<Map<String, Object>> selectLiabilityLevelDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                         @Param("endYear") Integer endYear);

    /**
     * 资产负债率分布统计
     */
    List<Map<String, Object>> selectAssetLiabilityRatioDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                              @Param("endYear") Integer endYear);

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新数据状态
     */
    int batchUpdateDataStatus(@Param("balanceSheetIds") List<String> balanceSheetIds, 
                              @Param("dataStatus") String dataStatus);

    /**
     * 批量更新审核状态
     */
    int batchUpdateAuditStatus(@Param("balanceSheetIds") List<String> balanceSheetIds, 
                               @Param("auditStatus") String auditStatus);

    /**
     * 批量计算财务比率
     */
    int batchCalculateFinancialRatios(@Param("enterpriseIds") List<String> enterpriseIds, 
                                      @Param("reportYear") Integer reportYear, 
                                      @Param("reportPeriod") Integer reportPeriod);

    // ==================== 数据维护方法 ====================

    /**
     * 删除过期资产负债表记录
     */
    int deleteExpiredBalanceSheetRecords(@Param("days") Integer days);

    /**
     * 获取资产负债表统计概览
     */
    Map<String, Object> selectBalanceSheetStatisticsOverview();

    /**
     * 导出资产负债表数据列表
     */
    List<Map<String, Object>> exportBalanceSheetDataList(@Param("queryVO") BalanceSheetDataQueryVO queryVO);

}
