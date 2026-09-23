package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.InvestmentProject;
import com.huabo.cybermonitor.vo.InvestmentProjectQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 投资项目信息数据访问接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface InvestmentProjectMapper extends BaseMapper<InvestmentProject> {

    /**
     * 分页查询投资项目列表
     *
     * @param queryVO 查询参数
     * @return 投资项目列表
     */
    List<InvestmentProject> selectInvestmentProjectList(@Param("queryVO") InvestmentProjectQueryVO queryVO);

    /**
     * 统计查询总数（配合分页，条件与 selectInvestmentProjectList 一致）
     *
     * @param queryVO 查询参数
     * @return 总数
     */
    long countInvestmentProjectList(@Param("queryVO") InvestmentProjectQueryVO queryVO);

    /**
     * 根据投资方企业ID查询项目
     *
     * @param investorEnterpriseId 投资方企业ID
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByInvestorEnterpriseId(@Param("investorEnterpriseId") String investorEnterpriseId);

    /**
     * 根据被投资方企业ID查询项目
     *
     * @param investeeEnterpriseId 被投资方企业ID
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByInvesteeEnterpriseId(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 按投资类型查询项目
     *
     * @param investmentType 投资类型
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByInvestmentType(@Param("investmentType") String investmentType);

    /**
     * 按投资行业查询项目
     *
     * @param investmentIndustry 投资行业
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByInvestmentIndustry(@Param("investmentIndustry") String investmentIndustry);

    /**
     * 按投资地区查询项目
     *
     * @param investmentRegion 投资地区
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByInvestmentRegion(@Param("investmentRegion") String investmentRegion);

    /**
     * 按项目状态查询项目
     *
     * @param projectStatus 项目状态
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByProjectStatus(@Param("projectStatus") String projectStatus);

    /**
     * 按投资阶段查询项目
     *
     * @param investmentStage 投资阶段
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByInvestmentStage(@Param("investmentStage") String investmentStage);

    /**
     * 按风险等级查询项目
     *
     * @param riskLevel 风险等级
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByRiskLevel(@Param("riskLevel") String riskLevel);

    /**
     * 按审批状态查询项目
     *
     * @param approvalStatus 审批状态
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus);

    /**
     * 查询战略投资项目
     *
     * @param isStrategicInvestment 是否战略投资
     * @return 投资项目列表
     */
    List<InvestmentProject> selectStrategicInvestmentProjects(@Param("isStrategicInvestment") Boolean isStrategicInvestment);

    /**
     * 查询关联交易项目
     *
     * @param isRelatedTransaction 是否关联交易
     * @return 投资项目列表
     */
    List<InvestmentProject> selectRelatedTransactionProjects(@Param("isRelatedTransaction") Boolean isRelatedTransaction);

    /**
     * 查询需要监管关注的项目
     *
     * @param needRegulatoryAttention 是否需要监管关注
     * @return 投资项目列表
     */
    List<InvestmentProject> selectRegulatoryAttentionProjects(@Param("needRegulatoryAttention") Boolean needRegulatoryAttention);

    /**
     * 查询项目负责人的项目
     *
     * @param projectManager 项目负责人
     * @return 投资项目列表
     */
    List<InvestmentProject> selectByProjectManager(@Param("projectManager") String projectManager);

    /**
     * 按投资类型统计项目
     *
     * @return 类型统计
     */
    List<Map<String, Object>> selectInvestmentTypeStatistics();

    /**
     * 按投资行业统计项目
     *
     * @return 行业统计
     */
    List<Map<String, Object>> selectInvestmentIndustryStatistics();

    /**
     * 按投资地区统计项目
     *
     * @return 地区统计
     */
    List<Map<String, Object>> selectInvestmentRegionStatistics();

    /**
     * 按项目状态统计项目
     *
     * @return 状态统计
     */
    List<Map<String, Object>> selectProjectStatusStatistics();

    /**
     * 按投资阶段统计项目
     *
     * @return 阶段统计
     */
    List<Map<String, Object>> selectInvestmentStageStatistics();

    /**
     * 按风险等级统计项目
     *
     * @return 风险等级统计
     */
    List<Map<String, Object>> selectRiskLevelStatistics();

    /**
     * 按审批状态统计项目
     *
     * @return 审批状态统计
     */
    List<Map<String, Object>> selectApprovalStatusStatistics();

    /**
     * 查询投资趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 投资趋势数据
     */
    List<Map<String, Object>> selectInvestmentTrend(@Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);

    /**
     * 查询投资金额趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 投资金额趋势数据
     */
    List<Map<String, Object>> selectInvestmentAmountTrend(@Param("startDate") String startDate,
                                                         @Param("endDate") String endDate);

    /**
     * 查询投资收益分析
     *
     * @return 投资收益分析数据
     */
    List<Map<String, Object>> selectInvestmentReturnAnalysis();

    /**
     * 查询投资风险分析
     *
     * @return 投资风险分析数据
     */
    List<Map<String, Object>> selectInvestmentRiskAnalysis();

    /**
     * 查询投资效果评估
     *
     * @param investorEnterpriseId 投资方企业ID
     * @return 投资效果评估数据
     */
    List<Map<String, Object>> selectInvestmentEffectEvaluation(@Param("investorEnterpriseId") String investorEnterpriseId);

    /**
     * 批量更新项目状态
     *
     * @param projectIds 项目ID列表
     * @param projectStatus 项目状态
     * @return 更新数量
     */
    int batchUpdateProjectStatus(@Param("projectIds") List<String> projectIds,
                                @Param("projectStatus") String projectStatus);

    /**
     * 批量更新审批状态
     *
     * @param projectIds 项目ID列表
     * @param approvalStatus 审批状态
     * @return 更新数量
     */
    int batchUpdateApprovalStatus(@Param("projectIds") List<String> projectIds,
                                 @Param("approvalStatus") String approvalStatus);

    /**
     * 批量更新风险等级
     *
     * @param projectIds 项目ID列表
     * @param riskLevel 风险等级
     * @return 更新数量
     */
    int batchUpdateRiskLevel(@Param("projectIds") List<String> projectIds,
                            @Param("riskLevel") String riskLevel);

    /**
     * 删除过期项目记录
     *
     * @param days 过期天数
     * @return 删除数量
     */
    int deleteExpiredProjectRecords(@Param("days") Integer days);

    /**
     * 获取投资项目统计概览
     *
     * @return 统计概览
     */
    Map<String, Object> selectInvestmentStatisticsOverview();

    /**
     * 导出投资项目列表
     *
     * @param queryVO 查询参数
     * @return 导出数据列表
     */
    List<Map<String, Object>> exportInvestmentProjectList(@Param("queryVO") InvestmentProjectQueryVO queryVO);
}
