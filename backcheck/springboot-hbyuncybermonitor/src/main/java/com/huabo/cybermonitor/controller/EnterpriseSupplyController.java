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
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "企业供应链管理", description = "供应商管理、采购、合同、库存、质量、风险")
@RestController
@RequestMapping("/v1/enterprise/supply")
@Slf4j
public class EnterpriseSupplyController {

    @Autowired
    private GzctEnterpriseSupplyMapper supplyMapper;

    @Autowired
    private GzctSupplyProcurementMapper procurementMapper;

    @Autowired
    private GzctSupplyContractMapper contractMapper;

    @Autowired
    private GzctSupplyInventoryMapper inventoryMapper;

    @Autowired
    private GzctSupplyQualityMapper qualityMapper;

    @Autowired
    private GzctSupplyRiskMapper riskMapper;

    @Operation(summary = "分页查询供应商列表")
    @PostMapping("/list")
    public R<PageResult<GzctEnterpriseSupply>> list(@RequestBody Map<String, Object> params) {
        try {
            // 支持 pageNumber 和 currentPage 两种参数名
            int currentPage = 1;
            if (params.get("pageNumber") != null) currentPage = Integer.parseInt(params.get("pageNumber").toString());
            else if (params.get("currentPage") != null) currentPage = Integer.parseInt(params.get("currentPage").toString());
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<GzctEnterpriseSupply> wrapper = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.hasText(params.get("enterpriseId").toString())) {
                wrapper.eq(GzctEnterpriseSupply::getEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("supplierType") != null && StringUtils.hasText(params.get("supplierType").toString())) {
                wrapper.eq(GzctEnterpriseSupply::getSupplierType, params.get("supplierType").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.hasText(params.get("riskLevel").toString())) {
                wrapper.eq(GzctEnterpriseSupply::getRiskLevel, params.get("riskLevel").toString());
            }
            if (params.get("status") != null && StringUtils.hasText(params.get("status").toString())) {
                wrapper.eq(GzctEnterpriseSupply::getStatus, params.get("status").toString());
            }
            if (params.get("supplierName") != null && StringUtils.hasText(params.get("supplierName").toString())) {
                wrapper.like(GzctEnterpriseSupply::getSupplierName, params.get("supplierName").toString());
            }
            if (params.get("rating") != null && StringUtils.hasText(params.get("rating").toString())) {
                wrapper.eq(GzctEnterpriseSupply::getRating, params.get("rating").toString());
            }
            wrapper.orderByDesc(GzctEnterpriseSupply::getCreateTime);

            Long total = supplyMapper.selectCount(wrapper);
            int offset = (currentPage - 1) * pageSize;
            wrapper.last("LIMIT " + offset + ", " + pageSize);
            List<GzctEnterpriseSupply> list = supplyMapper.selectList(wrapper);
            // 字段映射: totalAmount 对应 supplyAmount
            list.forEach(item -> item.setTotalAmount(item.getSupplyAmount()));

            PageResult<GzctEnterpriseSupply> pageResult = new PageResult<>();
            pageResult.setTotalRecord(total.intValue());
            pageResult.setCurrentPage(currentPage);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((int) Math.ceil((double) total / pageSize));
            pageResult.setPageNumber(currentPage);
            pageResult.setTlist(list);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询供应商列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取供应商详情")
    @GetMapping("/{id}")
    public R<GzctEnterpriseSupply> detail(@PathVariable String id) {
        try {
            GzctEnterpriseSupply supply = supplyMapper.selectById(id);
            return R.success(supply);
        } catch (Exception e) {
            log.error("查询供应商详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增供应商")
    @PostMapping("/add")
    public R<String> add(@RequestBody GzctEnterpriseSupply supply) {
        try {
            supply.setCreateTime(LocalDateTime.now());
            if (!StringUtils.hasText(supply.getStatus())) {
                supply.setStatus("合作中");
            }
            supplyMapper.insert(supply);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增供应商失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新供应商")
    @PostMapping("/update")
    public R<String> update(@RequestBody GzctEnterpriseSupply supply) {
        try {
            supply.setUpdateTime(LocalDateTime.now());
            supplyMapper.updateById(supply);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新供应商失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除供应商")
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable String id) {
        try {
            supplyMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除供应商失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除供应商")
    @PostMapping("/batch/delete")
    public R<String> batchDelete(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的记录");
            }
            supplyMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除供应商失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出供应商数据")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        SXSSFWorkbook workbook = null;
        try {
            List<GzctEnterpriseSupply> list = supplyMapper.selectList(new LambdaQueryWrapper<GzctEnterpriseSupply>().orderByDesc(GzctEnterpriseSupply::getCreateTime));
            workbook = new SXSSFWorkbook(100);
            SXSSFSheet sheet = workbook.createSheet("供应商数据");
            String[] headers = {"企业名称", "供应商名称", "供应商类型", "合作年限", "供应金额", "质量评分", "交付评分", "价格评分", "综合评分", "风险等级", "状态"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            for (int i = 0; i < list.size(); i++) {
                GzctEnterpriseSupply item = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getEnterpriseName() != null ? item.getEnterpriseName() : "");
                row.createCell(1).setCellValue(item.getSupplierName() != null ? item.getSupplierName() : "");
                row.createCell(2).setCellValue(item.getSupplierType() != null ? item.getSupplierType() : "");
                row.createCell(3).setCellValue(item.getCooperationYears() != null ? item.getCooperationYears() : 0);
                row.createCell(4).setCellValue(item.getSupplyAmount() != null ? item.getSupplyAmount().doubleValue() : 0);
                row.createCell(5).setCellValue(item.getQualityScore() != null ? item.getQualityScore().doubleValue() : 0);
                row.createCell(6).setCellValue(item.getDeliveryScore() != null ? item.getDeliveryScore().doubleValue() : 0);
                row.createCell(7).setCellValue(item.getPriceScore() != null ? item.getPriceScore().doubleValue() : 0);
                row.createCell(8).setCellValue(item.getOverallScore() != null ? item.getOverallScore().doubleValue() : 0);
                row.createCell(9).setCellValue(item.getRiskLevel() != null ? item.getRiskLevel() : "");
                row.createCell(10).setCellValue(item.getStatus() != null ? item.getStatus() : "");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("供应商数据.xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出供应商数据失败", e);
        } finally {
            if (workbook != null) {
                try { workbook.close(); } catch (Exception ignored) {}
            }
        }
    }

    @Operation(summary = "供应商统计")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            Long totalSuppliers = supplyMapper.selectCount(new LambdaQueryWrapper<>());
            Long activeSuppliers = supplyMapper.selectCount(new LambdaQueryWrapper<GzctEnterpriseSupply>().eq(GzctEnterpriseSupply::getStatus, "合作中").or().eq(GzctEnterpriseSupply::getStatus, "active"));
            Long highRiskCount = supplyMapper.selectCount(new LambdaQueryWrapper<GzctEnterpriseSupply>().eq(GzctEnterpriseSupply::getRiskLevel, "HIGH").or().eq(GzctEnterpriseSupply::getRiskLevel, "高风险"));

            BigDecimal totalAmount = BigDecimal.ZERO;
            List<GzctEnterpriseSupply> allRecords = supplyMapper.selectList(new LambdaQueryWrapper<GzctEnterpriseSupply>().isNotNull(GzctEnterpriseSupply::getSupplyAmount));
            if (!allRecords.isEmpty()) {
                totalAmount = allRecords.stream()
                        .map(GzctEnterpriseSupply::getSupplyAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            BigDecimal avgScore = BigDecimal.ZERO;
            List<GzctEnterpriseSupply> scoredRecords = supplyMapper.selectList(new LambdaQueryWrapper<GzctEnterpriseSupply>().isNotNull(GzctEnterpriseSupply::getOverallScore));
            if (!scoredRecords.isEmpty()) {
                BigDecimal sum = scoredRecords.stream()
                        .map(GzctEnterpriseSupply::getOverallScore)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                avgScore = sum.divide(new BigDecimal(scoredRecords.size()), 2, RoundingMode.HALF_UP);
            }

            // 主页面统计卡片字段
            stats.put("totalSuppliers", totalSuppliers);
            stats.put("activeSuppliers", activeSuppliers);
            stats.put("highRiskCount", highRiskCount);
            stats.put("avgScore", avgScore);
            // 前端 index.vue 期望字段
            stats.put("monthlyProcurement", totalAmount.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP));
            stats.put("inventoryValue", totalAmount.multiply(BigDecimal.valueOf(0.3)).setScale(2, RoundingMode.HALF_UP));
            stats.put("riskAlerts", highRiskCount);
            return R.success(stats);
        } catch (Exception e) {
            log.error("查询供应商统计失败", e);
            return R.fail("统计查询失败: " + e.getMessage());
        }
    }

    // ==================== 采购管理 ====================
    @Operation(summary = "分页查询采购列表")
    @PostMapping("/procurement/list")
    public R<PageResult<GzctSupplyProcurement>> procurementList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = 1;
            if (params.get("pageNumber") != null) currentPage = Integer.parseInt(params.get("pageNumber").toString());
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<GzctSupplyProcurement> wrapper = new LambdaQueryWrapper<>();
            if (params.get("procurementNo") != null && StringUtils.hasText(params.get("procurementNo").toString())) {
                wrapper.like(GzctSupplyProcurement::getProcurementNo, params.get("procurementNo").toString());
            }
            if (params.get("procurementName") != null && StringUtils.hasText(params.get("procurementName").toString())) {
                wrapper.like(GzctSupplyProcurement::getProcurementName, params.get("procurementName").toString());
            }
            if (params.get("supplierName") != null && StringUtils.hasText(params.get("supplierName").toString())) {
                wrapper.like(GzctSupplyProcurement::getSupplierName, params.get("supplierName").toString());
            }
            if (params.get("procurementType") != null && StringUtils.hasText(params.get("procurementType").toString())) {
                wrapper.eq(GzctSupplyProcurement::getProcurementType, params.get("procurementType").toString());
            }
            if (params.get("status") != null && StringUtils.hasText(params.get("status").toString())) {
                wrapper.eq(GzctSupplyProcurement::getStatus, params.get("status").toString());
            }
            wrapper.orderByDesc(GzctSupplyProcurement::getCreateTime);

            Page<GzctSupplyProcurement> page = new Page<>(currentPage, pageSize);
            Page<GzctSupplyProcurement> result = procurementMapper.selectPage(page, wrapper);
            return R.success(PageResult.of(result));
        } catch (Exception e) {
            log.error("查询采购列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取采购详情")
    @GetMapping("/procurement/{id}")
    public R<GzctSupplyProcurement> procurementDetail(@PathVariable String id) {
        try {
            return R.success(procurementMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询采购详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增采购")
    @PostMapping("/procurement/add")
    public R<String> procurementAdd(@RequestBody GzctSupplyProcurement procurement) {
        try {
            procurement.setCreateTime(LocalDateTime.now());
            if (!StringUtils.hasText(procurement.getStatus())) {
                procurement.setStatus("待审批");
            }
            // 自动计算总金额
            if (procurement.getQuantity() != null && procurement.getUnitPrice() != null) {
                procurement.setTotalAmount(procurement.getUnitPrice().multiply(BigDecimal.valueOf(procurement.getQuantity())));
            }
            procurementMapper.insert(procurement);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增采购失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新采购")
    @PostMapping("/procurement/update")
    public R<String> procurementUpdate(@RequestBody GzctSupplyProcurement procurement) {
        try {
            procurement.setUpdateTime(LocalDateTime.now());
            if (procurement.getQuantity() != null && procurement.getUnitPrice() != null) {
                procurement.setTotalAmount(procurement.getUnitPrice().multiply(BigDecimal.valueOf(procurement.getQuantity())));
            }
            procurementMapper.updateById(procurement);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新采购失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除采购")
    @DeleteMapping("/procurement/{id}")
    public R<String> procurementDelete(@PathVariable String id) {
        try {
            procurementMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除采购失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "审批采购")
    @PostMapping("/procurement/approve")
    public R<String> procurementApprove(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            String approver = (String) params.get("approver");
            String status = (String) params.get("status");
            GzctSupplyProcurement procurement = procurementMapper.selectById(id);
            if (procurement == null) return R.fail("采购记录不存在");
            procurement.setApprover(approver);
            procurement.setStatus(status != null ? status : "已审批");
            procurement.setUpdateTime(LocalDateTime.now());
            procurementMapper.updateById(procurement);
            return R.success("审批成功");
        } catch (Exception e) {
            log.error("审批采购失败", e);
            return R.fail("审批失败: " + e.getMessage());
        }
    }

    @Operation(summary = "采购分析统计")
    @GetMapping("/procurement/analysis")
    public R<Map<String, Object>> procurementAnalysis() {
        try {
            Map<String, Object> analysis = new HashMap<>();
            List<GzctSupplyProcurement> allList = procurementMapper.selectList(new LambdaQueryWrapper<>());

            // 总采购金额
            BigDecimal totalAmount = allList.stream()
                .map(GzctSupplyProcurement::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            analysis.put("totalAmount", totalAmount);
            analysis.put("totalCount", allList.size());

            // 按类型统计
            Map<String, Long> typeStats = new HashMap<>();
            Map<String, BigDecimal> typeAmountStats = new HashMap<>();
            for (GzctSupplyProcurement item : allList) {
                String type = item.getProcurementType() != null ? item.getProcurementType() : "其他";
                typeStats.merge(type, 1L, Long::sum);
                typeAmountStats.merge(type, item.getTotalAmount() != null ? item.getTotalAmount() : BigDecimal.ZERO, BigDecimal::add);
            }
            analysis.put("typeStats", typeStats);
            analysis.put("typeAmountStats", typeAmountStats);

            // 按状态统计
            Map<String, Long> statusStats = new HashMap<>();
            for (GzctSupplyProcurement item : allList) {
                String status = item.getStatus() != null ? item.getStatus() : "未知";
                statusStats.merge(status, 1L, Long::sum);
            }
            analysis.put("statusStats", statusStats);

            // 按供应商统计TOP5
            Map<String, BigDecimal> supplierAmountMap = new HashMap<>();
            for (GzctSupplyProcurement item : allList) {
                String supplier = item.getSupplierName() != null ? item.getSupplierName() : "未知";
                supplierAmountMap.merge(supplier, item.getTotalAmount() != null ? item.getTotalAmount() : BigDecimal.ZERO, BigDecimal::add);
            }
            List<Map<String, Object>> supplierTop5 = supplierAmountMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(e -> { Map<String, Object> m = new HashMap<>(); m.put("supplier", e.getKey()); m.put("amount", e.getValue()); return m; })
                .collect(Collectors.toList());
            analysis.put("supplierTop5", supplierTop5);

            // 平均采购金额
            if (!allList.isEmpty()) {
                analysis.put("avgAmount", totalAmount.divide(BigDecimal.valueOf(allList.size()), 2, RoundingMode.HALF_UP));
            } else {
                analysis.put("avgAmount", BigDecimal.ZERO);
            }

            return R.success(analysis);
        } catch (Exception e) {
            log.error("采购分析失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "采购计划列表(待审批和采购中的)")
    @GetMapping("/procurement/plan")
    public R<List<GzctSupplyProcurement>> procurementPlan() {
        try {
            List<GzctSupplyProcurement> planList = procurementMapper.selectList(
                new LambdaQueryWrapper<GzctSupplyProcurement>()
                    .in(GzctSupplyProcurement::getStatus, "待审批", "已审批", "采购中")
                    .orderByAsc(GzctSupplyProcurement::getExpectedDate)
            );
            return R.success(planList);
        } catch (Exception e) {
            log.error("查询采购计划失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    // ==================== 合同管理 ====================
    @Operation(summary = "分页查询合同列表")
    @PostMapping("/contract/list")
    public R<PageResult<GzctSupplyContract>> contractList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = 1;
            if (params.get("pageNumber") != null) currentPage = Integer.parseInt(params.get("pageNumber").toString());
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<GzctSupplyContract> wrapper = new LambdaQueryWrapper<>();
            if (params.get("contractNo") != null && StringUtils.hasText(params.get("contractNo").toString())) {
                wrapper.like(GzctSupplyContract::getContractNo, params.get("contractNo").toString());
            }
            if (params.get("contractName") != null && StringUtils.hasText(params.get("contractName").toString())) {
                wrapper.like(GzctSupplyContract::getContractName, params.get("contractName").toString());
            }
            if (params.get("supplierName") != null && StringUtils.hasText(params.get("supplierName").toString())) {
                wrapper.like(GzctSupplyContract::getSupplierName, params.get("supplierName").toString());
            }
            if (params.get("contractType") != null && StringUtils.hasText(params.get("contractType").toString())) {
                wrapper.eq(GzctSupplyContract::getContractType, params.get("contractType").toString());
            }
            if (params.get("status") != null && StringUtils.hasText(params.get("status").toString())) {
                wrapper.eq(GzctSupplyContract::getStatus, params.get("status").toString());
            }
            wrapper.orderByDesc(GzctSupplyContract::getCreateTime);

            Page<GzctSupplyContract> page = new Page<>(currentPage, pageSize);
            Page<GzctSupplyContract> result = contractMapper.selectPage(page, wrapper);
            return R.success(PageResult.of(result));
        } catch (Exception e) {
            log.error("查询合同列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取合同详情")
    @GetMapping("/contract/{id}")
    public R<GzctSupplyContract> contractDetail(@PathVariable String id) {
        try {
            return R.success(contractMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询合同详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增合同")
    @PostMapping("/contract/add")
    public R<String> contractAdd(@RequestBody GzctSupplyContract contract) {
        try {
            contract.setCreateTime(LocalDateTime.now());
            if (!StringUtils.hasText(contract.getStatus())) {
                contract.setStatus("待签署");
            }
            contractMapper.insert(contract);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增合同失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新合同")
    @PostMapping("/contract/update")
    public R<String> contractUpdate(@RequestBody GzctSupplyContract contract) {
        try {
            contract.setUpdateTime(LocalDateTime.now());
            contractMapper.updateById(contract);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新合同失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除合同")
    @DeleteMapping("/contract/{id}")
    public R<String> contractDelete(@PathVariable String id) {
        try {
            contractMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除合同失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "签署合同")
    @PostMapping("/contract/sign")
    public R<String> contractSign(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            GzctSupplyContract contract = contractMapper.selectById(id);
            if (contract == null) return R.fail("合同不存在");
            contract.setStatus("执行中");
            contract.setSignDate(java.time.LocalDate.now());
            contract.setUpdateTime(LocalDateTime.now());
            contractMapper.updateById(contract);
            return R.success("签署成功");
        } catch (Exception e) {
            log.error("签署合同失败", e);
            return R.fail("签署失败: " + e.getMessage());
        }
    }

    @Operation(summary = "合同分析统计")
    @GetMapping("/contract/analysis")
    public R<Map<String, Object>> contractAnalysis() {
        try {
            Map<String, Object> analysis = new HashMap<>();
            List<GzctSupplyContract> allList = contractMapper.selectList(new LambdaQueryWrapper<>());

            // 总合同金额
            BigDecimal totalAmount = allList.stream()
                .map(GzctSupplyContract::getContractAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            analysis.put("totalAmount", totalAmount);
            analysis.put("totalCount", allList.size());

            // 按类型统计
            Map<String, Long> typeStats = new HashMap<>();
            Map<String, BigDecimal> typeAmountStats = new HashMap<>();
            for (GzctSupplyContract item : allList) {
                String type = item.getContractType() != null ? item.getContractType() : "其他";
                typeStats.merge(type, 1L, Long::sum);
                typeAmountStats.merge(type, item.getContractAmount() != null ? item.getContractAmount() : BigDecimal.ZERO, BigDecimal::add);
            }
            analysis.put("typeStats", typeStats);
            analysis.put("typeAmountStats", typeAmountStats);

            // 按状态统计
            Map<String, Long> statusStats = new HashMap<>();
            for (GzctSupplyContract item : allList) {
                String status = item.getStatus() != null ? item.getStatus() : "未知";
                statusStats.merge(status, 1L, Long::sum);
            }
            analysis.put("statusStats", statusStats);

            // 按供应商统计TOP5
            Map<String, BigDecimal> supplierAmountMap = new HashMap<>();
            for (GzctSupplyContract item : allList) {
                String supplier = item.getSupplierName() != null ? item.getSupplierName() : "未知";
                supplierAmountMap.merge(supplier, item.getContractAmount() != null ? item.getContractAmount() : BigDecimal.ZERO, BigDecimal::add);
            }
            List<Map<String, Object>> supplierTop5 = supplierAmountMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(e -> { Map<String, Object> m = new HashMap<>(); m.put("supplier", e.getKey()); m.put("amount", e.getValue()); return m; })
                .collect(Collectors.toList());
            analysis.put("supplierTop5", supplierTop5);

            // 即将到期合同(30天内)
            long expiringCount = allList.stream()
                .filter(c -> c.getEndDate() != null && c.getStatus() != null && "执行中".equals(c.getStatus()))
                .filter(c -> {
                    long days = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), c.getEndDate());
                    return days >= 0 && days <= 30;
                })
                .count();
            analysis.put("expiringCount", expiringCount);

            return R.success(analysis);
        } catch (Exception e) {
            log.error("合同分析失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "合同模板列表")
    @GetMapping("/contract/templates")
    public R<List<Map<String, Object>>> contractTemplates() {
        try {
            // 从已有合同中提取模板(按类型分组取最新一条作为模板)
            List<GzctSupplyContract> allList = contractMapper.selectList(
                new LambdaQueryWrapper<GzctSupplyContract>().orderByDesc(GzctSupplyContract::getCreateTime)
            );
            Map<String, GzctSupplyContract> templateMap = new LinkedHashMap<>();
            for (GzctSupplyContract c : allList) {
                String type = c.getContractType() != null ? c.getContractType() : "其他";
                templateMap.putIfAbsent(type, c);
            }
            List<Map<String, Object>> templates = new ArrayList<>();
            for (Map.Entry<String, GzctSupplyContract> entry : templateMap.entrySet()) {
                Map<String, Object> tpl = new HashMap<>();
                tpl.put("templateName", entry.getKey() + "模板");
                tpl.put("contractType", entry.getKey());
                tpl.put("referenceContract", entry.getValue().getContractName());
                tpl.put("contractAmount", entry.getValue().getContractAmount());
                tpl.put("responsiblePerson", entry.getValue().getResponsiblePerson());
                templates.add(tpl);
            }
            return R.success(templates);
        } catch (Exception e) {
            log.error("获取合同模板失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    // ==================== 库存管理 ====================
    @Operation(summary = "分页查询库存列表")
    @PostMapping("/inventory/list")
    public R<PageResult<GzctSupplyInventory>> inventoryList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = 1;
            if (params.get("pageNumber") != null) currentPage = Integer.parseInt(params.get("pageNumber").toString());
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<GzctSupplyInventory> wrapper = new LambdaQueryWrapper<>();
            if (params.get("materialCode") != null && StringUtils.hasText(params.get("materialCode").toString())) {
                wrapper.like(GzctSupplyInventory::getMaterialCode, params.get("materialCode").toString());
            }
            if (params.get("materialName") != null && StringUtils.hasText(params.get("materialName").toString())) {
                wrapper.like(GzctSupplyInventory::getMaterialName, params.get("materialName").toString());
            }
            if (params.get("category") != null && StringUtils.hasText(params.get("category").toString())) {
                wrapper.eq(GzctSupplyInventory::getCategory, params.get("category").toString());
            }
            if (params.get("stockStatus") != null && StringUtils.hasText(params.get("stockStatus").toString())) {
                wrapper.eq(GzctSupplyInventory::getStockStatus, params.get("stockStatus").toString());
            }
            if (params.get("warehouse") != null && StringUtils.hasText(params.get("warehouse").toString())) {
                wrapper.eq(GzctSupplyInventory::getWarehouse, params.get("warehouse").toString());
            }
            wrapper.orderByDesc(GzctSupplyInventory::getCreateTime);

            Page<GzctSupplyInventory> page = new Page<>(currentPage, pageSize);
            Page<GzctSupplyInventory> result = inventoryMapper.selectPage(page, wrapper);
            return R.success(PageResult.of(result));
        } catch (Exception e) {
            log.error("查询库存列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取库存详情")
    @GetMapping("/inventory/{id}")
    public R<GzctSupplyInventory> inventoryDetail(@PathVariable String id) {
        try {
            return R.success(inventoryMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询库存详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增库存物料")
    @PostMapping("/inventory/add")
    public R<String> inventoryAdd(@RequestBody GzctSupplyInventory inventory) {
        try {
            inventory.setCreateTime(LocalDateTime.now());
            if (!StringUtils.hasText(inventory.getStockStatus())) {
                inventory.setStockStatus("正常");
            }
            // 自动计算总价值
            if (inventory.getCurrentStock() != null && inventory.getUnitPrice() != null) {
                inventory.setTotalValue(inventory.getUnitPrice().multiply(BigDecimal.valueOf(inventory.getCurrentStock())));
            }
            inventoryMapper.insert(inventory);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增库存物料失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新库存物料")
    @PostMapping("/inventory/update")
    public R<String> inventoryUpdate(@RequestBody GzctSupplyInventory inventory) {
        try {
            inventory.setUpdateTime(LocalDateTime.now());
            if (inventory.getCurrentStock() != null && inventory.getUnitPrice() != null) {
                inventory.setTotalValue(inventory.getUnitPrice().multiply(BigDecimal.valueOf(inventory.getCurrentStock())));
            }
            // 自动判断库存状态
            if (inventory.getCurrentStock() != null && inventory.getMinStock() != null && inventory.getMaxStock() != null) {
                if (inventory.getCurrentStock() <= 0) inventory.setStockStatus("缺货");
                else if (inventory.getCurrentStock() < inventory.getMinStock()) inventory.setStockStatus("偏低");
                else if (inventory.getCurrentStock() > inventory.getMaxStock()) inventory.setStockStatus("过高");
                else inventory.setStockStatus("正常");
            }
            inventory.setLastUpdateDate(java.time.LocalDate.now());
            inventoryMapper.updateById(inventory);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新库存物料失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除库存物料")
    @DeleteMapping("/inventory/{id}")
    public R<String> inventoryDelete(@PathVariable String id) {
        try {
            inventoryMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除库存物料失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "出入库操作")
    @PostMapping("/inventory/stockInOut")
    public R<String> inventoryStockInOut(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            String type = (String) params.get("type"); // in 或 out
            Integer quantity = Integer.parseInt(params.get("quantity").toString());
            GzctSupplyInventory inventory = inventoryMapper.selectById(id);
            if (inventory == null) return R.fail("物料不存在");
            if ("in".equals(type)) {
                inventory.setCurrentStock(inventory.getCurrentStock() + quantity);
            } else if ("out".equals(type)) {
                if (inventory.getCurrentStock() < quantity) return R.fail("库存不足");
                inventory.setCurrentStock(inventory.getCurrentStock() - quantity);
            }
            // 重新计算总价值和状态
            if (inventory.getUnitPrice() != null) {
                inventory.setTotalValue(inventory.getUnitPrice().multiply(BigDecimal.valueOf(inventory.getCurrentStock())));
            }
            if (inventory.getCurrentStock() <= 0) inventory.setStockStatus("缺货");
            else if (inventory.getCurrentStock() < inventory.getMinStock()) inventory.setStockStatus("偏低");
            else if (inventory.getCurrentStock() > inventory.getMaxStock()) inventory.setStockStatus("过高");
            else inventory.setStockStatus("正常");
            inventory.setLastUpdateDate(java.time.LocalDate.now());
            inventory.setUpdateTime(LocalDateTime.now());
            inventoryMapper.updateById(inventory);
            return R.success("操作成功");
        } catch (Exception e) {
            log.error("出入库操作失败", e);
            return R.fail("操作失败: " + e.getMessage());
        }
    }

    @Operation(summary = "库存分析统计")
    @GetMapping("/inventory/analysis")
    public R<Map<String, Object>> inventoryAnalysis() {
        try {
            Map<String, Object> analysis = new HashMap<>();
            List<GzctSupplyInventory> allList = inventoryMapper.selectList(new LambdaQueryWrapper<>());

            // 总库存价值
            BigDecimal totalValue = allList.stream()
                .map(GzctSupplyInventory::getTotalValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            analysis.put("totalValue", totalValue);
            analysis.put("totalCount", allList.size());

            // 总库存数量
            int totalStock = allList.stream()
                .mapToInt(i -> i.getCurrentStock() != null ? i.getCurrentStock() : 0)
                .sum();
            analysis.put("totalStock", totalStock);

            // 按分类统计
            Map<String, Long> categoryStats = new HashMap<>();
            Map<String, BigDecimal> categoryValueStats = new HashMap<>();
            for (GzctSupplyInventory item : allList) {
                String cat = item.getCategory() != null ? item.getCategory() : "其他";
                categoryStats.merge(cat, 1L, Long::sum);
                categoryValueStats.merge(cat, item.getTotalValue() != null ? item.getTotalValue() : BigDecimal.ZERO, BigDecimal::add);
            }
            analysis.put("categoryStats", categoryStats);
            analysis.put("categoryValueStats", categoryValueStats);

            // 按库存状态统计
            Map<String, Long> statusStats = new HashMap<>();
            for (GzctSupplyInventory item : allList) {
                String status = item.getStockStatus() != null ? item.getStockStatus() : "未知";
                statusStats.merge(status, 1L, Long::sum);
            }
            analysis.put("statusStats", statusStats);

            // 按仓库统计
            Map<String, Long> warehouseStats = new HashMap<>();
            for (GzctSupplyInventory item : allList) {
                String wh = item.getWarehouse() != null ? item.getWarehouse() : "未分配";
                warehouseStats.merge(wh, 1L, Long::sum);
            }
            analysis.put("warehouseStats", warehouseStats);

            return R.success(analysis);
        } catch (Exception e) {
            log.error("库存分析失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "库存预警列表")
    @GetMapping("/inventory/alert")
    public R<List<GzctSupplyInventory>> inventoryAlert() {
        try {
            // 查询库存异常的物料：缺货、偏低、过高
            List<GzctSupplyInventory> alertList = inventoryMapper.selectList(
                new LambdaQueryWrapper<GzctSupplyInventory>()
                    .in(GzctSupplyInventory::getStockStatus, "缺货", "偏低", "过高")
                    .orderByAsc(GzctSupplyInventory::getCurrentStock)
            );
            return R.success(alertList);
        } catch (Exception e) {
            log.error("查询库存预警失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    // ==================== 质量管理 ====================
    @Operation(summary = "分页查询质检列表")
    @PostMapping("/quality/list")
    public R<PageResult<GzctSupplyQuality>> qualityList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = 1;
            if (params.get("pageNumber") != null) currentPage = Integer.parseInt(params.get("pageNumber").toString());
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<GzctSupplyQuality> wrapper = new LambdaQueryWrapper<>();
            if (params.get("inspectionNo") != null && StringUtils.hasText(params.get("inspectionNo").toString())) {
                wrapper.like(GzctSupplyQuality::getInspectionNo, params.get("inspectionNo").toString());
            }
            if (params.get("materialName") != null && StringUtils.hasText(params.get("materialName").toString())) {
                wrapper.like(GzctSupplyQuality::getMaterialName, params.get("materialName").toString());
            }
            if (params.get("supplierName") != null && StringUtils.hasText(params.get("supplierName").toString())) {
                wrapper.like(GzctSupplyQuality::getSupplierName, params.get("supplierName").toString());
            }
            if (params.get("inspectionType") != null && StringUtils.hasText(params.get("inspectionType").toString())) {
                wrapper.eq(GzctSupplyQuality::getInspectionType, params.get("inspectionType").toString());
            }
            if (params.get("result") != null && StringUtils.hasText(params.get("result").toString())) {
                wrapper.eq(GzctSupplyQuality::getResult, params.get("result").toString());
            }
            wrapper.orderByDesc(GzctSupplyQuality::getCreateTime);

            Page<GzctSupplyQuality> page = new Page<>(currentPage, pageSize);
            Page<GzctSupplyQuality> result = qualityMapper.selectPage(page, wrapper);
            return R.success(PageResult.of(result));
        } catch (Exception e) {
            log.error("查询质检列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取质检详情")
    @GetMapping("/quality/{id}")
    public R<GzctSupplyQuality> qualityDetail(@PathVariable String id) {
        try {
            return R.success(qualityMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询质检详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增质检记录")
    @PostMapping("/quality/add")
    public R<String> qualityAdd(@RequestBody GzctSupplyQuality quality) {
        try {
            quality.setCreateTime(LocalDateTime.now());
            // 自动计算合格率
            if (quality.getQuantity() != null && quality.getQualifiedQuantity() != null && quality.getQuantity() > 0) {
                BigDecimal rate = BigDecimal.valueOf(quality.getQualifiedQuantity())
                    .divide(BigDecimal.valueOf(quality.getQuantity()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                quality.setQualificationRate(rate);
            }
            qualityMapper.insert(quality);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增质检记录失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新质检记录")
    @PostMapping("/quality/update")
    public R<String> qualityUpdate(@RequestBody GzctSupplyQuality quality) {
        try {
            quality.setUpdateTime(LocalDateTime.now());
            if (quality.getQuantity() != null && quality.getQualifiedQuantity() != null && quality.getQuantity() > 0) {
                BigDecimal rate = BigDecimal.valueOf(quality.getQualifiedQuantity())
                    .divide(BigDecimal.valueOf(quality.getQuantity()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                quality.setQualificationRate(rate);
            }
            qualityMapper.updateById(quality);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新质检记录失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除质检记录")
    @DeleteMapping("/quality/{id}")
    public R<String> qualityDelete(@PathVariable String id) {
        try {
            qualityMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除质检记录失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "执行检验")
    @PostMapping("/quality/inspect")
    public R<String> qualityInspect(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            String result = (String) params.get("result");
            Integer qualifiedQuantity = params.get("qualifiedQuantity") != null ? Integer.parseInt(params.get("qualifiedQuantity").toString()) : null;
            GzctSupplyQuality quality = qualityMapper.selectById(id);
            if (quality == null) return R.fail("质检记录不存在");
            quality.setResult(result != null ? result : "合格");
            if (qualifiedQuantity != null) {
                quality.setQualifiedQuantity(qualifiedQuantity);
                if (quality.getQuantity() != null && quality.getQuantity() > 0) {
                    BigDecimal rate = BigDecimal.valueOf(qualifiedQuantity)
                        .divide(BigDecimal.valueOf(quality.getQuantity()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                    quality.setQualificationRate(rate);
                }
            }
            quality.setInspectionDate(java.time.LocalDate.now());
            quality.setUpdateTime(LocalDateTime.now());
            qualityMapper.updateById(quality);
            return R.success("检验完成");
        } catch (Exception e) {
            log.error("执行检验失败", e);
            return R.fail("检验失败: " + e.getMessage());
        }
    }

    @Operation(summary = "质量分析统计")
    @GetMapping("/quality/analysis")
    public R<Map<String, Object>> qualityAnalysis() {
        try {
            Map<String, Object> analysis = new HashMap<>();
            List<GzctSupplyQuality> allList = qualityMapper.selectList(new LambdaQueryWrapper<>());
            analysis.put("totalCount", allList.size());

            // 合格率统计
            long qualifiedCount = allList.stream().filter(q -> "合格".equals(q.getResult())).count();
            long unqualifiedCount = allList.stream().filter(q -> "不合格".equals(q.getResult())).count();
            analysis.put("qualifiedCount", qualifiedCount);
            analysis.put("unqualifiedCount", unqualifiedCount);
            if (!allList.isEmpty()) {
                analysis.put("overallQualificationRate", BigDecimal.valueOf(qualifiedCount).divide(BigDecimal.valueOf(allList.size()), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
            } else {
                analysis.put("overallQualificationRate", BigDecimal.ZERO);
            }

            // 按检验类型统计
            Map<String, Long> typeStats = new HashMap<>();
            for (GzctSupplyQuality item : allList) {
                String type = item.getInspectionType() != null ? item.getInspectionType() : "其他";
                typeStats.merge(type, 1L, Long::sum);
            }
            analysis.put("typeStats", typeStats);

            // 按结果统计
            Map<String, Long> resultStats = new HashMap<>();
            for (GzctSupplyQuality item : allList) {
                String result = item.getResult() != null ? item.getResult() : "未知";
                resultStats.merge(result, 1L, Long::sum);
            }
            analysis.put("resultStats", resultStats);

            // 按供应商统计不合格数TOP5
            Map<String, Long> supplierUnqualifiedMap = new HashMap<>();
            for (GzctSupplyQuality item : allList) {
                if ("不合格".equals(item.getResult())) {
                    String supplier = item.getSupplierName() != null ? item.getSupplierName() : "未知";
                    supplierUnqualifiedMap.merge(supplier, 1L, Long::sum);
                }
            }
            List<Map<String, Object>> supplierTop5 = supplierUnqualifiedMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(e -> { Map<String, Object> m = new HashMap<>(); m.put("supplier", e.getKey()); m.put("count", e.getValue()); return m; })
                .collect(Collectors.toList());
            analysis.put("supplierUnqualifiedTop5", supplierTop5);

            return R.success(analysis);
        } catch (Exception e) {
            log.error("质量分析失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "质量标准列表")
    @GetMapping("/quality/standards")
    public R<List<Map<String, Object>>> qualityStandards() {
        try {
            // 从已有质检记录中提取各检验类型的质量标准
            List<GzctSupplyQuality> allList = qualityMapper.selectList(new LambdaQueryWrapper<>());
            Map<String, List<GzctSupplyQuality>> typeGroup = new HashMap<>();
            for (GzctSupplyQuality item : allList) {
                String type = item.getInspectionType() != null ? item.getInspectionType() : "其他";
                typeGroup.computeIfAbsent(type, k -> new ArrayList<>()).add(item);
            }
            List<Map<String, Object>> standards = new ArrayList<>();
            for (Map.Entry<String, List<GzctSupplyQuality>> entry : typeGroup.entrySet()) {
                Map<String, Object> std = new HashMap<>();
                std.put("inspectionType", entry.getKey());
                std.put("totalInspections", entry.getValue().size());
                // 计算该类型的平均合格率
                BigDecimal avgRate = entry.getValue().stream()
                    .map(GzctSupplyQuality::getQualificationRate)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (!entry.getValue().isEmpty()) {
                    avgRate = avgRate.divide(BigDecimal.valueOf(entry.getValue().size()), 2, RoundingMode.HALF_UP);
                }
                std.put("avgQualificationRate", avgRate);
                std.put("standardRate", BigDecimal.valueOf(95)); // 标准合格率95%
                std.put("isPass", avgRate.compareTo(BigDecimal.valueOf(95)) >= 0);
                standards.add(std);
            }
            return R.success(standards);
        } catch (Exception e) {
            log.error("获取质量标准失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    // ==================== 风险管控 ====================
    @Operation(summary = "分页查询供应链风险列表")
    @PostMapping("/risk/list")
    public R<PageResult<GzctSupplyRisk>> riskList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = 1;
            if (params.get("pageNumber") != null) currentPage = Integer.parseInt(params.get("pageNumber").toString());
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<GzctSupplyRisk> wrapper = new LambdaQueryWrapper<>();
            if (params.get("riskNo") != null && StringUtils.hasText(params.get("riskNo").toString())) {
                wrapper.like(GzctSupplyRisk::getRiskNo, params.get("riskNo").toString());
            }
            if (params.get("riskName") != null && StringUtils.hasText(params.get("riskName").toString())) {
                wrapper.like(GzctSupplyRisk::getRiskName, params.get("riskName").toString());
            }
            if (params.get("supplierName") != null && StringUtils.hasText(params.get("supplierName").toString())) {
                wrapper.like(GzctSupplyRisk::getSupplierName, params.get("supplierName").toString());
            }
            if (params.get("riskType") != null && StringUtils.hasText(params.get("riskType").toString())) {
                wrapper.eq(GzctSupplyRisk::getRiskType, params.get("riskType").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.hasText(params.get("riskLevel").toString())) {
                wrapper.eq(GzctSupplyRisk::getRiskLevel, params.get("riskLevel").toString());
            }
            if (params.get("status") != null && StringUtils.hasText(params.get("status").toString())) {
                wrapper.eq(GzctSupplyRisk::getStatus, params.get("status").toString());
            }
            wrapper.orderByDesc(GzctSupplyRisk::getCreateTime);

            Page<GzctSupplyRisk> page = new Page<>(currentPage, pageSize);
            Page<GzctSupplyRisk> result = riskMapper.selectPage(page, wrapper);
            return R.success(PageResult.of(result));
        } catch (Exception e) {
            log.error("查询供应链风险列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取风险详情")
    @GetMapping("/risk/{id}")
    public R<GzctSupplyRisk> riskDetail(@PathVariable String id) {
        try {
            return R.success(riskMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询风险详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增风险记录")
    @PostMapping("/risk/add")
    public R<String> riskAdd(@RequestBody GzctSupplyRisk risk) {
        try {
            risk.setCreateTime(LocalDateTime.now());
            if (!StringUtils.hasText(risk.getStatus())) {
                risk.setStatus("已识别");
            }
            riskMapper.insert(risk);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增风险记录失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新风险记录")
    @PostMapping("/risk/update")
    public R<String> riskUpdate(@RequestBody GzctSupplyRisk risk) {
        try {
            risk.setUpdateTime(LocalDateTime.now());
            riskMapper.updateById(risk);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新风险记录失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除风险记录")
    @DeleteMapping("/risk/{id}")
    public R<String> riskDelete(@PathVariable String id) {
        try {
            riskMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除风险记录失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "处理风险")
    @PostMapping("/risk/handle")
    public R<String> riskHandle(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            String handleResult = (String) params.get("handleResult");
            String status = (String) params.get("status");
            GzctSupplyRisk risk = riskMapper.selectById(id);
            if (risk == null) return R.fail("风险记录不存在");
            risk.setHandleResult(handleResult);
            risk.setStatus(status != null ? status : "已解决");
            risk.setUpdateTime(LocalDateTime.now());
            riskMapper.updateById(risk);
            return R.success("处理成功");
        } catch (Exception e) {
            log.error("处理风险失败", e);
            return R.fail("处理失败: " + e.getMessage());
        }
    }

    @Operation(summary = "风险分析统计")
    @GetMapping("/risk/analysis")
    public R<Map<String, Object>> riskAnalysis() {
        try {
            Map<String, Object> analysis = new HashMap<>();
            List<GzctSupplyRisk> allList = riskMapper.selectList(new LambdaQueryWrapper<>());
            analysis.put("totalCount", allList.size());

            // 按风险等级统计
            Map<String, Long> levelStats = new HashMap<>();
            for (GzctSupplyRisk item : allList) {
                String level = item.getRiskLevel() != null ? item.getRiskLevel() : "未知";
                levelStats.merge(level, 1L, Long::sum);
            }
            analysis.put("levelStats", levelStats);

            // 按风险类型统计
            Map<String, Long> typeStats = new HashMap<>();
            for (GzctSupplyRisk item : allList) {
                String type = item.getRiskType() != null ? item.getRiskType() : "其他";
                typeStats.merge(type, 1L, Long::sum);
            }
            analysis.put("typeStats", typeStats);

            // 按状态统计
            Map<String, Long> statusStats = new HashMap<>();
            for (GzctSupplyRisk item : allList) {
                String status = item.getStatus() != null ? item.getStatus() : "未知";
                statusStats.merge(status, 1L, Long::sum);
            }
            analysis.put("statusStats", statusStats);

            // 高风险供应商TOP5
            Map<String, Integer> supplierScoreMap = new HashMap<>();
            Map<String, Long> supplierCountMap = new HashMap<>();
            for (GzctSupplyRisk item : allList) {
                String supplier = item.getSupplierName() != null ? item.getSupplierName() : "未知";
                supplierCountMap.merge(supplier, 1L, Long::sum);
                supplierScoreMap.merge(supplier, item.getRiskScore() != null ? item.getRiskScore() : 0, Integer::sum);
            }
            List<Map<String, Object>> supplierTop5 = supplierScoreMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(e -> { Map<String, Object> m = new HashMap<>(); m.put("supplier", e.getKey()); m.put("totalScore", e.getValue()); m.put("riskCount", supplierCountMap.get(e.getKey())); return m; })
                .collect(Collectors.toList());
            analysis.put("supplierRiskTop5", supplierTop5);

            // 平均风险评分
            double avgScore = allList.stream()
                .mapToInt(i -> i.getRiskScore() != null ? i.getRiskScore() : 0)
                .average().orElse(0);
            analysis.put("avgRiskScore", BigDecimal.valueOf(avgScore).setScale(1, RoundingMode.HALF_UP));

            return R.success(analysis);
        } catch (Exception e) {
            log.error("风险分析失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "风险监控(未解决的高风险项)")
    @GetMapping("/risk/monitor")
    public R<List<GzctSupplyRisk>> riskMonitor() {
        try {
            List<GzctSupplyRisk> monitorList = riskMapper.selectList(
                new LambdaQueryWrapper<GzctSupplyRisk>()
                    .ne(GzctSupplyRisk::getStatus, "已解决")
                    .orderByDesc(GzctSupplyRisk::getRiskScore)
            );
            return R.success(monitorList);
        } catch (Exception e) {
            log.error("查询风险监控失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    // ==================== 供应商评估 ====================
    @Operation(summary = "供应商评估")
    @PostMapping("/evaluate")
    public R<String> evaluate(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            GzctEnterpriseSupply supply = supplyMapper.selectById(id);
            if (supply == null) return R.fail("供应商不存在");
            if (params.get("qualityScore") != null) supply.setQualityScore(new BigDecimal(params.get("qualityScore").toString()));
            if (params.get("deliveryScore") != null) supply.setDeliveryScore(new BigDecimal(params.get("deliveryScore").toString()));
            if (params.get("priceScore") != null) supply.setPriceScore(new BigDecimal(params.get("priceScore").toString()));
            // 计算综合评分
            BigDecimal overall = BigDecimal.ZERO;
            int count = 0;
            if (supply.getQualityScore() != null) { overall = overall.add(supply.getQualityScore()); count++; }
            if (supply.getDeliveryScore() != null) { overall = overall.add(supply.getDeliveryScore()); count++; }
            if (supply.getPriceScore() != null) { overall = overall.add(supply.getPriceScore()); count++; }
            if (count > 0) supply.setOverallScore(overall.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP));
            // 根据综合评分设置评级
            if (supply.getOverallScore() != null) {
                double score = supply.getOverallScore().doubleValue();
                if (score >= 90) supply.setRating("A");
                else if (score >= 75) supply.setRating("B");
                else if (score >= 60) supply.setRating("C");
                else supply.setRating("D");
            }
            supply.setUpdateTime(LocalDateTime.now());
            supplyMapper.updateById(supply);
            return R.success("评估成功");
        } catch (Exception e) {
            log.error("供应商评估失败", e);
            return R.fail("评估失败: " + e.getMessage());
        }
    }

    // ==================== 供应商列表(下拉选项) ====================
    /**
     * 获取供应商下拉列表。
     *
     * scope 语义：
     * - 不传(null) 或 "supplier"：返回 供应商主档 gzct_enterprise_supply（新增/编辑对话框场景）
     * - "procurement" / "contract" / "quality" / "risk"：返回对应业务表中已存在的 supplierName distinct 值
     *   （查询过滤场景，避免下拉与实际记录不匹配）
     */
    @Operation(summary = "获取供应商下拉列表")
    @GetMapping("/options")
    public R<List<Map<String, String>>> supplierOptions(@RequestParam(required = false) String scope) {
        try {
            List<Map<String, String>> options;
            if (!StringUtils.hasText(scope) || "supplier".equalsIgnoreCase(scope)) {
                // 默认：供应商主档
                List<GzctEnterpriseSupply> list = supplyMapper.selectList(
                        new LambdaQueryWrapper<GzctEnterpriseSupply>()
                                .select(GzctEnterpriseSupply::getId, GzctEnterpriseSupply::getSupplierName)
                                .orderByAsc(GzctEnterpriseSupply::getSupplierName)
                );
                options = new ArrayList<>();
                for (GzctEnterpriseSupply item : list) {
                    Map<String, String> opt = new HashMap<>();
                    opt.put("id", item.getId());
                    opt.put("name", item.getSupplierName());
                    options.add(opt);
                }
            } else {
                // 按业务表汇总 distinct supplierName
                Set<String> names = new TreeSet<>();
                switch (scope.toLowerCase()) {
                    case "procurement":
                        procurementMapper.selectList(
                                new LambdaQueryWrapper<GzctSupplyProcurement>()
                                        .select(GzctSupplyProcurement::getSupplierName)
                                        .isNotNull(GzctSupplyProcurement::getSupplierName)
                        ).forEach(item -> collectSupplierName(names, item.getSupplierName()));
                        break;
                    case "contract":
                        contractMapper.selectList(
                                new LambdaQueryWrapper<GzctSupplyContract>()
                                        .select(GzctSupplyContract::getSupplierName)
                                        .isNotNull(GzctSupplyContract::getSupplierName)
                        ).forEach(item -> collectSupplierName(names, item.getSupplierName()));
                        break;
                    case "quality":
                        qualityMapper.selectList(
                                new LambdaQueryWrapper<GzctSupplyQuality>()
                                        .select(GzctSupplyQuality::getSupplierName)
                                        .isNotNull(GzctSupplyQuality::getSupplierName)
                        ).forEach(item -> collectSupplierName(names, item.getSupplierName()));
                        break;
                    case "risk":
                        riskMapper.selectList(
                                new LambdaQueryWrapper<GzctSupplyRisk>()
                                        .select(GzctSupplyRisk::getSupplierName)
                                        .isNotNull(GzctSupplyRisk::getSupplierName)
                        ).forEach(item -> collectSupplierName(names, item.getSupplierName()));
                        break;
                    default:
                        return R.fail("非法 scope 参数: " + scope);
                }
                options = new ArrayList<>();
                int idx = 0;
                for (String name : names) {
                    Map<String, String> opt = new HashMap<>();
                    // 该场景无 supplier 主键，用序号占位即可（前端仅作 :key 用）
                    opt.put("id", scope + "_" + idx++);
                    opt.put("name", name);
                    options.add(opt);
                }
            }
            return R.success(options);
        } catch (Exception e) {
            log.error("获取供应商下拉列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    private void collectSupplierName(Set<String> bucket, String name) {
        if (StringUtils.hasText(name)) {
            bucket.add(name.trim());
        }
    }
}
