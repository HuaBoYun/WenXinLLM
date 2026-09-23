package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ProductCostQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 产品成本服务接口
 * 
 * @author AI Agent
 * @since 2024-12-19
 */
public interface ProductCostService {

    // ========== 基础CRUD操作 ==========

    /**
     * 分页查询产品成本列表
     */
    MyJsonBean<PageResult> getProductCostList(ProductCostQueryParam param);

    /**
     * 根据ID查询产品成本详情
     */
    MyJsonBean<Map<String, Object>> getProductCostById(Long costingId);

    /**
     * 新增产品成本
     */
    MyJsonBean<String> createProductCost(Map<String, Object> productCostData);

    /**
     * 修改产品成本
     */
    MyJsonBean<String> updateProductCost(Map<String, Object> productCostData);

    /**
     * 删除产品成本
     */
    MyJsonBean<String> deleteProductCost(Long costingId);

    /**
     * 批量删除产品成本
     */
    MyJsonBean<String> batchDeleteProductCost(List<Long> costingIds);

    // ========== 产品信息管理 ==========

    /**
     * 查询产品信息列表
     */
    MyJsonBean<PageResult> getProductInfoList(ProductCostQueryParam param);

    /**
     * 新增产品信息
     */
    MyJsonBean<String> createProductInfo(Map<String, Object> productData);

    /**
     * 修改产品信息
     */
    MyJsonBean<String> updateProductInfo(Map<String, Object> productData);

    /**
     * 删除产品信息
     */
    MyJsonBean<String> deleteProductInfo(Long productId);

    /**
     * 查询产品BOM信息
     */
    MyJsonBean<List<Map<String, Object>>> getProductBomInfo(Long productId);

    /**
     * 更新产品BOM信息
     */
    MyJsonBean<String> updateProductBomInfo(Map<String, Object> bomData);

    // ========== 成本核算 ==========

    /**
     * 执行成本核算
     */
    MyJsonBean<String> executeCostAccounting(ProductCostQueryParam param);

    /**
     * 查询成本核算结果
     */
    MyJsonBean<PageResult> getCostAccountingResults(ProductCostQueryParam param);

    /**
     * 重新计算成本
     */
    MyJsonBean<String> recalculateCost(Long costingId);

    /**
     * 批量成本核算
     */
    MyJsonBean<String> batchCostAccounting(List<Long> productIds, String costingPeriod);

    /**
     * 查询成本核算方法配置
     */
    MyJsonBean<List<Map<String, Object>>> getCostingMethodConfig();

    /**
     * 更新成本核算方法配置
     */
    MyJsonBean<String> updateCostingMethodConfig(Map<String, Object> configData);

    /**
     * 查询成本核算进度
     */
    MyJsonBean<Map<String, Object>> getCostingProgress(String costingPeriod);

    // ========== 成本分析 ==========

    /**
     * 成本构成分析
     */
    MyJsonBean<List<Map<String, Object>>> getCostCompositionAnalysis(ProductCostQueryParam param);

    /**
     * 成本趋势分析
     */
    MyJsonBean<List<Map<String, Object>>> getCostTrendAnalysis(ProductCostQueryParam param);

    /**
     * 成本对比分析
     */
    MyJsonBean<List<Map<String, Object>>> getCostComparisonAnalysis(ProductCostQueryParam param);

    /**
     * 成本差异分析
     */
    MyJsonBean<List<Map<String, Object>>> getCostVarianceAnalysis(ProductCostQueryParam param);

    /**
     * 产品成本排名
     */
    MyJsonBean<List<Map<String, Object>>> getProductCostRanking(ProductCostQueryParam param);

    /**
     * 成本中心成本汇总
     */
    MyJsonBean<List<Map<String, Object>>> getCostCenterSummary(ProductCostQueryParam param);

    /**
     * 成本分析报告
     */
    MyJsonBean<Map<String, Object>> generateCostAnalysisReport(ProductCostQueryParam param);

    // ========== 成本控制 ==========

    /**
     * 查询预算执行情况
     */
    MyJsonBean<List<Map<String, Object>>> getBudgetExecution(ProductCostQueryParam param);

    /**
     * 查询成本预警信息
     */
    MyJsonBean<Map<String, Object>> getCostAlerts(ProductCostQueryParam param);

    /**
     * 查询成本异常数据
     */
    MyJsonBean<List<Map<String, Object>>> getCostAnomalies(ProductCostQueryParam param);

    /**
     * 设置成本预警规则
     */
    MyJsonBean<String> setCostAlertRules(Map<String, Object> ruleData);

    /**
     * 执行成本控制措施
     */
    MyJsonBean<String> executeCostControlMeasures(Map<String, Object> measureData);

    /**
     * 查询成本控制效果
     */
    MyJsonBean<Map<String, Object>> getCostControlEffectiveness(ProductCostQueryParam param);

    // ========== BOM管理 ==========

    /**
     * 查询BOM列表
     */
    MyJsonBean<PageResult> getBomList(ProductCostQueryParam param);

    /**
     * 新增BOM
     */
    MyJsonBean<String> createBom(Map<String, Object> bomData);

    /**
     * 修改BOM
     */
    MyJsonBean<String> updateBom(Map<String, Object> bomData);

    /**
     * 删除BOM
     */
    MyJsonBean<String> deleteBom(Long bomId);

    /**
     * 查询BOM明细
     */
    MyJsonBean<List<Map<String, Object>>> getBomDetails(Long bomId);

    /**
     * 更新BOM明细
     */
    MyJsonBean<String> updateBomDetails(Map<String, Object> bomDetailData);

    /**
     * BOM版本管理
     */
    MyJsonBean<List<Map<String, Object>>> getBomVersions(Long productId);

    /**
     * 激活BOM版本
     */
    MyJsonBean<String> activateBomVersion(Long bomId, String version);

    /**
     * BOM成本计算
     */
    MyJsonBean<Map<String, Object>> calculateBomCost(Long bomId);

    // ========== 成本报告 ==========

    /**
     * 查询成本报告列表
     */
    MyJsonBean<PageResult> getCostReportList(ProductCostQueryParam param);

    /**
     * 生成成本报告
     */
    MyJsonBean<String> generateCostReport(Map<String, Object> reportConfig);

    /**
     * 下载成本报告
     */
    MyJsonBean<Map<String, Object>> downloadCostReport(Long reportId);

    /**
     * 删除成本报告
     */
    MyJsonBean<String> deleteCostReport(Long reportId);

    /**
     * 查询报告模板
     */
    MyJsonBean<List<Map<String, Object>>> getReportTemplates();

    /**
     * 保存报告模板
     */
    MyJsonBean<String> saveReportTemplate(Map<String, Object> templateData);

    // ========== 数据导入导出 ==========

    /**
     * 导出产品成本数据
     */
    MyJsonBean<Map<String, Object>> exportProductCostData(ProductCostQueryParam param);

    /**
     * 导入产品成本数据
     */
    MyJsonBean<String> importProductCostData(List<Map<String, Object>> dataList);

    /**
     * 下载导入模板
     */
    MyJsonBean<Map<String, Object>> downloadImportTemplate(String templateType);

    /**
     * 验证导入数据
     */
    MyJsonBean<List<Map<String, Object>>> validateImportData(List<Map<String, Object>> dataList);

    // ========== 审核流程 ==========

    /**
     * 提交审核
     */
    MyJsonBean<String> submitForAudit(List<Long> costingIds);

    /**
     * 审核通过
     */
    MyJsonBean<String> approveAudit(List<Long> costingIds, String auditOpinion);

    /**
     * 审核驳回
     */
    MyJsonBean<String> rejectAudit(List<Long> costingIds, String auditOpinion);

    /**
     * 查询待审核列表
     */
    MyJsonBean<PageResult> getPendingAuditList(ProductCostQueryParam param);

    /**
     * 查询审核历史
     */
    MyJsonBean<List<Map<String, Object>>> getAuditHistory(Long costingId);

    // ========== 凭证生成 ==========

    /**
     * 生成成本凭证
     */
    MyJsonBean<String> generateCostVoucher(List<Long> costingIds);

    /**
     * 查询凭证生成状态
     */
    MyJsonBean<List<Map<String, Object>>> getVoucherGenerationStatus(ProductCostQueryParam param);

    /**
     * 重新生成凭证
     */
    MyJsonBean<String> regenerateVoucher(Long costingId);

    // ========== 成本分摊 ==========

    /**
     * 执行成本分摊
     */
    MyJsonBean<String> executeCostAllocation(ProductCostQueryParam param);

    /**
     * 查询分摊规则
     */
    MyJsonBean<List<Map<String, Object>>> getAllocationRules();

    /**
     * 设置分摊规则
     */
    MyJsonBean<String> setAllocationRules(Map<String, Object> ruleData);

    /**
     * 查询分摊结果
     */
    MyJsonBean<List<Map<String, Object>>> getAllocationResults(ProductCostQueryParam param);

    // ========== 成本归集 ==========

    /**
     * 执行成本归集
     */
    MyJsonBean<String> executeCostCollection(ProductCostQueryParam param);

    /**
     * 查询归集规则
     */
    MyJsonBean<List<Map<String, Object>>> getCollectionRules();

    /**
     * 设置归集规则
     */
    MyJsonBean<String> setCollectionRules(Map<String, Object> ruleData);

    /**
     * 查询归集结果
     */
    MyJsonBean<List<Map<String, Object>>> getCollectionResults(ProductCostQueryParam param);

    // ========== 统计分析 ==========

    /**
     * 成本统计概览
     */
    MyJsonBean<Map<String, Object>> getCostStatisticsOverview(ProductCostQueryParam param);

    /**
     * 成本分布统计
     */
    MyJsonBean<List<Map<String, Object>>> getCostDistributionStatistics(ProductCostQueryParam param);

    /**
     * 成本变化趋势
     */
    MyJsonBean<List<Map<String, Object>>> getCostChangeTrend(ProductCostQueryParam param);

    /**
     * 成本效率分析
     */
    MyJsonBean<List<Map<String, Object>>> getCostEfficiencyAnalysis(ProductCostQueryParam param);

    /**
     * 成本质量分析
     */
    MyJsonBean<List<Map<String, Object>>> getCostQualityAnalysis(ProductCostQueryParam param);

    // ========== 系统配置 ==========

    /**
     * 查询系统配置
     */
    MyJsonBean<Map<String, Object>> getSystemConfig();

    /**
     * 更新系统配置
     */
    MyJsonBean<String> updateSystemConfig(Map<String, Object> configData);

    /**
     * 查询成本科目配置
     */
    MyJsonBean<List<Map<String, Object>>> getCostSubjectConfig();

    /**
     * 更新成本科目配置
     */
    MyJsonBean<String> updateCostSubjectConfig(Map<String, Object> subjectData);

    // ========== 数据维护 ==========

    /**
     * 数据完整性检查
     */
    MyJsonBean<List<Map<String, Object>>> checkDataIntegrity(ProductCostQueryParam param);

    /**
     * 数据一致性检查
     */
    MyJsonBean<List<Map<String, Object>>> checkDataConsistency(ProductCostQueryParam param);

    /**
     * 修复数据不一致
     */
    MyJsonBean<String> fixDataInconsistency(ProductCostQueryParam param);

    /**
     * 清理历史数据
     */
    MyJsonBean<String> cleanHistoricalData(String beforeDate);

    /**
     * 归档历史数据
     */
    MyJsonBean<String> archiveHistoricalData(ProductCostQueryParam param);

    // ========== 性能优化 ==========

    /**
     * 重建数据索引
     */
    MyJsonBean<String> rebuildDataIndex();

    /**
     * 更新统计信息
     */
    MyJsonBean<String> updateStatistics();

    /**
     * 优化数据存储
     */
    MyJsonBean<String> optimizeDataStorage();

    /**
     * 查询系统性能指标
     */
    MyJsonBean<Map<String, Object>> getSystemPerformanceMetrics();
}
