package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.FinancialStatementProcess;
import com.huabo.cybermonitor.vo.FinancialStatementProcessQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 财务报表编制流程服务接口
 *
 * @author huabo
 * @since 2024-12-12
 */
public interface IFinancialStatementProcessService extends IService<FinancialStatementProcess> {

    /**
     * 分页查询财务报表编制流程
     *
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<FinancialStatementProcess> getFinancialStatementProcessPage(FinancialStatementProcessQueryVo queryVo);

    /**
     * 新增财务报表编制流程
     *
     * @param financialStatementProcess 财务报表编制流程
     * @return 是否成功
     */
    boolean addFinancialStatementProcess(FinancialStatementProcess financialStatementProcess);

    /**
     * 修改财务报表编制流程
     *
     * @param financialStatementProcess 财务报表编制流程
     * @return 是否成功
     */
    boolean updateFinancialStatementProcess(FinancialStatementProcess financialStatementProcess);

    /**
     * 删除财务报表编制流程
     *
     * @param processId 流程ID
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean deleteFinancialStatementProcess(String processId, String updateBy);

    /**
     * 批量删除财务报表编制流程
     *
     * @param processIds 流程ID列表
     * @param updateBy   更新人
     * @return 是否成功
     */
    boolean batchDeleteFinancialStatementProcess(List<String> processIds, String updateBy);

    /**
     * 启动编制流程
     *
     * @param processId 流程ID
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean startCompilationProcess(String processId, String updateBy);

    /**
     * 暂停编制流程
     *
     * @param processId 流程ID
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean pauseCompilationProcess(String processId, String updateBy);

    /**
     * 恢复编制流程
     *
     * @param processId 流程ID
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean resumeCompilationProcess(String processId, String updateBy);

    /**
     * 完成编制流程
     *
     * @param processId 流程ID
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean completeCompilationProcess(String processId, String updateBy);

    /**
     * 取消编制流程
     *
     * @param processId 流程ID
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean cancelCompilationProcess(String processId, String updateBy);

    /**
     * 提交审核
     *
     * @param processId 流程ID
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean submitForAudit(String processId, String updateBy);

    /**
     * 初审
     *
     * @param processId    流程ID
     * @param auditor      审核人
     * @param opinion      审核意见
     * @param auditResult  审核结果
     * @param updateBy     更新人
     * @return 是否成功
     */
    boolean firstAudit(String processId, String auditor, String opinion, String auditResult, String updateBy);

    /**
     * 复审
     *
     * @param processId    流程ID
     * @param auditor      审核人
     * @param opinion      审核意见
     * @param auditResult  审核结果
     * @param updateBy     更新人
     * @return 是否成功
     */
    boolean secondAudit(String processId, String auditor, String opinion, String auditResult, String updateBy);

    /**
     * 终审
     *
     * @param processId    流程ID
     * @param auditor      审核人
     * @param opinion      审核意见
     * @param auditResult  审核结果
     * @param updateBy     更新人
     * @return 是否成功
     */
    boolean finalAudit(String processId, String auditor, String opinion, String auditResult, String updateBy);

    /**
     * 签字确认
     *
     * @param processId 流程ID
     * @param signatory 签字人
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean signatureConfirmation(String processId, String signatory, String updateBy);

    /**
     * 质量控制检查
     *
     * @param processId 流程ID
     * @param updateBy  更新人
     * @return 是否成功
     */
    boolean qualityControlCheck(String processId, String updateBy);

    /**
     * 问题整改
     *
     * @param processId          流程ID
     * @param correctiveMeasures 整改措施
     * @param updateBy           更新人
     * @return 是否成功
     */
    boolean issueCorrection(String processId, String correctiveMeasures, String updateBy);

    /**
     * 效果验证
     *
     * @param processId                   流程ID
     * @param effectivenessVerification   效果验证
     * @param updateBy                    更新人
     * @return 是否成功
     */
    boolean effectivenessVerification(String processId, String effectivenessVerification, String updateBy);

    /**
     * 批量更新编制状态
     *
     * @param processIds 流程ID列表
     * @param status     状态
     * @param updateBy   更新人
     * @return 是否成功
     */
    boolean batchUpdateCompilationStatus(List<String> processIds, String status, String updateBy);

    /**
     * 批量更新审核状态
     *
     * @param processIds 流程ID列表
     * @param status     状态
     * @param updateBy   更新人
     * @return 是否成功
     */
    boolean batchUpdateAuditStatus(List<String> processIds, String status, String updateBy);

    /**
     * 批量更新质量控制状态
     *
     * @param processIds 流程ID列表
     * @param status     状态
     * @param updateBy   更新人
     * @return 是否成功
     */
    boolean batchUpdateQualityControlStatus(List<String> processIds, String status, String updateBy);

    /**
     * 根据企业ID获取财务报表编制流程统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> getStatisticsByEnterpriseId(String enterpriseId);

    /**
     * 根据企业ID获取报表类型分布
     *
     * @param enterpriseId 企业ID
     * @return 报表类型分布
     */
    List<Map<String, Object>> getStatementTypeDistribution(String enterpriseId);

    /**
     * 根据企业ID获取编制状态分布
     *
     * @param enterpriseId 企业ID
     * @return 编制状态分布
     */
    List<Map<String, Object>> getCompilationStatusDistribution(String enterpriseId);

    /**
     * 根据企业ID获取审核状态分布
     *
     * @param enterpriseId 企业ID
     * @return 审核状态分布
     */
    List<Map<String, Object>> getAuditStatusDistribution(String enterpriseId);

    /**
     * 根据企业ID获取质量评分分布
     *
     * @param enterpriseId 企业ID
     * @return 质量评分分布
     */
    List<Map<String, Object>> getQualityScoreDistribution(String enterpriseId);

    /**
     * 根据企业ID获取编制进度趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 编制进度趋势
     */
    List<Map<String, Object>> getCompilationProgressTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取质量评分趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 质量评分趋势
     */
    List<Map<String, Object>> getQualityScoreTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取问题统计
     *
     * @param enterpriseId 企业ID
     * @return 问题统计
     */
    Map<String, Object> getIssueStatistics(String enterpriseId);

    /**
     * 根据企业ID获取问题趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 问题趋势
     */
    List<Map<String, Object>> getIssueTrend(String enterpriseId, Integer months);

    /**
     * 根据企业ID获取部门编制统计
     *
     * @param enterpriseId 企业ID
     * @return 部门编制统计
     */
    List<Map<String, Object>> getDepartmentCompilationStatistics(String enterpriseId);

    /**
     * 根据企业ID获取负责人编制统计
     *
     * @param enterpriseId 企业ID
     * @return 负责人编制统计
     */
    List<Map<String, Object>> getManagerCompilationStatistics(String enterpriseId);

    /**
     * 根据企业ID获取审核人员统计
     *
     * @param enterpriseId 企业ID
     * @return 审核人员统计
     */
    List<Map<String, Object>> getAuditorStatistics(String enterpriseId);

    /**
     * 根据企业ID获取模板版本使用统计
     *
     * @param enterpriseId 企业ID
     * @return 模板版本使用统计
     */
    List<Map<String, Object>> getTemplateVersionStatistics(String enterpriseId);

    /**
     * 根据企业ID获取数据来源统计
     *
     * @param enterpriseId 企业ID
     * @return 数据来源统计
     */
    List<Map<String, Object>> getDataSourceStatistics(String enterpriseId);

    /**
     * 根据企业ID获取编制效率分析
     *
     * @param enterpriseId 企业ID
     * @return 编制效率分析
     */
    List<Map<String, Object>> getCompilationEfficiencyAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取审核效率分析
     *
     * @param enterpriseId 企业ID
     * @return 审核效率分析
     */
    List<Map<String, Object>> getAuditEfficiencyAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取质量控制效果分析
     *
     * @param enterpriseId 企业ID
     * @return 质量控制效果分析
     */
    List<Map<String, Object>> getQualityControlEffectivenessAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取流程改进建议
     *
     * @param enterpriseId 企业ID
     * @return 流程改进建议
     */
    List<Map<String, Object>> getProcessImprovementSuggestions(String enterpriseId);

    /**
     * 根据企业ID获取最佳实践案例
     *
     * @param enterpriseId 企业ID
     * @return 最佳实践案例
     */
    List<Map<String, Object>> getBestPracticeCases(String enterpriseId);

    /**
     * 根据企业ID获取培训需求分析
     *
     * @param enterpriseId 企业ID
     * @return 培训需求分析
     */
    List<Map<String, Object>> getTrainingRequirementsAnalysis(String enterpriseId);

    /**
     * 根据企业ID获取能力提升建议
     *
     * @param enterpriseId 企业ID
     * @return 能力提升建议
     */
    List<Map<String, Object>> getCapabilityImprovementSuggestions(String enterpriseId);

    /**
     * 根据企业ID获取合规检查结果统计
     *
     * @param enterpriseId 企业ID
     * @return 合规检查结果统计
     */
    List<Map<String, Object>> getComplianceCheckResultStatistics(String enterpriseId);

    /**
     * 根据企业ID获取创新点统计
     *
     * @param enterpriseId 企业ID
     * @return 创新点统计
     */
    List<Map<String, Object>> getInnovationPointsStatistics(String enterpriseId);

    /**
     * 根据企业ID获取即将到期的编制任务
     *
     * @param enterpriseId 企业ID
     * @param days         天数
     * @return 即将到期的编制任务
     */
    List<FinancialStatementProcess> getUpcomingCompilationTasks(String enterpriseId, Integer days);

    /**
     * 根据企业ID获取逾期的编制任务
     *
     * @param enterpriseId 企业ID
     * @return 逾期的编制任务
     */
    List<FinancialStatementProcess> getOverdueCompilationTasks(String enterpriseId);

    /**
     * 根据企业ID获取待审核的编制任务
     *
     * @param enterpriseId 企业ID
     * @return 待审核的编制任务
     */
    List<FinancialStatementProcess> getPendingAuditTasks(String enterpriseId);

    /**
     * 根据企业ID获取待整改的编制任务
     *
     * @param enterpriseId 企业ID
     * @return 待整改的编制任务
     */
    List<FinancialStatementProcess> getPendingCorrectionTasks(String enterpriseId);

    /**
     * 导出财务报表编制流程
     *
     * @param queryVo 查询条件
     * @return 导出结果
     */
    Map<String, Object> exportFinancialStatementProcess(FinancialStatementProcessQueryVo queryVo);

    /**
     * 生成财务报表编制流程报告
     *
     * @param enterpriseId 企业ID
     * @param reportType   报告类型
     * @return 报告结果
     */
    Map<String, Object> generateFinancialStatementProcessReport(String enterpriseId, String reportType);
}
