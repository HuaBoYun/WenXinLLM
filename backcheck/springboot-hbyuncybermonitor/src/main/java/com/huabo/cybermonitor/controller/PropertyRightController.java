package com.huabo.cybermonitor.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.huabo.cybermonitor.entity.TblPropertyRight;
import com.huabo.cybermonitor.entity.TblPropertyTransaction;
import com.huabo.cybermonitor.service.ITblPropertyRightService;
import com.huabo.cybermonitor.service.ITblPropertyTransactionService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblPropertyRightQueryVO;
import com.huabo.cybermonitor.vo.TblPropertyTransactionQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "产权穿透式监管")
@RestController
@RequestMapping("/v1/supervision/property/right")
@Slf4j
public class PropertyRightController {

    @Autowired
    private ITblPropertyRightService propertyRightService;
    @Autowired
    private ITblPropertyTransactionService propertyTransactionService;

    @Operation(summary = "分页查询产权登记列表")
    @PostMapping("/list")
    public R<PageResult<TblPropertyRight>> list(@RequestBody TblPropertyRightQueryVO queryVO) {
        try {
            return R.success(propertyRightService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询产权登记列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询产权登记详情")
    @GetMapping("/{id}")
    public R<TblPropertyRight> detail(@PathVariable String id) {
        try {
            return R.success(propertyRightService.getById(id));
        } catch (Exception e) {
            log.error("查询产权登记详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增产权登记")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblPropertyRight record) {
        try {
            return propertyRightService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增产权登记失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新产权登记")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblPropertyRight record) {
        try {
            if (StringUtils.isEmpty(record.getPropertyId())) {
                return R.fail("产权ID不能为空");
            }
            return propertyRightService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新产权登记失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除产权登记（POST方式，传propertyId）")
    @PostMapping("/delete")
    public R<Boolean> deleteByPost(@RequestBody Map<String, String> body) {
        try {
            String id = body.get("propertyId");
            if (StringUtils.isEmpty(id)) return R.fail("产权ID不能为空");
            return propertyRightService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除产权登记失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除产权登记（RESTful）")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return propertyRightService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除产权登记失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除产权登记")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return propertyRightService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除产权登记失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取产权统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String companyName) {
        try {
            return R.success(propertyRightService.getStatistics(companyId, companyName));
        } catch (Exception e) {
            log.error("获取产权统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量导入产权登记（Excel）")
    @PostMapping("/import")
    public R<String> importData(@RequestParam("file") MultipartFile file) {
        try {
            List<TblPropertyRight> importList = new ArrayList<>();
            EasyExcel.read(file.getInputStream(), null, new AnalysisEventListener<Map<Integer, String>>() {
                boolean firstRow = true;
                @Override
                public void invoke(Map<Integer, String> row, AnalysisContext context) {
                    if (firstRow) { firstRow = false; return; } // 跳过表头
                    try {
                        TblPropertyRight r = new TblPropertyRight();
                        r.setCompanyName(row.get(0));
                        r.setParentCompanyName(row.get(1));
                        r.setEquityLevel(row.get(2) != null ? Integer.parseInt(row.get(2).trim()) : 1);
                        r.setEquityRatio(row.get(3) != null ? new BigDecimal(row.get(3).trim()) : BigDecimal.ZERO);
                        r.setInvestAmount(row.get(4) != null ? new BigDecimal(row.get(4).trim()) : BigDecimal.ZERO);
                        r.setRegisteredCapital(row.get(5) != null ? new BigDecimal(row.get(5).trim()) : BigDecimal.ZERO);
                        r.setBusinessStatus(row.get(6));
                        r.setPropertyStatus(row.get(7));
                        r.setRegistrationConsistency(row.get(8));
                        r.setIsConsolidated(row.get(9));
                        r.setUnifiedCreditCode(row.get(10));
                        r.setRightType(row.get(11));
                        r.setCreateTime(LocalDateTime.now());
                        importList.add(r);
                    } catch (Exception ex) {
                        log.warn("导入行解析失败: {}", ex.getMessage());
                    }
                }
                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {}
            }).sheet().doRead();

            if (importList.isEmpty()) return R.fail("未解析到有效数据，请检查文件格式");
            propertyRightService.saveBatch(importList);
            return R.success("成功导入 " + importList.size() + " 条记录");
        } catch (Exception e) {
            log.error("批量导入产权登记失败", e);
            return R.fail("导入失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出产权登记台账（CSV）")
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam(required = false) String companyName,
                       @RequestParam(required = false) String businessStatus,
                       @RequestParam(required = false) String propertyStatus,
                       @RequestParam(required = false) String registrationConsistency) {
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("产权登记台账.csv", "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            PrintWriter w = response.getWriter();
            w.write("\uFEFF");

            // 构建查询条件
            TblPropertyRightQueryVO queryVO = new TblPropertyRightQueryVO();
            queryVO.setPageNumber(1);
            queryVO.setPageSize(10000);
            queryVO.setCompanyName(companyName);
            queryVO.setBusinessStatus(businessStatus);
            queryVO.setPropertyStatus(propertyStatus);
            queryVO.setRegistrationConsistency(registrationConsistency);
            PageResult<TblPropertyRight> result = propertyRightService.selectByPage(queryVO);

            // 写表头
            w.println("企业名称,上级企业,股权层级,持股比例(%),出资额(万元),注册资本(万元),经营状态,产权状态,工商比对,是否并表,统一社会信用代码,产权类型,法定代表人,备注");
            for (TblPropertyRight r : result.getTlist()) {
                String[] row = {
                    s(r.getCompanyName()), s(r.getParentCompanyName()),
                    s(r.getEquityLevel()), s(r.getEquityRatio()),
                    s(r.getInvestAmount()), s(r.getRegisteredCapital()),
                    businessStatusCn(r.getBusinessStatus()), propertyStatusCn(r.getPropertyStatus()),
                    "CONSISTENT".equals(r.getRegistrationConsistency()) ? "一致" : "差异",
                    "Y".equals(r.getIsConsolidated()) ? "是" : "否",
                    s(r.getUnifiedCreditCode()), rightTypeCn(r.getRightType()),
                    s(r.getLegalRepresentative()), s(r.getRemark())
                };
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) sb.append(",");
                    String v = row[i] != null ? row[i] : "";
                    if (v.contains(",") || v.contains("\"")) sb.append("\"").append(v.replace("\"", "\"\"")).append("\"");
                    else sb.append(v);
                }
                w.println(sb);
            }
            w.flush();
        } catch (Exception e) {
            log.error("导出产权登记台账失败", e);
        }
    }

    private String s(Object o) { return o != null ? o.toString() : ""; }
    private String businessStatusCn(String v) {
        if (v == null) return "";
        switch (v) { case "NORMAL": return "正常经营"; case "LOSS": return "连续亏损"; case "LIQUIDATION": return "清算中"; case "CANCELLED": return "已注销"; default: return v; }
    }
    private String propertyStatusCn(String v) {
        if (v == null) return "";
        switch (v) { case "NORMAL": return "正常"; case "CHANGING": return "变动中"; case "FROZEN": return "冻结"; default: return v; }
    }
    private String rightTypeCn(String v) {
        if (v == null) return "";
        switch (v) { case "SOLE": return "国有独资"; case "HOLDING": return "国有控股"; case "PARTICIPATING": return "国有参股"; default: return v; }
    }

    // ========== 产权交易相关接口 ==========

    @Operation(summary = "分页查询产权交易列表")
    @PostMapping("/transaction/list")
    public R<PageResult<TblPropertyTransaction>> transactionList(@RequestBody TblPropertyTransactionQueryVO queryVO) {
        try {
            return R.success(propertyTransactionService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询产权交易列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询产权交易详情")
    @GetMapping("/transaction/{id}")
    public R<TblPropertyTransaction> transactionDetail(@PathVariable String id) {
        try {
            return R.success(propertyTransactionService.getById(id));
        } catch (Exception e) {
            log.error("查询产权交易详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增产权交易")
    @PostMapping("/transaction/add")
    public R<Boolean> addTransaction(@RequestBody TblPropertyTransaction record) {
        try {
            return propertyTransactionService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增产权交易失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新产权交易")
    @PostMapping("/transaction/update")
    public R<Boolean> updateTransaction(@RequestBody TblPropertyTransaction record) {
        try {
            return propertyTransactionService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新产权交易失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }
}

