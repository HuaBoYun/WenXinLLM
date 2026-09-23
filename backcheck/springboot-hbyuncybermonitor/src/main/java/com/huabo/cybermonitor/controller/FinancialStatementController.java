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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.FinancialStatement;
import com.huabo.cybermonitor.service.IFinancialStatementService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.FinancialStatementQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 财务报表控制器
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Tag(name="财务报表管理",description="财务报表管理")
@RestController
@RequestMapping("/v1/supervision/financial/statement")
public class FinancialStatementController {

	private static final Logger log = LoggerFactory.getLogger(FinancialStatementController.class);

    @Autowired
    private IFinancialStatementService financialStatementService;

    @Autowired
    private com.huabo.cybermonitor.mapper.GzctFinStatementMapper gzctFinStatementMapper;

    // ==================== 基础CRUD操作 ====================

    @Operation(summary = "分页查询财务报表列表")
    @PostMapping("/list")
    public R<Map<String, Object>> list(@RequestBody FinancialStatementQueryVO queryVO) {
        try {
            Map<String, Object> result = financialStatementService.selectFinancialStatementList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询财务报表列表失败", e);
            return R.fail("查询财务报表列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询财务报表详情")
    @GetMapping("/{statementId}")
    public R<FinancialStatement> getById(@Parameter(description="报表ID") @PathVariable String statementId) {
        try {
            FinancialStatement statement = financialStatementService.selectFinancialStatementById(statementId);
            return R.success(statement);
        } catch (Exception e) {
            log.error("查询财务报表详情失败: {}", statementId, e);
            return R.fail("查询财务报表详情失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增财务报表")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody FinancialStatement financialStatement) {
        try {
            boolean result = financialStatementService.insertFinancialStatement(financialStatement);
            return result ? R.success(true, "新增财务报表成功") : R.fail("新增财务报表失败");
        } catch (Exception e) {
            log.error("新增财务报表失败", e);
            return R.fail("新增财务报表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改财务报表")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody FinancialStatement financialStatement) {
        try {
            boolean result = financialStatementService.updateFinancialStatement(financialStatement);
            return result ? R.success(true, "修改财务报表成功") : R.fail("修改财务报表失败");
        } catch (Exception e) {
            log.error("修改财务报表失败", e);
            return R.fail("修改财务报表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除财务报表")
    @DeleteMapping("/{statementId}")
    public R<Boolean> delete(@Parameter(description="报表ID") @PathVariable String statementId) {
        try {
            int rows = gzctFinStatementMapper.deleteById(statementId);
            return rows > 0 ? R.success(true, "删除财务报表成功") : R.fail("记录不存在");
        } catch (Exception e) {
            log.error("删除财务报表失败: {}", statementId, e);
            return R.fail("删除财务报表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除财务报表")
    @DeleteMapping("/batch")
    public R<Boolean> deleteBatch(@RequestBody List<String> statementIds) {
        try {
            boolean result = financialStatementService.deleteFinancialStatementByIds(statementIds);
            return result ? R.success(true, "批量删除财务报表成功") : R.fail("批量删除财务报表失败");
        } catch (Exception e) {
            log.error("批量删除财务报表失败", e);
            return R.fail("批量删除财务报表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "保存财务报表（新增/更新）")
    @PostMapping("/save")
    public R<Boolean> save(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("statementId") != null ? params.get("statementId").toString().trim() : null;
            if (id != null && id.isEmpty()) id = null;
            com.huabo.cybermonitor.entity.GzctFinStatement record;
            if (id != null) {
                record = gzctFinStatementMapper.selectById(id);
                if (record == null) return R.fail("记录不存在");
            } else {
                record = new com.huabo.cybermonitor.entity.GzctFinStatement();
                record.setCreateTime(java.time.LocalDateTime.now());
            }
            if (params.get("companyName") != null && !params.get("companyName").toString().trim().isEmpty())
                record.setCompanyName(params.get("companyName").toString().trim());
            if (params.get("companyId") != null && !params.get("companyId").toString().trim().isEmpty())
                record.setCompanyId(params.get("companyId").toString().trim());
            if (params.get("statementType") != null && !params.get("statementType").toString().trim().isEmpty())
                record.setStatementType(params.get("statementType").toString().trim());
            String period = params.get("period") != null ? params.get("period").toString().trim() :
                (params.get("reportPeriod") != null ? params.get("reportPeriod").toString().trim() : null);
            if (period != null && !period.isEmpty()) record.setPeriod(period);
            if (params.get("totalAssets") != null && !params.get("totalAssets").toString().trim().isEmpty())
                record.setTotalAssets(new java.math.BigDecimal(params.get("totalAssets").toString().trim()));
            if (params.get("totalLiabilities") != null && !params.get("totalLiabilities").toString().trim().isEmpty())
                record.setTotalLiabilities(new java.math.BigDecimal(params.get("totalLiabilities").toString().trim()));
            if (params.get("netAssets") != null && !params.get("netAssets").toString().trim().isEmpty())
                record.setNetAssets(new java.math.BigDecimal(params.get("netAssets").toString().trim()));
            if (params.get("revenue") != null && !params.get("revenue").toString().trim().isEmpty())
                record.setRevenue(new java.math.BigDecimal(params.get("revenue").toString().trim()));
            if (params.get("netProfit") != null && !params.get("netProfit").toString().trim().isEmpty())
                record.setNetProfit(new java.math.BigDecimal(params.get("netProfit").toString().trim()));
            if (params.get("operatingCashflow") != null && !params.get("operatingCashflow").toString().trim().isEmpty())
                record.setOperatingCashflow(new java.math.BigDecimal(params.get("operatingCashflow").toString().trim()));
            if (params.get("auditStatus") != null && !params.get("auditStatus").toString().trim().isEmpty())
                record.setAuditStatus(params.get("auditStatus").toString().trim());
            record.setUpdateTime(java.time.LocalDateTime.now());
            if (id != null) {
                gzctFinStatementMapper.updateById(record);
            } else {
                if (record.getAuditStatus() == null || record.getAuditStatus().isEmpty()) {
                    record.setAuditStatus("DRAFT");
                }
                gzctFinStatementMapper.insert(record);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("保存财务报表失败", e);
            return R.fail("保存失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除财务报表(POST)")
    @PostMapping("/batch-delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的记录");
            int rows = gzctFinStatementMapper.deleteBatchIds(ids);
            return rows > 0 ? R.success(true, "批量删除成功") : R.fail("未找到要删除的记录");
        } catch (Exception e) {
            log.error("批量删除财务报表失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    // ==================== 动态筛选选项接口 ====================

    @Operation(summary = "获取可用报告期列表")
    @GetMapping("/periods")
    public R<List<Map<String, Object>>> getAvailablePeriods(
            @Parameter(description="报表类型") @RequestParam(required = false) String statementType) {
        try {
            LambdaQueryWrapper<com.huabo.cybermonitor.entity.GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (statementType != null && !statementType.isEmpty()) {
                w.eq(com.huabo.cybermonitor.entity.GzctFinStatement::getStatementType, statementType);
            }
            w.isNotNull(com.huabo.cybermonitor.entity.GzctFinStatement::getPeriod);
            w.select(com.huabo.cybermonitor.entity.GzctFinStatement::getPeriod);
            w.groupBy(com.huabo.cybermonitor.entity.GzctFinStatement::getPeriod);
            w.orderByDesc(com.huabo.cybermonitor.entity.GzctFinStatement::getPeriod);
            List<com.huabo.cybermonitor.entity.GzctFinStatement> records = gzctFinStatementMapper.selectList(w);
            List<Map<String, Object>> periods = new java.util.ArrayList<>();
            for (com.huabo.cybermonitor.entity.GzctFinStatement r : records) {
                if (r.getPeriod() != null && !r.getPeriod().isEmpty()) {
                    Map<String, Object> m = new java.util.HashMap<>();
                    m.put("value", r.getPeriod());
                    m.put("label", r.getPeriod());
                    periods.add(m);
                }
            }
            return R.success(periods);
        } catch (Exception e) {
            log.error("获取可用报告期列表失败", e);
            return R.fail("获取可用报告期列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取可用报表类型列表")
    @GetMapping("/statement-types")
    public R<List<Map<String, Object>>> getAvailableStatementTypes() {
        try {
            LambdaQueryWrapper<com.huabo.cybermonitor.entity.GzctFinStatement> w = new LambdaQueryWrapper<>();
            w.isNotNull(com.huabo.cybermonitor.entity.GzctFinStatement::getStatementType);
            w.select(com.huabo.cybermonitor.entity.GzctFinStatement::getStatementType);
            w.groupBy(com.huabo.cybermonitor.entity.GzctFinStatement::getStatementType);
            w.orderByAsc(com.huabo.cybermonitor.entity.GzctFinStatement::getStatementType);
            List<com.huabo.cybermonitor.entity.GzctFinStatement> records = gzctFinStatementMapper.selectList(w);
            List<Map<String, Object>> types = new java.util.ArrayList<>();
            for (com.huabo.cybermonitor.entity.GzctFinStatement r : records) {
                if (r.getStatementType() != null && !r.getStatementType().isEmpty()) {
                    Map<String, Object> m = new java.util.HashMap<>();
                    m.put("value", r.getStatementType());
                    m.put("label", r.getStatementType());
                    types.add(m);
                }
            }
            return R.success(types);
        } catch (Exception e) {
            log.error("获取可用报表类型列表失败", e);
            return R.fail("获取可用报表类型列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取可用审计状态列表")
    @GetMapping("/audit-statuses")
    public R<List<Map<String, Object>>> getAvailableAuditStatuses() {
        try {
            LambdaQueryWrapper<com.huabo.cybermonitor.entity.GzctFinStatement> w = new LambdaQueryWrapper<>();
            w.isNotNull(com.huabo.cybermonitor.entity.GzctFinStatement::getAuditStatus);
            w.select(com.huabo.cybermonitor.entity.GzctFinStatement::getAuditStatus);
            w.groupBy(com.huabo.cybermonitor.entity.GzctFinStatement::getAuditStatus);
            w.orderByAsc(com.huabo.cybermonitor.entity.GzctFinStatement::getAuditStatus);
            List<com.huabo.cybermonitor.entity.GzctFinStatement> records = gzctFinStatementMapper.selectList(w);
            List<Map<String, Object>> statuses = new java.util.ArrayList<>();
            for (com.huabo.cybermonitor.entity.GzctFinStatement r : records) {
                if (r.getAuditStatus() != null && !r.getAuditStatus().isEmpty()) {
                    Map<String, Object> m = new java.util.HashMap<>();
                    m.put("value", r.getAuditStatus());
                    m.put("label", r.getAuditStatus());
                    statuses.add(m);
                }
            }
            return R.success(statuses);
        } catch (Exception e) {
            log.error("获取可用审计状态列表失败", e);
            return R.fail("获取可用审计状态列表失败: " + e.getMessage());
        }
    }

    // ==================== 业务查询接口 ====================

    @Operation(summary = "根据企业ID查询财务报表")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<List<FinancialStatement>> getByEnterpriseId(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<FinancialStatement> statements = financialStatementService.selectByEnterpriseId(enterpriseId);
            return R.success(statements);
        } catch (Exception e) {
            log.error("根据企业ID查询财务报表失败: {}", enterpriseId, e);
            return R.fail("查询财务报表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据报表类型查询财务报表")
    @GetMapping("/type/{statementType}")
    public R<List<FinancialStatement>> getByStatementType(@Parameter(description="报表类型") @PathVariable String statementType) {
        try {
            List<FinancialStatement> statements = financialStatementService.selectByStatementType(statementType);
            return R.success(statements);
        } catch (Exception e) {
            log.error("根据报表类型查询财务报表失败: {}", statementType, e);
            return R.fail("查询财务报表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询监管关注报表")
    @GetMapping("/regulatory-attention")
    public R<List<FinancialStatement>> getRegulatoryAttentionStatements() {
        try {
            List<FinancialStatement> statements = financialStatementService.selectRegulatoryAttentionStatements(true);
            return R.success(statements);
        } catch (Exception e) {
            log.error("查询监管关注报表失败", e);
            return R.fail("查询监管关注报表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询异常报表")
    @GetMapping("/anomaly")
    public R<List<FinancialStatement>> getAnomalyStatements() {
        try {
            List<FinancialStatement> statements = financialStatementService.selectAnomalyStatements(true);
            return R.success(statements);
        } catch (Exception e) {
            log.error("查询异常报表失败", e);
            return R.fail("查询异常报表失败: " + e.getMessage());
        }
    }

    // ==================== 数据质量分析接口 ====================

    @Operation(summary = "数据质量分析")
    @PostMapping("/analyze/data-quality")
    public R<Map<String, Object>> analyzeDataQuality(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.analyzeDataQuality(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("数据质量分析失败: {}", enterpriseId, e);
            return R.fail("数据质量分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "数据完整性分析")
    @PostMapping("/analyze/data-completeness")
    public R<Map<String, Object>> analyzeDataCompleteness(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.analyzeDataCompleteness(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("数据完整性分析失败: {}", enterpriseId, e);
            return R.fail("数据完整性分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "计算数据质量评分")
    @PostMapping("/calculate/data-quality-score")
    public R<Map<String, Object>> calculateDataQualityScore(@Parameter(description="报表ID") @RequestParam String statementId) {
        try {
            Map<String, Object> result = financialStatementService.calculateDataQualityScore(statementId);
            return R.success(result);
        } catch (Exception e) {
            log.error("计算数据质量评分失败: {}", statementId, e);
            return R.fail("计算数据质量评分失败: " + e.getMessage());
        }
    }

    // ==================== 财务数据异常检测接口 ====================

    @Operation(summary = "财务数据异常检测")
    @PostMapping("/detect/anomalies")
    public R<Map<String, Object>> detectFinancialDataAnomalies(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.detectFinancialDataAnomalies(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("财务数据异常检测失败: {}", enterpriseId, e);
            return R.fail("财务数据异常检测失败: " + e.getMessage());
        }
    }

    @Operation(summary = "趋势异常识别")
    @PostMapping("/identify/trend-anomalies")
    public R<Map<String, Object>> identifyTrendAnomalies(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.identifyTrendAnomalies(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("趋势异常识别失败: {}", enterpriseId, e);
            return R.fail("趋势异常识别失败: " + e.getMessage());
        }
    }

    @Operation(summary = "标记异常报表")
    @PostMapping("/mark-anomaly")
    public R<Boolean> markAnomalyStatement(
            @Parameter(description="报表ID") @RequestParam String statementId,
            @Parameter(description="异常描述") @RequestParam String anomalyDescription) {
        try {
            boolean result = financialStatementService.markAnomalyStatement(statementId, anomalyDescription);
            return result ? R.success(true, "标记异常报表成功") : R.fail("标记异常报表失败");
        } catch (Exception e) {
            log.error("标记异常报表失败: {}", statementId, e);
            return R.fail("标记异常报表失败: " + e.getMessage());
        }
    }

    // ==================== 财务风险识别接口 ====================

    @Operation(summary = "财务风险预警分析")
    @PostMapping("/analyze/financial-risk")
    public R<Map<String, Object>> analyzeFinancialRiskWarning(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.analyzeFinancialRiskWarning(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("财务风险预警分析失败: {}", enterpriseId, e);
            return R.fail("财务风险预警分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "流动性风险分析")
    @PostMapping("/analyze/liquidity-risk")
    public R<Map<String, Object>> analyzeLiquidityRisk(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.analyzeLiquidityRisk(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("流动性风险分析失败: {}", enterpriseId, e);
            return R.fail("流动性风险分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "设置风险预警")
    @PostMapping("/set-risk-warning")
    public R<Boolean> setRiskWarning(
            @Parameter(description="报表ID") @RequestParam String statementId,
            @Parameter(description="风险等级") @RequestParam String riskLevel,
            @Parameter(description="风险因素") @RequestParam String riskFactors) {
        try {
            boolean result = financialStatementService.setRiskWarning(statementId, riskLevel, riskFactors);
            return result ? R.success(true, "设置风险预警成功") : R.fail("设置风险预警失败");
        } catch (Exception e) {
            log.error("设置风险预警失败: {}", statementId, e);
            return R.fail("设置风险预警失败: " + e.getMessage());
        }
    }

    // ==================== 合规监管接口 ====================

    @Operation(summary = "财务合规检查")
    @PostMapping("/check/compliance")
    public R<Map<String, Object>> checkFinancialCompliance(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.checkFinancialCompliance(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("财务合规检查失败: {}", enterpriseId, e);
            return R.fail("财务合规检查失败: " + e.getMessage());
        }
    }

    @Operation(summary = "会计准则合规性检查")
    @PostMapping("/check/accounting-standards")
    public R<Map<String, Object>> checkAccountingStandardsCompliance(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.checkAccountingStandardsCompliance(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("会计准则合规性检查失败: {}", enterpriseId, e);
            return R.fail("会计准则合规性检查失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析接口 ====================

    @Operation(summary = "按报表类型统计")
    @GetMapping("/statistics/statement-type")
    public R<List<Map<String, Object>>> getStatementTypeStatistics(
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            List<Map<String, Object>> result = financialStatementService.getStatementTypeStatistics(startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("按报表类型统计失败", e);
            return R.fail("按报表类型统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按审计状态统计")
    @GetMapping("/statistics/audit-status")
    public R<List<Map<String, Object>>> getAuditStatusStatistics(
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            List<Map<String, Object>> result = financialStatementService.getAuditStatusStatistics(startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("按审计状态统计失败", e);
            return R.fail("按审计状态统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "财务报表统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getFinancialStatementStatisticsOverview() {
        try {
            Map<String, Object> result = financialStatementService.getFinancialStatementStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取财务报表统计概览失败", e);
            return R.fail("获取财务报表统计概览失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作接口 ====================

    @Operation(summary = "批量更新报表状态")
    @PostMapping("/batch/update-status")
    public R<Boolean> batchUpdateStatementStatus(
            @RequestBody List<String> statementIds,
            @Parameter(description="报表状态") @RequestParam String statementStatus) {
        try {
            boolean result = financialStatementService.batchUpdateStatementStatus(statementIds, statementStatus);
            return result ? R.success(true, "批量更新报表状态成功") : R.fail("批量更新报表状态失败");
        } catch (Exception e) {
            log.error("批量更新报表状态失败", e);
            return R.fail("批量更新报表状态失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量导入财务报表")
    @PostMapping("/batch/import")
    public R<Map<String, Object>> batchImportFinancialStatements(@RequestBody List<FinancialStatement> statementList) {
        try {
            Map<String, Object> result = financialStatementService.batchImportFinancialStatements(statementList);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量导入财务报表失败", e);
            return R.fail("批量导入财务报表失败: " + e.getMessage());
        }
    }

    // ==================== 导出功能接口 ====================

    @Operation(summary = "导出财务报表列表")
    @PostMapping("/export/list")
    public R<List<Map<String, Object>>> exportFinancialStatementList(@RequestBody FinancialStatementQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = financialStatementService.exportFinancialStatementList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出财务报表列表失败", e);
            return R.fail("导出财务报表列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出数据质量报告")
    @PostMapping("/export/data-quality-report")
    public R<Map<String, Object>> exportDataQualityReport(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialStatementService.exportDataQualityAnalysisReport(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出数据质量报告失败: {}", enterpriseId, e);
            return R.fail("导出数据质量报告失败: " + e.getMessage());
        }
    }

}
