package com.financial.sharing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.financial.sharing.vo.param.CostEstimateQueryParam;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 成本估算服务接口
 * 
 * @author system
 * @since 2024-01-01
 */
public interface CostEstimateService {

    /**
     * 分页查询成本估算列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<Map<String, Object>>> getCostEstimateList(CostEstimateQueryParam param);

    /**
     * 根据ID查询成本估算详情
     * 
     * @param estimateId 估算ID
     * @return 成本估算详情
     */
    MyJsonBean<Map<String, Object>> getCostEstimateById(Long estimateId);

    /**
     * 根据估算编号查询成本估算
     * 
     * @param estimateNo 估算编号
     * @param tenantId 租户ID
     * @return 成本估算详情
     */
    MyJsonBean<Map<String, Object>> getCostEstimateByNo(String estimateNo, Long tenantId);

    /**
     * 新增成本估算
     * 
     * @param param 成本估算参数
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> createCostEstimate(Map<String, Object> param);

    /**
     * 更新成本估算
     * 
     * @param estimateId 估算ID
     * @param param 成本估算参数
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> updateCostEstimate(Long estimateId, Map<String, Object> param);

    /**
     * 删除成本估算
     * 
     * @param estimateId 估算ID
     * @return 操作结果
     */
    MyJsonBean<String> deleteCostEstimate(Long estimateId);

    /**
     * 批量删除成本估算
     * 
     * @param estimateIds 估算ID列表
     * @return 操作结果
     */
    MyJsonBean<String> batchDeleteCostEstimate(List<Long> estimateIds);

    /**
     * 复制成本估算
     * 
     * @param sourceId 源估算ID
     * @param param 复制参数
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> copyCostEstimate(Long sourceId, Map<String, Object> param);

    /**
     * 提交成本估算审批
     * 
     * @param estimateId 估算ID
     * @param param 审批参数
     * @return 操作结果
     */
    MyJsonBean<String> submitCostEstimateApproval(Long estimateId, Map<String, Object> param);

    /**
     * 审批成本估算
     * 
     * @param estimateId 估算ID
     * @param param 审批参数
     * @return 操作结果
     */
    MyJsonBean<String> approveCostEstimate(Long estimateId, Map<String, Object> param);

    /**
     * 查询产品的最新成本估算
     * 
     * @param productId 产品ID
     * @param tenantId 租户ID
     * @return 成本估算详情
     */
    MyJsonBean<Map<String, Object>> getLatestCostEstimateByProduct(Long productId, Long tenantId);

    /**
     * 查询产品在指定期间的成本估算
     * 
     * @param productId 产品ID
     * @param estimatePeriod 估算期间
     * @param tenantId 租户ID
     * @return 成本估算列表
     */
    MyJsonBean<List<Map<String, Object>>> getCostEstimateByProductAndPeriod(Long productId, String estimatePeriod, Long tenantId);

    /**
     * 查询成本估算统计信息
     * 
     * @param param 查询参数
     * @return 统计信息
     */
    MyJsonBean<Map<String, Object>> getCostEstimateStatistics(CostEstimateQueryParam param);

    /**
     * 查询成本估算趋势数据
     * 
     * @param productId 产品ID
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param tenantId 租户ID
     * @return 趋势数据
     */
    MyJsonBean<List<Map<String, Object>>> getCostEstimateTrend(Long productId, String startPeriod, String endPeriod, Long tenantId);

    /**
     * 查询成本估算差异分析数据
     * 
     * @param param 查询参数
     * @return 差异分析数据
     */
    MyJsonBean<List<Map<String, Object>>> getCostEstimateVarianceAnalysis(CostEstimateQueryParam param);

    /**
     * 查询成本估算汇总数据
     * 
     * @param param 查询参数
     * @return 汇总数据
     */
    MyJsonBean<List<Map<String, Object>>> getCostEstimateSummary(CostEstimateQueryParam param);

    /**
     * 批量导入成本估算数据
     * 
     * @param estimateList 成本估算列表
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> batchImportCostEstimate(List<Map<String, Object>> estimateList);

    /**
     * 导出成本估算数据
     * 
     * @param param 查询参数
     * @return 导出数据
     */
    MyJsonBean<List<Map<String, Object>>> exportCostEstimateData(CostEstimateQueryParam param);

    /**
     * 成本估算模拟计算
     * 
     * @param param 模拟参数
     * @return 模拟结果
     */
    MyJsonBean<Map<String, Object>> simulateCostEstimate(Map<String, Object> param);

    /**
     * 查询成本估算预算对比数据
     * 
     * @param param 查询参数
     * @return 预算对比数据
     */
    MyJsonBean<List<Map<String, Object>>> getCostEstimateBudgetComparison(CostEstimateQueryParam param);

    /**
     * 查询成本估算准确率统计
     * 
     * @param param 查询参数
     * @return 准确率统计
     */
    MyJsonBean<Map<String, Object>> getCostEstimateAccuracyStatistics(CostEstimateQueryParam param);

    /**
     * 查询成本估算成本构成分析
     * 
     * @param estimateId 估算ID
     * @return 成本构成数据
     */
    MyJsonBean<List<Map<String, Object>>> getCostEstimateComposition(Long estimateId);

    /**
     * 查询成本估算风险评估数据
     * 
     * @param param 查询参数
     * @return 风险评估数据
     */
    MyJsonBean<List<Map<String, Object>>> getCostEstimateRiskAssessment(CostEstimateQueryParam param);

    /**
     * 更新成本估算计算结果
     * 
     * @param estimateId 估算ID
     * @param param 计算结果参数
     * @return 操作结果
     */
    MyJsonBean<String> updateCostEstimateCalculationResult(Long estimateId, Map<String, Object> param);

    /**
     * 查询成本估算版本历史
     * 
     * @param baselineId 基准版本ID
     * @param tenantId 租户ID
     * @return 版本历史
     */
    MyJsonBean<List<Map<String, Object>>> getCostEstimateVersionHistory(Long baselineId, Long tenantId);

    /**
     * 查询待审批的成本估算
     * 
     * @param tenantId 租户ID
     * @param approver 审批人
     * @return 待审批列表
     */
    MyJsonBean<List<Map<String, Object>>> getPendingApprovalCostEstimate(Long tenantId, Long approver);

    /**
     * 生成成本估算编号
     * 
     * @param prefix 前缀
     * @param tenantId 租户ID
     * @return 新编号
     */
    MyJsonBean<String> generateCostEstimateNo(String prefix, Long tenantId);

    /**
     * 检查成本估算编号是否存在
     * 
     * @param estimateNo 估算编号
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    MyJsonBean<Boolean> checkCostEstimateNoExists(String estimateNo, Long tenantId, Long excludeId);

    /**
     * 批量更新成本估算状态
     * 
     * @param estimateIds 估算ID列表
     * @param status 状态
     * @return 操作结果
     */
    MyJsonBean<String> batchUpdateCostEstimateStatus(List<Long> estimateIds, Integer status);

    /**
     * 成本估算方案管理 - 获取方案列表
     * 
     * @param param 查询参数
     * @return 方案列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getCostEstimateSchemeList(CostEstimateQueryParam param);

    /**
     * 成本估算方案管理 - 创建方案
     * 
     * @param param 方案参数
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> createCostEstimateScheme(Map<String, Object> param);

    /**
     * 成本估算方案管理 - 更新方案
     * 
     * @param schemeId 方案ID
     * @param param 方案参数
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> updateCostEstimateScheme(Long schemeId, Map<String, Object> param);

    /**
     * 成本估算方案管理 - 删除方案
     * 
     * @param schemeId 方案ID
     * @return 操作结果
     */
    MyJsonBean<String> deleteCostEstimateScheme(Long schemeId);

    /**
     * 成本模拟 - 执行模拟计算
     * 
     * @param param 模拟参数
     * @return 模拟结果
     */
    MyJsonBean<Map<String, Object>> executeCostSimulation(Map<String, Object> param);

    /**
     * 差异分析 - 执行差异分析
     * 
     * @param param 分析参数
     * @return 分析结果
     */
    MyJsonBean<Map<String, Object>> executeVarianceAnalysis(Map<String, Object> param);

    /**
     * 预算编制 - 获取预算列表
     * 
     * @param param 查询参数
     * @return 预算列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getBudgetPreparationList(CostEstimateQueryParam param);

    /**
     * 预算编制 - 创建预算
     * 
     * @param param 预算参数
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> createBudgetPreparation(Map<String, Object> param);

    /**
     * 成本模型 - 获取模型列表
     * 
     * @param param 查询参数
     * @return 模型列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getCostModelList(CostEstimateQueryParam param);

    /**
     * 成本模型 - 创建模型
     * 
     * @param param 模型参数
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> createCostModel(Map<String, Object> param);

    /**
     * 估算报告 - 获取报告列表
     * 
     * @param param 查询参数
     * @return 报告列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getEstimationReportList(CostEstimateQueryParam param);

    /**
     * 估算报告 - 生成报告
     * 
     * @param param 报告参数
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> generateEstimationReport(Map<String, Object> param);
}
