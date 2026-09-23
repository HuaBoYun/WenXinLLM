package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.CashFlowData;
import com.huabo.cybermonitor.vo.CashFlowDataQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 现金流量表数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface CashFlowDataMapper extends BaseMapper<CashFlowData> {

    // ==================== 基础查询方法 ====================

    /**
     * 分页查询现金流量表数据列表
     */
    List<CashFlowData> selectCashFlowDataList(@Param("queryVO") CashFlowDataQueryVO queryVO);

    /**
     * 根据企业ID查询现金流量表数据
     */
    List<CashFlowData> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据报表年度查询
     */
    List<CashFlowData> selectByReportYear(@Param("reportYear") Integer reportYear);

    /**
     * 根据数据状态查询
     */
    List<CashFlowData> selectByDataStatus(@Param("dataStatus") String dataStatus);

    /**
     * 根据数据来源查询
     */
    List<CashFlowData> selectByDataSource(@Param("dataSource") String dataSource);

    /**
     * 根据审核状态查询
     */
    List<CashFlowData> selectByAuditStatus(@Param("auditStatus") String auditStatus);

    /**
     * 查询最新现金流量表数据
     */
    List<CashFlowData> selectLatestCashFlowData(@Param("enterpriseId") String enterpriseId, 
                                                @Param("limit") Integer limit);

    // ==================== 经营活动现金流量分析方法 ====================

    /**
     * 经营活动现金流量分析
     */
    List<Map<String, Object>> selectOperatingCashFlowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 经营活动现金流入分析
     */
    List<Map<String, Object>> selectOperatingCashInflowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                @Param("startYear") Integer startYear, 
                                                                @Param("endYear") Integer endYear);

    /**
     * 经营活动现金流出分析
     */
    List<Map<String, Object>> selectOperatingCashOutflowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startYear") Integer startYear, 
                                                                 @Param("endYear") Integer endYear);

    /**
     * 销售收现分析
     */
    List<Map<String, Object>> selectSalesCashReceiptAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 采购付现分析
     */
    List<Map<String, Object>> selectPurchaseCashPaymentAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                @Param("startYear") Integer startYear, 
                                                                @Param("endYear") Integer endYear);

    /**
     * 税费支付分析
     */
    List<Map<String, Object>> selectTaxPaymentAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                       @Param("startYear") Integer startYear, 
                                                       @Param("endYear") Integer endYear);

    /**
     * 职工薪酬支付分析
     */
    List<Map<String, Object>> selectEmployeePaymentAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    // ==================== 投资活动现金流量分析方法 ====================

    /**
     * 投资活动现金流量分析
     */
    List<Map<String, Object>> selectInvestingCashFlowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 投资活动现金流入分析
     */
    List<Map<String, Object>> selectInvestingCashInflowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                @Param("startYear") Integer startYear, 
                                                                @Param("endYear") Integer endYear);

    /**
     * 投资活动现金流出分析
     */
    List<Map<String, Object>> selectInvestingCashOutflowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startYear") Integer startYear, 
                                                                 @Param("endYear") Integer endYear);

    /**
     * 固定资产投资分析
     */
    List<Map<String, Object>> selectFixedAssetInvestmentAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startYear") Integer startYear, 
                                                                 @Param("endYear") Integer endYear);

    /**
     * 对外投资分析
     */
    List<Map<String, Object>> selectExternalInvestmentAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                               @Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    /**
     * 投资收益现金回收分析
     */
    List<Map<String, Object>> selectInvestmentReturnCashAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startYear") Integer startYear, 
                                                                 @Param("endYear") Integer endYear);

    /**
     * 资产处置现金回收分析
     */
    List<Map<String, Object>> selectAssetDisposalCashAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    // ==================== 筹资活动现金流量分析方法 ====================

    /**
     * 筹资活动现金流量分析
     */
    List<Map<String, Object>> selectFinancingCashFlowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 筹资活动现金流入分析
     */
    List<Map<String, Object>> selectFinancingCashInflowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                @Param("startYear") Integer startYear, 
                                                                @Param("endYear") Integer endYear);

    /**
     * 筹资活动现金流出分析
     */
    List<Map<String, Object>> selectFinancingCashOutflowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startYear") Integer startYear, 
                                                                 @Param("endYear") Integer endYear);

    /**
     * 股权融资分析
     */
    List<Map<String, Object>> selectEquityFinancingAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 债务融资分析
     */
    List<Map<String, Object>> selectDebtFinancingAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 债务偿还分析
     */
    List<Map<String, Object>> selectDebtRepaymentAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 股利分配分析
     */
    List<Map<String, Object>> selectDividendDistributionAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startYear") Integer startYear, 
                                                                 @Param("endYear") Integer endYear);

    // ==================== 现金流量质量分析方法 ====================

    /**
     * 现金流量质量分析
     */
    List<Map<String, Object>> selectCashFlowQualityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 现金流量结构分析
     */
    List<Map<String, Object>> selectCashFlowStructureAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 现金流量充足性分析
     */
    List<Map<String, Object>> selectCashFlowAdequacyAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 现金流量稳定性分析
     */
    List<Map<String, Object>> selectCashFlowStabilityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 现金流量与利润匹配分析
     */
    List<Map<String, Object>> selectCashFlowProfitMatchingAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                   @Param("startYear") Integer startYear, 
                                                                   @Param("endYear") Integer endYear);

    // ==================== 现金管理分析方法 ====================

    /**
     * 现金管理分析
     */
    List<Map<String, Object>> selectCashManagementAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startYear") Integer startYear, 
                                                           @Param("endYear") Integer endYear);

    /**
     * 现金周转分析
     */
    List<Map<String, Object>> selectCashTurnoverAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 现金持有量分析
     */
    List<Map<String, Object>> selectCashHoldingAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                        @Param("startYear") Integer startYear, 
                                                        @Param("endYear") Integer endYear);

    /**
     * 现金使用效率分析
     */
    List<Map<String, Object>> selectCashUtilizationEfficiencyAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                       @Param("startYear") Integer startYear, 
                                                                       @Param("endYear") Integer endYear);

    // ==================== 现金流量比率分析方法 ====================

    /**
     * 现金流量比率分析
     */
    List<Map<String, Object>> selectCashFlowRatioAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 现金流量覆盖比率分析
     */
    List<Map<String, Object>> selectCashFlowCoverageRatioAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                  @Param("startYear") Integer startYear, 
                                                                  @Param("endYear") Integer endYear);

    /**
     * 现金再投资比率分析
     */
    List<Map<String, Object>> selectCashReinvestmentRatioAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                  @Param("startYear") Integer startYear, 
                                                                  @Param("endYear") Integer endYear);

    // ==================== 趋势分析方法 ====================

    /**
     * 现金流量趋势分析
     */
    List<Map<String, Object>> selectCashFlowTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 经营现金流趋势分析
     */
    List<Map<String, Object>> selectOperatingCashFlowTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                   @Param("startYear") Integer startYear, 
                                                                   @Param("endYear") Integer endYear);

    /**
     * 投资现金流趋势分析
     */
    List<Map<String, Object>> selectInvestingCashFlowTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                   @Param("startYear") Integer startYear, 
                                                                   @Param("endYear") Integer endYear);

    /**
     * 筹资现金流趋势分析
     */
    List<Map<String, Object>> selectFinancingCashFlowTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
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
     * 现金流量规模分布统计
     */
    List<Map<String, Object>> selectCashFlowScaleDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                        @Param("endYear") Integer endYear);

    /**
     * 现金流量质量分布统计
     */
    List<Map<String, Object>> selectCashFlowQualityDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                          @Param("endYear") Integer endYear);

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新数据状态
     */
    int batchUpdateDataStatus(@Param("cashFlowIds") List<String> cashFlowIds, 
                              @Param("dataStatus") String dataStatus);

    /**
     * 批量更新审核状态
     */
    int batchUpdateAuditStatus(@Param("cashFlowIds") List<String> cashFlowIds, 
                               @Param("auditStatus") String auditStatus);

    /**
     * 批量计算现金流量比率
     */
    int batchCalculateCashFlowRatios(@Param("enterpriseIds") List<String> enterpriseIds, 
                                     @Param("reportYear") Integer reportYear, 
                                     @Param("reportPeriod") Integer reportPeriod);

    // ==================== 数据维护方法 ====================

    /**
     * 删除过期现金流量表记录
     */
    int deleteExpiredCashFlowRecords(@Param("days") Integer days);

    /**
     * 获取现金流量表统计概览
     */
    Map<String, Object> selectCashFlowStatisticsOverview();

    /**
     * 导出现金流量表数据列表
     */
    List<Map<String, Object>> exportCashFlowDataList(@Param("queryVO") CashFlowDataQueryVO queryVO);

}
