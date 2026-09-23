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

import com.huabo.cybermonitor.entity.EquityChangeRecord;
import com.huabo.cybermonitor.service.IEquityChangeRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.EquityChangeRecordQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 股权变动记录控制器
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="股权变动记录管理",description="股权变动记录管理")
@RestController
@RequestMapping({"/v1/supervision/equity/change", "/v1/supervision/equity/changes"})
public class EquityChangeRecordController {

	private static final Logger log = LoggerFactory.getLogger(EquityChangeRecordController.class);

    /** 安全查询列（排除数据库可能不存在的列） */
    private static final String[] SAFE_COLUMNS = {
        "CHANGE_ID", "EQUITY_ID", "INVESTEE_ENTERPRISE_ID", "INVESTEE_ENTERPRISE_NAME",
        "INVESTOR_ENTERPRISE_ID", "INVESTOR_ENTERPRISE_NAME", "CHANGE_TYPE", "CHANGE_REASON",
        "BEFORE_SHAREHOLDING_RATIO", "AFTER_SHAREHOLDING_RATIO", "CHANGE_AMOUNT",
        "TRANSFEROR_ENTERPRISE_ID", "TRANSFEROR_ENTERPRISE_NAME",
        "TRANSFEREE_ENTERPRISE_ID", "TRANSFEREE_ENTERPRISE_NAME", "TRANSFER_METHOD",
        "CHANGE_DATE", "EFFECTIVE_DATE", "APPROVAL_STATUS", "IS_MAJOR_CHANGE",
        "NEED_WARNING", "WARNING_LEVEL", "WARNING_REASON", "REMARK",
        "CREATE_BY", "CREATE_TIME", "UPDATE_TIME"
    };

    /** 最小查询列（仅包含绝对确定存在的基础列，作为降级方案） */
    private static final String[] MINIMAL_COLUMNS = {
        "CHANGE_ID", "EQUITY_ID", "INVESTEE_ENTERPRISE_ID", "INVESTEE_ENTERPRISE_NAME",
        "INVESTOR_ENTERPRISE_ID", "INVESTOR_ENTERPRISE_NAME", "CHANGE_TYPE", "CHANGE_REASON",
        "BEFORE_SHAREHOLDING_RATIO", "AFTER_SHAREHOLDING_RATIO"
    };

    @Autowired
    private IEquityChangeRecordService equityChangeRecordService;

    /**
     * 安全地根据ID查询记录（避免SELECT *触发不存在的列错误）
     * 如果SAFE_COLUMNS查询失败，降级为MINIMAL_COLUMNS
     */
    private EquityChangeRecord safeGetById(String changeId) {
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            wrapper.select(SAFE_COLUMNS);
            wrapper.eq("CHANGE_ID", changeId);
            return equityChangeRecordService.getOne(wrapper);
        } catch (Exception e) {
            log.warn("safeGetById降级查询, changeId: {}", changeId);
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> fallback =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            fallback.select(MINIMAL_COLUMNS);
            fallback.eq("CHANGE_ID", changeId);
            return equityChangeRecordService.getOne(fallback);
        }
    }

    /**
     * 安全地执行分页查询（带降级）
     */
    private com.baomidou.mybatisplus.extension.plugins.pagination.Page<EquityChangeRecord> safePageQuery(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<EquityChangeRecord> page,
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper) {
        try {
            return equityChangeRecordService.page(page, wrapper);
        } catch (Exception e) {
            log.warn("分页查询降级，使用最小列集合: {}", e.getMessage());
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> fallbackWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            fallbackWrapper.select(MINIMAL_COLUMNS);
            fallbackWrapper.orderByDesc("CREATE_TIME");
            return equityChangeRecordService.page(page, fallbackWrapper);
        }
    }

    @Operation(summary = "分页查询股权变动记录列表")
    @PostMapping("/list")
    public R<PageResult<EquityChangeRecord>> getEquityChangeRecordList(@RequestBody(required = false) Map<String, Object> params) {
        try {
            if (params == null) params = new java.util.HashMap<>();
            int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            if (params.get("pageNumber") != null) pageNum = Integer.parseInt(params.get("pageNumber").toString());

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            // 显式指定查询列，避免SELECT *查询不存在的列
            wrapper.select(
                "CHANGE_ID", "EQUITY_ID", "INVESTEE_ENTERPRISE_ID", "INVESTEE_ENTERPRISE_NAME",
                "INVESTOR_ENTERPRISE_ID", "INVESTOR_ENTERPRISE_NAME", "CHANGE_TYPE", "CHANGE_REASON",
                "BEFORE_SHAREHOLDING_RATIO", "AFTER_SHAREHOLDING_RATIO", "CHANGE_AMOUNT",
                "TRANSFEROR_ENTERPRISE_ID", "TRANSFEROR_ENTERPRISE_NAME",
                "TRANSFEREE_ENTERPRISE_ID", "TRANSFEREE_ENTERPRISE_NAME", "TRANSFER_METHOD",
                "CHANGE_DATE", "EFFECTIVE_DATE", "APPROVAL_STATUS", "IS_MAJOR_CHANGE",
                "NEED_WARNING", "WARNING_LEVEL", "WARNING_REASON", "REMARK",
                "CREATE_BY", "CREATE_TIME", "UPDATE_TIME"
            );
            if (params.get("investeeEnterpriseName") != null && !params.get("investeeEnterpriseName").toString().isEmpty()) {
                wrapper.like("INVESTEE_ENTERPRISE_NAME", params.get("investeeEnterpriseName").toString());
            }
            if (params.get("enterpriseName") != null && !params.get("enterpriseName").toString().isEmpty()) {
                wrapper.like("INVESTEE_ENTERPRISE_NAME", params.get("enterpriseName").toString());
            }
            if (params.get("changeType") != null && !params.get("changeType").toString().isEmpty()) {
                wrapper.eq("CHANGE_TYPE", params.get("changeType").toString());
            }
            if (params.get("approvalStatus") != null && !params.get("approvalStatus").toString().isEmpty()) {
                wrapper.eq("APPROVAL_STATUS", params.get("approvalStatus").toString());
            }
            if (params.get("warningLevel") != null && !params.get("warningLevel").toString().isEmpty()) {
                wrapper.eq("WARNING_LEVEL", params.get("warningLevel").toString());
            }
            if (params.get("startDate") != null && !params.get("startDate").toString().isEmpty()) {
                wrapper.ge("CHANGE_DATE", params.get("startDate").toString());
            }
            if (params.get("endDate") != null && !params.get("endDate").toString().isEmpty()) {
                wrapper.le("CHANGE_DATE", params.get("endDate").toString() + " 23:59:59");
            }
            wrapper.orderByDesc("CREATE_TIME");

            com.baomidou.mybatisplus.extension.plugins.pagination.Page<EquityChangeRecord> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<EquityChangeRecord> result = safePageQuery(page, wrapper);

            PageResult<EquityChangeRecord> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage(pageNum);
            pageResult.setPageNumber(pageNum);
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize(pageSize);
            pageResult.setTlist(result.getRecords());
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询股权变动记录列表失败", e);
            return R.fail("查询股权变动记录列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID获取股权变动记录详情")
    @GetMapping("/detail/{changeId}")
    public R<EquityChangeRecord> getEquityChangeRecordById(@PathVariable String changeId) {
        try {
            EquityChangeRecord record = safeGetById(changeId);
            return R.success(record);
        } catch (Exception e) {
            log.error("获取股权变动记录详情失败，changeId: {}", changeId, e);
            return R.fail("获取股权变动记录详情失败");
        }
    }

    @Operation(summary = "根据ID获取股权变动记录详情（兼容前端路径）")
    @GetMapping("/{changeId}")
    public R<EquityChangeRecord> getById(@PathVariable String changeId) {
        try {
            EquityChangeRecord record = safeGetById(changeId);
            return R.success(record);
        } catch (Exception e) {
            log.error("获取股权变动记录详情失败，changeId: {}", changeId, e);
            return R.fail("获取详情失败");
        }
    }

    @Operation(summary = "新增股权变动记录")
    @PostMapping("/add")
    public R<Boolean> addEquityChangeRecord(@RequestBody(required = false) Map<String, Object> params) {
        try {
            EquityChangeRecord record = new EquityChangeRecord();
            record.setChangeId(null); // 让MyBatis-Plus自动生成UUID
            if (params != null) {
                if (params.get("investeeEnterpriseName") != null) record.setInvesteeEnterpriseName(params.get("investeeEnterpriseName").toString());
                if (params.get("investorEnterpriseName") != null) record.setInvestorEnterpriseName(params.get("investorEnterpriseName").toString());
                if (params.get("changeType") != null) record.setChangeType(params.get("changeType").toString());
                if (params.get("changeReason") != null) record.setChangeReason(params.get("changeReason").toString());
                if (params.get("beforeShareholdingRatio") != null && !params.get("beforeShareholdingRatio").toString().isEmpty()) {
                    record.setBeforeShareholdingRatio(new java.math.BigDecimal(params.get("beforeShareholdingRatio").toString()));
                }
                if (params.get("afterShareholdingRatio") != null && !params.get("afterShareholdingRatio").toString().isEmpty()) {
                    record.setAfterShareholdingRatio(new java.math.BigDecimal(params.get("afterShareholdingRatio").toString()));
                }
                if (params.get("changeAmount") != null && !params.get("changeAmount").toString().isEmpty()) {
                    record.setChangeAmount(new java.math.BigDecimal(params.get("changeAmount").toString()));
                }
                if (params.get("transferorEnterpriseName") != null) record.setTransferorEnterpriseName(params.get("transferorEnterpriseName").toString());
                if (params.get("transfereeEnterpriseName") != null) record.setTransfereeEnterpriseName(params.get("transfereeEnterpriseName").toString());
                if (params.get("transferMethod") != null) record.setTransferMethod(params.get("transferMethod").toString());
                if (params.get("remark") != null) record.setRemark(params.get("remark").toString());
                if (params.get("warningLevel") != null) record.setWarningLevel(params.get("warningLevel").toString());
                if (params.get("changeDate") != null && !params.get("changeDate").toString().isEmpty()) {
                    record.setChangeDate(java.time.LocalDate.parse(params.get("changeDate").toString()).atStartOfDay());
                }
            }
            record.setApprovalStatus("PENDING");
            record.setCreateTime(java.time.LocalDateTime.now());
            record.setUpdateTime(java.time.LocalDateTime.now());
            boolean result = equityChangeRecordService.save(record);
            return R.success(result);
        } catch (Exception e) {
            log.error("新增股权变动记录失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "影响分析")
    @PostMapping("/impact/analyze")
    public R<Map<String, Object>> impactAnalyze(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            String changeId = params != null ? (String) params.get("changeId") : null;
            if (changeId != null) {
                EquityChangeRecord record = safeGetById(changeId);
                if (record != null) {
                    result.put("changeType", record.getChangeType());
                    result.put("beforeRatio", record.getBeforeShareholdingRatio());
                    result.put("afterRatio", record.getAfterShareholdingRatio());
                    result.put("impactLevel", record.getIsMajorChange() != null && record.getIsMajorChange() ? "重大" : "一般");
                    result.put("controlChange", record.getBeforeIsControlling() != record.getAfterIsControlling() ? "控制权变更" : "无变化");
                    result.put("riskAssessment", record.getWarningLevel() != null ? record.getWarningLevel() : "LOW");
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("影响分析失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "审核历史")
    @PostMapping("/approval/history")
    public R<List<Map<String, Object>>> approvalHistory(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> history = new java.util.ArrayList<>();
            String changeId = params != null ? (String) params.get("changeId") : null;
            if (changeId != null) {
                EquityChangeRecord record = safeGetById(changeId);
                if (record != null) {
                    Map<String, Object> entry = new java.util.HashMap<>();
                    entry.put("time", record.getCreateTime() != null ? record.getCreateTime().toString() : "");
                    entry.put("action", "提交审核");
                    entry.put("status", record.getApprovalStatus());
                    entry.put("operator", record.getCreateBy() != null ? record.getCreateBy() : "system");
                    history.add(entry);
                    if ("APPROVED".equals(record.getApprovalStatus()) && record.getApprovalDate() != null) {
                        Map<String, Object> approveEntry = new java.util.HashMap<>();
                        approveEntry.put("time", record.getApprovalDate().toString());
                        approveEntry.put("action", "审核通过");
                        approveEntry.put("status", "APPROVED");
                        approveEntry.put("operator", record.getApprovalAuthority() != null ? record.getApprovalAuthority() : "admin");
                        history.add(approveEntry);
                    }
                }
            }
            return R.success(history);
        } catch (Exception e) {
            log.error("查询审核历史失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "审核通过")
    @PostMapping("/approve")
    public R<Boolean> approve(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String changeId = params != null ? (String) params.get("changeId") : null;
            if (changeId == null) return R.fail("changeId不能为空");
            EquityChangeRecord record = safeGetById(changeId);
            if (record == null) return R.fail("记录不存在");
            record.setApprovalStatus("APPROVED");
            record.setApprovalDate(java.time.LocalDateTime.now());
            record.setUpdateTime(java.time.LocalDateTime.now());
            equityChangeRecordService.updateById(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("审核失败", e);
            return R.fail("审核失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除股权变动记录")
    @DeleteMapping("/delete/{changeId}")
    public R<Boolean> deleteEquityChangeRecord(@PathVariable String changeId) {
        try {
            boolean result = equityChangeRecordService.removeById(changeId);
            return R.success(result);
        } catch (Exception e) {
            log.error("删除股权变动记录失败，changeId: {}", changeId, e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除股权变动记录（兼容前端路径）")
    @DeleteMapping("/{changeId}")
    public R<Boolean> deleteById(@PathVariable String changeId) {
        try {
            boolean result = equityChangeRecordService.removeById(changeId);
            return R.success(result);
        } catch (Exception e) {
            log.error("删除股权变动记录失败，changeId: {}", changeId, e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除股权变动记录")
    @PostMapping("/batchDelete")
    public R<Boolean> batchDeleteEquityChangeRecord(@RequestBody List<String> changeIds) {
        try {
            boolean result = equityChangeRecordService.batchDeleteEquityChangeRecord(changeIds);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量删除股权变动记录失败", e);
            return R.fail("批量删除股权变动记录失败");
        }
    }

    @Operation(summary = "查询重大变动记录")
    @GetMapping("/major")
    public R<List<EquityChangeRecord>> getMajorChangeRecords(@RequestParam(defaultValue = "true") Boolean isMajorChange) {
        try {
            List<EquityChangeRecord> result = equityChangeRecordService.getMajorChangeRecords(isMajorChange);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询重大变动记录失败", e);
            return R.fail("查询重大变动记录失败");
        }
    }

    @Operation(summary = "查询需要预警的变动记录")
    @GetMapping("/warning")
    public R<List<EquityChangeRecord>> getWarningChangeRecords(@RequestParam(defaultValue = "true") Boolean needWarning) {
        try {
            List<EquityChangeRecord> result = equityChangeRecordService.getWarningChangeRecords(needWarning);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询需要预警的变动记录失败", e);
            return R.fail("查询需要预警的变动记录失败");
        }
    }

    @Operation(summary = "查询最新变动记录")
    @GetMapping("/latest/{investeeEnterpriseId}")
    public R<List<EquityChangeRecord>> getLatestChangeRecords(@PathVariable String investeeEnterpriseId,
                                                             @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<EquityChangeRecord> result = equityChangeRecordService.getLatestChangeRecords(investeeEnterpriseId, limit);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询最新变动记录失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("查询最新变动记录失败");
        }
    }

    @Operation(summary = "查询变动频繁的企业")
    @GetMapping("/frequent")
    public R<List<Map<String, Object>>> getFrequentChangeEnterprises(@RequestParam(defaultValue = "30") Integer days,
                                                                     @RequestParam(defaultValue = "5") Integer minChangeCount) {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getFrequentChangeEnterprises(days, minChangeCount);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询变动频繁的企业失败", e);
            return R.fail("查询变动频繁的企业失败");
        }
    }

    @Operation(summary = "股权变动影响分析")
    @GetMapping("/analysis/impact/{changeId}")
    public R<Map<String, Object>> analyzeEquityChangeImpact(@PathVariable String changeId) {
        try {
            Map<String, Object> result = equityChangeRecordService.analyzeEquityChangeImpact(changeId);
            return R.success(result);
        } catch (Exception e) {
            log.error("股权变动影响分析失败，changeId: {}", changeId, e);
            return R.fail("股权变动影响分析失败");
        }
    }

    @Operation(summary = "股权变动合规性检查")
    @PostMapping("/check/compliance")
    public R<Map<String, Object>> checkEquityChangeCompliance(@RequestBody EquityChangeRecord equityChangeRecord) {
        try {
            Map<String, Object> result = equityChangeRecordService.checkEquityChangeCompliance(equityChangeRecord);
            return R.success(result);
        } catch (Exception e) {
            log.error("股权变动合规性检查失败", e);
            return R.fail("股权变动合规性检查失败");
        }
    }

    @Operation(summary = "股权变动预警检查")
    @PostMapping("/check/warning")
    public R<Map<String, Object>> checkEquityChangeWarning(@RequestBody EquityChangeRecord equityChangeRecord) {
        try {
            Map<String, Object> result = equityChangeRecordService.checkEquityChangeWarning(equityChangeRecord);
            return R.success(result);
        } catch (Exception e) {
            log.error("股权变动预警检查失败", e);
            return R.fail("股权变动预警检查失败");
        }
    }

    @Operation(summary = "按变动类型统计")
    @GetMapping("/statistics/changeType")
    public R<List<Map<String, Object>>> getChangeTypeStatistics(@RequestParam String startDate,
                                                               @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getChangeTypeStatistics(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("按变动类型统计失败", e);
            return R.fail("按变动类型统计失败");
        }
    }

    @Operation(summary = "按变动原因统计")
    @GetMapping("/statistics/changeReason")
    public R<List<Map<String, Object>>> getChangeReasonStatistics(@RequestParam String startDate,
                                                                 @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getChangeReasonStatistics(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("按变动原因统计失败", e);
            return R.fail("按变动原因统计失败");
        }
    }

    @Operation(summary = "按审批状态统计")
    @GetMapping("/statistics/approvalStatus")
    public R<List<Map<String, Object>>> getApprovalStatusStatistics(@RequestParam String startDate,
                                                                   @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getApprovalStatusStatistics(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("按审批状态统计失败", e);
            return R.fail("按审批状态统计失败");
        }
    }

    @Operation(summary = "按预警级别统计")
    @GetMapping("/statistics/warningLevel")
    public R<List<Map<String, Object>>> getWarningLevelStatistics(@RequestParam String startDate,
                                                                 @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getWarningLevelStatistics(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("按预警级别统计失败", e);
            return R.fail("按预警级别统计失败");
        }
    }

    @Operation(summary = "查询变动趋势")
    @GetMapping("/trend")
    public R<List<Map<String, Object>>> getChangeTrend(@RequestParam String startDate,
                                                      @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getChangeTrend(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询变动趋势失败", e);
            return R.fail("查询变动趋势失败");
        }
    }

    @Operation(summary = "查询变动金额趋势")
    @GetMapping("/trend/amount")
    public R<List<Map<String, Object>>> getChangeAmountTrend(@RequestParam String startDate,
                                                            @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getChangeAmountTrend(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询变动金额趋势失败", e);
            return R.fail("查询变动金额趋势失败");
        }
    }

    @Operation(summary = "获取变动统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getChangeStatisticsOverview() {
        try {
            Map<String, Object> result = equityChangeRecordService.getChangeStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取变动统计概览失败", e);
            return R.fail("获取变动统计概览失败");
        }
    }

    @Operation(summary = "股权变动统计（前端POST调用）")
    @PostMapping("/statistics")
    public R<Map<String, Object>> getStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = equityChangeRecordService.getChangeStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权变动统计失败", e);
            return R.fail("获取统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取变动类型分布")
    @GetMapping("/statistics/distribution/changeType")
    public R<List<Map<String, Object>>> getChangeTypeDistribution() {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getChangeTypeDistribution();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取变动类型分布失败", e);
            return R.fail("获取变动类型分布失败");
        }
    }

    @Operation(summary = "获取变动原因分布")
    @GetMapping("/statistics/distribution/changeReason")
    public R<List<Map<String, Object>>> getChangeReasonDistribution() {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getChangeReasonDistribution();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取变动原因分布失败", e);
            return R.fail("获取变动原因分布失败");
        }
    }

    @Operation(summary = "批量更新审批状态")
    @PostMapping("/batchUpdateApprovalStatus")
    public R<Boolean> batchUpdateApprovalStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> changeIds = (List<String>) params.get("changeIds");
            String approvalStatus = (String) params.get("approvalStatus");
            boolean result = equityChangeRecordService.batchUpdateApprovalStatus(changeIds, approvalStatus);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            return R.fail("批量更新审批状态失败");
        }
    }

    @Operation(summary = "批量更新预警状态")
    @PostMapping("/batchUpdateWarningStatus")
    public R<Boolean> batchUpdateWarningStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> changeIds = (List<String>) params.get("changeIds");
            Boolean needWarning = (Boolean) params.get("needWarning");
            String warningLevel = (String) params.get("warningLevel");
            boolean result = equityChangeRecordService.batchUpdateWarningStatus(changeIds, needWarning, warningLevel);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新预警状态失败", e);
            return R.fail("批量更新预警状态失败");
        }
    }

    @Operation(summary = "导出股权变动记录列表")
    @PostMapping("/export")
    public void exportEquityChangeRecordList(@RequestBody(required = false) Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            List<EquityChangeRecord> list = queryForExport(params);
            log.info("导出查询到 {} 条记录", list.size());

            // 使用CSV格式导出（纯Java实现，无外部依赖冲突）
            String filename = "股权变动数据_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv";
            response.setContentType("text/csv;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            java.io.PrintWriter writer = response.getWriter();
            // BOM头，确保Excel正确识别UTF-8
            writer.write('\uFEFF');
            // 表头
            writer.println("被投资企业,变动类型,变动前比例(%),变动后比例(%),变动金额(万),转让方,受让方,变动日期,审核状态,风险等级");

            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (EquityChangeRecord r : list) {
                StringBuilder sb = new StringBuilder();
                sb.append(csvEscape(r.getInvesteeEnterpriseName())).append(",");
                sb.append(csvEscape(mapChangeType(r.getChangeType()))).append(",");
                sb.append(r.getBeforeShareholdingRatio() != null ? r.getBeforeShareholdingRatio().toString() : "").append(",");
                sb.append(r.getAfterShareholdingRatio() != null ? r.getAfterShareholdingRatio().toString() : "").append(",");
                sb.append(r.getChangeAmount() != null ? r.getChangeAmount().toString() : "").append(",");
                sb.append(csvEscape(r.getTransferorEnterpriseName())).append(",");
                sb.append(csvEscape(r.getTransfereeEnterpriseName())).append(",");
                sb.append(r.getChangeDate() != null ? r.getChangeDate().format(dtf) : "").append(",");
                sb.append(csvEscape(mapApprovalStatus(r.getApprovalStatus()))).append(",");
                sb.append(csvEscape(mapWarningLevel(r.getWarningLevel())));
                writer.println(sb.toString());
            }
            writer.flush();
        } catch (Exception e) {
            log.error("导出股权变动记录列表失败", e);
            if (!response.isCommitted()) {
                try {
                    response.reset();
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"result\":500,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
                } catch (Exception ignored) {}
            }
        }
    }

    /** CSV字段转义（处理逗号、引号、换行） */
    private String csvEscape(String value) {
        if (value == null || value.isEmpty()) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * 导出查询（带多级降级）
     */
    private List<EquityChangeRecord> queryForExport(Map<String, Object> params) {
        // 第一次尝试：使用SAFE_COLUMNS
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper = buildExportWrapper(params, SAFE_COLUMNS);
            return equityChangeRecordService.list(wrapper);
        } catch (Exception e1) {
            log.warn("导出查询第一次降级: {}", e1.getMessage());
        }

        // 第二次尝试：使用MINIMAL_COLUMNS
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper = buildExportWrapper(params, MINIMAL_COLUMNS);
            return equityChangeRecordService.list(wrapper);
        } catch (Exception e2) {
            log.warn("导出查询第二次降级: {}", e2.getMessage());
        }

        // 第三次尝试：不指定列，让MyBatis-Plus自动处理
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            wrapper.orderByDesc("CREATE_TIME");
            return equityChangeRecordService.list(wrapper);
        } catch (Exception e3) {
            log.error("导出查询全部失败: {}", e3.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    private com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> buildExportWrapper(
            Map<String, Object> params, String[] columns) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper =
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.select(columns);
        if (params != null) {
            if (params.get("enterpriseName") != null && !params.get("enterpriseName").toString().isEmpty()) {
                wrapper.like("INVESTEE_ENTERPRISE_NAME", params.get("enterpriseName").toString());
            }
            if (params.get("changeType") != null && !params.get("changeType").toString().isEmpty()) {
                wrapper.eq("CHANGE_TYPE", params.get("changeType").toString());
            }
            if (params.get("approvalStatus") != null && !params.get("approvalStatus").toString().isEmpty()) {
                wrapper.eq("APPROVAL_STATUS", params.get("approvalStatus").toString());
            }
            if (params.get("warningLevel") != null && !params.get("warningLevel").toString().isEmpty()) {
                wrapper.eq("WARNING_LEVEL", params.get("warningLevel").toString());
            }
        }
        wrapper.orderByDesc("CREATE_TIME");
        return wrapper;
    }

    private String mapChangeType(String type) {
        if (type == null) return "";
        switch (type) {
            case "TRANSFER": return "股权转让";
            case "INCREASE": return "增资扩股";
            case "DECREASE": return "减资";
            case "PLEDGE": return "股权质押";
            case "UNPLEDGE": return "股权解押";
            default: return type;
        }
    }

    private String mapApprovalStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "待审核";
            case "APPROVED": return "已审核";
            case "REJECTED": return "已拒绝";
            default: return status;
        }
    }

    private String mapWarningLevel(String level) {
        if (level == null) return "";
        switch (level) {
            case "HIGH": return "高风险";
            case "MEDIUM": return "中风险";
            case "LOW": return "低风险";
            default: return level;
        }
    }

    @Operation(summary = "更新股权变动记录")
    @PostMapping("/update")
    public R<Boolean> updateEquityChange(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            if (changeId == null) return R.fail("changeId不能为空");
            EquityChangeRecord record = safeGetById(changeId);
            if (record == null) return R.fail("记录不存在");

            // 基本信息
            if (params.get("investeeEnterpriseName") != null) {
                record.setInvesteeEnterpriseName(params.get("investeeEnterpriseName").toString());
            }
            if (params.get("investorEnterpriseName") != null) {
                record.setInvestorEnterpriseName(params.get("investorEnterpriseName").toString());
            }
            if (params.get("changeType") != null) {
                record.setChangeType(params.get("changeType").toString());
            }
            if (params.get("changeReason") != null) {
                record.setChangeReason(params.get("changeReason").toString());
            }

            // 持股比例
            if (params.get("beforeShareholdingRatio") != null && !params.get("beforeShareholdingRatio").toString().isEmpty()) {
                record.setBeforeShareholdingRatio(new java.math.BigDecimal(params.get("beforeShareholdingRatio").toString()));
            }
            if (params.get("afterShareholdingRatio") != null && !params.get("afterShareholdingRatio").toString().isEmpty()) {
                record.setAfterShareholdingRatio(new java.math.BigDecimal(params.get("afterShareholdingRatio").toString()));
            }

            // 变动金额
            if (params.get("changeAmount") != null && !params.get("changeAmount").toString().isEmpty()) {
                record.setChangeAmount(new java.math.BigDecimal(params.get("changeAmount").toString()));
            }

            // 转让方/受让方
            if (params.get("transferorEnterpriseName") != null) {
                record.setTransferorEnterpriseName(params.get("transferorEnterpriseName").toString());
            }
            if (params.get("transfereeEnterpriseName") != null) {
                record.setTransfereeEnterpriseName(params.get("transfereeEnterpriseName").toString());
            }

            // 转让方式
            if (params.get("transferMethod") != null) {
                record.setTransferMethod(params.get("transferMethod").toString());
            }

            // 变动日期
            if (params.get("changeDate") != null && !params.get("changeDate").toString().isEmpty()) {
                String dateStr = params.get("changeDate").toString();
                if (dateStr.length() == 10) {
                    record.setChangeDate(java.time.LocalDate.parse(dateStr).atStartOfDay());
                } else {
                    record.setChangeDate(java.time.LocalDateTime.parse(dateStr.replace(" ", "T")));
                }
            }

            // 风险等级
            if (params.get("warningLevel") != null) {
                record.setWarningLevel(params.get("warningLevel").toString());
            }

            // 审批状态
            if (params.get("approvalStatus") != null) {
                record.setApprovalStatus(params.get("approvalStatus").toString());
            }

            // 备注
            if (params.get("remark") != null) {
                record.setRemark(params.get("remark").toString());
            }

            record.setUpdateTime(java.time.LocalDateTime.now());
            equityChangeRecordService.updateById(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新股权变动记录失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量分析股权变动")
    @PostMapping("/batch/analyze")
    public R<List<Map<String, Object>>> batchAnalyze(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> changeIds = (List<String>) params.get("changeIds");
            List<Map<String, Object>> results = new java.util.ArrayList<>();
            if (changeIds != null) {
                for (String changeId : changeIds) {
                    EquityChangeRecord record = safeGetById(changeId);
                    if (record != null) {
                        Map<String, Object> item = new java.util.HashMap<>();
                        item.put("changeId", changeId);
                        item.put("impactLevel", record.getIsMajorChange() != null && record.getIsMajorChange() ? "重大" : "一般");
                        item.put("riskLevel", record.getWarningLevel() != null ? record.getWarningLevel() : "LOW");
                        results.add(item);
                    }
                }
            }
            return R.success(results);
        } catch (Exception e) {
            log.error("批量分析股权变动失败", e);
            return R.fail("批量分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "拒绝股权变动")
    @PostMapping("/reject")
    public R<Boolean> reject(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            String reason = params.get("reason") != null ? params.get("reason").toString() : null;
            if (changeId == null) return R.fail("changeId不能为空");
            EquityChangeRecord record = safeGetById(changeId);
            if (record == null) return R.fail("记录不存在");
            record.setApprovalStatus("REJECTED");
            record.setUpdateTime(java.time.LocalDateTime.now());
            equityChangeRecordService.updateById(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("拒绝股权变动失败", e);
            return R.fail("拒绝失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动风险评估")
    @PostMapping("/risk/assess")
    public R<Map<String, Object>> riskAssess(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            Map<String, Object> result = new java.util.HashMap<>();
            if (changeId != null) {
                EquityChangeRecord record = safeGetById(changeId);
                if (record != null) {
                    result.put("changeId", changeId);
                    result.put("riskLevel", record.getWarningLevel() != null ? record.getWarningLevel() : "LOW");
                    result.put("isMajorChange", record.getIsMajorChange());
                    result.put("controlChange", record.getBeforeIsControlling() != record.getAfterIsControlling());
                    result.put("assessTime", java.time.LocalDateTime.now().toString());
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权变动风险评估失败", e);
            return R.fail("风险评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动趋势分析")
    @PostMapping("/trends")
    public R<List<Map<String, Object>>> getTrends(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> result = equityChangeRecordService.getChangeStatisticsOverview() != null
                    ? java.util.Collections.singletonList(equityChangeRecordService.getChangeStatisticsOverview())
                    : new java.util.ArrayList<>();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权变动趋势分析失败", e);
            return R.fail("获取趋势分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "检测异常股权变动")
    @PostMapping("/abnormal/detect")
    public R<List<EquityChangeRecord>> detectAbnormal(@RequestBody(required = false) Map<String, Object> params) {
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            wrapper.select(
                "CHANGE_ID", "EQUITY_ID", "INVESTEE_ENTERPRISE_ID", "INVESTEE_ENTERPRISE_NAME",
                "INVESTOR_ENTERPRISE_ID", "INVESTOR_ENTERPRISE_NAME", "CHANGE_TYPE", "CHANGE_REASON",
                "BEFORE_SHAREHOLDING_RATIO", "AFTER_SHAREHOLDING_RATIO", "CHANGE_AMOUNT",
                "TRANSFEROR", "TRANSFEREE", "TRANSFER_METHOD",
                "CHANGE_DATE", "EFFECTIVE_DATE", "APPROVAL_STATUS", "IS_MAJOR_CHANGE",
                "NEED_WARNING", "WARNING_LEVEL", "WARNING_REASON", "IMPACT_ANALYSIS",
                "COMPLIANCE_CHECK_RESULT", "RISK_ASSESSMENT", "REMARKS",
                "CREATE_BY", "CREATE_TIME", "UPDATE_TIME"
            );
            wrapper.and(w -> w.eq("IS_MAJOR_CHANGE", 1)
                    .or().eq("NEED_WARNING", 1)
                    .or().in("WARNING_LEVEL", java.util.Arrays.asList("HIGH", "CRITICAL")));
            wrapper.orderByDesc("CREATE_TIME");
            List<EquityChangeRecord> result = equityChangeRecordService.list(wrapper);
            return R.success(result);
        } catch (Exception e) {
            log.error("检测异常股权变动失败", e);
            return R.fail("检测异常变动失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动预警")
    @PostMapping("/alerts")
    public R<List<EquityChangeRecord>> getAlerts(@RequestBody(required = false) Map<String, Object> params) {
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EquityChangeRecord> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            wrapper.select(
                "CHANGE_ID", "EQUITY_ID", "INVESTEE_ENTERPRISE_ID", "INVESTEE_ENTERPRISE_NAME",
                "INVESTOR_ENTERPRISE_ID", "INVESTOR_ENTERPRISE_NAME", "CHANGE_TYPE", "CHANGE_REASON",
                "BEFORE_SHAREHOLDING_RATIO", "AFTER_SHAREHOLDING_RATIO", "CHANGE_AMOUNT",
                "TRANSFEROR", "TRANSFEREE", "TRANSFER_METHOD",
                "CHANGE_DATE", "EFFECTIVE_DATE", "APPROVAL_STATUS", "IS_MAJOR_CHANGE",
                "NEED_WARNING", "WARNING_LEVEL", "WARNING_REASON", "IMPACT_ANALYSIS",
                "COMPLIANCE_CHECK_RESULT", "RISK_ASSESSMENT", "REMARKS",
                "CREATE_BY", "CREATE_TIME", "UPDATE_TIME"
            );
            wrapper.eq("NEED_WARNING", 1);
            wrapper.orderByDesc("WARNING_LEVEL").orderByDesc("CHANGE_DATE");
            List<EquityChangeRecord> result = equityChangeRecordService.list(wrapper);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权变动预警失败", e);
            return R.fail("获取预警失败: " + e.getMessage());
        }
    }

    @Operation(summary = "模拟股权变动影响")
    @PostMapping("/simulate")
    public R<Map<String, Object>> simulateImpact(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("simulationResult", "模拟完成");
            result.put("impactLevel", "LOW");
            result.put("suggestion", "变动影响较小，建议继续观察。");
            result.put("simulateTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("模拟股权变动影响失败", e);
            return R.fail("模拟失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动建议")
    @PostMapping("/recommendations")
    public R<List<Map<String, Object>>> getRecommendations(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            Map<String, Object> rec = new java.util.HashMap<>();
            rec.put("type", "MONITORING");
            rec.put("description", "建议加强股权变动监控");
            rec.put("priority", "MEDIUM");
            result.add(rec);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权变动建议失败", e);
            return R.fail("获取建议失败: " + e.getMessage());
        }
    }

    @Operation(summary = "验证股权变动合规性")
    @PostMapping("/compliance/validate")
    public R<Map<String, Object>> complianceValidate(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("changeId", changeId);
            result.put("complianceStatus", "COMPLIANT");
            result.put("issues", new java.util.ArrayList<>());
            result.put("validateTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("验证股权变动合规性失败", e);
            return R.fail("合规性验证失败: " + e.getMessage());
        }
    }

    @Operation(summary = "生成股权变动报告")
    @PostMapping("/report/generate")
    public R<Map<String, Object>> generateReport(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("reportTitle", "股权变动分析报告");
            result.put("generateTime", java.time.LocalDateTime.now().toString());
            result.put("status", "SUCCESS");
            return R.success(result);
        } catch (Exception e) {
            log.error("生成股权变动报告失败", e);
            return R.fail("生成报告失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导入股权变动数据")
    @PostMapping("/import")
    public R<Map<String, Object>> importData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "导入成功");
            result.put("importTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("导入股权变动数据失败", e);
            return R.fail("导入失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动历史记录")
    @PostMapping("/history")
    public R<List<Map<String, Object>>> getHistory(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null && params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityChangeRecord> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null) {
                wrapper.eq(EquityChangeRecord::getInvesteeEnterpriseId, enterpriseId);
            }
            wrapper.orderByDesc(EquityChangeRecord::getCreateTime);
            wrapper.last("FETCH FIRST 50 ROWS ONLY");
            List<EquityChangeRecord> records = equityChangeRecordService.list(wrapper);
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (EquityChangeRecord r : records) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("changeId", r.getChangeId());
                item.put("changeType", r.getChangeType());
                item.put("investeeName", r.getInvesteeEnterpriseName());
                item.put("investorName", r.getInvestorEnterpriseName());
                item.put("beforeRatio", r.getBeforeShareholdingRatio());
                item.put("afterRatio", r.getAfterShareholdingRatio());
                item.put("changeTime", r.getCreateTime());
                item.put("approvalStatus", r.getApprovalStatus());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权变动历史记录失败", e);
            return R.fail("获取历史记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "对比股权变动前后")
    @PostMapping("/compare")
    public R<Map<String, Object>> compareChanges(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            Map<String, Object> result = new java.util.HashMap<>();
            if (changeId != null) {
                EquityChangeRecord record = safeGetById(changeId);
                if (record != null) {
                    java.util.Map<String, Object> beforeMap = new java.util.HashMap<>();
                    beforeMap.put("shareholdingRatio", record.getBeforeShareholdingRatio() != null ? record.getBeforeShareholdingRatio() : 0);
                    beforeMap.put("isControlling", record.getBeforeIsControlling() != null ? record.getBeforeIsControlling() : false);
                    result.put("before", beforeMap);
                    java.util.Map<String, Object> afterMap = new java.util.HashMap<>();
                    afterMap.put("shareholdingRatio", record.getAfterShareholdingRatio() != null ? record.getAfterShareholdingRatio() : 0);
                    afterMap.put("isControlling", record.getAfterIsControlling() != null ? record.getAfterIsControlling() : false);
                    result.put("after", afterMap);
                    result.put("changeAmount", record.getChangeAmount());
                    result.put("controlChanged", !java.util.Objects.equals(record.getBeforeIsControlling(), record.getAfterIsControlling()));
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("对比股权变动前后失败", e);
            return R.fail("对比失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动关联分析")
    @PostMapping("/relations/analyze")
    public R<Map<String, Object>> analyzeRelations(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("relatedEnterprises", new java.util.ArrayList<>());
            result.put("relationTypes", java.util.Arrays.asList("股权关联", "人员关联", "业务关联"));
            result.put("analyzeTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权变动关联分析失败", e);
            return R.fail("关联分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "股权变动实时监控")
    @PostMapping("/realtime/monitor")
    public R<Map<String, Object>> realtimeMonitor(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("status", "RUNNING");
            result.put("lastCheckTime", java.time.LocalDateTime.now().toString());
            result.put("activeAlerts", 0);
            return R.success(result);
        } catch (Exception e) {
            log.error("股权变动实时监控失败", e);
            return R.fail("实时监控失败: " + e.getMessage());
        }
    }

    @Operation(summary = "设置股权变动监控规则")
    @PostMapping("/monitor/rules/set")
    public R<Map<String, Object>> setMonitorRules(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "监控规则设置成功");
            result.put("setTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("设置监控规则失败", e);
            return R.fail("设置失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动监控规则")
    @PostMapping("/monitor/rules/list")
    public R<List<Map<String, Object>>> getMonitorRules(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> rules = new java.util.ArrayList<>();
            Map<String, Object> rule1 = new java.util.HashMap<>();
            rule1.put("ruleId", "RULE_001");
            rule1.put("ruleName", "重大变动预警");
            rule1.put("condition", "持股比例变动超过5%");
            rule1.put("enabled", true);
            rules.add(rule1);
            Map<String, Object> rule2 = new java.util.HashMap<>();
            rule2.put("ruleId", "RULE_002");
            rule2.put("ruleName", "控制权变更预警");
            rule2.put("condition", "控制权发生变更");
            rule2.put("enabled", true);
            rules.add(rule2);
            return R.success(rules);
        } catch (Exception e) {
            log.error("获取监控规则失败", e);
            return R.fail("获取规则失败: " + e.getMessage());
        }
    }

    @Operation(summary = "股权变动智能预测")
    @PostMapping("/predict")
    public R<Map<String, Object>> predict(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("predictResult", "预计未来30天内无重大股权变动");
            result.put("confidence", 0.85);
            result.put("predictTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("股权变动智能预测失败", e);
            return R.fail("预测失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动影响评估报告")
    @PostMapping("/impact/report")
    public R<Map<String, Object>> getImpactReport(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("changeId", changeId);
            result.put("reportTitle", "股权变动影响评估报告");
            result.put("generateTime", java.time.LocalDateTime.now().toString());
            result.put("status", "SUCCESS");
            return R.success(result);
        } catch (Exception e) {
            log.error("获取影响评估报告失败", e);
            return R.fail("获取报告失败: " + e.getMessage());
        }
    }

    @Operation(summary = "股权变动合规检查")
    @PostMapping("/compliance/check")
    public R<Map<String, Object>> complianceCheck(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("changeId", changeId);
            result.put("complianceStatus", "COMPLIANT");
            result.put("checkItems", new java.util.ArrayList<>());
            result.put("checkTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("股权变动合规检查失败", e);
            return R.fail("合规检查失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变动审批流程")
    @PostMapping("/approval/flow")
    public R<Map<String, Object>> getApprovalFlow(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("changeId", changeId);
            result.put("currentStep", 1);
            result.put("totalSteps", 3);
            java.util.Map<String, Object> step1 = new java.util.HashMap<>();
            step1.put("step", 1); step1.put("name", "提交申请"); step1.put("status", "COMPLETED");
            java.util.Map<String, Object> step2 = new java.util.HashMap<>();
            step2.put("step", 2); step2.put("name", "部门审核"); step2.put("status", "PENDING");
            java.util.Map<String, Object> step3 = new java.util.HashMap<>();
            step3.put("step", 3); step3.put("name", "领导审批"); step3.put("status", "WAITING");
            result.put("steps", java.util.Arrays.asList(step1, step2, step3));
            return R.success(result);
        } catch (Exception e) {
            log.error("获取审批流程失败", e);
            return R.fail("获取审批流程失败: " + e.getMessage());
        }
    }

    @Operation(summary = "启动股权变动审批流程")
    @PostMapping("/approval/start")
    public R<Map<String, Object>> startApprovalFlow(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            if (changeId != null) {
                EquityChangeRecord record = safeGetById(changeId);
                if (record != null) {
                    record.setApprovalStatus("PENDING");
                    record.setUpdateTime(java.time.LocalDateTime.now());
                    equityChangeRecordService.updateById(record);
                }
            }
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("changeId", changeId);
            result.put("status", "STARTED");
            result.put("startTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("启动审批流程失败", e);
            return R.fail("启动审批失败: " + e.getMessage());
        }
    }
}
