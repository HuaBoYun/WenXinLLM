package com.huabo.fxgl.util;

import com.huabo.fxgl.entity.TblRiskWarning;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PDF 报告生成工具类
 * 使用 OpenPDF (iText5 LGPL fork) 生成风险预警报告
 *
 * @author 华博云开发团队
 * @since 2025-11-15
 */
public class PdfReportGenerator {

    private static BaseFont chineseBaseFont;

    static {
        try {
            chineseBaseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException("PDF中文字体初始化失败: " + e.getMessage(), e);
        }
    }

    private static Font titleFont() {
        return new Font(chineseBaseFont, 22, Font.BOLD, new Color(64, 158, 255));
    }
    private static Font h2Font() {
        return new Font(chineseBaseFont, 16, Font.BOLD, new Color(51, 51, 51));
    }
    private static Font normalFont() {
        return new Font(chineseBaseFont, 10, Font.NORMAL, new Color(51, 51, 51));
    }
    private static Font labelFont() {
        return new Font(chineseBaseFont, 10, Font.BOLD, new Color(51, 51, 51));
    }
    private static Font metaFont() {
        return new Font(chineseBaseFont, 9, Font.NORMAL, new Color(102, 102, 102));
    }
    private static Font tableHeaderFont() {
        return new Font(chineseBaseFont, 10, Font.BOLD, Color.WHITE);
    }

    /**
     * 生成PDF报告主入口
     */
    public static void generate(String filePath, Map<String, Object> reportParams,
                                List<TblRiskWarning> warnings, List<String> includeContents) throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // 🔥 根据报告类型计算最终包含的区块
            String reportType = (String) reportParams.get("reportType");
            Set<String> includes = ReportDataHelper.computeIncludes(reportType, includeContents);

            writeTitle(document, reportParams);
            writeMetaInfo(document, reportParams, warnings.size());
            writeDescription(document, reportParams);

            if (includes.contains("STATISTICS")) {
                writeStatistics(document, warnings);
            }
            if (includes.contains("TRENDS")) {
                writeTrends(document, warnings);
            }
            if (includes.contains("PERFORMANCE")) {
                writePerformance(document, warnings);
            }
            if (includes.contains("DETAILS")) {
                writeDetailTable(document, warnings);
            }
            if (includes.contains("RECOMMENDATIONS")) {
                writeRecommendations(document, warnings);
            }
        } finally {
            document.close();
        }
    }

    private static void writeTitle(Document document, Map<String, Object> params) throws DocumentException {
        String title = (String) params.getOrDefault("reportTitle", "风险预警报告");
        Paragraph titlePara = new Paragraph(title, titleFont());
        titlePara.setAlignment(Element.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);
    }

    private static void writeMetaInfo(Document document, Map<String, Object> params, int total) throws DocumentException {
        PdfPTable metaTable = new PdfPTable(4);
        metaTable.setWidthPercentage(100);
        metaTable.setSpacingAfter(15);

        String reportType = ReportDataHelper.getReportTypeText((String) params.get("reportType"));
        String timeRange = ReportDataHelper.getTimeRangeText((String) params.get("timeRange"));
        String generateTime = ReportDataHelper.formatDateTime(LocalDateTime.now());

        addMetaCell(metaTable, "报告类型", reportType);
        addMetaCell(metaTable, "时间范围", timeRange);
        addMetaCell(metaTable, "生成时间", generateTime);
        addMetaCell(metaTable, "数据条数", total + " 条");

        document.add(metaTable);
    }

    private static void addMetaCell(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont()));
        labelCell.setPadding(6);
        labelCell.setBackgroundColor(new Color(245, 247, 250));
        labelCell.setBorderColor(new Color(224, 224, 224));
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, normalFont()));
        valueCell.setPadding(6);
        valueCell.setBorderColor(new Color(224, 224, 224));
        table.addCell(valueCell);
    }

    private static void writeDescription(Document document, Map<String, Object> params) throws DocumentException {
        String description = (String) params.get("reportDescription");
        if (description == null || description.trim().isEmpty()) return;

        Paragraph descPara = new Paragraph(description, metaFont());
        descPara.setIndentationLeft(10);
        descPara.setSpacingAfter(15);
        document.add(descPara);
    }

    private static void writeStatistics(Document document, List<TblRiskWarning> warnings) throws DocumentException {
        Paragraph title = new Paragraph("统计概览", h2Font());
        title.setSpacingBefore(10);
        title.setSpacingAfter(10);
        document.add(title);

        Map<String, Integer> stats = ReportDataHelper.calculateStatistics(warnings);
        PdfPTable statTable = new PdfPTable(6);
        statTable.setWidthPercentage(100);
        statTable.setSpacingAfter(15);

        addStatCell(statTable, "预警总数", stats.get("total"), new Color(103, 194, 58));
        addStatCell(statTable, "高风险", stats.get("high"), new Color(245, 108, 108));
        addStatCell(statTable, "中风险", stats.get("medium"), new Color(230, 162, 60));
        addStatCell(statTable, "低风险", stats.get("low"), new Color(103, 194, 58));
        addStatCell(statTable, "待处理", stats.get("pending"), new Color(64, 158, 255));
        addStatCell(statTable, "已处理", stats.get("processed"), new Color(144, 147, 153));

        document.add(statTable);
    }

    private static void addStatCell(PdfPTable table, String label, int value, Color color) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(10);
        cell.setBackgroundColor(color);
        cell.setBorderColor(color);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setMinimumHeight(50);

        Font numFont = new Font(chineseBaseFont, 18, Font.BOLD, Color.WHITE);
        Font labelFont = new Font(chineseBaseFont, 9, Font.NORMAL, Color.WHITE);
        Paragraph numPara = new Paragraph(String.valueOf(value), numFont);
        numPara.setAlignment(Element.ALIGN_CENTER);
        Paragraph labelPara = new Paragraph(label, labelFont);
        labelPara.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(numPara);
        cell.addElement(labelPara);
        table.addCell(cell);
    }

    private static void writeDetailTable(Document document, List<TblRiskWarning> warnings) throws DocumentException {
        Paragraph title = new Paragraph("详细数据", h2Font());
        title.setSpacingBefore(15);
        title.setSpacingAfter(10);
        document.add(title);

        PdfPTable table = new PdfPTable(new float[]{6, 15, 12, 10, 10, 20, 12, 15});
        table.setWidthPercentage(100);
        table.setSpacingAfter(15);

        String[] headers = {"序号", "预警编码", "预警类型", "预警级别", "预警状态", "企业名称", "预警值", "预警时间"};
        for (String h : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(h, tableHeaderFont()));
            headerCell.setBackgroundColor(new Color(64, 158, 255));
            headerCell.setPadding(8);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBorderColor(new Color(224, 224, 224));
            table.addCell(headerCell);
        }
        table.setHeaderRows(1);

        Color oddBg = new Color(249, 249, 249);
        int rowIndex = 0;
        for (TblRiskWarning w : warnings) {
            rowIndex++;
            Color bg = (rowIndex % 2 == 0) ? oddBg : Color.WHITE;
            addDetailCell(table, String.valueOf(rowIndex), bg);
            addDetailCell(table, safe(w.getWarningCode()), bg);
            addDetailCell(table, ReportDataHelper.getWarningTypeText(w.getWarningType()), bg);
            addDetailCell(table, ReportDataHelper.getWarningLevelText(w.getWarningLevel()), bg);
            addDetailCell(table, ReportDataHelper.getWarningStatusText(w.getWarningStatus()), bg);
            addDetailCell(table, safe(w.getCompanyName()), bg);
            addDetailCell(table, w.getWarningValue() != null ? w.getWarningValue().toString() : "-", bg);
            addDetailCell(table, w.getCreateTime() != null ? ReportDataHelper.formatDate(w.getCreateTime()) : "-", bg);
        }
        document.add(table);
    }

    private static void addDetailCell(PdfPTable table, String value, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(value, normalFont()));
        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setBorderColor(new Color(224, 224, 224));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private static void writeRecommendations(Document document, List<TblRiskWarning> warnings) throws DocumentException {
        Paragraph title = new Paragraph("建议措施", h2Font());
        title.setSpacingBefore(15);
        title.setSpacingAfter(10);
        document.add(title);

        List<String> recommendations = ReportDataHelper.generateRecommendations(warnings);
        for (int i = 0; i < recommendations.size(); i++) {
            Paragraph rec = new Paragraph((i + 1) + ". " + recommendations.get(i), normalFont());
            rec.setIndentationLeft(20);
            rec.setSpacingAfter(6);
            document.add(rec);
        }
    }

    private static void writeTrends(Document document, List<TblRiskWarning> warnings) throws DocumentException {
        Paragraph title = new Paragraph("趋势分析", h2Font());
        title.setSpacingBefore(15);
        title.setSpacingAfter(10);
        document.add(title);

        List<String> insights = ReportDataHelper.generateTrendInsights(warnings);
        for (String insight : insights) {
            Paragraph p = new Paragraph("• " + insight, normalFont());
            p.setIndentationLeft(20);
            p.setSpacingAfter(6);
            document.add(p);
        }
    }

    private static void writePerformance(Document document, List<TblRiskWarning> warnings) throws DocumentException {
        Paragraph title = new Paragraph("处理效率分析", h2Font());
        title.setSpacingBefore(15);
        title.setSpacingAfter(10);
        document.add(title);

        Map<String, Object> perf = ReportDataHelper.calculatePerformance(warnings);
        PdfPTable perfTable = new PdfPTable(4);
        perfTable.setWidthPercentage(100);
        perfTable.setSpacingAfter(15);

        addStatCell(perfTable, "处理率", (String) perf.get("processedRate"),
                new Color(103, 194, 58));
        addStatCell(perfTable, "待处理率", (String) perf.get("pendingRate"),
                new Color(230, 162, 60));
        addStatCell(perfTable, "已忽略率", (String) perf.get("ignoredRate"),
                new Color(144, 147, 153));
        addStatCell(perfTable, "预警总数", (int) perf.get("total"),
                new Color(64, 158, 255));
        document.add(perfTable);
    }

    private static void addStatCell(PdfPTable table, String label, String value, Color color) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(10);
        cell.setBackgroundColor(color);
        cell.setBorderColor(color);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setMinimumHeight(50);

        Font bigFont = new Font(chineseBaseFont, 18, Font.BOLD, Color.WHITE);
        Font smallFont = new Font(chineseBaseFont, 9, Font.NORMAL, Color.WHITE);
        Paragraph valPara = new Paragraph(value, bigFont);
        valPara.setAlignment(Element.ALIGN_CENTER);
        Paragraph labelPara = new Paragraph(label, smallFont);
        labelPara.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(valPara);
        cell.addElement(labelPara);
        table.addCell(cell);
    }

    private static String safe(String v) {
        return v == null ? "" : v;
    }
}
