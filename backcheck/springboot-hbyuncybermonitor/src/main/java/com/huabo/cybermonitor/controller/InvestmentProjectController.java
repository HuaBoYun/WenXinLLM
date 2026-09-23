package com.huabo.cybermonitor.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.cybermonitor.entity.InvestmentProject;
import com.huabo.cybermonitor.service.IInvestmentProjectService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.InvestmentProjectQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 投资项目信息控制器
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="投资项目信息管理",description="投资项目信息管理")
@RestController
@RequestMapping("/v1/supervision/equity/project")
public class InvestmentProjectController {

	private static final Logger log = LoggerFactory.getLogger(InvestmentProjectController.class);

    @Autowired
    private IInvestmentProjectService investmentProjectService;

    @Operation(summary = "分页查询投资项目列表")
    @PostMapping("/list")
    public R<PageResult<InvestmentProject>> getInvestmentProjectList(@RequestBody InvestmentProjectQueryVO queryVO) {
        try {
            PageResult<InvestmentProject> result = investmentProjectService.getInvestmentProjectList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询投资项目列表失败", e);
            return R.fail("查询投资项目列表失败");
        }
    }

    @Operation(summary = "根据ID获取投资项目详情")
    @GetMapping("/detail/{projectId}")
    public R<InvestmentProject> getInvestmentProjectById(@PathVariable String projectId) {
        try {
            InvestmentProject project = investmentProjectService.getInvestmentProjectById(projectId);
            return R.success(project);
        } catch (Exception e) {
            log.error("获取投资项目详情失败，projectId: {}", projectId, e);
            return R.fail("获取投资项目详情失败");
        }
    }

    @Operation(summary = "新增投资项目")
    @PostMapping("/add")
    public R<Boolean> addInvestmentProject(@RequestBody InvestmentProject investmentProject) {
        try {
            boolean result = investmentProjectService.addInvestmentProject(investmentProject);
            return R.success(result);
        } catch (Exception e) {
            log.error("新增投资项目失败", e);
            return R.fail("新增投资项目失败");
        }
    }

    @Operation(summary = "更新投资项目")
    @PostMapping("/update")
    public R<Boolean> updateInvestmentProject(@RequestBody InvestmentProject investmentProject) {
        try {
            boolean result = investmentProjectService.updateInvestmentProject(investmentProject);
            return R.success(result);
        } catch (Exception e) {
            log.error("更新投资项目失败", e);
            return R.fail("更新投资项目失败");
        }
    }

    @Operation(summary = "删除投资项目")
    @DeleteMapping("/delete/{projectId}")
    public R<Boolean> deleteInvestmentProject(@PathVariable String projectId) {
        try {
            boolean result = investmentProjectService.deleteInvestmentProject(projectId);
            return R.success(result);
        } catch (Exception e) {
            log.error("删除投资项目失败，projectId: {}", projectId, e);
            return R.fail("删除投资项目失败");
        }
    }

    @Operation(summary = "批量删除投资项目")
    @PostMapping("/batchDelete")
    public R<Boolean> batchDeleteInvestmentProject(@RequestBody List<String> projectIds) {
        try {
            boolean result = investmentProjectService.batchDeleteInvestmentProject(projectIds);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量删除投资项目失败", e);
            return R.fail("批量删除投资项目失败");
        }
    }

    @Operation(summary = "查询战略投资项目")
    @GetMapping("/strategic")
    public R<List<InvestmentProject>> getStrategicInvestmentProjects(@RequestParam(defaultValue = "true") Boolean isStrategicInvestment) {
        try {
            List<InvestmentProject> result = investmentProjectService.getStrategicInvestmentProjects(isStrategicInvestment);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询战略投资项目失败", e);
            return R.fail("查询战略投资项目失败");
        }
    }

    @Operation(summary = "查询关联交易项目")
    @GetMapping("/relatedTransaction")
    public R<List<InvestmentProject>> getRelatedTransactionProjects(@RequestParam(defaultValue = "true") Boolean isRelatedTransaction) {
        try {
            List<InvestmentProject> result = investmentProjectService.getRelatedTransactionProjects(isRelatedTransaction);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询关联交易项目失败", e);
            return R.fail("查询关联交易项目失败");
        }
    }

    @Operation(summary = "查询需要监管关注的项目")
    @GetMapping("/regulatoryAttention")
    public R<List<InvestmentProject>> getRegulatoryAttentionProjects(@RequestParam(defaultValue = "true") Boolean needRegulatoryAttention) {
        try {
            List<InvestmentProject> result = investmentProjectService.getRegulatoryAttentionProjects(needRegulatoryAttention);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询需要监管关注的项目失败", e);
            return R.fail("查询需要监管关注的项目失败");
        }
    }

    @Operation(summary = "投资决策风险评估")
    @PostMapping("/assessment/risk")
    public R<Map<String, Object>> assessInvestmentDecisionRisk(@RequestBody InvestmentProject investmentProject) {
        try {
            Map<String, Object> result = investmentProjectService.assessInvestmentDecisionRisk(investmentProject);
            return R.success(result);
        } catch (Exception e) {
            log.error("投资决策风险评估失败", e);
            return R.fail("投资决策风险评估失败");
        }
    }

    @Operation(summary = "投资项目合规性检查")
    @PostMapping("/check/compliance")
    public R<Map<String, Object>> checkInvestmentProjectCompliance(@RequestBody InvestmentProject investmentProject) {
        try {
            Map<String, Object> result = investmentProjectService.checkInvestmentProjectCompliance(investmentProject);
            return R.success(result);
        } catch (Exception e) {
            log.error("投资项目合规性检查失败", e);
            return R.fail("投资项目合规性检查失败");
        }
    }

    @Operation(summary = "投资效果跟踪分析")
    @GetMapping("/track/effect/{projectId}")
    public R<Map<String, Object>> trackInvestmentEffect(@PathVariable String projectId) {
        try {
            Map<String, Object> result = investmentProjectService.trackInvestmentEffect(projectId);
            return R.success(result);
        } catch (Exception e) {
            log.error("投资效果跟踪分析失败，projectId: {}", projectId, e);
            return R.fail("投资效果跟踪分析失败");
        }
    }

    @Operation(summary = "按投资类型统计项目")
    @GetMapping("/statistics/investmentType")
    public R<List<Map<String, Object>>> getInvestmentTypeStatistics() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentTypeStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按投资类型统计项目失败", e);
            return R.fail("按投资类型统计项目失败");
        }
    }

    @Operation(summary = "按投资行业统计项目")
    @GetMapping("/statistics/investmentIndustry")
    public R<List<Map<String, Object>>> getInvestmentIndustryStatistics() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentIndustryStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按投资行业统计项目失败", e);
            return R.fail("按投资行业统计项目失败");
        }
    }

    @Operation(summary = "按投资地区统计项目")
    @GetMapping("/statistics/investmentRegion")
    public R<List<Map<String, Object>>> getInvestmentRegionStatistics() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentRegionStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按投资地区统计项目失败", e);
            return R.fail("按投资地区统计项目失败");
        }
    }

    @Operation(summary = "按项目状态统计项目")
    @GetMapping("/statistics/projectStatus")
    public R<List<Map<String, Object>>> getProjectStatusStatistics() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getProjectStatusStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按项目状态统计项目失败", e);
            return R.fail("按项目状态统计项目失败");
        }
    }

    @Operation(summary = "按投资阶段统计项目")
    @GetMapping("/statistics/investmentStage")
    public R<List<Map<String, Object>>> getInvestmentStageStatistics() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentStageStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按投资阶段统计项目失败", e);
            return R.fail("按投资阶段统计项目失败");
        }
    }

    @Operation(summary = "按风险等级统计项目")
    @GetMapping("/statistics/riskLevel")
    public R<List<Map<String, Object>>> getRiskLevelStatistics() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getRiskLevelStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按风险等级统计项目失败", e);
            return R.fail("按风险等级统计项目失败");
        }
    }

    @Operation(summary = "按审批状态统计项目")
    @GetMapping("/statistics/approvalStatus")
    public R<List<Map<String, Object>>> getApprovalStatusStatistics() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getApprovalStatusStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("按审批状态统计项目失败", e);
            return R.fail("按审批状态统计项目失败");
        }
    }

    @Operation(summary = "查询投资趋势")
    @GetMapping("/trend")
    public R<List<Map<String, Object>>> getInvestmentTrend(@RequestParam String startDate,
                                                          @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentTrend(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询投资趋势失败", e);
            return R.fail("查询投资趋势失败");
        }
    }

    @Operation(summary = "查询投资金额趋势")
    @GetMapping("/trend/amount")
    public R<List<Map<String, Object>>> getInvestmentAmountTrend(@RequestParam String startDate,
                                                                @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentAmountTrend(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询投资金额趋势失败", e);
            return R.fail("查询投资金额趋势失败");
        }
    }

    @Operation(summary = "查询投资收益分析")
    @GetMapping("/analysis/return")
    public R<List<Map<String, Object>>> getInvestmentReturnAnalysis() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentReturnAnalysis();
            return R.success(result);
        } catch (Exception e) {
            log.error("查询投资收益分析失败", e);
            return R.fail("查询投资收益分析失败");
        }
    }

    @Operation(summary = "查询投资风险分析")
    @GetMapping("/analysis/risk")
    public R<List<Map<String, Object>>> getInvestmentRiskAnalysis() {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentRiskAnalysis();
            return R.success(result);
        } catch (Exception e) {
            log.error("查询投资风险分析失败", e);
            return R.fail("查询投资风险分析失败");
        }
    }

    @Operation(summary = "查询投资效果评估")
    @GetMapping("/evaluation/effect/{investorEnterpriseId}")
    public R<List<Map<String, Object>>> getInvestmentEffectEvaluation(@PathVariable String investorEnterpriseId) {
        try {
            List<Map<String, Object>> result = investmentProjectService.getInvestmentEffectEvaluation(investorEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询投资效果评估失败，investorEnterpriseId: {}", investorEnterpriseId, e);
            return R.fail("查询投资效果评估失败");
        }
    }

    @Operation(summary = "获取投资项目统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getInvestmentStatisticsOverview() {
        try {
            Map<String, Object> result = investmentProjectService.getInvestmentStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取投资项目统计概览失败", e);
            return R.fail("获取投资项目统计概览失败");
        }
    }

    @Operation(summary = "批量更新项目状态")
    @PostMapping("/batchUpdateProjectStatus")
    public R<Boolean> batchUpdateProjectStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> projectIds = (List<String>) params.get("projectIds");
            String projectStatus = (String) params.get("projectStatus");
            boolean result = investmentProjectService.batchUpdateProjectStatus(projectIds, projectStatus);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新项目状态失败", e);
            return R.fail("批量更新项目状态失败");
        }
    }

    @Operation(summary = "批量更新审批状态")
    @PostMapping("/batchUpdateApprovalStatus")
    public R<Boolean> batchUpdateApprovalStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> projectIds = (List<String>) params.get("projectIds");
            String approvalStatus = (String) params.get("approvalStatus");
            boolean result = investmentProjectService.batchUpdateApprovalStatus(projectIds, approvalStatus);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            return R.fail("批量更新审批状态失败");
        }
    }

    @Operation(summary = "批量更新风险等级")
    @PostMapping("/batchUpdateRiskLevel")
    public R<Boolean> batchUpdateRiskLevel(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> projectIds = (List<String>) params.get("projectIds");
            String riskLevel = (String) params.get("riskLevel");
            boolean result = investmentProjectService.batchUpdateRiskLevel(projectIds, riskLevel);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新风险等级失败", e);
            return R.fail("批量更新风险等级失败");
        }
    }

    @Operation(summary = "导出投资项目列表")
    @PostMapping("/export")
    public R<List<Map<String, Object>>> exportInvestmentProjectList(@RequestBody InvestmentProjectQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = investmentProjectService.exportInvestmentProjectList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出投资项目列表失败", e);
            return R.fail("导出投资项目列表失败");
        }
    }
}
