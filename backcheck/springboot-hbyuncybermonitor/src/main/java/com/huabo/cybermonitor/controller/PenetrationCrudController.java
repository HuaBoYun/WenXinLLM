package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 穿透监管CRUD控制器
 * 包含：三表比对、交易审查、资产流向、资产映射、交叉持股、资产质量、控制链分析、受益所有人、投资决策
 */
@Slf4j
@RestController
@RequestMapping("/v1/supervision")
@Tag(name = "穿透监管CRUD", description = "穿透监管相关数据的增删改查接口")
public class PenetrationCrudController {

    @Autowired
    private TblThreeTableCompareMapper threeTableCompareMapper;
    @Autowired
    private TblTradeReviewMapper tradeReviewMapper;
    @Autowired
    private TblAssetFlowMapper assetFlowMapper;
    @Autowired
    private TblAssetMappingMapper assetMappingMapper;
    @Autowired
    private TblCrossHoldingMapper crossHoldingMapper;
    @Autowired
    private TblAssetQualityMapper assetQualityMapper;
    @Autowired
    private TblControlChainAnalysisMapper controlChainAnalysisMapper;
    @Autowired
    private TblBeneficialOwnerMapper beneficialOwnerMapper;
    @Autowired
    private TblInvestmentDecisionMapper investmentDecisionMapper;

    // ==================== 三表比对 ====================
    // 注意: list接口由PropertySupervisionController提供(/three-table-compare/list)

    @Operation(summary = "三表比对-详情")
    @GetMapping("/property/three-table-compare/{id}")
    public R<TblThreeTableCompare> threeTableCompareDetail(@PathVariable String id) {
        try { return R.success(threeTableCompareMapper.selectById(id)); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "三表比对-新增")
    @PostMapping("/property/three-table-compare/add")
    public R<Boolean> threeTableCompareAdd(@RequestBody TblThreeTableCompare record) {
        try { record.setCreateTime(LocalDateTime.now()); threeTableCompareMapper.insert(record); return R.success(true); }
        catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "三表比对-更新")
    @PostMapping("/property/three-table-compare/update")
    public R<Boolean> threeTableCompareUpdate(@RequestBody TblThreeTableCompare record) {
        try { record.setUpdateTime(LocalDateTime.now()); threeTableCompareMapper.updateById(record); return R.success(true); }
        catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "三表比对-删除")
    @DeleteMapping("/property/three-table-compare/{id}")
    public R<Boolean> threeTableCompareDelete(@PathVariable String id) {
        try { threeTableCompareMapper.deleteById(id); return R.success(true); }
        catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "三表比对-批量删除")
    @PostMapping("/property/three-table-compare/batch/delete")
    public R<Boolean> threeTableCompareBatchDelete(@RequestBody List<String> ids) {
        try { threeTableCompareMapper.deleteBatchIds(ids); return R.success(true); }
        catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); }
    }

    // ==================== 交易审查 ====================

    @Operation(summary = "交易审查-分页列表")
    @PostMapping("/property/trade-review/list")
    public R<PageResult<TblTradeReview>> tradeReviewList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<TblTradeReview> w = new LambdaQueryWrapper<>();
            String companyName = params.get("companyName") != null ? params.get("companyName").toString() : null;
            String transMethod = params.get("transMethod") != null ? params.get("transMethod").toString() : null;
            String complianceStatus = params.get("complianceStatus") != null ? params.get("complianceStatus").toString() : null;
            if (StringUtils.isNotBlank(companyName)) { w.like(TblTradeReview::getCompanyName, companyName); }
            if (StringUtils.isNotBlank(transMethod)) { w.eq(TblTradeReview::getTransMethod, transMethod); }
            if (StringUtils.isNotBlank(complianceStatus)) { w.eq(TblTradeReview::getComplianceStatus, complianceStatus); }
            w.orderByDesc(TblTradeReview::getCreateTime);
            Page<TblTradeReview> page = tradeReviewMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("交易审查查询失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "交易审查-详情")
    @GetMapping("/property/trade-review/{id}")
    public R<TblTradeReview> tradeReviewDetail(@PathVariable String id) {
        try { return R.success(tradeReviewMapper.selectById(id)); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "交易审查-新增")
    @PostMapping("/property/trade-review/add")
    public R<Boolean> tradeReviewAdd(@RequestBody TblTradeReview record) {
        try {
            // 手动生成短ID，避免UUID过长导致达梦数据库溢出
            record.setReviewId("TR" + System.currentTimeMillis());
            record.setCreateTime(LocalDateTime.now());
            // 清理空字符串字段，避免日期类型转换异常
            if (record.getTransDate() == null) {
                record.setTransDate(null);
            }
            tradeReviewMapper.insert(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("交易审查新增失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "交易审查-更新")
    @PostMapping("/property/trade-review/update")
    public R<Boolean> tradeReviewUpdate(@RequestBody TblTradeReview record) {
        try { record.setUpdateTime(LocalDateTime.now()); tradeReviewMapper.updateById(record); return R.success(true); }
        catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "交易审查-删除")
    @DeleteMapping("/property/trade-review/{id}")
    public R<Boolean> tradeReviewDelete(@PathVariable String id) {
        try { tradeReviewMapper.deleteById(id); return R.success(true); }
        catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "交易审查-批量删除")
    @PostMapping("/property/trade-review/batch/delete")
    public R<Boolean> tradeReviewBatchDelete(@RequestBody List<String> ids) {
        try { tradeReviewMapper.deleteBatchIds(ids); return R.success(true); }
        catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); }
    }
}
