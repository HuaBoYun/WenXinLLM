package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.consolidationReport.dto.ConsolidatedReportQueryParam;
import com.financial.sharing.consolidationReport.dto.ReportCompareResult;
import com.financial.sharing.consolidationReport.entity.TblConsolidatedReport;
import com.financial.sharing.consolidationReport.entity.TblEliminationVoucher;
import com.financial.sharing.consolidationReport.mapper.ConsolidatedReportMapper;
import com.financial.sharing.consolidationReport.mapper.EliminationVoucherMapper;
import com.financial.sharing.consolidationReport.service.ConsolidatedReportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

/**
 * 合并报表Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ConsolidatedReportServiceImpl implements ConsolidatedReportService {

    @Autowired
    private ConsolidatedReportMapper reportMapper;

    @Autowired
    private EliminationVoucherMapper voucherMapper;

    @Override
    public PageInfo<TblConsolidatedReport> getReportList(ConsolidatedReportQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblConsolidatedReport> list = reportMapper.selectReportList(param);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblConsolidatedReport> getReportListNoPage(ConsolidatedReportQueryParam param) {
        return reportMapper.selectReportList(param);
    }

    @Override
    public TblConsolidatedReport getReportById(String reportId) {
        return reportMapper.selectById(reportId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> generateReport(String modelId, String period, String reportType, boolean regenerate) {
        String tenantId = UserUtils.getTenantId().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        Map<String, Object> result = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();

        try {
            // 如果重新生成,先删除旧数据
            if (regenerate) {
                reportMapper.deleteByModelIdAndPeriodAndType(modelId, period, reportType);
            }

            // 确定要生成的报表类型
            List<String> reportTypes = new ArrayList<>();
            if (reportType != null && !reportType.isEmpty()) {
                reportTypes.add(reportType);
            } else {
                // 生成所有类型
                reportTypes.add("BALANCE_SHEET");
                reportTypes.add("INCOME_STATEMENT");
                reportTypes.add("CASH_FLOW");
            }

            int totalCount = 0;
            int successCount = 0;

            for (String type : reportTypes) {
                try {
                    // 生成指定类型的报表
                    List<TblConsolidatedReport> reportList = generateReportByType(modelId, period, type, tenantId, userId, now);
                    
                    if (!reportList.isEmpty()) {
                        // 批量插入
                        reportMapper.batchInsert(reportList);
                        successCount++;
                    }
                    totalCount++;
                } catch (Exception e) {
                    errorMessages.add("生成" + getReportTypeName(type) + "失败: " + e.getMessage());
                }
            }

            result.put("success", true);
            result.put("totalCount", totalCount);
            result.put("successCount", successCount);
            result.put("errorCount", totalCount - successCount);
            result.put("errorMessages", errorMessages);
            result.put("message", "生成完成,共" + totalCount + "个报表类型,成功" + successCount + "个,失败" + (totalCount - successCount) + "个");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "生成失败: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReport(String modelId, String period, String reportType) {
        reportMapper.deleteByModelIdAndPeriodAndType(modelId, period, reportType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReport(String modelId, String period, String reportType) {
        // 查询要确认的报表
        ConsolidatedReportQueryParam param = new ConsolidatedReportQueryParam();
        param.setModelId(modelId);
        param.setPeriod(period);
        param.setReportType(reportType);
        List<TblConsolidatedReport> reportList = reportMapper.selectReportList(param);

        // 更新状态为已确认
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        for (TblConsolidatedReport report : reportList) {
            report.setStatus("CONFIRMED");
            report.setUpdateUser(userId);
            report.setUpdateTime(now);
            reportMapper.updateById(report);
        }
    }

    @Override
    public List<String> getReportTypeList(String modelId, String period) {
        return reportMapper.selectReportTypeList(modelId, period);
    }

    /**
     * 生成指定类型的报表
     */
    private List<TblConsolidatedReport> generateReportByType(String modelId, String period, String reportType,
                                                              String tenantId, String userId, Date now) {
        List<TblConsolidatedReport> reportList = new ArrayList<>();

        // 获取报表模板(项目列表)
        List<Map<String, Object>> templateItems = getReportTemplate(reportType);

        // 查询抵消凭证
        QueryWrapper<TblEliminationVoucher> voucherWrapper = new QueryWrapper<>();
        voucherWrapper.eq("MODEL_ID", modelId);
        voucherWrapper.eq("PERIOD", period);
        voucherWrapper.eq("STATUS", "CONFIRMED");
        List<TblEliminationVoucher> voucherList = voucherMapper.selectList(voucherWrapper);

        // 计算抵消金额汇总(按科目)
        Map<String, BigDecimal> eliminationMap = calculateEliminationAmount(voucherList);

        // 生成报表数据
        for (Map<String, Object> item : templateItems) {
            TblConsolidatedReport report = new TblConsolidatedReport();
            report.setReportId(UUID.randomUUID().toString().replace("-", ""));
            report.setModelId(modelId);
            report.setPeriod(period);
            report.setReportType(reportType);
            report.setItemCode((String) item.get("itemCode"));
            report.setItemName((String) item.get("itemName"));
            report.setItemLevel((Integer) item.get("itemLevel"));
            report.setParentCode((String) item.get("parentCode"));
            report.setSortOrder((Integer) item.get("sortOrder"));

            // 这里使用模拟数据,实际应该从财务数据中获取
            // 母公司金额
            report.setParentAmount(new BigDecimal("1000000"));
            // 子公司金额
            report.setSubsidiaryAmount(new BigDecimal("500000"));
            // 抵消金额(从抵消凭证中获取)
            String itemCode = (String) item.get("itemCode");
            BigDecimal eliminationAmount = eliminationMap.getOrDefault(itemCode, BigDecimal.ZERO);
            report.setEliminationAmount(eliminationAmount);
            // 合并金额 = 母公司金额 + 子公司金额 - 抵消金额
            BigDecimal consolidatedAmount = report.getParentAmount()
                    .add(report.getSubsidiaryAmount())
                    .subtract(eliminationAmount);
            report.setConsolidatedAmount(consolidatedAmount);

            report.setStatus("DRAFT");
            report.setTenantId(tenantId);
            report.setCreateUser(userId);
            report.setCreateTime(now);
            report.setUpdateUser(userId);
            report.setUpdateTime(now);

            reportList.add(report);
        }

        return reportList;
    }

    /**
     * 获取报表模板
     */
    private List<Map<String, Object>> getReportTemplate(String reportType) {
        List<Map<String, Object>> template = new ArrayList<>();

        if ("BALANCE_SHEET".equals(reportType)) {
            // 资产负债表模板
            template.add(createTemplateItem("1000", "资产", 1, null, 1));
            template.add(createTemplateItem("1100", "流动资产", 2, "1000", 2));
            template.add(createTemplateItem("1101", "货币资金", 3, "1100", 3));
            template.add(createTemplateItem("1102", "应收账款", 3, "1100", 4));
            template.add(createTemplateItem("1103", "存货", 3, "1100", 5));
            template.add(createTemplateItem("1200", "非流动资产", 2, "1000", 6));
            template.add(createTemplateItem("1201", "固定资产", 3, "1200", 7));
            template.add(createTemplateItem("1202", "无形资产", 3, "1200", 8));
            template.add(createTemplateItem("2000", "负债", 1, null, 9));
            template.add(createTemplateItem("2100", "流动负债", 2, "2000", 10));
            template.add(createTemplateItem("2101", "应付账款", 3, "2100", 11));
            template.add(createTemplateItem("2102", "短期借款", 3, "2100", 12));
            template.add(createTemplateItem("2200", "非流动负债", 2, "2000", 13));
            template.add(createTemplateItem("2201", "长期借款", 3, "2200", 14));
            template.add(createTemplateItem("3000", "所有者权益", 1, null, 15));
            template.add(createTemplateItem("3100", "实收资本", 2, "3000", 16));
            template.add(createTemplateItem("3200", "未分配利润", 2, "3000", 17));
        } else if ("INCOME_STATEMENT".equals(reportType)) {
            // 利润表模板
            template.add(createTemplateItem("4000", "营业收入", 1, null, 1));
            template.add(createTemplateItem("4100", "主营业务收入", 2, "4000", 2));
            template.add(createTemplateItem("4200", "其他业务收入", 2, "4000", 3));
            template.add(createTemplateItem("5000", "营业成本", 1, null, 4));
            template.add(createTemplateItem("5100", "主营业务成本", 2, "5000", 5));
            template.add(createTemplateItem("5200", "其他业务成本", 2, "5000", 6));
            template.add(createTemplateItem("6000", "营业利润", 1, null, 7));
            template.add(createTemplateItem("7000", "利润总额", 1, null, 8));
            template.add(createTemplateItem("8000", "净利润", 1, null, 9));
        } else if ("CASH_FLOW".equals(reportType)) {
            // 现金流量表模板
            template.add(createTemplateItem("9000", "经营活动现金流量", 1, null, 1));
            template.add(createTemplateItem("9100", "销售商品、提供劳务收到的现金", 2, "9000", 2));
            template.add(createTemplateItem("9200", "购买商品、接受劳务支付的现金", 2, "9000", 3));
            template.add(createTemplateItem("9300", "经营活动现金流量净额", 2, "9000", 4));
            template.add(createTemplateItem("A000", "投资活动现金流量", 1, null, 5));
            template.add(createTemplateItem("A100", "购建固定资产支付的现金", 2, "A000", 6));
            template.add(createTemplateItem("A200", "投资活动现金流量净额", 2, "A000", 7));
            template.add(createTemplateItem("B000", "筹资活动现金流量", 1, null, 8));
            template.add(createTemplateItem("B100", "吸收投资收到的现金", 2, "B000", 9));
            template.add(createTemplateItem("B200", "筹资活动现金流量净额", 2, "B000", 10));
            template.add(createTemplateItem("C000", "现金及现金等价物净增加额", 1, null, 11));
        }

        return template;
    }

    /**
     * 创建模板项目
     */
    private Map<String, Object> createTemplateItem(String itemCode, String itemName,
                                                    int itemLevel, String parentCode, int sortOrder) {
        Map<String, Object> item = new HashMap<>();
        item.put("itemCode", itemCode);
        item.put("itemName", itemName);
        item.put("itemLevel", itemLevel);
        item.put("parentCode", parentCode);
        item.put("sortOrder", sortOrder);
        return item;
    }

    /**
     * 计算抵消金额汇总
     */
    private Map<String, BigDecimal> calculateEliminationAmount(List<TblEliminationVoucher> voucherList) {
        Map<String, BigDecimal> eliminationMap = new HashMap<>();

        for (TblEliminationVoucher voucher : voucherList) {
            String accountCode = voucher.getAccountCode();
            BigDecimal amount = voucher.getAmount();

            if (accountCode != null && amount != null) {
                // 根据分录类型确定金额的正负
                if ("DEBIT".equals(voucher.getEntryType())) {
                    // 借方金额为正
                    eliminationMap.put(accountCode,
                        eliminationMap.getOrDefault(accountCode, BigDecimal.ZERO).add(amount));
                } else {
                    // 贷方金额为负
                    eliminationMap.put(accountCode,
                        eliminationMap.getOrDefault(accountCode, BigDecimal.ZERO).subtract(amount));
                }
            }
        }

        return eliminationMap;
    }

    /**
     * 获取报表类型名称
     */
    private String getReportTypeName(String reportType) {
        if (reportType == null) {
            return "未知";
        }
        switch (reportType) {
            case "BALANCE_SHEET":
                return "资产负债表";
            case "INCOME_STATEMENT":
                return "利润表";
            case "CASH_FLOW":
                return "现金流量表";
            default:
                return reportType;
        }
    }

    @Override
    public void exportReportToExcel(String modelId, String period, String reportType, HttpServletResponse response) {
        try {
            // 查询报表数据
            ConsolidatedReportQueryParam param = new ConsolidatedReportQueryParam();
            param.setModelId(modelId);
            param.setPeriod(period);
            param.setReportType(reportType);
            List<TblConsolidatedReport> reportList = reportMapper.selectReportList(param);

            if (reportList.isEmpty()) {
                throw new RuntimeException("没有找到报表数据");
            }

            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(getReportTypeName(reportType));

            // 创建样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle amountStyle = createAmountStyle(workbook);

            // 创建标题行
            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("合并报表 - " + getReportTypeName(reportType));
            titleRow.getCell(0).setCellStyle(headerStyle);

            // 创建信息行
            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue("期间: " + period);
            infoRow.getCell(0).setCellStyle(dataStyle);

            // 创建表头
            Row headerRow = sheet.createRow(3);
            String[] headers = {"项目编码", "项目名称", "母公司金额", "子公司金额", "抵消金额", "合并金额"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 填充数据
            int rowNum = 4;
            for (TblConsolidatedReport report : reportList) {
                Row row = sheet.createRow(rowNum++);

                // 根据层级缩进项目名称
                String itemName = report.getItemName();
                if (report.getItemLevel() != null && report.getItemLevel() > 1) {
                    StringBuilder indent = new StringBuilder();
                    for (int i = 0; i < report.getItemLevel() - 1; i++) {
                        indent.append("  ");
                    }
                    itemName = indent.toString() + itemName;
                }

                row.createCell(0).setCellValue(report.getItemCode());
                row.createCell(1).setCellValue(itemName);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(report.getParentAmount() != null ? report.getParentAmount().doubleValue() : 0);
                cell2.setCellStyle(amountStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(report.getSubsidiaryAmount() != null ? report.getSubsidiaryAmount().doubleValue() : 0);
                cell3.setCellStyle(amountStyle);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(report.getEliminationAmount() != null ? report.getEliminationAmount().doubleValue() : 0);
                cell4.setCellStyle(amountStyle);

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(report.getConsolidatedAmount() != null ? report.getConsolidatedAmount().doubleValue() : 0);
                cell5.setCellStyle(amountStyle);
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            // 设置响应头
            String fileName = getReportTypeName(reportType) + "_" + period + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            // 写入输出流
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();

        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    @Override
    public List<ReportCompareResult> compareReports(String modelId, String period1, String period2, String reportType) {
        // 查询期间1的报表
        ConsolidatedReportQueryParam param1 = new ConsolidatedReportQueryParam();
        param1.setModelId(modelId);
        param1.setPeriod(period1);
        param1.setReportType(reportType);
        List<TblConsolidatedReport> reportList1 = reportMapper.selectReportList(param1);

        // 查询期间2的报表
        ConsolidatedReportQueryParam param2 = new ConsolidatedReportQueryParam();
        param2.setModelId(modelId);
        param2.setPeriod(period2);
        param2.setReportType(reportType);
        List<TblConsolidatedReport> reportList2 = reportMapper.selectReportList(param2);

        // 构建期间1的数据映射
        Map<String, TblConsolidatedReport> map1 = new HashMap<>();
        for (TblConsolidatedReport report : reportList1) {
            map1.put(report.getItemCode(), report);
        }

        // 构建期间2的数据映射
        Map<String, TblConsolidatedReport> map2 = new HashMap<>();
        for (TblConsolidatedReport report : reportList2) {
            map2.put(report.getItemCode(), report);
        }

        // 获取所有项目编码
        Set<String> allItemCodes = new HashSet<>();
        allItemCodes.addAll(map1.keySet());
        allItemCodes.addAll(map2.keySet());

        // 生成对比结果
        List<ReportCompareResult> compareResults = new ArrayList<>();
        for (String itemCode : allItemCodes) {
            TblConsolidatedReport report1 = map1.get(itemCode);
            TblConsolidatedReport report2 = map2.get(itemCode);

            ReportCompareResult result = new ReportCompareResult();
            result.setItemCode(itemCode);

            if (report1 != null) {
                result.setItemName(report1.getItemName());
                result.setItemLevel(report1.getItemLevel());
                result.setParentCode(report1.getParentCode());
                result.setSortOrder(report1.getSortOrder());
                result.setPeriod1(period1);
                result.setAmount1(report1.getConsolidatedAmount());
            }

            if (report2 != null) {
                if (result.getItemName() == null) {
                    result.setItemName(report2.getItemName());
                    result.setItemLevel(report2.getItemLevel());
                    result.setParentCode(report2.getParentCode());
                    result.setSortOrder(report2.getSortOrder());
                }
                result.setPeriod2(period2);
                result.setAmount2(report2.getConsolidatedAmount());
            }

            // 计算差异
            BigDecimal amount1 = result.getAmount1() != null ? result.getAmount1() : BigDecimal.ZERO;
            BigDecimal amount2 = result.getAmount2() != null ? result.getAmount2() : BigDecimal.ZERO;
            result.setDiffAmount(amount2.subtract(amount1));

            // 计算差异率
            if (amount1.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal diffRate = result.getDiffAmount()
                        .divide(amount1, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                result.setDiffRate(diffRate);
            } else {
                result.setDiffRate(BigDecimal.ZERO);
            }

            compareResults.add(result);
        }

        // 按排序号排序
        compareResults.sort(Comparator.comparing(ReportCompareResult::getSortOrder,
                Comparator.nullsLast(Comparator.naturalOrder())));

        return compareResults;
    }

    /**
     * 创建表头样式
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    /**
     * 创建数据样式
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 创建金额样式
     */
    private CellStyle createAmountStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));

        return style;
    }
}

