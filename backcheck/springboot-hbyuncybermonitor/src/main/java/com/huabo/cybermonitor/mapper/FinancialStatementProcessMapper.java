package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.FinancialStatementProcess;
import com.huabo.cybermonitor.vo.FinancialStatementProcessQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 财务报表编制流程Mapper接口
 *
 * @author huabo
 * @since 2024-12-12
 */
@Mapper
public interface FinancialStatementProcessMapper extends BaseMapper<FinancialStatementProcess> {

    /**
     * 分页查询财务报表编制流程
     *
     * @param page    分页参数
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<FinancialStatementProcess> selectFinancialStatementProcessPage(Page<FinancialStatementProcess> page, @Param("queryVo") FinancialStatementProcessQueryVo queryVo);

    /**
     * 根据企业ID获取财务报表编制流程统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> selectStatisticsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取报表类型分布
     *
     * @param enterpriseId 企业ID
     * @return 报表类型分布
     */
    List<Map<String, Object>> selectStatementTypeDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取编制状态分布
     *
     * @param enterpriseId 企业ID
     * @return 编制状态分布
     */
    List<Map<String, Object>> selectCompilationStatusDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取审核状态分布
     *
     * @param enterpriseId 企业ID
     * @return 审核状态分布
     */
    List<Map<String, Object>> selectAuditStatusDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取质量评分分布
     *
     * @param enterpriseId 企业ID
     * @return 质量评分分布
     */
    List<Map<String, Object>> selectQualityScoreDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取编制进度趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 编制进度趋势
     */
    List<Map<String, Object>> selectCompilationProgressTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取质量评分趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 质量评分趋势
     */
    List<Map<String, Object>> selectQualityScoreTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取问题统计
     *
     * @param enterpriseId 企业ID
     * @return 问题统计
     */
    Map<String, Object> selectIssueStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取问题趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 问题趋势
     */
    List<Map<String, Object>> selectIssueTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取部门编制统计
     *
     * @param enterpriseId 企业ID
     * @return 部门编制统计
     */
    List<Map<String, Object>> selectDepartmentCompilationStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取负责人编制统计
     *
     * @param enterpriseId 企业ID
     * @return 负责人编制统计
     */
    List<Map<String, Object>> selectManagerCompilationStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取审核人员统计
     *
     * @param enterpriseId 企业ID
     * @return 审核人员统计
     */
    List<Map<String, Object>> selectAuditorStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取模板版本使用统计
     *
     * @param enterpriseId 企业ID
     * @return 模板版本使用统计
     */
    List<Map<String, Object>> selectTemplateVersionStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取数据来源统计
     *
     * @param enterpriseId 企业ID
     * @return 数据来源统计
     */
    List<Map<String, Object>> selectDataSourceStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取编制效率分析
     *
     * @param enterpriseId 企业ID
     * @return 编制效率分析
     */
    List<Map<String, Object>> selectCompilationEfficiencyAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取审核效率分析
     *
     * @param enterpriseId 企业ID
     * @return 审核效率分析
     */
    List<Map<String, Object>> selectAuditEfficiencyAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取质量控制效果分析
     *
     * @param enterpriseId 企业ID
     * @return 质量控制效果分析
     */
    List<Map<String, Object>> selectQualityControlEffectivenessAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取流程改进建议
     *
     * @param enterpriseId 企业ID
     * @return 流程改进建议
     */
    List<Map<String, Object>> selectProcessImprovementSuggestions(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取最佳实践案例
     *
     * @param enterpriseId 企业ID
     * @return 最佳实践案例
     */
    List<Map<String, Object>> selectBestPracticeCases(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取培训需求分析
     *
     * @param enterpriseId 企业ID
     * @return 培训需求分析
     */
    List<Map<String, Object>> selectTrainingRequirementsAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取能力提升建议
     *
     * @param enterpriseId 企业ID
     * @return 能力提升建议
     */
    List<Map<String, Object>> selectCapabilityImprovementSuggestions(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取合规检查结果统计
     *
     * @param enterpriseId 企业ID
     * @return 合规检查结果统计
     */
    List<Map<String, Object>> selectComplianceCheckResultStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取创新点统计
     *
     * @param enterpriseId 企业ID
     * @return 创新点统计
     */
    List<Map<String, Object>> selectInnovationPointsStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 批量更新编制状态
     *
     * @param processIds 流程ID列表
     * @param status     状态
     * @param updateBy   更新人
     * @return 更新数量
     */
    int batchUpdateCompilationStatus(@Param("processIds") List<String> processIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量更新审核状态
     *
     * @param processIds 流程ID列表
     * @param status     状态
     * @param updateBy   更新人
     * @return 更新数量
     */
    int batchUpdateAuditStatus(@Param("processIds") List<String> processIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量更新质量控制状态
     *
     * @param processIds 流程ID列表
     * @param status     状态
     * @param updateBy   更新人
     * @return 更新数量
     */
    int batchUpdateQualityControlStatus(@Param("processIds") List<String> processIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量删除财务报表编制流程
     *
     * @param processIds 流程ID列表
     * @param updateBy   更新人
     * @return 删除数量
     */
    int batchDeleteFinancialStatementProcess(@Param("processIds") List<String> processIds, @Param("updateBy") String updateBy);

    /**
     * 根据企业ID获取即将到期的编制任务
     *
     * @param enterpriseId 企业ID
     * @param days         天数
     * @return 即将到期的编制任务
     */
    List<FinancialStatementProcess> selectUpcomingCompilationTasks(@Param("enterpriseId") String enterpriseId, @Param("days") Integer days);

    /**
     * 根据企业ID获取逾期的编制任务
     *
     * @param enterpriseId 企业ID
     * @return 逾期的编制任务
     */
    List<FinancialStatementProcess> selectOverdueCompilationTasks(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取待审核的编制任务
     *
     * @param enterpriseId 企业ID
     * @return 待审核的编制任务
     */
    List<FinancialStatementProcess> selectPendingAuditTasks(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取待整改的编制任务
     *
     * @param enterpriseId 企业ID
     * @return 待整改的编制任务
     */
    List<FinancialStatementProcess> selectPendingCorrectionTasks(@Param("enterpriseId") String enterpriseId);
}
