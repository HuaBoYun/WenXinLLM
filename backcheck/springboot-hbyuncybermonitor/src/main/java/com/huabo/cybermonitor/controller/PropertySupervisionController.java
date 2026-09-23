package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.service.IThreeTableCompareService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.ThreeTableCompareDetailVO;
import com.huabo.cybermonitor.vo.ThreeTableCompareQueryVO;
import com.huabo.cybermonitor.vo.ThreeTableDispatchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Tag(name = "产权穿透式监管-综合接口", description = "产权穿透式监管综合接口(股权穿透/变动管理/参股监控/预警/驾驶舱)")
@RestController
@RequestMapping("/v1/supervision/property")
@Slf4j
public class PropertySupervisionController {

    @Autowired private TblPropertyRightMapper propertyRightMapper;
    @Autowired private TblPropertyTransactionMapper propertyTransactionMapper;
    @Autowired private GzctPropertyChangeMapper propertyChangeMapper;
    @Autowired private GzctPropertyWarningMapper propertyWarningMapper;
    @Autowired private GzctShareholdingCompanyMapper shareholdingCompanyMapper;
    @Autowired private EquityStructureMapper equityStructureMapper;
    @Autowired private EquityChangeRecordMapper equityChangeRecordMapper;
    @Autowired private TblThreeTableCompareMapper threeTableCompareMapper;
    @Autowired private IThreeTableCompareService threeTableCompareService;
    @Autowired private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    private <T> PageResult<T> buildPageResult(Page<T> r) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
        pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
        pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
        return pr;
    }

    // ==================== 台账管理(补充接口) ====================
    // 注意：产权登记CRUD接口（list, {id}, add, update, delete, batch/delete, statistics, export）
    // 已由 PropertyRightController 提供，此处不再重复定义

    // ==================== 股权穿透 ====================

    @Operation(summary = "股权层级树(递归结构)")
    @GetMapping("/equity/tree/{companyId}")
    public R<Map<String, Object>> equityTree(@PathVariable String companyId, @RequestParam(required = false, defaultValue = "5") Integer depth) {
        try {
            // 查找根企业
            TblPropertyRight root = propertyRightMapper.selectById(companyId);
            if (root == null) {
                // 如果按ID查不到，尝试按companyId查
                List<TblPropertyRight> roots = propertyRightMapper.selectList(
                    new LambdaQueryWrapper<TblPropertyRight>().eq(TblPropertyRight::getCompanyId, companyId));
                if (!roots.isEmpty()) root = roots.get(0);
            }
            if (root == null) {
                // 创建虚拟根节点
                Map<String, Object> result = new HashMap<>();
                result.put("companyId", companyId);
                result.put("companyName", "集团总部");
                result.put("equityLevel", 1);
                result.put("equityRatio", 100);
                result.put("businessStatus", "NORMAL");
                result.put("isWarning", false);
                result.put("children", buildEquityTree(companyId, 1, depth));
                return R.success(result);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", root.getCompanyId());
            result.put("companyName", root.getCompanyName());
            result.put("equityLevel", root.getEquityLevel() != null ? root.getEquityLevel() : 1);
            result.put("equityRatio", root.getEquityRatio());
            result.put("businessStatus", root.getBusinessStatus());
            result.put("isWarning", root.getEquityLevel() != null && root.getEquityLevel() > 5);
            result.put("children", buildEquityTree(root.getCompanyId(), root.getEquityLevel() != null ? root.getEquityLevel() : 1, depth));
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    /** 递归构建股权树 */
    private List<Map<String, Object>> buildEquityTree(String parentCompanyId, int currentLevel, int maxDepth) {
        List<Map<String, Object>> children = new ArrayList<>();
        if (currentLevel >= maxDepth) return children;
        List<TblPropertyRight> subs = propertyRightMapper.selectList(
            new LambdaQueryWrapper<TblPropertyRight>().eq(TblPropertyRight::getParentCompanyId, parentCompanyId));
        for (TblPropertyRight sub : subs) {
            Map<String, Object> node = new HashMap<>();
            node.put("companyId", sub.getCompanyId());
            node.put("companyName", sub.getCompanyName());
            node.put("equityLevel", sub.getEquityLevel() != null ? sub.getEquityLevel() : currentLevel + 1);
            node.put("equityRatio", sub.getEquityRatio());
            node.put("businessStatus", sub.getBusinessStatus());
            node.put("isWarning", sub.getEquityLevel() != null && sub.getEquityLevel() > 5);
            node.put("children", buildEquityTree(sub.getCompanyId(), sub.getEquityLevel() != null ? sub.getEquityLevel() : currentLevel + 1, maxDepth));
            children.add(node);
        }
        return children;
    }

    @Operation(summary = "股权层级图谱数据")
    @GetMapping("/equity/graph/{companyId}")
    public R<Map<String, Object>> equityGraph(@PathVariable String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> nodes = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();
            List<TblPropertyRight> all = propertyRightMapper.selectList(null);
            for (TblPropertyRight pr : all) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", pr.getPropertyId()); node.put("name", pr.getCompanyName());
                node.put("equityRatio", pr.getEquityRatio()); nodes.add(node);
                if (StringUtils.isNotBlank(pr.getParentCompanyId())) {
                    Map<String, Object> link = new HashMap<>();
                    link.put("source", pr.getParentCompanyId()); link.put("target", pr.getPropertyId());
                    link.put("equityRatio", pr.getEquityRatio()); links.add(link);
                }
            }
            result.put("nodes", nodes); result.put("links", links);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "层级统计概览(含顶层企业列表)")
    @GetMapping("/equity/stats")
    public R<Map<String, Object>> equityStats() {
        try {
            Map<String, Object> result = new HashMap<>();
            Long totalProperties = propertyRightMapper.selectCount(null);
            result.put("totalProperties", totalProperties);

            // 层级分布
            List<TblPropertyRight> all = propertyRightMapper.selectList(null);
            List<Map<String, Object>> levelDistribution = new ArrayList<>();
            Map<Integer, Long> levelCountMap = new HashMap<>();
            for (TblPropertyRight pr : all) {
                int level = pr.getEquityLevel() != null ? pr.getEquityLevel() : 1;
                levelCountMap.merge(level, 1L, Long::sum);
            }
            for (Map.Entry<Integer, Long> entry : levelCountMap.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("level", entry.getKey());
                item.put("count", entry.getValue());
                levelDistribution.add(item);
            }
            levelDistribution.sort((a, b) -> Integer.compare((int) a.get("level"), (int) b.get("level")));
            result.put("levelDistribution", levelDistribution);

            // 顶层企业列表(股权层级=1或无上级企业的)
            List<TblPropertyRight> rootList = propertyRightMapper.selectList(
                new LambdaQueryWrapper<TblPropertyRight>()
                    .and(w -> w.isNull(TblPropertyRight::getParentCompanyId)
                        .or().eq(TblPropertyRight::getParentCompanyId, "")
                        .or().eq(TblPropertyRight::getEquityLevel, 1))
            );
            List<Map<String, Object>> rootListResult = new ArrayList<>();
            for (TblPropertyRight root : rootList) {
                Map<String, Object> rootItem = new HashMap<>();
                rootItem.put("companyId", root.getCompanyId());
                rootItem.put("companyName", root.getCompanyName());
                rootItem.put("equityRatio", root.getEquityRatio());
                rootListResult.add(rootItem);
            }
            result.put("rootList", rootListResult);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 变动管理 ====================

    @Operation(summary = "产权变动记录列表")
    @PostMapping("/change/list")
    public R<PageResult<GzctPropertyChange>> changeList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctPropertyChange> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctPropertyChange::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("changeType") != null && StringUtils.isNotBlank(params.get("changeType").toString())) {
                w.eq(GzctPropertyChange::getChangeType, params.get("changeType").toString());
            }
            if (params.get("approvalStatus") != null && StringUtils.isNotBlank(params.get("approvalStatus").toString())) {
                w.eq(GzctPropertyChange::getApprovalStatus, params.get("approvalStatus").toString());
            }
            w.orderByDesc(GzctPropertyChange::getCreateTime);
            Page<GzctPropertyChange> r = propertyChangeMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增产权变动登记")
    @PostMapping("/change/save")
    public R<Boolean> saveChange(@RequestBody GzctPropertyChange record) {
        try {
            if (StringUtils.isEmpty(record.getChangeId())) {
                record.setCreateTime(LocalDateTime.now());
                propertyChangeMapper.insert(record);
            } else {
                record.setUpdateTime(LocalDateTime.now());
                propertyChangeMapper.updateById(record);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除产权变动记录")
    @PostMapping("/change/delete")
    public R<Boolean> deleteChange(@RequestBody Map<String, Object> params) {
        try {
            String changeId = params.get("changeId") != null ? params.get("changeId").toString() : null;
            if (StringUtils.isEmpty(changeId)) {
                return R.fail("删除失败：changeId不能为空");
            }
            int rows = propertyChangeMapper.deleteById(changeId);
            return rows > 0 ? R.success(true) : R.fail("删除失败：记录不存在");
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "变动时间轴")
    @GetMapping("/change/timeline/{companyName}")
    public R<List<GzctPropertyChange>> changeTimeline(@PathVariable String companyName) {
        try {
            List<GzctPropertyChange> list = propertyChangeMapper.selectList(
                new LambdaQueryWrapper<GzctPropertyChange>()
                    .eq(GzctPropertyChange::getCompanyName, companyName)
                    .orderByDesc(GzctPropertyChange::getChangeDate));
            return R.success(list);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 交易合规 ====================

    @Operation(summary = "产权交易列表(分页+合规字段)")
    @PostMapping("/transaction/list")
    public R<PageResult<Map<String, Object>>> transactionList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<TblPropertyTransaction> w = new LambdaQueryWrapper<>();
            if (params.get("transactionType") != null && StringUtils.isNotBlank(params.get("transactionType").toString())) {
                w.eq(TblPropertyTransaction::getTransactionType, params.get("transactionType").toString());
            }
            if (params.get("approvalStatus") != null && StringUtils.isNotBlank(params.get("approvalStatus").toString())) {
                w.eq(TblPropertyTransaction::getApprovalStatus, params.get("approvalStatus").toString());
            }
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                String companyNameParam = params.get("companyName").toString();
                // 同时查询交易表自身companyName和关联产权登记表
                w.and(wrapper -> {
                    wrapper.like(TblPropertyTransaction::getCompanyName, companyNameParam);
                    LambdaQueryWrapper<TblPropertyRight> prw = new LambdaQueryWrapper<>();
                    prw.like(TblPropertyRight::getCompanyName, companyNameParam);
                    prw.select(TblPropertyRight::getPropertyId);
                    List<TblPropertyRight> prList = propertyRightMapper.selectList(prw);
                    List<String> propertyIds = new ArrayList<>();
                    for (TblPropertyRight pr : prList) { propertyIds.add(pr.getPropertyId()); }
                    if (!propertyIds.isEmpty()) {
                        wrapper.or().in(TblPropertyTransaction::getPropertyId, propertyIds);
                    }
                });
            }
            w.orderByDesc(TblPropertyTransaction::getCreateTime);
            Page<TblPropertyTransaction> r = propertyTransactionMapper.selectPage(new Page<>(pn, ps), w);

            // 将交易数据映射为含合规字段的格式
            List<Map<String, Object>> enriched = new ArrayList<>();
            for (TblPropertyTransaction t : r.getRecords()) {
                Map<String, Object> item = new HashMap<>();
                item.put("transactionId", t.getTransactionId());
                item.put("transNo", t.getTransactionId());
                // 通过propertyId获取公司名称，如果关联查询无结果则使用交易表自身的companyName
                String companyN = t.getCompanyName();
                if (t.getPropertyId() != null) {
                    TblPropertyRight pr = propertyRightMapper.selectById(t.getPropertyId());
                    if (pr != null && StringUtils.isNotBlank(pr.getCompanyName())) {
                        companyN = pr.getCompanyName();
                    }
                }
                item.put("companyName", companyN);
                item.put("transactionType", t.getTransactionType());
                item.put("transactionAmount", t.getTransactionAmount());
                item.put("transAmount", t.getTransactionAmount());
                item.put("counterparty", t.getCounterparty());
                item.put("transactionDate", t.getTransactionDate() != null ? t.getTransactionDate().toString() : null);
                item.put("transDate", t.getTransactionDate() != null ? t.getTransactionDate().toString() : null);
                item.put("transactionStatus", t.getApprovalStatus());

                // 合规字段
                String isExchangeTraded = t.getIsExchangeTraded() != null ? t.getIsExchangeTraded() : "0";
                String hasAppraisal = t.getHasAppraisal() != null ? t.getHasAppraisal() : "0";
                item.put("isExchangeTraded", "1".equals(isExchangeTraded));
                item.put("transMethod", "1".equals(isExchangeTraded) ? "EXCHANGE" : "AGREEMENT");
                item.put("hasAppraisal", hasAppraisal);
                item.put("appraisalValue", t.getAppraisalValue());

                // 价格比率计算
                java.math.BigDecimal priceRatio = null;
                if (t.getAppraisalValue() != null && t.getAppraisalValue().compareTo(java.math.BigDecimal.ZERO) > 0 && t.getTransactionAmount() != null) {
                    priceRatio = t.getTransactionAmount().multiply(new java.math.BigDecimal(100)).divide(t.getAppraisalValue(), 2, java.math.BigDecimal.ROUND_HALF_UP);
                }
                item.put("priceRatio", priceRatio);

                // 合规状态判定
                String complianceStatus = "COMPLIANT";
                String complianceIssues = null;
                if (!"1".equals(isExchangeTraded)) {
                    complianceStatus = "VIOLATION";
                    complianceIssues = "未通过产权交易所进场交易";
                } else if (!"1".equals(hasAppraisal)) {
                    complianceStatus = "ISSUE";
                    complianceIssues = "缺少独立机构资产评估报告";
                } else if (priceRatio != null && priceRatio.compareTo(new java.math.BigDecimal(90)) < 0) {
                    complianceStatus = "VIOLATION";
                    complianceIssues = "成交价格低于评估价值90%";
                }
                item.put("complianceStatus", complianceStatus);
                item.put("complianceIssues", complianceIssues);

                enriched.add(item);
            }
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage(pn);
            pr.setPageNumber(pn); pr.setTotalPage((int) r.getPages());
            pr.setPageSize(ps); pr.setTlist(enriched);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "保存产权交易记录")
    @PostMapping("/transaction/save")
    public R<Boolean> saveTransaction(@RequestBody TblPropertyTransaction record) {
        try {
            if (StringUtils.isEmpty(record.getTransactionId())) {
                record.setCreateTime(LocalDateTime.now());
                propertyTransactionMapper.insert(record);
            } else {
                record.setUpdateTime(LocalDateTime.now());
                propertyTransactionMapper.updateById(record);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除产权交易记录")
    @DeleteMapping("/transaction/{id}")
    public R<Boolean> deleteTransaction(@PathVariable String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return R.fail("删除失败：ID不能为空");
            }
            int rows = propertyTransactionMapper.deleteById(id);
            if (rows > 0) {
                return R.success(true);
            } else {
                return R.fail("删除失败：记录不存在");
            }
        } catch (Exception e) {
            log.error("删除产权交易记录失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询产权交易详情")
    @GetMapping("/transaction/{id}")
    public R<TblPropertyTransaction> getTransactionDetail(@PathVariable String id) {
        try {
            TblPropertyTransaction record = propertyTransactionMapper.selectById(id);
            if (record == null) {
                return R.fail("记录不存在");
            }
            return R.success(record);
        } catch (Exception e) {
            log.error("查询产权交易详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "交易合规统计")
    @GetMapping("/transaction/compliance/stats")
    public R<Map<String, Object>> transactionComplianceStats(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            Long totalTransactions = propertyTransactionMapper.selectCount(null);
            Long exchangeTraded = propertyTransactionMapper.selectCount(
                new LambdaQueryWrapper<TblPropertyTransaction>().eq(TblPropertyTransaction::getIsExchangeTraded, "1"));
            Long hasAppraisal = propertyTransactionMapper.selectCount(
                new LambdaQueryWrapper<TblPropertyTransaction>().eq(TblPropertyTransaction::getHasAppraisal, "1"));
            result.put("totalTransactions", totalTransactions);
            result.put("exchangeTradedCount", exchangeTraded);
            result.put("hasAppraisalCount", hasAppraisal);
            result.put("complianceRate", totalTransactions > 0 ? (double) exchangeTraded / totalTransactions * 100 : 0);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 参股监控 ====================

    @Operation(summary = "参股企业列表")
    @PostMapping("/shareholding/list")
    public R<PageResult<GzctShareholdingCompany>> shareholdingList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctShareholdingCompany> w = new LambdaQueryWrapper<>();
            // 企业名称模糊查询
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctShareholdingCompany::getCompanyName, params.get("companyName").toString().trim());
            }
            // 经营状态筛选
            if (params.get("businessStatus") != null && StringUtils.isNotBlank(params.get("businessStatus").toString())) {
                String status = params.get("businessStatus").toString();
                if ("LOSS".equals(status)) {
                    w.eq(GzctShareholdingCompany::getIsLoss, "1");
                } else if ("NORMAL".equals(status)) {
                    w.eq(GzctShareholdingCompany::getIsLoss, "0");
                }
            }
            // 综合评级筛选
            if (params.get("rating") != null && StringUtils.isNotBlank(params.get("rating").toString())) {
                w.eq(GzctShareholdingCompany::getRating, params.get("rating").toString());
            }
            // 兼容旧的isLoss筛选
            if (params.get("isLoss") != null && StringUtils.isNotBlank(params.get("isLoss").toString())) {
                w.eq(GzctShareholdingCompany::getIsLoss, params.get("isLoss").toString());
            }
            w.orderByDesc(GzctShareholdingCompany::getCreateTime);
            Page<GzctShareholdingCompany> r = shareholdingCompanyMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "参股企业经营分析详情")
    @GetMapping("/shareholding/analysis/{companyId}")
    public R<Map<String, Object>> shareholdingAnalysis(@PathVariable String companyId) {
        try {
            GzctShareholdingCompany company = shareholdingCompanyMapper.selectById(companyId);
            Map<String, Object> result = new HashMap<>();
            if (company != null) {
                result.put("company", company);
                // 查询该企业历年数据作为趋势
                List<GzctShareholdingCompany> historyList = shareholdingCompanyMapper.selectList(
                    new LambdaQueryWrapper<GzctShareholdingCompany>()
                        .eq(GzctShareholdingCompany::getCompanyName, company.getCompanyName())
                        .isNotNull(GzctShareholdingCompany::getReportYear)
                        .orderByAsc(GzctShareholdingCompany::getReportYear));
                List<String> trendYears = new ArrayList<>();
                List<BigDecimal> trendProfits = new ArrayList<>();
                for (GzctShareholdingCompany h : historyList) {
                    if (h.getReportYear() != null) {
                        trendYears.add(h.getReportYear());
                        trendProfits.add(h.getNetProfit() != null ? h.getNetProfit() : BigDecimal.ZERO);
                    }
                }
                result.put("trendYears", trendYears);
                result.put("trendProfits", trendProfits);
            } else {
                result.put("trendYears", new ArrayList<>());
                result.put("trendProfits", new ArrayList<>());
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "连续亏损企业清单")
    @GetMapping("/shareholding/loss-list")
    public R<List<GzctShareholdingCompany>> lossCompanyList() {
        try {
            List<GzctShareholdingCompany> list = shareholdingCompanyMapper.selectList(
                new LambdaQueryWrapper<GzctShareholdingCompany>()
                    .eq(GzctShareholdingCompany::getIsLoss, "1")
                    .orderByAsc(GzctShareholdingCompany::getLossYears));
            return R.success(list);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 预警管理 ====================

    @Operation(summary = "产权预警列表")
    @PostMapping("/warning/list")
    public R<PageResult<GzctPropertyWarning>> warningList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctPropertyWarning> w = new LambdaQueryWrapper<>();
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctPropertyWarning::getStatus, params.get("status").toString());
            }
            if (params.get("level") != null && StringUtils.isNotBlank(params.get("level").toString())) {
                w.eq(GzctPropertyWarning::getWarningLevel, params.get("level").toString());
            }
            w.orderByDesc(GzctPropertyWarning::getCreateTime);
            Page<GzctPropertyWarning> r = propertyWarningMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "处置产权预警")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("warningId") != null ? params.get("warningId").toString() : null;
            if (id == null) return R.fail("预警ID不能为空");
            GzctPropertyWarning warning = propertyWarningMapper.selectById(id);
            if (warning != null) {
                warning.setStatus("HANDLED");
                warning.setHandleOpinion(params.get("handleResult") != null ? params.get("handleResult").toString() : null);
                warning.setHandleTime(LocalDateTime.now());
                warning.setUpdateTime(LocalDateTime.now());
                propertyWarningMapper.updateById(warning);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("处理失败：" + e.getMessage()); }
    }

    @Operation(summary = "关闭产权预警")
    @PostMapping("/warning/close")
    public R<Boolean> closeWarning(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("warningId") != null ? params.get("warningId").toString() : null;
            if (id == null) return R.fail("预警ID不能为空");
            GzctPropertyWarning warning = propertyWarningMapper.selectById(id);
            if (warning != null) {
                warning.setStatus("CLOSED");
                warning.setUpdateTime(LocalDateTime.now());
                propertyWarningMapper.updateById(warning);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("关闭失败：" + e.getMessage()); }
    }

    // ==================== 三表比对 ====================

    /**
     * 三表比对列表 - 查询 THREE_TABLE_COMPARE 表，支持企业名称/差异状态筛选
     */
    @Operation(summary = "三表比对列表(产权登记/工商登记/财务并表)")
    @PostMapping("/three-table-compare/list")
    public R<PageResult<ThreeTableCompare>> threeTableCompareList(@RequestBody Map<String, Object> params) {
        try {
            ThreeTableCompareQueryVO queryVO = new ThreeTableCompareQueryVO();
            if (params != null) {
                if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                    queryVO.setCompanyName(params.get("companyName").toString());
                }
                if (params.get("diffStatus") != null && StringUtils.isNotBlank(params.get("diffStatus").toString())) {
                    queryVO.setDiffStatus(params.get("diffStatus").toString());
                }
                if (params.get("shareholderType") != null && StringUtils.isNotBlank(params.get("shareholderType").toString())) {
                    queryVO.setShareholderType(params.get("shareholderType").toString());
                }
                if (params.get("pageNumber") != null) {
                    queryVO.setPageNumber(Integer.parseInt(params.get("pageNumber").toString()));
                }
                if (params.get("pageSize") != null) {
                    queryVO.setPageSize(Integer.parseInt(params.get("pageSize").toString()));
                }
            }
            return R.success(threeTableCompareService.getList(queryVO));
        } catch (Exception e) {
            log.error("查询三表比对列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 三表比对详情（含差异分析明细）
     * 路径使用 /detail/{id} 避免与 PenetrationCrudController 的 GET /{id} 冲突
     */
    @Operation(summary = "三表比对详情(含差异分析)")
    @GetMapping("/three-table-compare/detail/{id}")
    public R<ThreeTableCompareDetailVO> threeTableCompareDetail2(@PathVariable String id) {
        try {
            return R.success(threeTableCompareService.getDetail(id));
        } catch (Exception e) {
            log.error("获取三表比对详情失败，id={}", id, e);
            return R.fail("获取详情失败：" + e.getMessage());
        }
    }

    /**
     * 导出三表比对差异报告（Excel）
     */
    @Operation(summary = "导出三表比对差异报告")
    @GetMapping("/three-table-compare/export")
    public void threeTableCompareExport(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String diffStatus,
            @RequestParam(required = false) String shareholderType,
            HttpServletResponse response) {
        ThreeTableCompareQueryVO queryVO = new ThreeTableCompareQueryVO();
        if (StringUtils.isNotBlank(companyName))     queryVO.setCompanyName(companyName);
        if (StringUtils.isNotBlank(diffStatus))      queryVO.setDiffStatus(diffStatus);
        if (StringUtils.isNotBlank(shareholderType)) queryVO.setShareholderType(shareholderType);
        threeTableCompareService.exportDiffReport(queryVO, response);
    }

    /**
     * 提交差异核查派单
     */
    @Operation(summary = "提交差异核查派单")
    @PostMapping("/three-table-compare/dispatch")
    public R<Void> threeTableCompareDispatch(@RequestBody ThreeTableDispatchVO dispatchVO) {
        try {
            if (StringUtils.isBlank(dispatchVO.getOwner())) {
                return R.fail("核查责任人不能为空");
            }
            if (StringUtils.isBlank(dispatchVO.getDeadline())) {
                return R.fail("核查期限不能为空");
            }
            if (StringUtils.isBlank(dispatchVO.getCompareId())) {
                return R.fail("关联比对记录ID不能为空");
            }
            threeTableCompareService.submitDispatch(dispatchVO);
            return R.success(null);
        } catch (Exception e) {
            log.error("提交差异核查派单失败", e);
            return R.fail("派单失败：" + e.getMessage());
        }
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "产权驾驶舱综合数据")
    @GetMapping("/dashboard")
    public R<Map<String, Object>> dashboard(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 组织层级过滤
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);

            // 基础统计
            LambdaQueryWrapper<TblPropertyRight> prBaseWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) prBaseWrapper.and(w -> w.like(TblPropertyRight::getOrgPath, orgPattern).or(sub -> sub.isNull(TblPropertyRight::getOrgPath).eq(TblPropertyRight::getCompanyId, companyId)));
            Long totalProperties = propertyRightMapper.selectCount(prBaseWrapper);

            LambdaQueryWrapper<TblPropertyTransaction> ptBaseWrapper = new LambdaQueryWrapper<>();
            // TBL_PROPERTY_TRANSACTION 无 ORG_PATH 字段，通过关联产权登记表间接过滤
            Long totalTransactions = propertyTransactionMapper.selectCount(ptBaseWrapper);
            Long activeWarnings = propertyWarningMapper.selectCount(
                new LambdaQueryWrapper<GzctPropertyWarning>().eq(GzctPropertyWarning::getStatus, "PENDING"));
            Long shareholdingCount = shareholdingCompanyMapper.selectCount(null);
            Long lossCount = shareholdingCompanyMapper.selectCount(
                new LambdaQueryWrapper<GzctShareholdingCompany>().eq(GzctShareholdingCompany::getIsLoss, "1"));

            // 进场交易率
            Long exchangeTraded = propertyTransactionMapper.selectCount(
                new LambdaQueryWrapper<TblPropertyTransaction>().eq(TblPropertyTransaction::getIsExchangeTraded, "1"));
            Long hasAppraisal = propertyTransactionMapper.selectCount(
                new LambdaQueryWrapper<TblPropertyTransaction>().eq(TblPropertyTransaction::getHasAppraisal, "1"));
            double exchangeTradeRate = totalTransactions > 0 ? (double) exchangeTraded / totalTransactions * 100 : 0;
            double appraisalRate = totalTransactions > 0 ? (double) hasAppraisal / totalTransactions * 100 : 0;

            // 参股企业盈利率
            Long profitShareholding = shareholdingCompanyMapper.selectCount(
                new LambdaQueryWrapper<GzctShareholdingCompany>().eq(GzctShareholdingCompany::getIsLoss, "0"));
            double shareholdingProfitRate = shareholdingCount > 0 ? (double) profitShareholding / shareholdingCount * 100 : 0;

            // 最深股权层级
            List<TblPropertyRight> allProperties = propertyRightMapper.selectList(null);
            int maxEquityLevel = 0;
            for (TblPropertyRight pr : allProperties) {
                if (pr.getEquityLevel() != null && pr.getEquityLevel() > maxEquityLevel) {
                    maxEquityLevel = pr.getEquityLevel();
                }
            }

            // KPI指标
            result.put("registeredCount", totalProperties);
            result.put("maxEquityLevel", maxEquityLevel);
            result.put("yearlyTransCount", totalTransactions);
            result.put("yearlyChangeCount", propertyChangeMapper.selectCount(null));
            result.put("complianceIssueCount", propertyWarningMapper.selectCount(
                new LambdaQueryWrapper<GzctPropertyWarning>().eq(GzctPropertyWarning::getWarningType, "COMPLIANCE")));
            result.put("inconsistentCount", threeTableCompareMapper.selectCount(
                new LambdaQueryWrapper<TblThreeTableCompare>().eq(TblThreeTableCompare::getIsConsistent, "N")));
            result.put("exchangeTradeRate", Math.round(exchangeTradeRate * 100.0) / 100.0);
            result.put("appraisalRate", Math.round(appraisalRate * 100.0) / 100.0);
            result.put("complianceRate", Math.round(exchangeTradeRate * 100.0) / 100.0);
            result.put("shareholdingCount", shareholdingCount);
            result.put("shareholdingProfitRate", Math.round(shareholdingProfitRate * 100.0) / 100.0);
            result.put("activeWarnings", activeWarnings);

            // 保留旧字段兼容
            result.put("totalProperties", totalProperties);
            result.put("totalTransactions", totalTransactions);
            result.put("lossCompanyCount", lossCount);

            // Frontend alias fields for STAT_MAP compatibility
            result.put("registryCount", totalProperties);
            result.put("propertyCount", totalProperties);
            result.put("totalCount", totalProperties);
            result.put("tradingCount", totalTransactions);
            result.put("transactionCount", totalTransactions);
            result.put("riskCount", activeWarnings);
            result.put("warningCount", activeWarnings);

            // 层级分布数据
            List<Map<String, Object>> levelDistribution = new ArrayList<>();
            Map<Integer, Long> levelCountMap = new HashMap<>();
            for (TblPropertyRight pr : allProperties) {
                int level = pr.getEquityLevel() != null ? pr.getEquityLevel() : 1;
                levelCountMap.merge(level, 1L, Long::sum);
            }
            for (Map.Entry<Integer, Long> entry : levelCountMap.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("level", entry.getKey());
                item.put("count", entry.getValue());
                levelDistribution.add(item);
            }
            levelDistribution.sort((a, b) -> Integer.compare((int) a.get("level"), (int) b.get("level")));
            result.put("levelDistribution", levelDistribution);

            // 年度交易趋势(从数据库按年份分组统计)
            List<Map<String, Object>> yearlyTrend = new ArrayList<>();
            int currentYear = java.time.LocalDate.now().getYear();
            for (int i = 4; i >= 0; i--) {
                int year = currentYear - i;
                java.time.LocalDate yearStart = java.time.LocalDate.of(year, 1, 1);
                java.time.LocalDate yearEnd = java.time.LocalDate.of(year, 12, 31);
                // 查询该年度交易总数
                Long yearCount = propertyTransactionMapper.selectCount(
                    new LambdaQueryWrapper<TblPropertyTransaction>()
                        .ge(TblPropertyTransaction::getTransactionDate, yearStart)
                        .le(TblPropertyTransaction::getTransactionDate, yearEnd));
                // 查询该年度合规交易数（进场交易）
                Long yearCompliant = propertyTransactionMapper.selectCount(
                    new LambdaQueryWrapper<TblPropertyTransaction>()
                        .ge(TblPropertyTransaction::getTransactionDate, yearStart)
                        .le(TblPropertyTransaction::getTransactionDate, yearEnd)
                        .eq(TblPropertyTransaction::getIsExchangeTraded, "1"));
                Map<String, Object> item = new HashMap<>();
                item.put("year", String.valueOf(year));
                item.put("count", yearCount != null ? yearCount : 0L);
                item.put("compliant", yearCompliant != null ? yearCompliant : 0L);
                yearlyTrend.add(item);
            }
            result.put("yearlyTrend", yearlyTrend);

            // 问题企业TOP10(从预警中统计)
            List<GzctPropertyWarning> allWarnings = propertyWarningMapper.selectList(
                new LambdaQueryWrapper<GzctPropertyWarning>().eq(GzctPropertyWarning::getStatus, "PENDING"));
            Map<String, Map<String, Object>> companyWarningMap = new HashMap<>();
            for (GzctPropertyWarning w : allWarnings) {
                String companyKey = w.getCompanyName() != null ? w.getCompanyName() : "unknown";
                Map<String, Object> entry = companyWarningMap.computeIfAbsent(companyKey, k -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("companyName", k); m.put("issueCount", 0L); m.put("issueType", "产权异常"); m.put("issueLevel", "MEDIUM");
                    return m;
                });
                entry.put("issueCount", (Long) entry.get("issueCount") + 1);
                if ("HIGH".equals(w.getWarningLevel())) entry.put("issueLevel", "HIGH");
            }
            List<Map<String, Object>> top10List = new ArrayList<>(companyWarningMap.values());
            top10List.sort((a, b) -> Long.compare((Long) b.get("issueCount"), (Long) a.get("issueCount")));
            if (top10List.size() > 10) top10List = top10List.subList(0, 10);
            result.put("top10List", top10List);

            // 异常企业列表
            List<Map<String, Object>> abnormalList = new ArrayList<>();
            for (GzctPropertyWarning w : allWarnings) {
                Map<String, Object> item = new HashMap<>();
                item.put("companyName", w.getCompanyName());
                item.put("issueType", w.getWarningType());
                item.put("issueLevel", w.getWarningLevel());
                item.put("issueLevelLabel", "HIGH".equals(w.getWarningLevel()) ? "高危" : "MEDIUM".equals(w.getWarningLevel()) ? "中危" : "低危");
                item.put("status", w.getStatus());
                item.put("statusLabel", "HANDLED".equals(w.getStatus()) ? "已处置" : "PENDING".equals(w.getStatus()) ? "待处置" : "处理中");
                item.put("foundDate", w.getCreateTime() != null ? w.getCreateTime().toLocalDate().toString() : "");
                abnormalList.add(item);
            }
            result.put("abnormalList", abnormalList);

            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 驾驶舱导出 ====================

    @Operation(summary = "导出产权驾驶舱报告(CSV)")
    @GetMapping("/dashboard/export")
    public void dashboardExport(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("产权监控驾驶舱报告.csv", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            java.io.PrintWriter w = response.getWriter();
            w.write("\uFEFF");

            // 基础统计
            Long totalProperties = propertyRightMapper.selectCount(null);
            Long totalTransactions = propertyTransactionMapper.selectCount(null);
            Long activeWarnings = propertyWarningMapper.selectCount(
                new LambdaQueryWrapper<GzctPropertyWarning>().eq(GzctPropertyWarning::getStatus, "PENDING"));
            Long shareholdingCount = shareholdingCompanyMapper.selectCount(null);
            Long exchangeTraded = propertyTransactionMapper.selectCount(
                new LambdaQueryWrapper<TblPropertyTransaction>().eq(TblPropertyTransaction::getIsExchangeTraded, "1"));
            Long hasAppraisal = propertyTransactionMapper.selectCount(
                new LambdaQueryWrapper<TblPropertyTransaction>().eq(TblPropertyTransaction::getHasAppraisal, "1"));
            double exchangeTradeRate = totalTransactions > 0 ? (double) exchangeTraded / totalTransactions * 100 : 0;
            double appraisalRate = totalTransactions > 0 ? (double) hasAppraisal / totalTransactions * 100 : 0;
            Long profitShareholding = shareholdingCompanyMapper.selectCount(
                new LambdaQueryWrapper<GzctShareholdingCompany>().eq(GzctShareholdingCompany::getIsLoss, "0"));
            double shareholdingProfitRate = shareholdingCount > 0 ? (double) profitShareholding / shareholdingCount * 100 : 0;

            // 写KPI概览
            w.println("=== 产权监控驾驶舱报告 ===");
            w.println("生成时间," + java.time.LocalDateTime.now().toString());
            w.println("");
            w.println("=== KPI指标 ===");
            w.println("指标名称,数值,单位");
            w.println("登记企业总数," + totalProperties + ",家");
            w.println("进场交易率," + String.format("%.2f", exchangeTradeRate) + ",%");
            w.println("评估覆盖率," + String.format("%.2f", appraisalRate) + ",%");
            w.println("参股企业盈利率," + String.format("%.2f", shareholdingProfitRate) + ",%");
            w.println("活跃预警数," + activeWarnings + ",条");
            w.println("产权交易总数," + totalTransactions + ",笔");
            w.println("参股企业总数," + shareholdingCount + ",家");
            w.println("");

            // 写年度交易趋势
            w.println("=== 近5年产权交易趋势 ===");
            w.println("年份,交易总数,合规交易数");
            int currentYear = java.time.LocalDate.now().getYear();
            for (int i = 4; i >= 0; i--) {
                int year = currentYear - i;
                java.time.LocalDate yearStart = java.time.LocalDate.of(year, 1, 1);
                java.time.LocalDate yearEnd = java.time.LocalDate.of(year, 12, 31);
                Long yearCount = propertyTransactionMapper.selectCount(
                    new LambdaQueryWrapper<TblPropertyTransaction>()
                        .ge(TblPropertyTransaction::getTransactionDate, yearStart)
                        .le(TblPropertyTransaction::getTransactionDate, yearEnd));
                Long yearCompliant = propertyTransactionMapper.selectCount(
                    new LambdaQueryWrapper<TblPropertyTransaction>()
                        .ge(TblPropertyTransaction::getTransactionDate, yearStart)
                        .le(TblPropertyTransaction::getTransactionDate, yearEnd)
                        .eq(TblPropertyTransaction::getIsExchangeTraded, "1"));
                w.println(year + "," + (yearCount != null ? yearCount : 0) + "," + (yearCompliant != null ? yearCompliant : 0));
            }
            w.println("");

            // 写问题企业TOP10
            w.println("=== 产权问题企业TOP10 ===");
            w.println("排名,企业名称,问题类型,问题数");
            List<GzctPropertyWarning> allWarnings = propertyWarningMapper.selectList(
                new LambdaQueryWrapper<GzctPropertyWarning>().eq(GzctPropertyWarning::getStatus, "PENDING"));
            Map<String, Long> companyCountMap = new HashMap<>();
            for (GzctPropertyWarning warn : allWarnings) {
                String name = warn.getCompanyName() != null ? warn.getCompanyName() : "未知";
                companyCountMap.merge(name, 1L, Long::sum);
            }
            List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(companyCountMap.entrySet());
            sortedEntries.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
            int rank = 1;
            for (Map.Entry<String, Long> entry : sortedEntries) {
                if (rank > 10) break;
                w.println(rank + "," + entry.getKey() + ",产权异常," + entry.getValue());
                rank++;
            }

            w.flush();
        } catch (Exception e) {
            log.error("导出产权驾驶舱报告失败", e);
        }
    }

    // ==================== 股权穿透图-台账弹窗 ====================

    @Operation(summary = "根据companyId查询企业产权台账详情(含直接子企业)")
    @GetMapping("/equity/registry/{companyId}")
    public R<Map<String, Object>> equityRegistryDetail(@PathVariable String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 查询当前企业
            List<TblPropertyRight> currentList = propertyRightMapper.selectList(
                new LambdaQueryWrapper<TblPropertyRight>().eq(TblPropertyRight::getCompanyId, companyId));
            TblPropertyRight current = currentList.isEmpty() ? null : currentList.get(0);

            if (current == null) {
                return R.fail("未找到企业产权登记信息，companyId=" + companyId);
            }

            // 当前企业信息
            Map<String, Object> companyInfo = new HashMap<>();
            companyInfo.put("propertyId", current.getPropertyId());
            companyInfo.put("companyId", current.getCompanyId());
            companyInfo.put("companyName", current.getCompanyName());
            companyInfo.put("parentCompanyId", current.getParentCompanyId());
            companyInfo.put("parentCompanyName", current.getParentCompanyName());
            companyInfo.put("rightType", current.getRightType());
            companyInfo.put("holdingRatio", current.getHoldingRatio());
            companyInfo.put("equityRatio", current.getEquityRatio());
            companyInfo.put("investAmount", current.getInvestAmount());
            companyInfo.put("registeredCapital", current.getRegisteredCapital());
            companyInfo.put("equityLevel", current.getEquityLevel());
            companyInfo.put("industry", current.getIndustry());
            companyInfo.put("region", current.getRegion());
            companyInfo.put("businessStatus", current.getBusinessStatus());
            companyInfo.put("registrationStatus", current.getRegistrationStatus());
            companyInfo.put("registrationNo", current.getRegistrationNo());
            companyInfo.put("propertyStatus", current.getPropertyStatus());
            companyInfo.put("legalRepresentative", current.getLegalRepresentative());
            companyInfo.put("unifiedCreditCode", current.getUnifiedCreditCode());
            companyInfo.put("establishDate", current.getEstablishDate());
            companyInfo.put("registrationConsistency", current.getRegistrationConsistency());
            companyInfo.put("isConsolidated", current.getIsConsolidated());
            companyInfo.put("remark", current.getRemark());
            companyInfo.put("createTime", current.getCreateTime());
            result.put("company", companyInfo);

            // 查询直接子企业列表
            List<TblPropertyRight> children = propertyRightMapper.selectList(
                new LambdaQueryWrapper<TblPropertyRight>().eq(TblPropertyRight::getParentCompanyId, companyId)
                    .orderByAsc(TblPropertyRight::getEquityLevel));
            List<Map<String, Object>> childList = new ArrayList<>();
            for (TblPropertyRight child : children) {
                Map<String, Object> childInfo = new HashMap<>();
                childInfo.put("propertyId", child.getPropertyId());
                childInfo.put("companyId", child.getCompanyId());
                childInfo.put("companyName", child.getCompanyName());
                childInfo.put("rightType", child.getRightType());
                childInfo.put("holdingRatio", child.getHoldingRatio());
                childInfo.put("equityRatio", child.getEquityRatio());
                childInfo.put("investAmount", child.getInvestAmount());
                childInfo.put("registeredCapital", child.getRegisteredCapital());
                childInfo.put("equityLevel", child.getEquityLevel());
                childInfo.put("industry", child.getIndustry());
                childInfo.put("region", child.getRegion());
                childInfo.put("businessStatus", child.getBusinessStatus());
                childInfo.put("registrationStatus", child.getRegistrationStatus());
                childInfo.put("propertyStatus", child.getPropertyStatus());
                childList.add(childInfo);
            }
            result.put("children", childList);
            result.put("childrenCount", childList.size());

            return R.success(result);
        } catch (Exception e) {
            log.error("查询企业产权台账详情失败，companyId={}", companyId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }
}
