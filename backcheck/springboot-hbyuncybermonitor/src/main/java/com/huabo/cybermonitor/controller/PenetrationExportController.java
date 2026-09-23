package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.SimpleXlsxWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

@Tag(name = "穿透式监管数据导出")
@RestController
@RequestMapping("/v1/supervision/export")
@Slf4j
public class PenetrationExportController {

    @Autowired private TblPropertyRightMapper propertyRightMapper;
    @Autowired private TblPropertyTransactionMapper propertyTransactionMapper;
    @Autowired private GzctPropertyChangeMapper propertyChangeMapper;
    @Autowired private GzctShareholdingCompanyMapper shareholdingCompanyMapper;
    @Autowired private TblThreeTableCompareMapper threeTableCompareMapper;
    @Autowired private TblTradeReviewMapper tradeReviewMapper;
    @Autowired private TblAssetConcentrationMapper assetConcentrationMapper;
    @Autowired private TblAssetFlowMapper assetFlowMapper;
    @Autowired private TblAssetMappingMapper assetMappingMapper;
    @Autowired private TblCrossHoldingMapper crossHoldingMapper;
    @Autowired private TblAssetAllocationMapper assetAllocationMapper;
    @Autowired private TblAssetQualityMapper assetQualityMapper;
    @Autowired private EquityStructureMapper equityStructureMapper;
    @Autowired private ControlChainMapper controlChainMapper;
    @Autowired private TblControlChainAnalysisMapper controlChainAnalysisMapper;
    @Autowired private EquityChangeRecordMapper equityChangeRecordMapper;
    @Autowired private TblBeneficialOwnerMapper beneficialOwnerMapper;
    @Autowired private TblInvestmentDecisionMapper investmentDecisionMapper;
    @Autowired private ShareholderAnalysisMapper shareholderAnalysisMapper;

    private PrintWriter initCsv(HttpServletResponse response, String fileName) throws Exception {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        PrintWriter w = response.getWriter();
        w.write("\uFEFF");
        return w;
    }

    private void row(PrintWriter w, String... c) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c.length; i++) {
            if (i > 0) sb.append(",");
            String v = c[i] != null ? c[i] : "";
            if (v.contains(",") || v.contains("\"") || v.contains("\n")) sb.append("\"").append(v.replace("\"", "\"\"")).append("\"");
            else sb.append(v);
        }
        w.println(sb);
    }

    private String s(Object o) { return o != null ? o.toString() : ""; }

    private String assetTypeCn(String v) {
        if (v == null) return "";
        switch (v) { case "FUND": return "资金"; case "EQUITY": return "股权"; case "FIXED": return "固定资产"; case "INTANGIBLE": return "无形资产"; case "OTHER": return "其他"; default: return v; }
    }
    private String flowTypeCn(String v) {
        if (v == null) return "";
        switch (v) { case "INVEST": return "投资"; case "TRANSFER": return "转移"; case "DISPOSE": return "处置"; case "LEASE": return "租赁"; default: return v; }
    }
    private String relatedCn(String v) {
        if (v == null) return "";
        switch (v) { case "Y": return "是"; case "N": return "否"; default: return v; }
    }
    private String riskCn(String v) {
        if (v == null) return "";
        switch (v) { case "HIGH": return "高风险"; case "MEDIUM": return "中风险"; case "LOW": return "低风险"; default: return v; }
    }

    @Operation(summary = "导出产权登记台账") @GetMapping("/property/right")
    public void exportRight(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "产权登记台账.csv");
            row(w, "企业名称","产权类型","持股比例","登记状态","投资金额","注册资本","股权层级","行业","地区","经营状态");
            for (TblPropertyRight r : propertyRightMapper.selectList(null))
                row(w, s(r.getCompanyName()),s(r.getRightType()),s(r.getHoldingRatio()),s(r.getRegistrationStatus()),s(r.getInvestAmount()),s(r.getRegisteredCapital()),s(r.getEquityLevel()),s(r.getIndustry()),s(r.getRegion()),s(r.getBusinessStatus()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出产权交易台账") @GetMapping("/property/transaction")
    public void exportTransaction(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "产权交易台账.csv");
            row(w, "企业名称","交易类型","交易方式","交易金额","评估价值","交易对手","是否进场","审批状态","合规状态","交易日期");
            for (TblPropertyTransaction r : propertyTransactionMapper.selectList(null))
                row(w, s(r.getCompanyName()),s(r.getTransactionType()),s(r.getTransactionMethod()),s(r.getTransactionAmount()),s(r.getAppraisalValue()),s(r.getCounterparty()),s(r.getIsExchangeTraded()),s(r.getApprovalStatus()),s(r.getComplianceStatus()),s(r.getTransactionDate()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出产权变动登记") @GetMapping("/property/change")
    public void exportChange(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "产权变动登记.csv");
            row(w, "企业名称","变动类型","变动原因","变动前比例","变动后比例","变动日期","审批状态","是否重大变动");
            for (GzctPropertyChange r : propertyChangeMapper.selectList(null))
                row(w, s(r.getCompanyName()),s(r.getChangeType()),s(r.getChangeReason()),s(r.getBeforeRatio()),s(r.getAfterRatio()),s(r.getChangeDate()),s(r.getApprovalStatus()),s(r.getIsMajorChange()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出参股企业") @GetMapping("/property/shareholding")
    public void exportShareholding(HttpServletResponse response) {
        try {
            List<GzctShareholdingCompany> dataList = shareholdingCompanyMapper.selectList(null);
            List<List<Object>> rows = new java.util.ArrayList<>();
            // 表头
            List<Object> header = java.util.Arrays.asList(
                "企业名称", "持股比例(%)", "出资额(万元)", "营业收入(万元)", "净利润(万元)",
                "总资产(万元)", "ROE(%)", "资产负债率(%)", "连续亏损年", "未分红年",
                "综合评级", "最近分红年份", "经营状态", "行业"
            );
            rows.add(header);
            for (GzctShareholdingCompany r : dataList) {
                List<Object> row = java.util.Arrays.asList(
                    r.getCompanyName(), r.getEquityRatio(), r.getInvestAmount(),
                    r.getRevenue(), r.getNetProfit(), r.getTotalAssets(),
                    r.getRoe(), r.getAssetLiabilityRatio(),
                    r.getConsecutiveLossYears(), r.getNoDividendYears(),
                    ratingToCn(r.getRating()), r.getLastDividendYear(),
                    businessStatusToCn(r.getBusinessStatus()), r.getIndustry()
                );
                rows.add(row);
            }
            byte[] xlsx = SimpleXlsxWriter.write("参股企业经营分析", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("参股企业经营分析.xlsx", "UTF-8"));
            response.getOutputStream().write(xlsx);
            response.getOutputStream().flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出三表比对") @GetMapping("/property/threeTable")
    public void exportThreeTable(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "三表比对看板.csv");
            row(w, "企业名称","产权登记状态","工商登记状态","财务并表","产权比例","工商比例","财务比例","是否一致","不一致类型","整改状态");
            for (TblThreeTableCompare r : threeTableCompareMapper.selectList(null))
                row(w, s(r.getCompanyName()),s(r.getPropertyRegStatus()),s(r.getBusinessRegStatus()),s(r.getFinancialConsolidation()),s(r.getPropertyRatio()),s(r.getBusinessRatio()),s(r.getFinancialRatio()),s(r.getIsConsistent()),s(r.getInconsistencyType()),s(r.getRectificationStatus()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出交易合规审查") @GetMapping("/property/tradeReview")
    public void exportTradeReview(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "产权交易合规审查.csv");
            row(w, "交易编号","企业名称","交易方式","交易金额(万元)","评估价值(万元)","价格比率(%)","合规状态","交易对手","交易日期","合规问题");
            for (TblTradeReview r : tradeReviewMapper.selectList(null))
                row(w, s(r.getTransNo()),s(r.getCompanyName()),transMethodLabel(r.getTransMethod()),
                    r.getTransAmount() != null ? r.getTransAmount().toString() : "",
                    r.getAppraisalValue() != null ? r.getAppraisalValue().toString() : "",
                    r.getPriceRatio() != null ? r.getPriceRatio().toString() : "",
                    complianceStatusLabel(r.getComplianceStatus()),
                    s(r.getCounterparty()),s(r.getTransDate()),s(r.getComplianceIssues()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出资产集中度") @GetMapping("/asset/concentration")
    public void exportConcentration(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "资产集中度分析.csv");
            row(w, "企业名称","行业","地区","总资产","资产占比","HHI指数","集中度等级","风险等级");
            for (TblAssetConcentration r : assetConcentrationMapper.selectList(null))
                row(w, s(r.getCompanyName()),s(r.getIndustry()),s(r.getRegion()),s(r.getTotalAssets()),s(r.getAssetRatio()),s(r.getHhiIndex()),s(r.getConcentrationLevel()),s(r.getRiskLevel()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出资产流向") @GetMapping("/asset/flow")
    public void exportFlow(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "资产流向追踪.csv");
            row(w, "源企业","目标企业","资产类型","资产名称","流向金额","流向类型","流向日期","是否关联方","风险等级");
            for (TblAssetFlow r : assetFlowMapper.selectList(null))
                row(w, s(r.getSourceCompanyName()),s(r.getTargetCompanyName()),assetTypeCn(r.getAssetType()),s(r.getAssetName()),s(r.getFlowAmount()),flowTypeCn(r.getFlowType()),s(r.getFlowDate()),relatedCn(r.getIsRelatedParty()),riskCn(r.getRiskLevel()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出资产映射") @GetMapping("/asset/mapping")
    public void exportMapping(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "资产映射.csv");
            row(w, "企业名称","资产类别","资产名称","资产价值","账面价值","公允价值","映射状态","差异类型","差异金额");
            for (TblAssetMapping r : assetMappingMapper.selectList(null))
                row(w, s(r.getCompanyName()),s(r.getAssetCategory()),s(r.getAssetName()),s(r.getAssetValue()),s(r.getBookValue()),s(r.getFairValue()),s(r.getMappingStatus()),s(r.getDiscrepancyType()),s(r.getDiscrepancyAmount()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出交叉持股") @GetMapping("/asset/crossHolding")
    public void exportCrossHolding(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "交叉持股分析.csv");
            row(w, "企业A","企业B","A持B比例(%)","B持A比例(%)","交叉类型","风险等级","资本虚增(万元)","是否循环","链条长度","发现日期");
            for (TblCrossHolding r : crossHoldingMapper.selectList(null)) {
                String crossTypeCn = "DIRECT".equals(r.getCrossType()) ? "直接交叉" : "INDIRECT".equals(r.getCrossType()) ? "间接交叉" : "CIRCULAR".equals(r.getCrossType()) ? "循环持股" : s(r.getCrossType());
                row(w, s(r.getCompanyAName()),s(r.getCompanyBName()),s(r.getAHoldBRatio()),s(r.getBHoldARatio()),crossTypeCn,riskCn(r.getRiskLevel()),s(r.getCapitalInflation()),relatedCn(r.getIsCircular()),s(r.getChainLength()),s(r.getDiscoveryDate()));
            }
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出资产配置") @GetMapping("/asset/allocation")
    public void exportAllocation(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "资产配置统计.csv");
            row(w, "企业名称","资产类型","资产金额","配置比例","目标比例","偏离度","收益率","配置状态");
            for (TblAssetAllocation r : assetAllocationMapper.selectList(null))
                row(w, s(r.getCompanyName()),s(r.getAssetType()),s(r.getAssetAmount()),s(r.getAllocationRatio()),s(r.getTargetRatio()),s(r.getDeviation()),s(r.getYieldRate()),s(r.getAllocationStatus()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出资产质量") @GetMapping("/asset/quality")
    public void exportQuality(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "资产质量统计.csv");
            row(w, "企业名称","资产名称","资产类别","资产价值","质量等级","质量评分","风险等级","减值金额","收益率","评估状态");
            for (TblAssetQuality r : assetQualityMapper.selectList(null))
                row(w, s(r.getEnterpriseName()),s(r.getAssetName()),s(r.getAssetCategory()),s(r.getAssetValue()),s(r.getQualityLevel()),s(r.getQualityScore()),s(r.getRiskLevel()),s(r.getImpairmentAmount()),s(r.getReturnRate()),s(r.getAssessmentStatus()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出股权结构") @GetMapping("/equity/structure")
    public void exportStructure(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "股权结构分析.csv");
            row(w, "被投企业ID","投资方名称","投资方类型","持股比例","持股金额","表决权比例","控制类型","状态");
            for (EquityStructure r : equityStructureMapper.selectList(null))
                row(w, s(r.getEnterpriseId()),s(r.getInvestorName()),s(r.getInvestorType()),s(r.getShareholdingRatio()),s(r.getShareholdingAmount()),s(r.getVotingRatio()),s(r.getControlType()),s(r.getStatus()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出控制链") @GetMapping("/equity/controlChain")
    public void exportControlChain(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "控制链分析.csv");
            row(w, "企业名称","控制人","控制类型","控制强度","链条长度","节点数","稳定性","风险等级","控制路径");
            for (ControlChain r : controlChainMapper.selectList(null))
                row(w, s(r.getEnterpriseName()),s(r.getControllerName()),s(r.getControlType()),s(r.getControlStrength()),s(r.getChainLength()),s(r.getNodeCount()),s(r.getStabilityLevel()),s(r.getRiskLevel()),s(r.getChainPath()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出控制链穿透分析") @GetMapping("/equity/controlChainAnalysis")
    public void exportChainAnalysis(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "控制链穿透分析.csv");
            row(w, "企业名称","分析类型","穿透深度","总控制比例","直接控制比例","间接控制比例","风险评分","薄弱环节数");
            for (TblControlChainAnalysis r : controlChainAnalysisMapper.selectList(null))
                row(w, s(r.getEnterpriseName()),s(r.getAnalysisType()),s(r.getPenetrationDepth()),s(r.getTotalControlRatio()),s(r.getDirectControlRatio()),s(r.getIndirectControlRatio()),s(r.getRiskScore()),s(r.getWeakLinkCount()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出股权变动记录") @GetMapping("/equity/changeRecord")
    public void exportChangeRecord(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "股权变动记录.csv");
            row(w, "被投企业","投资方","变动类型","变动原因","变动前比例","变动后比例","变动金额","审批状态","预警级别");
            for (EquityChangeRecord r : equityChangeRecordMapper.selectList(null))
                row(w, s(r.getInvesteeEnterpriseName()),s(r.getInvestorEnterpriseName()),s(r.getChangeType()),s(r.getChangeReason()),s(r.getBeforeShareholdingRatio()),s(r.getAfterShareholdingRatio()),s(r.getChangeAmount()),s(r.getApprovalStatus()),s(r.getWarningLevel()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出受益所有人") @GetMapping("/equity/beneficialOwner")
    public void exportBeneficialOwner(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "受益所有人.csv");
            row(w, "企业名称","所有人名称","类型","国籍","受益比例","控制方式","穿透层级","风险等级","核实状态");
            for (TblBeneficialOwner r : beneficialOwnerMapper.selectList(null))
                row(w, s(r.getEnterpriseName()),s(r.getOwnerName()),s(r.getOwnerType()),s(r.getNationality()),s(r.getBeneficialRatio()),s(r.getControlMethod()),s(r.getPenetrationLevel()),s(r.getRiskLevel()),s(r.getVerificationStatus()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出投资决策") @GetMapping("/equity/investmentDecision")
    public void exportInvestmentDecision(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "投资决策分析.csv");
            row(w, "企业名称","项目名称","投资类型","投资金额","预期收益率","风险等级","决策状态","决策日期","战略匹配度");
            for (TblInvestmentDecision r : investmentDecisionMapper.selectList(null))
                row(w, s(r.getEnterpriseName()),s(r.getProjectName()),s(r.getInvestmentType()),s(r.getInvestmentAmount()),s(r.getExpectedReturn()),s(r.getRiskLevel()),s(r.getDecisionStatus()),s(r.getDecisionDate()),s(r.getStrategicFit()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    @Operation(summary = "导出股东分析") @GetMapping("/equity/shareholderAnalysis")
    public void exportShareholderAnalysis(HttpServletResponse response) {
        try {
            PrintWriter w = initCsv(response, "股东穿透分析.csv");
            row(w, "企业名称","股东名称","股东类型","持股比例","持股金额","穿透层级","最终控制人","风险等级","是否关联方");
            for (ShareholderAnalysis r : shareholderAnalysisMapper.selectList(null))
                row(w, s(r.getEnterpriseName()),s(r.getShareholderName()),s(r.getShareholderType()),s(r.getShareholdingRatio()),s(r.getShareholdingAmount()),s(r.getPenetrationLevel()),s(r.getUltimateController()),s(r.getRiskLevel()),s(r.getIsRelatedParty()));
            w.flush();
        } catch (Exception e) { log.error("导出失败", e); }
    }

    /** 交易方式枚举转中文 */
    private String transMethodLabel(String code) {
        if (code == null) return "";
        switch (code) {
            case "EXCHANGE": return "进场交易";
            case "AGREEMENT": return "协议转让";
            case "AUCTION": return "拍卖";
            case "FREE_TRANSFER": return "无偿划转";
            default: return code;
        }
    }

    /** 合规状态枚举转中文 */
    private String complianceStatusLabel(String code) {
        if (code == null) return "";
        switch (code) {
            case "COMPLIANT": return "合规";
            case "ISSUE": return "问题";
            case "VIOLATION": return "违规";
            default: return code;
        }
    }

    /** 经营状态转中文 */
    private String businessStatusToCn(String code) {
        if (code == null) return "";
        switch (code) {
            case "NORMAL": return "正常";
            case "LOSS": return "亏损";
            case "CLOSED": return "停业";
            case "REVOKED": return "吊销";
            case "CANCELLED": return "注销";
            default: return code;
        }
    }

    /** 综合评级转中文 */
    private String ratingToCn(String code) {
        if (code == null) return "";
        switch (code) {
            case "A": return "A 良好";
            case "B": return "B 一般";
            case "C": return "C 关注";
            case "D": return "D 高危";
            default: return code;
        }
    }
}
