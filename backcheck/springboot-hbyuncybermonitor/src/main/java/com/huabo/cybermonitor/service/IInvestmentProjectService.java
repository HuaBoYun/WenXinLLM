package com.huabo.cybermonitor.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.InvestmentProject;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.InvestmentProjectQueryVO;

/**
 * 投资项目信息服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IInvestmentProjectService extends IService<InvestmentProject> {

    /**
     * 分页查询投资项目列表
     *
     * @param queryVO 查询参数
     * @return 分页结果
     */
    PageResult<InvestmentProject> getInvestmentProjectList(InvestmentProjectQueryVO queryVO);

    /**
     * 根据ID获取投资项目详情
     *
     * @param projectId 投资项目ID
     * @return 投资项目详情
     */
    InvestmentProject getInvestmentProjectById(String projectId);

    /**
     * 新增投资项目
     *
     * @param investmentProject 投资项目信息
     * @return 是否成功
     */
    boolean addInvestmentProject(InvestmentProject investmentProject);

    /**
     * 更新投资项目
     *
     * @param investmentProject 投资项目信息
     * @return 是否成功
     */
    boolean updateInvestmentProject(InvestmentProject investmentProject);

    /**
     * 删除投资项目
     *
     * @param projectId 投资项目ID
     * @return 是否成功
     */
    boolean deleteInvestmentProject(String projectId);

    /**
     * 批量删除投资项目
     *
     * @param projectIds 投资项目ID列表
     * @return 是否成功
     */
    boolean batchDeleteInvestmentProject(List<String> projectIds);

    /**
     * 根据投资方企业ID查询项目
     *
     * @param investorEnterpriseId 投资方企业ID
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByInvestorEnterpriseId(String investorEnterpriseId);

    /**
     * 根据被投资方企业ID查询项目
     *
     * @param investeeEnterpriseId 被投资方企业ID
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByInvesteeEnterpriseId(String investeeEnterpriseId);

    /**
     * 按投资类型查询项目
     *
     * @param investmentType 投资类型
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByInvestmentType(String investmentType);

    /**
     * 按投资行业查询项目
     *
     * @param investmentIndustry 投资行业
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByInvestmentIndustry(String investmentIndustry);

    /**
     * 按投资地区查询项目
     *
     * @param investmentRegion 投资地区
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByInvestmentRegion(String investmentRegion);

    /**
     * 按项目状态查询项目
     *
     * @param projectStatus 项目状态
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByProjectStatus(String projectStatus);

    /**
     * 按投资阶段查询项目
     *
     * @param investmentStage 投资阶段
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByInvestmentStage(String investmentStage);

    /**
     * 按风险等级查询项目
     *
     * @param riskLevel 风险等级
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByRiskLevel(String riskLevel);

    /**
     * 按审批状态查询项目
     *
     * @param approvalStatus 审批状态
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByApprovalStatus(String approvalStatus);

    /**
     * 查询战略投资项目
     *
     * @param isStrategicInvestment 是否战略投资
     * @return 投资项目列表
     */
    List<InvestmentProject> getStrategicInvestmentProjects(Boolean isStrategicInvestment);

    /**
     * 查询关联交易项目
     *
     * @param isRelatedTransaction 是否关联交易
     * @return 投资项目列表
     */
    List<InvestmentProject> getRelatedTransactionProjects(Boolean isRelatedTransaction);

    /**
     * 查询需要监管关注的项目
     *
     * @param needRegulatoryAttention 是否需要监管关注
     * @return 投资项目列表
     */
    List<InvestmentProject> getRegulatoryAttentionProjects(Boolean needRegulatoryAttention);

    /**
     * 查询项目负责人的项目
     *
     * @param projectManager 项目负责人
     * @return 投资项目列表
     */
    List<InvestmentProject> getInvestmentProjectByProjectManager(String projectManager);

    /**
     * 按投资类型统计项目
     *
     * @return 类型统计
     */
    List<Map<String, Object>> getInvestmentTypeStatistics();

    /**
     * 按投资行业统计项目
     *
     * @return 行业统计
     */
    List<Map<String, Object>> getInvestmentIndustryStatistics();

    /**
     * 按投资地区统计项目
     *
     * @return 地区统计
     */
    List<Map<String, Object>> getInvestmentRegionStatistics();

    /**
     * 按项目状态统计项目
     *
     * @return 状态统计
     */
    List<Map<String, Object>> getProjectStatusStatistics();

    /**
     * 按投资阶段统计项目
     *
     * @return 阶段统计
     */
    List<Map<String, Object>> getInvestmentStageStatistics();

    /**
     * 按风险等级统计项目
     *
     * @return 风险等级统计
     */
    List<Map<String, Object>> getRiskLevelStatistics();

    /**
     * 按审批状态统计项目
     *
     * @return 审批状态统计
     */
    List<Map<String, Object>> getApprovalStatusStatistics();

    /**
     * 查询投资趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 投资趋势数据
     */
    List<Map<String, Object>> getInvestmentTrend(String startDate, String endDate);

    /**
     * 查询投资金额趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 投资金额趋势数据
     */
    List<Map<String, Object>> getInvestmentAmountTrend(String startDate, String endDate);

    /**
     * 查询投资收益分析
     *
     * @return 投资收益分析数据
     */
    List<Map<String, Object>> getInvestmentReturnAnalysis();

    /**
     * 查询投资风险分析
     *
     * @return 投资风险分析数据
     */
    List<Map<String, Object>> getInvestmentRiskAnalysis();

    /**
     * 查询投资效果评估
     *
     * @param investorEnterpriseId 投资方企业ID
     * @return 投资效果评估数据
     */
    List<Map<String, Object>> getInvestmentEffectEvaluation(String investorEnterpriseId);

    /**
     * 批量更新项目状态
     *
     * @param projectIds 项目ID列表
     * @param projectStatus 项目状态
     * @return 是否成功
     */
    boolean batchUpdateProjectStatus(List<String> projectIds, String projectStatus);

    /**
     * 批量更新审批状态
     *
     * @param projectIds 项目ID列表
     * @param approvalStatus 审批状态
     * @return 是否成功
     */
    boolean batchUpdateApprovalStatus(List<String> projectIds, String approvalStatus);

    /**
     * 批量更新风险等级
     *
     * @param projectIds 项目ID列表
     * @param riskLevel 风险等级
     * @return 是否成功
     */
    boolean batchUpdateRiskLevel(List<String> projectIds, String riskLevel);

    /**
     * 删除过期项目记录
     *
     * @param days 过期天数
     * @return 删除数量
     */
    int deleteExpiredProjectRecords(Integer days);

    /**
     * 获取投资项目统计概览
     *
     * @return 统计概览
     */
    Map<String, Object> getInvestmentStatisticsOverview();

    /**
     * 导出投资项目列表
     *
     * @param queryVO 查询参数
     * @return 导出数据列表
     */
    List<Map<String, Object>> exportInvestmentProjectList(InvestmentProjectQueryVO queryVO);

    /**
     * 投资决策风险评估
     *
     * @param investmentProject 投资项目
     * @return 风险评估结果
     */
    Map<String, Object> assessInvestmentDecisionRisk(InvestmentProject investmentProject);

    /**
     * 投资项目合规性检查
     *
     * @param investmentProject 投资项目
     * @return 合规性检查结果
     */
    Map<String, Object> checkInvestmentProjectCompliance(InvestmentProject investmentProject);

    /**
     * 投资效果跟踪分析
     *
     * @param projectId 项目ID
     * @return 效果跟踪分析结果
     */
    Map<String, Object> trackInvestmentEffect(String projectId);

    /**
     * 获取投资类型标签转换
     *
     * @param investmentType 投资类型
     * @return 类型标签
     */
    String getInvestmentTypeLabel(String investmentType);

    /**
     * 获取投资方式标签转换
     *
     * @param investmentMethod 投资方式
     * @return 方式标签
     */
    String getInvestmentMethodLabel(String investmentMethod);

    /**
     * 获取项目状态标签转换
     *
     * @param projectStatus 项目状态
     * @return 状态标签
     */
    String getProjectStatusLabel(String projectStatus);

    /**
     * 获取投资阶段标签转换
     *
     * @param investmentStage 投资阶段
     * @return 阶段标签
     */
    String getInvestmentStageLabel(String investmentStage);

    /**
     * 获取风险等级标签转换
     *
     * @param riskLevel 风险等级
     * @return 风险等级标签
     */
    String getRiskLevelLabel(String riskLevel);

    /**
     * 获取决策流程状态标签转换
     *
     * @param decisionProcessStatus 决策流程状态
     * @return 状态标签
     */
    String getDecisionProcessStatusLabel(String decisionProcessStatus);

    /**
     * 获取审批状态标签转换
     *
     * @param approvalStatus 审批状态
     * @return 状态标签
     */
    String getApprovalStatusLabel(String approvalStatus);
}
