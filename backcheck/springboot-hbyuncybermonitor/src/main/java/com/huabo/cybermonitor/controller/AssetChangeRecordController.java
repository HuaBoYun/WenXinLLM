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

import com.huabo.cybermonitor.entity.AssetChangeRecord;
import com.huabo.cybermonitor.service.IAssetChangeRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.AssetChangeRecordQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 资产变动记录控制器
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@RestController
@RequestMapping("/v1/supervision/asset/change")
@Tag(name = "资产变动记录管理", description = "资产变动监控、资产流向追踪、资产运营分析相关接口")
public class AssetChangeRecordController {

	private static final Logger log = LoggerFactory.getLogger(AssetChangeRecordController.class);

    @Autowired
    private IAssetChangeRecordService assetChangeRecordService;

    @Operation(summary = "分页查询资产变动记录列表", description = "支持多条件组合查询资产变动记录")
    @PostMapping("/list")
    public R<PageResult<AssetChangeRecord>> getAssetChangeRecordList(@RequestBody AssetChangeRecordQueryVO queryVO) {
        try {
            PageResult<AssetChangeRecord> result = assetChangeRecordService.getAssetChangeRecordList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询资产变动记录列表失败", e);
            return R.fail("查询资产变动记录列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询资产变动记录", description = "根据变动记录ID查询详细信息")
    @GetMapping("/{changeId}")
    public R<AssetChangeRecord> getAssetChangeRecordById(@Parameter(description = "变动记录ID") @PathVariable String changeId) {
        try {
            AssetChangeRecord changeRecord = assetChangeRecordService.getById(changeId);
            return R.success(changeRecord);
        } catch (Exception e) {
            log.error("根据ID查询资产变动记录失败: {}", changeId, e);
            return R.fail("查询资产变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增资产变动记录", description = "新增资产变动记录")
    @PostMapping("/add")
    public R<Boolean> addAssetChangeRecord(@RequestBody AssetChangeRecord changeRecord) {
        try {
            boolean result = assetChangeRecordService.save(changeRecord);
            return R.success(result);
        } catch (Exception e) {
            log.error("新增资产变动记录失败", e);
            return R.fail("新增资产变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改资产变动记录", description = "修改资产变动记录")
    @PostMapping("/update")
    public R<Boolean> updateAssetChangeRecord(@RequestBody AssetChangeRecord changeRecord) {
        try {
            boolean result = assetChangeRecordService.updateById(changeRecord);
            return R.success(result);
        } catch (Exception e) {
            log.error("修改资产变动记录失败", e);
            return R.fail("修改资产变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除资产变动记录", description = "根据ID删除资产变动记录")
    @DeleteMapping("/{changeId}")
    public R<Boolean> deleteAssetChangeRecord(@Parameter(description = "变动记录ID") @PathVariable String changeId) {
        try {
            boolean result = assetChangeRecordService.removeById(changeId);
            return R.success(result);
        } catch (Exception e) {
            log.error("删除资产变动记录失败: {}", changeId, e);
            return R.fail("删除资产变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据资产ID查询变动记录", description = "查询指定资产的所有变动记录")
    @GetMapping("/asset/{assetId}")
    public R<List<AssetChangeRecord>> getChangeRecordsByAssetId(@Parameter(description = "资产ID") @PathVariable String assetId) {
        try {
            List<AssetChangeRecord> result = assetChangeRecordService.getChangeRecordsByAssetId(assetId);
            return R.success(result);
        } catch (Exception e) {
            log.error("根据资产ID查询变动记录失败: {}", assetId, e);
            return R.fail("查询变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据企业ID查询变动记录", description = "查询指定企业的所有资产变动记录")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<List<AssetChangeRecord>> getChangeRecordsByEnterpriseId(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            List<AssetChangeRecord> result = assetChangeRecordService.getChangeRecordsByEnterpriseId(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("根据企业ID查询变动记录失败: {}", enterpriseId, e);
            return R.fail("查询变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询重大变动记录", description = "查询重大资产变动记录")
    @GetMapping("/major")
    public R<List<AssetChangeRecord>> getMajorChangeRecords(@Parameter(description = "是否重大变动") @RequestParam Boolean isMajorChange) {
        try {
            List<AssetChangeRecord> result = assetChangeRecordService.getMajorChangeRecords(isMajorChange);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询重大变动记录失败", e);
            return R.fail("查询重大变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询预警变动记录", description = "查询需要预警的资产变动记录")
    @GetMapping("/warning")
    public R<List<AssetChangeRecord>> getWarningChangeRecords(@Parameter(description = "是否需要预警") @RequestParam Boolean needWarning) {
        try {
            List<AssetChangeRecord> result = assetChangeRecordService.getWarningChangeRecords(needWarning);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询预警变动记录失败", e);
            return R.fail("查询预警变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询最新变动记录", description = "查询企业最新的资产变动记录")
    @GetMapping("/latest/{enterpriseId}")
    public R<List<AssetChangeRecord>> getLatestChangeRecords(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "记录数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<AssetChangeRecord> result = assetChangeRecordService.getLatestChangeRecords(enterpriseId, limit);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询最新变动记录失败: {}", enterpriseId, e);
            return R.fail("查询最新变动记录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询变动频繁的资产", description = "查询在指定时间内变动频繁的资产")
    @GetMapping("/frequent")
    public R<List<Map<String, Object>>> getFrequentChangeAssets(
            @Parameter(description = "天数") @RequestParam(defaultValue = "30") Integer days,
            @Parameter(description = "最小变动次数") @RequestParam(defaultValue = "5") Integer minChangeCount) {
        try {
            List<Map<String, Object>> result = assetChangeRecordService.getFrequentChangeAssets(days, minChangeCount);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询变动频繁的资产失败", e);
            return R.fail("查询变动频繁的资产失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产变动监控", description = "监控企业资产变动情况")
    @GetMapping("/monitor/{enterpriseId}")
    public R<Map<String, Object>> monitorAssetChanges(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            Map<String, Object> result = assetChangeRecordService.monitorAssetChanges(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产变动监控失败: {}", enterpriseId, e);
            return R.fail("资产变动监控失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产流向分析", description = "分析企业资产流向")
    @GetMapping("/analysis/flow/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetFlow(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            Map<String, Object> result = assetChangeRecordService.analyzeAssetFlow(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产流向分析失败: {}", enterpriseId, e);
            return R.fail("资产流向分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产运营效率分析", description = "分析企业资产运营效率")
    @GetMapping("/analysis/operational-efficiency/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetOperationalEfficiency(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            Map<String, Object> result = assetChangeRecordService.analyzeAssetOperationalEfficiency(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产运营效率分析失败: {}", enterpriseId, e);
            return R.fail("资产运营效率分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产变动影响分析", description = "分析指定变动的影响")
    @GetMapping("/analysis/impact/{changeId}")
    public R<Map<String, Object>> analyzeAssetChangeImpact(@Parameter(description = "变动记录ID") @PathVariable String changeId) {
        try {
            Map<String, Object> result = assetChangeRecordService.analyzeAssetChangeImpact(changeId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产变动影响分析失败: {}", changeId, e);
            return R.fail("资产变动影响分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产变动合规性检查", description = "检查企业资产变动的合规性")
    @PostMapping("/check/compliance")
    public R<Map<String, Object>> checkAssetChangeCompliance(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            String startDate = params.get("startDate");
            String endDate = params.get("endDate");
            Map<String, Object> result = assetChangeRecordService.checkAssetChangeCompliance(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产变动合规性检查失败", e);
            return R.fail("资产变动合规性检查失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产变动风险评估", description = "评估企业资产变动风险")
    @PostMapping("/assessment/risk")
    public R<Map<String, Object>> assessAssetChangeRisk(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            String startDate = params.get("startDate");
            String endDate = params.get("endDate");
            Map<String, Object> result = assetChangeRecordService.assessAssetChangeRisk(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产变动风险评估失败", e);
            return R.fail("资产变动风险评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产处置效益分析", description = "分析企业资产处置效益")
    @GetMapping("/analysis/disposal-benefit/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetDisposalBenefit(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            Map<String, Object> result = assetChangeRecordService.analyzeAssetDisposalBenefit(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产处置效益分析失败: {}", enterpriseId, e);
            return R.fail("资产处置效益分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产购置分析", description = "分析企业资产购置情况")
    @GetMapping("/analysis/acquisition/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetAcquisition(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            Map<String, Object> result = assetChangeRecordService.analyzeAssetAcquisition(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产购置分析失败: {}", enterpriseId, e);
            return R.fail("资产购置分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产转移分析", description = "分析企业资产转移情况")
    @GetMapping("/analysis/transfer/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetTransfer(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            Map<String, Object> result = assetChangeRecordService.analyzeAssetTransfer(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产转移分析失败: {}", enterpriseId, e);
            return R.fail("资产转移分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按变动类型统计", description = "按变动类型统计资产变动情况")
    @GetMapping("/statistics/change-type")
    public R<List<Map<String, Object>>> getChangeTypeStatistics(
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = assetChangeRecordService.getChangeTypeStatistics(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("按变动类型统计失败", e);
            return R.fail("按变动类型统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按变动原因统计", description = "按变动原因统计资产变动情况")
    @GetMapping("/statistics/change-reason")
    public R<List<Map<String, Object>>> getChangeReasonStatistics(
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = assetChangeRecordService.getChangeReasonStatistics(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("按变动原因统计失败", e);
            return R.fail("按变动原因统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按审批状态统计", description = "按审批状态统计资产变动情况")
    @GetMapping("/statistics/approval-status")
    public R<List<Map<String, Object>>> getApprovalStatusStatistics(
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = assetChangeRecordService.getApprovalStatusStatistics(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("按审批状态统计失败", e);
            return R.fail("按审批状态统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询变动趋势", description = "查询资产变动趋势")
    @GetMapping("/trend")
    public R<List<Map<String, Object>>> getChangeTrend(
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = assetChangeRecordService.getChangeTrend(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询变动趋势失败", e);
            return R.fail("查询变动趋势失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询变动金额趋势", description = "查询资产变动金额趋势")
    @GetMapping("/trend/amount")
    public R<List<Map<String, Object>>> getChangeAmountTrend(
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = assetChangeRecordService.getChangeAmountTrend(startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询变动金额趋势失败", e);
            return R.fail("查询变动金额趋势失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量更新审批状态", description = "批量更新多个变动记录的审批状态")
    @PostMapping("/batch/update-approval-status")
    public R<Boolean> batchUpdateApprovalStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> changeIds = (List<String>) params.get("changeIds");
            String approvalStatus = (String) params.get("approvalStatus");
            boolean result = assetChangeRecordService.batchUpdateApprovalStatus(changeIds, approvalStatus);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            return R.fail("批量更新审批状态失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取变动统计概览", description = "获取资产变动统计概览信息")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getChangeStatisticsOverview() {
        try {
            Map<String, Object> result = assetChangeRecordService.getChangeStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取变动统计概览失败", e);
            return R.fail("获取变动统计概览失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出资产变动记录列表", description = "导出资产变动记录列表到Excel")
    @PostMapping("/export")
    public R<List<Map<String, Object>>> exportAssetChangeRecordList(@RequestBody AssetChangeRecordQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = assetChangeRecordService.exportAssetChangeRecordList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出资产变动记录列表失败", e);
            return R.fail("导出资产变动记录列表失败: " + e.getMessage());
        }
    }

}
