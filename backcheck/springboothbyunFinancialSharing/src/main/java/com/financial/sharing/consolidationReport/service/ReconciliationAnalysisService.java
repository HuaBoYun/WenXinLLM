package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.dto.ReconciliationAnalysisQueryParam;
import com.financial.sharing.consolidationReport.entity.TblReconciliationAnalysis;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 对账差异分析Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReconciliationAnalysisService {

    /**
     * 查询差异分析列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblReconciliationAnalysis> getAnalysisList(ReconciliationAnalysisQueryParam param);

    /**
     * 根据ID查询差异分析
     * 
     * @param analysisId 差异分析ID
     * @return 差异分析
     */
    TblReconciliationAnalysis getAnalysisById(String analysisId);

    /**
     * 根据对账数据ID查询差异分析
     * 
     * @param reconciliationId 对账数据ID
     * @return 差异分析
     */
    TblReconciliationAnalysis getAnalysisByReconciliationId(String reconciliationId);

    /**
     * 新增差异分析
     * 
     * @param analysis 差异分析
     */
    void saveAnalysis(TblReconciliationAnalysis analysis);

    /**
     * 修改差异分析
     * 
     * @param analysis 差异分析
     */
    void updateAnalysis(TblReconciliationAnalysis analysis);

    /**
     * 删除差异分析
     * 
     * @param analysisId 差异分析ID
     */
    void deleteAnalysis(String analysisId);

    /**
     * 处理差异
     * 
     * @param analysisId 差异分析ID
     * @param solution 处理方案
     * @param adjustAmount 调整金额
     */
    void handleDifference(String analysisId, String solution, java.math.BigDecimal adjustAmount);

    /**
     * 关闭差异
     * 
     * @param analysisId 差异分析ID
     */
    void closeDifference(String analysisId);

    /**
     * 根据模型ID和期间统计差异分析
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 统计结果
     */
    Map<String, Object> getStatistics(String modelId, String period);

    /**
     * 自动创建差异分析(从对账数据)
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 创建结果
     */
    Map<String, Object> autoCreateAnalysis(String modelId, String period);
}

