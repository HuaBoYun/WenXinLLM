package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.FinancialStatement;
import com.huabo.cybermonitor.vo.FinancialStatementQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 财务报表数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface FinancialStatementMapper extends BaseMapper<FinancialStatement> {

    // ==================== 基础查询方法 ====================

    /**
     * 分页查询财务报表列表
     */
    Page<FinancialStatement> selectFinancialStatementList(Page<FinancialStatement> page, @Param("queryVO") FinancialStatementQueryVO queryVO);

    /**
     * 根据企业ID查询财务报表
     */
    List<FinancialStatement> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据报表类型查询
     */
    List<FinancialStatement> selectByStatementType(@Param("statementType") String statementType);

    /**
     * 根据报表期间类型查询
     */
    List<FinancialStatement> selectByPeriodType(@Param("periodType") String periodType);

    /**
     * 根据报表年度查询
     */
    List<FinancialStatement> selectByReportYear(@Param("reportYear") Integer reportYear);

    /**
     * 根据报表状态查询
     */
    List<FinancialStatement> selectByStatementStatus(@Param("statementStatus") String statementStatus);

    /**
     * 根据审计状态查询
     */
    List<FinancialStatement> selectByAuditStatus(@Param("auditStatus") String auditStatus);

    /**
     * 根据审计机构查询
     */
    List<FinancialStatement> selectByAuditFirm(@Param("auditFirm") String auditFirm);

    /**
     * 根据审计意见查询
     */
    List<FinancialStatement> selectByAuditOpinion(@Param("auditOpinion") String auditOpinion);

    /**
     * 根据报送状态查询
     */
    List<FinancialStatement> selectBySubmissionStatus(@Param("submissionStatus") String submissionStatus);

    /**
     * 根据数据质量等级查询
     */
    List<FinancialStatement> selectByDataQualityLevel(@Param("dataQualityLevel") String dataQualityLevel);

    /**
     * 根据风险等级查询
     */
    List<FinancialStatement> selectByRiskLevel(@Param("riskLevel") String riskLevel);

    /**
     * 查询需要监管关注的报表
     */
    List<FinancialStatement> selectRegulatoryAttentionStatements(@Param("needRegulatoryAttention") Boolean needRegulatoryAttention);

    /**
     * 查询异常报表
     */
    List<FinancialStatement> selectAnomalyStatements(@Param("hasAnomalies") Boolean hasAnomalies);

    /**
     * 查询合并报表
     */
    List<FinancialStatement> selectConsolidatedStatements(@Param("isConsolidated") Boolean isConsolidated);

    // ==================== 数据质量分析方法 ====================

    /**
     * 数据质量分析
     */
    List<Map<String, Object>> selectDataQualityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                        @Param("startYear") Integer startYear, 
                                                        @Param("endYear") Integer endYear);

    /**
     * 数据完整性分析
     */
    List<Map<String, Object>> selectDataCompletenessAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 数据准确性分析
     */
    List<Map<String, Object>> selectDataAccuracyAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 数据及时性分析
     */
    List<Map<String, Object>> selectDataTimelinessAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startYear") Integer startYear, 
                                                           @Param("endYear") Integer endYear);

    /**
     * 数据一致性分析
     */
    List<Map<String, Object>> selectDataConsistencyAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    // ==================== 财务数据异常检测方法 ====================

    /**
     * 财务数据异常检测
     */
    List<Map<String, Object>> selectFinancialDataAnomalyDetection(@Param("enterpriseId") String enterpriseId, 
                                                                  @Param("startYear") Integer startYear, 
                                                                  @Param("endYear") Integer endYear);

    /**
     * 趋势异常识别
     */
    List<Map<String, Object>> selectTrendAnomalyIdentification(@Param("enterpriseId") String enterpriseId, 
                                                               @Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    /**
     * 数据波动分析
     */
    List<Map<String, Object>> selectDataVolatilityAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startYear") Integer startYear, 
                                                           @Param("endYear") Integer endYear);

    /**
     * 异常值检测
     */
    List<Map<String, Object>> selectOutlierDetection(@Param("enterpriseId") String enterpriseId, 
                                                     @Param("startYear") Integer startYear, 
                                                     @Param("endYear") Integer endYear);

    // ==================== 财务风险识别方法 ====================

    /**
     * 财务风险预警分析
     */
    List<Map<String, Object>> selectFinancialRiskWarningAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startYear") Integer startYear, 
                                                                 @Param("endYear") Integer endYear);

    /**
     * 流动性风险分析
     */
    List<Map<String, Object>> selectLiquidityRiskAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 信用风险分析
     */
    List<Map<String, Object>> selectCreditRiskAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                       @Param("startYear") Integer startYear, 
                                                       @Param("endYear") Integer endYear);

    /**
     * 市场风险分析
     */
    List<Map<String, Object>> selectMarketRiskAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                       @Param("startYear") Integer startYear, 
                                                       @Param("endYear") Integer endYear);

    /**
     * 财务风险等级评估
     */
    List<Map<String, Object>> selectFinancialRiskLevelAssessment(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startYear") Integer startYear, 
                                                                 @Param("endYear") Integer endYear);

    /**
     * 财务风险传导分析
     */
    List<Map<String, Object>> selectFinancialRiskTransmissionAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                      @Param("startYear") Integer startYear, 
                                                                      @Param("endYear") Integer endYear);

    // ==================== 合规监管方法 ====================

    /**
     * 财务合规检查
     */
    List<Map<String, Object>> selectFinancialComplianceCheck(@Param("enterpriseId") String enterpriseId, 
                                                             @Param("startYear") Integer startYear, 
                                                             @Param("endYear") Integer endYear);

    /**
     * 会计准则合规性检查
     */
    List<Map<String, Object>> selectAccountingStandardsComplianceCheck(@Param("enterpriseId") String enterpriseId, 
                                                                       @Param("startYear") Integer startYear, 
                                                                       @Param("endYear") Integer endYear);

    /**
     * 审计合规性检查
     */
    List<Map<String, Object>> selectAuditComplianceCheck(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 报送合规性检查
     */
    List<Map<String, Object>> selectSubmissionComplianceCheck(@Param("enterpriseId") String enterpriseId, 
                                                              @Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    // ==================== 统计分析方法 ====================

    /**
     * 按报表类型统计
     */
    List<Map<String, Object>> selectStatementTypeStatistics(@Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 按期间类型统计
     */
    List<Map<String, Object>> selectPeriodTypeStatistics(@Param("startYear") Integer startYear, 
                                                         @Param("endYear") Integer endYear);

    /**
     * 按报表状态统计
     */
    List<Map<String, Object>> selectStatementStatusStatistics(@Param("startYear") Integer startYear, 
                                                              @Param("endYear") Integer endYear);

    /**
     * 按审计状态统计
     */
    List<Map<String, Object>> selectAuditStatusStatistics(@Param("startYear") Integer startYear, 
                                                          @Param("endYear") Integer endYear);

    /**
     * 按数据质量等级统计
     */
    List<Map<String, Object>> selectDataQualityLevelStatistics(@Param("startYear") Integer startYear, 
                                                               @Param("endYear") Integer endYear);

    /**
     * 按风险等级统计
     */
    List<Map<String, Object>> selectRiskLevelStatistics(@Param("startYear") Integer startYear, 
                                                        @Param("endYear") Integer endYear);

    /**
     * 报表提交趋势分析
     */
    List<Map<String, Object>> selectSubmissionTrendAnalysis(@Param("startYear") Integer startYear, 
                                                            @Param("endYear") Integer endYear);

    /**
     * 审计机构分布统计
     */
    List<Map<String, Object>> selectAuditFirmDistributionStatistics(@Param("startYear") Integer startYear, 
                                                                    @Param("endYear") Integer endYear);

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新报表状态
     */
    int batchUpdateStatementStatus(@Param("statementIds") List<String> statementIds, 
                                   @Param("statementStatus") String statementStatus);

    /**
     * 批量更新审计状态
     */
    int batchUpdateAuditStatus(@Param("statementIds") List<String> statementIds, 
                               @Param("auditStatus") String auditStatus);

    /**
     * 批量更新数据质量等级
     */
    int batchUpdateDataQualityLevel(@Param("statementIds") List<String> statementIds, 
                                    @Param("dataQualityLevel") String dataQualityLevel);

    /**
     * 批量更新风险等级
     */
    int batchUpdateRiskLevel(@Param("statementIds") List<String> statementIds, 
                             @Param("riskLevel") String riskLevel);

    /**
     * 批量更新监管关注标识
     */
    int batchUpdateRegulatoryAttention(@Param("statementIds") List<String> statementIds, 
                                       @Param("needRegulatoryAttention") Boolean needRegulatoryAttention);

    // ==================== 数据维护方法 ====================

    /**
     * 删除过期报表记录
     */
    int deleteExpiredStatementRecords(@Param("days") Integer days);

    /**
     * 获取财务报表统计概览
     */
    Map<String, Object> selectFinancialStatementStatisticsOverview();

    /**
     * 导出财务报表列表
     */
    List<Map<String, Object>> exportFinancialStatementList(FinancialStatementQueryVO queryVO);

}
