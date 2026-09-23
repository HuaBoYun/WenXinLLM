package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.dto.ReconciliationAnalysisQueryParam;
import com.financial.sharing.consolidationReport.entity.TblReconciliationAnalysis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对账差异分析Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReconciliationAnalysisMapper extends BaseMapper<TblReconciliationAnalysis> {

    /**
     * 查询差异分析列表
     * 
     * @param param 查询参数
     * @return 差异分析列表
     */
    List<TblReconciliationAnalysis> selectAnalysisList(@Param("param") ReconciliationAnalysisQueryParam param);

    /**
     * 根据对账数据ID查询差异分析
     * 
     * @param reconciliationId 对账数据ID
     * @return 差异分析
     */
    TblReconciliationAnalysis selectByReconciliationId(@Param("reconciliationId") String reconciliationId);

    /**
     * 根据模型ID和期间统计差异分析
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 统计结果Map(status -> count)
     */
    List<java.util.Map<String, Object>> countByStatus(@Param("modelId") String modelId, @Param("period") String period);

    /**
     * 根据模型ID和期间统计差异原因
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 统计结果Map(diffReason -> count)
     */
    List<java.util.Map<String, Object>> countByReason(@Param("modelId") String modelId, @Param("period") String period);
}

