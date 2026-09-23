package com.huabo.cybermonitor.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.InvestmentProject;
import com.huabo.cybermonitor.mapper.InvestmentProjectMapper;
import com.huabo.cybermonitor.service.IInvestmentProjectService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.InvestmentProjectQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 投资项目信息服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class InvestmentProjectServiceImpl extends ServiceImpl<InvestmentProjectMapper, InvestmentProject> implements IInvestmentProjectService {

    @Autowired
    private InvestmentProjectMapper investmentProjectMapper;

    @Override
    public PageResult<InvestmentProject> getInvestmentProjectList(InvestmentProjectQueryVO queryVO) {
        try {
            List<InvestmentProject> list = investmentProjectMapper.selectInvestmentProjectList(queryVO);
            
            QueryWrapper<InvestmentProject> countWrapper = new QueryWrapper<>();
            if (queryVO.getInvestorEnterpriseId() != null) {
                countWrapper.eq("INVESTOR_ENTERPRISE_ID", queryVO.getInvestorEnterpriseId());
            }
            long total = this.count(countWrapper);
            
            return null;
        } catch (Exception e) {
            log.error("查询投资项目列表失败", e);
            return null;
        }
    }

    @Override
    public InvestmentProject getInvestmentProjectById(String projectId) {
        try {
            return this.getById(projectId);
        } catch (Exception e) {
            log.error("根据ID获取投资项目详情失败，projectId: {}", projectId, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addInvestmentProject(InvestmentProject investmentProject) {
        try {
            investmentProject.setCreateTime(LocalDateTime.now());
            investmentProject.setUpdateTime(LocalDateTime.now());
            
            // 进行投资决策风险评估
            this.assessInvestmentRisk(investmentProject);
            
            // 进行合规性检查
            this.checkInvestmentCompliance(investmentProject);
            
            return this.save(investmentProject);
        } catch (Exception e) {
            log.error("新增投资项目失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInvestmentProject(InvestmentProject investmentProject) {
        try {
            investmentProject.setUpdateTime(LocalDateTime.now());
            return this.updateById(investmentProject);
        } catch (Exception e) {
            log.error("更新投资项目失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInvestmentProject(String projectId) {
        try {
            return this.removeById(projectId);
        } catch (Exception e) {
            log.error("删除投资项目失败，projectId: {}", projectId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteInvestmentProject(List<String> projectIds) {
        try {
            return this.removeByIds(projectIds);
        } catch (Exception e) {
            log.error("批量删除投资项目失败", e);
            return false;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByInvestorEnterpriseId(String investorEnterpriseId) {
        try {
            return investmentProjectMapper.selectByInvestorEnterpriseId(investorEnterpriseId);
        } catch (Exception e) {
            log.error("根据投资方企业ID查询项目失败，investorEnterpriseId: {}", investorEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByInvesteeEnterpriseId(String investeeEnterpriseId) {
        try {
            return investmentProjectMapper.selectByInvesteeEnterpriseId(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("根据被投资方企业ID查询项目失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByInvestmentType(String investmentType) {
        try {
            return investmentProjectMapper.selectByInvestmentType(investmentType);
        } catch (Exception e) {
            log.error("按投资类型查询项目失败，investmentType: {}", investmentType, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByInvestmentIndustry(String investmentIndustry) {
        try {
            return investmentProjectMapper.selectByInvestmentIndustry(investmentIndustry);
        } catch (Exception e) {
            log.error("按投资行业查询项目失败，investmentIndustry: {}", investmentIndustry, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByInvestmentRegion(String investmentRegion) {
        try {
            return investmentProjectMapper.selectByInvestmentRegion(investmentRegion);
        } catch (Exception e) {
            log.error("按投资地区查询项目失败，investmentRegion: {}", investmentRegion, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByProjectStatus(String projectStatus) {
        try {
            return investmentProjectMapper.selectByProjectStatus(projectStatus);
        } catch (Exception e) {
            log.error("按项目状态查询项目失败，projectStatus: {}", projectStatus, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByInvestmentStage(String investmentStage) {
        try {
            return investmentProjectMapper.selectByInvestmentStage(investmentStage);
        } catch (Exception e) {
            log.error("按投资阶段查询项目失败，investmentStage: {}", investmentStage, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByRiskLevel(String riskLevel) {
        try {
            return investmentProjectMapper.selectByRiskLevel(riskLevel);
        } catch (Exception e) {
            log.error("按风险等级查询项目失败，riskLevel: {}", riskLevel, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByApprovalStatus(String approvalStatus) {
        try {
            return investmentProjectMapper.selectByApprovalStatus(approvalStatus);
        } catch (Exception e) {
            log.error("按审批状态查询项目失败，approvalStatus: {}", approvalStatus, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getStrategicInvestmentProjects(Boolean isStrategicInvestment) {
        try {
            return investmentProjectMapper.selectStrategicInvestmentProjects(isStrategicInvestment);
        } catch (Exception e) {
            log.error("查询战略投资项目失败，isStrategicInvestment: {}", isStrategicInvestment, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getRelatedTransactionProjects(Boolean isRelatedTransaction) {
        try {
            return investmentProjectMapper.selectRelatedTransactionProjects(isRelatedTransaction);
        } catch (Exception e) {
            log.error("查询关联交易项目失败，isRelatedTransaction: {}", isRelatedTransaction, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getRegulatoryAttentionProjects(Boolean needRegulatoryAttention) {
        try {
            return investmentProjectMapper.selectRegulatoryAttentionProjects(needRegulatoryAttention);
        } catch (Exception e) {
            log.error("查询需要监管关注的项目失败，needRegulatoryAttention: {}", needRegulatoryAttention, e);
            return null;
        }
    }

    @Override
    public List<InvestmentProject> getInvestmentProjectByProjectManager(String projectManager) {
        try {
            return investmentProjectMapper.selectByProjectManager(projectManager);
        } catch (Exception e) {
            log.error("查询项目负责人的项目失败，projectManager: {}", projectManager, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentTypeStatistics() {
        try {
            return investmentProjectMapper.selectInvestmentTypeStatistics();
        } catch (Exception e) {
            log.error("按投资类型统计项目失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentIndustryStatistics() {
        try {
            return investmentProjectMapper.selectInvestmentIndustryStatistics();
        } catch (Exception e) {
            log.error("按投资行业统计项目失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentRegionStatistics() {
        try {
            return investmentProjectMapper.selectInvestmentRegionStatistics();
        } catch (Exception e) {
            log.error("按投资地区统计项目失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getProjectStatusStatistics() {
        try {
            return investmentProjectMapper.selectProjectStatusStatistics();
        } catch (Exception e) {
            log.error("按项目状态统计项目失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentStageStatistics() {
        try {
            return investmentProjectMapper.selectInvestmentStageStatistics();
        } catch (Exception e) {
            log.error("按投资阶段统计项目失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getRiskLevelStatistics() {
        try {
            return investmentProjectMapper.selectRiskLevelStatistics();
        } catch (Exception e) {
            log.error("按风险等级统计项目失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getApprovalStatusStatistics() {
        try {
            return investmentProjectMapper.selectApprovalStatusStatistics();
        } catch (Exception e) {
            log.error("按审批状态统计项目失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentTrend(String startDate, String endDate) {
        try {
            return investmentProjectMapper.selectInvestmentTrend(startDate, endDate);
        } catch (Exception e) {
            log.error("查询投资趋势失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentAmountTrend(String startDate, String endDate) {
        try {
            return investmentProjectMapper.selectInvestmentAmountTrend(startDate, endDate);
        } catch (Exception e) {
            log.error("查询投资金额趋势失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentReturnAnalysis() {
        try {
            return investmentProjectMapper.selectInvestmentReturnAnalysis();
        } catch (Exception e) {
            log.error("查询投资收益分析失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentRiskAnalysis() {
        try {
            return investmentProjectMapper.selectInvestmentRiskAnalysis();
        } catch (Exception e) {
            log.error("查询投资风险分析失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentEffectEvaluation(String investorEnterpriseId) {
        try {
            return investmentProjectMapper.selectInvestmentEffectEvaluation(investorEnterpriseId);
        } catch (Exception e) {
            log.error("查询投资效果评估失败，investorEnterpriseId: {}", investorEnterpriseId, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateProjectStatus(List<String> projectIds, String projectStatus) {
        try {
            int result = investmentProjectMapper.batchUpdateProjectStatus(projectIds, projectStatus);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新项目状态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateApprovalStatus(List<String> projectIds, String approvalStatus) {
        try {
            int result = investmentProjectMapper.batchUpdateApprovalStatus(projectIds, approvalStatus);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateRiskLevel(List<String> projectIds, String riskLevel) {
        try {
            int result = investmentProjectMapper.batchUpdateRiskLevel(projectIds, riskLevel);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新风险等级失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteExpiredProjectRecords(Integer days) {
        try {
            if (days == null || days <= 0) {
                days = 365;
            }
            return investmentProjectMapper.deleteExpiredProjectRecords(days);
        } catch (Exception e) {
            log.error("删除过期项目记录失败，days: {}", days, e);
            return 0;
        }
    }

    @Override
    public Map<String, Object> getInvestmentStatisticsOverview() {
        try {
            return investmentProjectMapper.selectInvestmentStatisticsOverview();
        } catch (Exception e) {
            log.error("获取投资项目统计概览失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> exportInvestmentProjectList(InvestmentProjectQueryVO queryVO) {
        try {
            return investmentProjectMapper.exportInvestmentProjectList(queryVO);
        } catch (Exception e) {
            log.error("导出投资项目列表失败", e);
            return null;
        }
    }

    /**
     * 投资风险评估
     */
    private void assessInvestmentRisk(InvestmentProject project) {
        try {
            String riskLevel = InvestmentProject.RISK_LEVEL_LOW;
            
            // 根据投资金额评估风险
            if (project.getPlannedInvestmentAmount() != null) {
                BigDecimal amount = project.getPlannedInvestmentAmount();
                if (amount.compareTo(new BigDecimal("10000")) > 0) {
                    riskLevel = InvestmentProject.RISK_LEVEL_HIGH;
                } else if (amount.compareTo(new BigDecimal("5000")) > 0) {
                    riskLevel = InvestmentProject.RISK_LEVEL_MEDIUM;
                }
            }
            
            // 根据投资类型调整风险等级
            if (InvestmentProject.INVESTMENT_TYPE_VENTURE.equals(project.getInvestmentType()) ||
                InvestmentProject.INVESTMENT_TYPE_GROWTH.equals(project.getInvestmentType())) {
                riskLevel = InvestmentProject.RISK_LEVEL_HIGH;
            }
            
            project.setRiskLevel(riskLevel);
            
            // 设置是否需要监管关注
            if (InvestmentProject.RISK_LEVEL_HIGH.equals(riskLevel) ||
                InvestmentProject.RISK_LEVEL_CRITICAL.equals(riskLevel)) {
                project.setNeedRegulatoryAttention(true);
            }
            
        } catch (Exception e) {
            log.error("投资风险评估失败", e);
        }
    }

    @Override
    public Map<String, Object> assessInvestmentDecisionRisk(InvestmentProject investmentProject) {
        try {
            Map<String, Object> result = new HashMap<>();

            this.assessInvestmentRisk(investmentProject);
            result.put("riskLevel", investmentProject.getRiskLevel());
            result.put("needRegulatoryAttention", investmentProject.getNeedRegulatoryAttention());

            // 详细风险分析
            Map<String, Object> riskAnalysis = new HashMap<>();
            riskAnalysis.put("investmentAmount", investmentProject.getPlannedInvestmentAmount());
            riskAnalysis.put("investmentType", investmentProject.getInvestmentType());
            riskAnalysis.put("isStrategicInvestment", investmentProject.getIsStrategicInvestment());
            riskAnalysis.put("isRelatedTransaction", investmentProject.getIsRelatedTransaction());

            result.put("riskAnalysis", riskAnalysis);
            return result;
        } catch (Exception e) {
            log.error("投资决策风险评估失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> checkInvestmentProjectCompliance(InvestmentProject investmentProject) {
        try {
            Map<String, Object> result = new HashMap<>();

            this.checkInvestmentCompliance(investmentProject);
            result.put("complianceResult", investmentProject.getComplianceCheckResult());
            result.put("isCompliant", "合规".equals(investmentProject.getComplianceCheckResult()));

            return result;
        } catch (Exception e) {
            log.error("投资项目合规性检查失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> trackInvestmentEffect(String projectId) {
        try {
            InvestmentProject project = this.getById(projectId);
            if (project == null) {
                return new HashMap<>();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("project", project);

            // 投资效果分析
            Map<String, Object> effectAnalysis = new HashMap<>();
            effectAnalysis.put("investmentProgress", project.getInvestmentProgress());
            effectAnalysis.put("expectedReturnRate", project.getExpectedReturnRate());
            effectAnalysis.put("actualReturnRate", project.getActualReturnRate());

            // 计算投资效果评分
            BigDecimal effectScore = this.calculateEffectScore(project);
            effectAnalysis.put("effectScore", effectScore);

            result.put("effectAnalysis", effectAnalysis);
            return result;
        } catch (Exception e) {
            log.error("投资效果跟踪分析失败，projectId: {}", projectId, e);
            return new HashMap<>();
        }
    }

    @Override
    public String getInvestmentTypeLabel(String investmentType) {
        if (investmentType == null) return "";
        switch (investmentType) {
            case InvestmentProject.INVESTMENT_TYPE_EQUITY: return "股权投资";
            case InvestmentProject.INVESTMENT_TYPE_DEBT: return "债权投资";
            case InvestmentProject.INVESTMENT_TYPE_VENTURE: return "风险投资";
            case InvestmentProject.INVESTMENT_TYPE_GROWTH: return "成长投资";
            case InvestmentProject.INVESTMENT_TYPE_BUYOUT: return "并购投资";
            case InvestmentProject.INVESTMENT_TYPE_MEZZANINE: return "夹层投资";
            default: return investmentType;
        }
    }

    @Override
    public String getInvestmentMethodLabel(String investmentMethod) {
        if (investmentMethod == null) return "";
        switch (investmentMethod) {
            case InvestmentProject.INVESTMENT_METHOD_CASH: return "现金投资";
            case InvestmentProject.INVESTMENT_METHOD_ASSET: return "资产投资";
            case InvestmentProject.INVESTMENT_METHOD_EQUITY_SWAP: return "股权置换";
            case InvestmentProject.INVESTMENT_METHOD_DEBT_EQUITY_SWAP: return "债转股";
            case InvestmentProject.INVESTMENT_METHOD_MERGER: return "合并";
            case InvestmentProject.INVESTMENT_METHOD_ACQUISITION: return "收购";
            default: return investmentMethod;
        }
    }

    @Override
    public String getProjectStatusLabel(String projectStatus) {
        if (projectStatus == null) return "";
        switch (projectStatus) {
            case InvestmentProject.PROJECT_STATUS_PLANNING: return "规划中";
            case InvestmentProject.PROJECT_STATUS_APPROVED: return "已批准";
            case InvestmentProject.PROJECT_STATUS_EXECUTING: return "执行中";
            case InvestmentProject.PROJECT_STATUS_SUSPENDED: return "暂停";
            case InvestmentProject.PROJECT_STATUS_COMPLETED: return "已完成";
            case InvestmentProject.PROJECT_STATUS_CANCELLED: return "已取消";
            case InvestmentProject.PROJECT_STATUS_FAILED: return "失败";
            default: return projectStatus;
        }
    }

    @Override
    public String getInvestmentStageLabel(String investmentStage) {
        if (investmentStage == null) return "";
        switch (investmentStage) {
            case InvestmentProject.INVESTMENT_STAGE_SEED: return "种子期";
            case InvestmentProject.INVESTMENT_STAGE_STARTUP: return "初创期";
            case InvestmentProject.INVESTMENT_STAGE_GROWTH: return "成长期";
            case InvestmentProject.INVESTMENT_STAGE_EXPANSION: return "扩张期";
            case InvestmentProject.INVESTMENT_STAGE_MATURE: return "成熟期";
            case InvestmentProject.INVESTMENT_STAGE_DECLINE: return "衰退期";
            default: return investmentStage;
        }
    }

    @Override
    public String getRiskLevelLabel(String riskLevel) {
        if (riskLevel == null) return "";
        switch (riskLevel) {
            case InvestmentProject.RISK_LEVEL_LOW: return "低风险";
            case InvestmentProject.RISK_LEVEL_MEDIUM: return "中风险";
            case InvestmentProject.RISK_LEVEL_HIGH: return "高风险";
            case InvestmentProject.RISK_LEVEL_CRITICAL: return "严重风险";
            default: return riskLevel;
        }
    }

    @Override
    public String getDecisionProcessStatusLabel(String decisionProcessStatus) {
        if (decisionProcessStatus == null) return "";
        switch (decisionProcessStatus) {
            case InvestmentProject.DECISION_PROCESS_STATUS_INITIAL: return "初步评估";
            case InvestmentProject.DECISION_PROCESS_STATUS_DUE_DILIGENCE: return "尽职调查";
            case InvestmentProject.DECISION_PROCESS_STATUS_COMMITTEE_REVIEW: return "投委会审查";
            case InvestmentProject.DECISION_PROCESS_STATUS_BOARD_APPROVAL: return "董事会批准";
            case InvestmentProject.DECISION_PROCESS_STATUS_FINAL_APPROVAL: return "最终批准";
            default: return decisionProcessStatus;
        }
    }

    @Override
    public String getApprovalStatusLabel(String approvalStatus) {
        if (approvalStatus == null) return "";
        switch (approvalStatus) {
            case InvestmentProject.APPROVAL_STATUS_PENDING: return "待审批";
            case InvestmentProject.APPROVAL_STATUS_APPROVED: return "已审批";
            case InvestmentProject.APPROVAL_STATUS_REJECTED: return "已拒绝";
            case InvestmentProject.APPROVAL_STATUS_CANCELLED: return "已取消";
            case InvestmentProject.APPROVAL_STATUS_EXPIRED: return "已过期";
            default: return approvalStatus;
        }
    }

    /**
     * 计算投资效果评分
     */
    private BigDecimal calculateEffectScore(InvestmentProject project) {
        try {
            BigDecimal score = BigDecimal.ZERO;

            // 根据投资进度评分
            if (project.getInvestmentProgress() != null) {
                score = score.add(project.getInvestmentProgress().multiply(new BigDecimal("0.3")));
            }

            // 根据收益率评分
            if (project.getActualReturnRate() != null && project.getExpectedReturnRate() != null) {
                BigDecimal returnRatio = project.getActualReturnRate().divide(project.getExpectedReturnRate(), 2, BigDecimal.ROUND_HALF_UP);
                score = score.add(returnRatio.multiply(new BigDecimal("70")));
            }

            return score.min(new BigDecimal("100"));
        } catch (Exception e) {
            log.error("计算投资效果评分失败", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * 投资合规性检查
     */
    private void checkInvestmentCompliance(InvestmentProject project) {
        try {
            StringBuilder result = new StringBuilder("合规");

            // 检查投资金额是否需要审批
            if (project.getPlannedInvestmentAmount() != null &&
                project.getPlannedInvestmentAmount().compareTo(new BigDecimal("1000")) > 0) {
                if (project.getApprovalStatus() == null ||
                    !InvestmentProject.APPROVAL_STATUS_APPROVED.equals(project.getApprovalStatus())) {
                    result.append("；大额投资需要审批");
                }
            }

            // 检查关联交易
            if (Boolean.TRUE.equals(project.getIsRelatedTransaction())) {
                result.append("；关联交易需要特别审查");
            }

            project.setComplianceCheckResult(result.toString());
        } catch (Exception e) {
            log.error("投资合规性检查失败", e);
        }
    }
}
