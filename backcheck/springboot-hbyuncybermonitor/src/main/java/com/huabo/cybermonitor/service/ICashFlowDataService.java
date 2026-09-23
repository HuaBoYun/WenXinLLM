package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.CashFlowData;
import com.huabo.cybermonitor.vo.CashFlowDataQueryVO;

import java.util.List;
import java.util.Map;

/**
 * 现金流量表数据服务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface ICashFlowDataService extends IService<CashFlowData> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 分页查询现金流量表数据列表
     */
    Map<String, Object> selectCashFlowDataList(CashFlowDataQueryVO queryVO);

    /**
     * 根据ID查询现金流量表数据详情
     */
    CashFlowData selectCashFlowDataById(String cashFlowId);

    /**
     * 新增现金流量表数据
     */
    boolean insertCashFlowData(CashFlowData cashFlowData);

    /**
     * 修改现金流量表数据
     */
    boolean updateCashFlowData(CashFlowData cashFlowData);

    /**
     * 删除现金流量表数据
     */
    boolean deleteCashFlowDataById(String cashFlowId);

    /**
     * 批量删除现金流量表数据
     */
    boolean deleteCashFlowDataByIds(List<String> cashFlowIds);

    // ==================== 业务查询方法 ====================

    /**
     * 根据企业ID查询现金流量表数据
     */
    List<CashFlowData> selectByEnterpriseId(String enterpriseId);

    /**
     * 根据报表年度查询
     */
    List<CashFlowData> selectByReportYear(Integer reportYear);

    /**
     * 查询最新现金流量表数据
     */
    List<CashFlowData> selectLatestCashFlowData(String enterpriseId, Integer limit);

    // ==================== 经营活动现金流量分析方法 ====================

    /**
     * 经营活动现金流量综合分析
     */
    Map<String, Object> analyzeOperatingCashFlow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 经营活动现金流入分析
     */
    Map<String, Object> analyzeOperatingCashInflow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 经营活动现金流出分析
     */
    Map<String, Object> analyzeOperatingCashOutflow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 销售收现分析
     */
    Map<String, Object> analyzeSalesCashReceipt(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 采购付现分析
     */
    Map<String, Object> analyzePurchaseCashPayment(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 税费支付分析
     */
    Map<String, Object> analyzeTaxPayment(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 职工薪酬支付分析
     */
    Map<String, Object> analyzeEmployeePayment(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 经营现金流质量评估
     */
    Map<String, Object> assessOperatingCashFlowQuality(String enterpriseId, Integer reportYear);

    // ==================== 投资活动现金流量分析方法 ====================

    /**
     * 投资活动现金流量综合分析
     */
    Map<String, Object> analyzeInvestingCashFlow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 投资活动现金流入分析
     */
    Map<String, Object> analyzeInvestingCashInflow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 投资活动现金流出分析
     */
    Map<String, Object> analyzeInvestingCashOutflow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 固定资产投资分析
     */
    Map<String, Object> analyzeFixedAssetInvestment(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 对外投资分析
     */
    Map<String, Object> analyzeExternalInvestment(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 投资收益现金回收分析
     */
    Map<String, Object> analyzeInvestmentReturnCash(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 资产处置现金回收分析
     */
    Map<String, Object> analyzeAssetDisposalCash(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 投资效率评估
     */
    Map<String, Object> assessInvestmentEfficiency(String enterpriseId, Integer reportYear);

    // ==================== 筹资活动现金流量分析方法 ====================

    /**
     * 筹资活动现金流量综合分析
     */
    Map<String, Object> analyzeFinancingCashFlow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 筹资活动现金流入分析
     */
    Map<String, Object> analyzeFinancingCashInflow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 筹资活动现金流出分析
     */
    Map<String, Object> analyzeFinancingCashOutflow(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 股权融资分析
     */
    Map<String, Object> analyzeEquityFinancing(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 债务融资分析
     */
    Map<String, Object> analyzeDebtFinancing(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 债务偿还分析
     */
    Map<String, Object> analyzeDebtRepayment(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 股利分配分析
     */
    Map<String, Object> analyzeDividendDistribution(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 筹资结构优化建议
     */
    Map<String, Object> getFinancingStructureOptimizationAdvice(String enterpriseId, Integer reportYear);

    // ==================== 现金流量质量分析方法 ====================

    /**
     * 现金流量质量综合分析
     */
    Map<String, Object> analyzeCashFlowQuality(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量结构分析
     */
    Map<String, Object> analyzeCashFlowStructure(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量充足性分析
     */
    Map<String, Object> analyzeCashFlowAdequacy(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量稳定性分析
     */
    Map<String, Object> analyzeCashFlowStability(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量与利润匹配分析
     */
    Map<String, Object> analyzeCashFlowProfitMatching(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量预测分析
     */
    Map<String, Object> forecastCashFlow(String enterpriseId, Integer forecastYear);

    // ==================== 现金管理分析方法 ====================

    /**
     * 现金管理综合分析
     */
    Map<String, Object> analyzeCashManagement(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金周转分析
     */
    Map<String, Object> analyzeCashTurnover(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金持有量分析
     */
    Map<String, Object> analyzeCashHolding(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金使用效率分析
     */
    Map<String, Object> analyzeCashUtilizationEfficiency(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金管理优化建议
     */
    Map<String, Object> getCashManagementOptimizationAdvice(String enterpriseId, Integer reportYear);

    // ==================== 现金流量比率分析方法 ====================

    /**
     * 现金流量比率综合分析
     */
    Map<String, Object> analyzeCashFlowRatio(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量覆盖比率分析
     */
    Map<String, Object> analyzeCashFlowCoverageRatio(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金再投资比率分析
     */
    Map<String, Object> analyzeCashReinvestmentRatio(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金流量比率预警
     */
    Map<String, Object> getCashFlowRatioWarning(String enterpriseId, Integer reportYear);

    // ==================== 趋势分析方法 ====================

    /**
     * 现金流量趋势综合分析
     */
    Map<String, Object> analyzeCashFlowTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 经营现金流趋势分析
     */
    Map<String, Object> analyzeOperatingCashFlowTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 投资现金流趋势分析
     */
    Map<String, Object> analyzeInvestingCashFlowTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 筹资现金流趋势分析
     */
    Map<String, Object> analyzeFinancingCashFlowTrend(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 现金净变动趋势分析
     */
    Map<String, Object> analyzeNetCashChangeTrend(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 现金流量比率计算方法 ====================

    /**
     * 自动计算现金流量比率
     */
    CashFlowData calculateCashFlowRatios(String cashFlowId);

    /**
     * 批量计算现金流量比率
     */
    Map<String, Object> batchCalculateCashFlowRatios(List<String> enterpriseIds, Integer reportYear, Integer reportPeriod);

    /**
     * 重新计算现金流量比率
     */
    boolean recalculateCashFlowRatios(String cashFlowId);

    // ==================== 统计分析方法 ====================

    /**
     * 按数据状态统计
     */
    List<Map<String, Object>> getDataStatusStatistics(Integer startYear, Integer endYear);

    /**
     * 现金流量规模分布统计
     */
    List<Map<String, Object>> getCashFlowScaleDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 现金流量质量分布统计
     */
    List<Map<String, Object>> getCashFlowQualityDistributionStatistics(Integer startYear, Integer endYear);

    /**
     * 现金流量表统计概览
     */
    Map<String, Object> getCashFlowStatisticsOverview();

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新数据状态
     */
    boolean batchUpdateDataStatus(List<String> cashFlowIds, String dataStatus);

    /**
     * 批量更新审核状态
     */
    boolean batchUpdateAuditStatus(List<String> cashFlowIds, String auditStatus);

    /**
     * 批量导入现金流量表数据
     */
    Map<String, Object> batchImportCashFlowData(List<CashFlowData> cashFlowDataList);

    // ==================== 导出功能方法 ====================

    /**
     * 导出现金流量表数据列表
     */
    List<Map<String, Object>> exportCashFlowDataList(CashFlowDataQueryVO queryVO);

    /**
     * 导出现金流量分析报告
     */
    Map<String, Object> exportCashFlowAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出现金流量质量分析报告
     */
    Map<String, Object> exportCashFlowQualityAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    /**
     * 导出现金管理分析报告
     */
    Map<String, Object> exportCashManagementAnalysisReport(String enterpriseId, Integer startYear, Integer endYear);

    // ==================== 标签转换方法 ====================

    /**
     * 获取数据状态标签
     */
    String getDataStatusLabel(String dataStatus);

    /**
     * 获取数据来源标签
     */
    String getDataSourceLabel(String dataSource);

    /**
     * 获取审核状态标签
     */
    String getAuditStatusLabel(String auditStatus);

    // ==================== 数据维护方法 ====================

    /**
     * 删除过期现金流量表记录
     */
    int deleteExpiredCashFlowRecords(Integer days);

    /**
     * 数据清理和优化
     */
    Map<String, Object> cleanAndOptimizeData();

    /**
     * 重新计算所有比率
     */
    boolean recalculateAllRatios(List<String> cashFlowIds);

}
